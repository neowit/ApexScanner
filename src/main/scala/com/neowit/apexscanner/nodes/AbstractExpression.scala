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
import com.neowit.apexscanner.scanner.actions.ActionContext


/**
  * Created by Andrey Gavrikov 
  */
trait AbstractExpression extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = ExpressionNodeType

    protected def resolveDefinitionIfPartOfExprDotExpr(actionContext: ActionContext): Option[AstNode] = {
        getParentInAst(skipFallThroughNodes = true) match {
            case Some(n: ExpressionDotExpressionNode) =>
                // this node is part of expression1.expression2....
                n.getResolvedPartDefinition(this, actionContext)
            case _ => None
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
    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

    override def getValueType: Option[ValueType] = Option(dataType.getDataType)

    override def qualifiedName: Option[QualifiedName] = Option(dataType.getDataType.qualifiedName)
}

case class ThisExpressionNode(range: Range) extends AbstractExpression with HasTypeDefinition {
    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        Option(getClassOrInterfaceNode)
    }

    def getClassOrInterfaceNode: ClassLike = {
        findParentInAst(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }
}

case class SuperExpressionNode(range: Range) extends AbstractExpression with HasTypeDefinition {
    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        getClassOrInterfaceNode.getSuperClassOrInterface
    }

    def getClassOrInterfaceNode: ClassLike = {
        findParentInAst(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }
}
