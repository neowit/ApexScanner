package com.neowit.apex.nodes

/**
  * Created by Andrey Gavrikov on 23/03/2017.
  */
case class ImplementsInterfaceNode(interfaceWithType: DataType) extends AstNode {
    override def nodeType: AstNodeType = ImplementsInterfaceNodeType

    override def locationInterval: LocationInterval = interfaceWithType.locationInterval
}
