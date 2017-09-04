package com.neowit.apexscanner.nodes.soql

import com.neowit.apexscanner.nodes.{AstNode, FromNodeType, SoqlQueryNodeType, SubqueryNodeType}

/**
  * Created by Andrey Gavrikov 
  */
object SoqlAstUtils {

    def findFromNode(thisNode: AstNode, aliasOpt: Option[String]): Option[FromNode] = {
        // find parent query or subquery
        val queryNode =
            thisNode.findParentInAst(_.nodeType == SubqueryNodeType)
                .orElse(thisNode.findParentInAst(_.nodeType == SoqlQueryNodeType))

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
}
