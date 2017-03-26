package com.neowit.apex.nodes

case class MethodParameterNode(name: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = MethodParameterNodeType

    override def getDebugInfo: String = super.getDebugInfo + " TODO"
}
