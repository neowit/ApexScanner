package com.neowit.apex.nodes

case class ApexInterface(locationInterval: LocationInterval ) extends ClassLike {
    override def nodeType: AstNodeType = ApexInterfaceNodeType

}
