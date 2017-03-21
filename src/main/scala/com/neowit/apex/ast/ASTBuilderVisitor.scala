package com.neowit.apex.ast

import com.neowit.apex.scanner.antlr.ApexcodeBaseVisitor
import com.neowit.apex.scanner.antlr.ApexcodeParser._
import com.neowit.apex.units.Modifiers.Modifier
import com.neowit.apex.units._

/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
class ASTBuilderVisitor extends ApexcodeBaseVisitor[ApexUnit] {
    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassDef(ctx: ClassDefContext): ApexUnit = {
        //val x = ctx.classOrInterfaceModifier(0)
        var modifiers: Modifiers = Modifiers(Set.empty)
        var annotationOpt: Option[ApexAnnotation] = None
        var classNameOpt: Option[String] = None

        ctx.children.iterator().forEachRemaining{
            case elem: ClassOrInterfaceModifierContext =>
                if (null != elem.classOrInterfaceVisibilityModifier()) {
                    val currentModifiers = visitClassOrInterfaceVisibilityModifier(elem.classOrInterfaceVisibilityModifier()).asInstanceOf[Modifiers]
                    // add extra modifiers to existing set
                    modifiers = modifiers.copy(modifiers = modifiers.modifiers ++ currentModifiers.modifiers)
                }
                if (null != elem.annotation()) {
                    annotationOpt = Option(visitAnnotation(elem.annotation()).asInstanceOf[ApexAnnotation])
                }
                //visit(elem)
            case elem: ClassDeclarationContext =>
                if (null != elem.className()) {
                    classNameOpt = Option(elem.className().getText)
                }
            case elem => throw new NotImplementedError("Unsupported element: " + elem)

        }
        classNameOpt match {
          case Some(name) =>
              //TODO add supertype, implements & parentContext
              ApexClass(annotationOpt, modifiers, name, superTypeOpt = None, implements = Nil, parentContext = None)
          case None => throw new NotImplementedError("visitClassDef without Class name is not supported")
        }
        //val cls = ApexClass(ctx)
        super.visitClassDef(ctx)
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): ApexUnit = {
        val modifierSetBuilder = Set.newBuilder[Modifier]
        if (null != ctx.ABSTRACT()) {
            modifierSetBuilder += Modifiers.ABSTRACT
        }
        if (null != ctx.GLOBAL()) {
            modifierSetBuilder += Modifiers.GLOBAL
        }
        if (null != ctx.PRIVATE()) {
            modifierSetBuilder += Modifiers.PRIVATE
        }
        if (null != ctx.PUBLIC()) {
            modifierSetBuilder += Modifiers.PUBLIC
        }
        if (null != ctx.VIRTUAL()) {
            modifierSetBuilder += Modifiers.VIRTUAL
        }
        if (null != ctx.WEBSERVICE()) {
            modifierSetBuilder += Modifiers.WEBSERVICE
        }

        //super.visitClassOrInterfaceVisibilityModifier(ctx)
        Modifiers(modifierSetBuilder.result())
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitAnnotation(ctx: AnnotationContext): ApexUnit = {
        //TODO define params
        ApexAnnotation(name = ctx.annotationName().getText, params = Nil, parentContext = None)
        //super.visitAnnotation(ctx)
    }
}
