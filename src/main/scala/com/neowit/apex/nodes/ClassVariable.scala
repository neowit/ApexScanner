package com.neowit.apex.nodes

case class ClassVariable(locationInterval: LocationInterval) extends VariableLike {
    override def nodeType: AstNodeType = ClassVariableNodeType
}
