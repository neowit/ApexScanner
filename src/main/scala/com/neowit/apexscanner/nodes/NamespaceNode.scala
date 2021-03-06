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

/**
  * Created by Andrey Gavrikov 
  */
case class NamespaceNode(override val name: Option[String]) extends ClassLike {
    override def symbolKind: SymbolKind = SymbolKind.Class

    override def getValueType: Option[ValueType] = {
        qualifiedName.map(name => ValueTypeClass(name))
    }

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

    override def range: Range = Range.INVALID_LOCATION

    override def nodeType: AstNodeType = ClassNodeType

    override def toString: String = "Namespace: " + name
}
