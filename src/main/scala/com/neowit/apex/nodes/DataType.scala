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

trait DataType {
    def qualifiedName: QualifiedName
    def typeArguments: Seq[DataType]

    def isSameType(otherDataType: DataType): Boolean = {
        otherDataType match {
            case DataTypeAny => true
            case _ =>
                val namesMatch = qualifiedName.couldBeMatch(otherDataType.qualifiedName)

                //names match, now compare type arguments
                if (namesMatch && typeArguments.length == otherDataType.typeArguments.length) {
                    val numOfMatchingArgs =
                        typeArguments.zip(otherDataType.typeArguments).count{
                            case (left, right) => left.isSameType(right)
                        }
                    numOfMatchingArgs == typeArguments.length
                } else {
                    false
                }
        }
    }

    override def toString: String = {
        val arguments = if (typeArguments.isEmpty) "" else "<" + typeArguments.mkString(",") + ">"
        qualifiedName + arguments
    }
}
//Map<String, Map<Integer, List<String>>>
case class DataTypeConcrete(qualifiedName: QualifiedName, typeArguments: Seq[DataType]) extends DataType {

    //def text: String = qualifiedName + typeArgumentsOpt.map(_.text).getOrElse("")

}

//VOID
case object DataTypeVoid extends DataType {
    //override def text: String = "void"
    def qualifiedName: QualifiedName = QualifiedName(Array("void"))

    override def typeArguments: Seq[DataType] = Seq.empty
}
//ANY - special type which match all and any types
case object DataTypeAny extends DataType {
    //override def text: String = "void"
    def qualifiedName: QualifiedName = QualifiedName(Array.empty)

    override def typeArguments: Seq[DataType] = Seq.empty

    override def isSameType(otherDataType: DataType): Boolean = true
}

//Some[] - array type
case class DataTypeArray(qualifiedNameNode: QualifiedNameNode) extends DataType {
    def qualifiedName: QualifiedName = qualifiedNameNode.qualifiedName

    override def toString: String = qualifiedName + "[]"

    override def typeArguments: Seq[DataType] = Seq.empty
}


