/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 *  This file is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.neowit.apexscanner.completion

import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeLexer, ApexcodeParser}
import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.nodes.{AstNode, IsTypeDefinition, Position}
import com.neowit.apexscanner.resolvers.NodeByLocationFinder
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.misc.Pair

import scala.concurrent.{ExecutionContext, Future}

//sealed trait CaretContext
//case object ClassMember extends CaretContext

case class CaretScope(scopeNode: AstNode, typeDefinition: Option[IsTypeDefinition])
/**
  * Created by Andrey Gavrikov 
  */
class CaretExpressionResolver(project: Project)(implicit ex: ExecutionContext)  extends LazyLogging {


    def resolveCaretScope(caret: CaretInFile, caretToken: Token, tokens: TokenStream): Future[Option[CaretScope]] = {

        val document = caret.document
        findAstParentNode(document, caretToken).flatMap {
          case Some(astScopeNode) =>
              // find list of tokens before caret which are not in AST
              val lastAstNode = getNearestPrecedingAstNode(caret, astScopeNode)
              val lastPosition = lastAstNode.range.end
              // check if given token is after lastPosition
              val predicate: Token => Boolean = t => lastPosition.isBefore(t.getLine, t.getCharPositionInLine + t.getText.length - 1)
              var i = caretToken.getTokenIndex
              val tokensBeforeCaretBuilder = List.newBuilder[Token]
              var keepGoing = true
              while (keepGoing) {
                  ApexParserUtils.getPrevTokenOnChannel( i, tokens, predicate) match {
                    case Some(prevToken) => tokensBeforeCaretBuilder += prevToken
                    case None => keepGoing = false
                  }
                  i -= 1
              }
              // now we have list of tokens between last AST Node and caret
              val tokensBeforeCaret = tokensBeforeCaretBuilder.result().reverse
              // finally try to find definition of the last meaningful token before caret, aka "caret scope token"
              resolveExpressionBeforeCaret(tokensBeforeCaret, astScopeNode, lastAstNode).map {
                case Some(caretScopeTypeDef) =>
                    Option(CaretScope(astScopeNode, Option(caretScopeTypeDef)))
                case None =>
                    Option(CaretScope(astScopeNode, None))
              }
          case None =>
              Future.successful(None)
        }
    }

    /**
      * @param tokensBeforeCaret unresolved tokens in front of caret
      * @return
      */
    def resolveExpressionBeforeCaret(tokensBeforeCaret: List[Token], astScopeNode: AstNode, lastAstNode: AstNode): Future[Option[IsTypeDefinition]] = {

        import collection.JavaConverters._
        val tokenSource = new ListTokenSource(tokensBeforeCaret.asJava)
        val tokenStream = new CommonTokenStream(tokenSource)
        //val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //val tree = parser.expression()

        findLongestTree(tokensBeforeCaret) match {
            case Some(tree) =>
                // now visit resulting tree and try resolve caret context
                val resolver = new ContextResolver(project, astScopeNode, lastAstNode)
                resolver.resolveContext(tree, tokenStream)
            case None =>
                Future.successful(None)
        }
    }

    /**
      * try to guess what is missing in caret position in order to find longest tree
      * @param tokensBeforeCaret tokens in front of caret (on default channel)
      * @return
      */
    private def findLongestTree(tokensBeforeCaret: List[Token]): Option[ParserRuleContext] = {
        import collection.JavaConverters._
        def parseAsExpression(tokens: List[Token]): ParserRuleContext = {
            val tokenSource = new ListTokenSource(tokens.asJava)
            val tokenStream = new CommonTokenStream(tokenSource)
            val parser = new ApexcodeParser(tokenStream)
            //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
            parser.expression()
        }
        def parseAsClassVariable(tokens: List[Token]): ParserRuleContext = {
            val tokenSource = new ListTokenSource(tokens.asJava)
            val tokenStream = new CommonTokenStream(tokenSource)
            val parser = new ApexcodeParser(tokenStream)
            //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
            parser.classVariable()
        }
        def parse(tokens: List[Token]): Option[ParserRuleContext] = {
            val methods = Seq[List[Token] => ParserRuleContext](
                parseAsExpression,
                parseAsClassVariable
            )
            val tokensLength = tokens.length
            for (method <- methods) {
                val tree = method(tokens)
                // this condition may need tweaking
                // check if last token in resulting tree is near caret (i.e. last expression type/method is probably right guess)
                if (tree.stop.getTokenIndex + 1 >= tokensLength) {
                    return Option(tree)
                }
            }
            None
        }
        if (tokensBeforeCaret.nonEmpty) {
            val lastTokenBeforeCaret = tokensBeforeCaret.reverse.head
            val caretToken = createToken(ApexcodeLexer.FIXER_TOKEN, "FIXER_TOKEN", lastTokenBeforeCaret)
            val treeOpt = parse(tokensBeforeCaret :+ caretToken)
            treeOpt
            /*
            if (tree.stop.getTokenIndex < caretToken.getTokenIndex) {
                var prevTokens = tokensBeforeCaret :+ caretToken
                var prevToken = caretToken
                var i = 0
                val MAX_I = 3
                while (i < MAX_I && tree.stop.getTokenIndex < caretToken.getTokenIndex) {
                    val fixerToken = createToken(ApexcodeLexer.FIXER_TOKEN, "FIXER_TOKEN", prevToken)
                    val allTokens = prevTokens :+ fixerToken
                    tree = parse(allTokens)
                    i += 1
                    prevTokens = allTokens
                    prevToken = fixerToken
                }
            }
            Option(tree)
            */
        } else {
            None
        }

    }

    private def createToken(tokenType: Int, text: String, prevToken: Token): Token = {
        val start = prevToken.getStopIndex + 1
        val stop = start + text.length

        val source = new Pair[TokenSource, CharStream](prevToken.getTokenSource, prevToken.getInputStream)
        val token = new CommonToken(source, tokenType, prevToken.getChannel, start, stop)
        token
    }
    /**
      * find AST node which is closest to expression where caret is positioned
      */
    private def getNearestPrecedingAstNode(caret: CaretInFile, astScopeNode: AstNode): AstNode = {
        if (astScopeNode.children.isEmpty) {
            astScopeNode
        } else {
            val nodes = astScopeNode.children.toArray
            var i = 0
            var node = nodes(i)
            val length = nodes.length
            while (i < length && caret.isBefore(node.range)) {
                i += 1
                node = nodes(i)
            }
            node
        }
    }

    /**
      *
      * @param token a token to look for in the document
      * @return AST Node inside which given token resides
      */
    private def findAstParentNode(document: VirtualDocument, token: Token): Future[Option[AstNode]] = {

        project.getAst(document.file).map{
            case Some(_res) =>
                val position = Position(token.getLine, token.getCharPositionInLine)
                val locationFinder = new NodeByLocationFinder(position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNodeOpt @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNodeOpt
                    case None =>
                        None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + document.file)
                None
        }
    }

}
