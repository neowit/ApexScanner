package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.ClassDeclarationContext

/**
  * Created by Andrey Gavrikov on 23/03/2017.
  */
case class IdentifierNode(name: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = IdentifierNodeType
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
