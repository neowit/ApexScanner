package com.neowit.apex.nodes

case class TypeArgumentsNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = TypeArgumentsNodeType
    def components: Seq[FinalNode] = getChildren[FinalNode](FinalNodeType)
    def text:String = if (components.nonEmpty) components.map(_.text).mkString("")  else ""
}
