package com.neowit.apex.nodes

case class MethodParameter(name: String, location: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = MethodParameterNodeType
}
