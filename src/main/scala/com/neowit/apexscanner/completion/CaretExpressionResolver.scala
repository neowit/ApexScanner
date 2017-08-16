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
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.{NearestPrecedingNodeFinder, NodeByLocationFinder}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.misc.Pair

import scala.concurrent.{ExecutionContext, Future}

case class CaretScope(scopeNode: AstNode, typeDefinition: Option[IsTypeDefinition])
/**
  * Created by Andrey Gavrikov 
  */
class CaretExpressionResolver(project: Project)(implicit ex: ExecutionContext)  extends LazyLogging {


    def resolveCaretScope(caret: CaretInDocument, caretToken: Token, tokens: TokenStream): Future[Option[CaretScope]] = {

        val document = caret.document
        findAstScopeNode(document, caretToken, tokens).flatMap {
          case Some(astScopeNode) =>
              // find list of tokens before caret which are not in AST
              val lastAstNode = getNearestPrecedingAstNode(caret, astScopeNode)
              val lastPosition =
                  if (lastAstNode != astScopeNode) {
                      lastAstNode.range.end
                  } else {
                      // when last node is the same as scope node that means we have unresolved expression with
                      // caret which starts at the top of the current scope, e.g. at the beginning of method body
                      // in other words there are no valid expressions (AST nodes) between care expression and parent scope
                      astScopeNode.range.start
                  }
              // check if given token is after lastPosition
              val predicate: Token => Boolean = {t =>
                  lastPosition.isBefore(t.getLine, t.getCharPositionInLine + t.getText.length - 1)
              }
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
              resolveExpressionBeforeCaret(caret, tokensBeforeCaret, astScopeNode, lastAstNode).map {
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
    def resolveExpressionBeforeCaret(caret: Caret, tokensBeforeCaret: List[Token], astScopeNode: AstNode, lastAstNode: AstNode): Future[Option[IsTypeDefinition]] = {

        import collection.JavaConverters._
        val tokenSource = new ListTokenSource(tokensBeforeCaret.asJava)
        val tokenStream = new CommonTokenStream(tokenSource)
        //val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //val tree = parser.expression()
        tokensBeforeCaret match {
            case _tokens if _tokens.nonEmpty && _tokens.exists(ApexParserUtils.isWordToken) =>
                //TODO - is this ever being used (since adding FIXER_TOKEN to source document) ?
                findLongestTree(caret, tokensBeforeCaret) match {
                    case Some(tree) =>
                        // now visit resulting tree and try resolve caret context
                        val resolver = new ContextResolver(project, astScopeNode, lastAstNode)
                        resolver.resolveContext(tree, tokenStream)
                    case None =>
                        Future.successful(None)
                }
            case _ =>
                // looks like we have a valid AST up until caret
                // assume lastAstNode is a definition of caret
                lastAstNode match {
                    case n: FallThroughNode =>
                        lastAstNode.getParentInAst(true) match {
                            case Some(_lastAstNode) => resolveExpressionBeforeCaret(caret, tokensBeforeCaret, astScopeNode, _lastAstNode)
                            case None =>
                                Future.successful(None)
                        }
                    case n: IsTypeDefinition => Future.successful(Option(n))
                    case n: HasTypeDefinition =>
                        n.resolveDefinition() match {
                            case defOpt @ Some(_: IsTypeDefinition) =>
                                Future.successful(defOpt.map(_.asInstanceOf[IsTypeDefinition]))
                            case x =>
                                // TODO
                                println(x)
                                Future.successful(None)
                        }
                    case n: ExpressionListNode =>
                        // this may be something like new Account(Field1 = 'a', <CARET>)
                        // check if parent of expression list is CreatorNode
                        n.getParentInAst(skipFallThroughNodes = true) match {
                            case Some(_node: CreatorNode) =>
                                Future.successful(Option(_node))
                            case _ =>
                                println(n)
                                ??? //should we give up ?
                        }
                    case n =>
                        println(n)
                        ??? //should we give up ?
                }
        }
    }

    /**
      * try to guess what is missing in caret position in order to find longest tree
      * @param tokensBeforeCaret tokens in front of caret (on default channel)
      * @return
      */
    private def findLongestTree(caret: Caret, tokensBeforeCaret: List[Token]): Option[ParserRuleContext] = {
        if (tokensBeforeCaret.nonEmpty) {
            val tokensArray = tokensBeforeCaret.toArray
            var lastTokenBeforeCaret = tokensArray(tokensArray.length - 1)
            val exprTokens =
                if (ApexParserUtils.isWordToken(lastTokenBeforeCaret) && tokensArray.length > 1 && caret.isInside(lastTokenBeforeCaret)) {
                    //have a situation like this: aaa.bbb<caret>
                    // caret is inside of last token, so replace last token with caret
                    lastTokenBeforeCaret = tokensArray(tokensArray.length - 2)
                    val caretToken = createToken(ApexcodeLexer.FIXER_TOKEN, "FIXER_TOKEN", lastTokenBeforeCaret)
                    tokensBeforeCaret.dropRight(1) :+ caretToken
                } else {
                    // append caret after last token
                    val caretToken = createToken(ApexcodeLexer.FIXER_TOKEN, "FIXER_TOKEN", lastTokenBeforeCaret)
                    tokensBeforeCaret :+ caretToken
                }
            val treeOpt = parse(exprTokens)
            treeOpt
        } else {
            None
        }

    }

    private def parseAsExpression(tokens: java.util.List[Token]): ParserRuleContext = {
        val tokenSource = new ListTokenSource(tokens)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.expression()
    }

    private def parseAsClassVariable(tokens: java.util.List[Token]): ParserRuleContext = {
        val tokenSource = new ListTokenSource(tokens)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.classVariable()
    }

    private def parseAsCodeBlock(tokens: java.util.List[Token]): ParserRuleContext = {
        val tokenSource = new ListTokenSource(tokens)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.codeBlock()
    }

    private def parseAsClassBody(tokens: java.util.List[Token]): ParserRuleContext = {
        val tokenSource = new ListTokenSource(tokens)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.classBody()
    }

    private def parse(tokens: List[Token]): Option[ParserRuleContext] = {
        import collection.JavaConverters._
        val methods = Seq[java.util.List[Token] => ParserRuleContext](
            parseAsExpression,
            parseAsClassVariable,
            parseAsCodeBlock,
            parseAsClassBody
        )

        val tokensList = tokens.asJava
        val tokensLength = tokens.length

        for (method <- methods) {
            val tree = method(tokensList)
            // this condition may need tweaking
            // check if last token in resulting tree is near caret (i.e. last expression type/method is probably right guess)
            if (tree.stop.getTokenIndex + 1 >= tokensLength) {
                return Option(tree)
            }
        }
        None
    }

    private def createToken(tokenType: Int, text: String, prevToken: Token): Token = {
        val start = prevToken.getStopIndex + 1
        val stop = start + text.length

        val source = new Pair[TokenSource, CharStream](prevToken.getTokenSource, prevToken.getInputStream)
        val factory = CommonTokenFactory.DEFAULT
        val token = factory.create(source, tokenType, text, prevToken.getChannel, start, stop, prevToken.getLine, prevToken.getCharPositionInLine + prevToken.getText.length)
        token
    }
    /**
      * find AST node which is closest to expression where caret is positioned
      */
    private def getNearestPrecedingAstNode(caret: CaretInDocument, astScopeNode: AstNode): AstNode = {
        if (astScopeNode.children.isEmpty) {
            astScopeNode
        } else {
            val finder = new NearestPrecedingNodeFinder(caret.position)
            finder.findInside(astScopeNode).getOrElse(astScopeNode)
        }
    }

    private def findAstScopeNode(document: VirtualDocument, token: Token, tokens: TokenStream): Future[Option[AstNode]] = {

        // to make sure we do not work with stale version of current document use: forceRebuild = true
        project.getAst(document, forceRebuild = true).map{
            case Some(_res) =>
                val position = Position(token.getLine, token.getCharPositionInLine)
                val locationFinder = new NodeByLocationFinder(position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNodeOpt @ Some(node) if node.isScope =>
                        //finalNode is a scope node covering Caret position
                        finalNodeOpt
                    case Some(finalNode) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNode.findParentInAst(_.isScope)
                    case None =>
                        None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + document.file)
                None
        }
    }

    /*
    /**
      *
      * @param token a token to look for in the document
      * @return AST Node inside which given token resides
      */
    def findAstParentNodeOnLimitedAst(document: VirtualDocument, token: Token, tokens: TokenStream): Future[Option[AstNode]] = {

        buildAstTillCaret(token, document, tokens) match {
            case Some(rootNode) =>
                val position = Position(token.getLine, token.getCharPositionInLine)
                val locationFinder = new NodeByLocationFinder(position)
                locationFinder.findInside(rootNode) match {
                    case finalNodeOpt @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        Future.successful(finalNodeOpt)
                    case None =>
                        Future.successful(None)
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + document.file)
                Future.successful(None)
        }
    }
    /**
      * build ast using *only* tokens till caret but not further
      * @param caretToken previously found Token in caret position
      * @param document
      * @param fullTokenStream
      * @return
      */
    private def buildAstTillCaret(caretToken: Token, document: VirtualDocument, fullTokenStream: TokenStream): Option[AstNode] = {
        import collection.JavaConverters._
        val tokensBeforeCaret = takeTokensWhile(fullTokenStream, t => t.getTokenIndex < caretToken.getTokenIndex)
        val caretFixerToken = createToken(ApexcodeLexer.FIXER_TOKEN, "", tokensBeforeCaret.last)
        val tokenSource = new ListTokenSource((tokensBeforeCaret :+ caretFixerToken).asJava)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        parser.setErrorHandler(new DefaultErrorStrategy)
        parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        val tree = parser.compilationUnit()

        val visitor = new ASTBuilderVisitor(project, Option(document))
        val compileUnit = visitor.visit(tree)
        Option(compileUnit)
    }

    private def takeTokensWhile(tokenStream: TokenStream, predicate: Token => Boolean): List[Token] = {
        if (tokenStream.size() < 1) {
            List.empty[Token]
        } else {
            val resultBuilder = List.newBuilder[Token]
            tokenStream.seek(0)
            val maxIndex = tokenStream.size() - 1
            var index = 0
            var token: Token = tokenStream.get(index)
            while (index < maxIndex && predicate(token)) {
                resultBuilder += token
                index += 1
                token = tokenStream.get(index)
            }
            resultBuilder.result()
        }
    }*/
}

