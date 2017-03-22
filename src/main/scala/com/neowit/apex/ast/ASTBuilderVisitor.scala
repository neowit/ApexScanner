package com.neowit.apex.ast

import com.neowit.apex.scanner.antlr.ApexcodeBaseVisitor
import com.neowit.apex.scanner.antlr.ApexcodeParser._
import com.neowit.apex.nodes._

/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
class ASTBuilderVisitor extends ApexcodeBaseVisitor[AstNode] {
    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        var modifiersBuilder = Set.newBuilder[Modifier]
        var annotationOpt: Option[ApexAnnotation] = None
        var classNameOpt: Option[String] = None

        ctx.children.iterator().forEachRemaining{
            case elem: ClassOrInterfaceModifierContext =>
                if (null != elem.classOrInterfaceVisibilityModifier()) {
                    val modifier = visitClassOrInterfaceVisibilityModifier(elem.classOrInterfaceVisibilityModifier()).asInstanceOf[Modifier]
                    // add extra modifiers to existing set
                    modifiersBuilder += modifier
                }
                if (null != elem.annotation()) {
                    annotationOpt = Option(visit(elem.annotation()).asInstanceOf[ApexAnnotation])
                }
                //visit(elem)
            case elem: ClassDeclarationContext =>
                if (null != elem.className()) {
                    classNameOpt = Option(elem.className().getText)
                }
            case elem => throw new NotImplementedError("Unsupported element: " + elem)

        }

        val classNode =
        classNameOpt match {
          case Some(name) =>
              //TODO add supertype, implements & parentContext
              val cls = ApexClass(name, LocationInterval(ctx))

              annotationOpt.map{a =>
                  a.setParent(cls)
                  cls.addChild(a)
              }
              val modifiers = modifiersBuilder.result()
              modifiers.map{m =>
                  m.setParent(cls)
                  cls.addChild(m)
              }
              cls
          case None => throw new NotImplementedError("visitClassDef without Class name is not supported")
        }

        classNode
        //super.visitClassDef(ctx)
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): AstNode = {
        if (null != ctx.ABSTRACT()) {
            return Modifier(Modifier.ABSTRACT, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.GLOBAL()) {
            return Modifier(Modifier.GLOBAL, LocationInterval(ctx.GLOBAL()))
        }
        if (null != ctx.PRIVATE()) {
            return Modifier(Modifier.PRIVATE, LocationInterval(ctx.PRIVATE()))
        }
        if (null != ctx.PUBLIC()) {
            return Modifier(Modifier.PUBLIC, LocationInterval(ctx.PUBLIC()))
        }
        if (null != ctx.VIRTUAL()) {
            return Modifier(Modifier.VIRTUAL, LocationInterval(ctx.VIRTUAL()))
        }
        if (null != ctx.WEBSERVICE()) {
            return Modifier(Modifier.WEBSERVICE, LocationInterval(ctx.WEBSERVICE()))
        }
        EmptyNode
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitAnnotation(ctx: AnnotationContext): AstNode = {
        //TODO define params
        ApexAnnotation(name = ctx.annotationName().getText, params = Nil, LocationInterval(ctx))
        //super.visitAnnotation(ctx)
    }
}
