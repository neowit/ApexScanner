package com.neowit.apex.nodes

case class ApexInterface( name: String, location: LocationInterval ) extends ClassLike {
    override def nodeType: AstNodeType = ApexInterfaceNodeType
}
