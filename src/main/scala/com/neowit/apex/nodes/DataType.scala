package com.neowit.apex.nodes

trait DataTypeBase extends AstNode {

    override def nodeType: AstNodeType = DataTypeNodeType
    def text: String
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
case class DataType(qualifiedName: QualifiedNameNode, typeArgumentsOpt: Option[TypeArgumentsNode],
                    locationInterval: LocationInterval) extends DataTypeBase {
    def text: String =
        qualifiedName.text +
            typeArgumentsOpt.map(_.text).getOrElse("")

}

//VOID
case class DataTypeVoid(locationInterval: LocationInterval) extends DataTypeBase {
    override def text: String = "void"
}

//Some[] - array type
case class DataTypeArray(qualifiedName: QualifiedNameNode, locationInterval: LocationInterval) extends DataTypeBase {
    def text: String = qualifiedName.text + "[]"
}


