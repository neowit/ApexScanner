package com.neowit.apex.units

case class MethodParameter(modifiers: Set[String], dataType: DataType, name: String, parentContext: Option[ApexUnit]) extends ApexUnit
