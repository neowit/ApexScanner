package com.neowit.apex.nodes

trait ClassLike extends AstNode with HasApexDoc {
    def annotations: List[ApexAnnotation] = getChildren(ApexAnnotationNodeType).map(_.asInstanceOf[ApexAnnotation])
    def name: String
    def modifiers: Set[Modifier] = getChildren(ModifierNodeType).map(_.asInstanceOf[Modifier]).toSet


    override def getApexDoc: Option[ApexDoc] = getChildren(ApexDocNodeType).map(_.asInstanceOf[ApexDoc]).headOption

    //def superTypeOpt: Option[ClassLike]
    //def implements: List[ClassLike]
}
