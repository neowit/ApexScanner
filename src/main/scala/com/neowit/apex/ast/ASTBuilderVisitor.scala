package com.neowit.apex.ast

import com.neowit.apex.scanner.antlr.ApexcodeBaseVisitor
import com.neowit.apex.scanner.antlr.ApexcodeParser._
import com.neowit.apex.nodes._
import org.antlr.v4.runtime.tree.{RuleNode, TerminalNode}

class ASTBuilderVisitor extends ApexcodeBaseVisitor[AstNode] {

    override def defaultResult(): AstNode = NullNode

    private def visitChildren(parent: AstNode, ruleNode: RuleNode): AstNode = {

        for (i <- Range(0, ruleNode.getChildCount)) {
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

    override def visitTerminal(node: TerminalNode): AstNode = {
        FinalNode(node.getText, LocationInterval(node))
    }

    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        val classNode = ApexClass(LocationInterval(ctx))
        visitChildren(classNode, ctx)

        //todo figure out how to extract Implements (with complex type inside) from ClassDeclarationContext

        classNode
    }


    override def visitClassDeclaration(ctx: ClassDeclarationContext): AstNode = {
        val fallThroughNode = FallThroughNode(LocationInterval(ctx))
        visitChildren(fallThroughNode, ctx)
        //super.visitClassDeclaration(ctx)
    }


    override def visitClassName(ctx: ClassNameContext): AstNode = {
        IdentifierNode(ctx.getText, LocationInterval(ctx))
        //super.visitClassName(ctx)
    }

    override def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): AstNode = {
        Modifier.visitClassOrInterfaceVisibilityModifier(ctx)
    }

    override def visitAnnotation(ctx: AnnotationContext): AstNode = {
        ApexAnnotation.visitAnnotation(ctx)
        //super.visitAnnotation(ctx)
    }

    override def visitExtendsDeclaration(ctx: ExtendsDeclarationContext): AstNode = {
        val qualifiedName = QualifiedNameNode(LocationInterval(ctx.qualifiedName()))
        visitChildren(qualifiedName, ctx.qualifiedName())
        ExtendsNode(DataType(qualifiedName, typeArgumentsOpt = None, LocationInterval(ctx)))
    }


    override def visitImplementsDeclaration(ctx: ImplementsDeclarationContext): AstNode = {
        visitChildren(FallThroughNode(LocationInterval(ctx)), ctx)
    }


    /**
      * {@inheritDoc }
      *
      * <p>The default implementation returns the result of calling
      * {@link #visitChildren} on {@code ctx}.</p>
      */
    override def visitImplementsInterface(ctx: ImplementsInterfaceContext): AstNode = {
        val qualifiedName = QualifiedNameNode(LocationInterval(ctx.qualifiedName()))
        visitChildren(qualifiedName, ctx.qualifiedName())
        val typeArguments  =
            if (null != ctx.typeArguments()) {
                val typeArgumentsNode = TypeArgumentsNode(LocationInterval(ctx.typeArguments()))
                visitChildren(typeArgumentsNode, ctx.typeArguments())
                Option(typeArgumentsNode)
            } else {
                None
            }

        ImplementsInterfaceNode(DataType(qualifiedName, typeArgumentsOpt = typeArguments, LocationInterval(ctx)))
    }

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
