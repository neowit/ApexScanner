package com.neowit.apex.units


case class ApexClass(annotationOpt: Option[ApexAnnotation],
                     modifiers: Modifiers,
                     name: String,
                     superTypeOpt: Option[ClassLike],
                     implements: List[ClassLike],
                     parentContext: Option[ApexUnit]) extends ClassLike


/*
object ApexClass{
    def apply(ctx: ClassDefContext): ApexClass = {
        var annotationOpt = None
        val modifierSetBuilder = Set.newBuilder[Modifiers.Modifier]

        ctx.children.iterator().forEachRemaining{
            case elem: ClassOrInterfaceModifierContext =>

            case elem: ClassDeclarationContext =>
            case elem => throw new NotImplementedError("Unsupported element: " + elem)

        }
        ???
    }
}
*/