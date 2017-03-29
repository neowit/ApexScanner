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

package com.neowit.apex

import com.neowit.apex.ast.AstVisitor
import com.neowit.apex.nodes.{AstNode, MethodNode, MethodNodeType}

/**
  * Created by Andrey Gavrikov
  *
  * @param paramTypes list of type names (case insensitive) <br/>
  *                   List("integer", "list❮String❯") <br/>
  *                   List("integer", "*") - "*" means any type of second argument is a match
  *
  */
class FindMethodVisitor(methodName: String, paramTypes: Seq[String]) extends AstVisitor {
    private val matcher = new MethodMatcher(methodName, paramTypes)
    private var foundMethodNode: Option[MethodNode] = None

    override def visit(node: AstNode): Boolean = {
        if (MethodNodeType == node.nodeType) {
            val methodNode = node.asInstanceOf[MethodNode]
            methodNode.nameOpt match {
                case Some(otherMethodName) =>
                    if (matcher.isSameMethod(otherMethodName, methodNode.getParameterTypes)) {
                        foundMethodNode = Option(methodNode)
                    }
                case _ =>
            }
            false // no point looking further inside method
        } else {
            foundMethodNode.isEmpty // keep scanning until found appropriate node
        }
    }
    def getFoundMethod: Option[MethodNode] = {
        foundMethodNode
    }
}

class MethodMatcher(methodName: String, paramTypes: Seq[String]) {
    private val methodNameLower = methodName.toLowerCase()
    private val paramTypesLower = paramTypes.map(_.toLowerCase())
    private val paramTypesLength = paramTypes.length

    /*
      * @param paramTypes list of type names (case insensitive) <br/>
      *                   List("integer", "list❮String❯") <br/>
      *                   List("integer", "*") - "*" means any type of second argument is a match
      *
     */
    def isSameMethod(otherMethodName: String, otherParamTypes: Seq[String]): Boolean = {
        if (methodNameLower == otherMethodName.toLowerCase && paramTypesLength == otherParamTypes.length) {
            if (0 == paramTypesLength && otherParamTypes.isEmpty) {
                // target method does not have parameters
                true
            } else if (paramTypesLength > 0 && paramTypesLength == otherParamTypes.length) {
                val typePairs = paramTypesLower.zip(otherParamTypes.map(_.toLowerCase))
                // check if there is a combination of parameters which do not match
                val notExactMatch =
                    typePairs.exists {
                        case (left, right) =>
                            left != right && "*" != right && left != "*"
                    }
                // found target method if number of parameter match
                !notExactMatch
            } else {
                false
            }
        } else {
            false
        }
    }
}