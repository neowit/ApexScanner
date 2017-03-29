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

sealed trait AstNodeType

case object AnnotationNodeType extends AstNodeType
case object AnnotationParameterNodeType extends AstNodeType
case object ClassNodeType extends AstNodeType
case object ClassVariableNodeType extends AstNodeType
case object DataTypeNodeType extends AstNodeType
case object DocNodeType extends AstNodeType
case object ExpressionNodeType extends AstNodeType
case object ExpressionListNodeType extends AstNodeType
case object ExtendsNodeType extends AstNodeType
case object FallThroughNodeType extends AstNodeType
case object FileNodeType extends AstNodeType
case object IdentifierNodeType extends AstNodeType
case object ImplementsInterfaceNodeType extends AstNodeType
case object InterfaceNodeType extends AstNodeType
case object MethodBodyNodeType extends AstNodeType
case object MethodHeaderNodeType extends AstNodeType
case object MethodNodeType extends AstNodeType
case object MethodCallNodeType extends AstNodeType
case object MethodNameNodeType extends AstNodeType
case object MethodParameterNodeType extends AstNodeType
case object ModifierNodeType extends AstNodeType
case object NullNodeType extends AstNodeType
case object QualifiedNameNodeType extends AstNodeType
case object TypeArgumentsNodeType extends AstNodeType
case object LocalVariableNodeType extends AstNodeType

