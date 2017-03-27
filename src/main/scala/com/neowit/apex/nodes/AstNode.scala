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

package com.neowit.apex.nodes

trait AstNode {

    private var _parent: Option[AstNode] = None
    private val _children = new scala.collection.mutable.ListBuffer[AstNode]()

    def locationInterval:LocationInterval
    def nodeType: AstNodeType

    def setParent(parent: AstNode): AstNode = {
        _parent = Option(parent)
        parent
    }

    def getParent: Option[AstNode] = _parent

    /**
      * find parent matching specified condition
      * @param filter - condition
      * @return
      */
    def findParent(filter: (AstNode) => Boolean): Option[AstNode] = {
        getParent match {
            case Some(parentMember) =>
                if (filter(parentMember)) Some(parentMember) else parentMember.findParent(filter)
            case None => None
        }
    }

    def addChild(node: AstNode): AstNode = {
        if (NullNodeType != node.nodeType) {
            _children += node
            node.setParent(this)
        }
        node
    }

    def children: Seq[AstNode] = _children

    def getChildren[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Seq[T] = {
        val immediateChildren = _children.filter(_.nodeType == nodeType)

        // in case any of the children represent a FallThroughNode, query their children one step further
        val childrenViaFallThroughNodes =
            if (FallThroughNodeType != nodeType && !recursively) {
                _children.filter(_.nodeType == FallThroughNodeType).flatMap(_.getChildren[FallThroughNode](nodeType))
            } else {
                Nil
            }

        val immediateAndRecursiveChildren =
            if (recursively) {
                immediateChildren ++ immediateChildren.flatMap(_.getChildren(nodeType, recursively))
            } else {
                immediateChildren
            }

        val allChildren = childrenViaFallThroughNodes ++ immediateAndRecursiveChildren

        allChildren.map(_.asInstanceOf[T])
    }
    def getChild[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Option[T] = {
        getChildren[T](nodeType, recursively).headOption
    }

    /**
      * used for debug purposes
      * @return textual representation of this node and its children
      */
    def getDebugInfo: String = {
        val shift = List.fill(getLevelsDeep)("\t").mkString("")
        "\n" + shift + locationInterval.detDebugInfo + " " + nodeType + " => "
    }
    // used in getDebugInfo for visual shift of child level compared to parent
    private var levelsDeep: Int = -1
    protected def getLevelsDeep: Int = {
        if (levelsDeep < 0) {
            levelsDeep =
                getParent match {
                    case Some(p) => p.getLevelsDeep + 1
                    case None => 0
                }
        }
        levelsDeep
    }
}


