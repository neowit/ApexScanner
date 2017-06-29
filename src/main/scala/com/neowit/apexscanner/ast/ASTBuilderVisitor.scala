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

package com.neowit.apexscanner.ast

import java.nio.file.Path

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.{ApexcodeBaseVisitor, ApexcodeParser}
import com.neowit.apexscanner.antlr.ApexcodeParser._
import com.neowit.apexscanner.nodes._
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.{RuleNode, TerminalNode}

class ASTBuilderVisitor(project: Project, fileOpt: Option[Path]) extends ApexcodeBaseVisitor[AstNode] {
    private val _classLikeListBuilder = List.newBuilder[ClassLike]
    def getClassLikeNodes: List[ClassLike] = _classLikeListBuilder.result()

    override def defaultResult(): AstNode = NullNode

    override def visitChildren(node: RuleNode): AstNode = {

        val range = node match {
            case n: ParserRuleContext => Range(n)
            case _ =>
                throw new NotImplementedError("Unhandled case is this really a node without location ?" + node)
        }
        val fallThroughNode = FallThroughNode(range)
        visitChildren(fallThroughNode, node)
        //super.visitChildren(node)
    }

    private def visitChildren(parent: AstNode, ruleNode: RuleNode): AstNode = {
        for (i <- scala.collection.immutable.Range(0, ruleNode.getChildCount)) {
            val elem = ruleNode.getChild(i)
            val node = visit(elem)
            if (NullNode != node) {
                //node.setParent(parent)
                parent.addChildToAst(node)
            }
        }
        parent
    }

    /**
      * this Node is the top of the tree for given file
      *
      * @param ctx the parse tree
      */
    override def visitCompilationUnit(ctx: CompilationUnitContext): AstNode = {
        fileOpt match {
            case Some(file) =>
                visitChildren(FileNode(project, file, Range(ctx)), ctx)
            case None =>
                throw new IllegalArgumentException("fileOpt parameter of ASTBuilderVisitor must be defined when using visitor with CompilationUnitContext ")
        }
    }

    override def visitTerminal(node: TerminalNode): AstNode = {
        if (ApexcodeParser.Identifier == node.getSymbol.getType) {
            IdentifierNode(node.getText, Range(node))
        } else {
            NullNode // TODO - check if we need to return this
        }
    }

    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        val classNode = ClassNode(Range(ctx))
        visitChildren(classNode, ctx)

        _classLikeListBuilder += classNode
        classNode
    }


    override def visitInterfaceDef(ctx: InterfaceDefContext): AstNode = {
        val interfaceNode = InterfaceNode(Range(ctx))
        visitChildren(interfaceNode, ctx)

        _classLikeListBuilder += interfaceNode
        interfaceNode
    }

    override def visitTriggerDef(ctx: TriggerDefContext): AstNode = {
        val triggerNode = TriggerNode(Range(ctx))
        visitChildren(triggerNode, ctx)

        _classLikeListBuilder += triggerNode
        triggerNode
    }


    override def visitEnumDef(ctx: EnumDefContext): AstNode = {
        val enumNode = EnumNode(Range(ctx))
        visitChildren(enumNode, ctx)

        _classLikeListBuilder += enumNode
        enumNode
    }

    override def visitClassName(ctx: ClassNameContext): AstNode = {
        IdentifierNode(ctx.getText, Range(ctx))
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
        visitChildren(ExtendsNode(Range(ctx)), ctx)
    }


    override def visitImplementsDeclaration(ctx: ImplementsDeclarationContext): AstNode = {
        visitChildren(ImplementsInterfaceNode(Range(ctx)), ctx)
    }

    override def visitTypeArguments(ctx: TypeArgumentsContext): AstNode = {
        val typeArgumentsNode = TypeArgumentsNode(Range(ctx))
        ctx.dataType().iterator().forEachRemaining{elem =>
            val dataType = visitDataType(elem)
            typeArgumentsNode.addChildToAst(dataType)
            ()
        }
        typeArgumentsNode
    }

    override def visitDataType(ctx: DataTypeContext): AstNode = {
        if (null != ctx.VOID()) {
            DataTypeNodeVoid(Range(ctx))
        } else {
            val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
            if (null != ctx.arrayType) {
                DataTypeNodeArray(qualifiedNameNode, Range(ctx))
            } else if(null != ctx.typeArguments()) {
                //val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
                val typeArgumentsNode =
                    if (null != ctx.typeArguments())
                        Option(visit(ctx.typeArguments()).asInstanceOf[TypeArgumentsNode])
                    else
                        None
                DataTypeNodeGeneric(qualifiedNameNode, typeArgumentsNode, Range(ctx))
            } else {
                // last option
                //val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
                DataTypeNodeGeneric(qualifiedNameNode, typeArgumentsOpt = None, Range(ctx))
            }
        }
    }

    override def visitQualifiedName(ctx: QualifiedNameContext): AstNode = {
        visitChildren(QualifiedNameNode(Range(ctx)), ctx)
    }

    override def visitClassVariable(ctx: ClassVariableContext): AstNode = {
        visitChildren(ClassVariableNode(Range(ctx)), ctx)
    }

    /////////////////// method ////////////////////////////////
    override def visitClassMethod(ctx: ClassMethodContext): AstNode = {
        visitChildren(MethodNode(Range(ctx)), ctx)
    }

    override def visitMethodHeader(ctx: MethodHeaderContext): AstNode = {
        visitChildren(MethodHeaderNode(Range(ctx)), ctx)
    }

    override def visitMethodName(ctx: MethodNameContext): AstNode = {
        MethodNameNode(ctx.Identifier().getText, Range(ctx))
    }

    override def visitMethodParameter(ctx: MethodParameterContext): AstNode = {
        val methodParameterNode = MethodParameterNode(ctx.methodParameterName().getText, Range(ctx))
        visitChildren(methodParameterNode, ctx)
        methodParameterNode
    }


    override def visitMethodBody(ctx: MethodBodyContext): AstNode = {
        if (null != ctx.codeBlock()) {
            // concrete method
            visitChildren(MethodBodyNode(Range(ctx.codeBlock())), ctx.codeBlock())
        } else {
            throw new NotImplementedError("visitMethodBody with null ctx.codeBlock() is not implemented")
        }
    }

    override def visitBlockStatement(ctx: BlockStatementContext): AstNode = {
       if (null != ctx.localVariableDeclaration()) {
           visitChildren(LocalVariableNode(Range(ctx.localVariableDeclaration())), ctx.localVariableDeclaration())
       } else if (null != ctx.statement()){
           visit(ctx.statement())
       } else {
           NullNode
       }
    }

    override def visitExpressionStmt(ctx: ExpressionStmtContext): AstNode = {
        visitChildren(ExpressionStatementNode(Range(ctx)), ctx)
    }

    /*
    override def visitMethodCallExpr(ctx: MethodCallExprContext): AstNode = {
        val context = ctx.methodCall()
        val methodNameTerminal =
            if (null != context.Identifier()) context.Identifier()
            else if (null != context.SUPER()) context.SUPER()
            else if (null != context.THIS()) context.THIS()
            else null
        if (null != methodNameTerminal) {
            visitChildren(MethodCallNode(methodNameTerminal.getText, Range(ctx)), ctx)
        } else {
            NullNode
        }
    }
    */

    override def visitMethodCallExpr(ctx: MethodCallExprContext): AstNode = {
        visitChildren(MethodCallNode(ctx.func.getText, Range(ctx)), ctx)
    }

    override def visitExpressionList(ctx: ExpressionListContext): AstNode = {
        visitChildren(ExpressionListNode(Range(ctx)), ctx)
    }

    override def visitClassBodyMemberRef(ctx: ClassBodyMemberRefContext): AstNode = {
        visitChildren(ctx.expression())
    }

    override def visitAssignmentRightExpr(ctx: AssignmentRightExprContext): AstNode = {
        visitChildren(ctx.expression())
    }

    override def visitExprDotExpression(ctx: ExprDotExpressionContext): AstNode = {
        visitChildren(ExpressionDotExpressionNode(Range(ctx)), ctx)
    }

    override def visitPrimaryExpr(ctx: PrimaryExprContext): AstNode = {
        val primary = ctx.primary()
        if (null != primary.Identifier()) {
            IdentifierNode(primary.getText, Range(primary))
        } else if (null != primary.THIS()) {
            ThisExpressionNode(Range(primary))
        } else if (null != primary.SUPER()) {
            SuperExpressionNode(Range(primary))
        } else if (null != primary.dataType()) {
            //dataType '.' CLASS
            val dataType = visitDataType(primary.dataType()).asInstanceOf[DataTypeNode]
            ApexTypeExpressionNode(dataType, Range(primary))
        } else {
            // parExpr, literal
            visitChildren(ctx)
        }
    }

    override def visitCreatorExpression(ctx: CreatorExpressionContext): AstNode = {
        visitChildren(CreatorNode(Range(ctx)), ctx)
    }

    override def visitTypeCastExpr(ctx: TypeCastExprContext): AstNode = {
        visitChildren(TypeCastNode(Range(ctx)), ctx)
    }

    ///////////////// literals ///////////////////////////////
    override def visitIntLiteral(ctx: IntLiteralContext): AstNode = {
        LiteralNode(IntegerLiteral, ctx.IntegerLiteral(), Range(ctx))
    }

    override def visitFpLiteral(ctx: FpLiteralContext): AstNode = {
        LiteralNode(FloatingPointLiteral, ctx.FloatingPointLiteral(), Range(ctx))
    }

    override def visitStrLiteral(ctx: StrLiteralContext): AstNode = {
        LiteralNode(StringLiteral, ctx.StringLiteral(), Range(ctx))
    }

    override def visitBoolLiteral(ctx: BoolLiteralContext): AstNode = {
        LiteralNode(BooleanLiteral, ctx.BooleanLiteral(), Range(ctx))
    }

    override def visitNullLiteral(ctx: NullLiteralContext): AstNode = {
        LiteralNode(NULL, ctx.NULL(), Range(ctx))
    }

    override def visitSoslLiteral(ctx: SoslLiteralContext): AstNode = {
        LiteralNode(SoslLiteral, ctx.SoslLiteral(), Range(ctx))
    }

    override def visitSoqlLiteral(ctx: SoqlLiteralContext): AstNode = {
        LiteralNode(SoqlLiteral, ctx.SoqlLiteral(), Range(ctx))
    }
    ///////////////// END literals ///////////////////////////////
}
