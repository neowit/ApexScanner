package com.neowit.apex.units

case class ApexAnnotation(name: String, params: List[AnnotationParameter], parentContext: Option[ApexUnit]) extends ApexUnit

case class AnnotationParameter(name: String, value: String, parentContext: Option[ApexUnit]) extends ApexUnit

