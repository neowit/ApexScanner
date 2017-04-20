// Generated from /Users/andrey/development/scala/projects/ApexScanner/src/main/resources/Apexcode.g4 by ANTLR 4.7

package com.neowit.apexscanner.antlr;
import java.util.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ApexcodeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, APEXDOC_COMMENT=47, COMMENT=48, LINE_COMMENT=49, WS=50, BooleanLiteral=51, 
		ABSTRACT=52, CLASS=53, ENUM=54, EXTENDS=55, FALSE=56, FINAL=57, NULL=58, 
		IMPLEMENTS=59, INSTANCE_OF=60, INTERFACE=61, OVERRIDE=62, PRIVATE=63, 
		PROTECTED=64, PUBLIC=65, STATIC=66, SUPER=67, THIS=68, TRANSIENT=69, TRUE=70, 
		VIRTUAL=71, VOID=72, NEW=73, BREAK=74, CONTINUE=75, DO=76, ELSE=77, FOR=78, 
		IF=79, RETURN=80, THROW=81, TRY=82, WHILE=83, BRACKET_THEN_FIND=84, BRACKET_THEN_SELECT=85, 
		DB_DELETE=86, DB_INSERT=87, DB_MERGE=88, DB_UNDELETE=89, DB_UPDATE=90, 
		DB_UPSERT=91, TRIGGER_EVENT=92, GLOBAL=93, SYSTEM_RUNAS=94, TRIGGER_KEYWORD=95, 
		TRIGGER_ON_KEYWORD=96, TESTMETHOD=97, WEBSERVICE=98, WITHOUT_SHARING=99, 
		WITH_SHARING=100, GET_EMPTY=101, GET_OPEN_CURLY=102, SET_EMPTY=103, SET_OPEN_CURLY=104, 
		StringLiteral=105, SoqlLiteral=106, SoslLiteral=107, IntegerLiteral=108, 
		FloatingPointLiteral=109, Identifier=110;
	public static final int
		RULE_compilationUnit = 0, RULE_typeDeclaration = 1, RULE_classOrInterfaceModifier = 2, 
		RULE_classOrInterfaceVisibilityModifier = 3, RULE_classSharingModifier = 4, 
		RULE_classDeclaration = 5, RULE_className = 6, RULE_extendsDeclaration = 7, 
		RULE_implementsDeclaration = 8, RULE_classBody = 9, RULE_classBodyMember = 10, 
		RULE_classConstructor = 11, RULE_classConstructorModifier = 12, RULE_interfaceDeclaration = 13, 
		RULE_interfaceName = 14, RULE_interfaceBody = 15, RULE_triggerDeclaration = 16, 
		RULE_triggerName = 17, RULE_triggerSObjectType = 18, RULE_classVariable = 19, 
		RULE_classVariableModifier = 20, RULE_variableName = 21, RULE_classMethod = 22, 
		RULE_methodParameters = 23, RULE_methodParameter = 24, RULE_methodParameterName = 25, 
		RULE_methodHeader = 26, RULE_methodHeaderModifier = 27, RULE_methodName = 28, 
		RULE_methodBody = 29, RULE_classProperty = 30, RULE_propertyModifier = 31, 
		RULE_propertyName = 32, RULE_propertyGet = 33, RULE_propertySet = 34, 
		RULE_propertyGetSetModifier = 35, RULE_enumDeclaration = 36, RULE_enumName = 37, 
		RULE_enumConstants = 38, RULE_enumConstant = 39, RULE_annotation = 40, 
		RULE_annotationName = 41, RULE_annotationElementValuePairs = 42, RULE_annotationElementValuePair = 43, 
		RULE_annotationElementValue = 44, RULE_expression = 45, RULE_primary = 46, 
		RULE_parExpression = 47, RULE_expressionList = 48, RULE_runas_expression = 49, 
		RULE_db_shortcut_expression = 50, RULE_creator = 51, RULE_typeArguments = 52, 
		RULE_arrayCreatorRest = 53, RULE_mapCreatorRest = 54, RULE_setCreatorRest = 55, 
		RULE_classCreatorRest = 56, RULE_variableInitializer = 57, RULE_arrayInitializer = 58, 
		RULE_arguments = 59, RULE_codeBlock = 60, RULE_staticCodeBlock = 61, RULE_localVariableDeclaration = 62, 
		RULE_localVariableModifier = 63, RULE_blockStatement = 64, RULE_qualifiedName = 65, 
		RULE_forControl = 66, RULE_forInit = 67, RULE_enhancedForControl = 68, 
		RULE_forUpdate = 69, RULE_catchClause = 70, RULE_catchType = 71, RULE_finallyBlock = 72, 
		RULE_statement = 73, RULE_literal = 74, RULE_dataType = 75;
	public static final String[] ruleNames = {
		"compilationUnit", "typeDeclaration", "classOrInterfaceModifier", "classOrInterfaceVisibilityModifier", 
		"classSharingModifier", "classDeclaration", "className", "extendsDeclaration", 
		"implementsDeclaration", "classBody", "classBodyMember", "classConstructor", 
		"classConstructorModifier", "interfaceDeclaration", "interfaceName", "interfaceBody", 
		"triggerDeclaration", "triggerName", "triggerSObjectType", "classVariable", 
		"classVariableModifier", "variableName", "classMethod", "methodParameters", 
		"methodParameter", "methodParameterName", "methodHeader", "methodHeaderModifier", 
		"methodName", "methodBody", "classProperty", "propertyModifier", "propertyName", 
		"propertyGet", "propertySet", "propertyGetSetModifier", "enumDeclaration", 
		"enumName", "enumConstants", "enumConstant", "annotation", "annotationName", 
		"annotationElementValuePairs", "annotationElementValuePair", "annotationElementValue", 
		"expression", "primary", "parExpression", "expressionList", "runas_expression", 
		"db_shortcut_expression", "creator", "typeArguments", "arrayCreatorRest", 
		"mapCreatorRest", "setCreatorRest", "classCreatorRest", "variableInitializer", 
		"arrayInitializer", "arguments", "codeBlock", "staticCodeBlock", "localVariableDeclaration", 
		"localVariableModifier", "blockStatement", "qualifiedName", "forControl", 
		"forInit", "enhancedForControl", "forUpdate", "catchClause", "catchType", 
		"finallyBlock", "statement", "literal", "dataType"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'(hidden)'", "';'", "'{'", "'}'", "','", "'('", "')'", "'='", "'@'", 
		"'.'", "'['", "']'", "'++'", "'--'", "'+'", "'-'", "'!'", "'*'", "'/'", 
		"'%'", "'<'", "'>'", "'==='", "'=='", "'!='", "'<>'", "'&'", "'^'", "'|'", 
		"'&&'", "'||'", "'?'", "':'", "'+='", "'-='", "'*='", "'/='", "'&='", 
		"'|='", "'^='", "'>>='", "'>>>='", "'<<='", "'=>'", "'catch'", "'finally'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, "APEXDOC_COMMENT", 
		"COMMENT", "LINE_COMMENT", "WS", "BooleanLiteral", "ABSTRACT", "CLASS", 
		"ENUM", "EXTENDS", "FALSE", "FINAL", "NULL", "IMPLEMENTS", "INSTANCE_OF", 
		"INTERFACE", "OVERRIDE", "PRIVATE", "PROTECTED", "PUBLIC", "STATIC", "SUPER", 
		"THIS", "TRANSIENT", "TRUE", "VIRTUAL", "VOID", "NEW", "BREAK", "CONTINUE", 
		"DO", "ELSE", "FOR", "IF", "RETURN", "THROW", "TRY", "WHILE", "BRACKET_THEN_FIND", 
		"BRACKET_THEN_SELECT", "DB_DELETE", "DB_INSERT", "DB_MERGE", "DB_UNDELETE", 
		"DB_UPDATE", "DB_UPSERT", "TRIGGER_EVENT", "GLOBAL", "SYSTEM_RUNAS", "TRIGGER_KEYWORD", 
		"TRIGGER_ON_KEYWORD", "TESTMETHOD", "WEBSERVICE", "WITHOUT_SHARING", "WITH_SHARING", 
		"GET_EMPTY", "GET_OPEN_CURLY", "SET_EMPTY", "SET_OPEN_CURLY", "StringLiteral", 
		"SoqlLiteral", "SoslLiteral", "IntegerLiteral", "FloatingPointLiteral", 
		"Identifier"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Apexcode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ApexcodeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CompilationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ApexcodeParser.EOF, 0); }
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCompilationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		int _la;
		try {
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				match(T__0);
				setState(153);
				match(EOF);
				}
				break;
			case EOF:
			case T__1:
			case T__8:
			case ABSTRACT:
			case CLASS:
			case ENUM:
			case INTERFACE:
			case PRIVATE:
			case PUBLIC:
			case VIRTUAL:
			case GLOBAL:
			case TRIGGER_KEYWORD:
			case WEBSERVICE:
			case WITHOUT_SHARING:
			case WITH_SHARING:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__8) | (1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << INTERFACE) | (1L << PRIVATE))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (PUBLIC - 65)) | (1L << (VIRTUAL - 65)) | (1L << (GLOBAL - 65)) | (1L << (TRIGGER_KEYWORD - 65)) | (1L << (WEBSERVICE - 65)) | (1L << (WITHOUT_SHARING - 65)) | (1L << (WITH_SHARING - 65)))) != 0)) {
					{
					{
					setState(154);
					typeDeclaration();
					}
					}
					setState(159);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(160);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDeclarationContext extends ParserRuleContext {
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
	 
		public TypeDeclarationContext() { }
		public void copyFrom(TypeDeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TriggerDefContext extends TypeDeclarationContext {
		public TriggerDeclarationContext triggerDeclaration() {
			return getRuleContext(TriggerDeclarationContext.class,0);
		}
		public TriggerDefContext(TypeDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTriggerDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ClassDefContext extends TypeDeclarationContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public ClassDefContext(TypeDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EnumDefContext extends TypeDeclarationContext {
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public EnumDefContext(TypeDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnumDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InterfaceDefContext extends TypeDeclarationContext {
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public InterfaceDefContext(TypeDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInterfaceDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EmptyDefContext extends TypeDeclarationContext {
		public EmptyDefContext(TypeDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEmptyDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_typeDeclaration);
		int _la;
		try {
			setState(180);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new TriggerDefContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				triggerDeclaration();
				}
				break;
			case 2:
				_localctx = new ClassDefContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << ABSTRACT) | (1L << PRIVATE))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (PUBLIC - 65)) | (1L << (VIRTUAL - 65)) | (1L << (GLOBAL - 65)) | (1L << (WEBSERVICE - 65)) | (1L << (WITHOUT_SHARING - 65)) | (1L << (WITH_SHARING - 65)))) != 0)) {
					{
					{
					setState(164);
					classOrInterfaceModifier();
					}
					}
					setState(169);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(170);
				classDeclaration();
				}
				break;
			case 3:
				_localctx = new InterfaceDefContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << ABSTRACT) | (1L << PRIVATE))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (PUBLIC - 65)) | (1L << (VIRTUAL - 65)) | (1L << (GLOBAL - 65)) | (1L << (WEBSERVICE - 65)) | (1L << (WITHOUT_SHARING - 65)) | (1L << (WITH_SHARING - 65)))) != 0)) {
					{
					{
					setState(171);
					classOrInterfaceModifier();
					}
					}
					setState(176);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(177);
				interfaceDeclaration();
				}
				break;
			case 4:
				_localctx = new EnumDefContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(178);
				enumDeclaration();
				}
				break;
			case 5:
				_localctx = new EmptyDefContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(179);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ClassOrInterfaceVisibilityModifierContext classOrInterfaceVisibilityModifier() {
			return getRuleContext(ClassOrInterfaceVisibilityModifierContext.class,0);
		}
		public ClassSharingModifierContext classSharingModifier() {
			return getRuleContext(ClassSharingModifierContext.class,0);
		}
		public ClassOrInterfaceModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassOrInterfaceModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassOrInterfaceModifierContext classOrInterfaceModifier() throws RecognitionException {
		ClassOrInterfaceModifierContext _localctx = new ClassOrInterfaceModifierContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classOrInterfaceModifier);
		try {
			setState(185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				annotation();
				}
				break;
			case ABSTRACT:
			case PRIVATE:
			case PUBLIC:
			case VIRTUAL:
			case GLOBAL:
			case WEBSERVICE:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				classOrInterfaceVisibilityModifier();
				}
				break;
			case WITHOUT_SHARING:
			case WITH_SHARING:
				enterOuterAlt(_localctx, 3);
				{
				setState(184);
				classSharingModifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceVisibilityModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode ABSTRACT() { return getToken(ApexcodeParser.ABSTRACT, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode VIRTUAL() { return getToken(ApexcodeParser.VIRTUAL, 0); }
		public TerminalNode WEBSERVICE() { return getToken(ApexcodeParser.WEBSERVICE, 0); }
		public ClassOrInterfaceVisibilityModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceVisibilityModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassOrInterfaceVisibilityModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassOrInterfaceVisibilityModifierContext classOrInterfaceVisibilityModifier() throws RecognitionException {
		ClassOrInterfaceVisibilityModifierContext _localctx = new ClassOrInterfaceVisibilityModifierContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_classOrInterfaceVisibilityModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			_la = _input.LA(1);
			if ( !(((((_la - 52)) & ~0x3f) == 0 && ((1L << (_la - 52)) & ((1L << (ABSTRACT - 52)) | (1L << (PRIVATE - 52)) | (1L << (PUBLIC - 52)) | (1L << (VIRTUAL - 52)) | (1L << (GLOBAL - 52)) | (1L << (WEBSERVICE - 52)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassSharingModifierContext extends ParserRuleContext {
		public TerminalNode WITHOUT_SHARING() { return getToken(ApexcodeParser.WITHOUT_SHARING, 0); }
		public TerminalNode WITH_SHARING() { return getToken(ApexcodeParser.WITH_SHARING, 0); }
		public ClassSharingModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classSharingModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassSharingModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassSharingModifierContext classSharingModifier() throws RecognitionException {
		ClassSharingModifierContext _localctx = new ClassSharingModifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_classSharingModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_la = _input.LA(1);
			if ( !(_la==WITHOUT_SHARING || _la==WITH_SHARING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(ApexcodeParser.CLASS, 0); }
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public ExtendsDeclarationContext extendsDeclaration() {
			return getRuleContext(ExtendsDeclarationContext.class,0);
		}
		public ImplementsDeclarationContext implementsDeclaration() {
			return getRuleContext(ImplementsDeclarationContext.class,0);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(CLASS);
			setState(192);
			className();
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(193);
				extendsDeclaration();
				}
			}

			setState(197);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(196);
				implementsDeclaration();
				}
			}

			setState(199);
			match(T__2);
			setState(200);
			classBody();
			setState(201);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public ClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_className; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassNameContext className() throws RecognitionException {
		ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_className);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExtendsDeclarationContext extends ParserRuleContext {
		public TerminalNode EXTENDS() { return getToken(ApexcodeParser.EXTENDS, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ExtendsDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extendsDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitExtendsDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtendsDeclarationContext extendsDeclaration() throws RecognitionException {
		ExtendsDeclarationContext _localctx = new ExtendsDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_extendsDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(EXTENDS);
			setState(206);
			dataType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplementsDeclarationContext extends ParserRuleContext {
		public TerminalNode IMPLEMENTS() { return getToken(ApexcodeParser.IMPLEMENTS, 0); }
		public List<DataTypeContext> dataType() {
			return getRuleContexts(DataTypeContext.class);
		}
		public DataTypeContext dataType(int i) {
			return getRuleContext(DataTypeContext.class,i);
		}
		public ImplementsDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implementsDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitImplementsDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplementsDeclarationContext implementsDeclaration() throws RecognitionException {
		ImplementsDeclarationContext _localctx = new ImplementsDeclarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_implementsDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(IMPLEMENTS);
			setState(209);
			dataType();
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(210);
				match(T__4);
				setState(211);
				dataType();
				}
				}
				setState(216);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyContext extends ParserRuleContext {
		public List<StaticCodeBlockContext> staticCodeBlock() {
			return getRuleContexts(StaticCodeBlockContext.class);
		}
		public StaticCodeBlockContext staticCodeBlock(int i) {
			return getRuleContext(StaticCodeBlockContext.class,i);
		}
		public List<ClassBodyMemberContext> classBodyMember() {
			return getRuleContexts(ClassBodyMemberContext.class);
		}
		public ClassBodyMemberContext classBodyMember(int i) {
			return getRuleContext(ClassBodyMemberContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__5) | (1L << T__8) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << NULL) | (1L << INTERFACE) | (1L << OVERRIDE) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (STATIC - 64)) | (1L << (SUPER - 64)) | (1L << (THIS - 64)) | (1L << (TRANSIENT - 64)) | (1L << (VIRTUAL - 64)) | (1L << (VOID - 64)) | (1L << (NEW - 64)) | (1L << (BREAK - 64)) | (1L << (CONTINUE - 64)) | (1L << (DO - 64)) | (1L << (FOR - 64)) | (1L << (IF - 64)) | (1L << (RETURN - 64)) | (1L << (THROW - 64)) | (1L << (TRY - 64)) | (1L << (WHILE - 64)) | (1L << (DB_DELETE - 64)) | (1L << (DB_INSERT - 64)) | (1L << (DB_MERGE - 64)) | (1L << (DB_UNDELETE - 64)) | (1L << (DB_UPDATE - 64)) | (1L << (DB_UPSERT - 64)) | (1L << (GLOBAL - 64)) | (1L << (SYSTEM_RUNAS - 64)) | (1L << (TRIGGER_KEYWORD - 64)) | (1L << (TESTMETHOD - 64)) | (1L << (WEBSERVICE - 64)) | (1L << (WITHOUT_SHARING - 64)) | (1L << (WITH_SHARING - 64)) | (1L << (StringLiteral - 64)) | (1L << (SoqlLiteral - 64)) | (1L << (SoslLiteral - 64)) | (1L << (IntegerLiteral - 64)) | (1L << (FloatingPointLiteral - 64)) | (1L << (Identifier - 64)))) != 0)) {
				{
				setState(220);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(217);
					staticCodeBlock();
					}
					break;
				case 2:
					{
					setState(218);
					classBodyMember();
					}
					break;
				case 3:
					{
					setState(219);
					blockStatement();
					}
					break;
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyMemberContext extends ParserRuleContext {
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public ClassConstructorContext classConstructor() {
			return getRuleContext(ClassConstructorContext.class,0);
		}
		public ClassMethodContext classMethod() {
			return getRuleContext(ClassMethodContext.class,0);
		}
		public ClassVariableContext classVariable() {
			return getRuleContext(ClassVariableContext.class,0);
		}
		public ClassPropertyContext classProperty() {
			return getRuleContext(ClassPropertyContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public ClassBodyMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyMember; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassBodyMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyMemberContext classBodyMember() throws RecognitionException {
		ClassBodyMemberContext _localctx = new ClassBodyMemberContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_classBodyMember);
		try {
			setState(231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(225);
				enumDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(226);
				classConstructor();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(227);
				classMethod();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(228);
				classVariable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(229);
				classProperty();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(230);
				typeDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassConstructorContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<ClassConstructorModifierContext> classConstructorModifier() {
			return getRuleContexts(ClassConstructorModifierContext.class);
		}
		public ClassConstructorModifierContext classConstructorModifier(int i) {
			return getRuleContext(ClassConstructorModifierContext.class,i);
		}
		public MethodParametersContext methodParameters() {
			return getRuleContext(MethodParametersContext.class,0);
		}
		public ClassConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassConstructorContext classConstructor() throws RecognitionException {
		ClassConstructorContext _localctx = new ClassConstructorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_classConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << OVERRIDE) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (VIRTUAL - 64)) | (1L << (GLOBAL - 64)))) != 0)) {
				{
				setState(235);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(233);
					annotation();
					}
					break;
				case OVERRIDE:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case VIRTUAL:
				case GLOBAL:
					{
					setState(234);
					classConstructorModifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(240);
			qualifiedName();
			setState(241);
			match(T__5);
			setState(243);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 57)) & ~0x3f) == 0 && ((1L << (_la - 57)) & ((1L << (FINAL - 57)) | (1L << (VOID - 57)) | (1L << (Identifier - 57)))) != 0)) {
				{
				setState(242);
				methodParameters();
				}
			}

			setState(245);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassConstructorModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(ApexcodeParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode OVERRIDE() { return getToken(ApexcodeParser.OVERRIDE, 0); }
		public TerminalNode VIRTUAL() { return getToken(ApexcodeParser.VIRTUAL, 0); }
		public ClassConstructorModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classConstructorModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassConstructorModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassConstructorModifierContext classConstructorModifier() throws RecognitionException {
		ClassConstructorModifierContext _localctx = new ClassConstructorModifierContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_classConstructorModifier);
		int _la;
		try {
			setState(250);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case GLOBAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				_la = _input.LA(1);
				if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case OVERRIDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				match(OVERRIDE);
				}
				break;
			case VIRTUAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				match(VIRTUAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceDeclarationContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(ApexcodeParser.INTERFACE, 0); }
		public InterfaceNameContext interfaceName() {
			return getRuleContext(InterfaceNameContext.class,0);
		}
		public InterfaceBodyContext interfaceBody() {
			return getRuleContext(InterfaceBodyContext.class,0);
		}
		public ExtendsDeclarationContext extendsDeclaration() {
			return getRuleContext(ExtendsDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInterfaceDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
		InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_interfaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(INTERFACE);
			setState(253);
			interfaceName();
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(254);
				extendsDeclaration();
				}
			}

			setState(257);
			match(T__2);
			setState(258);
			interfaceBody();
			setState(259);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public InterfaceNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInterfaceName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceNameContext interfaceName() throws RecognitionException {
		InterfaceNameContext _localctx = new InterfaceNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_interfaceName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceBodyContext extends ParserRuleContext {
		public List<MethodHeaderContext> methodHeader() {
			return getRuleContexts(MethodHeaderContext.class);
		}
		public MethodHeaderContext methodHeader(int i) {
			return getRuleContext(MethodHeaderContext.class,i);
		}
		public InterfaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInterfaceBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceBodyContext interfaceBody() throws RecognitionException {
		InterfaceBodyContext _localctx = new InterfaceBodyContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_interfaceBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << ABSTRACT) | (1L << OVERRIDE) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (STATIC - 64)) | (1L << (VIRTUAL - 64)) | (1L << (VOID - 64)) | (1L << (GLOBAL - 64)) | (1L << (TESTMETHOD - 64)) | (1L << (WEBSERVICE - 64)) | (1L << (Identifier - 64)))) != 0)) {
				{
				{
				setState(263);
				methodHeader();
				setState(264);
				match(T__1);
				}
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerDeclarationContext extends ParserRuleContext {
		public TerminalNode TRIGGER_KEYWORD() { return getToken(ApexcodeParser.TRIGGER_KEYWORD, 0); }
		public TriggerNameContext triggerName() {
			return getRuleContext(TriggerNameContext.class,0);
		}
		public TerminalNode TRIGGER_ON_KEYWORD() { return getToken(ApexcodeParser.TRIGGER_ON_KEYWORD, 0); }
		public TriggerSObjectTypeContext triggerSObjectType() {
			return getRuleContext(TriggerSObjectTypeContext.class,0);
		}
		public List<TerminalNode> TRIGGER_EVENT() { return getTokens(ApexcodeParser.TRIGGER_EVENT); }
		public TerminalNode TRIGGER_EVENT(int i) {
			return getToken(ApexcodeParser.TRIGGER_EVENT, i);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public TriggerDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTriggerDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriggerDeclarationContext triggerDeclaration() throws RecognitionException {
		TriggerDeclarationContext _localctx = new TriggerDeclarationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_triggerDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(TRIGGER_KEYWORD);
			setState(272);
			triggerName();
			setState(273);
			match(TRIGGER_ON_KEYWORD);
			setState(274);
			triggerSObjectType();
			setState(275);
			match(T__5);
			setState(276);
			match(TRIGGER_EVENT);
			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(277);
				match(T__4);
				setState(278);
				match(TRIGGER_EVENT);
				}
				}
				setState(283);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(284);
			match(T__6);
			setState(285);
			match(T__2);
			setState(286);
			classBody();
			setState(287);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public TriggerNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTriggerName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriggerNameContext triggerName() throws RecognitionException {
		TriggerNameContext _localctx = new TriggerNameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_triggerName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerSObjectTypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public TriggerSObjectTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerSObjectType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTriggerSObjectType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriggerSObjectTypeContext triggerSObjectType() throws RecognitionException {
		TriggerSObjectTypeContext _localctx = new TriggerSObjectTypeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_triggerSObjectType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(291);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassVariableContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public List<VariableNameContext> variableName() {
			return getRuleContexts(VariableNameContext.class);
		}
		public VariableNameContext variableName(int i) {
			return getRuleContext(VariableNameContext.class,i);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<ClassVariableModifierContext> classVariableModifier() {
			return getRuleContexts(ClassVariableModifierContext.class);
		}
		public ClassVariableModifierContext classVariableModifier(int i) {
			return getRuleContext(ClassVariableModifierContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ClassVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classVariable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassVariableContext classVariable() throws RecognitionException {
		ClassVariableContext _localctx = new ClassVariableContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_classVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << FINAL) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (STATIC - 64)) | (1L << (TRANSIENT - 64)) | (1L << (GLOBAL - 64)) | (1L << (WEBSERVICE - 64)))) != 0)) {
				{
				setState(295);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(293);
					annotation();
					}
					break;
				case FINAL:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case STATIC:
				case TRANSIENT:
				case GLOBAL:
				case WEBSERVICE:
					{
					setState(294);
					classVariableModifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(300);
			dataType();
			setState(301);
			variableName();
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(302);
				match(T__7);
				setState(303);
				expression(0);
				}
			}

			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(306);
				match(T__4);
				setState(307);
				variableName();
				setState(310);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(308);
					match(T__7);
					setState(309);
					expression(0);
					}
				}

				}
				}
				setState(316);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(317);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassVariableModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(ApexcodeParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode FINAL() { return getToken(ApexcodeParser.FINAL, 0); }
		public TerminalNode STATIC() { return getToken(ApexcodeParser.STATIC, 0); }
		public TerminalNode TRANSIENT() { return getToken(ApexcodeParser.TRANSIENT, 0); }
		public TerminalNode WEBSERVICE() { return getToken(ApexcodeParser.WEBSERVICE, 0); }
		public ClassVariableModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classVariableModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassVariableModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassVariableModifierContext classVariableModifier() throws RecognitionException {
		ClassVariableModifierContext _localctx = new ClassVariableModifierContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_classVariableModifier);
		int _la;
		try {
			setState(324);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case GLOBAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(319);
				_la = _input.LA(1);
				if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case FINAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(320);
				match(FINAL);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 3);
				{
				setState(321);
				match(STATIC);
				}
				break;
			case TRANSIENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(322);
				match(TRANSIENT);
				}
				break;
			case WEBSERVICE:
				enterOuterAlt(_localctx, 5);
				{
				setState(323);
				match(WEBSERVICE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public VariableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitVariableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableNameContext variableName() throws RecognitionException {
		VariableNameContext _localctx = new VariableNameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_variableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassMethodContext extends ParserRuleContext {
		public MethodHeaderContext methodHeader() {
			return getRuleContext(MethodHeaderContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public ClassMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classMethod; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassMethodContext classMethod() throws RecognitionException {
		ClassMethodContext _localctx = new ClassMethodContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_classMethod);
		try {
			setState(334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(328);
				methodHeader();
				setState(329);
				methodBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331);
				methodHeader();
				setState(332);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodParametersContext extends ParserRuleContext {
		public List<MethodParameterContext> methodParameter() {
			return getRuleContexts(MethodParameterContext.class);
		}
		public MethodParameterContext methodParameter(int i) {
			return getRuleContext(MethodParameterContext.class,i);
		}
		public MethodParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodParameters; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodParametersContext methodParameters() throws RecognitionException {
		MethodParametersContext _localctx = new MethodParametersContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_methodParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			methodParameter();
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(337);
				match(T__4);
				setState(338);
				methodParameter();
				}
				}
				setState(343);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodParameterContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public MethodParameterNameContext methodParameterName() {
			return getRuleContext(MethodParameterNameContext.class,0);
		}
		public TerminalNode FINAL() { return getToken(ApexcodeParser.FINAL, 0); }
		public MethodParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodParameter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodParameterContext methodParameter() throws RecognitionException {
		MethodParameterContext _localctx = new MethodParameterContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_methodParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FINAL) {
				{
				setState(344);
				match(FINAL);
				}
			}

			setState(347);
			dataType();
			setState(348);
			methodParameterName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodParameterNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public MethodParameterNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodParameterName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodParameterName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodParameterNameContext methodParameterName() throws RecognitionException {
		MethodParameterNameContext _localctx = new MethodParameterNameContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_methodParameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodHeaderContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public MethodNameContext methodName() {
			return getRuleContext(MethodNameContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<MethodHeaderModifierContext> methodHeaderModifier() {
			return getRuleContexts(MethodHeaderModifierContext.class);
		}
		public MethodHeaderModifierContext methodHeaderModifier(int i) {
			return getRuleContext(MethodHeaderModifierContext.class,i);
		}
		public MethodParametersContext methodParameters() {
			return getRuleContext(MethodParametersContext.class,0);
		}
		public MethodHeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodHeader; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodHeaderContext methodHeader() throws RecognitionException {
		MethodHeaderContext _localctx = new MethodHeaderContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_methodHeader);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << ABSTRACT) | (1L << OVERRIDE) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (STATIC - 64)) | (1L << (VIRTUAL - 64)) | (1L << (GLOBAL - 64)) | (1L << (TESTMETHOD - 64)) | (1L << (WEBSERVICE - 64)))) != 0)) {
				{
				setState(354);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(352);
					annotation();
					}
					break;
				case ABSTRACT:
				case OVERRIDE:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case STATIC:
				case VIRTUAL:
				case GLOBAL:
				case TESTMETHOD:
				case WEBSERVICE:
					{
					setState(353);
					methodHeaderModifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(358);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(359);
			dataType();
			setState(360);
			methodName();
			setState(361);
			match(T__5);
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 57)) & ~0x3f) == 0 && ((1L << (_la - 57)) & ((1L << (FINAL - 57)) | (1L << (VOID - 57)) | (1L << (Identifier - 57)))) != 0)) {
				{
				setState(362);
				methodParameters();
				}
			}

			setState(365);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodHeaderModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(ApexcodeParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode OVERRIDE() { return getToken(ApexcodeParser.OVERRIDE, 0); }
		public TerminalNode ABSTRACT() { return getToken(ApexcodeParser.ABSTRACT, 0); }
		public TerminalNode VIRTUAL() { return getToken(ApexcodeParser.VIRTUAL, 0); }
		public TerminalNode STATIC() { return getToken(ApexcodeParser.STATIC, 0); }
		public TerminalNode TESTMETHOD() { return getToken(ApexcodeParser.TESTMETHOD, 0); }
		public TerminalNode WEBSERVICE() { return getToken(ApexcodeParser.WEBSERVICE, 0); }
		public MethodHeaderModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodHeaderModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodHeaderModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodHeaderModifierContext methodHeaderModifier() throws RecognitionException {
		MethodHeaderModifierContext _localctx = new MethodHeaderModifierContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_methodHeaderModifier);
		int _la;
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case GLOBAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				_la = _input.LA(1);
				if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case OVERRIDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(368);
				match(OVERRIDE);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 3);
				{
				setState(369);
				match(ABSTRACT);
				}
				break;
			case VIRTUAL:
				enterOuterAlt(_localctx, 4);
				{
				setState(370);
				match(VIRTUAL);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(371);
				match(STATIC);
				}
				break;
			case TESTMETHOD:
				enterOuterAlt(_localctx, 6);
				{
				setState(372);
				match(TESTMETHOD);
				}
				break;
			case WEBSERVICE:
				enterOuterAlt(_localctx, 7);
				{
				setState(373);
				match(WEBSERVICE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public MethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodNameContext methodName() throws RecognitionException {
		MethodNameContext _localctx = new MethodNameContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_methodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodBodyContext extends ParserRuleContext {
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_methodBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			codeBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassPropertyContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public PropertyNameContext propertyName() {
			return getRuleContext(PropertyNameContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<PropertyModifierContext> propertyModifier() {
			return getRuleContexts(PropertyModifierContext.class);
		}
		public PropertyModifierContext propertyModifier(int i) {
			return getRuleContext(PropertyModifierContext.class,i);
		}
		public List<PropertyGetContext> propertyGet() {
			return getRuleContexts(PropertyGetContext.class);
		}
		public PropertyGetContext propertyGet(int i) {
			return getRuleContext(PropertyGetContext.class,i);
		}
		public List<PropertySetContext> propertySet() {
			return getRuleContexts(PropertySetContext.class);
		}
		public PropertySetContext propertySet(int i) {
			return getRuleContext(PropertySetContext.class,i);
		}
		public ClassPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classProperty; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassPropertyContext classProperty() throws RecognitionException {
		ClassPropertyContext _localctx = new ClassPropertyContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_classProperty);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << FINAL) | (1L << PRIVATE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (STATIC - 64)) | (1L << (TRANSIENT - 64)) | (1L << (GLOBAL - 64)))) != 0)) {
				{
				setState(382);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(380);
					annotation();
					}
					break;
				case FINAL:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case STATIC:
				case TRANSIENT:
				case GLOBAL:
					{
					setState(381);
					propertyModifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(387);
			dataType();
			setState(388);
			propertyName();
			setState(389);
			match(T__2);
			setState(392); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(392);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(390);
					propertyGet();
					}
					break;
				case 2:
					{
					setState(391);
					propertySet();
					}
					break;
				}
				}
				setState(394); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (STATIC - 63)) | (1L << (GLOBAL - 63)) | (1L << (GET_EMPTY - 63)) | (1L << (GET_OPEN_CURLY - 63)) | (1L << (SET_EMPTY - 63)) | (1L << (SET_OPEN_CURLY - 63)))) != 0) );
			setState(396);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(ApexcodeParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode FINAL() { return getToken(ApexcodeParser.FINAL, 0); }
		public TerminalNode STATIC() { return getToken(ApexcodeParser.STATIC, 0); }
		public TerminalNode TRANSIENT() { return getToken(ApexcodeParser.TRANSIENT, 0); }
		public PropertyModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPropertyModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyModifierContext propertyModifier() throws RecognitionException {
		PropertyModifierContext _localctx = new PropertyModifierContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_propertyModifier);
		int _la;
		try {
			setState(402);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case GLOBAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(398);
				_la = _input.LA(1);
				if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case FINAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(399);
				match(FINAL);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 3);
				{
				setState(400);
				match(STATIC);
				}
				break;
			case TRANSIENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(401);
				match(TRANSIENT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public PropertyNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNameContext propertyName() throws RecognitionException {
		PropertyNameContext _localctx = new PropertyNameContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_propertyName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyGetContext extends ParserRuleContext {
		public TerminalNode GET_EMPTY() { return getToken(ApexcodeParser.GET_EMPTY, 0); }
		public TerminalNode GET_OPEN_CURLY() { return getToken(ApexcodeParser.GET_OPEN_CURLY, 0); }
		public List<PropertyGetSetModifierContext> propertyGetSetModifier() {
			return getRuleContexts(PropertyGetSetModifierContext.class);
		}
		public PropertyGetSetModifierContext propertyGetSetModifier(int i) {
			return getRuleContext(PropertyGetSetModifierContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public PropertyGetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyGet; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPropertyGet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyGetContext propertyGet() throws RecognitionException {
		PropertyGetContext _localctx = new PropertyGetContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_propertyGet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (STATIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) {
				{
				{
				setState(406);
				propertyGetSetModifier();
				}
				}
				setState(411);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(421);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GET_EMPTY:
				{
				setState(412);
				match(GET_EMPTY);
				}
				break;
			case GET_OPEN_CURLY:
				{
				setState(413);
				match(GET_OPEN_CURLY);
				setState(417);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << FINAL) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (TRANSIENT - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (BREAK - 67)) | (1L << (CONTINUE - 67)) | (1L << (DO - 67)) | (1L << (FOR - 67)) | (1L << (IF - 67)) | (1L << (RETURN - 67)) | (1L << (THROW - 67)) | (1L << (TRY - 67)) | (1L << (WHILE - 67)) | (1L << (DB_DELETE - 67)) | (1L << (DB_INSERT - 67)) | (1L << (DB_MERGE - 67)) | (1L << (DB_UNDELETE - 67)) | (1L << (DB_UPDATE - 67)) | (1L << (DB_UPSERT - 67)) | (1L << (SYSTEM_RUNAS - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					{
					setState(414);
					blockStatement();
					}
					}
					setState(419);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(420);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertySetContext extends ParserRuleContext {
		public TerminalNode SET_EMPTY() { return getToken(ApexcodeParser.SET_EMPTY, 0); }
		public TerminalNode SET_OPEN_CURLY() { return getToken(ApexcodeParser.SET_OPEN_CURLY, 0); }
		public List<PropertyGetSetModifierContext> propertyGetSetModifier() {
			return getRuleContexts(PropertyGetSetModifierContext.class);
		}
		public PropertyGetSetModifierContext propertyGetSetModifier(int i) {
			return getRuleContext(PropertyGetSetModifierContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public PropertySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertySet; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPropertySet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertySetContext propertySet() throws RecognitionException {
		PropertySetContext _localctx = new PropertySetContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_propertySet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (STATIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) {
				{
				{
				setState(423);
				propertyGetSetModifier();
				}
				}
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(438);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SET_EMPTY:
				{
				setState(429);
				match(SET_EMPTY);
				}
				break;
			case SET_OPEN_CURLY:
				{
				setState(430);
				match(SET_OPEN_CURLY);
				setState(434);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << FINAL) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (TRANSIENT - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (BREAK - 67)) | (1L << (CONTINUE - 67)) | (1L << (DO - 67)) | (1L << (FOR - 67)) | (1L << (IF - 67)) | (1L << (RETURN - 67)) | (1L << (THROW - 67)) | (1L << (TRY - 67)) | (1L << (WHILE - 67)) | (1L << (DB_DELETE - 67)) | (1L << (DB_INSERT - 67)) | (1L << (DB_MERGE - 67)) | (1L << (DB_UNDELETE - 67)) | (1L << (DB_UPDATE - 67)) | (1L << (DB_UPSERT - 67)) | (1L << (SYSTEM_RUNAS - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					{
					setState(431);
					blockStatement();
					}
					}
					setState(436);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(437);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyGetSetModifierContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(ApexcodeParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(ApexcodeParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(ApexcodeParser.PRIVATE, 0); }
		public TerminalNode GLOBAL() { return getToken(ApexcodeParser.GLOBAL, 0); }
		public TerminalNode STATIC() { return getToken(ApexcodeParser.STATIC, 0); }
		public PropertyGetSetModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyGetSetModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPropertyGetSetModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyGetSetModifierContext propertyGetSetModifier() throws RecognitionException {
		PropertyGetSetModifierContext _localctx = new PropertyGetSetModifierContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_propertyGetSetModifier);
		int _la;
		try {
			setState(442);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case GLOBAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(440);
				_la = _input.LA(1);
				if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (PRIVATE - 63)) | (1L << (PROTECTED - 63)) | (1L << (PUBLIC - 63)) | (1L << (GLOBAL - 63)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(441);
				match(STATIC);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(ApexcodeParser.ENUM, 0); }
		public EnumNameContext enumName() {
			return getRuleContext(EnumNameContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public EnumConstantsContext enumConstants() {
			return getRuleContext(EnumConstantsContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnumDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << ABSTRACT) | (1L << PRIVATE))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (PUBLIC - 65)) | (1L << (VIRTUAL - 65)) | (1L << (GLOBAL - 65)) | (1L << (WEBSERVICE - 65)) | (1L << (WITHOUT_SHARING - 65)) | (1L << (WITH_SHARING - 65)))) != 0)) {
				{
				{
				setState(444);
				classOrInterfaceModifier();
				}
				}
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(450);
			match(ENUM);
			setState(451);
			enumName();
			setState(452);
			match(T__2);
			setState(454);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(453);
				enumConstants();
				}
			}

			setState(456);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public EnumNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnumName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumNameContext enumName() throws RecognitionException {
		EnumNameContext _localctx = new EnumNameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_enumName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantsContext extends ParserRuleContext {
		public List<EnumConstantContext> enumConstant() {
			return getRuleContexts(EnumConstantContext.class);
		}
		public EnumConstantContext enumConstant(int i) {
			return getRuleContext(EnumConstantContext.class,i);
		}
		public EnumConstantsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstants; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnumConstants(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumConstantsContext enumConstants() throws RecognitionException {
		EnumConstantsContext _localctx = new EnumConstantsContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_enumConstants);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(460);
			enumConstant();
			setState(465);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(461);
				match(T__4);
				setState(462);
				enumConstant();
				}
				}
				setState(467);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public EnumConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnumConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumConstantContext enumConstant() throws RecognitionException {
		EnumConstantContext _localctx = new EnumConstantContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_enumConstant);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(468);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationNameContext annotationName() {
			return getRuleContext(AnnotationNameContext.class,0);
		}
		public AnnotationElementValuePairsContext annotationElementValuePairs() {
			return getRuleContext(AnnotationElementValuePairsContext.class,0);
		}
		public AnnotationElementValueContext annotationElementValue() {
			return getRuleContext(AnnotationElementValueContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(T__8);
			setState(471);
			annotationName();
			setState(478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(472);
				match(T__5);
				setState(475);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
				case 1:
					{
					setState(473);
					annotationElementValuePairs();
					}
					break;
				case 2:
					{
					setState(474);
					annotationElementValue();
					}
					break;
				}
				setState(477);
				match(T__6);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public AnnotationNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAnnotationName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationNameContext annotationName() throws RecognitionException {
		AnnotationNameContext _localctx = new AnnotationNameContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_annotationName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationElementValuePairsContext extends ParserRuleContext {
		public List<AnnotationElementValuePairContext> annotationElementValuePair() {
			return getRuleContexts(AnnotationElementValuePairContext.class);
		}
		public AnnotationElementValuePairContext annotationElementValuePair(int i) {
			return getRuleContext(AnnotationElementValuePairContext.class,i);
		}
		public AnnotationElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationElementValuePairs; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAnnotationElementValuePairs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationElementValuePairsContext annotationElementValuePairs() throws RecognitionException {
		AnnotationElementValuePairsContext _localctx = new AnnotationElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_annotationElementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			annotationElementValuePair();
			setState(487);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(483);
				match(T__4);
				setState(484);
				annotationElementValuePair();
				}
				}
				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationElementValuePairContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public AnnotationElementValueContext annotationElementValue() {
			return getRuleContext(AnnotationElementValueContext.class,0);
		}
		public AnnotationElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationElementValuePair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAnnotationElementValuePair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationElementValuePairContext annotationElementValuePair() throws RecognitionException {
		AnnotationElementValuePairContext _localctx = new AnnotationElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_annotationElementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(Identifier);
			setState(491);
			match(T__7);
			setState(492);
			annotationElementValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationElementValueContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public AnnotationElementValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationElementValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAnnotationElementValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationElementValueContext annotationElementValue() throws RecognitionException {
		AnnotationElementValueContext _localctx = new AnnotationElementValueContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_annotationElementValue);
		try {
			setState(496);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case BooleanLiteral:
			case NULL:
			case SUPER:
			case THIS:
			case VOID:
			case NEW:
			case StringLiteral:
			case SoqlLiteral:
			case SoslLiteral:
			case IntegerLiteral:
			case FloatingPointLiteral:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(494);
				expression(0);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(495);
				annotation();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AssignmentExprContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignmentExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitAssignmentExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PostIncrementExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PostIncrementExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPostIncrementExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitwiseXorExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BitwiseXorExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBitwiseXorExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InstanceOfExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INSTANCE_OF() { return getToken(ApexcodeParser.INSTANCE_OF, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public InstanceOfExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprDotExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExprDotExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitExprDotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitwiseOrExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BitwiseOrExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBitwiseOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitwiseAndExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BitwiseAndExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBitwiseAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparisonExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode WS() { return getToken(ApexcodeParser.WS, 0); }
		public ComparisonExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeCastComplexExprContext extends ExpressionContext {
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeCastComplexExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTypeCastComplexExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayIndexExprContext extends ExpressionContext {
		public ExpressionContext arr;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayIndexExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitArrayIndexExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PreIncrementExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PreIncrementExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPreIncrementExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreatorExpressionContext extends ExpressionContext {
		public TerminalNode NEW() { return getToken(ApexcodeParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public CreatorExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCreatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodCallExprContext extends ExpressionContext {
		public ExpressionContext func;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MethodCallExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMethodCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryInequalityExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryInequalityExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitUnaryInequalityExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixAndExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixAndExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimaryExprContext extends ExpressionContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public PrimaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixMulExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixMulExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixMulExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TernaryExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TernaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTernaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixOrExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixOrExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixShiftExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixShiftExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixShiftExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixAddExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixAddExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixAddExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixEqualityExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InfixEqualityExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitInfixEqualityExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeCastSimpleExprContext extends ExpressionContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeCastSimpleExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTypeCastSimpleExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 90;
		enterRecursionRule(_localctx, 90, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				_localctx = new PrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(499);
				primary();
				}
				break;
			case 2:
				{
				_localctx = new CreatorExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(500);
				match(NEW);
				setState(501);
				creator();
				}
				break;
			case 3:
				{
				_localctx = new TypeCastComplexExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(502);
				match(T__5);
				setState(503);
				typeArguments();
				setState(504);
				match(T__6);
				setState(505);
				expression(19);
				}
				break;
			case 4:
				{
				_localctx = new PreIncrementExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(507);
				((PreIncrementExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
					((PreIncrementExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(508);
				expression(17);
				}
				break;
			case 5:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(509);
				((UnaryExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__14 || _la==T__15) ) {
					((UnaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(510);
				expression(16);
				}
				break;
			case 6:
				{
				_localctx = new UnaryInequalityExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(511);
				((UnaryInequalityExprContext)_localctx).op = match(T__16);
				setState(512);
				expression(15);
				}
				break;
			case 7:
				{
				_localctx = new TypeCastSimpleExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(513);
				match(T__5);
				setState(514);
				dataType();
				setState(515);
				match(T__6);
				setState(516);
				expression(2);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(601);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(599);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
					case 1:
						{
						_localctx = new ExprDotExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(520);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(521);
						match(T__9);
						setState(522);
						expression(24);
						}
						break;
					case 2:
						{
						_localctx = new InfixMulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(523);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(524);
						((InfixMulExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) ) {
							((InfixMulExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(525);
						expression(15);
						}
						break;
					case 3:
						{
						_localctx = new InfixAddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(526);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(527);
						_la = _input.LA(1);
						if ( !(_la==T__14 || _la==T__15) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(528);
						expression(14);
						}
						break;
					case 4:
						{
						_localctx = new InfixShiftExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(529);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(537);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
						case 1:
							{
							setState(530);
							match(T__20);
							setState(531);
							match(T__20);
							}
							break;
						case 2:
							{
							setState(532);
							match(T__21);
							setState(533);
							match(T__21);
							setState(534);
							match(T__21);
							}
							break;
						case 3:
							{
							setState(535);
							match(T__21);
							setState(536);
							match(T__21);
							}
							break;
						}
						setState(539);
						expression(13);
						}
						break;
					case 5:
						{
						_localctx = new ComparisonExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(540);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(553);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
						case 1:
							{
							setState(541);
							match(T__20);
							setState(543);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==WS) {
								{
								setState(542);
								match(WS);
								}
							}

							setState(545);
							match(T__7);
							}
							break;
						case 2:
							{
							setState(546);
							match(T__21);
							setState(548);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==WS) {
								{
								setState(547);
								match(WS);
								}
							}

							setState(550);
							match(T__7);
							}
							break;
						case 3:
							{
							setState(551);
							match(T__21);
							}
							break;
						case 4:
							{
							setState(552);
							match(T__20);
							}
							break;
						}
						setState(555);
						expression(12);
						}
						break;
					case 6:
						{
						_localctx = new InfixEqualityExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(556);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(557);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(558);
						expression(10);
						}
						break;
					case 7:
						{
						_localctx = new BitwiseAndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(559);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(560);
						match(T__26);
						setState(561);
						expression(9);
						}
						break;
					case 8:
						{
						_localctx = new BitwiseXorExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(562);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(563);
						match(T__27);
						setState(564);
						expression(8);
						}
						break;
					case 9:
						{
						_localctx = new BitwiseOrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(565);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(566);
						match(T__28);
						setState(567);
						expression(7);
						}
						break;
					case 10:
						{
						_localctx = new InfixAndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(568);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(569);
						match(T__29);
						setState(570);
						expression(6);
						}
						break;
					case 11:
						{
						_localctx = new InfixOrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(571);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(572);
						match(T__30);
						setState(573);
						expression(5);
						}
						break;
					case 12:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(574);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(575);
						match(T__31);
						setState(576);
						expression(0);
						setState(577);
						match(T__32);
						setState(578);
						expression(4);
						}
						break;
					case 13:
						{
						_localctx = new AssignmentExprContext(new ExpressionContext(_parentctx, _parentState));
						((AssignmentExprContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(580);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(581);
						((AssignmentExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) ) {
							((AssignmentExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(582);
						((AssignmentExprContext)_localctx).right = expression(1);
						}
						break;
					case 14:
						{
						_localctx = new MethodCallExprContext(new ExpressionContext(_parentctx, _parentState));
						((MethodCallExprContext)_localctx).func = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(583);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(584);
						match(T__5);
						setState(586);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
							{
							setState(585);
							expressionList();
							}
						}

						setState(588);
						match(T__6);
						}
						break;
					case 15:
						{
						_localctx = new ArrayIndexExprContext(new ExpressionContext(_parentctx, _parentState));
						((ArrayIndexExprContext)_localctx).arr = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(589);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(590);
						match(T__10);
						setState(591);
						expression(0);
						setState(592);
						match(T__11);
						}
						break;
					case 16:
						{
						_localctx = new PostIncrementExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(594);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(595);
						((PostIncrementExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
							((PostIncrementExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 17:
						{
						_localctx = new InstanceOfExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(596);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(597);
						match(INSTANCE_OF);
						setState(598);
						dataType();
						}
						break;
					}
					} 
				}
				setState(603);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrimaryContext extends ParserRuleContext {
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public TerminalNode THIS() { return getToken(ApexcodeParser.THIS, 0); }
		public TerminalNode SUPER() { return getToken(ApexcodeParser.SUPER, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(ApexcodeParser.Identifier, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode CLASS() { return getToken(ApexcodeParser.CLASS, 0); }
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_primary);
		try {
			setState(613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(604);
				parExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(605);
				match(THIS);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(606);
				match(SUPER);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(607);
				literal();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(608);
				match(Identifier);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(609);
				dataType();
				setState(610);
				match(T__9);
				setState(611);
				match(CLASS);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitParExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(615);
			match(T__5);
			setState(616);
			expression(0);
			setState(617);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(619);
			expression(0);
			setState(624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(620);
				match(T__4);
				setState(621);
				expression(0);
				}
				}
				setState(626);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Runas_expressionContext extends ParserRuleContext {
		public TerminalNode SYSTEM_RUNAS() { return getToken(ApexcodeParser.SYSTEM_RUNAS, 0); }
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public Runas_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_runas_expression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitRunas_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Runas_expressionContext runas_expression() throws RecognitionException {
		Runas_expressionContext _localctx = new Runas_expressionContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_runas_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			match(SYSTEM_RUNAS);
			setState(628);
			match(T__5);
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
				{
				setState(629);
				expressionList();
				}
			}

			setState(632);
			match(T__6);
			setState(633);
			codeBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Db_shortcut_expressionContext extends ParserRuleContext {
		public Db_shortcut_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_db_shortcut_expression; }
	 
		public Db_shortcut_expressionContext() { }
		public void copyFrom(Db_shortcut_expressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DbShortcutTwoOpContext extends Db_shortcut_expressionContext {
		public TerminalNode DB_UPSERT() { return getToken(ApexcodeParser.DB_UPSERT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DbShortcutTwoOpContext(Db_shortcut_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDbShortcutTwoOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbShortcutMergeContext extends Db_shortcut_expressionContext {
		public TerminalNode DB_MERGE() { return getToken(ApexcodeParser.DB_MERGE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DbShortcutMergeContext(Db_shortcut_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDbShortcutMerge(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbShortcutOneOpContext extends Db_shortcut_expressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DB_UPDATE() { return getToken(ApexcodeParser.DB_UPDATE, 0); }
		public TerminalNode DB_DELETE() { return getToken(ApexcodeParser.DB_DELETE, 0); }
		public TerminalNode DB_INSERT() { return getToken(ApexcodeParser.DB_INSERT, 0); }
		public TerminalNode DB_UNDELETE() { return getToken(ApexcodeParser.DB_UNDELETE, 0); }
		public TerminalNode DB_UPSERT() { return getToken(ApexcodeParser.DB_UPSERT, 0); }
		public DbShortcutOneOpContext(Db_shortcut_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDbShortcutOneOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Db_shortcut_expressionContext db_shortcut_expression() throws RecognitionException {
		Db_shortcut_expressionContext _localctx = new Db_shortcut_expressionContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_db_shortcut_expression);
		int _la;
		try {
			setState(645);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				_localctx = new DbShortcutMergeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(635);
				match(DB_MERGE);
				setState(636);
				expression(0);
				setState(637);
				expression(0);
				}
				break;
			case 2:
				_localctx = new DbShortcutTwoOpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(639);
				match(DB_UPSERT);
				setState(640);
				expression(0);
				setState(641);
				expression(0);
				}
				break;
			case 3:
				_localctx = new DbShortcutOneOpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(643);
				_la = _input.LA(1);
				if ( !(((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & ((1L << (DB_DELETE - 86)) | (1L << (DB_INSERT - 86)) | (1L << (DB_UNDELETE - 86)) | (1L << (DB_UPDATE - 86)) | (1L << (DB_UPSERT - 86)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(644);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public ArrayCreatorRestContext arrayCreatorRest() {
			return getRuleContext(ArrayCreatorRestContext.class,0);
		}
		public MapCreatorRestContext mapCreatorRest() {
			return getRuleContext(MapCreatorRestContext.class,0);
		}
		public SetCreatorRestContext setCreatorRest() {
			return getRuleContext(SetCreatorRestContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_creator);
		try {
			setState(661);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(647);
				dataType();
				setState(648);
				match(T__5);
				setState(649);
				match(T__6);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(651);
				dataType();
				setState(652);
				parExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(654);
				dataType();
				setState(659);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
				case 1:
					{
					setState(655);
					classCreatorRest();
					}
					break;
				case 2:
					{
					setState(656);
					arrayCreatorRest();
					}
					break;
				case 3:
					{
					setState(657);
					mapCreatorRest();
					}
					break;
				case 4:
					{
					setState(658);
					setCreatorRest();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsContext extends ParserRuleContext {
		public List<DataTypeContext> dataType() {
			return getRuleContexts(DataTypeContext.class);
		}
		public DataTypeContext dataType(int i) {
			return getRuleContext(DataTypeContext.class,i);
		}
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTypeArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(663);
			match(T__20);
			setState(664);
			dataType();
			setState(669);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(665);
				match(T__4);
				setState(666);
				dataType();
				}
				}
				setState(671);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(672);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorRestContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorRest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitArrayCreatorRest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
		ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_arrayCreatorRest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			match(T__10);
			setState(702);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
				{
				setState(675);
				match(T__11);
				setState(680);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__10) {
					{
					{
					setState(676);
					match(T__10);
					setState(677);
					match(T__11);
					}
					}
					setState(682);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(683);
				arrayInitializer();
				}
				break;
			case T__5:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case BooleanLiteral:
			case NULL:
			case SUPER:
			case THIS:
			case VOID:
			case NEW:
			case StringLiteral:
			case SoqlLiteral:
			case SoslLiteral:
			case IntegerLiteral:
			case FloatingPointLiteral:
			case Identifier:
				{
				setState(684);
				expression(0);
				setState(685);
				match(T__11);
				setState(692);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(686);
						match(T__10);
						setState(687);
						expression(0);
						setState(688);
						match(T__11);
						}
						} 
					}
					setState(694);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				}
				setState(699);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(695);
						match(T__10);
						setState(696);
						match(T__11);
						}
						} 
					}
					setState(701);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapCreatorRestContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MapCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapCreatorRest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitMapCreatorRest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapCreatorRestContext mapCreatorRest() throws RecognitionException {
		MapCreatorRestContext _localctx = new MapCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_mapCreatorRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704);
			match(T__2);
			setState(729);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
				{
				setState(707);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
				case 1:
					{
					setState(705);
					literal();
					}
					break;
				case 2:
					{
					setState(706);
					expression(0);
					}
					break;
				}
				setState(709);
				match(T__43);
				setState(712);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
				case 1:
					{
					setState(710);
					literal();
					}
					break;
				case 2:
					{
					setState(711);
					expression(0);
					}
					break;
				}
				setState(726);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(714);
					match(T__4);
					setState(717);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
					case 1:
						{
						setState(715);
						literal();
						}
						break;
					case 2:
						{
						setState(716);
						expression(0);
						}
						break;
					}
					setState(719);
					match(T__43);
					setState(722);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
					case 1:
						{
						setState(720);
						literal();
						}
						break;
					case 2:
						{
						setState(721);
						expression(0);
						}
						break;
					}
					}
					}
					setState(728);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(731);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetCreatorRestContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SetCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setCreatorRest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitSetCreatorRest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetCreatorRestContext setCreatorRest() throws RecognitionException {
		SetCreatorRestContext _localctx = new SetCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_setCreatorRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
			match(T__2);
			setState(736);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(734);
				literal();
				}
				break;
			case 2:
				{
				setState(735);
				expression(0);
				}
				break;
			}
			setState(745);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(738);
				match(T__4);
				setState(741);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
				case 1:
					{
					setState(739);
					literal();
					}
					break;
				case 2:
					{
					setState(740);
					expression(0);
					}
					break;
				}
				}
				}
				setState(747);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(748);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassCreatorRestContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classCreatorRest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitClassCreatorRest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassCreatorRestContext classCreatorRest() throws RecognitionException {
		ClassCreatorRestContext _localctx = new ClassCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_classCreatorRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitializerContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInitializer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitVariableInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableInitializerContext variableInitializer() throws RecognitionException {
		VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_variableInitializer);
		try {
			setState(754);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(752);
				arrayInitializer();
				}
				break;
			case T__5:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case BooleanLiteral:
			case NULL:
			case SUPER:
			case THIS:
			case VOID:
			case NEW:
			case StringLiteral:
			case SoqlLiteral:
			case SoslLiteral:
			case IntegerLiteral:
			case FloatingPointLiteral:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(753);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayInitializerContext extends ParserRuleContext {
		public List<VariableInitializerContext> variableInitializer() {
			return getRuleContexts(VariableInitializerContext.class);
		}
		public VariableInitializerContext variableInitializer(int i) {
			return getRuleContext(VariableInitializerContext.class,i);
		}
		public ArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayInitializer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitArrayInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayInitializerContext arrayInitializer() throws RecognitionException {
		ArrayInitializerContext _localctx = new ArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_arrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			match(T__2);
			setState(768);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
				{
				setState(757);
				variableInitializer();
				setState(762);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(758);
						match(T__4);
						setState(759);
						variableInitializer();
						}
						} 
					}
					setState(764);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
				}
				setState(766);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(765);
					match(T__4);
					}
				}

				}
			}

			setState(770);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(772);
			match(T__5);
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
				{
				setState(773);
				expressionList();
				}
			}

			setState(776);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodeBlockContext extends ParserRuleContext {
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public CodeBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCodeBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeBlockContext codeBlock() throws RecognitionException {
		CodeBlockContext _localctx = new CodeBlockContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_codeBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			match(T__2);
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << FINAL) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (TRANSIENT - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (BREAK - 67)) | (1L << (CONTINUE - 67)) | (1L << (DO - 67)) | (1L << (FOR - 67)) | (1L << (IF - 67)) | (1L << (RETURN - 67)) | (1L << (THROW - 67)) | (1L << (TRY - 67)) | (1L << (WHILE - 67)) | (1L << (DB_DELETE - 67)) | (1L << (DB_INSERT - 67)) | (1L << (DB_MERGE - 67)) | (1L << (DB_UNDELETE - 67)) | (1L << (DB_UPDATE - 67)) | (1L << (DB_UPSERT - 67)) | (1L << (SYSTEM_RUNAS - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
				{
				{
				setState(779);
				blockStatement();
				}
				}
				setState(784);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(785);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StaticCodeBlockContext extends ParserRuleContext {
		public TerminalNode STATIC() { return getToken(ApexcodeParser.STATIC, 0); }
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public StaticCodeBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_staticCodeBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitStaticCodeBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StaticCodeBlockContext staticCodeBlock() throws RecognitionException {
		StaticCodeBlockContext _localctx = new StaticCodeBlockContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_staticCodeBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(787);
			match(STATIC);
			setState(788);
			codeBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalVariableDeclarationContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public List<VariableNameContext> variableName() {
			return getRuleContexts(VariableNameContext.class);
		}
		public VariableNameContext variableName(int i) {
			return getRuleContext(VariableNameContext.class,i);
		}
		public List<LocalVariableModifierContext> localVariableModifier() {
			return getRuleContexts(LocalVariableModifierContext.class);
		}
		public LocalVariableModifierContext localVariableModifier(int i) {
			return getRuleContext(LocalVariableModifierContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitLocalVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
		LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_localVariableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(793);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==TRANSIENT) {
				{
				{
				setState(790);
				localVariableModifier();
				}
				}
				setState(795);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(796);
			dataType();
			setState(797);
			variableName();
			setState(800);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(798);
				match(T__7);
				setState(799);
				expression(0);
				}
			}

			setState(810);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(802);
				match(T__4);
				setState(803);
				variableName();
				setState(806);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(804);
					match(T__7);
					setState(805);
					expression(0);
					}
				}

				}
				}
				setState(812);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalVariableModifierContext extends ParserRuleContext {
		public TerminalNode FINAL() { return getToken(ApexcodeParser.FINAL, 0); }
		public TerminalNode TRANSIENT() { return getToken(ApexcodeParser.TRANSIENT, 0); }
		public LocalVariableModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitLocalVariableModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalVariableModifierContext localVariableModifier() throws RecognitionException {
		LocalVariableModifierContext _localctx = new LocalVariableModifierContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_localVariableModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
			_la = _input.LA(1);
			if ( !(_la==FINAL || _la==TRANSIENT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_blockStatement);
		try {
			setState(819);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(815);
				localVariableDeclaration();
				setState(816);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(818);
				statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(ApexcodeParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(ApexcodeParser.Identifier, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(Identifier);
			setState(826);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(822);
					match(T__9);
					setState(823);
					match(Identifier);
					}
					} 
				}
				setState(828);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForControlContext extends ParserRuleContext {
		public EnhancedForControlContext enhancedForControl() {
			return getRuleContext(EnhancedForControlContext.class,0);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForUpdateContext forUpdate() {
			return getRuleContext(ForUpdateContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitForControl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_forControl);
		int _la;
		try {
			setState(841);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(829);
				enhancedForControl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(831);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << FINAL) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (TRANSIENT - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					setState(830);
					forInit();
					}
				}

				setState(833);
				match(T__1);
				setState(835);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					setState(834);
					expression(0);
					}
				}

				setState(837);
				match(T__1);
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					setState(838);
					forUpdate();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForInitContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_forInit);
		try {
			setState(845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(843);
				localVariableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(844);
				expressionList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnhancedForControlContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public VariableNameContext variableName() {
			return getRuleContext(VariableNameContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EnhancedForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enhancedForControl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEnhancedForControl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnhancedForControlContext enhancedForControl() throws RecognitionException {
		EnhancedForControlContext _localctx = new EnhancedForControlContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_enhancedForControl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(847);
			dataType();
			setState(848);
			variableName();
			setState(849);
			match(T__32);
			setState(850);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForUpdateContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forUpdate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitForUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForUpdateContext forUpdate() throws RecognitionException {
		ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_forUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
			expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchClauseContext extends ParserRuleContext {
		public CatchTypeContext catchType() {
			return getRuleContext(CatchTypeContext.class,0);
		}
		public VariableNameContext variableName() {
			return getRuleContext(VariableNameContext.class,0);
		}
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_catchClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
			match(T__44);
			setState(855);
			match(T__5);
			setState(856);
			catchType();
			setState(857);
			variableName();
			setState(858);
			match(T__6);
			setState(859);
			codeBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchTypeContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public CatchTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitCatchType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchTypeContext catchType() throws RecognitionException {
		CatchTypeContext _localctx = new CatchTypeContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_catchType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			qualifiedName();
			setState(866);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__28) {
				{
				{
				setState(862);
				match(T__28);
				setState(863);
				qualifiedName();
				}
				}
				setState(868);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FinallyBlockContext extends ParserRuleContext {
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitFinallyBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FinallyBlockContext finallyBlock() throws RecognitionException {
		FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_finallyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(869);
			match(T__45);
			setState(870);
			codeBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ForStmtContext extends StatementContext {
		public TerminalNode FOR() { return getToken(ApexcodeParser.FOR, 0); }
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ForStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileStmtContext extends StatementContext {
		public TerminalNode WHILE() { return getToken(ApexcodeParser.WHILE, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionStmtContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitExpressionStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EmptyStmtContext extends StatementContext {
		public EmptyStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitEmptyStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnStmtContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(ApexcodeParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DbShortcutStmtContext extends StatementContext {
		public Db_shortcut_expressionContext db_shortcut_expression() {
			return getRuleContext(Db_shortcut_expressionContext.class,0);
		}
		public DbShortcutStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDbShortcutStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ThrowStmtContext extends StatementContext {
		public TerminalNode THROW() { return getToken(ApexcodeParser.THROW, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ThrowStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitThrowStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TryCatchFinallyStmtContext extends StatementContext {
		public TerminalNode TRY() { return getToken(ApexcodeParser.TRY, 0); }
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public FinallyBlockContext finallyBlock() {
			return getRuleContext(FinallyBlockContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public TryCatchFinallyStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitTryCatchFinallyStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RunAsStmtContext extends StatementContext {
		public Runas_expressionContext runas_expression() {
			return getRuleContext(Runas_expressionContext.class,0);
		}
		public RunAsStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitRunAsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoWhileStmtContext extends StatementContext {
		public TerminalNode DO() { return getToken(ApexcodeParser.DO, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode WHILE() { return getToken(ApexcodeParser.WHILE, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public DoWhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDoWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlockStmtContext extends StatementContext {
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public BlockStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfElseStmtContext extends StatementContext {
		public TerminalNode IF() { return getToken(ApexcodeParser.IF, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(ApexcodeParser.ELSE, 0); }
		public IfElseStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitIfElseStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakStmtContext extends StatementContext {
		public TerminalNode BREAK() { return getToken(ApexcodeParser.BREAK, 0); }
		public BreakStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueStmtContext extends StatementContext {
		public TerminalNode CONTINUE() { return getToken(ApexcodeParser.CONTINUE, 0); }
		public ContinueStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_statement);
		int _la;
		try {
			setState(930);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(872);
				codeBlock();
				}
				break;
			case BREAK:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(873);
				match(BREAK);
				setState(874);
				match(T__1);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(875);
				match(CONTINUE);
				setState(876);
				match(T__1);
				}
				break;
			case DO:
				_localctx = new DoWhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(877);
				match(DO);
				setState(878);
				statement();
				setState(879);
				match(WHILE);
				setState(880);
				parExpression();
				setState(881);
				match(T__1);
				}
				break;
			case FOR:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(883);
				match(FOR);
				setState(884);
				match(T__5);
				setState(885);
				forControl();
				setState(886);
				match(T__6);
				setState(887);
				statement();
				}
				break;
			case IF:
				_localctx = new IfElseStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(889);
				match(IF);
				setState(890);
				parExpression();
				setState(891);
				statement();
				setState(894);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
				case 1:
					{
					setState(892);
					match(ELSE);
					setState(893);
					statement();
					}
					break;
				}
				}
				break;
			case RETURN:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(896);
				match(RETURN);
				setState(898);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << BooleanLiteral) | (1L << NULL))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (SUPER - 67)) | (1L << (THIS - 67)) | (1L << (VOID - 67)) | (1L << (NEW - 67)) | (1L << (StringLiteral - 67)) | (1L << (SoqlLiteral - 67)) | (1L << (SoslLiteral - 67)) | (1L << (IntegerLiteral - 67)) | (1L << (FloatingPointLiteral - 67)) | (1L << (Identifier - 67)))) != 0)) {
					{
					setState(897);
					expression(0);
					}
				}

				setState(900);
				match(T__1);
				}
				break;
			case THROW:
				_localctx = new ThrowStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(901);
				match(THROW);
				setState(902);
				expression(0);
				setState(903);
				match(T__1);
				}
				break;
			case TRY:
				_localctx = new TryCatchFinallyStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(905);
				match(TRY);
				setState(906);
				codeBlock();
				setState(916);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__44:
					{
					setState(908); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(907);
						catchClause();
						}
						}
						setState(910); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==T__44 );
					setState(913);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__45) {
						{
						setState(912);
						finallyBlock();
						}
					}

					}
					break;
				case T__45:
					{
					setState(915);
					finallyBlock();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case WHILE:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(918);
				match(WHILE);
				setState(919);
				parExpression();
				setState(920);
				statement();
				}
				break;
			case T__1:
				_localctx = new EmptyStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(922);
				match(T__1);
				}
				break;
			case SYSTEM_RUNAS:
				_localctx = new RunAsStmtContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(923);
				runas_expression();
				}
				break;
			case DB_DELETE:
			case DB_INSERT:
			case DB_MERGE:
			case DB_UNDELETE:
			case DB_UPDATE:
			case DB_UPSERT:
				_localctx = new DbShortcutStmtContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(924);
				db_shortcut_expression();
				setState(925);
				match(T__1);
				}
				break;
			case T__5:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case BooleanLiteral:
			case NULL:
			case SUPER:
			case THIS:
			case VOID:
			case NEW:
			case StringLiteral:
			case SoqlLiteral:
			case SoslLiteral:
			case IntegerLiteral:
			case FloatingPointLiteral:
			case Identifier:
				_localctx = new ExpressionStmtContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(927);
				expression(0);
				setState(928);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StrLiteralContext extends LiteralContext {
		public TerminalNode StringLiteral() { return getToken(ApexcodeParser.StringLiteral, 0); }
		public StrLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitStrLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends LiteralContext {
		public TerminalNode NULL() { return getToken(ApexcodeParser.NULL, 0); }
		public NullLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SoslLiteralContext extends LiteralContext {
		public TerminalNode SoslLiteral() { return getToken(ApexcodeParser.SoslLiteral, 0); }
		public SoslLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitSoslLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FpLiteralContext extends LiteralContext {
		public TerminalNode FloatingPointLiteral() { return getToken(ApexcodeParser.FloatingPointLiteral, 0); }
		public FpLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitFpLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntLiteralContext extends LiteralContext {
		public TerminalNode IntegerLiteral() { return getToken(ApexcodeParser.IntegerLiteral, 0); }
		public IntLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SoqlLiteralContext extends LiteralContext {
		public TerminalNode SoqlLiteral() { return getToken(ApexcodeParser.SoqlLiteral, 0); }
		public SoqlLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitSoqlLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolLiteralContext extends LiteralContext {
		public TerminalNode BooleanLiteral() { return getToken(ApexcodeParser.BooleanLiteral, 0); }
		public BoolLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitBoolLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_literal);
		try {
			setState(939);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
				_localctx = new IntLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(932);
				match(IntegerLiteral);
				}
				break;
			case FloatingPointLiteral:
				_localctx = new FpLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(933);
				match(FloatingPointLiteral);
				}
				break;
			case StringLiteral:
				_localctx = new StrLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(934);
				match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				_localctx = new BoolLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(935);
				match(BooleanLiteral);
				}
				break;
			case NULL:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(936);
				match(NULL);
				}
				break;
			case SoslLiteral:
				_localctx = new SoslLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(937);
				match(SoslLiteral);
				}
				break;
			case SoqlLiteral:
				_localctx = new SoqlLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(938);
				match(SoqlLiteral);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataTypeContext extends ParserRuleContext {
		public QualifiedNameContext arrayType;
		public TerminalNode VOID() { return getToken(ApexcodeParser.VOID, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ApexcodeVisitor ) return ((ApexcodeVisitor<? extends T>)visitor).visitDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_dataType);
		try {
			setState(950);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(941);
				match(VOID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(942);
				((DataTypeContext)_localctx).arrayType = qualifiedName();
				setState(943);
				match(T__10);
				setState(944);
				match(T__11);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(946);
				qualifiedName();
				setState(948);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
				case 1:
					{
					setState(947);
					typeArguments();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 45:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 23);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 11);
		case 5:
			return precpred(_ctx, 9);
		case 6:
			return precpred(_ctx, 8);
		case 7:
			return precpred(_ctx, 7);
		case 8:
			return precpred(_ctx, 6);
		case 9:
			return precpred(_ctx, 5);
		case 10:
			return precpred(_ctx, 4);
		case 11:
			return precpred(_ctx, 3);
		case 12:
			return precpred(_ctx, 1);
		case 13:
			return precpred(_ctx, 22);
		case 14:
			return precpred(_ctx, 21);
		case 15:
			return precpred(_ctx, 18);
		case 16:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3p\u03bb\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\3\2\3\2\3\2\7\2\u009e\n\2\f\2\16\2\u00a1\13"+
		"\2\3\2\5\2\u00a4\n\2\3\3\3\3\7\3\u00a8\n\3\f\3\16\3\u00ab\13\3\3\3\3\3"+
		"\7\3\u00af\n\3\f\3\16\3\u00b2\13\3\3\3\3\3\3\3\5\3\u00b7\n\3\3\4\3\4\3"+
		"\4\5\4\u00bc\n\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\5\7\u00c5\n\7\3\7\5\7\u00c8"+
		"\n\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\7\n\u00d7\n\n"+
		"\f\n\16\n\u00da\13\n\3\13\3\13\3\13\7\13\u00df\n\13\f\13\16\13\u00e2\13"+
		"\13\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00ea\n\f\3\r\3\r\7\r\u00ee\n\r\f\r\16"+
		"\r\u00f1\13\r\3\r\3\r\3\r\5\r\u00f6\n\r\3\r\3\r\3\16\3\16\3\16\5\16\u00fd"+
		"\n\16\3\17\3\17\3\17\5\17\u0102\n\17\3\17\3\17\3\17\3\17\3\20\3\20\3\21"+
		"\3\21\3\21\7\21\u010d\n\21\f\21\16\21\u0110\13\21\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\7\22\u011a\n\22\f\22\16\22\u011d\13\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\7\25\u012a\n\25\f\25\16"+
		"\25\u012d\13\25\3\25\3\25\3\25\3\25\5\25\u0133\n\25\3\25\3\25\3\25\3\25"+
		"\5\25\u0139\n\25\7\25\u013b\n\25\f\25\16\25\u013e\13\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\3\26\5\26\u0147\n\26\3\27\3\27\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\5\30\u0151\n\30\3\31\3\31\3\31\7\31\u0156\n\31\f\31\16\31\u0159"+
		"\13\31\3\32\5\32\u015c\n\32\3\32\3\32\3\32\3\33\3\33\3\34\3\34\7\34\u0165"+
		"\n\34\f\34\16\34\u0168\13\34\3\34\3\34\3\34\3\34\5\34\u016e\n\34\3\34"+
		"\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0179\n\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \7 \u0181\n \f \16 \u0184\13 \3 \3 \3 \3 \3 \6 \u018b\n \r"+
		" \16 \u018c\3 \3 \3!\3!\3!\3!\5!\u0195\n!\3\"\3\"\3#\7#\u019a\n#\f#\16"+
		"#\u019d\13#\3#\3#\3#\7#\u01a2\n#\f#\16#\u01a5\13#\3#\5#\u01a8\n#\3$\7"+
		"$\u01ab\n$\f$\16$\u01ae\13$\3$\3$\3$\7$\u01b3\n$\f$\16$\u01b6\13$\3$\5"+
		"$\u01b9\n$\3%\3%\5%\u01bd\n%\3&\7&\u01c0\n&\f&\16&\u01c3\13&\3&\3&\3&"+
		"\3&\5&\u01c9\n&\3&\3&\3\'\3\'\3(\3(\3(\7(\u01d2\n(\f(\16(\u01d5\13(\3"+
		")\3)\3*\3*\3*\3*\3*\5*\u01de\n*\3*\5*\u01e1\n*\3+\3+\3,\3,\3,\7,\u01e8"+
		"\n,\f,\16,\u01eb\13,\3-\3-\3-\3-\3.\3.\5.\u01f3\n.\3/\3/\3/\3/\3/\3/\3"+
		"/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\5/\u0209\n/\3/\3/\3/\3/\3/\3"+
		"/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\5/\u021c\n/\3/\3/\3/\3/\5/\u0222\n"+
		"/\3/\3/\3/\5/\u0227\n/\3/\3/\3/\5/\u022c\n/\3/\3/\3/\3/\3/\3/\3/\3/\3"+
		"/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\5"+
		"/\u024d\n/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\7/\u025a\n/\f/\16/\u025d\13"+
		"/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u0268\n\60\3\61\3"+
		"\61\3\61\3\61\3\62\3\62\3\62\7\62\u0271\n\62\f\62\16\62\u0274\13\62\3"+
		"\63\3\63\3\63\5\63\u0279\n\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\5\64\u0288\n\64\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u0296\n\65\5\65\u0298\n\65\3\66\3"+
		"\66\3\66\3\66\7\66\u029e\n\66\f\66\16\66\u02a1\13\66\3\66\3\66\3\67\3"+
		"\67\3\67\3\67\7\67\u02a9\n\67\f\67\16\67\u02ac\13\67\3\67\3\67\3\67\3"+
		"\67\3\67\3\67\3\67\7\67\u02b5\n\67\f\67\16\67\u02b8\13\67\3\67\3\67\7"+
		"\67\u02bc\n\67\f\67\16\67\u02bf\13\67\5\67\u02c1\n\67\38\38\38\58\u02c6"+
		"\n8\38\38\38\58\u02cb\n8\38\38\38\58\u02d0\n8\38\38\38\58\u02d5\n8\78"+
		"\u02d7\n8\f8\168\u02da\138\58\u02dc\n8\38\38\39\39\39\59\u02e3\n9\39\3"+
		"9\39\59\u02e8\n9\79\u02ea\n9\f9\169\u02ed\139\39\39\3:\3:\3;\3;\5;\u02f5"+
		"\n;\3<\3<\3<\3<\7<\u02fb\n<\f<\16<\u02fe\13<\3<\5<\u0301\n<\5<\u0303\n"+
		"<\3<\3<\3=\3=\5=\u0309\n=\3=\3=\3>\3>\7>\u030f\n>\f>\16>\u0312\13>\3>"+
		"\3>\3?\3?\3?\3@\7@\u031a\n@\f@\16@\u031d\13@\3@\3@\3@\3@\5@\u0323\n@\3"+
		"@\3@\3@\3@\5@\u0329\n@\7@\u032b\n@\f@\16@\u032e\13@\3A\3A\3B\3B\3B\3B"+
		"\5B\u0336\nB\3C\3C\3C\7C\u033b\nC\fC\16C\u033e\13C\3D\3D\5D\u0342\nD\3"+
		"D\3D\5D\u0346\nD\3D\3D\5D\u034a\nD\5D\u034c\nD\3E\3E\5E\u0350\nE\3F\3"+
		"F\3F\3F\3F\3G\3G\3H\3H\3H\3H\3H\3H\3H\3I\3I\3I\7I\u0363\nI\fI\16I\u0366"+
		"\13I\3J\3J\3J\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3"+
		"K\3K\3K\3K\5K\u0381\nK\3K\3K\5K\u0385\nK\3K\3K\3K\3K\3K\3K\3K\3K\6K\u038f"+
		"\nK\rK\16K\u0390\3K\5K\u0394\nK\3K\5K\u0397\nK\3K\3K\3K\3K\3K\3K\3K\3"+
		"K\3K\3K\3K\3K\5K\u03a5\nK\3L\3L\3L\3L\3L\3L\3L\5L\u03ae\nL\3M\3M\3M\3"+
		"M\3M\3M\3M\5M\u03b7\nM\5M\u03b9\nM\3M\2\3\\N\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|"+
		"~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096"+
		"\u0098\2\f\b\2\66\66AACCII__dd\3\2ef\4\2AC__\3\2\17\20\3\2\21\22\3\2\24"+
		"\26\3\2\31\34\4\2\n\n$-\4\2XY[]\4\2;;GG\2\u041e\2\u00a3\3\2\2\2\4\u00b6"+
		"\3\2\2\2\6\u00bb\3\2\2\2\b\u00bd\3\2\2\2\n\u00bf\3\2\2\2\f\u00c1\3\2\2"+
		"\2\16\u00cd\3\2\2\2\20\u00cf\3\2\2\2\22\u00d2\3\2\2\2\24\u00e0\3\2\2\2"+
		"\26\u00e9\3\2\2\2\30\u00ef\3\2\2\2\32\u00fc\3\2\2\2\34\u00fe\3\2\2\2\36"+
		"\u0107\3\2\2\2 \u010e\3\2\2\2\"\u0111\3\2\2\2$\u0123\3\2\2\2&\u0125\3"+
		"\2\2\2(\u012b\3\2\2\2*\u0146\3\2\2\2,\u0148\3\2\2\2.\u0150\3\2\2\2\60"+
		"\u0152\3\2\2\2\62\u015b\3\2\2\2\64\u0160\3\2\2\2\66\u0166\3\2\2\28\u0178"+
		"\3\2\2\2:\u017a\3\2\2\2<\u017c\3\2\2\2>\u0182\3\2\2\2@\u0194\3\2\2\2B"+
		"\u0196\3\2\2\2D\u019b\3\2\2\2F\u01ac\3\2\2\2H\u01bc\3\2\2\2J\u01c1\3\2"+
		"\2\2L\u01cc\3\2\2\2N\u01ce\3\2\2\2P\u01d6\3\2\2\2R\u01d8\3\2\2\2T\u01e2"+
		"\3\2\2\2V\u01e4\3\2\2\2X\u01ec\3\2\2\2Z\u01f2\3\2\2\2\\\u0208\3\2\2\2"+
		"^\u0267\3\2\2\2`\u0269\3\2\2\2b\u026d\3\2\2\2d\u0275\3\2\2\2f\u0287\3"+
		"\2\2\2h\u0297\3\2\2\2j\u0299\3\2\2\2l\u02a4\3\2\2\2n\u02c2\3\2\2\2p\u02df"+
		"\3\2\2\2r\u02f0\3\2\2\2t\u02f4\3\2\2\2v\u02f6\3\2\2\2x\u0306\3\2\2\2z"+
		"\u030c\3\2\2\2|\u0315\3\2\2\2~\u031b\3\2\2\2\u0080\u032f\3\2\2\2\u0082"+
		"\u0335\3\2\2\2\u0084\u0337\3\2\2\2\u0086\u034b\3\2\2\2\u0088\u034f\3\2"+
		"\2\2\u008a\u0351\3\2\2\2\u008c\u0356\3\2\2\2\u008e\u0358\3\2\2\2\u0090"+
		"\u035f\3\2\2\2\u0092\u0367\3\2\2\2\u0094\u03a4\3\2\2\2\u0096\u03ad\3\2"+
		"\2\2\u0098\u03b8\3\2\2\2\u009a\u009b\7\3\2\2\u009b\u00a4\7\2\2\3\u009c"+
		"\u009e\5\4\3\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d\3\2"+
		"\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2"+
		"\u00a4\7\2\2\3\u00a3\u009a\3\2\2\2\u00a3\u009f\3\2\2\2\u00a4\3\3\2\2\2"+
		"\u00a5\u00b7\5\"\22\2\u00a6\u00a8\5\6\4\2\u00a7\u00a6\3\2\2\2\u00a8\u00ab"+
		"\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ac\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ac\u00b7\5\f\7\2\u00ad\u00af\5\6\4\2\u00ae\u00ad\3\2"+
		"\2\2\u00af\u00b2\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\u00b3\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00b7\5\34\17\2\u00b4\u00b7\5"+
		"J&\2\u00b5\u00b7\7\4\2\2\u00b6\u00a5\3\2\2\2\u00b6\u00a9\3\2\2\2\u00b6"+
		"\u00b0\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b5\3\2\2\2\u00b7\5\3\2\2\2"+
		"\u00b8\u00bc\5R*\2\u00b9\u00bc\5\b\5\2\u00ba\u00bc\5\n\6\2\u00bb\u00b8"+
		"\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\7\3\2\2\2\u00bd"+
		"\u00be\t\2\2\2\u00be\t\3\2\2\2\u00bf\u00c0\t\3\2\2\u00c0\13\3\2\2\2\u00c1"+
		"\u00c2\7\67\2\2\u00c2\u00c4\5\16\b\2\u00c3\u00c5\5\20\t\2\u00c4\u00c3"+
		"\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c8\5\22\n\2"+
		"\u00c7\u00c6\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ca"+
		"\7\5\2\2\u00ca\u00cb\5\24\13\2\u00cb\u00cc\7\6\2\2\u00cc\r\3\2\2\2\u00cd"+
		"\u00ce\7p\2\2\u00ce\17\3\2\2\2\u00cf\u00d0\79\2\2\u00d0\u00d1\5\u0098"+
		"M\2\u00d1\21\3\2\2\2\u00d2\u00d3\7=\2\2\u00d3\u00d8\5\u0098M\2\u00d4\u00d5"+
		"\7\7\2\2\u00d5\u00d7\5\u0098M\2\u00d6\u00d4\3\2\2\2\u00d7\u00da\3\2\2"+
		"\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\23\3\2\2\2\u00da\u00d8"+
		"\3\2\2\2\u00db\u00df\5|?\2\u00dc\u00df\5\26\f\2\u00dd\u00df\5\u0082B\2"+
		"\u00de\u00db\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00dd\3\2\2\2\u00df\u00e2"+
		"\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\25\3\2\2\2\u00e2"+
		"\u00e0\3\2\2\2\u00e3\u00ea\5J&\2\u00e4\u00ea\5\30\r\2\u00e5\u00ea\5.\30"+
		"\2\u00e6\u00ea\5(\25\2\u00e7\u00ea\5> \2\u00e8\u00ea\5\4\3\2\u00e9\u00e3"+
		"\3\2\2\2\u00e9\u00e4\3\2\2\2\u00e9\u00e5\3\2\2\2\u00e9\u00e6\3\2\2\2\u00e9"+
		"\u00e7\3\2\2\2\u00e9\u00e8\3\2\2\2\u00ea\27\3\2\2\2\u00eb\u00ee\5R*\2"+
		"\u00ec\u00ee\5\32\16\2\u00ed\u00eb\3\2\2\2\u00ed\u00ec\3\2\2\2\u00ee\u00f1"+
		"\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f2\3\2\2\2\u00f1"+
		"\u00ef\3\2\2\2\u00f2\u00f3\5\u0084C\2\u00f3\u00f5\7\b\2\2\u00f4\u00f6"+
		"\5\60\31\2\u00f5\u00f4\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2"+
		"\u00f7\u00f8\7\t\2\2\u00f8\31\3\2\2\2\u00f9\u00fd\t\4\2\2\u00fa\u00fd"+
		"\7@\2\2\u00fb\u00fd\7I\2\2\u00fc\u00f9\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc"+
		"\u00fb\3\2\2\2\u00fd\33\3\2\2\2\u00fe\u00ff\7?\2\2\u00ff\u0101\5\36\20"+
		"\2\u0100\u0102\5\20\t\2\u0101\u0100\3\2\2\2\u0101\u0102\3\2\2\2\u0102"+
		"\u0103\3\2\2\2\u0103\u0104\7\5\2\2\u0104\u0105\5 \21\2\u0105\u0106\7\6"+
		"\2\2\u0106\35\3\2\2\2\u0107\u0108\7p\2\2\u0108\37\3\2\2\2\u0109\u010a"+
		"\5\66\34\2\u010a\u010b\7\4\2\2\u010b\u010d\3\2\2\2\u010c\u0109\3\2\2\2"+
		"\u010d\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f!\3"+
		"\2\2\2\u0110\u010e\3\2\2\2\u0111\u0112\7a\2\2\u0112\u0113\5$\23\2\u0113"+
		"\u0114\7b\2\2\u0114\u0115\5&\24\2\u0115\u0116\7\b\2\2\u0116\u011b\7^\2"+
		"\2\u0117\u0118\7\7\2\2\u0118\u011a\7^\2\2\u0119\u0117\3\2\2\2\u011a\u011d"+
		"\3\2\2\2\u011b\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011e\3\2\2\2\u011d"+
		"\u011b\3\2\2\2\u011e\u011f\7\t\2\2\u011f\u0120\7\5\2\2\u0120\u0121\5\24"+
		"\13\2\u0121\u0122\7\6\2\2\u0122#\3\2\2\2\u0123\u0124\7p\2\2\u0124%\3\2"+
		"\2\2\u0125\u0126\7p\2\2\u0126\'\3\2\2\2\u0127\u012a\5R*\2\u0128\u012a"+
		"\5*\26\2\u0129\u0127\3\2\2\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b"+
		"\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012e\3\2\2\2\u012d\u012b\3\2"+
		"\2\2\u012e\u012f\5\u0098M\2\u012f\u0132\5,\27\2\u0130\u0131\7\n\2\2\u0131"+
		"\u0133\5\\/\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u013c\3\2"+
		"\2\2\u0134\u0135\7\7\2\2\u0135\u0138\5,\27\2\u0136\u0137\7\n\2\2\u0137"+
		"\u0139\5\\/\2\u0138\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013b\3\2"+
		"\2\2\u013a\u0134\3\2\2\2\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013c"+
		"\u013d\3\2\2\2\u013d\u013f\3\2\2\2\u013e\u013c\3\2\2\2\u013f\u0140\7\4"+
		"\2\2\u0140)\3\2\2\2\u0141\u0147\t\4\2\2\u0142\u0147\7;\2\2\u0143\u0147"+
		"\7D\2\2\u0144\u0147\7G\2\2\u0145\u0147\7d\2\2\u0146\u0141\3\2\2\2\u0146"+
		"\u0142\3\2\2\2\u0146\u0143\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0145\3\2"+
		"\2\2\u0147+\3\2\2\2\u0148\u0149\7p\2\2\u0149-\3\2\2\2\u014a\u014b\5\66"+
		"\34\2\u014b\u014c\5<\37\2\u014c\u0151\3\2\2\2\u014d\u014e\5\66\34\2\u014e"+
		"\u014f\7\4\2\2\u014f\u0151\3\2\2\2\u0150\u014a\3\2\2\2\u0150\u014d\3\2"+
		"\2\2\u0151/\3\2\2\2\u0152\u0157\5\62\32\2\u0153\u0154\7\7\2\2\u0154\u0156"+
		"\5\62\32\2\u0155\u0153\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3\2\2\2"+
		"\u0157\u0158\3\2\2\2\u0158\61\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015c"+
		"\7;\2\2\u015b\u015a\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\3\2\2\2\u015d"+
		"\u015e\5\u0098M\2\u015e\u015f\5\64\33\2\u015f\63\3\2\2\2\u0160\u0161\7"+
		"p\2\2\u0161\65\3\2\2\2\u0162\u0165\5R*\2\u0163\u0165\58\35\2\u0164\u0162"+
		"\3\2\2\2\u0164\u0163\3\2\2\2\u0165\u0168\3\2\2\2\u0166\u0164\3\2\2\2\u0166"+
		"\u0167\3\2\2\2\u0167\u0169\3\2\2\2\u0168\u0166\3\2\2\2\u0169\u016a\5\u0098"+
		"M\2\u016a\u016b\5:\36\2\u016b\u016d\7\b\2\2\u016c\u016e\5\60\31\2\u016d"+
		"\u016c\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0170\7\t"+
		"\2\2\u0170\67\3\2\2\2\u0171\u0179\t\4\2\2\u0172\u0179\7@\2\2\u0173\u0179"+
		"\7\66\2\2\u0174\u0179\7I\2\2\u0175\u0179\7D\2\2\u0176\u0179\7c\2\2\u0177"+
		"\u0179\7d\2\2\u0178\u0171\3\2\2\2\u0178\u0172\3\2\2\2\u0178\u0173\3\2"+
		"\2\2\u0178\u0174\3\2\2\2\u0178\u0175\3\2\2\2\u0178\u0176\3\2\2\2\u0178"+
		"\u0177\3\2\2\2\u01799\3\2\2\2\u017a\u017b\7p\2\2\u017b;\3\2\2\2\u017c"+
		"\u017d\5z>\2\u017d=\3\2\2\2\u017e\u0181\5R*\2\u017f\u0181\5@!\2\u0180"+
		"\u017e\3\2\2\2\u0180\u017f\3\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180\3\2"+
		"\2\2\u0182\u0183\3\2\2\2\u0183\u0185\3\2\2\2\u0184\u0182\3\2\2\2\u0185"+
		"\u0186\5\u0098M\2\u0186\u0187\5B\"\2\u0187\u018a\7\5\2\2\u0188\u018b\5"+
		"D#\2\u0189\u018b\5F$\2\u018a\u0188\3\2\2\2\u018a\u0189\3\2\2\2\u018b\u018c"+
		"\3\2\2\2\u018c\u018a\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u018f\7\6\2\2\u018f?\3\2\2\2\u0190\u0195\t\4\2\2\u0191\u0195\7;\2\2\u0192"+
		"\u0195\7D\2\2\u0193\u0195\7G\2\2\u0194\u0190\3\2\2\2\u0194\u0191\3\2\2"+
		"\2\u0194\u0192\3\2\2\2\u0194\u0193\3\2\2\2\u0195A\3\2\2\2\u0196\u0197"+
		"\7p\2\2\u0197C\3\2\2\2\u0198\u019a\5H%\2\u0199\u0198\3\2\2\2\u019a\u019d"+
		"\3\2\2\2\u019b\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u01a7\3\2\2\2\u019d"+
		"\u019b\3\2\2\2\u019e\u01a8\7g\2\2\u019f\u01a3\7h\2\2\u01a0\u01a2\5\u0082"+
		"B\2\u01a1\u01a0\3\2\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3"+
		"\u01a4\3\2\2\2\u01a4\u01a6\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6\u01a8\7\6"+
		"\2\2\u01a7\u019e\3\2\2\2\u01a7\u019f\3\2\2\2\u01a8E\3\2\2\2\u01a9\u01ab"+
		"\5H%\2\u01aa\u01a9\3\2\2\2\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac"+
		"\u01ad\3\2\2\2\u01ad\u01b8\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b9\7i"+
		"\2\2\u01b0\u01b4\7j\2\2\u01b1\u01b3\5\u0082B\2\u01b2\u01b1\3\2\2\2\u01b3"+
		"\u01b6\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b7\3\2"+
		"\2\2\u01b6\u01b4\3\2\2\2\u01b7\u01b9\7\6\2\2\u01b8\u01af\3\2\2\2\u01b8"+
		"\u01b0\3\2\2\2\u01b9G\3\2\2\2\u01ba\u01bd\t\4\2\2\u01bb\u01bd\7D\2\2\u01bc"+
		"\u01ba\3\2\2\2\u01bc\u01bb\3\2\2\2\u01bdI\3\2\2\2\u01be\u01c0\5\6\4\2"+
		"\u01bf\u01be\3\2\2\2\u01c0\u01c3\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c1\u01c2"+
		"\3\2\2\2\u01c2\u01c4\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c4\u01c5\78\2\2\u01c5"+
		"\u01c6\5L\'\2\u01c6\u01c8\7\5\2\2\u01c7\u01c9\5N(\2\u01c8\u01c7\3\2\2"+
		"\2\u01c8\u01c9\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cb\7\6\2\2\u01cbK"+
		"\3\2\2\2\u01cc\u01cd\7p\2\2\u01cdM\3\2\2\2\u01ce\u01d3\5P)\2\u01cf\u01d0"+
		"\7\7\2\2\u01d0\u01d2\5P)\2\u01d1\u01cf\3\2\2\2\u01d2\u01d5\3\2\2\2\u01d3"+
		"\u01d1\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4O\3\2\2\2\u01d5\u01d3\3\2\2\2"+
		"\u01d6\u01d7\7p\2\2\u01d7Q\3\2\2\2\u01d8\u01d9\7\13\2\2\u01d9\u01e0\5"+
		"T+\2\u01da\u01dd\7\b\2\2\u01db\u01de\5V,\2\u01dc\u01de\5Z.\2\u01dd\u01db"+
		"\3\2\2\2\u01dd\u01dc\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01df\3\2\2\2\u01df"+
		"\u01e1\7\t\2\2\u01e0\u01da\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1S\3\2\2\2"+
		"\u01e2\u01e3\7p\2\2\u01e3U\3\2\2\2\u01e4\u01e9\5X-\2\u01e5\u01e6\7\7\2"+
		"\2\u01e6\u01e8\5X-\2\u01e7\u01e5\3\2\2\2\u01e8\u01eb\3\2\2\2\u01e9\u01e7"+
		"\3\2\2\2\u01e9\u01ea\3\2\2\2\u01eaW\3\2\2\2\u01eb\u01e9\3\2\2\2\u01ec"+
		"\u01ed\7p\2\2\u01ed\u01ee\7\n\2\2\u01ee\u01ef\5Z.\2\u01efY\3\2\2\2\u01f0"+
		"\u01f3\5\\/\2\u01f1\u01f3\5R*\2\u01f2\u01f0\3\2\2\2\u01f2\u01f1\3\2\2"+
		"\2\u01f3[\3\2\2\2\u01f4\u01f5\b/\1\2\u01f5\u0209\5^\60\2\u01f6\u01f7\7"+
		"K\2\2\u01f7\u0209\5h\65\2\u01f8\u01f9\7\b\2\2\u01f9\u01fa\5j\66\2\u01fa"+
		"\u01fb\7\t\2\2\u01fb\u01fc\5\\/\25\u01fc\u0209\3\2\2\2\u01fd\u01fe\t\5"+
		"\2\2\u01fe\u0209\5\\/\23\u01ff\u0200\t\6\2\2\u0200\u0209\5\\/\22\u0201"+
		"\u0202\7\23\2\2\u0202\u0209\5\\/\21\u0203\u0204\7\b\2\2\u0204\u0205\5"+
		"\u0098M\2\u0205\u0206\7\t\2\2\u0206\u0207\5\\/\4\u0207\u0209\3\2\2\2\u0208"+
		"\u01f4\3\2\2\2\u0208\u01f6\3\2\2\2\u0208\u01f8\3\2\2\2\u0208\u01fd\3\2"+
		"\2\2\u0208\u01ff\3\2\2\2\u0208\u0201\3\2\2\2\u0208\u0203\3\2\2\2\u0209"+
		"\u025b\3\2\2\2\u020a\u020b\f\31\2\2\u020b\u020c\7\f\2\2\u020c\u025a\5"+
		"\\/\32\u020d\u020e\f\20\2\2\u020e\u020f\t\7\2\2\u020f\u025a\5\\/\21\u0210"+
		"\u0211\f\17\2\2\u0211\u0212\t\6\2\2\u0212\u025a\5\\/\20\u0213\u021b\f"+
		"\16\2\2\u0214\u0215\7\27\2\2\u0215\u021c\7\27\2\2\u0216\u0217\7\30\2\2"+
		"\u0217\u0218\7\30\2\2\u0218\u021c\7\30\2\2\u0219\u021a\7\30\2\2\u021a"+
		"\u021c\7\30\2\2\u021b\u0214\3\2\2\2\u021b\u0216\3\2\2\2\u021b\u0219\3"+
		"\2\2\2\u021c\u021d\3\2\2\2\u021d\u025a\5\\/\17\u021e\u022b\f\r\2\2\u021f"+
		"\u0221\7\27\2\2\u0220\u0222\7\64\2\2\u0221\u0220\3\2\2\2\u0221\u0222\3"+
		"\2\2\2\u0222\u0223\3\2\2\2\u0223\u022c\7\n\2\2\u0224\u0226\7\30\2\2\u0225"+
		"\u0227\7\64\2\2\u0226\u0225\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0228\3"+
		"\2\2\2\u0228\u022c\7\n\2\2\u0229\u022c\7\30\2\2\u022a\u022c\7\27\2\2\u022b"+
		"\u021f\3\2\2\2\u022b\u0224\3\2\2\2\u022b\u0229\3\2\2\2\u022b\u022a\3\2"+
		"\2\2\u022c\u022d\3\2\2\2\u022d\u025a\5\\/\16\u022e\u022f\f\13\2\2\u022f"+
		"\u0230\t\b\2\2\u0230\u025a\5\\/\f\u0231\u0232\f\n\2\2\u0232\u0233\7\35"+
		"\2\2\u0233\u025a\5\\/\13\u0234\u0235\f\t\2\2\u0235\u0236\7\36\2\2\u0236"+
		"\u025a\5\\/\n\u0237\u0238\f\b\2\2\u0238\u0239\7\37\2\2\u0239\u025a\5\\"+
		"/\t\u023a\u023b\f\7\2\2\u023b\u023c\7 \2\2\u023c\u025a\5\\/\b\u023d\u023e"+
		"\f\6\2\2\u023e\u023f\7!\2\2\u023f\u025a\5\\/\7\u0240\u0241\f\5\2\2\u0241"+
		"\u0242\7\"\2\2\u0242\u0243\5\\/\2\u0243\u0244\7#\2\2\u0244\u0245\5\\/"+
		"\6\u0245\u025a\3\2\2\2\u0246\u0247\f\3\2\2\u0247\u0248\t\t\2\2\u0248\u025a"+
		"\5\\/\3\u0249\u024a\f\30\2\2\u024a\u024c\7\b\2\2\u024b\u024d\5b\62\2\u024c"+
		"\u024b\3\2\2\2\u024c\u024d\3\2\2\2\u024d\u024e\3\2\2\2\u024e\u025a\7\t"+
		"\2\2\u024f\u0250\f\27\2\2\u0250\u0251\7\r\2\2\u0251\u0252\5\\/\2\u0252"+
		"\u0253\7\16\2\2\u0253\u025a\3\2\2\2\u0254\u0255\f\24\2\2\u0255\u025a\t"+
		"\5\2\2\u0256\u0257\f\f\2\2\u0257\u0258\7>\2\2\u0258\u025a\5\u0098M\2\u0259"+
		"\u020a\3\2\2\2\u0259\u020d\3\2\2\2\u0259\u0210\3\2\2\2\u0259\u0213\3\2"+
		"\2\2\u0259\u021e\3\2\2\2\u0259\u022e\3\2\2\2\u0259\u0231\3\2\2\2\u0259"+
		"\u0234\3\2\2\2\u0259\u0237\3\2\2\2\u0259\u023a\3\2\2\2\u0259\u023d\3\2"+
		"\2\2\u0259\u0240\3\2\2\2\u0259\u0246\3\2\2\2\u0259\u0249\3\2\2\2\u0259"+
		"\u024f\3\2\2\2\u0259\u0254\3\2\2\2\u0259\u0256\3\2\2\2\u025a\u025d\3\2"+
		"\2\2\u025b\u0259\3\2\2\2\u025b\u025c\3\2\2\2\u025c]\3\2\2\2\u025d\u025b"+
		"\3\2\2\2\u025e\u0268\5`\61\2\u025f\u0268\7F\2\2\u0260\u0268\7E\2\2\u0261"+
		"\u0268\5\u0096L\2\u0262\u0268\7p\2\2\u0263\u0264\5\u0098M\2\u0264\u0265"+
		"\7\f\2\2\u0265\u0266\7\67\2\2\u0266\u0268\3\2\2\2\u0267\u025e\3\2\2\2"+
		"\u0267\u025f\3\2\2\2\u0267\u0260\3\2\2\2\u0267\u0261\3\2\2\2\u0267\u0262"+
		"\3\2\2\2\u0267\u0263\3\2\2\2\u0268_\3\2\2\2\u0269\u026a\7\b\2\2\u026a"+
		"\u026b\5\\/\2\u026b\u026c\7\t\2\2\u026ca\3\2\2\2\u026d\u0272\5\\/\2\u026e"+
		"\u026f\7\7\2\2\u026f\u0271\5\\/\2\u0270\u026e\3\2\2\2\u0271\u0274\3\2"+
		"\2\2\u0272\u0270\3\2\2\2\u0272\u0273\3\2\2\2\u0273c\3\2\2\2\u0274\u0272"+
		"\3\2\2\2\u0275\u0276\7`\2\2\u0276\u0278\7\b\2\2\u0277\u0279\5b\62\2\u0278"+
		"\u0277\3\2\2\2\u0278\u0279\3\2\2\2\u0279\u027a\3\2\2\2\u027a\u027b\7\t"+
		"\2\2\u027b\u027c\5z>\2\u027ce\3\2\2\2\u027d\u027e\7Z\2\2\u027e\u027f\5"+
		"\\/\2\u027f\u0280\5\\/\2\u0280\u0288\3\2\2\2\u0281\u0282\7]\2\2\u0282"+
		"\u0283\5\\/\2\u0283\u0284\5\\/\2\u0284\u0288\3\2\2\2\u0285\u0286\t\n\2"+
		"\2\u0286\u0288\5\\/\2\u0287\u027d\3\2\2\2\u0287\u0281\3\2\2\2\u0287\u0285"+
		"\3\2\2\2\u0288g\3\2\2\2\u0289\u028a\5\u0098M\2\u028a\u028b\7\b\2\2\u028b"+
		"\u028c\7\t\2\2\u028c\u0298\3\2\2\2\u028d\u028e\5\u0098M\2\u028e\u028f"+
		"\5`\61\2\u028f\u0298\3\2\2\2\u0290\u0295\5\u0098M\2\u0291\u0296\5r:\2"+
		"\u0292\u0296\5l\67\2\u0293\u0296\5n8\2\u0294\u0296\5p9\2\u0295\u0291\3"+
		"\2\2\2\u0295\u0292\3\2\2\2\u0295\u0293\3\2\2\2\u0295\u0294\3\2\2\2\u0296"+
		"\u0298\3\2\2\2\u0297\u0289\3\2\2\2\u0297\u028d\3\2\2\2\u0297\u0290\3\2"+
		"\2\2\u0298i\3\2\2\2\u0299\u029a\7\27\2\2\u029a\u029f\5\u0098M\2\u029b"+
		"\u029c\7\7\2\2\u029c\u029e\5\u0098M\2\u029d\u029b\3\2\2\2\u029e\u02a1"+
		"\3\2\2\2\u029f\u029d\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0\u02a2\3\2\2\2\u02a1"+
		"\u029f\3\2\2\2\u02a2\u02a3\7\30\2\2\u02a3k\3\2\2\2\u02a4\u02c0\7\r\2\2"+
		"\u02a5\u02aa\7\16\2\2\u02a6\u02a7\7\r\2\2\u02a7\u02a9\7\16\2\2\u02a8\u02a6"+
		"\3\2\2\2\u02a9\u02ac\3\2\2\2\u02aa\u02a8\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab"+
		"\u02ad\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ad\u02c1\5v<\2\u02ae\u02af\5\\/"+
		"\2\u02af\u02b6\7\16\2\2\u02b0\u02b1\7\r\2\2\u02b1\u02b2\5\\/\2\u02b2\u02b3"+
		"\7\16\2\2\u02b3\u02b5\3\2\2\2\u02b4\u02b0\3\2\2\2\u02b5\u02b8\3\2\2\2"+
		"\u02b6\u02b4\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02bd\3\2\2\2\u02b8\u02b6"+
		"\3\2\2\2\u02b9\u02ba\7\r\2\2\u02ba\u02bc\7\16\2\2\u02bb\u02b9\3\2\2\2"+
		"\u02bc\u02bf\3\2\2\2\u02bd\u02bb\3\2\2\2\u02bd\u02be\3\2\2\2\u02be\u02c1"+
		"\3\2\2\2\u02bf\u02bd\3\2\2\2\u02c0\u02a5\3\2\2\2\u02c0\u02ae\3\2\2\2\u02c1"+
		"m\3\2\2\2\u02c2\u02db\7\5\2\2\u02c3\u02c6\5\u0096L\2\u02c4\u02c6\5\\/"+
		"\2\u02c5\u02c3\3\2\2\2\u02c5\u02c4\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7\u02ca"+
		"\7.\2\2\u02c8\u02cb\5\u0096L\2\u02c9\u02cb\5\\/\2\u02ca\u02c8\3\2\2\2"+
		"\u02ca\u02c9\3\2\2\2\u02cb\u02d8\3\2\2\2\u02cc\u02cf\7\7\2\2\u02cd\u02d0"+
		"\5\u0096L\2\u02ce\u02d0\5\\/\2\u02cf\u02cd\3\2\2\2\u02cf\u02ce\3\2\2\2"+
		"\u02d0\u02d1\3\2\2\2\u02d1\u02d4\7.\2\2\u02d2\u02d5\5\u0096L\2\u02d3\u02d5"+
		"\5\\/\2\u02d4\u02d2\3\2\2\2\u02d4\u02d3\3\2\2\2\u02d5\u02d7\3\2\2\2\u02d6"+
		"\u02cc\3\2\2\2\u02d7\u02da\3\2\2\2\u02d8\u02d6\3\2\2\2\u02d8\u02d9\3\2"+
		"\2\2\u02d9\u02dc\3\2\2\2\u02da\u02d8\3\2\2\2\u02db\u02c5\3\2\2\2\u02db"+
		"\u02dc\3\2\2\2\u02dc\u02dd\3\2\2\2\u02dd\u02de\7\6\2\2\u02deo\3\2\2\2"+
		"\u02df\u02e2\7\5\2\2\u02e0\u02e3\5\u0096L\2\u02e1\u02e3\5\\/\2\u02e2\u02e0"+
		"\3\2\2\2\u02e2\u02e1\3\2\2\2\u02e3\u02eb\3\2\2\2\u02e4\u02e7\7\7\2\2\u02e5"+
		"\u02e8\5\u0096L\2\u02e6\u02e8\5\\/\2\u02e7\u02e5\3\2\2\2\u02e7\u02e6\3"+
		"\2\2\2\u02e8\u02ea\3\2\2\2\u02e9\u02e4\3\2\2\2\u02ea\u02ed\3\2\2\2\u02eb"+
		"\u02e9\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ee\3\2\2\2\u02ed\u02eb\3\2"+
		"\2\2\u02ee\u02ef\7\6\2\2\u02efq\3\2\2\2\u02f0\u02f1\5x=\2\u02f1s\3\2\2"+
		"\2\u02f2\u02f5\5v<\2\u02f3\u02f5\5\\/\2\u02f4\u02f2\3\2\2\2\u02f4\u02f3"+
		"\3\2\2\2\u02f5u\3\2\2\2\u02f6\u0302\7\5\2\2\u02f7\u02fc\5t;\2\u02f8\u02f9"+
		"\7\7\2\2\u02f9\u02fb\5t;\2\u02fa\u02f8\3\2\2\2\u02fb\u02fe\3\2\2\2\u02fc"+
		"\u02fa\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd\u0300\3\2\2\2\u02fe\u02fc\3\2"+
		"\2\2\u02ff\u0301\7\7\2\2\u0300\u02ff\3\2\2\2\u0300\u0301\3\2\2\2\u0301"+
		"\u0303\3\2\2\2\u0302\u02f7\3\2\2\2\u0302\u0303\3\2\2\2\u0303\u0304\3\2"+
		"\2\2\u0304\u0305\7\6\2\2\u0305w\3\2\2\2\u0306\u0308\7\b\2\2\u0307\u0309"+
		"\5b\62\2\u0308\u0307\3\2\2\2\u0308\u0309\3\2\2\2\u0309\u030a\3\2\2\2\u030a"+
		"\u030b\7\t\2\2\u030by\3\2\2\2\u030c\u0310\7\5\2\2\u030d\u030f\5\u0082"+
		"B\2\u030e\u030d\3\2\2\2\u030f\u0312\3\2\2\2\u0310\u030e\3\2\2\2\u0310"+
		"\u0311\3\2\2\2\u0311\u0313\3\2\2\2\u0312\u0310\3\2\2\2\u0313\u0314\7\6"+
		"\2\2\u0314{\3\2\2\2\u0315\u0316\7D\2\2\u0316\u0317\5z>\2\u0317}\3\2\2"+
		"\2\u0318\u031a\5\u0080A\2\u0319\u0318\3\2\2\2\u031a\u031d\3\2\2\2\u031b"+
		"\u0319\3\2\2\2\u031b\u031c\3\2\2\2\u031c\u031e\3\2\2\2\u031d\u031b\3\2"+
		"\2\2\u031e\u031f\5\u0098M\2\u031f\u0322\5,\27\2\u0320\u0321\7\n\2\2\u0321"+
		"\u0323\5\\/\2\u0322\u0320\3\2\2\2\u0322\u0323\3\2\2\2\u0323\u032c\3\2"+
		"\2\2\u0324\u0325\7\7\2\2\u0325\u0328\5,\27\2\u0326\u0327\7\n\2\2\u0327"+
		"\u0329\5\\/\2\u0328\u0326\3\2\2\2\u0328\u0329\3\2\2\2\u0329\u032b\3\2"+
		"\2\2\u032a\u0324\3\2\2\2\u032b\u032e\3\2\2\2\u032c\u032a\3\2\2\2\u032c"+
		"\u032d\3\2\2\2\u032d\177\3\2\2\2\u032e\u032c\3\2\2\2\u032f\u0330\t\13"+
		"\2\2\u0330\u0081\3\2\2\2\u0331\u0332\5~@\2\u0332\u0333\7\4\2\2\u0333\u0336"+
		"\3\2\2\2\u0334\u0336\5\u0094K\2\u0335\u0331\3\2\2\2\u0335\u0334\3\2\2"+
		"\2\u0336\u0083\3\2\2\2\u0337\u033c\7p\2\2\u0338\u0339\7\f\2\2\u0339\u033b"+
		"\7p\2\2\u033a\u0338\3\2\2\2\u033b\u033e\3\2\2\2\u033c\u033a\3\2\2\2\u033c"+
		"\u033d\3\2\2\2\u033d\u0085\3\2\2\2\u033e\u033c\3\2\2\2\u033f\u034c\5\u008a"+
		"F\2\u0340\u0342\5\u0088E\2\u0341\u0340\3\2\2\2\u0341\u0342\3\2\2\2\u0342"+
		"\u0343\3\2\2\2\u0343\u0345\7\4\2\2\u0344\u0346\5\\/\2\u0345\u0344\3\2"+
		"\2\2\u0345\u0346\3\2\2\2\u0346\u0347\3\2\2\2\u0347\u0349\7\4\2\2\u0348"+
		"\u034a\5\u008cG\2\u0349\u0348\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u034c"+
		"\3\2\2\2\u034b\u033f\3\2\2\2\u034b\u0341\3\2\2\2\u034c\u0087\3\2\2\2\u034d"+
		"\u0350\5~@\2\u034e\u0350\5b\62\2\u034f\u034d\3\2\2\2\u034f\u034e\3\2\2"+
		"\2\u0350\u0089\3\2\2\2\u0351\u0352\5\u0098M\2\u0352\u0353\5,\27\2\u0353"+
		"\u0354\7#\2\2\u0354\u0355\5\\/\2\u0355\u008b\3\2\2\2\u0356\u0357\5b\62"+
		"\2\u0357\u008d\3\2\2\2\u0358\u0359\7/\2\2\u0359\u035a\7\b\2\2\u035a\u035b"+
		"\5\u0090I\2\u035b\u035c\5,\27\2\u035c\u035d\7\t\2\2\u035d\u035e\5z>\2"+
		"\u035e\u008f\3\2\2\2\u035f\u0364\5\u0084C\2\u0360\u0361\7\37\2\2\u0361"+
		"\u0363\5\u0084C\2\u0362\u0360\3\2\2\2\u0363\u0366\3\2\2\2\u0364\u0362"+
		"\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0091\3\2\2\2\u0366\u0364\3\2\2\2\u0367"+
		"\u0368\7\60\2\2\u0368\u0369\5z>\2\u0369\u0093\3\2\2\2\u036a\u03a5\5z>"+
		"\2\u036b\u036c\7L\2\2\u036c\u03a5\7\4\2\2\u036d\u036e\7M\2\2\u036e\u03a5"+
		"\7\4\2\2\u036f\u0370\7N\2\2\u0370\u0371\5\u0094K\2\u0371\u0372\7U\2\2"+
		"\u0372\u0373\5`\61\2\u0373\u0374\7\4\2\2\u0374\u03a5\3\2\2\2\u0375\u0376"+
		"\7P\2\2\u0376\u0377\7\b\2\2\u0377\u0378\5\u0086D\2\u0378\u0379\7\t\2\2"+
		"\u0379\u037a\5\u0094K\2\u037a\u03a5\3\2\2\2\u037b\u037c\7Q\2\2\u037c\u037d"+
		"\5`\61\2\u037d\u0380\5\u0094K\2\u037e\u037f\7O\2\2\u037f\u0381\5\u0094"+
		"K\2\u0380\u037e\3\2\2\2\u0380\u0381\3\2\2\2\u0381\u03a5\3\2\2\2\u0382"+
		"\u0384\7R\2\2\u0383\u0385\5\\/\2\u0384\u0383\3\2\2\2\u0384\u0385\3\2\2"+
		"\2\u0385\u0386\3\2\2\2\u0386\u03a5\7\4\2\2\u0387\u0388\7S\2\2\u0388\u0389"+
		"\5\\/\2\u0389\u038a\7\4\2\2\u038a\u03a5\3\2\2\2\u038b\u038c\7T\2\2\u038c"+
		"\u0396\5z>\2\u038d\u038f\5\u008eH\2\u038e\u038d\3\2\2\2\u038f\u0390\3"+
		"\2\2\2\u0390\u038e\3\2\2\2\u0390\u0391\3\2\2\2\u0391\u0393\3\2\2\2\u0392"+
		"\u0394\5\u0092J\2\u0393\u0392\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0397"+
		"\3\2\2\2\u0395\u0397\5\u0092J\2\u0396\u038e\3\2\2\2\u0396\u0395\3\2\2"+
		"\2\u0397\u03a5\3\2\2\2\u0398\u0399\7U\2\2\u0399\u039a\5`\61\2\u039a\u039b"+
		"\5\u0094K\2\u039b\u03a5\3\2\2\2\u039c\u03a5\7\4\2\2\u039d\u03a5\5d\63"+
		"\2\u039e\u039f\5f\64\2\u039f\u03a0\7\4\2\2\u03a0\u03a5\3\2\2\2\u03a1\u03a2"+
		"\5\\/\2\u03a2\u03a3\7\4\2\2\u03a3\u03a5\3\2\2\2\u03a4\u036a\3\2\2\2\u03a4"+
		"\u036b\3\2\2\2\u03a4\u036d\3\2\2\2\u03a4\u036f\3\2\2\2\u03a4\u0375\3\2"+
		"\2\2\u03a4\u037b\3\2\2\2\u03a4\u0382\3\2\2\2\u03a4\u0387\3\2\2\2\u03a4"+
		"\u038b\3\2\2\2\u03a4\u0398\3\2\2\2\u03a4\u039c\3\2\2\2\u03a4\u039d\3\2"+
		"\2\2\u03a4\u039e\3\2\2\2\u03a4\u03a1\3\2\2\2\u03a5\u0095\3\2\2\2\u03a6"+
		"\u03ae\7n\2\2\u03a7\u03ae\7o\2\2\u03a8\u03ae\7k\2\2\u03a9\u03ae\7\65\2"+
		"\2\u03aa\u03ae\7<\2\2\u03ab\u03ae\7m\2\2\u03ac\u03ae\7l\2\2\u03ad\u03a6"+
		"\3\2\2\2\u03ad\u03a7\3\2\2\2\u03ad\u03a8\3\2\2\2\u03ad\u03a9\3\2\2\2\u03ad"+
		"\u03aa\3\2\2\2\u03ad\u03ab\3\2\2\2\u03ad\u03ac\3\2\2\2\u03ae\u0097\3\2"+
		"\2\2\u03af\u03b9\7J\2\2\u03b0\u03b1\5\u0084C\2\u03b1\u03b2\7\r\2\2\u03b2"+
		"\u03b3\7\16\2\2\u03b3\u03b9\3\2\2\2\u03b4\u03b6\5\u0084C\2\u03b5\u03b7"+
		"\5j\66\2\u03b6\u03b5\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03b9\3\2\2\2\u03b8"+
		"\u03af\3\2\2\2\u03b8\u03b0\3\2\2\2\u03b8\u03b4\3\2\2\2\u03b9\u0099\3\2"+
		"\2\2l\u009f\u00a3\u00a9\u00b0\u00b6\u00bb\u00c4\u00c7\u00d8\u00de\u00e0"+
		"\u00e9\u00ed\u00ef\u00f5\u00fc\u0101\u010e\u011b\u0129\u012b\u0132\u0138"+
		"\u013c\u0146\u0150\u0157\u015b\u0164\u0166\u016d\u0178\u0180\u0182\u018a"+
		"\u018c\u0194\u019b\u01a3\u01a7\u01ac\u01b4\u01b8\u01bc\u01c1\u01c8\u01d3"+
		"\u01dd\u01e0\u01e9\u01f2\u0208\u021b\u0221\u0226\u022b\u024c\u0259\u025b"+
		"\u0267\u0272\u0278\u0287\u0295\u0297\u029f\u02aa\u02b6\u02bd\u02c0\u02c5"+
		"\u02ca\u02cf\u02d4\u02d8\u02db\u02e2\u02e7\u02eb\u02f4\u02fc\u0300\u0302"+
		"\u0308\u0310\u031b\u0322\u0328\u032c\u0335\u033c\u0341\u0345\u0349\u034b"+
		"\u034f\u0364\u0380\u0384\u0390\u0393\u0396\u03a4\u03ad\u03b6\u03b8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}