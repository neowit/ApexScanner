package com.neowit.apex.nodes

import com.neowit.apex.scanner.antlr.ApexcodeParser.ClassOrInterfaceVisibilityModifierContext


/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
case class Modifier(`type`: Modifier.ModifierType, locationInterval: LocationInterval) extends AstNode {

    override def nodeType: AstNodeType = ModifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + `type`
}

object Modifier {

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
            return Modifier(Modifier.ABSTRACT, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.GLOBAL()) {
            return Modifier(Modifier.GLOBAL, LocationInterval(ctx.GLOBAL()))
        }
        if (null != ctx.PRIVATE()) {
            return Modifier(Modifier.PRIVATE, LocationInterval(ctx.PRIVATE()))
        }
        if (null != ctx.PUBLIC()) {
            return Modifier(Modifier.PUBLIC, LocationInterval(ctx.PUBLIC()))
        }
        if (null != ctx.VIRTUAL()) {
            return Modifier(Modifier.VIRTUAL, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.WEBSERVICE()) {
            return Modifier(Modifier.WEBSERVICE, LocationInterval(ctx.WEBSERVICE()))
        }
        NullNode
    }
}
