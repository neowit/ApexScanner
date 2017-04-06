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

import com.neowit.apex.nodes.{MethodNode, QualifiedName}

/**
  * Created by Andrey Gavrikov 
  */
class MethodMatcher(methodName: QualifiedName, paramTypes: Seq[String]) {
    private val paramTypesLower = paramTypes.map(_.toLowerCase())
    private val paramTypesLength = paramTypes.length

//    def isSameMethod(otherMethodName: String, otherParamTypes: Seq[String]): Boolean = {
//        if (methodNameLower == otherMethodName.toLowerCase && paramTypesLength == otherParamTypes.length) {
//            if (0 == paramTypesLength && otherParamTypes.isEmpty) {
//                // target method does not have parameters
//                true
//            } else if (paramTypesLength > 0 && paramTypesLength == otherParamTypes.length) {
//                val typePairs = paramTypesLower.zip(otherParamTypes.map(_.toLowerCase))
//                // check if there is a combination of parameters which do not match
//                val notExactMatch =
//                    typePairs.exists {
//                        case (left, right) =>
//                            left != right && "*" != right && left != "*"
//                    }
//                // found target method if number of parameter match
//                !notExactMatch
//            } else {
//                false
//            }
//        } else {
//            false
//        }
//    }

    /*
      * @param paramTypes list of type names (case insensitive) <br/>
      *                   List("integer", "list❮String❯") <br/>
      *                   List("integer", "*") - "*" means any type of second argument is a match
      *
     */
    def isSameMethod(otherMethodName: QualifiedName, otherParamTypes: Seq[String]): Boolean = {
        // find longer name
        val (leftName, rightName) =
            if (methodName.components.length > otherMethodName.components.length)
                (methodName, otherMethodName)
            else
                (otherMethodName, methodName)
        val nameMatch = leftName.endsWith(rightName)

        if (nameMatch && paramTypesLength == otherParamTypes.length) {
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

    def isSameMethod(otherMethod: MethodNode): Boolean = {
        otherMethod.qualifiedName match {
          case Some(otherMethodName) => isSameMethod(otherMethodName, otherMethod.getParameterTypes)
          case None => false
        }
    }
}
