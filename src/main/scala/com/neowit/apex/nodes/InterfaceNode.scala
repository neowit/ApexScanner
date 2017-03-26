package com.neowit.apex.nodes

case class InterfaceNode(locationInterval: LocationInterval ) extends ClassLike {
    override def nodeType: AstNodeType = InterfaceNodeType

}
