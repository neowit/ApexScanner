package com.neowit.apex.nodes

case class ApexDoc(text: String, location: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ApexDocNodeType
}
