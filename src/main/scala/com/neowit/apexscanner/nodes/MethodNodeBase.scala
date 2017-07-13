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

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.symbols
import com.neowit.apexscanner.symbols.SymbolKind

/**
  * Created by Andrey Gavrikov 
  */
trait MethodNodeBase extends AstNode with HasApexDoc with IsTypeDefinition with ClassOrInterfaceBodyMember {

    override def nodeType: AstNodeType = MethodNodeType
    def nameOpt: Option[String]
    def isAbstract: Boolean
    def isStatic: Boolean
    /**
      * @return true if this node is a Scope node (e.g. method declaration, class declaration)
      */
    override def isScope: Boolean = !isAbstract && Range.INVALID_LOCATION != range

    override def qualifiedName: Option[QualifiedName] = {
        nameOpt match {
            case Some(name) =>
                getClassOrInterfaceName.map(clsName => QualifiedName(clsName, name))
            case None =>
                None
        }
    }

    override def isSymbol: Boolean = true

    override def getClassOrInterfaceNode: ClassLike = {
        findParentInAst(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }
    override def symbolName: String = qualifiedName.map(_.getLastComponent).getOrElse("")

    override def symbolKind: SymbolKind = SymbolKind.Method



    override def parentSymbol: Option[symbols.Symbol] = Option(getClassOrInterfaceNode)

    override def getDebugInfo: String = super.getDebugInfo + " Method: " + nameOpt

    def getParameterTypes: Seq[ValueType] = {
        getChildrenInAst[MethodParameterNode](MethodParameterNodeType, recursively = true).flatMap(_.getValueType)
    }
}