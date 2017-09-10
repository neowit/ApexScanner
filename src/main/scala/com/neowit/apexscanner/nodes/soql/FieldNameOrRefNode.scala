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
import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, FieldNameOrRefNodeType, HasTypeDefinition, Range}
import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder

/**
  * Created by Andrey Gavrikov 
  */
case class FieldNameOrRefNode(qualifiedName: QualifiedName, range: Range) extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = FieldNameOrRefNodeType

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        // assume first component of qualifiedName is alias
        val maybeAliasName = qualifiedName.getFirstComponent
        val fromObjectRefOpt =
            SoqlAstUtils.findFromNode(this, Option(maybeAliasName)) match {
                case Some(FromNode(objectRef, _, _)) => Option(objectRef)
                case None =>
                    // looks like first component of QualifiedName is not an alias
                    SoqlAstUtils.findFromNode(this, aliasOpt = None) match {
                        case Some(FromNode(objectRef, _, _)) => Option(objectRef)
                        case None => None
                    }
            }

        fromObjectRefOpt  match {
            case Some(fromObjectRef) =>
                val qName = QualifiedName.fromOptions(fromObjectRef, Option(qualifiedName))
                getProject match {
                    case Some(_project) =>
                        val finder = new QualifiedNameDefinitionFinder(_project)
                        qName.flatMap(finder.findDefinition(_))
                    case None => None
                }

                //getProject.flatMap(_.getByQualifiedName(fromObjectRef))
            case None => None
        }
    }
}
