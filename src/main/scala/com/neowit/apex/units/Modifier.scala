package com.neowit.apex.units


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

