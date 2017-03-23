package com.neowit.apex.nodes

case class TypeArgumentsNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = TypeArgumentsNodeType
    def components: Seq[DataTypeBase] =
        getChildren[DataTypeBase](DataTypeNodeType, recursively = true)
    def text:String =
        if (components.nonEmpty) "<" + components.map(_.text).mkString(", ") + ">"  else ""
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
