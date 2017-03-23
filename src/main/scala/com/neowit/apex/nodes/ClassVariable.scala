package com.neowit.apex.nodes

case class ClassVariable(locationInterval: LocationInterval) extends VariableLike {
    override def nodeType: AstNodeType = ClassVariableNodeType

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = super.getDebugInfo + " " + "TODO"
}
