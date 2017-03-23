package com.neowit.apex.nodes

case class FinalNode(text: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = FinalNodeType

}
