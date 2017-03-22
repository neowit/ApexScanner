package com.neowit.apex.nodes


/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
case class Modifier(`type`: Modifier.ModifierType, location: LocationInterval) extends AstNode {

    override def nodeType: AstNodeType = ModifierNodeType
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
}
