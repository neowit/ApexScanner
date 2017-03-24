package com.neowit.apex.nodes

case class QualifiedNameNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = QualifiedNameNodeType
    def text: String = getChildren[IdentifierNode](IdentifierNodeType).map(_.name).mkString(".")
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
