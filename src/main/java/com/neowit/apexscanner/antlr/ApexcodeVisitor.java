// Generated from /Users/andrey/development/scala/projects/ApexScanner/src/main/resources/Apexcode.g4 by ANTLR 4.6

package com.neowit.apexscanner.scanner.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ApexcodeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ApexcodeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(ApexcodeParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code triggerDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerDef(ApexcodeParser.TriggerDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDef(ApexcodeParser.ClassDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interfaceDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDef(ApexcodeParser.InterfaceDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code enumDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDef(ApexcodeParser.EnumDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyDef(ApexcodeParser.EmptyDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceModifier(ApexcodeParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classOrInterfaceVisibilityModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceVisibilityModifier(ApexcodeParser.ClassOrInterfaceVisibilityModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classSharingModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassSharingModifier(ApexcodeParser.ClassSharingModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(ApexcodeParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#className}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassName(ApexcodeParser.ClassNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#extendsDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsDeclaration(ApexcodeParser.ExtendsDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#implementsDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementsDeclaration(ApexcodeParser.ImplementsDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(ApexcodeParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classBodyMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyMember(ApexcodeParser.ClassBodyMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassConstructor(ApexcodeParser.ClassConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classConstructorModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassConstructorModifier(ApexcodeParser.ClassConstructorModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDeclaration(ApexcodeParser.InterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#interfaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceName(ApexcodeParser.InterfaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(ApexcodeParser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#triggerDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerDeclaration(ApexcodeParser.TriggerDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#triggerName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerName(ApexcodeParser.TriggerNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#triggerSObjectType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerSObjectType(ApexcodeParser.TriggerSObjectTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassVariable(ApexcodeParser.ClassVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classVariableModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassVariableModifier(ApexcodeParser.ClassVariableModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#variableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableName(ApexcodeParser.VariableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassMethod(ApexcodeParser.ClassMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodParameters(ApexcodeParser.MethodParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodParameter(ApexcodeParser.MethodParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodParameterName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodParameterName(ApexcodeParser.MethodParameterNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodHeader(ApexcodeParser.MethodHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodHeaderModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodHeaderModifier(ApexcodeParser.MethodHeaderModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodName(ApexcodeParser.MethodNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#methodBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBody(ApexcodeParser.MethodBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassProperty(ApexcodeParser.ClassPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#propertyModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyModifier(ApexcodeParser.PropertyModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#propertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyName(ApexcodeParser.PropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#propertyGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyGet(ApexcodeParser.PropertyGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#propertySet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertySet(ApexcodeParser.PropertySetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#propertyGetSetModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyGetSetModifier(ApexcodeParser.PropertyGetSetModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(ApexcodeParser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#enumName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumName(ApexcodeParser.EnumNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#enumConstants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstants(ApexcodeParser.EnumConstantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#enumConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstant(ApexcodeParser.EnumConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(ApexcodeParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#annotationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationName(ApexcodeParser.AnnotationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#annotationElementValuePairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationElementValuePairs(ApexcodeParser.AnnotationElementValuePairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#annotationElementValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationElementValuePair(ApexcodeParser.AnnotationElementValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#annotationElementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationElementValue(ApexcodeParser.AnnotationElementValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpr(ApexcodeParser.AssignmentExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code postIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostIncrementExpr(ApexcodeParser.PostIncrementExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitwiseXorExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseXorExpr(ApexcodeParser.BitwiseXorExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code instanceOfExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceOfExpr(ApexcodeParser.InstanceOfExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprDotExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprDotExpression(ApexcodeParser.ExprDotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitwiseOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseOrExpr(ApexcodeParser.BitwiseOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitwiseAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseAndExpr(ApexcodeParser.BitwiseAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparisonExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(ApexcodeParser.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeCastComplexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCastComplexExpr(ApexcodeParser.TypeCastComplexExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayIndexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndexExpr(ApexcodeParser.ArrayIndexExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code preIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreIncrementExpr(ApexcodeParser.PreIncrementExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code creatorExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatorExpression(ApexcodeParser.CreatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCallExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCallExpr(ApexcodeParser.MethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryInequalityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryInequalityExpr(ApexcodeParser.UnaryInequalityExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(ApexcodeParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixAndExpr(ApexcodeParser.InfixAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(ApexcodeParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixMulExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixMulExpr(ApexcodeParser.InfixMulExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpr(ApexcodeParser.TernaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixOrExpr(ApexcodeParser.InfixOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixShiftExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixShiftExpr(ApexcodeParser.InfixShiftExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixAddExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixAddExpr(ApexcodeParser.InfixAddExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixEqualityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixEqualityExpr(ApexcodeParser.InfixEqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeCastSimpleExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCastSimpleExpr(ApexcodeParser.TypeCastSimpleExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(ApexcodeParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(ApexcodeParser.ParExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(ApexcodeParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#runas_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRunas_expression(ApexcodeParser.Runas_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbShortcutMerge}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbShortcutMerge(ApexcodeParser.DbShortcutMergeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbShortcutTwoOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbShortcutTwoOp(ApexcodeParser.DbShortcutTwoOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbShortcutOneOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbShortcutOneOp(ApexcodeParser.DbShortcutOneOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(ApexcodeParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#typeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArguments(ApexcodeParser.TypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorRest(ApexcodeParser.ArrayCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#mapCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapCreatorRest(ApexcodeParser.MapCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#setCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCreatorRest(ApexcodeParser.SetCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#classCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassCreatorRest(ApexcodeParser.ClassCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(ApexcodeParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(ApexcodeParser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(ApexcodeParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#codeBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodeBlock(ApexcodeParser.CodeBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#staticCodeBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticCodeBlock(ApexcodeParser.StaticCodeBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclaration(ApexcodeParser.LocalVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#localVariableModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableModifier(ApexcodeParser.LocalVariableModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(ApexcodeParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(ApexcodeParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(ApexcodeParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(ApexcodeParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#enhancedForControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnhancedForControl(ApexcodeParser.EnhancedForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(ApexcodeParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(ApexcodeParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#catchType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchType(ApexcodeParser.CatchTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#finallyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyBlock(ApexcodeParser.FinallyBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(ApexcodeParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(ApexcodeParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(ApexcodeParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doWhileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoWhileStmt(ApexcodeParser.DoWhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(ApexcodeParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElseStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseStmt(ApexcodeParser.IfElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(ApexcodeParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code throwStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStmt(ApexcodeParser.ThrowStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tryCatchFinallyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchFinallyStmt(ApexcodeParser.TryCatchFinallyStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(ApexcodeParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmt(ApexcodeParser.EmptyStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code runAsStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRunAsStmt(ApexcodeParser.RunAsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dbShortcutStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbShortcutStmt(ApexcodeParser.DbShortcutStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStmt(ApexcodeParser.ExpressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(ApexcodeParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fpLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpLiteral(ApexcodeParser.FpLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrLiteral(ApexcodeParser.StrLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(ApexcodeParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(ApexcodeParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code soslLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSoslLiteral(ApexcodeParser.SoslLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code soqlLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSoqlLiteral(ApexcodeParser.SoqlLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ApexcodeParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(ApexcodeParser.DataTypeContext ctx);
}