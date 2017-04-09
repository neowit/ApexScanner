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

package com.neowit.apex.resolvers

import com.neowit.apex.nodes._

import scala.annotation.tailrec

object AscendingDefinitionFinder {
    type NodeMatcherFunc = HasTypeDefinition => Boolean
}
/**
  * Created by Andrey Gavrikov
  *
  * attempts to find node defining expression at specified location
  * this is ASCENDING lookup, starts from the bottom and goes up
  * Use this to find definition in the same file as target node
  */
class AscendingDefinitionFinder() {
    import AscendingDefinitionFinder._

    def findDefinition(rootNode: AstNode, location: Position): Option[AstNode] = {
        // first find actual node which we need to find the definition for
        val nodeFinder = new NodeByLocationFinder(location)
        nodeFinder.findInside(rootNode) match {
          case Some(targetNode) if targetNode.getParent(skipFallThroughNodes = true).exists(_.nodeType == MethodCallNodeType)=>
              // method call
              findDefinitionInternal(targetNode, targetNode) //TODO add appropriate matcher
          case Some(targetNode) =>
              // variable
              findDefinitionInternal(targetNode, targetNode) //TODO add appropriate matcher
          case None =>
                None
        }
    }

    def findDefinition(target: AstNode, startNode: AstNode): Option[AstNode] = {
        findDefinitionInternal(target, startNode)
    }

    /**
      * starting from startNode go UP  the scope (node hierarchy) and look for potential definition of given target node
      * @param target node which definition we are trying to find
      * @param startNode lowest node to start from and go UP
      * @return
      */
    @tailrec
    private def findDefinitionInternal(target: AstNode, startNode: AstNode): Option[AstNode] = {
        val targetName = target match {
            case t:IdentifierNode =>
                Option(QualifiedName(Array(t.name)))
            case _ =>
                None
        }
        target match {
            case n: LocalVariableNode =>
                Option(n)
            case n: ClassVariableNode  =>
                Option(n)
            case n: MethodNode =>
                Option(n) //TODO - for methods we need to check method parameter types as well
            case _ => startNode.getParent(true) match {
              case Some(parent) =>
                  val definitionNode =
                      parent.findChild{
                          case child: HasTypeDefinition =>
                              targetName.exists(qName => child.qualifiedName.exists(_.couldBeMatch(qName)))
                          case _ => false
                      }
                  definitionNode match {
                    case Some(_) => definitionNode
                    case None =>
                        // go higher in parents hierarchy
                        findDefinitionInternal(target, parent)
                  }
              case None => None
            }
        }
    }
    private def findVariableDefinition(target: AstNode, startNode: AstNode): Option[AstNode] = {
        ???
    }

    private def findMethodDefinition(target: AstNode, startNode: AstNode): Option[AstNode] = {
        ???
    }

    //TODO
    private def getVariableMatcher(targetName: QualifiedName): NodeMatcherFunc = {
        (node: HasTypeDefinition) => node.qualifiedName.exists(_.couldBeMatch(targetName))
    }
}
