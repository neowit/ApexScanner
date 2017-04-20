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

trait DataTypeNode extends AstNode {
    override def nodeType: AstNodeType = DataTypeNodeType
    def getDataType: ValueType
}
/**
  * Created by Andrey Gavrikov 
  */
case class DataTypeNodeGeneric(qualifiedNameNode: QualifiedNameNode, typeArgumentsOpt: Option[TypeArgumentsNode], range: Range) extends DataTypeNode {

    def getDataType: ValueType = {
        typeArgumentsOpt match {
            case Some(typeArguments) if typeArguments.components.isEmpty =>
                ValueTypeSimple(qualifiedNameNode.qualifiedName)
            case Some(typeArguments) =>
                ValueTypeComplex(qualifiedNameNode.qualifiedName, typeArguments.components.map(_.getDataType))
            case None =>
                ValueTypeSimple(qualifiedNameNode.qualifiedName)
        }
    }
}
case class DataTypeNodeVoid(range: Range ) extends DataTypeNode {
    override def getDataType: ValueType = ValueTypeVoid
}

case class DataTypeNodeArray(qualifiedNameNode: QualifiedNameNode, range: Range ) extends DataTypeNode {
    override def getDataType: ValueType = ValueTypeArray(qualifiedNameNode)
}
