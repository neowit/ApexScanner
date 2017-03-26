package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.ClassOrInterfaceVisibilityModifierContext


case class ModifierNode(`type`: ModifierNode.ModifierType, locationInterval: LocationInterval) extends AstNode {

    override def nodeType: AstNodeType = ModifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + `type`
}

object ModifierNode {

    sealed trait ModifierType

    trait Visibility extends ModifierType
    case object PUBLIC extends Visibility
    case object PRIVATE extends Visibility
    case object PROTECTED extends Visibility
    case object GLOBAL extends Visibility

    case object FINAL extends ModifierType
    case object STATIC extends ModifierType
    case object TRANSIENT extends ModifierType
    case object WEBSERVICE extends ModifierType

    case object ABSTRACT extends ModifierType
    case object VIRTUAL extends ModifierType

    def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): AstNode = {
        if (null != ctx.ABSTRACT()) {
            return ModifierNode(ModifierNode.ABSTRACT, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, LocationInterval(ctx.GLOBAL()))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, LocationInterval(ctx.PRIVATE()))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, LocationInterval(ctx.PUBLIC()))
        }
        if (null != ctx.VIRTUAL()) {
            return ModifierNode(ModifierNode.VIRTUAL, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.WEBSERVICE()) {
            return ModifierNode(ModifierNode.WEBSERVICE, LocationInterval(ctx.WEBSERVICE()))
        }
        NullNode
    }
}
