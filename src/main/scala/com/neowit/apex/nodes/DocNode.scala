package com.neowit.apex.nodes

case class DocNode(text: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = DocNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
