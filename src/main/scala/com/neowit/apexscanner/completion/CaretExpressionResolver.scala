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
import com.neowit.apexscanner.nodes.{AstNode, FallThroughNode, IsTypeDefinition, Position}
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeByLocationFinder}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{CommonTokenStream, Token}

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext, Future}

sealed trait CaretSituation
case object ClassMember extends CaretSituation

case class CaretScope(scopeToken: Option[Token], typeDefinition: Option[IsTypeDefinition], situation: CaretSituation)
/**
  * Created by Andrey Gavrikov 
  */
class CaretExpressionResolver(project: Project)(implicit ex: ExecutionContext)  extends LazyLogging {

    def resolveCaretScope(caret: CaretInFile, caretReachedException: CaretReachedException, tokens: CommonTokenStream): Future[Option[CaretScope]] = {
        val defByTokenIndex = new mutable.HashMap[Int, IsTypeDefinition]()
        // get parent of final context
        // then follow token stream from start till caret resolving all items
        val start = caretReachedException.finalContext.parent.getSourceInterval.a
        if (start > 0) {
            tokens.seek(start)
            var i = 1
            var token = tokens.LT(i)
            val document = caret.document
            var lastWordTokenIndex = -1
            val tokensToResolve = List.newBuilder[Token]
            while (caret.isAfter(token)) {
                if (ApexParserUtils.isWordToken(token)) {
                    lastWordTokenIndex = token.getTokenIndex
                    //tokensToResolve += token
                    // resolve
                    val resFuture =
                        findAstNode(document, token).map{
                            case Some(astNode: FallThroughNode) =>
                            // do nothing
                            case Some(astNode) =>

                                findTypeDefinition(astNode) match {
                                    case Some(typeDef) =>
                                        defByTokenIndex += token.getTokenIndex -> typeDef
                                    case None =>
                                }
                            case None => // looks like AST does not go this far
                                ???
                        }
                    //Await.result(resFuture, concurrent.duration.Duration.Inf)
                }
                i += 1
                token = tokens.LT(i)

            }
            // at this point we have cached definitions of tokens right before the caret
            // now we can step back and find what caret means
            // TODO
            if ("." == tokens.LT(i-1).getText) {
                // caret is part Expr.Expr, so it must be member of token before .
                defByTokenIndex.get(lastWordTokenIndex) match {
                    case Some(tokenDef) =>
                        Option(CaretScope(Option(tokens.get(lastWordTokenIndex)), Option(tokenDef), ClassMember))
                    case None =>
                        None
                }
            } else {
                ???
            }
        } else {
            Future.successful(None)
        }
    }

    private def findAstNode(document: VirtualDocument, token: Token): Future[Option[AstNode]] = {

        project.getAst(document.file).map{
            case Some(_res) =>
                val position = Position(token.getLine, token.getCharPositionInLine)
                val locationFinder = new NodeByLocationFinder(position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNode @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNode
                    case None => None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + document.file)
                None
        }
    }

    private def findTypeDefinition(node: AstNode): Option[IsTypeDefinition] = {

        findDefinitionAscending(node) match {
          case typeDefOpt @ Some(typeDef) =>
              typeDefOpt
          case None =>
              // definition not found in current file
              // try other places
                ???
        }
    }

    private def findDefinitionAscending(node: AstNode): Option[IsTypeDefinition] = {
        val defFinder = new AscendingDefinitionFinder()
        val definitions = defFinder.findDefinition(node, node)
        definitions match {
            case Nil =>
                // definition not found in current file
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
