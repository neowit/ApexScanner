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

trait VariableLike extends AstNode with HasApexDoc with HasTypeDefinition {

    def annotations: Seq[AnnotationNode] = getChildren(AnnotationNodeType).map(_.asInstanceOf[AnnotationNode])
    def modifiers: Set[ModifierNode] = getChildren(ModifierNodeType).map(_.asInstanceOf[ModifierNode]).toSet
    def name: Option[String] = getChild[IdentifierNode](IdentifierNodeType).map(_.name)
    def dataType: Option[DataType] = getChildren(DataTypeNodeType).headOption.map(_.asInstanceOf[DataType])
    def initExpression: Option[ExpressionNode] = getChildren(ExpressionNodeType).headOption.map(_.asInstanceOf[ExpressionNode])

    override def getApexDoc: Option[DocNode] = getChildren(DocNodeType).map(_.asInstanceOf[DocNode]).headOption
}

