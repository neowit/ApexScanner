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

import com.neowit.apex.matchers.MethodMatcher
import com.neowit.apex.nodes._

import scala.annotation.tailrec

object AscendingDefinitionFinder {
    type NodeMatcherFunc = AstNode => Boolean

    def variableMatchFunc(targetName: QualifiedName): NodeMatcherFunc = {
        case node: HasTypeDefinition =>
            node.qualifiedName.exists(_.couldBeMatch(targetName))
        case _ => false
    }

    def methodMatchFunc(targetCaller: MethodCallNode): NodeMatcherFunc = (n: AstNode) => {
        val methodMatcher = new MethodMatcher(targetCaller)
        n match {
            case node: MethodNode =>
                methodMatcher.isSameMethod(node)
            case _ => false
        }
    }
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

    def findDefinition(rootNode: AstNode, location: Position): Seq[AstNode] = {
        // first find actual node which we need to find the definition for
        val nodeFinder = new NodeByLocationFinder(location)
        nodeFinder.findInside(rootNode) match {
          case Some(targetNode)=>
              findDefinition(targetNode, targetNode)
          case None =>
              Seq.empty
        }
    }

    def findDefinition(target: AstNode, startNode: AstNode): Seq[AstNode] = {
        target.getParent(skipFallThroughNodes = true) match {
            case Some(methodCaller: MethodCallNode) =>
                // looks like target is a method call
                val matchFunc = methodMatchFunc(methodCaller)
                findDefinitionInternal(target, methodCaller.methodName, startNode, matchFunc, Seq.empty)
            case _ =>
                target match {
                    case t:IdentifierNode =>
                        val targetName = QualifiedName(Array(t.name))
                        // assume variable
                        val matchFunc = variableMatchFunc(targetName)
                        findDefinitionInternal(target, targetName, startNode, matchFunc, Seq.empty)
                    case _ =>
                        Seq.empty
                }

        }
    }

    /**
      * starting from startNode go UP  the scope (node hierarchy) and look for potential definition of given target node
      * @param target node definition of which we are trying to find
      * @param targetName  QualifiedName of the node we are trying to find
      * @param startNode lowest node to start from and go UP
      * @return
      */
    @tailrec
    private def findDefinitionInternal(target: AstNode, targetName: QualifiedName, startNode: AstNode,
                                       isMatching: AstNode => Boolean, foundNodes: Seq[AstNode]): Seq[AstNode] = {

        startNode.getParent(true) match {
            case Some(parent) =>
                parent.findChildren(n => isMatching(n)) match {
                    case definitionNodes if definitionNodes.nonEmpty =>
                        println(definitionNodes.length)
                        findDefinitionInternal(target, targetName, parent, isMatching, foundNodes ++ definitionNodes)
                    case _ =>
                        // go higher in parents hierarchy
                        findDefinitionInternal(target, targetName, parent, isMatching, foundNodes)
                }
            case None => foundNodes
        }


    }

}
