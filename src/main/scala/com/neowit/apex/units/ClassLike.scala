package com.neowit.apex.units

trait ClassLike extends ApexUnit{
    def annotationOpt: Option[ApexAnnotation]
    def name: String
    def modifiers: Modifiers
    def superTypeOpt: Option[ClassLike]
    def implements: List[ClassLike]
}
