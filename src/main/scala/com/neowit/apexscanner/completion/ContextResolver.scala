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

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.ApexcodeParser.{CreatorExpressionContext, PrimaryExprContext, TypeCastExprContext}
import com.neowit.apexscanner.ast.ASTBuilderVisitor
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeBySymbolTextFinder}
import org.antlr.v4.runtime.{ParserRuleContext, Token}

import scala.concurrent.Future

/**
  * Created by Andrey Gavrikov 
  */
class ContextResolver(project: Project, lastAstNode: AstNode) {
    private val _visitor = new ASTBuilderVisitor(project, fileOpt = None)

    def resolveContext(context: ParserRuleContext, lastAstNode: AstNode): Future[Option[IsTypeDefinition]] = {
        context match {
            case c: PrimaryExprContext => // single token
                Future.successful(resolvePrimary(c))
            case c: CreatorExpressionContext => // single token
                Future.successful(resolveCreator(c))
            case c: TypeCastExprContext => // single token
                Future.successful(resolveTypeCast(c))
            case _ => ???
        }
    }

    private def resolvePrimary(context: PrimaryExprContext): Option[IsTypeDefinition] = {
        val astNode = _visitor.visitPrimaryExpr(context)
        // look inside expression itself, it may be type cast, e.g. ((ObjectType)obj)
        val primary = context.primary()
        if (null != primary.Identifier()) {
            findTokenTypeDefinition(primary.Identifier().getSymbol, astNode).orElse{
                findTokenTypeDefinition(primary.Identifier().getSymbol, lastAstNode)
            }
        } else {
            astNode match {
                case n: FallThroughNode =>
                    // find first meaningful node
                    n.findChildInAst(_.nodeType != FallThroughNodeType) match {
                        case Some(n: TypeCastNode) => // single token
                            Option(n)
                        case x =>
                            println(x)
                            ???
                    }
                case n => ???
            }
        }
    }
    private def resolveCreator(context: CreatorExpressionContext): Option[IsTypeDefinition] = {
        _visitor.visitCreatorExpression(context) match {
            case creator: CreatorNode =>
                Option(creator)
            case _ => ???
        }
    }
    private def resolveTypeCast(context: TypeCastExprContext): Option[IsTypeDefinition] = {
        _visitor.visitTypeCastExpr(context) match {
            case n: TypeCastNode =>
                Option(n)
            case _ => ???
        }
    }

    /**
      * looks for definition of "tokenToResolve" when AST node is NOT available.
      * This method is a last resort - it is unreliable because does matching using token text
      * @param startNode search will start inside this AST node
      * @param tokenToResolve
      * @return
      */
    private def findTokenTypeDefinition(tokenToResolve: Token, startNode: AstNode): Option[IsTypeDefinition] = {
        val rootNode = startNode
        val finder = new NodeBySymbolTextFinder(tokenToResolve.getText)

        /**
          * @param currentStartNode search will continue inside this AST node
          * @param processedParents nodes already visited
          */
        def _findTypeDefinition(currentStartNode: AstNode, processedParents: Set[AstNode]): Option[IsTypeDefinition] = {
            finder.findInside(rootNode, processedParents) match {
                case Some(node) =>
                    findNodeTypeDefinition(node)
                case None =>
                    currentStartNode.getParentInAst(skipFallThroughNodes = true) match {
                        case Some(parent) =>
                            _findTypeDefinition(parent, processedParents + currentStartNode)
                        case None =>
                            None
                    }
            }

        }
        _findTypeDefinition(startNode, Set(startNode))
    }

    /**
      * looks for definition of given AST node when AST node is available
      * @param node AST node which we need to find a definition for
      * @return
      */
    private def findNodeTypeDefinition(node: AstNode): Option[IsTypeDefinition] = {
        node match {
            case _:IsTypeDefinition =>
                // given node is already a type definition, nothing to search
                Option(node.asInstanceOf[IsTypeDefinition])
            case _ =>
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

/*
private def findTypeDefinition(startNode: AstNode, tokenToResolve: Token, processedParents: Set[AstNode]): Option[IsTypeDefinition] = {
startNode match {
    case _:IsTypeDefinition =>
        Option(startNode.asInstanceOf[IsTypeDefinition])
    case _ =>
        //TODO - this is WRONG, it will not search for correct token
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
*/
}

