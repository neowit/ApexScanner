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

case class MethodNode(range: Range) extends MethodNodeBase { self =>

    def nameOpt: Option[String] =
        getChildInAst[MethodHeaderNode](MethodHeaderNodeType).flatMap(_.methodName)


    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

    def getApexDoc: Option[DocNode] = getChildrenInAst[DocNode](DocNodeType).headOption


    lazy val isAbstract: Boolean = getChildInAst[MethodBodyNode](MethodBodyNodeType).isEmpty

    lazy val isStatic: Boolean = {
        getChildInAst[MethodHeaderNode](MethodHeaderNodeType)
            .exists(_.modifiers.exists(_.modifierType == ModifierNode.STATIC))
    }

    override def visibility: Option[String] = {
        getChildInAst[MethodHeaderNode](MethodHeaderNodeType)
            .flatMap(_.modifiers.find(_.modifierType.isInstanceOf[ModifierNode.Visibility]).map(_.modifierType.toString))
    }

    override def getValueType: Option[ValueType] = {
        getChildInAst[MethodHeaderNode](MethodHeaderNodeType).flatMap(_.dataType)
    }


    override protected def getSelf: AstNode = self

}
object MethodNode {

    /**
      * allows to construct MethodNode from existing attributes (when there is no need to traverse parse tree)
      * this may be useful when adding standard apex methods, e.g. every object has hashcode() method
      * @param methodName name of method
      * @param methodIsStatic true if method is static
      * @param methodIsAbstract true if method is abstract
      * @param methodReturnType DataTypeNode representing method return type
      * @param parameterTypes types of method parameters
      * @param methodApexDoc java-doc style comment for given method, if available
      * @return instance of method.
      *         Note - parent AstNode must be assigned explicitly by the code calling createMethodNode()
      */
    def createMethodNode( methodName: String, methodIsStatic: Boolean, methodIsAbstract: Boolean,
                       methodReturnType: ValueType,
                       parameterTypes: Array[ValueType], methodApexDoc: Option[String]): MethodNodeBase = {

        val m = new MethodNodeBase { self =>

            override def nameOpt: Option[String] = Option(methodName)

            override def isAbstract: Boolean = methodIsAbstract

            override def isStatic: Boolean = methodIsStatic

            override def getValueType: Option[ValueType] = {
                Option(methodReturnType)
            }

            override def range: Range = Range.INVALID_LOCATION

            override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

            override def getApexDoc: Option[DocNode] = {
                methodApexDoc match {
                    case Some(text) =>
                        Option(DocNode(text, Range.INVALID_LOCATION))
                    case None => None
                }
            }

            override def getParameterTypes: Seq[ValueType] = parameterTypes

            override protected def getSelf: AstNode = self

            override def visibility: Option[String] = Option("public")
        }

        m

    }

    def addStandardMethods(parent: AstNode): Unit = {
        val qNameInteger = QualifiedName(Array("System", "Integer"))
        val qNameBoolean = QualifiedName(Array("System", "Boolean"))
        val qNameObject = QualifiedName(Array("Object"))

        // equals(Object): Boolean
        parent.addChildToAst(
            MethodNode.createMethodNode(
                methodName = "equals",
                methodIsStatic = false,
                methodIsAbstract = false,
                methodReturnType = ValueTypeSimple(qNameBoolean),
                parameterTypes = Array(ValueTypeSimple(qNameObject)),
                methodApexDoc =
                    Option(
                        """
                          |Indicates whether some other object is 'equal to' this one.
                          |
                          |Keep in mind the following when implementing the equals method. Assuming x, y, and z are non-null instances of your class, the equals method must be:
                          |– Reflexive: x.equals(x)
                          |– Symmetric: x.equals(y) should return true if and only if y.equals(x) returns true
                          |– Transitive: if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true
                          |– Consistent: multiple invocations of x.equals(y) consistently return true or consistently return false
                          |– For any non-null reference value x, x.equals(null) should return false
                          |The equals method in Apex is based on the equals method in Java.
                        """.stripMargin)
            ))

        // hashCode(): Integer
        parent.addChildToAst(
            MethodNode.createMethodNode(
                methodName = "hashCode",
                methodIsStatic = false,
                methodIsAbstract = false,
                methodReturnType = ValueTypeSimple(qNameInteger),
                parameterTypes = Array.empty,
                methodApexDoc =
                    Option(
                        """
                          |Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by Map.
                          |
                          |Keep in mind the following when implementing the hashCode method.
                          |– If the hashCode method is invoked on the same object more than once during execution of an Apex request, it must return the same value.
                          |– If two objects are equal, based on the equals method, hashCode must return the same value.
                          |– If two objects are unequal, based on the result of the equals method, it is not required that hashCode return distinct values.
                          |
                          |The hashCode method in Apex is based on the hashCode method in Java.
                        """.stripMargin)
            ))

        ()
    }
}

