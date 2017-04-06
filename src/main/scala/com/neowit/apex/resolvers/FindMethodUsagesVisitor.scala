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

package com.neowit.apex.resolvers

import com.neowit.apex.ast._
import com.neowit.apex.nodes._

/**
  * Created by Andrey Gavrikov 
  */

class FindMethodUsagesVisitor(targetToFind: MethodNode) extends AstVisitor {
    private val methodMatcher: Option[MethodMatcher] = targetToFind.qualifiedName match {
        case Some(methodName) => Option(new MethodMatcher(methodName, targetToFind.getParameterTypes))
        case None => None
    }
    private val foundNodesBuilder = Seq.newBuilder[MethodCallNode]
    override def visit(node: AstNode): Boolean = {
        if (MethodCallNodeType == node.nodeType) {
            val methodCallNode = node.asInstanceOf[MethodCallNode]
            methodMatcher match {
              case Some(matcher) if matcher.isSameMethod(methodCallNode.methodName, methodCallNode.getParameterTypes)=>
                  foundNodesBuilder += methodCallNode
              case _ =>
            }

        }
        true
    }
    def getResult: Seq[MethodCallNode] = foundNodesBuilder.result()
}


