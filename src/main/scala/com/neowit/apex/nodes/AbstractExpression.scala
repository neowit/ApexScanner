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
import com.neowit.apex.ast.QualifiedName
import com.neowit.apex.resolvers.{AscendingDefinitionFinder, DescendingDefinitionFinder}


/**
  * Created by Andrey Gavrikov 
  */
trait AbstractExpression extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = ExpressionNodeType
}

case class ExpressionDotExpressionNode(range: Range) extends AbstractExpression {
    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        val expressions = getChildren[AbstractExpression](ExpressionNodeType)
        // start with unknown head/base definition
        resolveDefinitionFromHead(container = None, expressions)
    }

    /**
      * traverse expression from left to right
      * e.g.
      * some.other.methodCall(methodCall2(param1, param2.other))
      * @param container already resolved parent container, e.g. "some" in the above example
      * @param expressions expressions yet to be resolved, e.g. "other.methodCall(methodCall2(param1, param2.other))" in the above example
      * @return
      */
    private def resolveDefinitionFromHead(container: Option[AstNode with IsTypeDefinition], expressions: Seq[AbstractExpression]): Option[AstNode] = {
        if (expressions.isEmpty) {
            container
        } else {
            expressions match {
                case lst if lst.nonEmpty=>
                    val head = lst.head
                    val tail = lst.drop(1)
                    head match {
                        case n:IsTypeDefinition =>
                            if (tail.nonEmpty) {
                                resolveDefinitionFromHead(Option(n), tail)
                            } else {
                                Option(n)
                            }
                        case n =>
                            container match {
                                case Some(_container) =>
                                    // from known parent descend through children
                                    val finder = new DescendingDefinitionFinder()
                                    finder.findDefinition(n, _container)
                                        .headOption
                                        .flatMap{
                                            case _definition: IsTypeDefinition => resolveDefinitionFromHead(Option(_definition), tail)
                                        }
                                case None =>
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
}


/**
  *
  * produced by expression like:
  *     List<Integer>.class;
  * result is
  *     List<Integer>
  */
case class ApexTypeExpressionNode(dataType: DataTypeNode,range: Range) extends AbstractExpression with HasTypeDefinition with IsTypeDefinition {
    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

    override def getValueType: Option[ValueType] = Option(dataType.getDataType)

    override def qualifiedName: Option[QualifiedName] = Option(dataType.getDataType.qualifiedName)
}

case class ThisExpressionNode(range: Range) extends AbstractExpression with HasTypeDefinition {
    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        Option(getClassOrInterfaceNode)
    }

    def getClassOrInterfaceNode: ClassLike = {
        findParent(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }
}

case class SuperExpressionNode(range: Range) extends AbstractExpression with HasTypeDefinition {
    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        getClassOrInterfaceNode.getSuperClassOrInterface
    }

    def getClassOrInterfaceNode: ClassLike = {
        findParent(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }
}
