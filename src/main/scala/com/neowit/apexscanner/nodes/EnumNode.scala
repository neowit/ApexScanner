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

import com.neowit.apexscanner.symbols.{Symbol, SymbolKind}

case class EnumNode(range: Range ) extends ClassLike {
    override def nodeType: AstNodeType = EnumNodeType

    override def symbolKind: SymbolKind = SymbolKind.Enum

    override def getValueType: Option[ValueType] = {
        qualifiedName.map(name => ValueTypeEnum(name))
    }

    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

    /**
      * find container of current class/interface
      */
    override def parentClassOrInterface: Option[ClassLike] = None

    override def extendsNode: Option[ExtendsNode] = None

    /**
      * get super class of current class/interface
      *
      * @return
      */
    override def getSuperClassOrInterface: Option[ClassLike] = None

    override def implements: Seq[ImplementsInterfaceNode] = Seq.empty

    override def getSymbolsOfKind(kind: SymbolKind): Seq[Symbol] = {
        val symbols: Seq[Symbol] =
            kind match {
                case SymbolKind.Method => findChildrenInAst(_.nodeType == MethodNodeType).map(_.asInstanceOf[Symbol])
                case SymbolKind.Variable => findChildrenInAst(_.nodeType == ClassVariableNodeType).map(_.asInstanceOf[Symbol])
                case SymbolKind.Property => findChildrenInAst(_.nodeType == EnumConstantNodeType).map(_.asInstanceOf[Symbol])
                case _ => Seq.empty
            }
        symbols
    }
}
