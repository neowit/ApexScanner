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
import com.neowit.apexscanner.nodes.{AstNode, HasQualifiedName}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
class QualifiedNameDefinitionFinder(project: Project)(implicit ex: ExecutionContext) {
    def findDefinition(target: QualifiedName): Future[Option[AstNode]] = {
        // first try to find by full name
        project.getByQualifiedName(target)  match {
            case nodeOpt @ Some(_) =>
                Future.successful(nodeOpt)
            case None =>
                //go step by step
                findDefinitionFromTop(target) match {
                    case nodeOpt @ Some(_) =>
                        Future.successful(nodeOpt)
                    case None =>
                        // check if given name matches one of existing project files (which has not been scanned into AST yet)
                        findByDocumentName(target)

                }
        }
    }

    // check if given name matches one of existing project files (which has not been scanned into AST yet)
    private def findByDocumentName(target: QualifiedName): Future[Option[AstNode]] = {
        project.getDocumentByName(target.getFirstComponent) match {
            case Some(document) =>
                // generate AST for the document
                project.getAst(document).flatMap{
                    case Some(astBuilderResult) =>
                        // try again, but now we are sure that AST for given file is loaded
                        Future.successful(findDefinitionFromTop(target))
                    case _  => Future.successful(None)
                }
            case None => Future.successful(None)
        }
    }

    private def findDefinitionFromTop(target: QualifiedName): Option[AstNode] = {
        val qName = target.head
        project.getByQualifiedName(qName)  match {
            case nodeOpt @ Some(_) if 1 == target.length =>
                nodeOpt // this was last component in the target
            case Some(containerNode) =>
                //go down one step
                findDefinition(containerNode, target.tailOption)
            case None =>
                None
        }
    }

    def findDefinition(containerNode: AstNode, targetOpt: Option[QualifiedName]): Option[AstNode] = {
        targetOpt match {
            case Some(target) if 1 == target.length=>
                containerNode.findChildInAst{
                    case child: HasQualifiedName =>
                        child.qualifiedName.exists(childQName => childQName.equals(target) || childQName.endsWith(target))
                    case _ => false
                }
            case Some(target) if target.length > 0=>
                val nextChildOpt =
                containerNode.findChildInAst{
                    case child: HasQualifiedName => child.qualifiedName.exists(_.couldBeMatch(target.head))
                    case _ => false
                }
                nextChildOpt match {
                    case Some(nextStepContainerNode) =>
                        findDefinition(nextStepContainerNode, target.tailOption)
                    case None =>
                        None
                }
            case None =>
                Option(containerNode)
        }
    }
}
