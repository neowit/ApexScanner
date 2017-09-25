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
case class ConstructorNode(range: Range) extends MethodNodeBase { self =>
    def nameOpt: Option[String] =
        getChildInAst[MethodHeaderNode](MethodHeaderNodeType).flatMap(_.methodName)


    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

    def getApexDoc: Option[DocNode] = getChildrenInAst[DocNode](DocNodeType).headOption


    val isAbstract: Boolean = false // constructors can not be abstract
    val isStatic: Boolean = false // constructors can not be static

    override def getValueType: Option[ValueType] = {
        getClassOrInterfaceNode.getValueType
    }

    override def symbolKind: SymbolKind = SymbolKind.Constructor

    override protected def getSelf: AstNode = self
}
