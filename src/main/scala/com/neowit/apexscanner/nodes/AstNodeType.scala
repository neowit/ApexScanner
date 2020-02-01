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

import com.neowit.apexscanner.symbols.SymbolKind

sealed trait AstNodeType {
    def getSymbolKind: SymbolKind = {
        AstNodeType.getSymbolKindByNodeType(this)
    }
}

case object AnnotationNodeType extends AstNodeType
case object AnnotationParameterNodeType extends AstNodeType
case object ClassNodeType extends AstNodeType
case object ClassVariableNodeType extends AstNodeType
case object ClassPropertyNodeType extends AstNodeType
case object CreatorNodeType extends AstNodeType
case object DataTypeNodeType extends AstNodeType
case object DocNodeType extends AstNodeType
case object EnumConstantNodeType extends AstNodeType
case object EnumNodeType extends AstNodeType
case object ExpressionListNodeType extends AstNodeType
case object ExpressionNodeType extends AstNodeType
case object ExpressionStatementNodeType extends AstNodeType
case object ExtendsNodeType extends AstNodeType
case object ForControlNodeType extends AstNodeType
case object SwitchStatementNodeType extends AstNodeType
case object SwitchWhenNodeType extends AstNodeType
case object SwitchWhenElseNodeType extends AstNodeType
case object FallThroughNodeType extends AstNodeType
case object FileNodeType extends AstNodeType
case object IdentifierNodeType extends AstNodeType
case object ImplementsInterfaceNodeType extends AstNodeType
case object InterfaceNodeType extends AstNodeType
//case object LiteralNodeType extends AstNodeType
case object LocalVariableNodeType extends AstNodeType
case object MethodBodyNodeType extends AstNodeType
case object MethodCallNodeType extends AstNodeType
case object MethodHeaderNodeType extends AstNodeType
case object MethodNameNodeType extends AstNodeType
case object MethodNodeType extends AstNodeType
case object MethodParameterNodeType extends AstNodeType
case object ModifierNodeType extends AstNodeType
case object NullNodeType extends AstNodeType
case object QualifiedNameNodeType extends AstNodeType
case object TriggerNodeType extends AstNodeType
case object TypeArgumentsNodeType extends AstNodeType
case object TypeCastNodeType extends AstNodeType

// SOQL
case object SelectItemExpressionNodeType extends AstNodeType
case object SoqlQueryNodeType extends AstNodeType
case object SelectStatementNodeType extends AstNodeType
case object SoqlWhereNodeType extends AstNodeType
case object SoqlWhereFieldExpressionNodeType extends AstNodeType
case object SubqueryNodeType extends AstNodeType
case object ChildRelationshipSubqueryNodeType extends AstNodeType
case object SObjectChildRelationshipNodeType extends AstNodeType // single child relationship
case object SObjectChildRelationshipContainerNodeType extends AstNodeType // container with all child relationships of SObject
case object TypeOfExpressionNodeType extends AstNodeType

case object FromExpressionNodeType extends AstNodeType
case object FromNodeType extends AstNodeType
case object FieldNameOrRefNodeType extends AstNodeType

object AstNodeType {

    def getSymbolKindByNodeType(astNodeType: AstNodeType): SymbolKind = {
        astNodeType match {
            case MethodNodeType => SymbolKind.Method
            case ClassVariableNodeType => SymbolKind.Variable
            case ClassPropertyNodeType => SymbolKind.Property
            case EnumNodeType => SymbolKind.Enum
            case _ => ???
        }
    }
}