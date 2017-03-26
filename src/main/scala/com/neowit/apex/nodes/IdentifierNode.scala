package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.ClassDeclarationContext

case class IdentifierNode(name: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = IdentifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + name
}

object IdentifierNode {
    def apply(ctx: ClassDeclarationContext): IdentifierNode = {
        if (null != ctx.className()) {
            IdentifierNode(ctx.className().getText, LocationInterval(ctx.className()))
        } else {
            throw new UnsupportedOperationException("ClassDeclarationContext without className was Encountered")
        }
    }
}
