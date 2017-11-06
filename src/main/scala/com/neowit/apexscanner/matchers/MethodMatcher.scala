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

package com.neowit.apexscanner.matchers

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{MethodCallNode, MethodNode, ValueType}
import com.neowit.apexscanner.resolvers.{ChildOf, InheritanceChecker, SameType}
import com.neowit.apexscanner.scanner.actions.ActionContext

import scala.collection.mutable

/**
  * Created by Andrey Gavrikov 
  */
class MethodMatcher(methodName: QualifiedName, paramTypes: Seq[ValueType], actionContext: ActionContext, projectOpt: Option[Project]) {
    private val paramTypesLength = paramTypes.length
    private val _inheritanceCheckersCache: mutable.HashMap[ValueType, InheritanceChecker] = new mutable.HashMap[ValueType, InheritanceChecker]()


    def this(methodCallNode: MethodCallNode, actionContext: ActionContext) = {
        this(methodCallNode.methodName, methodCallNode.getParameterTypes(actionContext), actionContext, methodCallNode.getProject)
    }

    /*
      * @param paramTypes list of type names (case insensitive) <br/>
      *                   List("integer", "list❮String❯") <br/>
      *                   List("integer", "*") - "*" means any type of second argument is a match
      *
      * @param withApexConversions if true then (in case if no exact match found) apply check if potential Apex conversions
      *                            e.g. Decimal == Integer
     */
    def isSameMethod(definitionMethodName: QualifiedName, methodDefinitionParamTypes: Seq[ValueType], withApexConversions: Boolean): Boolean = {
        100 == getSimilarityScore(definitionMethodName, methodDefinitionParamTypes, withApexConversions)
    }

    def isSameMethod(methodDefinition: MethodNode, withApexConversions: Boolean): Boolean = {
        100 == getSimilarityScore(methodDefinition, withApexConversions)
    }

    /**
      * check how similar given methodDefinition to its caller signature
      * If method name or number of params do not match then return 0
      * @param methodDefinition - method definition
      * @param withApexConversions if true then (in case if no exact match found) apply check if potential Apex conversions
      *                            e.g. Decimal == Integer
      * @return
      *        0 - if name does not match or none of parameters match or parameter count does not match
      *        nn - if name matches and some of the parameters match
      */
    def getSimilarityScore(methodDefinition: MethodNode, withApexConversions: Boolean): Integer = {
        methodDefinition.qualifiedName match {
            case Some(definitionMethodName) => getSimilarityScore(definitionMethodName, methodDefinition.getParameterTypes, withApexConversions)
            case None => 0 // names can not be matched
        }
    }

    /**
      * check how similar given methodDefinition to its caller signature
      * If method name or number of params do not match then return 0
      * @param definitionMethodName - method definition - name
      * @param methodDefinitionParamTypes - method definition - parameters
      * @param withApexConversions if true then (in case if no exact match found) apply check if potential Apex conversions
      *                            e.g. Decimal == Integer
      * @return
      *        0 - if name does not match or none of parameters match or parameter count does not match
      *        nn - if name matches and some of the parameters match
      */
    def getSimilarityScore(definitionMethodName: QualifiedName, methodDefinitionParamTypes: Seq[ValueType], withApexConversions: Boolean): Integer = {
        val nameMatch = methodName.couldBeMatch(definitionMethodName)

        if (nameMatch && paramTypesLength == methodDefinitionParamTypes.length) {
            if (0 == paramTypesLength && methodDefinitionParamTypes.isEmpty) {
                // target method does not have parameters
                100
            } else if (paramTypesLength > 0 && paramTypesLength == methodDefinitionParamTypes.length) {
                val typePairs = paramTypes.zip(methodDefinitionParamTypes)
                // check if there is a combination of parameters which do not match
                val countOfMatchingParamTypes =
                    typePairs.count{
                        case (callerParam, definitionParam) =>
                            isSameType(callerParam, definitionParam, withApexConversions)
                    }
                // found target method if number of parameter match
                100 * countOfMatchingParamTypes / paramTypesLength
            } else {
                0
            }
        } else {
            0
        }
    }

    def isSameType(callerParam: ValueType, definitionParam: ValueType, withTypeModifications: Boolean):Boolean = {
        if (callerParam.isSameType(definitionParam, withTypeModifications)) {
            true
        } else if (withTypeModifications) {
            getInheritanceChecker(callerParam) match {
                case Some(checker) =>
                    checker.getRelation(definitionParam) match {
                        case SameType => true
                        case ChildOf => true
                        case _ => false
                    }
                case None => false // inheritance checker can not be created
            }
        } else {
            false // no match, even with type conversion & inheritance
        }
    }

    private def getInheritanceChecker(valueType: ValueType): Option[InheritanceChecker] = {
        projectOpt match {
            case Some(project) =>
                _inheritanceCheckersCache.get(valueType) match {
                    case checkerOpt @ Some(_) => checkerOpt
                    case None =>
                        val checker = new InheritanceChecker(project, valueType)
                        _inheritanceCheckersCache += valueType -> checker
                        Option(checker)
                }
            case None => None
        }
    }
}
