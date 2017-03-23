package com.neowit.apex.nodes

case class FallThroughNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = FallThroughNodeType
}
