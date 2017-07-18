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
case class ExpressionStatementNode(range: Range) extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = ExpressionStatementNodeType

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        // ExpressionStatementNode can not contain anything other than single "expression" child
        getChildInAst[AbstractExpression](ExpressionNodeType)  match {
            case Some(ex: HasTypeDefinition) =>
                // finally try to resolve definition of fudged expression
                ex.resolveDefinition() match {
                    case defOpt @ Some(_: IsTypeDefinition) =>
                        defOpt
                    case _ =>
                        // TODO
                        ???
                }

            case _ =>
                None
        }
    }

    override def getDebugInfo: String = super.getDebugInfo + " STATEMENT"
}
