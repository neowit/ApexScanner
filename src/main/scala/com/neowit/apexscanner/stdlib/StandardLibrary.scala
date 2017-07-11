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

package com.neowit.apexscanner.stdlib

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.symbols.SymbolKind

/**
  * Created by Andrey Gavrikov 
  */
trait StandardLibrary {
    def findChild(name: QualifiedName): Option[StdlibNode]

}

trait StdlibNode extends AstNode {
    override def range: Range = Range.INVALID_LOCATION
}

trait StdlibClassLike extends StdlibNode with ClassLike {
    override def nodeType: AstNodeType = ClassNodeType

    override def symbolKind: SymbolKind = SymbolKind.Class

    override def getValueType: Option[ValueType] = {
        qualifiedName.map(name => ValueTypeClass(name))
    }

    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)
}

case class StdlibNamespace(classMap: Map[String, StdlibClass]) extends StdlibClassLike

case class StdlibProperty(name: String) extends StdlibNode {
    override def nodeType: AstNodeType = ClassVariableNodeType
}
case class StdlibMethodParameter(name: String, `type`: String) extends StdlibNode {
    override def nodeType: AstNodeType = MethodParameterNodeType
}
case class StdlibMethod( argTypes: Option[Array[String]],
                         isStatic: Option[Boolean],
                         methodDoc: Option[String],
                         name: String,
                         parameters: Array[StdlibMethodParameter]
                       ) extends StdlibNode {
    override def nodeType: AstNodeType = MethodNodeType
}

case class StdlibClass(constructors: Array[StdlibMethod], methods: Array[StdlibMethod], properties: Array[StdlibProperty]) extends StdlibClassLike

case class ApexAPI(publicDeclarations: Map[String, StdlibNamespace])
