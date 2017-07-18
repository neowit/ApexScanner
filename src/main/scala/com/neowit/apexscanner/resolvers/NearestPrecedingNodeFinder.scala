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

import com.neowit.apexscanner.ast.{AstVisitor, AstWalker}
import com.neowit.apexscanner.nodes.{AstNode, Position}

/**
  * Created by Andrey Gavrikov 
  */
class NearestPrecedingNodeFinder(location: Position) extends AstVisitor {
    private var foundNode: Option[AstNode] = None

    override def visit(node: AstNode): Boolean = {
        if (node.range.end.isBefore(location)) {
            foundNode match {
                case Some(previouslyFoundNode) =>
                    val nearest = getNearestPreceding(previouslyFoundNode, node)
                    foundNode = Option(nearest) // new found node is closer to target
                    val continue = previouslyFoundNode != nearest
                    continue
                case None =>
                    foundNode = Option(node)
                    true
            }
        } else if (node.range.includesLocation(location)) {
            true
        } else {
            false
        }
    }

    def findInside(rootNode: AstNode): Option[AstNode] = {
        new AstWalker().walk(rootNode, this)
        foundNode
    }
    def getNearestPreceding(n1: AstNode, n2:AstNode): AstNode = {

        if (n1.range.end.isBefore(location) && n2.range.end.isBefore(location)) {
            // both nodes before target
            if (n1.range.end.line == n2.range.end.line) {
                if (n1.range.end.col < n2.range.end.col) {
                    n2
                } else {
                    n1
                }
            } else if (n1.range.end.isBefore(n2.range.end)) {
                n2
            } else {
                n1
            }
        } else if (n1.range.end.isBefore(location)) {
            n1
        } else {
            n2
        }
    }
}
