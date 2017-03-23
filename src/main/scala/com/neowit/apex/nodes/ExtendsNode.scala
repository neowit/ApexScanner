package com.neowit.apex.nodes

case class ExtendsNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ExtendsNodeType
    def dataType: Option[DataTypeBase] = getChild[DataTypeBase](DataTypeNodeType)
    def text: String = dataType.map(_.text).getOrElse("")
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
