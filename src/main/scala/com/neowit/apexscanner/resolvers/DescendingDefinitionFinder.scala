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
import com.neowit.apexscanner.nodes._
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
      */
    def findDefinition(target: AstNode, containerNode: AstNode): Seq[AstNode] = {
        val targetNameOpt =
            target match {
                case _target:HasQualifiedName =>
                    _target.qualifiedName
                case _target: IdentifierNode =>
                    val qName = QualifiedName(Array(_target.name))
                    Option(qName)
                case _target =>
                    throw new NotImplementedError("DescendingDefinitionFinder.findDefinition for node + " + _target.nodeType +" is not implemented")
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
                        case child:IsTypeDefinition =>
                            child.qualifiedName match {
                                case Some(childName) => targetName.couldBeMatch(childName)
                                case None => false
                            }
                        case _ => false
                    }

                containerNodeChildOpt match {
                    case Some(foundInAst) => Seq(foundInAst)
                    case None =>
                        containerNode match {
                            case _containerNode: IsTypeDefinition =>
                                // check if target is inside container valueType node
                                // e.g. if container is variable definition with value type = Class
                                _containerNode.getValueType match {
                                    case Some(typeDef) =>
                                        project.getByQualifiedName(QualifiedName.getFullyQualifiedValueTypeName(_containerNode)) match {
                                            case Some(_container: IsTypeDefinition) if containerNode != _container=>
                                                // above have to check containerNode != _container to make sure we do not get stuck in infinite loop
                                                findDefinition(target, _container)
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

}
