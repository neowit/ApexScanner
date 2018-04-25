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

import com.neowit.apexscanner.{Project, TextBasedDocument, VirtualDocument}
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeBaseVisitor, ApexcodeLexer, ApexcodeParser}
import com.neowit.apexscanner.antlr.ApexcodeParser._
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.scanner.SoqlScanner
import org.antlr.v4.runtime.{CommonTokenStream, ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.{RuleNode, TerminalNode}

object ApexAstBuilderVisitor {
    val VISITOR_CREATOR_FUN: AstBuilder.VisitorCreatorFun = (projectOpt, documentOpt, tokenStreamOpt) => new ApexAstBuilderVisitor(projectOpt, documentOpt, tokenStreamOpt)
}

class ApexAstBuilderVisitor(override val projectOpt: Option[Project],
                            override val documentOpt: Option[VirtualDocument],
                            override val tokenStreamOpt: Option[CommonTokenStream]
                           )
        extends ApexcodeBaseVisitor[AstNode] with AstBuilderVisitor {
    private val _classLikeListBuilder = List.newBuilder[ClassLike]

    private val _soqlAstBuilder = new AstBuilder(projectOpt, SoqlAstBuilderVisitor.VISITOR_CREATOR_FUN)
    private val _soqlScanner = SoqlScanner.createDefaultScanner(_soqlAstBuilder)

    private val _documentOffsetPosition: Position = {
        documentOpt.flatMap(_.offset) match {
            case Some(_offset) => _offset
            case None => Position(0, 0)
        }
    }

    def getClassLikeNodes: List[ClassLike] = _classLikeListBuilder.result()

    override def defaultResult(): AstNode = NullNode

    override def visitChildren(node: RuleNode): AstNode = {

        val range = node match {
            case n: ParserRuleContext => Range(n, _documentOffsetPosition)
            case _ =>
                throw new NotImplementedError("Unhandled case is this really a node without location ?" + node)
        }
        val fallThroughNode = FallThroughNode(range)
        visitChildren(fallThroughNode, node)
        //super.visitChildren(node)
    }

    override def onComplete(): Unit = {
        // record all ClassLike nodes in project
        projectOpt match {
            case Some(project) =>
                getClassLikeNodes.foreach(project.addByQualifiedName(_))
            case None =>
        }

        // add standard ENUM method - have to do it here because need to make sure that full parent/child hierarchy is already in place
        getClassLikeNodes.filter(_.isInstanceOf[EnumNode])
            .foreach{
                case enumNode: EnumNode => EnumNode.addStandardMethods(enumNode)
            }
    }

    /**
      * this Node is the top of the tree for given file
      *
      * @param ctx the parse tree
      */
    override def visitCompilationUnit(ctx: CompilationUnitContext): AstNode = {
        documentOpt match {
            case Some(document) if document.fileOpt.isDefined =>
                projectOpt match {
                    case Some(project) =>
                        document.fileOpt match {
                            case Some(file) =>
                                visitChildren(FileNode(project, file, Range(ctx, _documentOffsetPosition)), ctx)
                            case None => NullNode
                        }
                    case None =>
                        throw new IllegalArgumentException("When parsing whole file - Project must be provided.")
                }
            case None =>
                throw new IllegalArgumentException("fileOpt parameter of ApexAstBuilderVisitor must be defined when using visitor with CompilationUnitContext ")
        }
    }

    override def visitTerminal(node: TerminalNode): AstNode = {
        if (ApexcodeParser.Identifier == node.getSymbol.getType) {
            IdentifierNode(node.getText, Range(node, _documentOffsetPosition))
        } else {
            NullNode // TODO - check if we need to return this
        }
    }

    /*
    override def visitApexDoc(ctx: ApexDocContext): AstNode = {
        val text = ctx.getText
        visitChildren(DocNode(text, Range(ctx, _documentOffsetPosition)), ctx)
    }
    */

    override def visitClassDef(ctx: ClassDefContext): AstNode = {
        if (null != ctx.classDeclaration().className()) {
            val nameOpt = Option(ctx.classDeclaration().className().getText)
            val classNode = ClassNode(nameOpt, Range(ctx, _documentOffsetPosition))
            visitChildren(classNode, ctx)
            findApexDoc(ctx.getStart).foreach(docNode => classNode.addChildToAst(docNode))
            _classLikeListBuilder += classNode
            classNode
        } else {
            NullNode
        }
    }
    private val apexDocPredicate: Token => Boolean = {t =>
        t.getType == ApexcodeLexer.APEXDOC_COMMENT
    }
    private def findApexDoc(startToken: Token): Option[DocNode] = {
        tokenStreamOpt match {
            case Some(tokenStream) =>
                ApexParserUtils.getPrevTokenOnChannel(startToken.getTokenIndex, tokenStream, apexDocPredicate, Token.HIDDEN_CHANNEL)
                    .map{docToken =>
                        val text = unwrapJavadoc(docToken.getText)
                        DocNode(text, Range(docToken, _documentOffsetPosition))
                    }
            case None => None
        }
    }
    /**
      * if doc is inside javadoc style comments then we need to remove leading spaces and "*" in each line
      * @param text - text to clean up
      * @return
      */
    private def unwrapJavadoc(text: String): String = {
        if (null != text) {
            text.split("\\r?\\n").map(line => line.replaceFirst("\\s*((\\/\\*+)|(\\**\\/)|(\\**)*)", "")).filterNot(_.trim.isEmpty).mkString(System.getProperty("line.separator"))
        } else {
            ""
        }
    }

    override def visitInterfaceDef(ctx: InterfaceDefContext): AstNode = {
        if (null != ctx.interfaceDeclaration().interfaceName()) {
            val nameOpt = Option(ctx.interfaceDeclaration().interfaceName().getText)
            val interfaceNode = InterfaceNode(nameOpt, Range(ctx, _documentOffsetPosition))
            visitChildren(interfaceNode, ctx)

            _classLikeListBuilder += interfaceNode
            interfaceNode

        } else {
            NullNode
        }
    }

    override def visitTriggerDef(ctx: TriggerDefContext): AstNode = {
        if (null != ctx.triggerDeclaration().triggerName()) {
            val nameOpt = Option(ctx.triggerDeclaration().triggerName().getText)
            val triggerNode = TriggerNode(nameOpt, Range(ctx, _documentOffsetPosition))
            visitChildren(triggerNode, ctx)

            _classLikeListBuilder += triggerNode
            triggerNode
        } else {
            NullNode
        }
    }


    override def visitEnumDef(ctx: EnumDefContext): AstNode = {
        if (null != ctx.enumDeclaration().enumName()) {
            val nameOpt = Option(ctx.enumDeclaration().enumName().getText)
            val enumNode = EnumNode(nameOpt, Range(ctx, _documentOffsetPosition))
            visitChildren(enumNode, ctx)

            _classLikeListBuilder += enumNode
            enumNode
        } else {
            NullNode
        }
    }

    override def visitEnumConstant(ctx: EnumConstantContext): AstNode = {
        if (null != ctx.Identifier()) {
            val node = EnumConstantNode(ctx.Identifier().getSymbol.getText, Range(ctx, _documentOffsetPosition))
            EnumConstantNode.addStandardMethods(node)
            node
        } else {
            NullNode
        }
    }

    override def visitClassName(ctx: ClassNameContext): AstNode = {
        IdentifierNode(ctx.getText, Range(ctx, _documentOffsetPosition))
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
        if (null != ctx.annotationName()) {
            AnnotationNode.visitAnnotation(ctx)
        } else {
            NullNode
        }
    }

    override def visitExtendsDeclaration(ctx: ExtendsDeclarationContext): AstNode = {
        visitChildren(ExtendsNode(Range(ctx, _documentOffsetPosition)), ctx)
    }


    override def visitImplementsDeclaration(ctx: ImplementsDeclarationContext): AstNode = {
        visitChildren(ImplementsInterfaceNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitTypeArguments(ctx: TypeArgumentsContext): AstNode = {
        val typeArgumentsNode = TypeArgumentsNode(Range(ctx, _documentOffsetPosition))
        ctx.dataType().iterator().forEachRemaining{elem =>
            val dataType = visitDataType(elem)
            typeArgumentsNode.addChildToAst(dataType)
            ()
        }
        typeArgumentsNode
    }

    override def visitDataType(ctx: DataTypeContext): AstNode = {
        if (null != ctx.VOID()) {
            DataTypeNodeVoid(Range(ctx, _documentOffsetPosition))
        } else {
            val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
            val dataTypeNode =
            if (null != ctx.arrayType) {
                DataTypeNodeArray(qualifiedNameNode, Range(ctx, _documentOffsetPosition))
            } else if(null != ctx.typeArguments()) {
                //val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
                val typeArgumentsNode =
                    if (null != ctx.typeArguments())
                        Option(visit(ctx.typeArguments()).asInstanceOf[TypeArgumentsNode])
                    else
                        None
                DataTypeNodeGeneric(qualifiedNameNode, typeArgumentsNode, Range(ctx, _documentOffsetPosition))
            } else {
                // last option
                //val qualifiedNameNode = visit(ctx.qualifiedName()).asInstanceOf[QualifiedNameNode]
                DataTypeNodeGeneric(qualifiedNameNode, typeArgumentsOpt = None, Range(ctx, _documentOffsetPosition))
            }
            // add qualifiedNameNode as a child in order to be able to split type name by individual components/nodes
            dataTypeNode.addChildToAst(qualifiedNameNode)
            dataTypeNode
        }
    }

    override def visitQualifiedName(ctx: QualifiedNameContext): AstNode = {
        visitChildren(QualifiedNameNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitClassVariable(ctx: ClassVariableContext): AstNode = {
        visitChildren(ClassVariableNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitClassVariableModifier(ctx: ClassVariableModifierContext): AstNode = {
        if (null != ctx.PROTECTED()) {
            return ModifierNode(ModifierNode.PROTECTED, Range(ctx.PROTECTED(), _documentOffsetPosition))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, Range(ctx.GLOBAL(), _documentOffsetPosition))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, Range(ctx.PRIVATE(), _documentOffsetPosition))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, Range(ctx.PUBLIC(), _documentOffsetPosition))
        }
        if (null != ctx.TRANSIENT()) {
            return ModifierNode(ModifierNode.TRANSIENT, Range(ctx.TRANSIENT(), _documentOffsetPosition))
        }
        NullNode
    }

    override def visitClassProperty(ctx: ClassPropertyContext): AstNode = {
        visitChildren(ClassPropertyNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    /////////////////// constructor ////////////////////////////////

    override def visitPropertyModifier(ctx: PropertyModifierContext): AstNode = {
        if (null != ctx.PROTECTED()) {
            return ModifierNode(ModifierNode.PROTECTED, Range(ctx.PROTECTED(), _documentOffsetPosition))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, Range(ctx.GLOBAL(), _documentOffsetPosition))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, Range(ctx.PRIVATE(), _documentOffsetPosition))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, Range(ctx.PUBLIC(), _documentOffsetPosition))
        }
        if (null != ctx.TRANSIENT()) {
            return ModifierNode(ModifierNode.TRANSIENT, Range(ctx.TRANSIENT(), _documentOffsetPosition))
        }
        NullNode
    }

    override def visitClassConstructor(ctx: ClassConstructorContext): AstNode = {
        visitChildren(ConstructorNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitClassConstructorModifier(ctx: ClassConstructorModifierContext): AstNode = {
        if (null != ctx.PROTECTED()) {
            return ModifierNode(ModifierNode.PROTECTED, Range(ctx.PROTECTED(), _documentOffsetPosition))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, Range(ctx.GLOBAL(), _documentOffsetPosition))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, Range(ctx.PRIVATE(), _documentOffsetPosition))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, Range(ctx.PUBLIC(), _documentOffsetPosition))
        }
        if (null != ctx.VIRTUAL()) {
            return ModifierNode(ModifierNode.VIRTUAL, Range(ctx.VIRTUAL(), _documentOffsetPosition))
        }
        NullNode
    }

    /////////////////// method ////////////////////////////////
    override def visitClassMethod(ctx: ClassMethodContext): AstNode = {
        visitChildren(MethodNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitMethodHeader(ctx: MethodHeaderContext): AstNode = {
        visitChildren(MethodHeaderNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitMethodHeaderModifier(ctx: MethodHeaderModifierContext): AstNode = {
        if (null != ctx.PROTECTED()) {
            return ModifierNode(ModifierNode.PROTECTED, Range(ctx.PROTECTED(), _documentOffsetPosition))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, Range(ctx.GLOBAL(), _documentOffsetPosition))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, Range(ctx.PRIVATE(), _documentOffsetPosition))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, Range(ctx.PUBLIC(), _documentOffsetPosition))
        }
        if (null != ctx.VIRTUAL()) {
            return ModifierNode(ModifierNode.VIRTUAL, Range(ctx.VIRTUAL(), _documentOffsetPosition))
        }
        NullNode
    }


    override def visitDefinedMethodName(ctx: DefinedMethodNameContext): AstNode = {
        MethodNameNode(ctx.Identifier().getText, Range(ctx, _documentOffsetPosition))
    }

    override def visitCalledMethodName(ctx: CalledMethodNameContext): AstNode = {
        MethodNameNode(ctx.func.getText, Range(ctx, _documentOffsetPosition))
    }

    /*
    override def visitMethodName(ctx: CalledMethodNameContext): AstNode = {
        MethodNameNode(ctx.Identifier().getText, Range(ctx, _documentOffsetPosition))
    }
    */

    override def visitMethodParameter(ctx: MethodParameterContext): AstNode = {
        val methodParameterNode = MethodParameterNode(ctx.methodParameterName().getText, Range(ctx, _documentOffsetPosition))
        visitChildren(methodParameterNode, ctx)
        methodParameterNode
    }


    override def visitMethodBody(ctx: MethodBodyContext): AstNode = {
        if (null != ctx.codeBlock()) {
            // concrete method
            visitChildren(MethodBodyNode(Range(ctx.codeBlock(), _documentOffsetPosition)), ctx.codeBlock())
        } else {
            throw new NotImplementedError("visitMethodBody with null ctx.codeBlock() is not implemented")
        }
    }

    override def visitBlockStatement(ctx: BlockStatementContext): AstNode = {
       if (null != ctx.localVariableDeclaration()) {
           visitChildren(LocalVariableNode(Range(ctx.localVariableDeclaration(), _documentOffsetPosition)), ctx.localVariableDeclaration())
       } else if (null != ctx.statement()){
           visit(ctx.statement())
       } else {
           NullNode
       }
    }

    override def visitExpressionStmt(ctx: ExpressionStmtContext): AstNode = {
        visitChildren(ExpressionStatementNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitMethodCallExpr(ctx: MethodCallExprContext): AstNode = {
        visitChildren(MethodCallNode(ctx.func.getText, Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitExpressionList(ctx: ExpressionListContext): AstNode = {
        import scala.collection.JavaConverters._
        if (null != ctx.expression()) {
            val expressions: Seq[AstNode] = ctx.expression().asScala.map(visit)
            val expressionListNode = ExpressionListNode(expressions, Range(ctx, _documentOffsetPosition))
            expressions.map(_.setParentInAst(expressionListNode))
            visitChildren(expressionListNode, ctx)
        } else {
            NullNode
        }
    }

    override def visitExprDotExpression(ctx: ExprDotExpressionContext): AstNode = {
        visitChildren(ExpressionDotExpressionNode(Range(ctx, _documentOffsetPosition)), ctx)
    }


    // collectionExpression[indexExpr]
    override def visitArrayIndexExpr(ctx: ArrayIndexExprContext): AstNode = {
        if (null != ctx.arr) {
            val collectionNode = visit(ctx.arr) // collectionExpression
            val indexExprNode = visit(ctx.expression(1)) // indexExpr

            val arrIndexExpr = ArrayIndexExpressionNode(collectionNode, indexExprNode, Range(ctx, _documentOffsetPosition))
            arrIndexExpr.addChildToAst(collectionNode)
            arrIndexExpr.addChildToAst(indexExprNode)
            arrIndexExpr
        } else {
            NullNode
        }
    }

    override def visitForControl(ctx: ForControlContext): AstNode = {
        if (null != ctx.enhancedForControl()) {
            visitEnhancedForControl(ctx.enhancedForControl())
        } else if (null != ctx.forInit()) {
            val forControlNode = ForControlNode(Range(ctx, _documentOffsetPosition))
            visitChildren(forControlNode, ctx)
        } else {
            NullNode
        }
    }

    override def visitEnhancedForControl(ctx: EnhancedForControlContext): AstNode = {
        val forControlNode = ForControlNode(Range(ctx, _documentOffsetPosition))
        if (null != ctx.dataType() && null != ctx.variableName()) {
            val dataType = visitDataType(ctx.dataType()).asInstanceOf[DataTypeNode]
            forControlNode.addChildToAst(EnhancedForVariableNode(dataType, Option(ctx.variableName().getText), Range(ctx, _documentOffsetPosition)))

            if (null != ctx.expression()) {
                forControlNode.addChildToAst(visit(ctx.expression()))
            }
        } else {
            NullNode
        }
        forControlNode
    }

    override def visitForInit(ctx: ForInitContext): AstNode  = {
        if (null != ctx.localVariableDeclaration()) {
            visitChildren(LocalVariableNode(Range(ctx.localVariableDeclaration(), _documentOffsetPosition)), ctx.localVariableDeclaration())
        } else if (null != ctx.expressionList()) {
            visitChildren(ctx.expressionList())
        } else {
            NullNode
        }
    }

    override def visitPrimaryExpr(ctx: PrimaryExprContext): AstNode = {
        val primary = ctx.primary()
        if (null != primary.Identifier()) {
            IdentifierNode(primary.getText, Range(primary, _documentOffsetPosition))
        } else if (null != primary.THIS()) {
            ThisExpressionNode(Range(primary, _documentOffsetPosition))
        } else if (null != primary.SUPER()) {
            SuperExpressionNode(Range(primary, _documentOffsetPosition))
        } else if (null != primary.dataType()) {
            //dataType '.' CLASS
            val dataType = visitDataType(primary.dataType()).asInstanceOf[DataTypeNode]
            ApexTypeExpressionNode(dataType, Range(primary, _documentOffsetPosition))
        } else {
            // parExpr, literal
            visitChildren(ctx)
        }
    }

    override def visitCreatorExpression(ctx: CreatorExpressionContext): AstNode = {
        visitChildren(CreatorNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitTypeCastExpr(ctx: TypeCastExprContext): AstNode = {
        visitChildren(TypeCastNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    ///////////////// literals ///////////////////////////////
    override def visitIntLiteral(ctx: IntLiteralContext): AstNode = {
        LiteralNode(IntegerLiteral, ctx.IntegerLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitLongLiteral(ctx: LongLiteralContext): AstNode = {
        LiteralNode(LongLiteral, ctx.LongLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitFpLiteral(ctx: FpLiteralContext): AstNode = {
        LiteralNode(FloatingPointLiteral, ctx.FloatingPointLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitStrLiteral(ctx: StrLiteralContext): AstNode = {
        LiteralNode(StringLiteral, ctx.StringLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitBoolLiteral(ctx: BoolLiteralContext): AstNode = {
        LiteralNode(BooleanLiteral, ctx.BooleanLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitNullLiteral(ctx: NullLiteralContext): AstNode = {
        LiteralNode(NULL, ctx.NULL(), Range(ctx, _documentOffsetPosition))
    }

    override def visitSoslLiteral(ctx: SoslLiteralContext): AstNode = {
        LiteralNode(SoslLiteral, ctx.SoslLiteral(), Range(ctx, _documentOffsetPosition))
    }

    override def visitSoqlLiteral(ctx: SoqlLiteralContext): AstNode = {
        //LiteralNode(SoqlLiteral, ctx.SoqlLiteral(), Range(ctx, _documentOffsetPosition))
        if (null != ctx.SoqlLiteral()) {
            val offsetPosition = Range(ctx, _documentOffsetPosition).start // position of SOQL statement in the outer document/class
            val soqlQueryStr = ctx.SoqlLiteral().getText
            //SoqlQueryNode(ctx.SoqlLiteral().getText, Range(ctx, _documentOffsetPosition))
            val soqlDocument = TextBasedDocument(soqlQueryStr, fileOpt = None, Option(offsetPosition))
            _soqlAstBuilder.build(soqlDocument, _soqlScanner)
            _soqlAstBuilder.getAst(soqlDocument) match {
                case Some(astBuilderResult) =>
                    astBuilderResult.rootNode
                case None => NullNode
            }
        } else {
            NullNode
        }
    }
    ///////////////// END literals ///////////////////////////////
}
