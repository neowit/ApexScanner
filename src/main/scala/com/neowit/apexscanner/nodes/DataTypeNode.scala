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
import com.neowit.apexscanner.nodes.ClassLike.CLASS_LIKE_TYPES

trait DataTypeNode extends AstNode with IsTypeDefinition {
    override def nodeType: AstNodeType = DataTypeNodeType
    def getDataType: ValueType

    override def getValueType: Option[ValueType] = Option(getDataType)
    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)
    override def qualifiedName: Option[QualifiedName] = Option(getDataType.qualifiedName)
}
/**
  * Created by Andrey Gavrikov 
  */
case class DataTypeNodeGeneric(qualifiedNameNode: QualifiedNameNode, typeArgumentsOpt: Option[TypeArgumentsNode], range: Range) extends DataTypeNode {

    def getDataType: ValueType = {
        val qName = qualifiedNameNode.qualifiedName
        // check if this data type represents inner ClassLike node
        // and if its name needs to be concatenated with QualifiedName of outer class

        val fullQName =
            findParentInAst{n =>
                    CLASS_LIKE_TYPES.contains(n.nodeType) &&
                        !qName.couldBeMatch(n.asInstanceOf[ClassLike].qualifiedName)
            } match {
                case Some(parentClassLike: ClassLike) if parentClassLike.findClassLikeChild(qName).isDefined =>
                    parentClassLike.qualifiedName match {
                        case Some(parentQName) if !qName.contains(parentQName.getLastComponent)=> QualifiedName(parentQName, qName)
                        case _ => qName
                    }
                case _ => qName
            }

        typeArgumentsOpt match {
            case Some(typeArguments) if typeArguments.components.isEmpty =>
                ValueTypeSimple(fullQName)
            case Some(typeArguments) =>
                ValueTypeComplex(fullQName, typeArguments.components.map(_.getDataType))
            case None =>
                ValueTypeSimple(fullQName)
        }
    }
}
case class DataTypeNodeVoid(range: Range ) extends DataTypeNode {
    override def getDataType: ValueType = ValueTypeVoid
}

case class DataTypeNodeArray(qualifiedNameNode: QualifiedNameNode, range: Range ) extends DataTypeNode {
    override def getDataType: ValueType = ValueTypeArray(qualifiedNameNode)
}
