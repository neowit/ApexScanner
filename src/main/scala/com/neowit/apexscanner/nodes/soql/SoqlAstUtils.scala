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
import com.neowit.apexscanner.nodes.{AstNode, FromNodeType, SoqlQueryNodeType, SubqueryNodeType}

/**
  * Created by Andrey Gavrikov 
  */
object SoqlAstUtils {

    def findFromNode(thisNode: AstNode, aliasOpt: Option[String]): Option[FromNode] = {
        // find parent query or subquery
        val queryNode =
            if (thisNode.nodeType == SubqueryNodeType || thisNode.nodeType == SoqlQueryNodeType) {
                Option(thisNode)
            } else {
                thisNode.findParentInAst(_.nodeType == SubqueryNodeType)
                    .orElse(thisNode.findParentInAst(_.nodeType == SoqlQueryNodeType))
            }

        // find one or more FromNode-s under current query node
        val fromNodes:Seq[FromNode] =
            queryNode match {
                case  Some(queryNode @ SoqlQueryNode(_, _)) =>
                    queryNode.findChildrenInAst(_.nodeType == FromNodeType)
                        .map(_.asInstanceOf[FromNode])
                case  Some(queryNode @ SubqueryNode(_, _)) =>
                    queryNode.findChildrenInAst(_.nodeType == FromNodeType)
                        .map(_.asInstanceOf[FromNode])
                case  _ => Seq.empty
            }

        fromNodes match {
            case nodes if nodes.isEmpty => None
            case nodes => // one or more objects in FROM clause
                aliasOpt.map(_.toLowerCase) match {
                    case Some(alias) =>
                        // alias is provided - match given alias to found FromNode-s
                        nodes.find(_.aliasOpt.exists(_.toLowerCase() == alias))
                    case None => Option(nodes.head)
                }
        }
    }

    /**
      * check if given node is part of Subquery
      */
    def isSubquery(thisNode: AstNode): Boolean = {
        thisNode.nodeType == SubqueryNodeType || thisNode.findParentInAst(_.nodeType == SubqueryNodeType).isDefined
    }

    /**
      * find outer most SOQL Query node
      * @param thisNode node to start from
      * @return
      */
    def findParentFromNode(thisNode: AstNode, aliasOpt: Option[String]): Option[FromNode] = {
        val queryNodeOpt =
            thisNode.findParentInAst(_.nodeType == SubqueryNodeType)
                .orElse(thisNode.findParentInAst(_.nodeType == SoqlQueryNodeType))

        queryNodeOpt match {
            case Some(queryNode) if queryNode.nodeType == SubqueryNodeType=>
                findParentFromNode(queryNode, aliasOpt)
            case Some(queryNode) if queryNode.nodeType == SoqlQueryNodeType=>
                findFromNode(queryNode, aliasOpt)
            case None => None
        }
    }

    /**
      * for outer query this will be name of object in FROM: e.g. "Account"
      * for relationship queries this will be combined name of all FROM-s, e.g. Account.Contacts
      * @param thisNode node which is part of SOQL query or subquery
      * @param aliasOpt e.g. `select a.Name from Account a`
      *                 alias is "a"
      * @return
      */
    def getFullyQualifiedFromName(thisNode: AstNode, aliasOpt: Option[String]): Option[QualifiedName] = {
        def _getFullyQualifiedFromName(thisNode: AstNode, aliasOpt: Option[String], nameSoFar: Option[QualifiedName]): Option[QualifiedName] = {
            findFromNode(thisNode, aliasOpt) match {
                case Some(fromNode) if isSubquery(fromNode)=>
                    val qName =
                        QualifiedName.fromOptions(fromNode.qualifiedName, nameSoFar)

                    fromNode.findParentInAst(n => n.nodeType == SubqueryNodeType || n.nodeType == SoqlQueryNodeType)
                        .flatMap(_.getParentInAst()) match {
                        case Some(queryNode) =>
                            _getFullyQualifiedFromName(queryNode, fromNode.aliasOpt, qName)
                        case None =>
                            qName
                    }
                case Some(fromNode) if !isSubquery(fromNode)=>
                    // drop alias if exists (i.e. covert a.Contacts to Contacts)
                    val nameSoFarWithoutAlias =
                        nameSoFar match {
                            case Some(name) =>
                                if (name.length > 1 && fromNode.aliasOpt.exists(_.toLowerCase == name.getFirstComponent.toLowerCase)) {
                                    name.tailOption
                                } else {
                                    nameSoFar
                                }
                            case _ => nameSoFar
                        }

                    QualifiedName.fromOptions(fromNode.qualifiedName, nameSoFarWithoutAlias)
                case None => nameSoFar
            }

        }
        _getFullyQualifiedFromName(thisNode, aliasOpt, nameSoFar = None)
    }
}
