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

import com.neowit.apexscanner.symbols._

case class ClassNode(override val name: Option[String], range: Range) extends ClassLike {
    override def nodeType: AstNodeType = ClassNodeType

    override def symbolKind: SymbolKind = SymbolKind.Class

    override def getValueType: Option[ValueType] = {
        qualifiedName.map(name => ValueTypeClass(name))
    }

    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

    /**
      * @return true if this node is a Scope node (e.g. method declaration, class declaration)
      */
    override def isScope: Boolean = true

    /**
      * this is only useful for isScope == true AST Nodes
      * return all symbols of given kind in scope of current AST Node
      * @param kind type of symbol in scope of this node
      * @return
      */
    override def getSymbolsOfKind(kind: SymbolKind): Seq[Symbol] = {
        val symbols: Seq[Symbol] =
            kind match {
                case SymbolKind.Method => findChildrenInAst(_.nodeType == MethodNodeType).map(_.asInstanceOf[Symbol])
                case SymbolKind.Variable => findChildrenInAst(n => ClassVariableNodeType == n.nodeType || ClassPropertyNodeType == n.nodeType).map(_.asInstanceOf[Symbol])
                case SymbolKind.Enum => findChildrenInAst(_.nodeType == EnumNodeType).map(_.asInstanceOf[Symbol])
                case _ => Seq.empty
            }
        val parentSymbols: Seq[Symbol] = getParentScopeNode match {
            case Some(parentScopeNode) => parentScopeNode.getSymbolsOfKind(kind)
            case _ => Seq.empty
        }

        symbols ++ parentSymbols
    }
}


