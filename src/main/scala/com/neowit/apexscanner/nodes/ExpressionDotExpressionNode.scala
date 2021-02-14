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

package com.neowit.apexscanner.nodes

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, DescendingDefinitionFinder}
import com.neowit.apexscanner.scanner.actions.ActionContext

/**
  * Created by Andrey Gavrikov 
  */
case class ExpressionDotExpressionNode(range: Range) extends AbstractExpression {
    private var _resolvedParts: Array[AstNode] = Array.empty[AstNode]
    private var _resolutionDone = false

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        val expressions = getExpressions
        // start with unknown head/base definition
        //val lastNode = resolveDefinitionFromHead(container = None, expressions)
        _resolvedParts = resolveDefinitionFromHead(expressions, actionContext).toArray
        _resolutionDone = true
        _resolvedParts.lastOption
    }

    /**
      * a chain of expression like this: aaa.bbb.ccc
      * will be represented as
      *     ExpressionDotExpressionNode(ExpressionDotExpressionNode(aaa, bbb), ccc)
      * we want those to become
      *     ExpressionDotExpressionNode(aaa, bbb, ccc)
      * so have to unpack nested ExpressionDotExpressionNode nodes
      * @return
      */
    private def getExpressions: Seq[AbstractExpression] = {
        def unpack(n: AbstractExpression): Seq[AbstractExpression] = {
            n match {
                case chain: ExpressionDotExpressionNode =>
                    chain.getExpressions
                case one => Seq(one)
            }
        }

        val expressions = findChildrenInAst {
            case _: AbstractExpression => true
            case _ => false
        }.map(_.asInstanceOf[AbstractExpression])

        val resUnordered = expressions.foldLeft(Seq.empty[AbstractExpression])(_ ++ unpack(_))
        // due to nested FallThrough expressions nodes obtained above may be in the wrong logical order,
        // i.e. not in the order they appear in the document
        // make sure that expressions are returned in the same order as they appear in the document
        val res = resUnordered.sortBy(_.range.start)
        res
    }
    /**
      * check if given node is part of this expression and
      * find its position in the list of expressions comprising this expression
      * @param astNode expression to find
      * @return 0 or greater if given node is part of current expression, -1 otherwise
      */
    private def getExpressionIndex(astNode: AstNode): Int = {
        getExpressions.indexWhere(n => n.nodeType == astNode.nodeType && n.range == astNode.range)
    }


    /**
      * get definition of given node.
      * Node must be part of this expression
      * @param part node which is part of current expression
      * @return
      */
    def getResolvedPartDefinition(part: AstNode, actionContext: ActionContext): Option[AstNode] = {
        if (!_resolutionDone) {
            resolveDefinition(actionContext)
        }
        val index = getExpressionIndex(part)
        if (index >=0 && index < _resolvedParts.length) {
            Option(_resolvedParts(index))
        } else {
            None
        }
    }


    /**
      * find definition of head element and then find definitions of the rest of the expression
      * as children of previous element in expression chain
      * e.g. some.other.elem
      * some = head
      * other = child of "some"
      * elem = child of "other"
      * @param expressions chain of expressions to be resolved
      * @return
      */
    private def resolveDefinitionFromHead(expressions: Seq[AbstractExpression], actionContext: ActionContext): Seq[AstNode] = {
        expressions match {
            case lst if lst.nonEmpty=>
                val head = lst.head
                val tail = lst.tail
                head match {
                    case n:IsTypeDefinition => resolveTailDefinitions(n, tail, actionContext)
                    case n: ThisExpressionNode =>
                        n.resolveDefinition(actionContext) match {
                            case Some(_def: IsTypeDefinition) =>
                                resolveTailDefinitions(_def, tail, actionContext)
                            case _ =>
                                Seq.empty
                        }
                    case n: SuperExpressionNode =>
                        n.resolveDefinition(actionContext) match {
                            case Some(_def: IsTypeDefinition) =>
                                resolveTailDefinitions(_def, tail, actionContext)
                            case _ =>
                                Seq.empty
                        }

                    case n: ArrayIndexExpressionNode =>
                        n.resolveDefinition(actionContext) match {
                            case Some(_def: IsTypeDefinition) =>
                                resolveTailDefinitions(_def, tail, actionContext)
                            case _ =>
                                Seq.empty
                        }
                    case n: LiteralLike =>
                        n.resolveDefinition(actionContext) match {
                            case Some(_def: IsTypeDefinition) =>
                                resolveTailDefinitions(_def, tail, actionContext)
                            case _ =>
                                Seq.empty
                        }
                    case n =>
                        //head of expression has not been resolved yet, find its definition going UPwards
                        val finder = new AscendingDefinitionFinder(actionContext)
                        finder.findDefinition(n, n).headOption match {
                            case Some(_def: IsTypeDefinition) =>
                                resolveTailDefinitions(_def, tail, actionContext)
                            case _ =>
                                n match {
                                    case identifier @ IdentifierNode(_, _) =>
                                        identifier.resolveDefinitionByQualifiedName() match {
                                            case Some(_def: IsTypeDefinition) =>
                                                resolveTailDefinitions(_def, tail, actionContext)
                                            case _ =>
                                                Seq.empty
                                        }
                                    case _ => Seq.empty

                                }
                        }
                }
            case _ => Seq.empty

        }
    }

    /**
      *
      * @param container resolved definition of Head expression
      * @param expressionsToResolve all other expressions, except head one
      * @return Head + tail expression definitions
      */
    private def resolveTailDefinitions(container: AstNode with IsTypeDefinition, expressionsToResolve: Seq[AbstractExpression],
                                       actionContext: ActionContext): Seq[AstNode] = {

        def _resolveTailDefinitions(project: Project, container: AstNode with IsTypeDefinition, expressionsToResolve: Seq[AbstractExpression],
                                    resolvedExpressions: Seq[AstNode]): Seq[AstNode] = {

            if (expressionsToResolve.isEmpty) {
                resolvedExpressions
            } else {
                val head = expressionsToResolve.head
                val tail = expressionsToResolve.tail
                val finder = new DescendingDefinitionFinder(project, actionContext)
                finder.findDefinition(head, container).headOption match {
                    case Some(_def: IsTypeDefinition) =>
                        _resolveTailDefinitions(project, _def, tail, resolvedExpressions ++ Seq(_def))
                    case _ =>
                        Seq.empty
                }
            }
        }
        getProject match {
            case Some(project) =>
                _resolveTailDefinitions(project, container, expressionsToResolve, Seq(container))
            case None =>
                Seq.empty
        }
    }
}
