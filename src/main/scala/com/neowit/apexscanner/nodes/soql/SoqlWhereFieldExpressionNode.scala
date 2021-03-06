/*
 * Copyright (c) 2017 Andrey Gavrikov.
 * this file is part of tooling-force.com application
 * https://github.com/neowit/tooling-force.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neowit.apexscanner.nodes.soql

import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, HasTypeDefinition, Range, SoqlWhereFieldExpressionNodeType, SoqlWhereNodeType}

/**
  * Created by Andrey Gavrikov 
  */
case class SoqlWhereFieldExpressionNode (range: Range) extends AstNode with HasTypeDefinition {

    override def nodeType: AstNodeType = SoqlWhereFieldExpressionNodeType

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = findParentInAst(_.nodeType == SoqlWhereNodeType)
}
