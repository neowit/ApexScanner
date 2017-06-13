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

import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.antlr.ApexParserUtils
import com.neowit.apexscanner.nodes.{AstNode, IsTypeDefinition, Position}
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeByLocationFinder, NodeBySymbolTextFinder}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{Token, TokenStream}

import scala.concurrent.{ExecutionContext, Future}

sealed trait CaretContext
case object ClassMember extends CaretContext

case class CaretScope(scopeToken: Option[Token], typeDefinition: Option[IsTypeDefinition], situation: CaretContext)
/**
  * Created by Andrey Gavrikov 
  */
class CaretExpressionResolver(project: Project)(implicit ex: ExecutionContext)  extends LazyLogging {

    def resolveCaretScope(caret: CaretInFile, caretToken: Token, tokens: TokenStream): Future[Option[CaretScope]] = {
        // now we can step back and find what caret means
        if (isNextToDot(caretToken, tokens)) {
            val document = caret.document
            // caret is part Expr.Expr, so it must be member of token before .
            val caretTokenIndex = caretToken.getTokenIndex
            findPrecedingWordToken(caretTokenIndex, tokens) match {
                case Some(scopeToken) =>
                    findAstScopeNode(document, scopeToken).map{
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
    // check if we are inside expression like
    // something.<caret>
    // something.som<caret>
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

    private def findTypeDefinition(startNode: AstNode, tokenToResolve: Token, processedParents: Set[AstNode]): Option[IsTypeDefinition] = {
        startNode match {
            case _:IsTypeDefinition =>
                Option(startNode.asInstanceOf[IsTypeDefinition])
            case _ =>
                findDefinitionAscending(startNode) match {
                    case typeDefOpt @ Some(typeDef) =>
                        typeDefOpt
                    case None =>
                        // looks like token is not part of available AST
                        // fall back to search by token text
                        val rootNode = startNode
                        val finder = new NodeBySymbolTextFinder(tokenToResolve.getText)
                        finder.findInside(rootNode, processedParents) match {
                            case Some(node) =>
                                findTypeDefinition(node, tokenToResolve, processedParents + startNode)
                            case None =>
                                startNode.getParentInAst(skipFallThroughNodes = true) match {
                                    case Some(parent) =>
                                        findTypeDefinition(parent, tokenToResolve, processedParents + startNode)
                                    case None =>
                                        None
                                }
                        }
                }
        }

    }

    private def findAstScopeNode(document: VirtualDocument, token: Token): Future[Option[AstNode]] = {

        project.getAst(document.file).map{
            case Some(_res) =>
                val position = Position(token.getLine, token.getCharPositionInLine)
                val locationFinder = new NodeByLocationFinder(position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNode @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNode
                    case None =>
                        None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + document.file)
                None
        }
    }

    private def findDefinitionAscending(node: AstNode): Option[IsTypeDefinition] = {
        val defFinder = new AscendingDefinitionFinder()
        val definitions = defFinder.findDefinition(node, node)
        definitions match {
            case Nil =>
                // definition not found in current file
                //TODO
                None
            case head :: Nil =>
                // exactly 1 node found, looks promising
                head match {
                    case n:IsTypeDefinition =>
                        Option(n)
                    case _ => None
                }
            case nodes =>
                nodes.find(_.isInstanceOf[IsTypeDefinition]).map(_.asInstanceOf[IsTypeDefinition])
        }
    }
}
