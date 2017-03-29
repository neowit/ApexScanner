/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 *  This file is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.neowit.apex.ast

import java.nio.file.Path

import com.neowit.apex.scanner.antlr.{ApexcodeBaseVisitor, ApexcodeParser}
import com.neowit.apex.scanner.antlr.ApexcodeParser._
import com.neowit.apex.nodes._
import org.antlr.v4.runtime.tree.{RuleNode, TerminalNode}

class ASTBuilderVisitor(file: Path) extends ApexcodeBaseVisitor[AstNode] {

    override def defaultResult(): AstNode = NullNode

    override def visitChildren(node: RuleNode): AstNode = {

        val fallThroughNode = FallThroughNode(LocationInterval(node))
        visitChildren(fallThroughNode, node)
        //super.visitChildren(node)
    }

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

    /**
      * this Node is the top of the tree for given file
      * @param ctx the parse tree
      */
    override def visitCompilationUnit(ctx: CompilationUnitContext): AstNode = {
        visitChildren(FileNode(file, LocationInterval(ctx)), ctx)
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

        classNode
    }

    override def visitClassName(ctx: ClassNameContext): AstNode = {
        IdentifierNode(ctx.getText, LocationInterval(ctx))
    }

    override def visitClassOrInterfaceModifier(ctx: ClassOrInterfaceModifierContext): AstNode = {
        if (null != ctx.classOrInterfaceVisibilityModifier()) {
            ModifierNode.visitClassOrInterfaceVisibilityModifier(ctx.classOrInterfaceVisibilityModifier)
        } else if (null != ctx.annotation()) {
            visitAnnotation(ctx.annotation())
        } else if (null != ctx.classSharingModifier()) {
            ModifierNode.visitClassSharingModifier(ctx.classSharingModifier())
        } else {
            throw new NotImplementedError("Unsupported ClassOrInterfaceModifier: " + ctx.getText)
        }
    }

    override def visitAnnotation(ctx: AnnotationContext): AstNode = {
        AnnotationNode.visitAnnotation(ctx)
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

    /////////////////// method ////////////////////////////////
    override def visitClassMethod(ctx: ClassMethodContext): AstNode = {
        visitChildren(MethodNode(LocationInterval(ctx)), ctx)
    }

    override def visitMethodHeader(ctx: MethodHeaderContext): AstNode = {
        visitChildren(MethodHeaderNode(LocationInterval(ctx)), ctx)
    }

    override def visitMethodName(ctx: MethodNameContext): AstNode = {
        MethodNameNode(ctx.Identifier().getText, LocationInterval(ctx))
    }

    override def visitMethodParameter(ctx: MethodParameterContext): AstNode = {
        val methodParameterNode = MethodParameterNode(ctx.methodParameterName().getText, LocationInterval(ctx))
        visitChildren(methodParameterNode, ctx)
        methodParameterNode
    }


    override def visitMethodBody(ctx: MethodBodyContext): AstNode = {
        if (null != ctx.codeBlock()) {
            // concrete method
            visitChildren(MethodBodyNode(LocationInterval(ctx.codeBlock())), ctx.codeBlock())
        } else {
            throw new NotImplementedError("visitMethodBody with null ctx.codeBlock() is not implemented")
        }
    }

    override def visitBlockStatement(ctx: BlockStatementContext): AstNode = {
       if (null != ctx.localVariableDeclaration()) {
           visitChildren(LocalVariableNode(LocationInterval(ctx.localVariableDeclaration())), ctx.localVariableDeclaration())
       } else {
           visit(ctx.statement())
       }
    }

    override def visitExpressionStmt(ctx: ExpressionStmtContext): AstNode = {
        visitChildren(ExpressionNode(LocationInterval(ctx)), ctx)
    }

    override def visitMethodCallExpr(ctx: MethodCallExprContext): AstNode = {
        visitChildren(MethodCallNode(ctx.func.getText, LocationInterval(ctx)), ctx)
    }

    override def visitExpressionList(ctx: ExpressionListContext): AstNode = {
        visitChildren(ExpressionListNode(LocationInterval(ctx)), ctx)
    }

}
