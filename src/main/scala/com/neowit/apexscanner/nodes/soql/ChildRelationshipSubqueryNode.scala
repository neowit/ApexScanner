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

package com.neowit.apexscanner.nodes.soql

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, ChildRelationshipSubqueryNodeType, FromNodeType, IsTypeDefinition, Range, ValueType, ValueTypeSimple}
/**
  * Created by Andrey Gavrikov 
  */
case class ChildRelationshipSubqueryNode(queryStr: String, range: Range) extends AstNode with IsTypeDefinition {
    override def nodeType: AstNodeType = ChildRelationshipSubqueryNodeType

    override def isScope: Boolean = true

    override val qualifiedName: Option[QualifiedName] = Option(new QualifiedName(Array(queryStr)))

    override def getValueType: Option[ValueType] = {
        getFromNodes.toList match {
            case Nil =>
                //return all child relationships
                SoqlAstUtils.findParentFromNode(this, aliasOpt = None) match {
                    case Some(FromNode(Some(parentQualifiedName), _, _)) =>
                        val globalQname = QualifiedName(SoqlQueryNode.LIBRARY_NAME)
                        val qName = QualifiedName(globalQname, parentQualifiedName)
                        Option(ValueTypeSimple(qName))
                    case _ => None
                }
            case fromNode :: Nil =>
                // single node found
                fromNode.getValueType
            case nodes =>
                // multiple object types defined in From clause
                // can not return singular value type
                None

        }
    }

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        Option(this)
    }

    private def getFromNodes: Seq[FromNode] = {
        findChildrenInAst(_.nodeType == FromNodeType).map(_.asInstanceOf[FromNode])
    }
}
