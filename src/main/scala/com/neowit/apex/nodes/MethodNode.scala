package com.neowit.apex.nodes

case class MethodNode(name: String, locationInterval: LocationInterval ) extends AstNode with HasApexDoc {
    override def nodeType: AstNodeType = MethodNodeType

    def getApexDoc: Option[DocNode] = getChildren(DocNodeType).map(_.asInstanceOf[DocNode]).headOption

    override def getDebugInfo: String = super.getDebugInfo + " TODO"
}


