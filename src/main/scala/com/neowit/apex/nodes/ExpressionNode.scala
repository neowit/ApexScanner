package com.neowit.apex.nodes

/**
  * Created by Andrey Gavrikov on 23/03/2017.
  */
case class ExpressionNode(locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ExpressionNodeType
}
