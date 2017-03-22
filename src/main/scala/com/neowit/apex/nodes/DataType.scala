package com.neowit.apex.nodes

//TODO - make more detailed, e.g. Map<String, List<Integer>>
case class DataType(value: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = DataTypeNodeType
}


