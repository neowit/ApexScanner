package com.neowit.apex.nodes

object NullNode extends AstNode {
    override def locationInterval: LocationInterval = LocationInterval.INVALID_LOCATION

    override def nodeType: AstNodeType = NullNodeType
    override def getDebugInfo: String = super.getDebugInfo
}
