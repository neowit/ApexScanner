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

case class MethodNode(range: Range ) extends AstNode with HasApexDoc with HasTypeDefinition with ClassOrInterfaceBodyMember{
    def nameOpt: Option[String] =
        getChild[MethodHeaderNode](MethodHeaderNodeType).flatMap(_.methodName)

    override def nodeType: AstNodeType = MethodNodeType

    def getApexDoc: Option[DocNode] = getChildren[DocNode](DocNodeType).headOption
    override def getDebugInfo: String = super.getDebugInfo + " Method: " + nameOpt
    def getParameterTypes: Seq[String] = {
        getChildren[MethodParameterNode](MethodParameterNodeType, recursively = true).flatMap(_.getType.map(_.text))
    }

    lazy val isAbstract: Boolean = getChild[MethodBodyNode](MethodBodyNodeType).nonEmpty

    override def getType: Option[DataTypeBase] = {
        getChild[MethodHeaderNode](MethodHeaderNodeType).flatMap(_.dataType)
    }

    override def getClassOrInterfaceNode: ClassLike = {
        findParent(p => p.nodeType == ClassNodeType || p.nodeType == InterfaceNodeType ) match {
            case Some(n: ClassLike) => n
            case n => throw new NotImplementedError("getClassOrInterfaceNode support for this element is not implemented: " + n)
        }
    }

    override def qualifiedName: Option[QualifiedName] = {
        nameOpt match {
          case Some(name) =>
              getClassOrInterfaceName.map(clsName => QualifiedName(clsName, name))
          case None =>
                None
        }
    }
}


