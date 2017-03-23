package com.neowit.apex.nodes

case class DataType(qualifiedName: QualifiedNameNode, typeArgumentsOpt: Option[TypeArgumentsNode], locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = DataTypeNodeType
    def text: String = qualifiedName.text + typeArgumentsOpt.map(_.text).getOrElse("")
}


