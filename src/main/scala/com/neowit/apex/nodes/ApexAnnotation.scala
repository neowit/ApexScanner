package com.neowit.apex.nodes

case class ApexAnnotation(name: String, params: List[AnnotationParameter], location: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ApexAnnotationNodeType
}

case class AnnotationParameter(name: String, value: String, location: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
}

