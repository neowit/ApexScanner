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
import com.neowit.apexscanner.symbols._

/**
  * Created by Andrey Gavrikov 
  */
case class EnumConstantNode (constantName: String, range: Range) extends VariableLike with ClassOrInterfaceBodyMember {self =>

    override val name: Option[String] = Option(constantName)

    override def getClassOrInterfaceNode: ClassLike = {
        findParentInAst(p => p.nodeType == EnumNodeType) match {
            case Some(n: ClassLike) => n
            case n =>
                throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }

    override protected def getSelf: AstNode = self

    override def isSymbol = true

    override def getValueType: Option[ValueType] = {
        qualifiedName.map(name => ValueTypeEnumConstant(name))
    }

    override def qualifiedName: Option[QualifiedName] = {
        getClassOrInterfaceNode.qualifiedName match {
            case Some(className) => Option(QualifiedName(className.components ++ Array(constantName)))
            case None => None
        }
    }

    override def nodeType: AstNodeType = EnumConstantNodeType

    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(self)

    override def symbolName: String = name.getOrElse("")

    override def symbolKind: SymbolKind = SymbolKind.Property

    override def parentSymbol: Option[Symbol] = Option(getClassOrInterfaceNode)
}

object EnumConstantNode {
    /**
      * add standard ENUM property methods
      * @param node parent node
      */
    def addStandardMethods(node: EnumConstantNode): Unit = {

        val qNameString = QualifiedName(Array("System", "String"))
        val qNameInteger = QualifiedName(Array("System", "Integer"))

        // name(): String
        node.addChildToAst(
            MethodNode.createMethodNode(
                methodName = "name",
                methodIsStatic = false,
                methodIsAbstract = false,
                methodReturnType = ValueTypeSimple(qNameString),
                parameterTypes = Array.empty,
                methodApexDoc = Option("Returns the name of the Enum item as a String.")
            ))

        // ordinal(): Integer
        node.addChildToAst(
            MethodNode.createMethodNode(
                methodName = "ordinal",
                methodIsStatic = false,
                methodIsAbstract = false,
                methodReturnType = ValueTypeSimple(qNameInteger),
                parameterTypes = Array.empty,
                methodApexDoc = Option("Returns the position of the item, as an Integer, in the list of Enum values starting with zero.")
            ))

        // add equals(Object) and hashCode()
        MethodNode.addStandardMethods(node)
    }
}