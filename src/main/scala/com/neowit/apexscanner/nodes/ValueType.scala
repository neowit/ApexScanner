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
      * compare two types taking into account potential Type conversion which can be performed by Apex
      * @param leftDataType first type to compare with second
      * @param definitionParamDataType second type to compare with first
      * @return true if types may be equal according to potential Apex conversions
      *         e.g. Decimal is "equal" to "Integer"
      */
    def isSameTypeWithConversion(leftDataType: ValueType, definitionParamDataType: ValueType): Boolean = {
        if (definitionParamDataType.qualifiedName.endsWith(QualifiedName("Object"))) {
            true
        } else {
            // try standard Apex type conversions
            // leftDataType - usually parameter defined in method caller
            // rightDataType - usually parameter defined in method definition
            // i.e. public void method(Decimal) can accept
            // - call some.method(Integer)
            // - call some.method(Double)
            // i.e. public void method(Double) can accept
            // - call some.method(Integer)
            leftDataType.qualifiedName.getLastComponent.toLowerCase match {
                case "integer" =>
                    definitionParamDataType.qualifiedName.endsWith(QualifiedName("Double")) ||
                        definitionParamDataType.qualifiedName.endsWith(QualifiedName("Decimal"))
                case "double" =>
                    definitionParamDataType.qualifiedName.endsWith(QualifiedName("Decimal"))
                case "id" =>
                    definitionParamDataType.qualifiedName.endsWith(QualifiedName("String"))
                case "string" =>
                    definitionParamDataType.qualifiedName.endsWith(QualifiedName("Id"))
                case _ => false
            }
        }
    }
}
trait ValueType {
    def qualifiedName: QualifiedName
    def typeArguments: Seq[ValueType]

    /**
      * @param definitionParamDataType type to compare with
      * @param withTypeModifications if true then (in case if no exact match found) apply check for potential Apex conversions
      *                            e.g. Decimal == Integer
      * @return
      */
    def isSameType(definitionParamDataType: ValueType, withTypeModifications: Boolean = true): Boolean = {
        val isExactMatch =
            definitionParamDataType match {
                case ValueTypeAny => true
                case _ =>
                    val namesMatch = qualifiedName.couldBeMatch(definitionParamDataType.qualifiedName)

                    //names match, now compare type arguments
                    if (namesMatch && typeArguments.length == definitionParamDataType.typeArguments.length) {
                        val numOfMatchingArgs =
                            typeArguments.zip(definitionParamDataType.typeArguments).count{
                                case (left, right) => left.isSameType(right)
                            }
                        numOfMatchingArgs == typeArguments.length
                    } else {
                        false
                    }
            }
        if (!isExactMatch && withTypeModifications) {
            isEquivalentTo(definitionParamDataType)
        } else {
            isExactMatch
        }
    }

    /**
      * check if current DataType can be automatically converted by Apex to otherDataType
      *  This method is mainly useful for checking if given value can be passed to a method with parameter of different type
      * e.g.
      *  - Integer is equivalent to Decimal
      *  - Child Class is equivalent to Parent class
      *
      * @param definitionParamDataType type (used in method definition) to check equivalence with
      * @return
      */
    def isEquivalentTo(definitionParamDataType: ValueType): Boolean = false

    override def toString: String = {
        val arguments = if (typeArguments.isEmpty) "" else "<" + typeArguments.mkString(",") + ">"
        qualifiedName.toString + arguments
    }
}
//Map<String, Map<Integer, List<String>>>
case class ValueTypeComplex(qualifiedName: QualifiedName, typeArguments: Seq[ValueType]) extends ValueType

// String
case class ValueTypeSimple(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def isEquivalentTo(otherDataType: ValueType): Boolean = ValueType.isSameTypeWithConversion(this, otherDataType)
}

case class ValueTypeClass(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def isEquivalentTo(definitionParamDataType: ValueType): Boolean = {
        if (ValueType.isSameTypeWithConversion(this, definitionParamDataType)) {
            true
        } else {
            // fall back to exact qualified name comparison
            qualifiedName.couldBeMatch(definitionParamDataType.qualifiedName)
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

    override def isSameType(definitionParamDataType: ValueType, withTypeModifications: Boolean): Boolean = {
        if ( super.isSameType(definitionParamDataType, withTypeModifications) ) {
            return true
        }
        if (withTypeModifications) {
            // exact match is not required
            // check if this enum constant belongs to definitionParamDataType
            // i.e. MyEnum.My_Const == MyEnum
            QualifiedName(this.qualifiedName.components.dropRight(1)).equals(definitionParamDataType.qualifiedName)
        } else {
            false
        }
    }

    override def toString: String = {
        "enum constant: " + qualifiedName
    }
}

case class ValueTypeAnnotation(qualifiedName: QualifiedName) extends ValueType {
    override def typeArguments: Seq[ValueType] = Seq.empty

    override def toString: String = {
        "annotation: @" + qualifiedName
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

    override def toString: String = qualifiedName.toString + "[]"

    override def typeArguments: Seq[ValueType] = Seq.empty
}


