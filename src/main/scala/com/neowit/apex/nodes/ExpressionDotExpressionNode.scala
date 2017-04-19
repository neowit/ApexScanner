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

package com.neowit.apex.nodes

import com.neowit.apex.resolvers.{AscendingDefinitionFinder, DescendingDefinitionFinder}

/**
  * Created by Andrey Gavrikov 
  */
case class ExpressionDotExpressionNode(range: Range) extends AbstractExpression {
    private var _resolvedParts: Array[AstNode] = Array.empty[AstNode]
    private var _resolutionDone = false

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        val expressions = getExpressions
        // start with unknown head/base definition
        //val lastNode = resolveDefinitionFromHead(container = None, expressions)
        val resolvedDefinitions = resolveDefinitionFromHead(expressions)
        _resolutionDone = true
        resolvedDefinitions.lastOption
    }

    private def getExpressions: Seq[AbstractExpression] = {
        val expressions = findChildrenInAst{
            case _:AbstractExpression => true
            case _ => false
        }.map(_.asInstanceOf[AbstractExpression])
        expressions
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
    def getResolvedPartDefinition(part: AstNode): Option[AstNode] = {
        if (!_resolutionDone) {
            resolveDefinition()
        }
        val index = getExpressionIndex(part)
        if (index >=0 && index < _resolvedParts.length) {
            Option(_resolvedParts(index))
        } else {
            None
        }
    }

    /**
      * traverse expression from left to right
      * e.g.
      * some.other.methodCall(methodCall2(param1, param2.other))
      * @param container already resolved parent container, e.g. "some" in the above example
      * @param expressions expressions yet to be resolved, e.g. "other.methodCall(methodCall2(param1, param2.other))" in the above example
      * @return
      */
    /*
    private def resolveDefinitionFromHead(container: Option[AstNode], expressions: Seq[AbstractExpression]): Option[AstNode] = {
        container match {
          case Some(_resolvedExprNode) =>
              // add resolved part of expression to map
              _resolvedParts += _resolvedExprNode
          case _ =>
        }
        if (expressions.isEmpty) {
            container
        } else {
            expressions match {
                case lst if lst.nonEmpty=>
                    val head = lst.head
                    val tail = lst.drop(1)
                    head match {
                        case n:IsTypeDefinition => resolveDefinitionFromHead(Option(n), tail)
                        case n: ThisExpressionNode => resolveDefinitionFromHead(n.resolveDefinition(), tail)
                        case n: SuperExpressionNode => resolveDefinitionFromHead(n.resolveDefinition(), tail)
                        case n =>
                            container match {
                                case Some(_container: IsTypeDefinition) =>
                                    // from known parent descend through children
                                    val finder = new DescendingDefinitionFinder()
                                    finder.findDefinition(n, _container)
                                        .headOption
                                        .flatMap{
                                            case _definition: IsTypeDefinition => resolveDefinitionFromHead(Option(_definition), tail)
                                        }
                                case _ =>
                                    //head of expression has not been resolved yet, find its definition going UPwards
                                    val finder = new AscendingDefinitionFinder()
                                    finder.findDefinition(n, n)
                                        .headOption
                                        .flatMap{
                                            case _definition: IsTypeDefinition => resolveDefinitionFromHead(Option(_definition), tail)
                                        }

                            }

                    }
            }
        }
    }
    */

    /**
      * find definition of head element and then find definitions of the rest of the expression
      * as children of previous element in expression chain
      * e.g. some.other.elem
      * some = head
      * other = child of "some"
      * elem = child of "other"
      * @param expressions
      * @return
      */
    private def resolveDefinitionFromHead(expressions: Seq[AbstractExpression]): Seq[AstNode] = {
        if (expressions.isEmpty) {
            Seq.empty
        } else {
            expressions match {
                case lst if lst.nonEmpty=>
                    val head = lst.head
                    val tail = lst.drop(1)
                    head match {
                        case n:IsTypeDefinition => resolveTailDefinitions(Option(n), tail)
                        case n: ThisExpressionNode => resolveTailDefinitions(n.resolveDefinition(), tail)
                        case n: SuperExpressionNode => resolveTailDefinitions(n.resolveDefinition(), tail)
                        case n =>
                            //head of expression has not been resolved yet, find its definition going UPwards
                            val finder = new AscendingDefinitionFinder()
                            val headDefinition = finder.findDefinition(n, n)
                            resolveTailDefinitions(headDefinition.headOption, tail)
                    }
            }
        }
    }

    /**
      *
      * @param headOpt resolved definition of Head expression
      * @param tailExpressions all other expressions, except head one
      * @return Head + tail expression definitions
      */
    private def resolveTailDefinitions(headOpt: Option[AstNode], tailExpressions: Seq[AbstractExpression]): Seq[AstNode] = {
        headOpt match {
          case Some(head: ClassLike) =>
              val finder = new DescendingDefinitionFinder()

          case _ =>
              // head could not be resolved, no point to move further through the rest of the expression chain
              Seq.empty
        }
        ???
    }
}
