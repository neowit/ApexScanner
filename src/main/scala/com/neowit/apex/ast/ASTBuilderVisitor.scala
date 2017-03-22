package com.neowit.apex.ast

import com.neowit.apex.scanner.antlr.ApexcodeBaseVisitor
import com.neowit.apex.scanner.antlr.ApexcodeParser._
import com.neowit.apex.nodes._
import org.antlr.v4.runtime.tree.RuleNode

/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
class ASTBuilderVisitor extends ApexcodeBaseVisitor[AstNode] {

    override def defaultResult(): AstNode = NullNode

    def visitChildren(parent: AstNode, ruleNode: RuleNode): AstNode = {

        for (i <- Range(0, ruleNode.getChildCount-1)) {
            val elem = ruleNode.getChild(i)
            val node = visit(elem)
            if (NullNode != node) {
                node.setParent(parent)
                parent.addChild(node)
            }
        }
        //super.visitChildren(ruleNode)
        parent
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        val classNode = ApexClass(LocationInterval(ctx))
        visitChildren(classNode, ctx)
        //todo figure out how to extract class Name, Extends and Implements from ClassDeclarationContext

        classNode
        //super.visitClassDef(ctx)
    }


    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassDeclaration(ctx: ClassDeclarationContext): AstNode = {

        super.visitClassDeclaration(ctx)
    }


    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): AstNode = {
        Modifier.visitClassOrInterfaceVisibilityModifier(ctx)
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitAnnotation(ctx: AnnotationContext): AstNode = {
        ApexAnnotation.visitAnnotation(ctx)
        //super.visitAnnotation(ctx)
    }

    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitClassVariable(ctx: ClassVariableContext): AstNode = {
        val classVariableNode = ClassVariable(LocationInterval(ctx))
        ctx.children.iterator().forEachRemaining{ elem =>
            val node = visit(elem)
            if (NullNode != node) {
                node.setParent(classVariableNode)
                classVariableNode.addChild(node)
            }
    }
        super.visitClassVariable(ctx)
    }

}
