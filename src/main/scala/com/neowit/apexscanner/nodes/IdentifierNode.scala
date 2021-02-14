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
import com.neowit.apexscanner.scanner.actions.ActionContext
import com.typesafe.scalalogging.LazyLogging

case class IdentifierNode(name: String, range: Range) extends AbstractExpression with LazyLogging {
    override def nodeType: AstNodeType = IdentifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + name

    override protected def resolveDefinitionImpl(actionContext: ActionContext): Option[AstNode] = {
        logger.debug("resolve definition of identifier: " + name)
        resolveDefinitionIfPartOfExprDotExpr(actionContext) match {
            case defOpt@ Some(_) =>
                // this identifier is part of expression1.expression2....
                defOpt
            case _ =>
                // first check if this Identifier is part of DataTypeNode
                val dataTypeNodeDef =
                    findParentInAst{
                        case n: DataTypeNode => n.qualifiedName.exists(_.endsWith(QualifiedName(Array(name))))
                        case _ => false
                    }.flatMap{
                        case n: DataTypeNode => n.resolveDefinition(actionContext)
                        case _ => None
                    }

                dataTypeNodeDef match {
                    case defOpt @ Some(_) =>
                        defOpt
                    case None =>
                        val finder = new AscendingDefinitionFinder(actionContext)
                        val res = finder.findDefinition(this, this).headOption
                        res match {
                            case resOpt @ Some(_) => resOpt
                            case None => // finally check in global AST
                                resolveDefinitionByQualifiedName()
                        }
                }
        }

    }

    def resolveDefinitionByQualifiedName(): Option[AstNode] = {
        import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder

        this.getProject  match {
            case Some(project) =>
                val finder = new QualifiedNameDefinitionFinder(project)
                val result = finder.findDefinition(QualifiedName(Array(name)))
                result
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
