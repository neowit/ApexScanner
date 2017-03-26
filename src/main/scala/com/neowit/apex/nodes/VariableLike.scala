package com.neowit.apex.nodes

trait VariableLike extends AstNode with HasApexDoc {

    private var nameOpt: Option[String] = None

    def setName(name: String): Unit = {
        nameOpt = Option(name)
    }

    def annotations: Seq[AnnotationNode] = getChildren(AnnotationNodeType).map(_.asInstanceOf[AnnotationNode])
    def modifiers: Set[ModifierNode] = getChildren(ModifierNodeType).map(_.asInstanceOf[ModifierNode]).toSet
    def name: Option[String] = nameOpt
    def dataType: Option[DataType] = getChildren(DataTypeNodeType).headOption.map(_.asInstanceOf[DataType])
    def initExpression: Option[ExpressionNode] = getChildren(ExpressionNodeType).headOption.map(_.asInstanceOf[ExpressionNode])

    override def getApexDoc: Option[DocNode] = getChildren(DocNodeType).map(_.asInstanceOf[DocNode]).headOption
}

