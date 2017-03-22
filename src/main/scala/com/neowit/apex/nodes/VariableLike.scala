package com.neowit.apex.nodes

trait VariableLike extends AstNode with HasApexDoc {

    private var nameOpt: Option[String] = None

    def setName(name: String): Unit = {
        nameOpt = Option(name)
    }

    def annotations: Seq[ApexAnnotation] = getChildren(ApexAnnotationNodeType).map(_.asInstanceOf[ApexAnnotation])
    def modifiers: Set[Modifier] = getChildren(ModifierNodeType).map(_.asInstanceOf[Modifier]).toSet
    def name: Option[String] = nameOpt
    def dataType: Option[DataType] = getChildren(DataTypeNodeType).headOption.map(_.asInstanceOf[DataType])
    def initExpression: Option[ExpressionNode] = getChildren(ExpressionNodeType).headOption.map(_.asInstanceOf[ExpressionNode])

    override def getApexDoc: Option[ApexDoc] = getChildren(ApexDocNodeType).map(_.asInstanceOf[ApexDoc]).headOption
}

