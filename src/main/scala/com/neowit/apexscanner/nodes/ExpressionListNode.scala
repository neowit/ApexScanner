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

/**
  * Created by Andrey Gavrikov 
  */
case class ExpressionListNode(expressions: Seq[AstNode], range: Range) extends AstNode {
    override def nodeType: AstNodeType = ExpressionListNodeType

    def getExpressions: Seq[AbstractExpression] = {
        expressions.flatMap{
            case n: AbstractExpression => Option(n)
            case n: FallThroughNode => n.getChildInAst[AbstractExpression](ExpressionNodeType)
        }
    }
    /*
    def getExpressions: Seq[AbstractExpression] = findChildrenInAst{
        case n: AbstractExpression => true
        case _ => false
    }.map(_.asInstanceOf[AbstractExpression])
    */

    override def getDebugInfo: String = super.getDebugInfo + getExpressions.map(_.getDebugInfo).mkString(", ")
}
