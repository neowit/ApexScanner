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
import com.neowit.apex.ast.{AstVisitor, QualifiedName}

/**
  * Created by Andrey Gavrikov 
  */
object MethodCallNode {
    def apply(methodName: String, range: Range): MethodCallNode = {
        MethodCallNode(QualifiedName(methodName.split("\\.")), range)
    }
}

case class MethodCallNode(methodName: QualifiedName, range: Range) extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = MethodCallNodeType

    private var _resolvedParameterTypes: Option[Seq[ValueType]] = None
    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = super.getDebugInfo + " calling: " + methodName

    override protected def resolveDefinitionImpl(visitor: AstVisitor): Option[AstNode] = {
        ???
    }

    //TODO implement taking real parameter types into account
    // current version returns "*" for each parameter
    def getParameterTypes: Seq[ValueType] = {
        _resolvedParameterTypes match {
          case Some(paramTypes) =>
              paramTypes
          case None =>
              getChild[ExpressionListNode](ExpressionListNodeType) match {
                  case Some( expressionList ) => expressionList.getExpressions.map(e => ValueTypeAny)
                  case None => Seq.empty
              }
        }
    }

    def setResolvedParameterTypes(paramTypes: Seq[ValueType]): Unit = {
        _resolvedParameterTypes = Option(paramTypes)
    }
    def isParameterTypesResolved: Boolean = _resolvedParameterTypes.nonEmpty

    def getParameterExpressionNodes: Seq[AstNode] = {
        getChild[ExpressionListNode](ExpressionListNodeType) match {
            case Some( expressionList ) => expressionList.getExpressions
            case None => Seq.empty
        }
    }
}

