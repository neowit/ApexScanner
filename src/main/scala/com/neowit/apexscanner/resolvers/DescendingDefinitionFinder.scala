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

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.matchers.MethodMatcher
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.scanner.actions.ActionContext
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
class DescendingDefinitionFinder(project: Project, actionContext: ActionContext) extends LazyLogging {
    /**
      * try to find definition of target inside containerNode
      * target can be
      * - class variable
      * - class property
      * - SObject field
      * - class method
      * - enum
      *
      * @param containerValueTypeOpt value type of parent container context
      *                              e.g.
      *                              List<Type1> myList;
      *                              list.get(0)
      *                              - containerValueTypeOpt: Some(List<Type1>)
      *                              this value type helps to resolve method return type for List, Set, Map classes
      */
    def findDefinition(target: AstNode, containerNode: AstNode, containerValueTypeOpt: Option[ValueType] = None): Seq[AstNode] = {
        var methodMatcher: Option[MethodMatcher] = None // this may be defined later if target is MethodCallNode
        val targetNameOpt: Option[QualifiedName] =
            target match {
                case _target:MethodCallNode =>
                    // method call nodes need special treatment because there may be more than 1 method with same name
                    // so looking by just qualified name is not enough
                    methodMatcher = Option(new MethodMatcher(_target, actionContext))
                    _target.qualifiedName
                case _target:HasQualifiedName =>
                    _target.qualifiedName
                case _target: IdentifierNode =>
                    val qName = QualifiedName(Array(_target.name))
                    Option(qName)
                case _target =>
                    throw new NotImplementedError("DescendingDefinitionFinder.findDefinition for node " + _target.nodeType +" is not implemented")
            }
        val result =
        targetNameOpt match {
            case Some(targetName) =>
                // check if target is inside container node

                // for different container types use different "find" methods
                val _find = containerNode match {
                    case _containerNode: ClassLike =>
                        logger.trace("use findChildInHierarchy")
                        _containerNode.findChildInAstThenHierarchy _
                    case _containerNode =>
                        logger.trace("use findChildInAst")
                        _containerNode.findChildInAst _
                }
                val containerNodeChildOpt =
                    _find{
                        case _child: MethodNode if methodMatcher.isDefined =>
                            // for methods - compare qualified name as well as parameter types
                            methodMatcher.exists(_.isSameMethod(_child, withApexConversions = true))
                        case child:IsTypeDefinition =>
                            child.qualifiedName match {
                                case Some(childName) =>
                                    val vt = child.getValueType
                                    logger.trace("child.valueType: " + vt)
                                    targetName.couldBeMatch(childName)
                                case None => false
                            }
                        case _ => false
                    }

                containerNodeChildOpt match {
                    case Some(foundInAst: MethodNodeBase) if !foundInAst.isValueTypeProvided =>
                        // attempt special treatment if this is a method of collection like List, Map, Set
                        setCollectionMethodReturnType(containerValueTypeOpt, foundInAst)
                        Seq(foundInAst)
                    case Some(foundInAst) =>
                        Seq(foundInAst)
                    case None =>
                        containerNode match {
                            case _containerNode: IsTypeDefinition =>
                                // check if target is inside container valueType node
                                // e.g. if container is variable definition with value type = Class
                                _containerNode.getValueType match {
                                    case Some(_containerValueType) =>
                                        project.getByQualifiedName(QualifiedName.getFullyQualifiedValueTypeName(_containerNode)) match {
                                            case Some(_container: IsTypeDefinition) if containerNode != _container=>
                                                // above have to check containerNode != _container to make sure we do not get stuck in infinite loop
                                                findDefinition(target, _container, Option(_containerValueType))
                                            case _ =>
                                                Seq.empty
                                        }

                                    case None => Seq.empty
                                }
                            case _ => Seq.empty
                        }
                }
            case None => Seq.empty
        }
        result
    }

    private def setCollectionMethodReturnType(containerValueTypeOpt: Option[ValueType], methodFoundInAst: MethodNodeBase): Unit = {
        // check if this is a method of collection like List, Map, Set
        val methodName = methodFoundInAst.qualifiedName.map(_.getLastComponent).getOrElse("unsupported").toLowerCase
        containerValueTypeOpt match {
            case Some(valueType @ ValueTypeComplex(_qualifiedName, _typeArguments)) =>
                valueType.qualifiedName.getLastComponent.toUpperCase() match {
                    case "MAP"  =>
                        methodName match {
                            case "get" => methodFoundInAst.setValueType(_typeArguments.last)
                            case "put" => methodFoundInAst.setValueType(_typeArguments.last)
                            case "remove" => methodFoundInAst.setValueType(_typeArguments.last)
                            case "values" => methodFoundInAst.setValueType(ValueTypeComplex(QualifiedName("List"), Seq(_typeArguments.last)))
                            case "clone" => methodFoundInAst.setValueType(valueType)
                            case "deepclone" => methodFoundInAst.setValueType(valueType)
                            case "keySet" => methodFoundInAst.setValueType(ValueTypeComplex(QualifiedName("Set"), Seq(_typeArguments.head)))
                            case _ =>
                        }
                    case "SET"  =>
                        methodName match {
                            case "clone" => methodFoundInAst.setValueType(valueType)
                            case _ =>
                        }
                    case "LIST"  =>
                        methodName match {
                            case "get" => methodFoundInAst.setValueType(_typeArguments.head)
                            case "clone" => methodFoundInAst.setValueType(valueType)
                            case "deepclone" => methodFoundInAst.setValueType(valueType)
                            case "remove" => methodFoundInAst.setValueType(_typeArguments.head)
                            case _ =>
                        }
                }
            case _ =>
        }
    }
}
