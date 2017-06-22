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

import com.neowit.apexscanner.antlr.ApexcodeParser
import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.nodes.{AstNode, IsTypeDefinition, Position}
import com.neowit.apexscanner.resolvers.NodeByLocationFinder
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime._

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
              val lastPosition =lastAstNode.range.end
              // check if given token is after lastPosition
              val predicate: Token => Boolean = t => lastPosition.isBefore(t.getLine, t.getCharPositionInLine + t.getText.length - 1)
              var i = caretToken.getTokenIndex
              val tokensBeforeCaretBuilder = List.newBuilder[Token]
              var keepGoing = true
              while (keepGoing) {
                  getPrevTokenOnChannel( i, tokens, predicate) match {
                    case Some(prevToken) => tokensBeforeCaretBuilder += prevToken
                    case None => keepGoing = false
                  }
                  i -= 1
              }
              // now we have list of tokens between last AST Node and caret
              val tokensBeforeCaret = tokensBeforeCaretBuilder.result().reverse
              // finally try to find definition of the last meaningful token before caret, aka "caret scope token"
              resolveExpressionBeforeCaret(tokensBeforeCaret, lastAstNode).map {
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
    def resolveExpressionBeforeCaret(tokensBeforeCaret: List[Token], lastAstNode: AstNode): Future[Option[IsTypeDefinition]] = {
        import collection.JavaConverters._
        val tokenSource = new ListTokenSource(tokensBeforeCaret.asJava)
        val tokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokenStream)
        val tree = parser.expression()
        // now visit resulting tree and try resolve caret context
        val resolver = new ContextResolver(project, lastAstNode)
        resolver.resolveContext(tree, lastAstNode)
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
    private def getPrevTokenOnChannel(startTokenIndex: Int, tokens: TokenStream, predicate: Token => Boolean): Option[Token] = {
        var i = startTokenIndex - 1
        while (i >=0) {
            val token = tokens.get(i)

            if (Token.DEFAULT_CHANNEL == token.getChannel && predicate(token)) {
                return Option(token)
            }
            i -= 1
        }
        None
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

    /*
    def resolveCaretScope(caret: CaretInFile, caretToken: Token, tokens: TokenStream): Future[Option[CaretScope]] = {
        // now we can step back and find what caret means
        if (isNextToDot(caretToken, tokens)) {
            val document = caret.document
            // caret is part Expr.Expr, so it must be member of token before .
            val caretTokenIndex = caretToken.getTokenIndex
            findPrecedingWordToken(caretTokenIndex, tokens) match {
                case Some(scopeToken) =>
                    findAstParentNode(document, scopeToken).map{
                        case Some(astScopeNode) =>
                            // this node is either around or is the node in front of caret
                            // resolve
                            findTypeDefinition(astScopeNode, scopeToken, Set.empty) match {
                                case Some(typeDef) =>
                                    //defByTokenIndex += token.getTokenIndex -> typeDef
                                    Option(CaretScope(Option(scopeToken), Option(typeDef), ClassMember))
                                case None =>
                                    None
                            }
                        case None => // looks like AST does not go this far
                            None
                    }
                case None =>
                    Future.successful(None)
            }
        } else {
            // TODO
            Future.successful(None)
        }
    }
    */
    // check if we are inside expression like
    // something.<caret>
    // something.som<caret>
    /*
    private def isNextToDot(caretToken: Token, tokens: TokenStream): Boolean = {
        getTokenBeforeCaret(caretToken.getTokenIndex, tokens) match {
          case Some(token) if "." == token.getText => true
          case _ => false
        }

    }

    private def getTokenBeforeCaret(caretTokenIndex: Int, tokens: TokenStream): Option[Token] = {
        val i = caretTokenIndex - 1
        if (i > 0) {
            if (ApexParserUtils.isWordToken(tokens.get(i))) {
                // this looks like:
                //  obj.othe<caret>
                // we should skip "othe" and move further left
                getPrevTokenOnChannel(i, tokens, t => true)
            } else {
                Option(tokens.get(i))
            }
        } else {
            None
        }

    }
    private def findPrecedingWordToken(caretTokenIndex: Int, tokens: TokenStream): Option[Token] = {
        getTokenBeforeCaret(caretTokenIndex, tokens) match {
          case Some(token) =>
              if (ApexParserUtils.isWordToken(token)) {
                  Option(token)
              } else {
                  getPrevTokenOnChannel(token.getTokenIndex, tokens, t => ApexParserUtils.isWordToken(t))
              }
          case _ =>
                None
        }
    }
    private def getNextTokenOnChannel(startTokenIndex: Int, tokens: TokenStream, predicate: Token => Boolean): Option[Token] = {
        var i = startTokenIndex + 1
        while (i < tokens.size()) {
            val token = tokens.get(i)

            if (Token.DEFAULT_CHANNEL == token.getChannel && predicate(token)) {
                return Option(token)
            }
            i += 1
        }
        None
    }
    */


    //private def findDefinitionGlobally(node: AstNode): Option[IsTypeDefinition] = {
    //    project.getByQualifiedName()
    //}
}
