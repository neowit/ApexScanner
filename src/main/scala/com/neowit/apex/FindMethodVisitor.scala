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
class FindMethodVisitor(methodName: String, paramTypes: List[String]) extends AstVisitor {
    private var foundMethodNode: Option[MethodNode] = None
    private val methodNameLower = methodName.toLowerCase()
    private val paramTypesLower = paramTypes.map(_.toLowerCase())
    private val paramTypesLength = paramTypes.length

    override def visit(node: AstNode): Boolean = {
        if (MethodNodeType == node.nodeType) {
            val methodNode = node.asInstanceOf[MethodNode]
            methodNode.nameOpt match {
                case Some(name) if methodNameLower == name.toLowerCase =>
                    // check if parameter type(s) match
                    val currentParamTypes = methodNode.getParameterTypes
                    if (0 == paramTypesLength && currentParamTypes.isEmpty) {
                        // target method does not have parameters
                        foundMethodNode = Option(methodNode)
                    } else if (paramTypesLength > 0 && paramTypesLength == currentParamTypes.length) {
                        val typePairs = paramTypesLower.zip(currentParamTypes.map(_.toLowerCase))
                        val notExactMatch =
                            // check if there is a combination of parameters which do not match
                            typePairs.exists{
                                case (left, right) =>
                                    left != right && "*" != left
                            }
                        // found target method if number of parameter match
                        if (!notExactMatch) {
                            foundMethodNode = Option(methodNode)
                        }
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
