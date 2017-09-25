/*
 *
 *  * Copyright (c) 2017 Andrey Gavrikov.
 *  * this file is part of tooling-force.com application
 *  * https://github.com/neowit/tooling-force.com
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.neowit.apexscanner.nodes

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.symbols
import com.neowit.apexscanner.symbols.SymbolKind

trait ClassVariableNodeBase extends VariableLike with ClassOrInterfaceBodyMember {

    override def getValueType: Option[ValueType] = {
        getChildInAst[DataTypeNode](DataTypeNodeType) .map(_.getDataType)
    }

    override def qualifiedName: Option[QualifiedName] = {
        name match {
          case Some(_name) =>
              getClassOrInterfaceNode.qualifiedName match {
                  case Some(className) => Option(QualifiedName(className.components ++ Array(_name)))
                  case None => None
              }
          case None => None
        }
    }

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

    override def getClassOrInterfaceNode: ClassLike = {
        findParentInAst(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
          case Some(n: ClassLike) => n
          case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }

    override protected def getSelf: AstNode = this

    override def symbolName: String = name.getOrElse("")

    override def symbolKind: SymbolKind = SymbolKind.Field

    override def parentSymbol: Option[symbols.Symbol] = Option(getClassOrInterfaceNode)

    override def symbolIsStatic: Boolean = modifiers.exists(_.modifierType == ModifierNode.STATIC)

    override def symbolValueType: Option[String] = getValueType.map(_.toString)

    override def visibility: Option[String] = modifiers.find(_.modifierType.isInstanceOf[ModifierNode.Visibility]).map(_.modifierType.toString)
    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = super.getDebugInfo + " " + "TODO"
}
