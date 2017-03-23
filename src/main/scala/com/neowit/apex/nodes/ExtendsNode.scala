package com.neowit.apex.nodes

case class ExtendsNode(dataType: DataType) extends AstNode {
    override def nodeType: AstNodeType = ExtendsNodeType

    override def locationInterval: LocationInterval = dataType.locationInterval
}
