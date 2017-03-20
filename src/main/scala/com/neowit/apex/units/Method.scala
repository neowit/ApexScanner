package com.neowit.apex.units

case class Method(annotation: Option[ApexAnnotation],
                  modifiers: Set[String],
                  dataType: DataType,
                  name: String,
                  parameters: List[MethodParameter],
                  parentContext: Option[ApexUnit]) extends ApexUnit


