package com.neowit.apex.units

case class ApexClass(annotationOpt: Option[ApexAnnotation],
                     modifiers: ModifierSet,
                     name: String,
                     superTypeOpt: Option[ClassLike],
                     implements: List[ClassLike],
                     parentContext: Option[ApexUnit]) extends ClassLike


