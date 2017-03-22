package com.neowit.apex.nodes

trait ClassLike extends AstNode with HasApexDoc {

    def name: Option[String] = getChild[IdentifierNode](IdentifierNodeType).map(_.name)
    def annotations: Seq[ApexAnnotation] = getChildren[ApexAnnotation](ApexAnnotationNodeType)
    def modifiers: Set[Modifier] = getChildren[Modifier](ModifierNodeType).toSet


    override def getApexDoc: Option[ApexDoc] = getChild[ApexDoc](ApexDocNodeType)

    //def superTypeOpt: Option[ClassLike]
    //def implements: List[ClassLike]
}
