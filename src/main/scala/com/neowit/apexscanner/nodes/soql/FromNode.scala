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
import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder

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
            qualifiedName match {
                case Some(_) =>
                    SoqlAstUtils.getFullyQualifiedFromName(this, aliasOpt).map(ValueTypeSimple)
                case None =>
                    // if FROM is empty then return Namespace: "SObjectLibrary" itself, to allow query all its members/SObjects
                    Option(ValueTypeSimple(QualifiedName(SoqlQueryNode.LIBRARY_NAME)))
            }
        }

    }

    override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

    override def isScope: Boolean = true

    // select Id, (SELECT Contact.LastName FROM Account.Contacts) from Account
    // select Id, (SELECT Contact.LastName FROM a.Contacts) from Account a
    // select Id, (SELECT Name from Contacts) from Account
    // select Id, (select cc.Name from a.Contacts cc) from Account a
    private def getChildRelationshipValueType: Option[ValueType] = {

        qualifiedName match {
            case Some(_) =>
                SoqlAstUtils.getFullyQualifiedFromName(this, aliasOpt) match {
                    case Some(fullName) =>
                        // add _Child_Relationships before child relationship name
                        //e.g. Account.Contacts becomes Account._Child_Relationships.Contacts
                        val relationshipName = fullName.components.last

                        if (fullName.length > 1) {
                            val childRelationshipQNameComponents = fullName.components.dropRight(1) ++ Array(SoqlQueryNode.CHILD_RELATIONSHIPS_NODE_NAME)
                            val childRelationshipQName = QualifiedName(childRelationshipQNameComponents)
                            getProject match {
                                case Some(project) =>
                                    val finder = new QualifiedNameDefinitionFinder(project)
                                    //for Account.Contacts return ValueTypeSimple(Contact)
                                    finder.findDefinition(childRelationshipQName) match {
                                        case Some(container: SObjectChildRelationshipContainerNodeBase)  =>
                                            container.findChildByRelationshipName(relationshipName) match {
                                                case Some(valueTypeNode) =>
                                                    valueTypeNode.getValueType
                                                case None => None
                                            }
                                        case _ => None
                                    }
                                case None => None
                            }
                        } else if (1 == fullName.length){
                            // looks like child relationship is NOT provided and we only have parent Object name
                            //e.g. Account becomes Account._Child_Relationships
                            Option(ValueTypeSimple(QualifiedName(fullName.components ++ Array(SoqlQueryNode.CHILD_RELATIONSHIPS_NODE_NAME))))
                        } else {
                            // looks like parent SObject type is also unknown
                            None
                        }

                    case None => None
                }
            case None =>
                SoqlAstUtils.findParentFromNode(this, aliasOpt) match {
                    case Some(FromNode(Some(parentQualifiedName), _, _)) =>
                        //find container by QualifiedName: Account.SoqlQueryNode.CHILD_RELATIONSHIPS_NODE_NAME
                        // this should allow to list all child relationships in "list-completions" part
                        Option(ValueTypeSimple(QualifiedName(parentQualifiedName, SoqlQueryNode.CHILD_RELATIONSHIPS_NODE_NAME)))
                    case _ =>
                        // looks like parent does not have a From <name> either, give up
                        None
                }
        }
    }
}
