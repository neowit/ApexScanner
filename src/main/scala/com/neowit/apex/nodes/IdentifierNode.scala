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

import com.neowit.apex.resolvers.AscendingDefinitionFinder
import com.neowit.apex.scanner.antlr.ApexcodeParser.ClassDeclarationContext

case class IdentifierNode(name: String, range: Range) extends AbstractExpression with HasTypeDefinition {
    override def nodeType: AstNodeType = IdentifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + name

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        println("resolve definition of: " + name)
        getParent(skipFallThroughNodes = true) match {
          case Some(n:ExpressionDotExpressionNode) =>
              // this identifier is part of expression1.expression2....
              // TODO update ExpressionDotExpressionNode.resolveDefinition() to make it record all resolved parts
              n.getResolvedPartDefinition(this)
          case _ =>
              val finder = new AscendingDefinitionFinder()
              val res = finder.findDefinition(this, this).headOption
              res
        }

    }
}

object IdentifierNode {
    def apply(ctx: ClassDeclarationContext): IdentifierNode = {
        if (null != ctx.className()) {
            IdentifierNode(ctx.className().getText, Range(ctx.className()))
        } else {
            throw new UnsupportedOperationException("ClassDeclarationContext without className was Encountered")
        }
    }
}
