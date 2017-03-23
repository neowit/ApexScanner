package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.AnnotationContext

case class ApexAnnotation(name: String, params: List[AnnotationParameter], locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = ApexAnnotationNodeType

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        val myText =
        if (params.nonEmpty) {
            name + "(" + params.map(_.getDebugInfo).mkString(",") + ")"
        } else {
            name
        }
        super.getDebugInfo + " " + myText
    }
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
    override def getDebugInfo: String = {
        super.getDebugInfo + " " + name + "=" + value
    }
}

