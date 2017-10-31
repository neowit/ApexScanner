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

object ValueType {
    /**
      * compare two types taking inti account potential Type conversion which can be performed by Apex
      * @param leftDataType first type to compare with second
      * @param rightDataType second type to compare with first
      * @return true if types may be equal according to potential Apex conversions
      *         e.g. Decimal is "equal" to "Integer"
      */
    def isSameTypeWithConversion(leftDataType: ValueType, rightDataType: ValueType): Boolean = {
        // try standard Apex type conversions
        leftDataType.qualifiedName.getLastComponent.toLowerCase match {
            case "integer" =>
                rightDataType.qualifiedName.endsWith(QualifiedName("Integer")) ||
                    rightDataType.qualifiedName.endsWith(QualifiedName("Decimal"))
            case "decimal" =>
                rightDataType.qualifiedName.endsWith(QualifiedName("Integer")) ||
                    rightDataType.qualifiedName.endsWith(QualifiedName("Decimal"))
            case _ => false
        }
    }
}
trait ValueType {
    def qualifiedName: QualifiedName
    def typeArguments: Seq[ValueType]

    /**
      * @param otherDataType type to compare with
      * @param withApexConversions if true then (in case if no exact match found) apply check if potential Apex conversions
      *                            e.g. Decimal == Integer
      * @return
      */
    def isSameType(otherDataType: ValueType, withApexConversions: Boolean = true): Boolean = {
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

    override def isSameType(otherDataType: ValueType, withApexConversions: Boolean = true): Boolean = {
        if (super.isSameType(otherDataType)) {
            true
        } else {
            if (withApexConversions) {
                ValueType.isSameTypeWithConversion(this, otherDataType)
            } else {
                false
            }
        }
    }
}

case class ValueTypeClass(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def isSameType(otherDataType: ValueType, withApexConversions: Boolean = true): Boolean = {
        if (super.isSameType(otherDataType)) {
            true
        } else {
            if (withApexConversions) {
                ValueType.isSameTypeWithConversion(this, otherDataType)
            } else {
                false
            }
        }
    }
    override def toString: String = {
        "class: " + qualifiedName
    }
}
case class ValueTypeInterface(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "interface: " + qualifiedName
    }
}

case class ValueTypeTrigger(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "trigger: " + qualifiedName
    }
}

case class ValueTypeEnum(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "enum: " + qualifiedName
    }
}

case class ValueTypeEnumConstant(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "enum constant: " + qualifiedName
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
    def qualifiedName: QualifiedName = new QualifiedName(Array.empty)

    override def typeArguments: Seq[ValueType] = Seq.empty

    override def isSameType(otherDataType: ValueType, withApexConversions: Boolean) = true
}

//Some[] - array type
case class ValueTypeArray(qualifiedNameNode: QualifiedNameNode) extends ValueType {
    def qualifiedName: QualifiedName = qualifiedNameNode.qualifiedName

    override def toString: String = qualifiedName + "[]"

    override def typeArguments: Seq[ValueType] = Seq.empty
}


