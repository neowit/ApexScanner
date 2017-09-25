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

package com.neowit.apexscanner.resolvers

import com.neowit.apexscanner.ast.{AstVisitor, QualifiedName}
import com.neowit.apexscanner.matchers.MethodMatcher
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.scanner.actions.ActionContext

/**
  * Created by Andrey Gavrikov
  *
  * @param paramTypes list of type names (case insensitive) <br/>
  *                   List("integer", "list❮String❯") <br/>
  *                   List("integer", "*") - "*" means any type of second argument is a match
  *
  */
class FindMethodVisitor(methodName: QualifiedName, paramTypes: Seq[ValueType], actionContext: ActionContext) extends AstVisitor {
    private val matcher = new MethodMatcher(methodName, paramTypes, actionContext)
    private var foundMethodNode: Option[MethodNode] = None

    override def visit(node: AstNode): Boolean = {
        if (MethodNodeType == node.nodeType) {
            val methodNode = node.asInstanceOf[MethodNode]
            methodNode.qualifiedName match {
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

