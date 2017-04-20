// Generated from /Users/andrey/development/scala/projects/ApexScanner/src/main/resources/Apexcode.g4 by ANTLR 4.6

package com.neowit.apexscanner.scanner.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ApexcodeParser}.
 */
public interface ApexcodeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(ApexcodeParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(ApexcodeParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code triggerDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTriggerDef(ApexcodeParser.TriggerDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code triggerDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTriggerDef(ApexcodeParser.TriggerDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(ApexcodeParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(ApexcodeParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code interfaceDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDef(ApexcodeParser.InterfaceDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code interfaceDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDef(ApexcodeParser.InterfaceDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code enumDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDef(ApexcodeParser.EnumDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code enumDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDef(ApexcodeParser.EnumDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEmptyDef(ApexcodeParser.EmptyDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyDef}
	 * labeled alternative in {@link ApexcodeParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEmptyDef(ApexcodeParser.EmptyDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifier(ApexcodeParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifier(ApexcodeParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classOrInterfaceVisibilityModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceVisibilityModifier(ApexcodeParser.ClassOrInterfaceVisibilityModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classOrInterfaceVisibilityModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceVisibilityModifier(ApexcodeParser.ClassOrInterfaceVisibilityModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classSharingModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassSharingModifier(ApexcodeParser.ClassSharingModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classSharingModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassSharingModifier(ApexcodeParser.ClassSharingModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(ApexcodeParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(ApexcodeParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(ApexcodeParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(ApexcodeParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#extendsDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExtendsDeclaration(ApexcodeParser.ExtendsDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#extendsDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExtendsDeclaration(ApexcodeParser.ExtendsDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#implementsDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImplementsDeclaration(ApexcodeParser.ImplementsDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#implementsDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImplementsDeclaration(ApexcodeParser.ImplementsDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(ApexcodeParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(ApexcodeParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classBodyMember}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyMember(ApexcodeParser.ClassBodyMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classBodyMember}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyMember(ApexcodeParser.ClassBodyMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classConstructor}.
	 * @param ctx the parse tree
	 */
	void enterClassConstructor(ApexcodeParser.ClassConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classConstructor}.
	 * @param ctx the parse tree
	 */
	void exitClassConstructor(ApexcodeParser.ClassConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classConstructorModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassConstructorModifier(ApexcodeParser.ClassConstructorModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classConstructorModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassConstructorModifier(ApexcodeParser.ClassConstructorModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(ApexcodeParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(ApexcodeParser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#interfaceName}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceName(ApexcodeParser.InterfaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#interfaceName}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceName(ApexcodeParser.InterfaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(ApexcodeParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(ApexcodeParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#triggerDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTriggerDeclaration(ApexcodeParser.TriggerDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#triggerDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTriggerDeclaration(ApexcodeParser.TriggerDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#triggerName}.
	 * @param ctx the parse tree
	 */
	void enterTriggerName(ApexcodeParser.TriggerNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#triggerName}.
	 * @param ctx the parse tree
	 */
	void exitTriggerName(ApexcodeParser.TriggerNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#triggerSObjectType}.
	 * @param ctx the parse tree
	 */
	void enterTriggerSObjectType(ApexcodeParser.TriggerSObjectTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#triggerSObjectType}.
	 * @param ctx the parse tree
	 */
	void exitTriggerSObjectType(ApexcodeParser.TriggerSObjectTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classVariable}.
	 * @param ctx the parse tree
	 */
	void enterClassVariable(ApexcodeParser.ClassVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classVariable}.
	 * @param ctx the parse tree
	 */
	void exitClassVariable(ApexcodeParser.ClassVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classVariableModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassVariableModifier(ApexcodeParser.ClassVariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classVariableModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassVariableModifier(ApexcodeParser.ClassVariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#variableName}.
	 * @param ctx the parse tree
	 */
	void enterVariableName(ApexcodeParser.VariableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#variableName}.
	 * @param ctx the parse tree
	 */
	void exitVariableName(ApexcodeParser.VariableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void enterClassMethod(ApexcodeParser.ClassMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void exitClassMethod(ApexcodeParser.ClassMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodParameters}.
	 * @param ctx the parse tree
	 */
	void enterMethodParameters(ApexcodeParser.MethodParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodParameters}.
	 * @param ctx the parse tree
	 */
	void exitMethodParameters(ApexcodeParser.MethodParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodParameter}.
	 * @param ctx the parse tree
	 */
	void enterMethodParameter(ApexcodeParser.MethodParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodParameter}.
	 * @param ctx the parse tree
	 */
	void exitMethodParameter(ApexcodeParser.MethodParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodParameterName}.
	 * @param ctx the parse tree
	 */
	void enterMethodParameterName(ApexcodeParser.MethodParameterNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodParameterName}.
	 * @param ctx the parse tree
	 */
	void exitMethodParameterName(ApexcodeParser.MethodParameterNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodHeader}.
	 * @param ctx the parse tree
	 */
	void enterMethodHeader(ApexcodeParser.MethodHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodHeader}.
	 * @param ctx the parse tree
	 */
	void exitMethodHeader(ApexcodeParser.MethodHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodHeaderModifier}.
	 * @param ctx the parse tree
	 */
	void enterMethodHeaderModifier(ApexcodeParser.MethodHeaderModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodHeaderModifier}.
	 * @param ctx the parse tree
	 */
	void exitMethodHeaderModifier(ApexcodeParser.MethodHeaderModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(ApexcodeParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(ApexcodeParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(ApexcodeParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(ApexcodeParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classProperty}.
	 * @param ctx the parse tree
	 */
	void enterClassProperty(ApexcodeParser.ClassPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classProperty}.
	 * @param ctx the parse tree
	 */
	void exitClassProperty(ApexcodeParser.ClassPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#propertyModifier}.
	 * @param ctx the parse tree
	 */
	void enterPropertyModifier(ApexcodeParser.PropertyModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#propertyModifier}.
	 * @param ctx the parse tree
	 */
	void exitPropertyModifier(ApexcodeParser.PropertyModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void enterPropertyName(ApexcodeParser.PropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void exitPropertyName(ApexcodeParser.PropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#propertyGet}.
	 * @param ctx the parse tree
	 */
	void enterPropertyGet(ApexcodeParser.PropertyGetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#propertyGet}.
	 * @param ctx the parse tree
	 */
	void exitPropertyGet(ApexcodeParser.PropertyGetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#propertySet}.
	 * @param ctx the parse tree
	 */
	void enterPropertySet(ApexcodeParser.PropertySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#propertySet}.
	 * @param ctx the parse tree
	 */
	void exitPropertySet(ApexcodeParser.PropertySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#propertyGetSetModifier}.
	 * @param ctx the parse tree
	 */
	void enterPropertyGetSetModifier(ApexcodeParser.PropertyGetSetModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#propertyGetSetModifier}.
	 * @param ctx the parse tree
	 */
	void exitPropertyGetSetModifier(ApexcodeParser.PropertyGetSetModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(ApexcodeParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(ApexcodeParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#enumName}.
	 * @param ctx the parse tree
	 */
	void enterEnumName(ApexcodeParser.EnumNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#enumName}.
	 * @param ctx the parse tree
	 */
	void exitEnumName(ApexcodeParser.EnumNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstants(ApexcodeParser.EnumConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstants(ApexcodeParser.EnumConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(ApexcodeParser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(ApexcodeParser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(ApexcodeParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(ApexcodeParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationName(ApexcodeParser.AnnotationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationName(ApexcodeParser.AnnotationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#annotationElementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationElementValuePairs(ApexcodeParser.AnnotationElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#annotationElementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationElementValuePairs(ApexcodeParser.AnnotationElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#annotationElementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationElementValuePair(ApexcodeParser.AnnotationElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#annotationElementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationElementValuePair(ApexcodeParser.AnnotationElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#annotationElementValue}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationElementValue(ApexcodeParser.AnnotationElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#annotationElementValue}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationElementValue(ApexcodeParser.AnnotationElementValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignmentExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpr(ApexcodeParser.AssignmentExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignmentExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpr(ApexcodeParser.AssignmentExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code postIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPostIncrementExpr(ApexcodeParser.PostIncrementExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code postIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPostIncrementExpr(ApexcodeParser.PostIncrementExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitwiseXorExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseXorExpr(ApexcodeParser.BitwiseXorExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitwiseXorExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseXorExpr(ApexcodeParser.BitwiseXorExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code instanceOfExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceOfExpr(ApexcodeParser.InstanceOfExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code instanceOfExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceOfExpr(ApexcodeParser.InstanceOfExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprDotExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExprDotExpression(ApexcodeParser.ExprDotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprDotExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExprDotExpression(ApexcodeParser.ExprDotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitwiseOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseOrExpr(ApexcodeParser.BitwiseOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitwiseOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseOrExpr(ApexcodeParser.BitwiseOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitwiseAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseAndExpr(ApexcodeParser.BitwiseAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitwiseAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseAndExpr(ApexcodeParser.BitwiseAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparisonExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpr(ApexcodeParser.ComparisonExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparisonExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpr(ApexcodeParser.ComparisonExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeCastComplexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTypeCastComplexExpr(ApexcodeParser.TypeCastComplexExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeCastComplexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTypeCastComplexExpr(ApexcodeParser.TypeCastComplexExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayIndexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndexExpr(ApexcodeParser.ArrayIndexExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayIndexExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndexExpr(ApexcodeParser.ArrayIndexExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code preIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPreIncrementExpr(ApexcodeParser.PreIncrementExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code preIncrementExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPreIncrementExpr(ApexcodeParser.PreIncrementExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code creatorExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCreatorExpression(ApexcodeParser.CreatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code creatorExpression}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCreatorExpression(ApexcodeParser.CreatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCallExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallExpr(ApexcodeParser.MethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCallExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallExpr(ApexcodeParser.MethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryInequalityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryInequalityExpr(ApexcodeParser.UnaryInequalityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryInequalityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryInequalityExpr(ApexcodeParser.UnaryInequalityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(ApexcodeParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(ApexcodeParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixAndExpr(ApexcodeParser.InfixAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixAndExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixAndExpr(ApexcodeParser.InfixAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(ApexcodeParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(ApexcodeParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixMulExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixMulExpr(ApexcodeParser.InfixMulExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixMulExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixMulExpr(ApexcodeParser.InfixMulExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpr(ApexcodeParser.TernaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpr(ApexcodeParser.TernaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixOrExpr(ApexcodeParser.InfixOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixOrExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixOrExpr(ApexcodeParser.InfixOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixShiftExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixShiftExpr(ApexcodeParser.InfixShiftExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixShiftExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixShiftExpr(ApexcodeParser.InfixShiftExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixAddExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixAddExpr(ApexcodeParser.InfixAddExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixAddExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixAddExpr(ApexcodeParser.InfixAddExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code infixEqualityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInfixEqualityExpr(ApexcodeParser.InfixEqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code infixEqualityExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInfixEqualityExpr(ApexcodeParser.InfixEqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeCastSimpleExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTypeCastSimpleExpr(ApexcodeParser.TypeCastSimpleExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeCastSimpleExpr}
	 * labeled alternative in {@link ApexcodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTypeCastSimpleExpr(ApexcodeParser.TypeCastSimpleExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(ApexcodeParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(ApexcodeParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(ApexcodeParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(ApexcodeParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(ApexcodeParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(ApexcodeParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#runas_expression}.
	 * @param ctx the parse tree
	 */
	void enterRunas_expression(ApexcodeParser.Runas_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#runas_expression}.
	 * @param ctx the parse tree
	 */
	void exitRunas_expression(ApexcodeParser.Runas_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbShortcutMerge}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void enterDbShortcutMerge(ApexcodeParser.DbShortcutMergeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbShortcutMerge}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void exitDbShortcutMerge(ApexcodeParser.DbShortcutMergeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbShortcutTwoOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void enterDbShortcutTwoOp(ApexcodeParser.DbShortcutTwoOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbShortcutTwoOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void exitDbShortcutTwoOp(ApexcodeParser.DbShortcutTwoOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbShortcutOneOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void enterDbShortcutOneOp(ApexcodeParser.DbShortcutOneOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbShortcutOneOp}
	 * labeled alternative in {@link ApexcodeParser#db_shortcut_expression}.
	 * @param ctx the parse tree
	 */
	void exitDbShortcutOneOp(ApexcodeParser.DbShortcutOneOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(ApexcodeParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(ApexcodeParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(ApexcodeParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(ApexcodeParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(ApexcodeParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(ApexcodeParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#mapCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterMapCreatorRest(ApexcodeParser.MapCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#mapCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitMapCreatorRest(ApexcodeParser.MapCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#setCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterSetCreatorRest(ApexcodeParser.SetCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#setCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitSetCreatorRest(ApexcodeParser.SetCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(ApexcodeParser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(ApexcodeParser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(ApexcodeParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(ApexcodeParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(ApexcodeParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(ApexcodeParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(ApexcodeParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(ApexcodeParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void enterCodeBlock(ApexcodeParser.CodeBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void exitCodeBlock(ApexcodeParser.CodeBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#staticCodeBlock}.
	 * @param ctx the parse tree
	 */
	void enterStaticCodeBlock(ApexcodeParser.StaticCodeBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#staticCodeBlock}.
	 * @param ctx the parse tree
	 */
	void exitStaticCodeBlock(ApexcodeParser.StaticCodeBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(ApexcodeParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(ApexcodeParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#localVariableModifier}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableModifier(ApexcodeParser.LocalVariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#localVariableModifier}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableModifier(ApexcodeParser.LocalVariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(ApexcodeParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(ApexcodeParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(ApexcodeParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(ApexcodeParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(ApexcodeParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(ApexcodeParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(ApexcodeParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(ApexcodeParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(ApexcodeParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(ApexcodeParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(ApexcodeParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(ApexcodeParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(ApexcodeParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(ApexcodeParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(ApexcodeParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(ApexcodeParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(ApexcodeParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(ApexcodeParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(ApexcodeParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(ApexcodeParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(ApexcodeParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(ApexcodeParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(ApexcodeParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(ApexcodeParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doWhileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStmt(ApexcodeParser.DoWhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doWhileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStmt(ApexcodeParser.DoWhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(ApexcodeParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(ApexcodeParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElseStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfElseStmt(ApexcodeParser.IfElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElseStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfElseStmt(ApexcodeParser.IfElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(ApexcodeParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(ApexcodeParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code throwStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStmt(ApexcodeParser.ThrowStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code throwStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStmt(ApexcodeParser.ThrowStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tryCatchFinallyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchFinallyStmt(ApexcodeParser.TryCatchFinallyStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tryCatchFinallyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchFinallyStmt(ApexcodeParser.TryCatchFinallyStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(ApexcodeParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(ApexcodeParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmt(ApexcodeParser.EmptyStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmt(ApexcodeParser.EmptyStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code runAsStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRunAsStmt(ApexcodeParser.RunAsStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code runAsStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRunAsStmt(ApexcodeParser.RunAsStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dbShortcutStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDbShortcutStmt(ApexcodeParser.DbShortcutStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dbShortcutStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDbShortcutStmt(ApexcodeParser.DbShortcutStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStmt(ApexcodeParser.ExpressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionStmt}
	 * labeled alternative in {@link ApexcodeParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStmt(ApexcodeParser.ExpressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(ApexcodeParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(ApexcodeParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fpLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFpLiteral(ApexcodeParser.FpLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fpLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFpLiteral(ApexcodeParser.FpLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStrLiteral(ApexcodeParser.StrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStrLiteral(ApexcodeParser.StrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(ApexcodeParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(ApexcodeParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(ApexcodeParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(ApexcodeParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soslLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterSoslLiteral(ApexcodeParser.SoslLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soslLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitSoslLiteral(ApexcodeParser.SoslLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soqlLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterSoqlLiteral(ApexcodeParser.SoqlLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soqlLiteral}
	 * labeled alternative in {@link ApexcodeParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitSoqlLiteral(ApexcodeParser.SoqlLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ApexcodeParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(ApexcodeParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ApexcodeParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(ApexcodeParser.DataTypeContext ctx);
}