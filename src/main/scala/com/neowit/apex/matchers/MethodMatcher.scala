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

package com.neowit.apex.matchers

import com.neowit.apex.nodes.{DataType, MethodCallNode, MethodNode, QualifiedName}

/**
  * Created by Andrey Gavrikov 
  */
class MethodMatcher(methodName: QualifiedName, paramTypes: Seq[DataType]) {
    private val paramTypesLength = paramTypes.length

    def this(methodCallNode: MethodCallNode) = {
        this(methodCallNode.methodName, methodCallNode.getParameterTypes)
    }

    /*
      * @param paramTypes list of type names (case insensitive) <br/>
      *                   List("integer", "list❮String❯") <br/>
      *                   List("integer", "*") - "*" means any type of second argument is a match
      *
     */
    def isSameMethod(otherMethodName: QualifiedName, otherParamTypes: Seq[DataType]): Boolean = {
        val nameMatch = methodName.couldBeMatch(otherMethodName)

        if (nameMatch && paramTypesLength == otherParamTypes.length) {
            if (0 == paramTypesLength && otherParamTypes.isEmpty) {
                // target method does not have parameters
                true
            } else if (paramTypesLength > 0 && paramTypesLength == otherParamTypes.length) {
                val typePairs = paramTypes.zip(otherParamTypes)
                // check if there is a combination of parameters which do not match
                val notExactMatch =
                    typePairs.exists {
                        case (left, right) =>
                            !left.isSameType(right)
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

    def isSameMethod(otherMethod: MethodNode): Boolean = {
        otherMethod.qualifiedName match {
          case Some(otherMethodName) => isSameMethod(otherMethodName, otherMethod.getParameterTypes)
          case None => false
        }
    }
}
