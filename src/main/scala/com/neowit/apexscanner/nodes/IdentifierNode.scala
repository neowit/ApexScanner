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

import com.neowit.apexscanner.antlr.ApexcodeParser.ClassDeclarationContext
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.resolvers.AscendingDefinitionFinder

case class IdentifierNode(name: String, range: Range) extends AbstractExpression {
    override def nodeType: AstNodeType = IdentifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + name

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        println("resolve definition of: " + name)
        getParentInAst(skipFallThroughNodes = true) match {
          case Some(n:ExpressionDotExpressionNode) =>
              // this identifier is part of expression1.expression2....
              n.getResolvedPartDefinition(this)
          case _ =>
              val finder = new AscendingDefinitionFinder()
              val res = finder.findDefinition(this, this).headOption
              res match {
                  case resOpt @ Some(_) => resOpt
                  case None => // finally check in global AST
                      resolveDefinitionByQualifiedName()
              }
        }

    }

    // TODO - implement proper (non blocking) future handling
    def resolveDefinitionByQualifiedName(): Option[AstNode] = {
        import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder
        import scala.concurrent.duration.Duration
        import scala.concurrent.Await
        import scala.concurrent.ExecutionContext.Implicits.global

        this.getProject  match {
            case Some(project) =>
                val finder = new QualifiedNameDefinitionFinder(project)
                val futureResult = finder.findDefinition(QualifiedName(Array(name)))
                Await.result(futureResult, Duration.Inf)
            case None => None
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
