/*
 * Copyright (c) 2017 Andrey Gavrikov.
 * this file is part of tooling-force.com application
 * https://github.com/neowit/tooling-force.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neowit.apexscanner.resolvers

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.nodes.{AstNode, ClassLike, ValueType}

import scala.annotation.tailrec

/**
  * Created by Andrey Gavrikov 
  */
trait RelationType
case object Unrelated extends RelationType
case object ChildOf extends RelationType
case object ParentOf extends RelationType
case object SameType extends RelationType

class InheritanceChecker(project: Project, val valueType: ValueType) {
    private val _definitionFinder = new QualifiedNameDefinitionFinder(project)
    private val _thisTypeDefinitionOpt: Option[AstNode] = _definitionFinder.findDefinition(valueType.qualifiedName)

    def getRelation(otherType: ValueType): RelationType = {
        _thisTypeDefinitionOpt match {
            case Some(thisType: ClassLike) =>
                _definitionFinder.findDefinition(otherType.qualifiedName) match {
                    case Some(otherType: ClassLike) =>
                        if (thisType.qualifiedName.exists(_.couldBeMatch(otherType.qualifiedName))) {
                            SameType
                        } else {
                            // check if parent classes of valueType match otherType
                            if (getSuperClassHierarchy(thisType).exists(_.qualifiedName.exists(_.couldBeMatch(otherType.qualifiedName)))) {
                                ChildOf
                            } else if (getSuperClassHierarchy(otherType).exists(_.qualifiedName.exists(_.couldBeMatch(thisType.qualifiedName)))) {
                                // parent classes of otherType match thisType
                                ParentOf

                            } else {
                                Unrelated
                            }
                        }
                    case _ => Unrelated
                }
            case _ => Unrelated
        }
    }

    def getSuperClassHierarchy(classLike: ClassLike): Seq[ClassLike] = {
        @tailrec
        def _getClassHierarchy(cls: ClassLike, clsList: Seq[ClassLike]): Seq[ClassLike] = {
            cls.getSuperClassOrInterface match {
                case Some(superClass) => _getClassHierarchy(superClass, clsList :+ superClass )
                case None => clsList
            }
        }
        _getClassHierarchy(classLike, Seq.empty[ClassLike])
    }

}
