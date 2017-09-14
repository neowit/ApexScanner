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
import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, FromNodeType, IsTypeDefinition, Range, ValueType, ValueTypeSimple}

/**
  * Created by Andrey Gavrikov 
  */
case class FromNode(qualifiedName: Option[QualifiedName], aliasOpt: Option[String], range: Range) extends AstNode with IsTypeDefinition {
    override def nodeType: AstNodeType = FromNodeType


    //SoqlQueryNode.CHILD_RELATIONSHIPS_NODE_NAME
    override def getValueType: Option[ValueType] = {
        // for outer query value type is just its QualifiedName
        // for relationship subquery the value type is a combination of:
        //  - parent query qualifiedName and this node qualified name
        if (SoqlAstUtils.isChildRelationshipSubquery(this)) {
            // child relationship query
            getChildRelationshipValueType
        } else {
            // normal query
            SoqlAstUtils.getFullyQualifiedFromName(this, aliasOpt).map(ValueTypeSimple)
        }

    }

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        qualifiedName match {
            case Some(_) => Option(this)
            case None => None
        }
    }

    override def isScope: Boolean = true

    // select Id, (SELECT Contact.LastName FROM Account.Contacts) from Account
    // select Id, (SELECT Contact.LastName FROM a.Contacts) from Account a
    // select Id, (SELECT Name from Contacts) from Account
    // select Id, (select cc.Name from a.Contacts cc) from Account a
    private def getChildRelationshipValueType: Option[ValueType] = {
        /*
        val (parentQName, thisQName) =
            SoqlAstUtils.findParentFromNode(this, aliasOpt) match {
                case Some(FromNode(Some(_parentQualifiedName), None, _)) =>
                    (_parentQualifiedName, )
                case Some(FromNode(Some(_parentQualifiedName), Some(parentAlias), _)) =>
                // parent has alias

                case _ => None
            }
        */
        ???
    }
}
