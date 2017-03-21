package com.neowit.apex.units

case class Variable(apexAnnotation: Option[ApexAnnotation],
                    modifiers: Modifiers.ModifierSet,
                    dataType: DataType,
                    name: String,
                    parentContext: Option[ApexUnit]) extends ApexUnit


