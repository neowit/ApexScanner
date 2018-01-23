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

package com.neowit.apexscanner.nodes

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
object ArrayIndexExpressionNode {
    /**
      * given full type definition, e.g. myList: List<String>
      * extract "String" and return its type definition
      * @param collectionDef type definition of the whole collection - e.g. List<String>
      * @return
      */
    def getCollectionElementDefinition(collectionDef: IsTypeDefinition, project: Option[Project],
                                       actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        val elemValueTypeOpt =
            collectionDef.getValueType match {
                case Some(_valueType) =>
                    // when we resolve type of element in expression like: coll[1]
                    // we are only interested in the type of single element in this collection, hence _valueType.typeArguments.headOption
                    _valueType.typeArguments.headOption
                case None => None
            }
        elemValueTypeOpt match {
            case Some(_elementValueType) =>
                project.flatMap { project =>
                    val defFinder = new QualifiedNameDefinitionFinder(project)
                    defFinder.findDefinition(_elementValueType.qualifiedName)
                }
            case None =>
                None
        }
    }
}
/**
  * collection[somethingElse]
  * @param collectionNode collection
  * @param indexExprNode somethingElse
  * @param range source location range
  */
case class ArrayIndexExpressionNode(collectionNode: AstNode, indexExprNode: AstNode, range: Range) extends AbstractExpression with LazyLogging {
    /**
      * when we have an expression like
      *     collection[somethingElse]
      * its definition will be whatever is the type of underlying collection
      * e.g.
      *     List<String> collection;
      * value type definition of collection[xxx] will be "System.String"
      */
    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        collectionNode match {
            case collectionExpr: AbstractExpression =>
                collectionExpr.resolveDefinition(actionContext) match {
                    case Some(collectionDefinition: IsTypeDefinition) =>
                        println(collectionDefinition)
                        ArrayIndexExpressionNode.getCollectionElementDefinition(collectionDefinition, collectionNode.getProject, actionContext)
                    case _ => None
                }
            case _ => None
        }
    }

}
