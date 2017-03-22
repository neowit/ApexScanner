package com.neowit.apex.nodes

case class Variable( name: String, location: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = VariableNodeType
}


