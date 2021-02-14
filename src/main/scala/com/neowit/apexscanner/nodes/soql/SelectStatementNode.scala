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

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, FieldNameOrRefNodeType, IsTypeDefinition, Range, SelectStatementNodeType, SoqlQueryNodeType, ValueType}
import com.neowit.apexscanner.symbols.Symbol

/**
  * Created by Andrey Gavrikov 
  */
case class SelectStatementNode(range: Range) extends AstNode with IsTypeDefinition {

    override def nodeType: AstNodeType = SelectStatementNodeType

    override def isScope: Boolean = true

    override def getValueType: Option[ValueType] = {
        findParentInAst(_.nodeType == SoqlQueryNodeType).flatMap{
            case n@ SoqlQueryNode(_, _) => n.getValueType
            case _ => None
        }
    }

    override def qualifiedName: Option[QualifiedName] = {
        findParentInAst(_.nodeType == SoqlQueryNodeType).flatMap{
            case n@ SoqlQueryNode(_, _) => n.getValueType.map(_.qualifiedName)
            case _ => None
        }
    }

    override def filterCompletionSymbols(symbols: Seq[Symbol]): Seq[Symbol] = {
        // SOQL query does not support duplicate names in SELECT ... part
        //remove Field Names which are already present in SELECT ... FROM part
        val existingFields = getChildrenInAst[FieldNameOrRefNode](FieldNameOrRefNodeType, recursively = true)

        if (existingFields.isEmpty) {
            symbols
        } else {
            // remove existing fields from symbols list
            val existingFieldNames: Set[String] = existingFields.filterNot(_.qualifiedName.isEmpty).map(_.qualifiedName.getLastComponent.toLowerCase).toSet
            symbols.filterNot(s => existingFieldNames.contains(s.symbolName.toLowerCase))
        }
    }

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

}
