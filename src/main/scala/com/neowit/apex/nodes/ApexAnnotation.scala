package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.AnnotationContext

case class ApexAnnotation(name: String, params: List[AnnotationParameter], locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ApexAnnotationNodeType
}

object ApexAnnotation {
    def visitAnnotation(ctx: AnnotationContext): AstNode = {
        val annotation = ApexAnnotation(name = ctx.annotationName().getText, params = Nil, LocationInterval(ctx))
        //TODO define annotation params
        annotation
    }
}

case class AnnotationParameter(name: String, value: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
}

