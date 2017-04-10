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

trait ValueType {
    def qualifiedName: QualifiedName
    def typeArguments: Seq[ValueType]

    def isSameType(otherDataType: ValueType): Boolean = {
        otherDataType match {
            case ValueTypeAny => true
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
case class ValueTypeComplex(qualifiedName: QualifiedName, typeArguments: Seq[ValueType]) extends ValueType

// String
case class ValueTypeSimple(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty
}

case class ValueTypeClass(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "class" + qualifiedName
    }
}
case class ValueTypeInterface(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "interface" + qualifiedName
    }
}

case class ValueTypeTrigger(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "trigger" + qualifiedName
    }
}

//VOID
case object ValueTypeVoid extends ValueType {
    //override def text: String = "void"
    def qualifiedName: QualifiedName = QualifiedName(Array("void"))

    override def typeArguments: Seq[ValueType] = Seq.empty
}
//ANY - special type which match all and any types
case object ValueTypeAny extends ValueType {
    //override def text: String = "void"
    def qualifiedName: QualifiedName = QualifiedName(Array.empty)

    override def typeArguments: Seq[ValueType] = Seq.empty

    override def isSameType(otherDataType: ValueType): Boolean = true
}

//Some[] - array type
case class ValueTypeArray(qualifiedNameNode: QualifiedNameNode) extends ValueType {
    def qualifiedName: QualifiedName = qualifiedNameNode.qualifiedName

    override def toString: String = qualifiedName + "[]"

    override def typeArguments: Seq[ValueType] = Seq.empty
}


