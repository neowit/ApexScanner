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
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.matchers.MethodMatcher
import com.neowit.apexscanner.resolvers.AscendingDefinitionFinder
import com.neowit.apexscanner.scanner.actions.ActionContext
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov
  */
object MethodCallNode {
    def apply(methodName: String, range: Range): MethodCallNode = {
        MethodCallNode(QualifiedName(methodName.split("\\.")), range)
    }
}

case class MethodCallNode(methodName: QualifiedName, range: Range) extends AbstractExpression with HasQualifiedName with LazyLogging {
    override def nodeType: AstNodeType = MethodCallNodeType

    private var _resolvedParameterTypes: Option[Seq[ValueType]] = None
    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = super.getDebugInfo + " calling: " + methodName + "(" + getParameterExpressionNodes.mkString(",") + ")"

    override protected def resolveDefinitionImpl(actionContext: ActionContext): Option[AstNode] = {
        logger.debug("resolve definition of method call: " + getDebugInfo)
        resolveDefinitionIfPartOfExprDotExpr(actionContext) match {
            case defOpt@Some(_) =>
                // this identifier is part of expression1.expression2....
                defOpt
            case _ =>
                findDefinition(actionContext).headOption
        }

    }

    override def qualifiedName: Option[QualifiedName] = Option(methodName)

    def getParameterTypes(actionContext: ActionContext): Seq[ValueType] = {
        _resolvedParameterTypes match {
            case Some(paramTypes) =>
                paramTypes
            case None =>
                //NOTE - when type can not be resolved it should be replaced with Any, and not removed
                // because this will make number of method parameters incorrect
                val valueTypes =
                    getParameterExpressionNodes.map{n =>
                        n.resolveDefinition(actionContext) match {
                            case Some(defNode: IsTypeDefinition) =>
                                defNode.getValueType.getOrElse(ValueTypeAny)
                            case _ => ValueTypeAny
                        }
                    }
                setResolvedParameterTypes(valueTypes)
                valueTypes
        }
    }

    def setResolvedParameterTypes(paramTypes: Seq[ValueType]): Unit = {
        _resolvedParameterTypes = Option(paramTypes)
    }
    def isParameterTypesResolved: Boolean = _resolvedParameterTypes.nonEmpty

    def getParameterExpressionNodes: Seq[AbstractExpression] = {
        getChildInAst[ExpressionListNode](ExpressionListNodeType) match {
            case Some( expressionList ) =>
                expressionList.getExpressions
            case None => Seq.empty
        }
    }

    private def findDefinition(actionContext: ActionContext): Seq[AstNode] = {
        val methodCallNode: MethodCallNode = this
        val finder = new AscendingDefinitionFinder(actionContext)
        val methods = finder.findDefinition(methodCallNode, methodCallNode)
        if (methods.nonEmpty && methods.length > 1) {
            // try to find more precise match using exact parameter types, do NOT allow apex conversions (e.g. Integer <> Decimal)
            val matcher = new MethodMatcher(methodCallNode, actionContext)
            val sortedMethods =
                methods.sortWith{
                    case (left: MethodNode, right: MethodNode) =>
                        val diff =
                            matcher.getSimilarityScore(left, withApexConversions = false) -
                                matcher.getSimilarityScore(right, withApexConversions = false)
                        diff > 0
                    case _ => false
                }
            sortedMethods
        } else {
            methods
        }
    }
}

