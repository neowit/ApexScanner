package com.neowit.apex.ast

import com.neowit.apex.scanner.antlr.{ApexcodeBaseVisitor, ApexcodeParser}
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
                //node.setParent(parent)
                parent.addChild(node)
            }
        }
        //super.visitChildren(ruleNode)
        parent
    }

    override def visitTerminal(node: TerminalNode): AstNode = {
        if (ApexcodeParser.Identifier == node.getSymbol.getType) {
            IdentifierNode(node.getText, LocationInterval(node))
        } else {
            NullNode
        }
    }

    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        val classNode = ClassNode(LocationInterval(ctx))
        visitChildren(classNode, ctx)

        //todo figure out how to extract Implements (with complex type inside) from ClassDeclarationContext

        new AstWalker().walk(classNode, new DebugVisitor)
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
        ModifierNode.visitClassOrInterfaceVisibilityModifier(ctx)
    }

    override def visitAnnotation(ctx: AnnotationContext): AstNode = {
        AnnotationNode.visitAnnotation(ctx)
        //super.visitAnnotation(ctx)
    }

    override def visitExtendsDeclaration(ctx: ExtendsDeclarationContext): AstNode = {
        visitChildren(ExtendsNode(LocationInterval(ctx)), ctx)
    }


    override def visitImplementsDeclaration(ctx: ImplementsDeclarationContext): AstNode = {
        visitChildren(ImplementsInterfaceNode(LocationInterval(ctx)), ctx)
    }

    override def visitTypeArguments(ctx: TypeArgumentsContext): AstNode = {
        val typeArgumentsNode = TypeArgumentsNode(LocationInterval(ctx))
        ctx.dataType().iterator().forEachRemaining{elem =>
            val dataType = visitDataType(elem)
            typeArgumentsNode.addChild(dataType)
        }
        typeArgumentsNode
    }

    override def visitDataType(ctx: DataTypeContext): AstNode = {
        if (null != ctx.VOID()) {
            DataTypeVoid(LocationInterval(ctx))
        } else if(null != ctx.typeArguments()) {
            val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
            val typeArgumentsNode =
                if (null != ctx.typeArguments())
                    Option(visit(ctx.typeArguments()).asInstanceOf[TypeArgumentsNode])
                else
                    None
            DataType(qualifiedNameNode, typeArgumentsNode, LocationInterval(ctx))
        } else {
            // last option
            val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
            DataType(qualifiedNameNode, typeArgumentsOpt = None, LocationInterval(ctx))
        }
    }

    override def visitQualifiedName(ctx: QualifiedNameContext): AstNode = {
        visitChildren(QualifiedNameNode(LocationInterval(ctx)), ctx)
    }

    //TODO
    override def visitClassVariable(ctx: ClassVariableContext): AstNode = {
        val classVariableNode = ClassVariableNode(LocationInterval(ctx))
        ctx.children.iterator().forEachRemaining{ elem =>
            val node = visit(elem)
            if (NullNode != node) {
                //node.setParent(classVariableNode)
                classVariableNode.addChild(node)
            }
    }
        super.visitClassVariable(ctx)
    }

}
