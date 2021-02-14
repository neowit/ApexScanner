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

package com.neowit.apexscanner.nodes.soql

import com.neowit.apexscanner.nodes.{AbstractExpression, AstNode, AstNodeType, ExpressionListNodeType, ExpressionNodeType, FallThroughNode, Range}

/**
  * Created by Andrey Gavrikov 
  */
case class SelectItemsNode(items: Seq[AstNode],range: Range) extends AstNode {
    override def nodeType: AstNodeType = ExpressionListNodeType
    def getExpressions: Seq[AbstractExpression] = {
        items.flatMap{
            case n: AbstractExpression => Option(n)
            case n: FallThroughNode => n.getChildInAst[AbstractExpression](ExpressionNodeType)
            case _ => Seq.empty
        }
    }

    override def getDebugInfo: String = super.getDebugInfo + getExpressions.map(_.getDebugInfo).mkString(", ")
}
