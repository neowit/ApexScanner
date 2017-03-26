package com.neowit.apex.nodes

case class ClassNode(locationInterval: LocationInterval) extends ClassLike {
    override def nodeType: AstNodeType = ClassNodeType

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