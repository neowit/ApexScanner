package com.neowit.apex.ast

import com.neowit.apex.nodes.AstNode

class AstWalker {

    def walk(node: AstNode, visitor: AstVisitor): Unit = {
        val continue = visitor.visit(node)
        if (continue) {
            node.children.foreach(walk(_, visitor))
        }
    }
}

trait AstVisitor {
    def visit(node: AstNode): Boolean
}

class DebugVisitor extends AstVisitor {
    override def visit(node: AstNode): Boolean = {
        println(node.getDebugInfo)
        true
    }
}
