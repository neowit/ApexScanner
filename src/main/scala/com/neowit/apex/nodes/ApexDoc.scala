package com.neowit.apex.nodes

case class ApexDoc(text: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ApexDocNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
