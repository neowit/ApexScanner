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

case class ClassVariableNode(range: Range) extends VariableLike with ClassOrInterfaceBodyMember {
    override def nodeType: AstNodeType = ClassVariableNodeType

    override def getType: DataTypeBase = {
        getChild[DataType](DataTypeNodeType)
            .map(_.asInstanceOf[DataTypeBase])
            .getOrElse(throw new NotImplementedError("Data Type support for this element is not implemented: " + this))
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

    //def getClassName: QualifiedName = findParent{}
    override def getClassOrInterfaceNode: ClassLike = {
        findParent(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
          case Some(n: ClassLike) => n
          case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = super.getDebugInfo + " " + "TODO"
}
