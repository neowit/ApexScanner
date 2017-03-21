package com.neowit.apex.units


/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
case class Modifiers(modifiers: Modifiers.ModifierSet) extends ApexUnit {
    override val parentContext: Option[ApexUnit] = None
}
object Modifiers {
    type ModifierSet = Set[Modifiers.Modifier]
    sealed trait Modifier

    trait Visibility extends Modifier
    case object PUBLIC extends Visibility
    case object PRIVATE extends Visibility
    case object PROTECTED extends Visibility
    case object GLOBAL extends Visibility

    case object FINAL extends Modifier
    case object STATIC extends Modifier
    case object TRANSIENT extends Modifier
    case object WEBSERVICE extends Modifier

    case object ABSTRACT extends Modifier
    case object VIRTUAL extends Modifier
}
