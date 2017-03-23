package com.neowit.apex.nodes

case class Method(name: String, locationInterval: LocationInterval ) extends AstNode with HasApexDoc {
    override def nodeType: AstNodeType = MethodNodeType

    def getApexDoc: Option[ApexDoc] = getChildren(ApexDocNodeType).map(_.asInstanceOf[ApexDoc]).headOption

    override def getDebugInfo: String = super.getDebugInfo + " TODO"
}


