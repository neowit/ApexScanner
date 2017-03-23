package com.neowit.apex.nodes

/**
  * Created by Andrey Gavrikov on 23/03/2017.
  */
case class ImplementsInterfaceNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ImplementsInterfaceNodeType

    def dataType: Seq[DataTypeBase] = getChildren[DataTypeBase](DataTypeNodeType)
    def text: String = dataType.map(_.text).mkString(",")
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
