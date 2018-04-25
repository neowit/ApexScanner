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

package com.neowit.apexscanner.ast

import com.neowit.apexscanner.nodes.{AstNode, NullNode}
import com.neowit.apexscanner.{Project, VirtualDocument}
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.{ParseTree, RuleNode}

/**
  * Created by Andrey Gavrikov 
  */
trait AstBuilderVisitor {
    def projectOpt: Option[Project]
    def documentOpt: Option[VirtualDocument]
    def tokenStreamOpt: Option[CommonTokenStream]

    def visit(tree: ParseTree): AstNode

    def onComplete(): Unit

    def visitChildren(parent: AstNode, ruleNode: RuleNode): AstNode = {
        for (i <- scala.collection.immutable.Range(0, ruleNode.getChildCount)) {
            val elem = ruleNode.getChild(i)
            val node = visit(elem)
            if (NullNode != node) {
                //node.setParent(parent)
                parent.addChildToAst(node)
            }
        }
        parent
    }
}
