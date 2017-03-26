package com.neowit.apex.nodes

trait ClassLike extends AstNode with HasApexDoc {

    def name: Option[String] = getChild[IdentifierNode](IdentifierNodeType).map(_.name)
    def annotations: Seq[AnnotationNode] = getChildren[AnnotationNode](AnnotationNodeType)
    def modifiers: Set[ModifierNode] = getChildren[ModifierNode](ModifierNodeType).toSet

    def extendsNode: Option[ExtendsNode] = getChild[ExtendsNode](ExtendsNodeType)
    def extendsText: Option[String] = extendsNode.flatMap(_.dataType.map(_.text))

    def implements: Seq[ImplementsInterfaceNode] = getChildren[ImplementsInterfaceNode](ImplementsInterfaceNodeType)
    def implementsText: Seq[String] = implements.map(_.text)

    override def getApexDoc: Option[DocNode] = getChild[DocNode](DocNodeType)

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        super.getDebugInfo +
            annotations.map(_.getDebugInfo).mkString(" ") + modifiers.map(_.getDebugInfo).mkString(" ") + " "
            name.getOrElse("") + " " + extendsText.map(t => "extends " + t) + " " + implementsText.map(t => "implements " + t)

    }
}
