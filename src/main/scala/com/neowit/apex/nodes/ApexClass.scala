package com.neowit.apex.nodes

case class ApexClass(name: String, location: LocationInterval) extends ClassLike {
    override def nodeType: AstNodeType = ApexClassNodeType

}


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