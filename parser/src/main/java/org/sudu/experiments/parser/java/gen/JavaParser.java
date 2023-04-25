// Generated from parser/src/main/resources/grammar/java/JavaParser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.java.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ABSTRACT=1, ASSERT=2, BOOLEAN=3, BREAK=4, BYTE=5, CASE=6, CATCH=7, CHAR=8, 
		CLASS=9, CONST=10, CONTINUE=11, DEFAULT=12, DO=13, DOUBLE=14, ELSE=15, 
		ENUM=16, EXTENDS=17, FINAL=18, FINALLY=19, FLOAT=20, FOR=21, IF=22, GOTO=23, 
		IMPLEMENTS=24, IMPORT=25, INSTANCEOF=26, INT=27, INTERFACE=28, LONG=29, 
		NATIVE=30, NEW=31, PACKAGE=32, PRIVATE=33, PROTECTED=34, PUBLIC=35, RETURN=36, 
		SHORT=37, STATIC=38, STRICTFP=39, SUPER=40, SWITCH=41, SYNCHRONIZED=42, 
		THIS=43, THROW=44, THROWS=45, TRANSIENT=46, TRY=47, VOID=48, VOLATILE=49, 
		WHILE=50, MODULE=51, OPEN=52, REQUIRES=53, EXPORTS=54, OPENS=55, TO=56, 
		USES=57, PROVIDES=58, WITH=59, TRANSITIVE=60, VAR=61, YIELD=62, RECORD=63, 
		SEALED=64, PERMITS=65, NON_SEALED=66, DECIMAL_LITERAL=67, HEX_LITERAL=68, 
		OCT_LITERAL=69, BINARY_LITERAL=70, FLOAT_LITERAL=71, HEX_FLOAT_LITERAL=72, 
		BOOL_LITERAL=73, CHAR_LITERAL=74, STRING_LITERAL=75, TEXT_BLOCK=76, NULL_LITERAL=77, 
		LPAREN=78, RPAREN=79, LBRACE=80, RBRACE=81, LBRACK=82, RBRACK=83, SEMI=84, 
		COMMA=85, DOT=86, ASSIGN=87, GT=88, LT=89, BANG=90, TILDE=91, QUESTION=92, 
		COLON=93, EQUAL=94, LE=95, GE=96, NOTEQUAL=97, AND=98, OR=99, INC=100, 
		DEC=101, ADD=102, SUB=103, MUL=104, DIV=105, BITAND=106, BITOR=107, CARET=108, 
		MOD=109, ADD_ASSIGN=110, SUB_ASSIGN=111, MUL_ASSIGN=112, DIV_ASSIGN=113, 
		AND_ASSIGN=114, OR_ASSIGN=115, XOR_ASSIGN=116, MOD_ASSIGN=117, LSHIFT_ASSIGN=118, 
		RSHIFT_ASSIGN=119, URSHIFT_ASSIGN=120, ARROW=121, COLONCOLON=122, AT=123, 
		ELLIPSIS=124, WS=125, COMMENT=126, LINE_COMMENT=127, NEW_LINE=128, IDENTIFIER=129;
	public static final int
		RULE_compilationUnit = 0, RULE_packageDeclaration = 1, RULE_importDeclaration = 2, 
		RULE_typeDeclaration = 3, RULE_modifier = 4, RULE_classOrInterfaceModifier = 5, 
		RULE_variableModifier = 6, RULE_classDeclaration = 7, RULE_typeParameters = 8, 
		RULE_typeParameter = 9, RULE_typeBound = 10, RULE_enumDeclaration = 11, 
		RULE_enumConstants = 12, RULE_enumConstant = 13, RULE_enumBodyDeclarations = 14, 
		RULE_interfaceDeclaration = 15, RULE_classBody = 16, RULE_interfaceBody = 17, 
		RULE_classBodyDeclaration = 18, RULE_memberDeclaration = 19, RULE_methodDeclaration = 20, 
		RULE_methodBody = 21, RULE_typeTypeOrVoid = 22, RULE_genericMethodDeclaration = 23, 
		RULE_genericConstructorDeclaration = 24, RULE_constructorDeclaration = 25, 
		RULE_compactConstructorDeclaration = 26, RULE_fieldDeclaration = 27, RULE_interfaceBodyDeclaration = 28, 
		RULE_interfaceMemberDeclaration = 29, RULE_constDeclaration = 30, RULE_constantDeclarator = 31, 
		RULE_interfaceMethodDeclaration = 32, RULE_interfaceMethodModifier = 33, 
		RULE_genericInterfaceMethodDeclaration = 34, RULE_interfaceCommonBodyDeclaration = 35, 
		RULE_variableDeclarators = 36, RULE_variableDeclarator = 37, RULE_variableDeclaratorId = 38, 
		RULE_variableInitializer = 39, RULE_arrayInitializer = 40, RULE_classOrInterfaceType = 41, 
		RULE_typeArgument = 42, RULE_qualifiedNameList = 43, RULE_formalParameters = 44, 
		RULE_receiverParameter = 45, RULE_formalParameterList = 46, RULE_formalParameter = 47, 
		RULE_lastFormalParameter = 48, RULE_lambdaLVTIList = 49, RULE_lambdaLVTIParameter = 50, 
		RULE_qualifiedName = 51, RULE_literal = 52, RULE_integerLiteral = 53, 
		RULE_floatLiteral = 54, RULE_altAnnotationQualifiedName = 55, RULE_annotation = 56, 
		RULE_elementValuePairs = 57, RULE_elementValuePair = 58, RULE_elementValue = 59, 
		RULE_elementValueArrayInitializer = 60, RULE_annotationTypeDeclaration = 61, 
		RULE_annotationTypeBody = 62, RULE_annotationTypeElementDeclaration = 63, 
		RULE_annotationTypeElementRest = 64, RULE_annotationMethodOrConstantRest = 65, 
		RULE_annotationMethodRest = 66, RULE_annotationConstantRest = 67, RULE_defaultValue = 68, 
		RULE_moduleDeclaration = 69, RULE_moduleBody = 70, RULE_moduleDirective = 71, 
		RULE_requiresModifier = 72, RULE_recordDeclaration = 73, RULE_recordHeader = 74, 
		RULE_recordComponentList = 75, RULE_recordComponent = 76, RULE_recordBody = 77, 
		RULE_block = 78, RULE_blockStatement = 79, RULE_localVariableDeclaration = 80, 
		RULE_identifier = 81, RULE_typeIdentifier = 82, RULE_localTypeDeclaration = 83, 
		RULE_statement = 84, RULE_catchClause = 85, RULE_catchType = 86, RULE_finallyBlock = 87, 
		RULE_resourceSpecification = 88, RULE_resources = 89, RULE_resource = 90, 
		RULE_switchBlockStatementGroup = 91, RULE_switchLabel = 92, RULE_forControl = 93, 
		RULE_forInit = 94, RULE_enhancedForControl = 95, RULE_parExpression = 96, 
		RULE_expressionList = 97, RULE_methodCall = 98, RULE_expression = 99, 
		RULE_pattern = 100, RULE_lambdaExpression = 101, RULE_lambdaParameters = 102, 
		RULE_lambdaBody = 103, RULE_primary = 104, RULE_switchExpression = 105, 
		RULE_switchLabeledRule = 106, RULE_guardedPattern = 107, RULE_switchRuleOutcome = 108, 
		RULE_classType = 109, RULE_creator = 110, RULE_createdName = 111, RULE_innerCreator = 112, 
		RULE_arrayCreatorRest = 113, RULE_classCreatorRest = 114, RULE_explicitGenericInvocation = 115, 
		RULE_typeArgumentsOrDiamond = 116, RULE_nonWildcardTypeArgumentsOrDiamond = 117, 
		RULE_nonWildcardTypeArguments = 118, RULE_typeList = 119, RULE_typeType = 120, 
		RULE_primitiveType = 121, RULE_typeArguments = 122, RULE_superSuffix = 123, 
		RULE_explicitGenericInvocationSuffix = 124, RULE_arguments = 125, RULE_classOrInterfaceBodyDeclaration = 126, 
		RULE_unknownInterval = 127, RULE_anySeq = 128;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnit", "packageDeclaration", "importDeclaration", "typeDeclaration", 
			"modifier", "classOrInterfaceModifier", "variableModifier", "classDeclaration", 
			"typeParameters", "typeParameter", "typeBound", "enumDeclaration", "enumConstants", 
			"enumConstant", "enumBodyDeclarations", "interfaceDeclaration", "classBody", 
			"interfaceBody", "classBodyDeclaration", "memberDeclaration", "methodDeclaration", 
			"methodBody", "typeTypeOrVoid", "genericMethodDeclaration", "genericConstructorDeclaration", 
			"constructorDeclaration", "compactConstructorDeclaration", "fieldDeclaration", 
			"interfaceBodyDeclaration", "interfaceMemberDeclaration", "constDeclaration", 
			"constantDeclarator", "interfaceMethodDeclaration", "interfaceMethodModifier", 
			"genericInterfaceMethodDeclaration", "interfaceCommonBodyDeclaration", 
			"variableDeclarators", "variableDeclarator", "variableDeclaratorId", 
			"variableInitializer", "arrayInitializer", "classOrInterfaceType", "typeArgument", 
			"qualifiedNameList", "formalParameters", "receiverParameter", "formalParameterList", 
			"formalParameter", "lastFormalParameter", "lambdaLVTIList", "lambdaLVTIParameter", 
			"qualifiedName", "literal", "integerLiteral", "floatLiteral", "altAnnotationQualifiedName", 
			"annotation", "elementValuePairs", "elementValuePair", "elementValue", 
			"elementValueArrayInitializer", "annotationTypeDeclaration", "annotationTypeBody", 
			"annotationTypeElementDeclaration", "annotationTypeElementRest", "annotationMethodOrConstantRest", 
			"annotationMethodRest", "annotationConstantRest", "defaultValue", "moduleDeclaration", 
			"moduleBody", "moduleDirective", "requiresModifier", "recordDeclaration", 
			"recordHeader", "recordComponentList", "recordComponent", "recordBody", 
			"block", "blockStatement", "localVariableDeclaration", "identifier", 
			"typeIdentifier", "localTypeDeclaration", "statement", "catchClause", 
			"catchType", "finallyBlock", "resourceSpecification", "resources", "resource", 
			"switchBlockStatementGroup", "switchLabel", "forControl", "forInit", 
			"enhancedForControl", "parExpression", "expressionList", "methodCall", 
			"expression", "pattern", "lambdaExpression", "lambdaParameters", "lambdaBody", 
			"primary", "switchExpression", "switchLabeledRule", "guardedPattern", 
			"switchRuleOutcome", "classType", "creator", "createdName", "innerCreator", 
			"arrayCreatorRest", "classCreatorRest", "explicitGenericInvocation", 
			"typeArgumentsOrDiamond", "nonWildcardTypeArgumentsOrDiamond", "nonWildcardTypeArguments", 
			"typeList", "typeType", "primitiveType", "typeArguments", "superSuffix", 
			"explicitGenericInvocationSuffix", "arguments", "classOrInterfaceBodyDeclaration", 
			"unknownInterval", "anySeq"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", "'case'", 
			"'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'", 
			"'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'", 
			"'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'", 
			"'int'", "'interface'", "'long'", "'native'", "'new'", "'package'", "'private'", 
			"'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", 
			"'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", 
			"'transient'", "'try'", "'void'", "'volatile'", "'while'", "'module'", 
			"'open'", "'requires'", "'exports'", "'opens'", "'to'", "'uses'", "'provides'", 
			"'with'", "'transitive'", "'var'", "'yield'", "'record'", "'sealed'", 
			"'permits'", "'non-sealed'", null, null, null, null, null, null, null, 
			null, null, null, "'null'", "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", 
			"'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", 
			"'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", "'*='", 
			"'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='", "'->'", 
			"'::'", "'@'", "'...'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", 
			"CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", 
			"ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", 
			"IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", 
			"NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", 
			"STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", 
			"THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "MODULE", 
			"OPEN", "REQUIRES", "EXPORTS", "OPENS", "TO", "USES", "PROVIDES", "WITH", 
			"TRANSITIVE", "VAR", "YIELD", "RECORD", "SEALED", "PERMITS", "NON_SEALED", 
			"DECIMAL_LITERAL", "HEX_LITERAL", "OCT_LITERAL", "BINARY_LITERAL", "FLOAT_LITERAL", 
			"HEX_FLOAT_LITERAL", "BOOL_LITERAL", "CHAR_LITERAL", "STRING_LITERAL", 
			"TEXT_BLOCK", "NULL_LITERAL", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", 
			"TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", 
			"OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", 
			"MOD", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", 
			"OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", 
			"URSHIFT_ASSIGN", "ARROW", "COLONCOLON", "AT", "ELLIPSIS", "WS", "COMMENT", 
			"LINE_COMMENT", "NEW_LINE", "IDENTIFIER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
	public String getGrammarFileName() { return "JavaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitContext extends ParserRuleContext {
		public PackageDeclarationContext packageDeclaration() {
			return getRuleContext(PackageDeclarationContext.class,0);
		}
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public ModuleDeclarationContext moduleDeclaration() {
			return getRuleContext(ModuleDeclarationContext.class,0);
		}
		public TerminalNode EOF() { return getToken(JavaParser.EOF, 0); }
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCompilationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCompilationUnit(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		int _la;
		try {
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(259);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(258);
					packageDeclaration();
					}
					break;
				}
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IMPORT) {
					{
					{
					setState(261);
					importDeclaration();
					}
					}
					setState(266);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2250914781658622L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752304472071L) != 0) || _la==IDENTIFIER) {
					{
					{
					setState(267);
					typeDeclaration();
					}
					}
					setState(272);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(273);
				moduleDeclaration();
				setState(274);
				match(EOF);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PackageDeclarationContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(JavaParser.PACKAGE, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterPackageDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitPackageDeclaration(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(278);
				annotation();
				}
				}
				setState(283);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(284);
			match(PACKAGE);
			setState(285);
			qualifiedName();
			setState(286);
			match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDeclarationContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(JavaParser.IMPORT, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public TerminalNode DOT() { return getToken(JavaParser.DOT, 0); }
		public TerminalNode MUL() { return getToken(JavaParser.MUL, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitImportDeclaration(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(IMPORT);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(289);
				match(STATIC);
				}
			}

			setState(292);
			qualifiedName();
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(293);
				match(DOT);
				setState(294);
				match(MUL);
				}
			}

			setState(297);
			match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeDeclaration(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeDeclaration);
		try {
			int _alt;
			setState(313);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case CLASS:
			case ENUM:
			case FINAL:
			case INTERFACE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case NON_SEALED:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(299);
						classOrInterfaceModifier();
						}
						} 
					}
					setState(304);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(310);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(305);
					classDeclaration();
					}
					break;
				case ENUM:
					{
					setState(306);
					enumDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(307);
					interfaceDeclaration();
					}
					break;
				case AT:
					{
					setState(308);
					annotationTypeDeclaration();
					}
					break;
				case RECORD:
					{
					setState(309);
					recordDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(312);
				match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ModifierContext extends ParserRuleContext {
		public ClassOrInterfaceModifierContext classOrInterfaceModifier() {
			return getRuleContext(ClassOrInterfaceModifierContext.class,0);
		}
		public TerminalNode NATIVE() { return getToken(JavaParser.NATIVE, 0); }
		public TerminalNode SYNCHRONIZED() { return getToken(JavaParser.SYNCHRONIZED, 0); }
		public TerminalNode TRANSIENT() { return getToken(JavaParser.TRANSIENT, 0); }
		public TerminalNode VOLATILE() { return getToken(JavaParser.VOLATILE, 0); }
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitModifier(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modifier);
		try {
			setState(320);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case FINAL:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case NON_SEALED:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(315);
				classOrInterfaceModifier();
				}
				break;
			case NATIVE:
				enterOuterAlt(_localctx, 2);
				{
				setState(316);
				match(NATIVE);
				}
				break;
			case SYNCHRONIZED:
				enterOuterAlt(_localctx, 3);
				{
				setState(317);
				match(SYNCHRONIZED);
				}
				break;
			case TRANSIENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(318);
				match(TRANSIENT);
				}
				break;
			case VOLATILE:
				enterOuterAlt(_localctx, 5);
				{
				setState(319);
				match(VOLATILE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassOrInterfaceModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public TerminalNode PUBLIC() { return getToken(JavaParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(JavaParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(JavaParser.PRIVATE, 0); }
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public TerminalNode ABSTRACT() { return getToken(JavaParser.ABSTRACT, 0); }
		public TerminalNode FINAL() { return getToken(JavaParser.FINAL, 0); }
		public TerminalNode STRICTFP() { return getToken(JavaParser.STRICTFP, 0); }
		public TerminalNode SEALED() { return getToken(JavaParser.SEALED, 0); }
		public TerminalNode NON_SEALED() { return getToken(JavaParser.NON_SEALED, 0); }
		public ClassOrInterfaceModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassOrInterfaceModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassOrInterfaceModifier(this);
		}
	}

	public final ClassOrInterfaceModifierContext classOrInterfaceModifier() throws RecognitionException {
		ClassOrInterfaceModifierContext _localctx = new ClassOrInterfaceModifierContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_classOrInterfaceModifier);
		try {
			setState(332);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(322);
				annotation();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(323);
				match(PUBLIC);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(324);
				match(PROTECTED);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(325);
				match(PRIVATE);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(326);
				match(STATIC);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(327);
				match(ABSTRACT);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(328);
				match(FINAL);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(329);
				match(STRICTFP);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(330);
				match(SEALED);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(331);
				match(NON_SEALED);
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

	@SuppressWarnings("CheckReturnValue")
	public static class VariableModifierContext extends ParserRuleContext {
		public TerminalNode FINAL() { return getToken(JavaParser.FINAL, 0); }
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public VariableModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterVariableModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitVariableModifier(this);
		}
	}

	public final VariableModifierContext variableModifier() throws RecognitionException {
		VariableModifierContext _localctx = new VariableModifierContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_variableModifier);
		try {
			setState(336);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FINAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(334);
				match(FINAL);
				}
				break;
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(JavaParser.CLASS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(JavaParser.EXTENDS, 0); }
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode IMPLEMENTS() { return getToken(JavaParser.IMPLEMENTS, 0); }
		public List<TypeListContext> typeList() {
			return getRuleContexts(TypeListContext.class);
		}
		public TypeListContext typeList(int i) {
			return getRuleContext(TypeListContext.class,i);
		}
		public TerminalNode PERMITS() { return getToken(JavaParser.PERMITS, 0); }
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassDeclaration(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(CLASS);
			setState(339);
			identifier();
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(340);
				typeParameters();
				}
			}

			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(343);
				match(EXTENDS);
				setState(344);
				typeType();
				}
			}

			setState(349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(347);
				match(IMPLEMENTS);
				setState(348);
				typeList();
				}
			}

			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PERMITS) {
				{
				setState(351);
				match(PERMITS);
				setState(352);
				typeList();
				}
			}

			setState(355);
			classBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeParametersContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(JavaParser.LT, 0); }
		public List<TypeParameterContext> typeParameter() {
			return getRuleContexts(TypeParameterContext.class);
		}
		public TypeParameterContext typeParameter(int i) {
			return getRuleContext(TypeParameterContext.class,i);
		}
		public TerminalNode GT() { return getToken(JavaParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public TypeParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeParameters(this);
		}
	}

	public final TypeParametersContext typeParameters() throws RecognitionException {
		TypeParametersContext _localctx = new TypeParametersContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_typeParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			match(LT);
			setState(358);
			typeParameter();
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(359);
				match(COMMA);
				setState(360);
				typeParameter();
				}
				}
				setState(365);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(366);
			match(GT);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeParameterContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(JavaParser.EXTENDS, 0); }
		public TypeBoundContext typeBound() {
			return getRuleContext(TypeBoundContext.class,0);
		}
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeParameter(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_typeParameter);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(368);
					annotation();
					}
					} 
				}
				setState(373);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			setState(374);
			identifier();
			setState(383);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(375);
				match(EXTENDS);
				setState(379);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(376);
						annotation();
						}
						} 
					}
					setState(381);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				setState(382);
				typeBound();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeBoundContext extends ParserRuleContext {
		public List<TypeTypeContext> typeType() {
			return getRuleContexts(TypeTypeContext.class);
		}
		public TypeTypeContext typeType(int i) {
			return getRuleContext(TypeTypeContext.class,i);
		}
		public List<TerminalNode> BITAND() { return getTokens(JavaParser.BITAND); }
		public TerminalNode BITAND(int i) {
			return getToken(JavaParser.BITAND, i);
		}
		public TypeBoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeBound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeBound(this);
		}
	}

	public final TypeBoundContext typeBound() throws RecognitionException {
		TypeBoundContext _localctx = new TypeBoundContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_typeBound);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			typeType();
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITAND) {
				{
				{
				setState(386);
				match(BITAND);
				setState(387);
				typeType();
				}
				}
				setState(392);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(JavaParser.ENUM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(JavaParser.IMPLEMENTS, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public EnumConstantsContext enumConstants() {
			return getRuleContext(EnumConstantsContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(JavaParser.COMMA, 0); }
		public EnumBodyDeclarationsContext enumBodyDeclarations() {
			return getRuleContext(EnumBodyDeclarationsContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitEnumDeclaration(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(ENUM);
			setState(394);
			identifier();
			setState(397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(395);
				match(IMPLEMENTS);
				setState(396);
				typeList();
				}
			}

			setState(399);
			match(LBRACE);
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(400);
				enumConstants();
				}
			}

			setState(404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(403);
				match(COMMA);
				}
			}

			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(406);
				enumBodyDeclarations();
				}
			}

			setState(409);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnumConstantsContext extends ParserRuleContext {
		public List<EnumConstantContext> enumConstant() {
			return getRuleContexts(EnumConstantContext.class);
		}
		public EnumConstantContext enumConstant(int i) {
			return getRuleContext(EnumConstantContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public EnumConstantsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstants; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterEnumConstants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitEnumConstants(this);
		}
	}

	public final EnumConstantsContext enumConstants() throws RecognitionException {
		EnumConstantsContext _localctx = new EnumConstantsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_enumConstants);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			enumConstant();
			setState(416);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(412);
					match(COMMA);
					setState(413);
					enumConstant();
					}
					} 
				}
				setState(418);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnumConstantContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public EnumConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterEnumConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitEnumConstant(this);
		}
	}

	public final EnumConstantContext enumConstant() throws RecognitionException {
		EnumConstantContext _localctx = new EnumConstantContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumConstant);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(422);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(419);
					annotation();
					}
					} 
				}
				setState(424);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(425);
			identifier();
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(426);
				arguments();
				}
			}

			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(429);
				classBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnumBodyDeclarationsContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public EnumBodyDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumBodyDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterEnumBodyDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitEnumBodyDeclarations(this);
		}
	}

	public final EnumBodyDeclarationsContext enumBodyDeclarations() throws RecognitionException {
		EnumBodyDeclarationsContext _localctx = new EnumBodyDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_enumBodyDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			match(SEMI);
			setState(436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1331583875988694L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752338092039L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(433);
				classBodyDeclaration();
				}
				}
				setState(438);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceDeclarationContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(JavaParser.INTERFACE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public InterfaceBodyContext interfaceBody() {
			return getRuleContext(InterfaceBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(JavaParser.EXTENDS, 0); }
		public List<TypeListContext> typeList() {
			return getRuleContexts(TypeListContext.class);
		}
		public TypeListContext typeList(int i) {
			return getRuleContext(TypeListContext.class,i);
		}
		public TerminalNode PERMITS() { return getToken(JavaParser.PERMITS, 0); }
		public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceDeclaration(this);
		}
	}

	public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
		InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_interfaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(INTERFACE);
			setState(440);
			identifier();
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(441);
				typeParameters();
				}
			}

			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(444);
				match(EXTENDS);
				setState(445);
				typeList();
				}
			}

			setState(450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PERMITS) {
				{
				setState(448);
				match(PERMITS);
				setState(449);
				typeList();
				}
			}

			setState(452);
			interfaceBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassBody(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(LBRACE);
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1331583875988694L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752338092039L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(455);
				classBodyDeclaration();
				}
				}
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(461);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<InterfaceBodyDeclarationContext> interfaceBodyDeclaration() {
			return getRuleContexts(InterfaceBodyDeclarationContext.class);
		}
		public InterfaceBodyDeclarationContext interfaceBodyDeclaration(int i) {
			return getRuleContext(InterfaceBodyDeclarationContext.class,i);
		}
		public InterfaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceBody(this);
		}
	}

	public final InterfaceBodyContext interfaceBody() throws RecognitionException {
		InterfaceBodyContext _localctx = new InterfaceBodyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_interfaceBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(LBRACE);
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1331583875984598L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752338026503L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(464);
				interfaceBodyDeclaration();
				}
				}
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(470);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassBodyDeclarationContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public MemberDeclarationContext memberDeclaration() {
			return getRuleContext(MemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public ClassBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassBodyDeclaration(this);
		}
	}

	public final ClassBodyDeclarationContext classBodyDeclaration() throws RecognitionException {
		ClassBodyDeclarationContext _localctx = new ClassBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_classBodyDeclaration);
		int _la;
		try {
			int _alt;
			setState(484);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(472);
				match(SEMI);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(474);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STATIC) {
					{
					setState(473);
					match(STATIC);
					}
				}

				setState(476);
				block();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(480);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(477);
						modifier();
						}
						} 
					}
					setState(482);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				}
				setState(483);
				memberDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class MemberDeclarationContext extends ParserRuleContext {
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext genericMethodDeclaration() {
			return getRuleContext(GenericMethodDeclarationContext.class,0);
		}
		public FieldDeclarationContext fieldDeclaration() {
			return getRuleContext(FieldDeclarationContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext genericConstructorDeclaration() {
			return getRuleContext(GenericConstructorDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public MemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitMemberDeclaration(this);
		}
	}

	public final MemberDeclarationContext memberDeclaration() throws RecognitionException {
		MemberDeclarationContext _localctx = new MemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_memberDeclaration);
		try {
			setState(496);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(486);
				recordDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(487);
				methodDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(488);
				genericMethodDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(489);
				fieldDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(490);
				constructorDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(491);
				genericConstructorDeclaration();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(492);
				interfaceDeclaration();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(493);
				annotationTypeDeclaration();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(494);
				classDeclaration();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(495);
				enumDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class MethodDeclarationContext extends ParserRuleContext {
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
		public TerminalNode THROWS() { return getToken(JavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitMethodDeclaration(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_methodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			typeTypeOrVoid();
			setState(499);
			identifier();
			setState(500);
			formalParameters();
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(501);
				match(LBRACK);
				setState(502);
				match(RBRACK);
				}
				}
				setState(507);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(510);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(508);
				match(THROWS);
				setState(509);
				qualifiedNameList();
				}
			}

			setState(512);
			methodBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class MethodBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterMethodBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitMethodBody(this);
		}
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_methodBody);
		try {
			setState(516);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(514);
				block();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(515);
				match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeTypeOrVoidContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode VOID() { return getToken(JavaParser.VOID, 0); }
		public TypeTypeOrVoidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeTypeOrVoid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeTypeOrVoid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeTypeOrVoid(this);
		}
	}

	public final TypeTypeOrVoidContext typeTypeOrVoid() throws RecognitionException {
		TypeTypeOrVoidContext _localctx = new TypeTypeOrVoidContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_typeTypeOrVoid);
		try {
			setState(520);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(518);
				typeType();
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 2);
				{
				setState(519);
				match(VOID);
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

	@SuppressWarnings("CheckReturnValue")
	public static class GenericMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterGenericMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitGenericMethodDeclaration(this);
		}
	}

	public final GenericMethodDeclarationContext genericMethodDeclaration() throws RecognitionException {
		GenericMethodDeclarationContext _localctx = new GenericMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_genericMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			typeParameters();
			setState(523);
			methodDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class GenericConstructorDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericConstructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterGenericConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitGenericConstructorDeclaration(this);
		}
	}

	public final GenericConstructorDeclarationContext genericConstructorDeclaration() throws RecognitionException {
		GenericConstructorDeclarationContext _localctx = new GenericConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_genericConstructorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			typeParameters();
			setState(526);
			constructorDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorDeclarationContext extends ParserRuleContext {
		public BlockContext constructorBody;
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode THROWS() { return getToken(JavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public ConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitConstructorDeclaration(this);
		}
	}

	public final ConstructorDeclarationContext constructorDeclaration() throws RecognitionException {
		ConstructorDeclarationContext _localctx = new ConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_constructorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			identifier();
			setState(529);
			formalParameters();
			setState(532);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(530);
				match(THROWS);
				setState(531);
				qualifiedNameList();
				}
			}

			setState(534);
			((ConstructorDeclarationContext)_localctx).constructorBody = block();
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

	@SuppressWarnings("CheckReturnValue")
	public static class CompactConstructorDeclarationContext extends ParserRuleContext {
		public BlockContext constructorBody;
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public CompactConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compactConstructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCompactConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCompactConstructorDeclaration(this);
		}
	}

	public final CompactConstructorDeclarationContext compactConstructorDeclaration() throws RecognitionException {
		CompactConstructorDeclarationContext _localctx = new CompactConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_compactConstructorDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(539);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(536);
					modifier();
					}
					} 
				}
				setState(541);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			}
			setState(542);
			identifier();
			setState(543);
			((CompactConstructorDeclarationContext)_localctx).constructorBody = block();
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

	@SuppressWarnings("CheckReturnValue")
	public static class FieldDeclarationContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFieldDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFieldDeclaration(this);
		}
	}

	public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
		FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_fieldDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			typeType();
			setState(546);
			variableDeclarators();
			setState(547);
			match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceBodyDeclarationContext extends ParserRuleContext {
		public InterfaceMemberDeclarationContext interfaceMemberDeclaration() {
			return getRuleContext(InterfaceMemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public InterfaceBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceBodyDeclaration(this);
		}
	}

	public final InterfaceBodyDeclarationContext interfaceBodyDeclaration() throws RecognitionException {
		InterfaceBodyDeclarationContext _localctx = new InterfaceBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_interfaceBodyDeclaration);
		try {
			int _alt;
			setState(557);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DEFAULT:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOID:
			case VOLATILE:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case NON_SEALED:
			case LT:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(552);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(549);
						modifier();
						}
						} 
					}
					setState(554);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				}
				setState(555);
				interfaceMemberDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(556);
				match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceMemberDeclarationContext extends ParserRuleContext {
		public ConstDeclarationContext constDeclaration() {
			return getRuleContext(ConstDeclarationContext.class,0);
		}
		public InterfaceMethodDeclarationContext interfaceMethodDeclaration() {
			return getRuleContext(InterfaceMethodDeclarationContext.class,0);
		}
		public GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() {
			return getRuleContext(GenericInterfaceMethodDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public InterfaceMemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMemberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceMemberDeclaration(this);
		}
	}

	public final InterfaceMemberDeclarationContext interfaceMemberDeclaration() throws RecognitionException {
		InterfaceMemberDeclarationContext _localctx = new InterfaceMemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_interfaceMemberDeclaration);
		try {
			setState(567);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(559);
				constDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(560);
				interfaceMethodDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(561);
				genericInterfaceMethodDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(562);
				interfaceDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(563);
				annotationTypeDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(564);
				classDeclaration();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(565);
				enumDeclaration();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(566);
				recordDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConstDeclarationContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public List<ConstantDeclaratorContext> constantDeclarator() {
			return getRuleContexts(ConstantDeclaratorContext.class);
		}
		public ConstantDeclaratorContext constantDeclarator(int i) {
			return getRuleContext(ConstantDeclaratorContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public ConstDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterConstDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitConstDeclaration(this);
		}
	}

	public final ConstDeclarationContext constDeclaration() throws RecognitionException {
		ConstDeclarationContext _localctx = new ConstDeclarationContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_constDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(569);
			typeType();
			setState(570);
			constantDeclarator();
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(571);
				match(COMMA);
				setState(572);
				constantDeclarator();
				}
				}
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(578);
			match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConstantDeclaratorContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
		public ConstantDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterConstantDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitConstantDeclarator(this);
		}
	}

	public final ConstantDeclaratorContext constantDeclarator() throws RecognitionException {
		ConstantDeclaratorContext _localctx = new ConstantDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_constantDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			identifier();
			setState(585);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(581);
				match(LBRACK);
				setState(582);
				match(RBRACK);
				}
				}
				setState(587);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(588);
			match(ASSIGN);
			setState(589);
			variableInitializer();
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceMethodDeclarationContext extends ParserRuleContext {
		public InterfaceCommonBodyDeclarationContext interfaceCommonBodyDeclaration() {
			return getRuleContext(InterfaceCommonBodyDeclarationContext.class,0);
		}
		public List<InterfaceMethodModifierContext> interfaceMethodModifier() {
			return getRuleContexts(InterfaceMethodModifierContext.class);
		}
		public InterfaceMethodModifierContext interfaceMethodModifier(int i) {
			return getRuleContext(InterfaceMethodModifierContext.class,i);
		}
		public InterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceMethodDeclaration(this);
		}
	}

	public final InterfaceMethodDeclarationContext interfaceMethodDeclaration() throws RecognitionException {
		InterfaceMethodDeclarationContext _localctx = new InterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_interfaceMethodDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(591);
					interfaceMethodModifier();
					}
					} 
				}
				setState(596);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			}
			setState(597);
			interfaceCommonBodyDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceMethodModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public TerminalNode PUBLIC() { return getToken(JavaParser.PUBLIC, 0); }
		public TerminalNode ABSTRACT() { return getToken(JavaParser.ABSTRACT, 0); }
		public TerminalNode DEFAULT() { return getToken(JavaParser.DEFAULT, 0); }
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public TerminalNode STRICTFP() { return getToken(JavaParser.STRICTFP, 0); }
		public InterfaceMethodModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMethodModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceMethodModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceMethodModifier(this);
		}
	}

	public final InterfaceMethodModifierContext interfaceMethodModifier() throws RecognitionException {
		InterfaceMethodModifierContext _localctx = new InterfaceMethodModifierContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_interfaceMethodModifier);
		try {
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(599);
				annotation();
				}
				break;
			case PUBLIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(600);
				match(PUBLIC);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 3);
				{
				setState(601);
				match(ABSTRACT);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 4);
				{
				setState(602);
				match(DEFAULT);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(603);
				match(STATIC);
				}
				break;
			case STRICTFP:
				enterOuterAlt(_localctx, 6);
				{
				setState(604);
				match(STRICTFP);
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

	@SuppressWarnings("CheckReturnValue")
	public static class GenericInterfaceMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public InterfaceCommonBodyDeclarationContext interfaceCommonBodyDeclaration() {
			return getRuleContext(InterfaceCommonBodyDeclarationContext.class,0);
		}
		public List<InterfaceMethodModifierContext> interfaceMethodModifier() {
			return getRuleContexts(InterfaceMethodModifierContext.class);
		}
		public InterfaceMethodModifierContext interfaceMethodModifier(int i) {
			return getRuleContext(InterfaceMethodModifierContext.class,i);
		}
		public GenericInterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericInterfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterGenericInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitGenericInterfaceMethodDeclaration(this);
		}
	}

	public final GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() throws RecognitionException {
		GenericInterfaceMethodDeclarationContext _localctx = new GenericInterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_genericInterfaceMethodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(610);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2250940820221950L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752303423491L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(607);
				interfaceMethodModifier();
				}
				}
				setState(612);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(613);
			typeParameters();
			setState(614);
			interfaceCommonBodyDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceCommonBodyDeclarationContext extends ParserRuleContext {
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
		public TerminalNode THROWS() { return getToken(JavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public InterfaceCommonBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceCommonBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInterfaceCommonBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInterfaceCommonBodyDeclaration(this);
		}
	}

	public final InterfaceCommonBodyDeclarationContext interfaceCommonBodyDeclaration() throws RecognitionException {
		InterfaceCommonBodyDeclarationContext _localctx = new InterfaceCommonBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_interfaceCommonBodyDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(619);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(616);
					annotation();
					}
					} 
				}
				setState(621);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			}
			setState(622);
			typeTypeOrVoid();
			setState(623);
			identifier();
			setState(624);
			formalParameters();
			setState(629);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(625);
				match(LBRACK);
				setState(626);
				match(RBRACK);
				}
				}
				setState(631);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(634);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(632);
				match(THROWS);
				setState(633);
				qualifiedNameList();
				}
			}

			setState(636);
			methodBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorsContext extends ParserRuleContext {
		public List<VariableDeclaratorContext> variableDeclarator() {
			return getRuleContexts(VariableDeclaratorContext.class);
		}
		public VariableDeclaratorContext variableDeclarator(int i) {
			return getRuleContext(VariableDeclaratorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public VariableDeclaratorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarators; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterVariableDeclarators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitVariableDeclarators(this);
		}
	}

	public final VariableDeclaratorsContext variableDeclarators() throws RecognitionException {
		VariableDeclaratorsContext _localctx = new VariableDeclaratorsContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_variableDeclarators);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(638);
			variableDeclarator();
			setState(643);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(639);
				match(COMMA);
				setState(640);
				variableDeclarator();
				}
				}
				setState(645);
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

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorContext extends ParserRuleContext {
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterVariableDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitVariableDeclarator(this);
		}
	}

	public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
		VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(646);
			variableDeclaratorId();
			setState(649);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(647);
				match(ASSIGN);
				setState(648);
				variableInitializer();
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

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorIdContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
		public VariableDeclaratorIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaratorId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterVariableDeclaratorId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitVariableDeclaratorId(this);
		}
	}

	public final VariableDeclaratorIdContext variableDeclaratorId() throws RecognitionException {
		VariableDeclaratorIdContext _localctx = new VariableDeclaratorIdContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_variableDeclaratorId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(651);
			identifier();
			setState(656);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(652);
				match(LBRACK);
				setState(653);
				match(RBRACK);
				}
				}
				setState(658);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterVariableInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitVariableInitializer(this);
		}
	}

	public final VariableInitializerContext variableInitializer() throws RecognitionException {
		VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_variableInitializer);
		try {
			setState(661);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(659);
				arrayInitializer();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case SWITCH:
			case THIS:
			case VOID:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case TEXT_BLOCK:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(660);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayInitializerContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<VariableInitializerContext> variableInitializer() {
			return getRuleContexts(VariableInitializerContext.class);
		}
		public VariableInitializerContext variableInitializer(int i) {
			return getRuleContext(VariableInitializerContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public ArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitArrayInitializer(this);
		}
	}

	public final ArrayInitializerContext arrayInitializer() throws RecognitionException {
		ArrayInitializerContext _localctx = new ArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_arrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(663);
			match(LBRACE);
			setState(675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343707135L) != 0)) {
				{
				setState(664);
				variableInitializer();
				setState(669);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(665);
						match(COMMA);
						setState(666);
						variableInitializer();
						}
						} 
					}
					setState(671);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
				}
				setState(673);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(672);
					match(COMMA);
					}
				}

				}
			}

			setState(677);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
		public TypeIdentifierContext typeIdentifier() {
			return getRuleContext(TypeIdentifierContext.class,0);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaParser.DOT, i);
		}
		public List<TypeArgumentsContext> typeArguments() {
			return getRuleContexts(TypeArgumentsContext.class);
		}
		public TypeArgumentsContext typeArguments(int i) {
			return getRuleContext(TypeArgumentsContext.class,i);
		}
		public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassOrInterfaceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassOrInterfaceType(this);
		}
	}

	public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
		ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_classOrInterfaceType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(687);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(679);
					identifier();
					setState(681);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(680);
						typeArguments();
						}
					}

					setState(683);
					match(DOT);
					}
					} 
				}
				setState(689);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			setState(690);
			typeIdentifier();
			setState(692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				{
				setState(691);
				typeArguments();
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeArgumentContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(JavaParser.QUESTION, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(JavaParser.EXTENDS, 0); }
		public TerminalNode SUPER() { return getToken(JavaParser.SUPER, 0); }
		public TypeArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeArgument(this);
		}
	}

	public final TypeArgumentContext typeArgument() throws RecognitionException {
		TypeArgumentContext _localctx = new TypeArgumentContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_typeArgument);
		int _la;
		try {
			setState(706);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(694);
				typeType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(698);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					{
					setState(695);
					annotation();
					}
					}
					setState(700);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(701);
				match(QUESTION);
				setState(704);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXTENDS || _la==SUPER) {
					{
					setState(702);
					_la = _input.LA(1);
					if ( !(_la==EXTENDS || _la==SUPER) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(703);
					typeType();
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

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedNameListContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public QualifiedNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedNameList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterQualifiedNameList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitQualifiedNameList(this);
		}
	}

	public final QualifiedNameListContext qualifiedNameList() throws RecognitionException {
		QualifiedNameListContext _localctx = new QualifiedNameListContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_qualifiedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(708);
			qualifiedName();
			setState(713);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(709);
				match(COMMA);
				setState(710);
				qualifiedName();
				}
				}
				setState(715);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FormalParametersContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public ReceiverParameterContext receiverParameter() {
			return getRuleContext(ReceiverParameterContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(JavaParser.COMMA, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public FormalParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFormalParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFormalParameters(this);
		}
	}

	public final FormalParametersContext formalParameters() throws RecognitionException {
		FormalParametersContext _localctx = new FormalParametersContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_formalParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			match(LPAREN);
			setState(728);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(718);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223090579141953573L) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(717);
					receiverParameter();
					}
				}

				}
				break;

			case 2:
				{
				setState(720);
				receiverParameter();
				setState(723);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(721);
					match(COMMA);
					setState(722);
					formalParameterList();
					}
				}

				}
				break;

			case 3:
				{
				setState(726);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223090579141986341L) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(725);
					formalParameterList();
					}
				}

				}
				break;
			}
			setState(730);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ReceiverParameterContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode THIS() { return getToken(JavaParser.THIS, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaParser.DOT, i);
		}
		public ReceiverParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_receiverParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterReceiverParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitReceiverParameter(this);
		}
	}

	public final ReceiverParameterContext receiverParameter() throws RecognitionException {
		ReceiverParameterContext _localctx = new ReceiverParameterContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_receiverParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(732);
			typeType();
			setState(738);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(733);
				identifier();
				setState(734);
				match(DOT);
				}
				}
				setState(740);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(741);
			match(THIS);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FormalParameterListContext extends ParserRuleContext {
		public List<FormalParameterContext> formalParameter() {
			return getRuleContexts(FormalParameterContext.class);
		}
		public FormalParameterContext formalParameter(int i) {
			return getRuleContext(FormalParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public LastFormalParameterContext lastFormalParameter() {
			return getRuleContext(LastFormalParameterContext.class,0);
		}
		public FormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFormalParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFormalParameterList(this);
		}
	}

	public final FormalParameterListContext formalParameterList() throws RecognitionException {
		FormalParameterListContext _localctx = new FormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_formalParameterList);
		int _la;
		try {
			int _alt;
			setState(756);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(743);
				formalParameter();
				setState(748);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(744);
						match(COMMA);
						setState(745);
						formalParameter();
						}
						} 
					}
					setState(750);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				}
				setState(753);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(751);
					match(COMMA);
					setState(752);
					lastFormalParameter();
					}
				}

				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(755);
				lastFormalParameter();
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

	@SuppressWarnings("CheckReturnValue")
	public static class FormalParameterContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public FormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFormalParameter(this);
		}
	}

	public final FormalParameterContext formalParameter() throws RecognitionException {
		FormalParameterContext _localctx = new FormalParameterContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_formalParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(761);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(758);
					variableModifier();
					}
					} 
				}
				setState(763);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			}
			setState(764);
			typeType();
			setState(765);
			variableDeclaratorId();
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

	@SuppressWarnings("CheckReturnValue")
	public static class LastFormalParameterContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(JavaParser.ELLIPSIS, 0); }
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public LastFormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lastFormalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLastFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLastFormalParameter(this);
		}
	}

	public final LastFormalParameterContext lastFormalParameter() throws RecognitionException {
		LastFormalParameterContext _localctx = new LastFormalParameterContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_lastFormalParameter);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(767);
					variableModifier();
					}
					} 
				}
				setState(772);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
			}
			setState(773);
			typeType();
			setState(777);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(774);
				annotation();
				}
				}
				setState(779);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(780);
			match(ELLIPSIS);
			setState(781);
			variableDeclaratorId();
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

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaLVTIListContext extends ParserRuleContext {
		public List<LambdaLVTIParameterContext> lambdaLVTIParameter() {
			return getRuleContexts(LambdaLVTIParameterContext.class);
		}
		public LambdaLVTIParameterContext lambdaLVTIParameter(int i) {
			return getRuleContext(LambdaLVTIParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public LambdaLVTIListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaLVTIList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLambdaLVTIList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLambdaLVTIList(this);
		}
	}

	public final LambdaLVTIListContext lambdaLVTIList() throws RecognitionException {
		LambdaLVTIListContext _localctx = new LambdaLVTIListContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_lambdaLVTIList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(783);
			lambdaLVTIParameter();
			setState(788);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(784);
				match(COMMA);
				setState(785);
				lambdaLVTIParameter();
				}
				}
				setState(790);
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

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaLVTIParameterContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(JavaParser.VAR, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LambdaLVTIParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaLVTIParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLambdaLVTIParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLambdaLVTIParameter(this);
		}
	}

	public final LambdaLVTIParameterContext lambdaLVTIParameter() throws RecognitionException {
		LambdaLVTIParameterContext _localctx = new LambdaLVTIParameterContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_lambdaLVTIParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(794);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(791);
					variableModifier();
					}
					} 
				}
				setState(796);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
			}
			setState(797);
			match(VAR);
			setState(798);
			identifier();
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

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedNameContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaParser.DOT, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			identifier();
			setState(805);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(801);
					match(DOT);
					setState(802);
					identifier();
					}
					} 
				}
				setState(807);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public IntegerLiteralContext integerLiteral() {
			return getRuleContext(IntegerLiteralContext.class,0);
		}
		public FloatLiteralContext floatLiteral() {
			return getRuleContext(FloatLiteralContext.class,0);
		}
		public TerminalNode CHAR_LITERAL() { return getToken(JavaParser.CHAR_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(JavaParser.STRING_LITERAL, 0); }
		public TerminalNode BOOL_LITERAL() { return getToken(JavaParser.BOOL_LITERAL, 0); }
		public TerminalNode NULL_LITERAL() { return getToken(JavaParser.NULL_LITERAL, 0); }
		public TerminalNode TEXT_BLOCK() { return getToken(JavaParser.TEXT_BLOCK, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_literal);
		try {
			setState(815);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(808);
				integerLiteral();
				}
				break;
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(809);
				floatLiteral();
				}
				break;
			case CHAR_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(810);
				match(CHAR_LITERAL);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 4);
				{
				setState(811);
				match(STRING_LITERAL);
				}
				break;
			case BOOL_LITERAL:
				enterOuterAlt(_localctx, 5);
				{
				setState(812);
				match(BOOL_LITERAL);
				}
				break;
			case NULL_LITERAL:
				enterOuterAlt(_localctx, 6);
				{
				setState(813);
				match(NULL_LITERAL);
				}
				break;
			case TEXT_BLOCK:
				enterOuterAlt(_localctx, 7);
				{
				setState(814);
				match(TEXT_BLOCK);
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

	@SuppressWarnings("CheckReturnValue")
	public static class IntegerLiteralContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() { return getToken(JavaParser.DECIMAL_LITERAL, 0); }
		public TerminalNode HEX_LITERAL() { return getToken(JavaParser.HEX_LITERAL, 0); }
		public TerminalNode OCT_LITERAL() { return getToken(JavaParser.OCT_LITERAL, 0); }
		public TerminalNode BINARY_LITERAL() { return getToken(JavaParser.BINARY_LITERAL, 0); }
		public IntegerLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitIntegerLiteral(this);
		}
	}

	public final IntegerLiteralContext integerLiteral() throws RecognitionException {
		IntegerLiteralContext _localctx = new IntegerLiteralContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_integerLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			_la = _input.LA(1);
			if ( !(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 15L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class FloatLiteralContext extends ParserRuleContext {
		public TerminalNode FLOAT_LITERAL() { return getToken(JavaParser.FLOAT_LITERAL, 0); }
		public TerminalNode HEX_FLOAT_LITERAL() { return getToken(JavaParser.HEX_FLOAT_LITERAL, 0); }
		public FloatLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFloatLiteral(this);
		}
	}

	public final FloatLiteralContext floatLiteral() throws RecognitionException {
		FloatLiteralContext _localctx = new FloatLiteralContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_floatLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			_la = _input.LA(1);
			if ( !(_la==FLOAT_LITERAL || _la==HEX_FLOAT_LITERAL) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class AltAnnotationQualifiedNameContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(JavaParser.AT, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaParser.DOT, i);
		}
		public AltAnnotationQualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_altAnnotationQualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAltAnnotationQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAltAnnotationQualifiedName(this);
		}
	}

	public final AltAnnotationQualifiedNameContext altAnnotationQualifiedName() throws RecognitionException {
		AltAnnotationQualifiedNameContext _localctx = new AltAnnotationQualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_altAnnotationQualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(826);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(821);
				identifier();
				setState(822);
				match(DOT);
				}
				}
				setState(828);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(829);
			match(AT);
			setState(830);
			identifier();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(JavaParser.AT, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public AltAnnotationQualifiedNameContext altAnnotationQualifiedName() {
			return getRuleContext(AltAnnotationQualifiedNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public ElementValuePairsContext elementValuePairs() {
			return getRuleContext(ElementValuePairsContext.class,0);
		}
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotation(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(835);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(832);
				match(AT);
				setState(833);
				qualifiedName();
				}
				break;

			case 2:
				{
				setState(834);
				altAnnotationQualifiedName();
				}
				break;
			}
			setState(843);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(837);
				match(LPAREN);
				setState(840);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(838);
					elementValuePairs();
					}
					break;

				case 2:
					{
					setState(839);
					elementValue();
					}
					break;
				}
				setState(842);
				match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ElementValuePairsContext extends ParserRuleContext {
		public List<ElementValuePairContext> elementValuePair() {
			return getRuleContexts(ElementValuePairContext.class);
		}
		public ElementValuePairContext elementValuePair(int i) {
			return getRuleContext(ElementValuePairContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePairs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterElementValuePairs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitElementValuePairs(this);
		}
	}

	public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
		ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
			elementValuePair();
			setState(850);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(846);
				match(COMMA);
				setState(847);
				elementValuePair();
				}
				}
				setState(852);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ElementValuePairContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterElementValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitElementValuePair(this);
		}
	}

	public final ElementValuePairContext elementValuePair() throws RecognitionException {
		ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			identifier();
			setState(854);
			match(ASSIGN);
			setState(855);
			elementValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ElementValueContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ElementValueArrayInitializerContext elementValueArrayInitializer() {
			return getRuleContext(ElementValueArrayInitializerContext.class,0);
		}
		public ElementValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterElementValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitElementValue(this);
		}
	}

	public final ElementValueContext elementValue() throws RecognitionException {
		ElementValueContext _localctx = new ElementValueContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_elementValue);
		try {
			setState(860);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(857);
				expression(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(858);
				annotation();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(859);
				elementValueArrayInitializer();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ElementValueArrayInitializerContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<ElementValueContext> elementValue() {
			return getRuleContexts(ElementValueContext.class);
		}
		public ElementValueContext elementValue(int i) {
			return getRuleContext(ElementValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public ElementValueArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValueArrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterElementValueArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitElementValueArrayInitializer(this);
		}
	}

	public final ElementValueArrayInitializerContext elementValueArrayInitializer() throws RecognitionException {
		ElementValueArrayInitializerContext _localctx = new ElementValueArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(862);
			match(LBRACE);
			setState(871);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343707135L) != 0)) {
				{
				setState(863);
				elementValue();
				setState(868);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(864);
						match(COMMA);
						setState(865);
						elementValue();
						}
						} 
					}
					setState(870);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
				}
				}
			}

			setState(874);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(873);
				match(COMMA);
				}
			}

			setState(876);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationTypeDeclarationContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(JavaParser.AT, 0); }
		public TerminalNode INTERFACE() { return getToken(JavaParser.INTERFACE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AnnotationTypeBodyContext annotationTypeBody() {
			return getRuleContext(AnnotationTypeBodyContext.class,0);
		}
		public AnnotationTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationTypeDeclaration(this);
		}
	}

	public final AnnotationTypeDeclarationContext annotationTypeDeclaration() throws RecognitionException {
		AnnotationTypeDeclarationContext _localctx = new AnnotationTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_annotationTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(878);
			match(AT);
			setState(879);
			match(INTERFACE);
			setState(880);
			identifier();
			setState(881);
			annotationTypeBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationTypeBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<AnnotationTypeElementDeclarationContext> annotationTypeElementDeclaration() {
			return getRuleContexts(AnnotationTypeElementDeclarationContext.class);
		}
		public AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration(int i) {
			return getRuleContext(AnnotationTypeElementDeclarationContext.class,i);
		}
		public AnnotationTypeBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationTypeBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationTypeBody(this);
		}
	}

	public final AnnotationTypeBodyContext annotationTypeBody() throws RecognitionException {
		AnnotationTypeBodyContext _localctx = new AnnotationTypeBodyContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_annotationTypeBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(883);
			match(LBRACE);
			setState(887);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1613058852699350L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752304472071L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(884);
				annotationTypeElementDeclaration();
				}
				}
				setState(889);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(890);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationTypeElementDeclarationContext extends ParserRuleContext {
		public AnnotationTypeElementRestContext annotationTypeElementRest() {
			return getRuleContext(AnnotationTypeElementRestContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public AnnotationTypeElementDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationTypeElementDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationTypeElementDeclaration(this);
		}
	}

	public final AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration() throws RecognitionException {
		AnnotationTypeElementDeclarationContext _localctx = new AnnotationTypeElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_annotationTypeElementDeclaration);
		try {
			int _alt;
			setState(900);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOLATILE:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case NON_SEALED:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(895);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(892);
						modifier();
						}
						} 
					}
					setState(897);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
				}
				setState(898);
				annotationTypeElementRest();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(899);
				match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationTypeElementRestContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() {
			return getRuleContext(AnnotationMethodOrConstantRestContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public AnnotationTypeElementRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationTypeElementRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationTypeElementRest(this);
		}
	}

	public final AnnotationTypeElementRestContext annotationTypeElementRest() throws RecognitionException {
		AnnotationTypeElementRestContext _localctx = new AnnotationTypeElementRestContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_annotationTypeElementRest);
		try {
			setState(926);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(902);
				typeType();
				setState(903);
				annotationMethodOrConstantRest();
				setState(904);
				match(SEMI);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(906);
				classDeclaration();
				setState(908);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
				case 1:
					{
					setState(907);
					match(SEMI);
					}
					break;
				}
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(910);
				interfaceDeclaration();
				setState(912);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
				case 1:
					{
					setState(911);
					match(SEMI);
					}
					break;
				}
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(914);
				enumDeclaration();
				setState(916);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
				case 1:
					{
					setState(915);
					match(SEMI);
					}
					break;
				}
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(918);
				annotationTypeDeclaration();
				setState(920);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
				case 1:
					{
					setState(919);
					match(SEMI);
					}
					break;
				}
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(922);
				recordDeclaration();
				setState(924);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
				case 1:
					{
					setState(923);
					match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationMethodOrConstantRestContext extends ParserRuleContext {
		public AnnotationMethodRestContext annotationMethodRest() {
			return getRuleContext(AnnotationMethodRestContext.class,0);
		}
		public AnnotationConstantRestContext annotationConstantRest() {
			return getRuleContext(AnnotationConstantRestContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodOrConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationMethodOrConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationMethodOrConstantRest(this);
		}
	}

	public final AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() throws RecognitionException {
		AnnotationMethodOrConstantRestContext _localctx = new AnnotationMethodOrConstantRestContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_annotationMethodOrConstantRest);
		try {
			setState(930);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(928);
				annotationMethodRest();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(929);
				annotationConstantRest();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationMethodRestContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public DefaultValueContext defaultValue() {
			return getRuleContext(DefaultValueContext.class,0);
		}
		public AnnotationMethodRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationMethodRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationMethodRest(this);
		}
	}

	public final AnnotationMethodRestContext annotationMethodRest() throws RecognitionException {
		AnnotationMethodRestContext _localctx = new AnnotationMethodRestContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_annotationMethodRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(932);
			identifier();
			setState(933);
			match(LPAREN);
			setState(934);
			match(RPAREN);
			setState(936);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(935);
				defaultValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationConstantRestContext extends ParserRuleContext {
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public AnnotationConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnnotationConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnnotationConstantRest(this);
		}
	}

	public final AnnotationConstantRestContext annotationConstantRest() throws RecognitionException {
		AnnotationConstantRestContext _localctx = new AnnotationConstantRestContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_annotationConstantRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
			variableDeclarators();
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

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultValueContext extends ParserRuleContext {
		public TerminalNode DEFAULT() { return getToken(JavaParser.DEFAULT, 0); }
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public DefaultValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitDefaultValue(this);
		}
	}

	public final DefaultValueContext defaultValue() throws RecognitionException {
		DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			match(DEFAULT);
			setState(941);
			elementValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleDeclarationContext extends ParserRuleContext {
		public TerminalNode MODULE() { return getToken(JavaParser.MODULE, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ModuleBodyContext moduleBody() {
			return getRuleContext(ModuleBodyContext.class,0);
		}
		public TerminalNode OPEN() { return getToken(JavaParser.OPEN, 0); }
		public ModuleDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterModuleDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitModuleDeclaration(this);
		}
	}

	public final ModuleDeclarationContext moduleDeclaration() throws RecognitionException {
		ModuleDeclarationContext _localctx = new ModuleDeclarationContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_moduleDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(944);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN) {
				{
				setState(943);
				match(OPEN);
				}
			}

			setState(946);
			match(MODULE);
			setState(947);
			qualifiedName();
			setState(948);
			moduleBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<ModuleDirectiveContext> moduleDirective() {
			return getRuleContexts(ModuleDirectiveContext.class);
		}
		public ModuleDirectiveContext moduleDirective(int i) {
			return getRuleContext(ModuleDirectiveContext.class,i);
		}
		public ModuleBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterModuleBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitModuleBody(this);
		}
	}

	public final ModuleBodyContext moduleBody() throws RecognitionException {
		ModuleBodyContext _localctx = new ModuleBodyContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_moduleBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(950);
			match(LBRACE);
			setState(954);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 495395959010754560L) != 0)) {
				{
				{
				setState(951);
				moduleDirective();
				}
				}
				setState(956);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(957);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleDirectiveContext extends ParserRuleContext {
		public TerminalNode REQUIRES() { return getToken(JavaParser.REQUIRES, 0); }
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public List<RequiresModifierContext> requiresModifier() {
			return getRuleContexts(RequiresModifierContext.class);
		}
		public RequiresModifierContext requiresModifier(int i) {
			return getRuleContext(RequiresModifierContext.class,i);
		}
		public TerminalNode EXPORTS() { return getToken(JavaParser.EXPORTS, 0); }
		public TerminalNode TO() { return getToken(JavaParser.TO, 0); }
		public TerminalNode OPENS() { return getToken(JavaParser.OPENS, 0); }
		public TerminalNode USES() { return getToken(JavaParser.USES, 0); }
		public TerminalNode PROVIDES() { return getToken(JavaParser.PROVIDES, 0); }
		public TerminalNode WITH() { return getToken(JavaParser.WITH, 0); }
		public ModuleDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterModuleDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitModuleDirective(this);
		}
	}

	public final ModuleDirectiveContext moduleDirective() throws RecognitionException {
		ModuleDirectiveContext _localctx = new ModuleDirectiveContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_moduleDirective);
		int _la;
		try {
			int _alt;
			setState(995);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case REQUIRES:
				enterOuterAlt(_localctx, 1);
				{
				setState(959);
				match(REQUIRES);
				setState(963);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(960);
						requiresModifier();
						}
						} 
					}
					setState(965);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
				}
				setState(966);
				qualifiedName();
				setState(967);
				match(SEMI);
				}
				break;
			case EXPORTS:
				enterOuterAlt(_localctx, 2);
				{
				setState(969);
				match(EXPORTS);
				setState(970);
				qualifiedName();
				setState(973);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TO) {
					{
					setState(971);
					match(TO);
					setState(972);
					qualifiedName();
					}
				}

				setState(975);
				match(SEMI);
				}
				break;
			case OPENS:
				enterOuterAlt(_localctx, 3);
				{
				setState(977);
				match(OPENS);
				setState(978);
				qualifiedName();
				setState(981);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TO) {
					{
					setState(979);
					match(TO);
					setState(980);
					qualifiedName();
					}
				}

				setState(983);
				match(SEMI);
				}
				break;
			case USES:
				enterOuterAlt(_localctx, 4);
				{
				setState(985);
				match(USES);
				setState(986);
				qualifiedName();
				setState(987);
				match(SEMI);
				}
				break;
			case PROVIDES:
				enterOuterAlt(_localctx, 5);
				{
				setState(989);
				match(PROVIDES);
				setState(990);
				qualifiedName();
				setState(991);
				match(WITH);
				setState(992);
				qualifiedName();
				setState(993);
				match(SEMI);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RequiresModifierContext extends ParserRuleContext {
		public TerminalNode TRANSITIVE() { return getToken(JavaParser.TRANSITIVE, 0); }
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public RequiresModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requiresModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRequiresModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRequiresModifier(this);
		}
	}

	public final RequiresModifierContext requiresModifier() throws RecognitionException {
		RequiresModifierContext _localctx = new RequiresModifierContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_requiresModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(997);
			_la = _input.LA(1);
			if ( !(_la==STATIC || _la==TRANSITIVE) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class RecordDeclarationContext extends ParserRuleContext {
		public TerminalNode RECORD() { return getToken(JavaParser.RECORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RecordHeaderContext recordHeader() {
			return getRuleContext(RecordHeaderContext.class,0);
		}
		public RecordBodyContext recordBody() {
			return getRuleContext(RecordBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TerminalNode IMPLEMENTS() { return getToken(JavaParser.IMPLEMENTS, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public RecordDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRecordDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRecordDeclaration(this);
		}
	}

	public final RecordDeclarationContext recordDeclaration() throws RecognitionException {
		RecordDeclarationContext _localctx = new RecordDeclarationContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_recordDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(999);
			match(RECORD);
			setState(1000);
			identifier();
			setState(1002);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1001);
				typeParameters();
				}
			}

			setState(1004);
			recordHeader();
			setState(1007);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(1005);
				match(IMPLEMENTS);
				setState(1006);
				typeList();
				}
			}

			setState(1009);
			recordBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class RecordHeaderContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public RecordComponentListContext recordComponentList() {
			return getRuleContext(RecordComponentListContext.class,0);
		}
		public RecordHeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordHeader; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRecordHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRecordHeader(this);
		}
	}

	public final RecordHeaderContext recordHeader() throws RecognitionException {
		RecordHeaderContext _localctx = new RecordHeaderContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_recordHeader);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1011);
			match(LPAREN);
			setState(1013);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223090579141953573L) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(1012);
				recordComponentList();
				}
			}

			setState(1015);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RecordComponentListContext extends ParserRuleContext {
		public List<RecordComponentContext> recordComponent() {
			return getRuleContexts(RecordComponentContext.class);
		}
		public RecordComponentContext recordComponent(int i) {
			return getRuleContext(RecordComponentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public RecordComponentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordComponentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRecordComponentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRecordComponentList(this);
		}
	}

	public final RecordComponentListContext recordComponentList() throws RecognitionException {
		RecordComponentListContext _localctx = new RecordComponentListContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_recordComponentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1017);
			recordComponent();
			setState(1022);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1018);
				match(COMMA);
				setState(1019);
				recordComponent();
				}
				}
				setState(1024);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RecordComponentContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RecordComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordComponent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRecordComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRecordComponent(this);
		}
	}

	public final RecordComponentContext recordComponent() throws RecognitionException {
		RecordComponentContext _localctx = new RecordComponentContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_recordComponent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1025);
			typeType();
			setState(1026);
			identifier();
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

	@SuppressWarnings("CheckReturnValue")
	public static class RecordBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public List<CompactConstructorDeclarationContext> compactConstructorDeclaration() {
			return getRuleContexts(CompactConstructorDeclarationContext.class);
		}
		public CompactConstructorDeclarationContext compactConstructorDeclaration(int i) {
			return getRuleContext(CompactConstructorDeclarationContext.class,i);
		}
		public RecordBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterRecordBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitRecordBody(this);
		}
	}

	public final RecordBodyContext recordBody() throws RecognitionException {
		RecordBodyContext _localctx = new RecordBodyContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_recordBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1028);
			match(LBRACE);
			setState(1033);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1331583875988694L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576460752338092039L) != 0) || _la==IDENTIFIER) {
				{
				setState(1031);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
				case 1:
					{
					setState(1029);
					classBodyDeclaration();
					}
					break;

				case 2:
					{
					setState(1030);
					compactConstructorDeclaration();
					}
					break;
				}
				}
				setState(1035);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1036);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1038);
			match(LBRACE);
			setState(1042);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -668508564985026L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576461783331602431L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(1039);
				blockStatement();
				}
				}
				setState(1044);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1045);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public LocalTypeDeclarationContext localTypeDeclaration() {
			return getRuleContext(LocalTypeDeclarationContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitBlockStatement(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_blockStatement);
		try {
			setState(1052);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1047);
				localVariableDeclaration();
				setState(1048);
				match(SEMI);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1050);
				localTypeDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1051);
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

	@SuppressWarnings("CheckReturnValue")
	public static class LocalVariableDeclarationContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(JavaParser.VAR, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLocalVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLocalVariableDeclaration(this);
		}
	}

	public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
		LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_localVariableDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,120,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1054);
					variableModifier();
					}
					} 
				}
				setState(1059);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,120,_ctx);
			}
			setState(1068);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1060);
				match(VAR);
				setState(1061);
				identifier();
				setState(1062);
				match(ASSIGN);
				setState(1063);
				expression(0);
				}
				break;

			case 2:
				{
				setState(1065);
				typeType();
				setState(1066);
				variableDeclarators();
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public TerminalNode MODULE() { return getToken(JavaParser.MODULE, 0); }
		public TerminalNode OPEN() { return getToken(JavaParser.OPEN, 0); }
		public TerminalNode REQUIRES() { return getToken(JavaParser.REQUIRES, 0); }
		public TerminalNode EXPORTS() { return getToken(JavaParser.EXPORTS, 0); }
		public TerminalNode OPENS() { return getToken(JavaParser.OPENS, 0); }
		public TerminalNode TO() { return getToken(JavaParser.TO, 0); }
		public TerminalNode USES() { return getToken(JavaParser.USES, 0); }
		public TerminalNode PROVIDES() { return getToken(JavaParser.PROVIDES, 0); }
		public TerminalNode WITH() { return getToken(JavaParser.WITH, 0); }
		public TerminalNode TRANSITIVE() { return getToken(JavaParser.TRANSITIVE, 0); }
		public TerminalNode YIELD() { return getToken(JavaParser.YIELD, 0); }
		public TerminalNode SEALED() { return getToken(JavaParser.SEALED, 0); }
		public TerminalNode PERMITS() { return getToken(JavaParser.PERMITS, 0); }
		public TerminalNode RECORD() { return getToken(JavaParser.RECORD, 0); }
		public TerminalNode VAR() { return getToken(JavaParser.VAR, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitIdentifier(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1070);
			_la = _input.LA(1);
			if ( !(((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==IDENTIFIER) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public TerminalNode MODULE() { return getToken(JavaParser.MODULE, 0); }
		public TerminalNode OPEN() { return getToken(JavaParser.OPEN, 0); }
		public TerminalNode REQUIRES() { return getToken(JavaParser.REQUIRES, 0); }
		public TerminalNode EXPORTS() { return getToken(JavaParser.EXPORTS, 0); }
		public TerminalNode OPENS() { return getToken(JavaParser.OPENS, 0); }
		public TerminalNode TO() { return getToken(JavaParser.TO, 0); }
		public TerminalNode USES() { return getToken(JavaParser.USES, 0); }
		public TerminalNode PROVIDES() { return getToken(JavaParser.PROVIDES, 0); }
		public TerminalNode WITH() { return getToken(JavaParser.WITH, 0); }
		public TerminalNode TRANSITIVE() { return getToken(JavaParser.TRANSITIVE, 0); }
		public TerminalNode SEALED() { return getToken(JavaParser.SEALED, 0); }
		public TerminalNode PERMITS() { return getToken(JavaParser.PERMITS, 0); }
		public TerminalNode RECORD() { return getToken(JavaParser.RECORD, 0); }
		public TypeIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeIdentifier(this);
		}
	}

	public final TypeIdentifierContext typeIdentifier() throws RecognitionException {
		TypeIdentifierContext _localctx = new TypeIdentifierContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_typeIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1072);
			_la = _input.LA(1);
			if ( !(((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 29695L) != 0) || _la==IDENTIFIER) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class LocalTypeDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public LocalTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLocalTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLocalTypeDeclaration(this);
		}
	}

	public final LocalTypeDeclarationContext localTypeDeclaration() throws RecognitionException {
		LocalTypeDeclarationContext _localctx = new LocalTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_localTypeDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1077);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1074);
					classOrInterfaceModifier();
					}
					} 
				}
				setState(1079);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
			}
			setState(1083);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				{
				setState(1080);
				classDeclaration();
				}
				break;
			case INTERFACE:
				{
				setState(1081);
				interfaceDeclaration();
				}
				break;
			case RECORD:
				{
				setState(1082);
				recordDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public BlockContext blockLabel;
		public ExpressionContext statementExpression;
		public IdentifierContext identifierLabel;
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode ASSERT() { return getToken(JavaParser.ASSERT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public TerminalNode COLON() { return getToken(JavaParser.COLON, 0); }
		public TerminalNode IF() { return getToken(JavaParser.IF, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(JavaParser.ELSE, 0); }
		public TerminalNode FOR() { return getToken(JavaParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public TerminalNode WHILE() { return getToken(JavaParser.WHILE, 0); }
		public TerminalNode DO() { return getToken(JavaParser.DO, 0); }
		public TerminalNode TRY() { return getToken(JavaParser.TRY, 0); }
		public FinallyBlockContext finallyBlock() {
			return getRuleContext(FinallyBlockContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public ResourceSpecificationContext resourceSpecification() {
			return getRuleContext(ResourceSpecificationContext.class,0);
		}
		public TerminalNode SWITCH() { return getToken(JavaParser.SWITCH, 0); }
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
			return getRuleContexts(SwitchBlockStatementGroupContext.class);
		}
		public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
			return getRuleContext(SwitchBlockStatementGroupContext.class,i);
		}
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public TerminalNode SYNCHRONIZED() { return getToken(JavaParser.SYNCHRONIZED, 0); }
		public TerminalNode RETURN() { return getToken(JavaParser.RETURN, 0); }
		public TerminalNode THROW() { return getToken(JavaParser.THROW, 0); }
		public TerminalNode BREAK() { return getToken(JavaParser.BREAK, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode CONTINUE() { return getToken(JavaParser.CONTINUE, 0); }
		public TerminalNode YIELD() { return getToken(JavaParser.YIELD, 0); }
		public SwitchExpressionContext switchExpression() {
			return getRuleContext(SwitchExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(1198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1085);
				((StatementContext)_localctx).blockLabel = block();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1086);
				match(ASSERT);
				setState(1087);
				expression(0);
				setState(1090);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(1088);
					match(COLON);
					setState(1089);
					expression(0);
					}
				}

				setState(1092);
				match(SEMI);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1094);
				match(IF);
				setState(1095);
				parExpression();
				setState(1096);
				statement();
				setState(1099);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
				case 1:
					{
					setState(1097);
					match(ELSE);
					setState(1098);
					statement();
					}
					break;
				}
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1101);
				match(FOR);
				setState(1102);
				match(LPAREN);
				setState(1103);
				forControl();
				setState(1104);
				match(RPAREN);
				setState(1105);
				statement();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1107);
				match(WHILE);
				setState(1108);
				parExpression();
				setState(1109);
				statement();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1111);
				match(DO);
				setState(1112);
				statement();
				setState(1113);
				match(WHILE);
				setState(1114);
				parExpression();
				setState(1115);
				match(SEMI);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1117);
				match(TRY);
				setState(1118);
				block();
				setState(1128);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CATCH:
					{
					setState(1120); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(1119);
						catchClause();
						}
						}
						setState(1122); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==CATCH );
					setState(1125);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==FINALLY) {
						{
						setState(1124);
						finallyBlock();
						}
					}

					}
					break;
				case FINALLY:
					{
					setState(1127);
					finallyBlock();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1130);
				match(TRY);
				setState(1131);
				resourceSpecification();
				setState(1132);
				block();
				setState(1136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CATCH) {
					{
					{
					setState(1133);
					catchClause();
					}
					}
					setState(1138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FINALLY) {
					{
					setState(1139);
					finallyBlock();
					}
				}

				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1142);
				match(SWITCH);
				setState(1143);
				parExpression();
				setState(1144);
				match(LBRACE);
				setState(1148);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1145);
						switchBlockStatementGroup();
						}
						} 
					}
					setState(1150);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
				}
				setState(1154);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CASE || _la==DEFAULT) {
					{
					{
					setState(1151);
					switchLabel();
					}
					}
					setState(1156);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1157);
				match(RBRACE);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1159);
				match(SYNCHRONIZED);
				setState(1160);
				parExpression();
				setState(1161);
				block();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1163);
				match(RETURN);
				setState(1165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1164);
					expression(0);
					}
				}

				setState(1167);
				match(SEMI);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1168);
				match(THROW);
				setState(1169);
				expression(0);
				setState(1170);
				match(SEMI);
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1172);
				match(BREAK);
				setState(1174);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==IDENTIFIER) {
					{
					setState(1173);
					identifier();
					}
				}

				setState(1176);
				match(SEMI);
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1177);
				match(CONTINUE);
				setState(1179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==IDENTIFIER) {
					{
					setState(1178);
					identifier();
					}
				}

				setState(1181);
				match(SEMI);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1182);
				match(YIELD);
				setState(1183);
				expression(0);
				setState(1184);
				match(SEMI);
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1186);
				match(SEMI);
				}
				break;

			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1187);
				((StatementContext)_localctx).statementExpression = expression(0);
				setState(1188);
				match(SEMI);
				}
				break;

			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(1190);
				switchExpression();
				setState(1192);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
				case 1:
					{
					setState(1191);
					match(SEMI);
					}
					break;
				}
				}
				break;

			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(1194);
				((StatementContext)_localctx).identifierLabel = identifier();
				setState(1195);
				match(COLON);
				setState(1196);
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

	@SuppressWarnings("CheckReturnValue")
	public static class CatchClauseContext extends ParserRuleContext {
		public TerminalNode CATCH() { return getToken(JavaParser.CATCH, 0); }
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public CatchTypeContext catchType() {
			return getRuleContext(CatchTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCatchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCatchClause(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_catchClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1200);
			match(CATCH);
			setState(1201);
			match(LPAREN);
			setState(1205);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1202);
					variableModifier();
					}
					} 
				}
				setState(1207);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
			}
			setState(1208);
			catchType();
			setState(1209);
			identifier();
			setState(1210);
			match(RPAREN);
			setState(1211);
			block();
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

	@SuppressWarnings("CheckReturnValue")
	public static class CatchTypeContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<TerminalNode> BITOR() { return getTokens(JavaParser.BITOR); }
		public TerminalNode BITOR(int i) {
			return getToken(JavaParser.BITOR, i);
		}
		public CatchTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCatchType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCatchType(this);
		}
	}

	public final CatchTypeContext catchType() throws RecognitionException {
		CatchTypeContext _localctx = new CatchTypeContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_catchType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1213);
			qualifiedName();
			setState(1218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITOR) {
				{
				{
				setState(1214);
				match(BITOR);
				setState(1215);
				qualifiedName();
				}
				}
				setState(1220);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FinallyBlockContext extends ParserRuleContext {
		public TerminalNode FINALLY() { return getToken(JavaParser.FINALLY, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterFinallyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitFinallyBlock(this);
		}
	}

	public final FinallyBlockContext finallyBlock() throws RecognitionException {
		FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_finallyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1221);
			match(FINALLY);
			setState(1222);
			block();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ResourceSpecificationContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public ResourcesContext resources() {
			return getRuleContext(ResourcesContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public TerminalNode SEMI() { return getToken(JavaParser.SEMI, 0); }
		public ResourceSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterResourceSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitResourceSpecification(this);
		}
	}

	public final ResourceSpecificationContext resourceSpecification() throws RecognitionException {
		ResourceSpecificationContext _localctx = new ResourceSpecificationContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_resourceSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1224);
			match(LPAREN);
			setState(1225);
			resources();
			setState(1227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(1226);
				match(SEMI);
				}
			}

			setState(1229);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ResourcesContext extends ParserRuleContext {
		public List<ResourceContext> resource() {
			return getRuleContexts(ResourceContext.class);
		}
		public ResourceContext resource(int i) {
			return getRuleContext(ResourceContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(JavaParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(JavaParser.SEMI, i);
		}
		public ResourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterResources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitResources(this);
		}
	}

	public final ResourcesContext resources() throws RecognitionException {
		ResourcesContext _localctx = new ResourcesContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_resources);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			resource();
			setState(1236);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1232);
					match(SEMI);
					setState(1233);
					resource();
					}
					} 
				}
				setState(1238);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ResourceContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public TerminalNode VAR() { return getToken(JavaParser.VAR, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitResource(this);
		}
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_resource);
		try {
			int _alt;
			setState(1256);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1242);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,142,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1239);
						variableModifier();
						}
						} 
					}
					setState(1244);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,142,_ctx);
				}
				setState(1250);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
				case 1:
					{
					setState(1245);
					classOrInterfaceType();
					setState(1246);
					variableDeclaratorId();
					}
					break;

				case 2:
					{
					setState(1248);
					match(VAR);
					setState(1249);
					identifier();
					}
					break;
				}
				setState(1252);
				match(ASSIGN);
				setState(1253);
				expression(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1255);
				identifier();
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

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchBlockStatementGroupContext extends ParserRuleContext {
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchBlockStatementGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSwitchBlockStatementGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSwitchBlockStatementGroup(this);
		}
	}

	public final SwitchBlockStatementGroupContext switchBlockStatementGroup() throws RecognitionException {
		SwitchBlockStatementGroupContext _localctx = new SwitchBlockStatementGroupContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_switchBlockStatementGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1259); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1258);
				switchLabel();
				}
				}
				setState(1261); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CASE || _la==DEFAULT );
			setState(1264); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1263);
				blockStatement();
				}
				}
				setState(1266); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -668508564985026L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576461783331602431L) != 0) || _la==IDENTIFIER );
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

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchLabelContext extends ParserRuleContext {
		public ExpressionContext constantExpression;
		public Token enumConstantName;
		public IdentifierContext varName;
		public TerminalNode CASE() { return getToken(JavaParser.CASE, 0); }
		public TerminalNode COLON() { return getToken(JavaParser.COLON, 0); }
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(JavaParser.DEFAULT, 0); }
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSwitchLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSwitchLabel(this);
		}
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_switchLabel);
		try {
			setState(1279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1268);
				match(CASE);
				setState(1274);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
				case 1:
					{
					setState(1269);
					((SwitchLabelContext)_localctx).constantExpression = expression(0);
					}
					break;

				case 2:
					{
					setState(1270);
					((SwitchLabelContext)_localctx).enumConstantName = match(IDENTIFIER);
					}
					break;

				case 3:
					{
					setState(1271);
					typeType();
					setState(1272);
					((SwitchLabelContext)_localctx).varName = identifier();
					}
					break;
				}
				setState(1276);
				match(COLON);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1277);
				match(DEFAULT);
				setState(1278);
				match(COLON);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ForControlContext extends ParserRuleContext {
		public ExpressionListContext forUpdate;
		public EnhancedForControlContext enhancedForControl() {
			return getRuleContext(EnhancedForControlContext.class,0);
		}
		public List<TerminalNode> SEMI() { return getTokens(JavaParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(JavaParser.SEMI, i);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitForControl(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_forControl);
		int _la;
		try {
			setState(1293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1281);
				enhancedForControl();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1283);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610998821L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1282);
					forInit();
					}
				}

				setState(1285);
				match(SEMI);
				setState(1287);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1286);
					expression(0);
					}
				}

				setState(1289);
				match(SEMI);
				setState(1291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1290);
					((ForControlContext)_localctx).forUpdate = expressionList();
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitForInit(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_forInit);
		try {
			setState(1297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1295);
				localVariableDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1296);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnhancedForControlContext extends ParserRuleContext {
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public TerminalNode COLON() { return getToken(JavaParser.COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode VAR() { return getToken(JavaParser.VAR, 0); }
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public EnhancedForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enhancedForControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterEnhancedForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitEnhancedForControl(this);
		}
	}

	public final EnhancedForControlContext enhancedForControl() throws RecognitionException {
		EnhancedForControlContext _localctx = new EnhancedForControlContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_enhancedForControl);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1302);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,154,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1299);
					variableModifier();
					}
					} 
				}
				setState(1304);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,154,_ctx);
			}
			setState(1307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1305);
				typeType();
				}
				break;

			case 2:
				{
				setState(1306);
				match(VAR);
				}
				break;
			}
			setState(1309);
			variableDeclaratorId();
			setState(1310);
			match(COLON);
			setState(1311);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ParExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterParExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitParExpression(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			match(LPAREN);
			setState(1314);
			expression(0);
			setState(1315);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
			expression(0);
			setState(1322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1318);
				match(COMMA);
				setState(1319);
				expression(0);
				}
				}
				setState(1324);
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

	@SuppressWarnings("CheckReturnValue")
	public static class MethodCallContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode THIS() { return getToken(JavaParser.THIS, 0); }
		public TerminalNode SUPER() { return getToken(JavaParser.SUPER, 0); }
		public MethodCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitMethodCall(this);
		}
	}

	public final MethodCallContext methodCall() throws RecognitionException {
		MethodCallContext _localctx = new MethodCallContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_methodCall);
		int _la;
		try {
			setState(1344);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1325);
				identifier();
				setState(1326);
				match(LPAREN);
				setState(1328);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1327);
					expressionList();
					}
				}

				setState(1330);
				match(RPAREN);
				}
				break;
			case THIS:
				enterOuterAlt(_localctx, 2);
				{
				setState(1332);
				match(THIS);
				setState(1333);
				match(LPAREN);
				setState(1335);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1334);
					expressionList();
					}
				}

				setState(1337);
				match(RPAREN);
				}
				break;
			case SUPER:
				enterOuterAlt(_localctx, 3);
				{
				setState(1338);
				match(SUPER);
				setState(1339);
				match(LPAREN);
				setState(1341);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
					{
					setState(1340);
					expressionList();
					}
				}

				setState(1343);
				match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public Token prefix;
		public Token bop;
		public Token postfix;
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public MethodCallContext methodCall() {
			return getRuleContext(MethodCallContext.class,0);
		}
		public TerminalNode NEW() { return getToken(JavaParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public List<TypeTypeContext> typeType() {
			return getRuleContexts(TypeTypeContext.class);
		}
		public TypeTypeContext typeType(int i) {
			return getRuleContext(TypeTypeContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> BITAND() { return getTokens(JavaParser.BITAND); }
		public TerminalNode BITAND(int i) {
			return getToken(JavaParser.BITAND, i);
		}
		public TerminalNode ADD() { return getToken(JavaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(JavaParser.SUB, 0); }
		public TerminalNode INC() { return getToken(JavaParser.INC, 0); }
		public TerminalNode DEC() { return getToken(JavaParser.DEC, 0); }
		public TerminalNode TILDE() { return getToken(JavaParser.TILDE, 0); }
		public TerminalNode BANG() { return getToken(JavaParser.BANG, 0); }
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public SwitchExpressionContext switchExpression() {
			return getRuleContext(SwitchExpressionContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(JavaParser.COLONCOLON, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public TerminalNode MUL() { return getToken(JavaParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(JavaParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(JavaParser.MOD, 0); }
		public List<TerminalNode> LT() { return getTokens(JavaParser.LT); }
		public TerminalNode LT(int i) {
			return getToken(JavaParser.LT, i);
		}
		public List<TerminalNode> GT() { return getTokens(JavaParser.GT); }
		public TerminalNode GT(int i) {
			return getToken(JavaParser.GT, i);
		}
		public TerminalNode LE() { return getToken(JavaParser.LE, 0); }
		public TerminalNode GE() { return getToken(JavaParser.GE, 0); }
		public TerminalNode EQUAL() { return getToken(JavaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(JavaParser.NOTEQUAL, 0); }
		public TerminalNode CARET() { return getToken(JavaParser.CARET, 0); }
		public TerminalNode BITOR() { return getToken(JavaParser.BITOR, 0); }
		public TerminalNode AND() { return getToken(JavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(JavaParser.OR, 0); }
		public TerminalNode COLON() { return getToken(JavaParser.COLON, 0); }
		public TerminalNode QUESTION() { return getToken(JavaParser.QUESTION, 0); }
		public TerminalNode ASSIGN() { return getToken(JavaParser.ASSIGN, 0); }
		public TerminalNode ADD_ASSIGN() { return getToken(JavaParser.ADD_ASSIGN, 0); }
		public TerminalNode SUB_ASSIGN() { return getToken(JavaParser.SUB_ASSIGN, 0); }
		public TerminalNode MUL_ASSIGN() { return getToken(JavaParser.MUL_ASSIGN, 0); }
		public TerminalNode DIV_ASSIGN() { return getToken(JavaParser.DIV_ASSIGN, 0); }
		public TerminalNode AND_ASSIGN() { return getToken(JavaParser.AND_ASSIGN, 0); }
		public TerminalNode OR_ASSIGN() { return getToken(JavaParser.OR_ASSIGN, 0); }
		public TerminalNode XOR_ASSIGN() { return getToken(JavaParser.XOR_ASSIGN, 0); }
		public TerminalNode RSHIFT_ASSIGN() { return getToken(JavaParser.RSHIFT_ASSIGN, 0); }
		public TerminalNode URSHIFT_ASSIGN() { return getToken(JavaParser.URSHIFT_ASSIGN, 0); }
		public TerminalNode LSHIFT_ASSIGN() { return getToken(JavaParser.LSHIFT_ASSIGN, 0); }
		public TerminalNode MOD_ASSIGN() { return getToken(JavaParser.MOD_ASSIGN, 0); }
		public TerminalNode DOT() { return getToken(JavaParser.DOT, 0); }
		public TerminalNode THIS() { return getToken(JavaParser.THIS, 0); }
		public InnerCreatorContext innerCreator() {
			return getRuleContext(InnerCreatorContext.class,0);
		}
		public TerminalNode SUPER() { return getToken(JavaParser.SUPER, 0); }
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext explicitGenericInvocation() {
			return getRuleContext(ExplicitGenericInvocationContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public TerminalNode LBRACK() { return getToken(JavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(JavaParser.RBRACK, 0); }
		public TerminalNode INSTANCEOF() { return getToken(JavaParser.INSTANCEOF, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitExpression(this);
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
		int _startState = 198;
		enterRecursionRule(_localctx, 198, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1391);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				{
				setState(1347);
				primary();
				}
				break;

			case 2:
				{
				setState(1348);
				methodCall();
				}
				break;

			case 3:
				{
				setState(1349);
				match(NEW);
				setState(1350);
				creator();
				}
				break;

			case 4:
				{
				setState(1351);
				match(LPAREN);
				setState(1355);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1352);
						annotation();
						}
						} 
					}
					setState(1357);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
				}
				setState(1358);
				typeType();
				setState(1363);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==BITAND) {
					{
					{
					setState(1359);
					match(BITAND);
					setState(1360);
					typeType();
					}
					}
					setState(1365);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1366);
				match(RPAREN);
				setState(1367);
				expression(22);
				}
				break;

			case 5:
				{
				setState(1369);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & 15L) != 0)) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1370);
				expression(20);
				}
				break;

			case 6:
				{
				setState(1371);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==BANG || _la==TILDE) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1372);
				expression(19);
				}
				break;

			case 7:
				{
				setState(1373);
				lambdaExpression();
				}
				break;

			case 8:
				{
				setState(1374);
				switchExpression();
				}
				break;

			case 9:
				{
				setState(1375);
				typeType();
				setState(1376);
				match(COLONCOLON);
				setState(1382);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MODULE:
				case OPEN:
				case REQUIRES:
				case EXPORTS:
				case OPENS:
				case TO:
				case USES:
				case PROVIDES:
				case WITH:
				case TRANSITIVE:
				case VAR:
				case YIELD:
				case RECORD:
				case SEALED:
				case PERMITS:
				case LT:
				case IDENTIFIER:
					{
					setState(1378);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1377);
						typeArguments();
						}
					}

					setState(1380);
					identifier();
					}
					break;
				case NEW:
					{
					setState(1381);
					match(NEW);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 10:
				{
				setState(1384);
				classType();
				setState(1385);
				match(COLONCOLON);
				setState(1387);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1386);
					typeArguments();
					}
				}

				setState(1389);
				match(NEW);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1476);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1474);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,172,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1393);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1394);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 104)) & ~0x3f) == 0 && ((1L << (_la - 104)) & 35L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1395);
						expression(19);
						}
						break;

					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1396);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1397);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1398);
						expression(18);
						}
						break;

					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1399);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1407);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
						case 1:
							{
							setState(1400);
							match(LT);
							setState(1401);
							match(LT);
							}
							break;

						case 2:
							{
							setState(1402);
							match(GT);
							setState(1403);
							match(GT);
							setState(1404);
							match(GT);
							}
							break;

						case 3:
							{
							setState(1405);
							match(GT);
							setState(1406);
							match(GT);
							}
							break;
						}
						setState(1409);
						expression(17);
						}
						break;

					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1410);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(1411);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 88)) & ~0x3f) == 0 && ((1L << (_la - 88)) & 387L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1412);
						expression(16);
						}
						break;

					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1413);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1414);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1415);
						expression(14);
						}
						break;

					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1416);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1417);
						((ExpressionContext)_localctx).bop = match(BITAND);
						setState(1418);
						expression(13);
						}
						break;

					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1419);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1420);
						((ExpressionContext)_localctx).bop = match(CARET);
						setState(1421);
						expression(12);
						}
						break;

					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1422);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1423);
						((ExpressionContext)_localctx).bop = match(BITOR);
						setState(1424);
						expression(11);
						}
						break;

					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1425);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1426);
						((ExpressionContext)_localctx).bop = match(AND);
						setState(1427);
						expression(10);
						}
						break;

					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1428);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1429);
						((ExpressionContext)_localctx).bop = match(OR);
						setState(1430);
						expression(9);
						}
						break;

					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1431);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1432);
						((ExpressionContext)_localctx).bop = match(QUESTION);
						setState(1433);
						expression(0);
						setState(1434);
						match(COLON);
						setState(1435);
						expression(7);
						}
						break;

					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1437);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1438);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 87)) & ~0x3f) == 0 && ((1L << (_la - 87)) & 17171480577L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1439);
						expression(6);
						}
						break;

					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1440);
						if (!(precpred(_ctx, 26))) throw new FailedPredicateException(this, "precpred(_ctx, 26)");
						setState(1441);
						((ExpressionContext)_localctx).bop = match(DOT);
						setState(1453);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
						case 1:
							{
							setState(1442);
							identifier();
							}
							break;

						case 2:
							{
							setState(1443);
							methodCall();
							}
							break;

						case 3:
							{
							setState(1444);
							match(THIS);
							}
							break;

						case 4:
							{
							setState(1445);
							match(NEW);
							setState(1447);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==LT) {
								{
								setState(1446);
								nonWildcardTypeArguments();
								}
							}

							setState(1449);
							innerCreator();
							}
							break;

						case 5:
							{
							setState(1450);
							match(SUPER);
							setState(1451);
							superSuffix();
							}
							break;

						case 6:
							{
							setState(1452);
							explicitGenericInvocation();
							}
							break;
						}
						}
						break;

					case 14:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1455);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(1456);
						match(LBRACK);
						setState(1457);
						expression(0);
						setState(1458);
						match(RBRACK);
						}
						break;

					case 15:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1460);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(1461);
						((ExpressionContext)_localctx).postfix = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((ExpressionContext)_localctx).postfix = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;

					case 16:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1462);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(1463);
						((ExpressionContext)_localctx).bop = match(INSTANCEOF);
						setState(1466);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
						case 1:
							{
							setState(1464);
							typeType();
							}
							break;

						case 2:
							{
							setState(1465);
							pattern();
							}
							break;
						}
						}
						break;

					case 17:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1468);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1469);
						match(COLONCOLON);
						setState(1471);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(1470);
							typeArguments();
							}
						}

						setState(1473);
						identifier();
						}
						break;
					}
					} 
				}
				setState(1478);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PatternContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitPattern(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_pattern);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1482);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,174,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1479);
					variableModifier();
					}
					} 
				}
				setState(1484);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,174,_ctx);
			}
			setState(1485);
			typeType();
			setState(1489);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,175,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1486);
					annotation();
					}
					} 
				}
				setState(1491);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,175,_ctx);
			}
			setState(1492);
			identifier();
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

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaExpressionContext extends ParserRuleContext {
		public LambdaParametersContext lambdaParameters() {
			return getRuleContext(LambdaParametersContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(JavaParser.ARROW, 0); }
		public LambdaBodyContext lambdaBody() {
			return getRuleContext(LambdaBodyContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLambdaExpression(this);
		}
	}

	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1494);
			lambdaParameters();
			setState(1495);
			match(ARROW);
			setState(1496);
			lambdaBody();
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

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaParametersContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public LambdaLVTIListContext lambdaLVTIList() {
			return getRuleContext(LambdaLVTIListContext.class,0);
		}
		public LambdaParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLambdaParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLambdaParameters(this);
		}
	}

	public final LambdaParametersContext lambdaParameters() throws RecognitionException {
		LambdaParametersContext _localctx = new LambdaParametersContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_lambdaParameters);
		int _la;
		try {
			setState(1520);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1498);
				identifier();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1499);
				match(LPAREN);
				setState(1501);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223090579141986341L) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(1500);
					formalParameterList();
					}
				}

				setState(1503);
				match(RPAREN);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1504);
				match(LPAREN);
				setState(1505);
				identifier();
				setState(1510);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1506);
					match(COMMA);
					setState(1507);
					identifier();
					}
					}
					setState(1512);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1513);
				match(RPAREN);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1515);
				match(LPAREN);
				setState(1517);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 281466386776065L) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(1516);
					lambdaLVTIList();
					}
				}

				setState(1519);
				match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaBodyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public LambdaBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterLambdaBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitLambdaBody(this);
		}
	}

	public final LambdaBodyContext lambdaBody() throws RecognitionException {
		LambdaBodyContext _localctx = new LambdaBodyContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_lambdaBody);
		try {
			setState(1524);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case SWITCH:
			case THIS:
			case VOID:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case TEXT_BLOCK:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1522);
				expression(0);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1523);
				block();
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

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public TerminalNode THIS() { return getToken(JavaParser.THIS, 0); }
		public TerminalNode SUPER() { return getToken(JavaParser.SUPER, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JavaParser.DOT, 0); }
		public TerminalNode CLASS() { return getToken(JavaParser.CLASS, 0); }
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitPrimary(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_primary);
		try {
			setState(1544);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1526);
				match(LPAREN);
				setState(1527);
				expression(0);
				setState(1528);
				match(RPAREN);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1530);
				match(THIS);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1531);
				match(SUPER);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1532);
				literal();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1533);
				identifier();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1534);
				typeTypeOrVoid();
				setState(1535);
				match(DOT);
				setState(1536);
				match(CLASS);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1538);
				nonWildcardTypeArguments();
				setState(1542);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SUPER:
				case MODULE:
				case OPEN:
				case REQUIRES:
				case EXPORTS:
				case OPENS:
				case TO:
				case USES:
				case PROVIDES:
				case WITH:
				case TRANSITIVE:
				case VAR:
				case YIELD:
				case RECORD:
				case SEALED:
				case PERMITS:
				case IDENTIFIER:
					{
					setState(1539);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1540);
					match(THIS);
					setState(1541);
					arguments();
					}
					break;
				default:
					throw new NoViableAltException(this);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchExpressionContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(JavaParser.SWITCH, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(JavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaParser.RBRACE, 0); }
		public List<SwitchLabeledRuleContext> switchLabeledRule() {
			return getRuleContexts(SwitchLabeledRuleContext.class);
		}
		public SwitchLabeledRuleContext switchLabeledRule(int i) {
			return getRuleContext(SwitchLabeledRuleContext.class,i);
		}
		public SwitchExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSwitchExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSwitchExpression(this);
		}
	}

	public final SwitchExpressionContext switchExpression() throws RecognitionException {
		SwitchExpressionContext _localctx = new SwitchExpressionContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_switchExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1546);
			match(SWITCH);
			setState(1547);
			parExpression();
			setState(1548);
			match(LBRACE);
			setState(1552);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CASE || _la==DEFAULT) {
				{
				{
				setState(1549);
				switchLabeledRule();
				}
				}
				setState(1554);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1555);
			match(RBRACE);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchLabeledRuleContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(JavaParser.CASE, 0); }
		public SwitchRuleOutcomeContext switchRuleOutcome() {
			return getRuleContext(SwitchRuleOutcomeContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(JavaParser.ARROW, 0); }
		public TerminalNode COLON() { return getToken(JavaParser.COLON, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode NULL_LITERAL() { return getToken(JavaParser.NULL_LITERAL, 0); }
		public GuardedPatternContext guardedPattern() {
			return getRuleContext(GuardedPatternContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(JavaParser.DEFAULT, 0); }
		public SwitchLabeledRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabeledRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSwitchLabeledRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSwitchLabeledRule(this);
		}
	}

	public final SwitchLabeledRuleContext switchLabeledRule() throws RecognitionException {
		SwitchLabeledRuleContext _localctx = new SwitchLabeledRuleContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_switchLabeledRule);
		int _la;
		try {
			setState(1568);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1557);
				match(CASE);
				setState(1561);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,184,_ctx) ) {
				case 1:
					{
					setState(1558);
					expressionList();
					}
					break;

				case 2:
					{
					setState(1559);
					match(NULL_LITERAL);
					}
					break;

				case 3:
					{
					setState(1560);
					guardedPattern(0);
					}
					break;
				}
				setState(1563);
				_la = _input.LA(1);
				if ( !(_la==COLON || _la==ARROW) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1564);
				switchRuleOutcome();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1565);
				match(DEFAULT);
				setState(1566);
				_la = _input.LA(1);
				if ( !(_la==COLON || _la==ARROW) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1567);
				switchRuleOutcome();
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

	@SuppressWarnings("CheckReturnValue")
	public static class GuardedPatternContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public GuardedPatternContext guardedPattern() {
			return getRuleContext(GuardedPatternContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(JavaParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(JavaParser.AND, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public GuardedPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_guardedPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterGuardedPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitGuardedPattern(this);
		}
	}

	public final GuardedPatternContext guardedPattern() throws RecognitionException {
		return guardedPattern(0);
	}

	private GuardedPatternContext guardedPattern(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		GuardedPatternContext _localctx = new GuardedPatternContext(_ctx, _parentState);
		GuardedPatternContext _prevctx = _localctx;
		int _startState = 214;
		enterRecursionRule(_localctx, 214, RULE_guardedPattern, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1596);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				{
				setState(1571);
				match(LPAREN);
				setState(1572);
				guardedPattern(0);
				setState(1573);
				match(RPAREN);
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FINAL:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case AT:
			case IDENTIFIER:
				{
				setState(1578);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,186,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1575);
						variableModifier();
						}
						} 
					}
					setState(1580);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,186,_ctx);
				}
				setState(1581);
				typeType();
				setState(1585);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,187,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1582);
						annotation();
						}
						} 
					}
					setState(1587);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,187,_ctx);
				}
				setState(1588);
				identifier();
				setState(1593);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,188,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1589);
						match(AND);
						setState(1590);
						expression(0);
						}
						} 
					}
					setState(1595);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,188,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(1603);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,190,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new GuardedPatternContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_guardedPattern);
					setState(1598);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(1599);
					match(AND);
					setState(1600);
					expression(0);
					}
					} 
				}
				setState(1605);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,190,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchRuleOutcomeContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public SwitchRuleOutcomeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchRuleOutcome; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSwitchRuleOutcome(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSwitchRuleOutcome(this);
		}
	}

	public final SwitchRuleOutcomeContext switchRuleOutcome() throws RecognitionException {
		SwitchRuleOutcomeContext _localctx = new SwitchRuleOutcomeContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_switchRuleOutcome);
		int _la;
		try {
			setState(1613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1606);
				block();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1610);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -668508564985026L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 576461783331602431L) != 0) || _la==IDENTIFIER) {
					{
					{
					setState(1607);
					blockStatement();
					}
					}
					setState(1612);
					_errHandler.sync(this);
					_la = _input.LA(1);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JavaParser.DOT, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassType(this);
		}
	}

	public final ClassTypeContext classType() throws RecognitionException {
		ClassTypeContext _localctx = new ClassTypeContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_classType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1618);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,193,_ctx) ) {
			case 1:
				{
				setState(1615);
				classOrInterfaceType();
				setState(1616);
				match(DOT);
				}
				break;
			}
			setState(1623);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,194,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1620);
					annotation();
					}
					} 
				}
				setState(1625);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,194,_ctx);
			}
			setState(1626);
			identifier();
			setState(1628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1627);
				typeArguments();
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

	@SuppressWarnings("CheckReturnValue")
	public static class CreatorContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public CreatedNameContext createdName() {
			return getRuleContext(CreatedNameContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public ArrayCreatorRestContext arrayCreatorRest() {
			return getRuleContext(ArrayCreatorRestContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCreator(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_creator);
		try {
			setState(1639);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1630);
				nonWildcardTypeArguments();
				setState(1631);
				createdName();
				setState(1632);
				classCreatorRest();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1634);
				createdName();
				setState(1637);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LBRACK:
					{
					setState(1635);
					arrayCreatorRest();
					}
					break;
				case LPAREN:
					{
					setState(1636);
					classCreatorRest();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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

	@SuppressWarnings("CheckReturnValue")
	public static class CreatedNameContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TypeArgumentsOrDiamondContext> typeArgumentsOrDiamond() {
			return getRuleContexts(TypeArgumentsOrDiamondContext.class);
		}
		public TypeArgumentsOrDiamondContext typeArgumentsOrDiamond(int i) {
			return getRuleContext(TypeArgumentsOrDiamondContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaParser.DOT, i);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public CreatedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createdName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterCreatedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitCreatedName(this);
		}
	}

	public final CreatedNameContext createdName() throws RecognitionException {
		CreatedNameContext _localctx = new CreatedNameContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_createdName);
		int _la;
		try {
			setState(1656);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1641);
				identifier();
				setState(1643);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1642);
					typeArgumentsOrDiamond();
					}
				}

				setState(1652);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1645);
					match(DOT);
					setState(1646);
					identifier();
					setState(1648);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1647);
						typeArgumentsOrDiamond();
						}
					}

					}
					}
					setState(1654);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1655);
				primitiveType();
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

	@SuppressWarnings("CheckReturnValue")
	public static class InnerCreatorContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() {
			return getRuleContext(NonWildcardTypeArgumentsOrDiamondContext.class,0);
		}
		public InnerCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_innerCreator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterInnerCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitInnerCreator(this);
		}
	}

	public final InnerCreatorContext innerCreator() throws RecognitionException {
		InnerCreatorContext _localctx = new InnerCreatorContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_innerCreator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1658);
			identifier();
			setState(1660);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1659);
				nonWildcardTypeArgumentsOrDiamond();
				}
			}

			setState(1662);
			classCreatorRest();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayCreatorRestContext extends ParserRuleContext {
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterArrayCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitArrayCreatorRest(this);
		}
	}

	public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
		ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_arrayCreatorRest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1664);
			match(LBRACK);
			setState(1692);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RBRACK:
				{
				setState(1665);
				match(RBRACK);
				setState(1670);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LBRACK) {
					{
					{
					setState(1666);
					match(LBRACK);
					setState(1667);
					match(RBRACK);
					}
					}
					setState(1672);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1673);
				arrayInitializer();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case SWITCH:
			case THIS:
			case VOID:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case TEXT_BLOCK:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case AT:
			case IDENTIFIER:
				{
				setState(1674);
				expression(0);
				setState(1675);
				match(RBRACK);
				setState(1682);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,204,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1676);
						match(LBRACK);
						setState(1677);
						expression(0);
						setState(1678);
						match(RBRACK);
						}
						} 
					}
					setState(1684);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,204,_ctx);
				}
				setState(1689);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,205,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1685);
						match(LBRACK);
						setState(1686);
						match(RBRACK);
						}
						} 
					}
					setState(1691);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,205,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassCreatorRestContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public ClassCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classCreatorRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassCreatorRest(this);
		}
	}

	public final ClassCreatorRestContext classCreatorRest() throws RecognitionException {
		ClassCreatorRestContext _localctx = new ClassCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_classCreatorRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1694);
			arguments();
			setState(1696);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,207,_ctx) ) {
			case 1:
				{
				setState(1695);
				classBody();
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExplicitGenericInvocationContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterExplicitGenericInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitExplicitGenericInvocation(this);
		}
	}

	public final ExplicitGenericInvocationContext explicitGenericInvocation() throws RecognitionException {
		ExplicitGenericInvocationContext _localctx = new ExplicitGenericInvocationContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_explicitGenericInvocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1698);
			nonWildcardTypeArguments();
			setState(1699);
			explicitGenericInvocationSuffix();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeArgumentsOrDiamondContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(JavaParser.LT, 0); }
		public TerminalNode GT() { return getToken(JavaParser.GT, 0); }
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public TypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeArgumentsOrDiamond(this);
		}
	}

	public final TypeArgumentsOrDiamondContext typeArgumentsOrDiamond() throws RecognitionException {
		TypeArgumentsOrDiamondContext _localctx = new TypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_typeArgumentsOrDiamond);
		try {
			setState(1704);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,208,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1701);
				match(LT);
				setState(1702);
				match(GT);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1703);
				typeArguments();
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

	@SuppressWarnings("CheckReturnValue")
	public static class NonWildcardTypeArgumentsOrDiamondContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(JavaParser.LT, 0); }
		public TerminalNode GT() { return getToken(JavaParser.GT, 0); }
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterNonWildcardTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitNonWildcardTypeArgumentsOrDiamond(this);
		}
	}

	public final NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() throws RecognitionException {
		NonWildcardTypeArgumentsOrDiamondContext _localctx = new NonWildcardTypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_nonWildcardTypeArgumentsOrDiamond);
		try {
			setState(1709);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,209,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1706);
				match(LT);
				setState(1707);
				match(GT);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1708);
				nonWildcardTypeArguments();
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

	@SuppressWarnings("CheckReturnValue")
	public static class NonWildcardTypeArgumentsContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(JavaParser.LT, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public TerminalNode GT() { return getToken(JavaParser.GT, 0); }
		public NonWildcardTypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterNonWildcardTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitNonWildcardTypeArguments(this);
		}
	}

	public final NonWildcardTypeArgumentsContext nonWildcardTypeArguments() throws RecognitionException {
		NonWildcardTypeArgumentsContext _localctx = new NonWildcardTypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_nonWildcardTypeArguments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1711);
			match(LT);
			setState(1712);
			typeList();
			setState(1713);
			match(GT);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeListContext extends ParserRuleContext {
		public List<TypeTypeContext> typeType() {
			return getRuleContexts(TypeTypeContext.class);
		}
		public TypeTypeContext typeType(int i) {
			return getRuleContext(TypeTypeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeList(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1715);
			typeType();
			setState(1720);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1716);
				match(COMMA);
				setState(1717);
				typeType();
				}
				}
				setState(1722);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeTypeContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> LBRACK() { return getTokens(JavaParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(JavaParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(JavaParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(JavaParser.RBRACK, i);
		}
		public TypeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeType(this);
		}
	}

	public final TypeTypeContext typeType() throws RecognitionException {
		TypeTypeContext _localctx = new TypeTypeContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_typeType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1726);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,211,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1723);
					annotation();
					}
					} 
				}
				setState(1728);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,211,_ctx);
			}
			setState(1731);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				{
				setState(1729);
				classOrInterfaceType();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				{
				setState(1730);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1743);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,214,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1736);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (((((_la - 51)) & ~0x3f) == 0 && ((1L << (_la - 51)) & 32767L) != 0) || _la==AT || _la==IDENTIFIER) {
						{
						{
						setState(1733);
						annotation();
						}
						}
						setState(1738);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1739);
					match(LBRACK);
					setState(1740);
					match(RBRACK);
					}
					} 
				}
				setState(1745);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,214,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode BOOLEAN() { return getToken(JavaParser.BOOLEAN, 0); }
		public TerminalNode CHAR() { return getToken(JavaParser.CHAR, 0); }
		public TerminalNode BYTE() { return getToken(JavaParser.BYTE, 0); }
		public TerminalNode SHORT() { return getToken(JavaParser.SHORT, 0); }
		public TerminalNode INT() { return getToken(JavaParser.INT, 0); }
		public TerminalNode LONG() { return getToken(JavaParser.LONG, 0); }
		public TerminalNode FLOAT() { return getToken(JavaParser.FLOAT, 0); }
		public TerminalNode DOUBLE() { return getToken(JavaParser.DOUBLE, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitPrimitiveType(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1746);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 138111107368L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeArgumentsContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(JavaParser.LT, 0); }
		public List<TypeArgumentContext> typeArgument() {
			return getRuleContexts(TypeArgumentContext.class);
		}
		public TypeArgumentContext typeArgument(int i) {
			return getRuleContext(TypeArgumentContext.class,i);
		}
		public TerminalNode GT() { return getToken(JavaParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JavaParser.COMMA, i);
		}
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitTypeArguments(this);
		}
	}

	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1748);
			match(LT);
			setState(1749);
			typeArgument();
			setState(1754);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1750);
				match(COMMA);
				setState(1751);
				typeArgument();
				}
				}
				setState(1756);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1757);
			match(GT);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SuperSuffixContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JavaParser.DOT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public SuperSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_superSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterSuperSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitSuperSuffix(this);
		}
	}

	public final SuperSuffixContext superSuffix() throws RecognitionException {
		SuperSuffixContext _localctx = new SuperSuffixContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_superSuffix);
		int _la;
		try {
			setState(1768);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(1759);
				arguments();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1760);
				match(DOT);
				setState(1762);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1761);
					typeArguments();
					}
				}

				setState(1764);
				identifier();
				setState(1766);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,217,_ctx) ) {
				case 1:
					{
					setState(1765);
					arguments();
					}
					break;
				}
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExplicitGenericInvocationSuffixContext extends ParserRuleContext {
		public TerminalNode SUPER() { return getToken(JavaParser.SUPER, 0); }
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocationSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterExplicitGenericInvocationSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitExplicitGenericInvocationSuffix(this);
		}
	}

	public final ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws RecognitionException {
		ExplicitGenericInvocationSuffixContext _localctx = new ExplicitGenericInvocationSuffixContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_explicitGenericInvocationSuffix);
		try {
			setState(1775);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUPER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1770);
				match(SUPER);
				setState(1771);
				superSuffix();
				}
				break;
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1772);
				identifier();
				setState(1773);
				arguments();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(JavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JavaParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitArguments(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1777);
			match(LPAREN);
			setState(1779);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 9223127275610966053L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4683743741343698943L) != 0)) {
				{
				setState(1778);
				expressionList();
				}
			}

			setState(1781);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ClassOrInterfaceBodyDeclarationContext extends ParserRuleContext {
		public ClassBodyDeclarationContext classBodyDeclaration() {
			return getRuleContext(ClassBodyDeclarationContext.class,0);
		}
		public InterfaceBodyDeclarationContext interfaceBodyDeclaration() {
			return getRuleContext(InterfaceBodyDeclarationContext.class,0);
		}
		public ClassOrInterfaceBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterClassOrInterfaceBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitClassOrInterfaceBodyDeclaration(this);
		}
	}

	public final ClassOrInterfaceBodyDeclarationContext classOrInterfaceBodyDeclaration() throws RecognitionException {
		ClassOrInterfaceBodyDeclarationContext _localctx = new ClassOrInterfaceBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_classOrInterfaceBodyDeclaration);
		try {
			setState(1785);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,221,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1783);
				classBodyDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1784);
				interfaceBodyDeclaration();
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

	@SuppressWarnings("CheckReturnValue")
	public static class UnknownIntervalContext extends ParserRuleContext {
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public List<ClassOrInterfaceBodyDeclarationContext> classOrInterfaceBodyDeclaration() {
			return getRuleContexts(ClassOrInterfaceBodyDeclarationContext.class);
		}
		public ClassOrInterfaceBodyDeclarationContext classOrInterfaceBodyDeclaration(int i) {
			return getRuleContext(ClassOrInterfaceBodyDeclarationContext.class,i);
		}
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<PackageDeclarationContext> packageDeclaration() {
			return getRuleContexts(PackageDeclarationContext.class);
		}
		public PackageDeclarationContext packageDeclaration(int i) {
			return getRuleContext(PackageDeclarationContext.class,i);
		}
		public List<AnySeqContext> anySeq() {
			return getRuleContexts(AnySeqContext.class);
		}
		public AnySeqContext anySeq(int i) {
			return getRuleContext(AnySeqContext.class,i);
		}
		public TerminalNode EOF() { return getToken(JavaParser.EOF, 0); }
		public UnknownIntervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknownInterval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterUnknownInterval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitUnknownInterval(this);
		}
	}

	public final UnknownIntervalContext unknownInterval() throws RecognitionException {
		UnknownIntervalContext _localctx = new UnknownIntervalContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_unknownInterval);
		try {
			int _alt;
			setState(1797);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case ASSERT:
			case BOOLEAN:
			case BREAK:
			case BYTE:
			case CASE:
			case CATCH:
			case CHAR:
			case CLASS:
			case CONST:
			case CONTINUE:
			case DEFAULT:
			case DO:
			case DOUBLE:
			case ELSE:
			case ENUM:
			case EXTENDS:
			case FINAL:
			case FINALLY:
			case FLOAT:
			case FOR:
			case IF:
			case GOTO:
			case IMPLEMENTS:
			case IMPORT:
			case INSTANCEOF:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case NEW:
			case PACKAGE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case RETURN:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SUPER:
			case SWITCH:
			case SYNCHRONIZED:
			case THIS:
			case THROW:
			case THROWS:
			case TRANSIENT:
			case TRY:
			case VOID:
			case VOLATILE:
			case WHILE:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case NON_SEALED:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case TEXT_BLOCK:
			case NULL_LITERAL:
			case LPAREN:
			case RPAREN:
			case LBRACE:
			case RBRACE:
			case LBRACK:
			case RBRACK:
			case SEMI:
			case COMMA:
			case DOT:
			case ASSIGN:
			case GT:
			case LT:
			case BANG:
			case TILDE:
			case QUESTION:
			case COLON:
			case EQUAL:
			case LE:
			case GE:
			case NOTEQUAL:
			case AND:
			case OR:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case BITAND:
			case BITOR:
			case CARET:
			case MOD:
			case ADD_ASSIGN:
			case SUB_ASSIGN:
			case MUL_ASSIGN:
			case DIV_ASSIGN:
			case AND_ASSIGN:
			case OR_ASSIGN:
			case XOR_ASSIGN:
			case MOD_ASSIGN:
			case LSHIFT_ASSIGN:
			case RSHIFT_ASSIGN:
			case URSHIFT_ASSIGN:
			case ARROW:
			case COLONCOLON:
			case AT:
			case ELLIPSIS:
			case WS:
			case COMMENT:
			case LINE_COMMENT:
			case NEW_LINE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1792); 
				_errHandler.sync(this);
				_alt = 1+1;
				do {
					switch (_alt) {
					case 1+1:
						{
						setState(1792);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,222,_ctx) ) {
						case 1:
							{
							setState(1787);
							typeDeclaration();
							}
							break;

						case 2:
							{
							setState(1788);
							classOrInterfaceBodyDeclaration();
							}
							break;

						case 3:
							{
							setState(1789);
							importDeclaration();
							}
							break;

						case 4:
							{
							setState(1790);
							packageDeclaration();
							}
							break;

						case 5:
							{
							setState(1791);
							anySeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1794); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,223,_ctx);
				} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case EOF:
				enterOuterAlt(_localctx, 2);
				{
				setState(1796);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AnySeqContext extends ParserRuleContext {
		public AnySeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anySeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).enterAnySeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaParserListener ) ((JavaParserListener)listener).exitAnySeq(this);
		}
	}

	public final AnySeqContext anySeq() throws RecognitionException {
		AnySeqContext _localctx = new AnySeqContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_anySeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1800); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1799);
					matchWildcard();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1802); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
		case 99:
			return expression_sempred((ExpressionContext)_localctx, predIndex);

		case 107:
			return guardedPattern_sempred((GuardedPatternContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);

		case 1:
			return precpred(_ctx, 17);

		case 2:
			return precpred(_ctx, 16);

		case 3:
			return precpred(_ctx, 15);

		case 4:
			return precpred(_ctx, 13);

		case 5:
			return precpred(_ctx, 12);

		case 6:
			return precpred(_ctx, 11);

		case 7:
			return precpred(_ctx, 10);

		case 8:
			return precpred(_ctx, 9);

		case 9:
			return precpred(_ctx, 8);

		case 10:
			return precpred(_ctx, 7);

		case 11:
			return precpred(_ctx, 6);

		case 12:
			return precpred(_ctx, 26);

		case 13:
			return precpred(_ctx, 25);

		case 14:
			return precpred(_ctx, 21);

		case 15:
			return precpred(_ctx, 14);

		case 16:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean guardedPattern_sempred(GuardedPatternContext _localctx, int predIndex) {
		switch (predIndex) {
		case 17:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0081\u070d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007"+
		"T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007"+
		"Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007"+
		"^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007"+
		"c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007"+
		"h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007"+
		"m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007"+
		"r\u0002s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007"+
		"w\u0002x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007"+
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007"+
		"\u0080\u0001\u0000\u0003\u0000\u0104\b\u0000\u0001\u0000\u0005\u0000\u0107"+
		"\b\u0000\n\u0000\f\u0000\u010a\t\u0000\u0001\u0000\u0005\u0000\u010d\b"+
		"\u0000\n\u0000\f\u0000\u0110\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0003\u0000\u0115\b\u0000\u0001\u0001\u0005\u0001\u0118\b\u0001\n\u0001"+
		"\f\u0001\u011b\t\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0003\u0002\u0123\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0003\u0002\u0128\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0005\u0003\u012d\b\u0003\n\u0003\f\u0003\u0130\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u0137\b\u0003\u0001"+
		"\u0003\u0003\u0003\u013a\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004\u0141\b\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005\u014d\b\u0005\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u0151\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0156"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u015a\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0003\u0007\u015e\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"\u0162\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0005\b\u016a\b\b\n\b\f\b\u016d\t\b\u0001\b\u0001\b\u0001\t\u0005\t\u0172"+
		"\b\t\n\t\f\t\u0175\t\t\u0001\t\u0001\t\u0001\t\u0005\t\u017a\b\t\n\t\f"+
		"\t\u017d\t\t\u0001\t\u0003\t\u0180\b\t\u0001\n\u0001\n\u0001\n\u0005\n"+
		"\u0185\b\n\n\n\f\n\u0188\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u018e\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0192"+
		"\b\u000b\u0001\u000b\u0003\u000b\u0195\b\u000b\u0001\u000b\u0003\u000b"+
		"\u0198\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0005\f"+
		"\u019f\b\f\n\f\f\f\u01a2\t\f\u0001\r\u0005\r\u01a5\b\r\n\r\f\r\u01a8\t"+
		"\r\u0001\r\u0001\r\u0003\r\u01ac\b\r\u0001\r\u0003\r\u01af\b\r\u0001\u000e"+
		"\u0001\u000e\u0005\u000e\u01b3\b\u000e\n\u000e\f\u000e\u01b6\t\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u01bb\b\u000f\u0001\u000f\u0001"+
		"\u000f\u0003\u000f\u01bf\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u01c3"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0005\u0010\u01c9"+
		"\b\u0010\n\u0010\f\u0010\u01cc\t\u0010\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0005\u0011\u01d2\b\u0011\n\u0011\f\u0011\u01d5\t\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0003\u0012\u01db\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0005\u0012\u01df\b\u0012\n\u0012\f\u0012\u01e2\t\u0012"+
		"\u0001\u0012\u0003\u0012\u01e5\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u01f1\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0005\u0014\u01f8\b\u0014\n\u0014\f\u0014\u01fb"+
		"\t\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01ff\b\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0015\u0001\u0015\u0003\u0015\u0205\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0003\u0016\u0209\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0003\u0019\u0215\b\u0019\u0001\u0019\u0001\u0019\u0001\u001a"+
		"\u0005\u001a\u021a\b\u001a\n\u001a\f\u001a\u021d\t\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0005\u001c\u0227\b\u001c\n\u001c\f\u001c\u022a\t\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u022e\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u0238\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e"+
		"\u023e\b\u001e\n\u001e\f\u001e\u0241\t\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u0248\b\u001f\n\u001f\f\u001f"+
		"\u024b\t\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001 \u0005 \u0251"+
		"\b \n \f \u0254\t \u0001 \u0001 \u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0003!\u025e\b!\u0001\"\u0005\"\u0261\b\"\n\"\f\"\u0264\t\"\u0001\""+
		"\u0001\"\u0001\"\u0001#\u0005#\u026a\b#\n#\f#\u026d\t#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0005#\u0274\b#\n#\f#\u0277\t#\u0001#\u0001#\u0003#\u027b"+
		"\b#\u0001#\u0001#\u0001$\u0001$\u0001$\u0005$\u0282\b$\n$\f$\u0285\t$"+
		"\u0001%\u0001%\u0001%\u0003%\u028a\b%\u0001&\u0001&\u0001&\u0005&\u028f"+
		"\b&\n&\f&\u0292\t&\u0001\'\u0001\'\u0003\'\u0296\b\'\u0001(\u0001(\u0001"+
		"(\u0001(\u0005(\u029c\b(\n(\f(\u029f\t(\u0001(\u0003(\u02a2\b(\u0003("+
		"\u02a4\b(\u0001(\u0001(\u0001)\u0001)\u0003)\u02aa\b)\u0001)\u0001)\u0005"+
		")\u02ae\b)\n)\f)\u02b1\t)\u0001)\u0001)\u0003)\u02b5\b)\u0001*\u0001*"+
		"\u0005*\u02b9\b*\n*\f*\u02bc\t*\u0001*\u0001*\u0001*\u0003*\u02c1\b*\u0003"+
		"*\u02c3\b*\u0001+\u0001+\u0001+\u0005+\u02c8\b+\n+\f+\u02cb\t+\u0001,"+
		"\u0001,\u0003,\u02cf\b,\u0001,\u0001,\u0001,\u0003,\u02d4\b,\u0001,\u0003"+
		",\u02d7\b,\u0003,\u02d9\b,\u0001,\u0001,\u0001-\u0001-\u0001-\u0001-\u0005"+
		"-\u02e1\b-\n-\f-\u02e4\t-\u0001-\u0001-\u0001.\u0001.\u0001.\u0005.\u02eb"+
		"\b.\n.\f.\u02ee\t.\u0001.\u0001.\u0003.\u02f2\b.\u0001.\u0003.\u02f5\b"+
		".\u0001/\u0005/\u02f8\b/\n/\f/\u02fb\t/\u0001/\u0001/\u0001/\u00010\u0005"+
		"0\u0301\b0\n0\f0\u0304\t0\u00010\u00010\u00050\u0308\b0\n0\f0\u030b\t"+
		"0\u00010\u00010\u00010\u00011\u00011\u00011\u00051\u0313\b1\n1\f1\u0316"+
		"\t1\u00012\u00052\u0319\b2\n2\f2\u031c\t2\u00012\u00012\u00012\u00013"+
		"\u00013\u00013\u00053\u0324\b3\n3\f3\u0327\t3\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00034\u0330\b4\u00015\u00015\u00016\u00016\u0001"+
		"7\u00017\u00017\u00057\u0339\b7\n7\f7\u033c\t7\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00038\u0344\b8\u00018\u00018\u00018\u00038\u0349\b8\u0001"+
		"8\u00038\u034c\b8\u00019\u00019\u00019\u00059\u0351\b9\n9\f9\u0354\t9"+
		"\u0001:\u0001:\u0001:\u0001:\u0001;\u0001;\u0001;\u0003;\u035d\b;\u0001"+
		"<\u0001<\u0001<\u0001<\u0005<\u0363\b<\n<\f<\u0366\t<\u0003<\u0368\b<"+
		"\u0001<\u0003<\u036b\b<\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0001>\u0001>\u0005>\u0376\b>\n>\f>\u0379\t>\u0001>\u0001>\u0001?\u0005"+
		"?\u037e\b?\n?\f?\u0381\t?\u0001?\u0001?\u0003?\u0385\b?\u0001@\u0001@"+
		"\u0001@\u0001@\u0001@\u0001@\u0003@\u038d\b@\u0001@\u0001@\u0003@\u0391"+
		"\b@\u0001@\u0001@\u0003@\u0395\b@\u0001@\u0001@\u0003@\u0399\b@\u0001"+
		"@\u0001@\u0003@\u039d\b@\u0003@\u039f\b@\u0001A\u0001A\u0003A\u03a3\b"+
		"A\u0001B\u0001B\u0001B\u0001B\u0003B\u03a9\bB\u0001C\u0001C\u0001D\u0001"+
		"D\u0001D\u0001E\u0003E\u03b1\bE\u0001E\u0001E\u0001E\u0001E\u0001F\u0001"+
		"F\u0005F\u03b9\bF\nF\fF\u03bc\tF\u0001F\u0001F\u0001G\u0001G\u0005G\u03c2"+
		"\bG\nG\fG\u03c5\tG\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0003"+
		"G\u03ce\bG\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0003G\u03d6\bG\u0001"+
		"G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001"+
		"G\u0001G\u0003G\u03e4\bG\u0001H\u0001H\u0001I\u0001I\u0001I\u0003I\u03eb"+
		"\bI\u0001I\u0001I\u0001I\u0003I\u03f0\bI\u0001I\u0001I\u0001J\u0001J\u0003"+
		"J\u03f6\bJ\u0001J\u0001J\u0001K\u0001K\u0001K\u0005K\u03fd\bK\nK\fK\u0400"+
		"\tK\u0001L\u0001L\u0001L\u0001M\u0001M\u0001M\u0005M\u0408\bM\nM\fM\u040b"+
		"\tM\u0001M\u0001M\u0001N\u0001N\u0005N\u0411\bN\nN\fN\u0414\tN\u0001N"+
		"\u0001N\u0001O\u0001O\u0001O\u0001O\u0001O\u0003O\u041d\bO\u0001P\u0005"+
		"P\u0420\bP\nP\fP\u0423\tP\u0001P\u0001P\u0001P\u0001P\u0001P\u0001P\u0001"+
		"P\u0001P\u0003P\u042d\bP\u0001Q\u0001Q\u0001R\u0001R\u0001S\u0005S\u0434"+
		"\bS\nS\fS\u0437\tS\u0001S\u0001S\u0001S\u0003S\u043c\bS\u0001T\u0001T"+
		"\u0001T\u0001T\u0001T\u0003T\u0443\bT\u0001T\u0001T\u0001T\u0001T\u0001"+
		"T\u0001T\u0001T\u0003T\u044c\bT\u0001T\u0001T\u0001T\u0001T\u0001T\u0001"+
		"T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001"+
		"T\u0001T\u0001T\u0001T\u0004T\u0461\bT\u000bT\fT\u0462\u0001T\u0003T\u0466"+
		"\bT\u0001T\u0003T\u0469\bT\u0001T\u0001T\u0001T\u0001T\u0005T\u046f\b"+
		"T\nT\fT\u0472\tT\u0001T\u0003T\u0475\bT\u0001T\u0001T\u0001T\u0001T\u0005"+
		"T\u047b\bT\nT\fT\u047e\tT\u0001T\u0005T\u0481\bT\nT\fT\u0484\tT\u0001"+
		"T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0003T\u048e\bT\u0001"+
		"T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0003T\u0497\bT\u0001T\u0001"+
		"T\u0001T\u0003T\u049c\bT\u0001T\u0001T\u0001T\u0001T\u0001T\u0001T\u0001"+
		"T\u0001T\u0001T\u0001T\u0001T\u0003T\u04a9\bT\u0001T\u0001T\u0001T\u0001"+
		"T\u0003T\u04af\bT\u0001U\u0001U\u0001U\u0005U\u04b4\bU\nU\fU\u04b7\tU"+
		"\u0001U\u0001U\u0001U\u0001U\u0001U\u0001V\u0001V\u0001V\u0005V\u04c1"+
		"\bV\nV\fV\u04c4\tV\u0001W\u0001W\u0001W\u0001X\u0001X\u0001X\u0003X\u04cc"+
		"\bX\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0005Y\u04d3\bY\nY\fY\u04d6\tY"+
		"\u0001Z\u0005Z\u04d9\bZ\nZ\fZ\u04dc\tZ\u0001Z\u0001Z\u0001Z\u0001Z\u0001"+
		"Z\u0003Z\u04e3\bZ\u0001Z\u0001Z\u0001Z\u0001Z\u0003Z\u04e9\bZ\u0001[\u0004"+
		"[\u04ec\b[\u000b[\f[\u04ed\u0001[\u0004[\u04f1\b[\u000b[\f[\u04f2\u0001"+
		"\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003\\\u04fb\b\\\u0001\\\u0001"+
		"\\\u0001\\\u0003\\\u0500\b\\\u0001]\u0001]\u0003]\u0504\b]\u0001]\u0001"+
		"]\u0003]\u0508\b]\u0001]\u0001]\u0003]\u050c\b]\u0003]\u050e\b]\u0001"+
		"^\u0001^\u0003^\u0512\b^\u0001_\u0005_\u0515\b_\n_\f_\u0518\t_\u0001_"+
		"\u0001_\u0003_\u051c\b_\u0001_\u0001_\u0001_\u0001_\u0001`\u0001`\u0001"+
		"`\u0001`\u0001a\u0001a\u0001a\u0005a\u0529\ba\na\fa\u052c\ta\u0001b\u0001"+
		"b\u0001b\u0003b\u0531\bb\u0001b\u0001b\u0001b\u0001b\u0001b\u0003b\u0538"+
		"\bb\u0001b\u0001b\u0001b\u0001b\u0003b\u053e\bb\u0001b\u0003b\u0541\b"+
		"b\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0005c\u054a\bc\nc"+
		"\fc\u054d\tc\u0001c\u0001c\u0001c\u0005c\u0552\bc\nc\fc\u0555\tc\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0003c\u0563\bc\u0001c\u0001c\u0003c\u0567\bc\u0001c\u0001c\u0001"+
		"c\u0003c\u056c\bc\u0001c\u0001c\u0003c\u0570\bc\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0003c\u0580\bc\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0003c\u05a8\bc\u0001c\u0001c\u0001c\u0001c\u0003c\u05ae\bc\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0003"+
		"c\u05bb\bc\u0001c\u0001c\u0001c\u0003c\u05c0\bc\u0001c\u0005c\u05c3\b"+
		"c\nc\fc\u05c6\tc\u0001d\u0005d\u05c9\bd\nd\fd\u05cc\td\u0001d\u0001d\u0005"+
		"d\u05d0\bd\nd\fd\u05d3\td\u0001d\u0001d\u0001e\u0001e\u0001e\u0001e\u0001"+
		"f\u0001f\u0001f\u0003f\u05de\bf\u0001f\u0001f\u0001f\u0001f\u0001f\u0005"+
		"f\u05e5\bf\nf\ff\u05e8\tf\u0001f\u0001f\u0001f\u0001f\u0003f\u05ee\bf"+
		"\u0001f\u0003f\u05f1\bf\u0001g\u0001g\u0003g\u05f5\bg\u0001h\u0001h\u0001"+
		"h\u0001h\u0001h\u0001h\u0001h\u0001h\u0001h\u0001h\u0001h\u0001h\u0001"+
		"h\u0001h\u0001h\u0001h\u0003h\u0607\bh\u0003h\u0609\bh\u0001i\u0001i\u0001"+
		"i\u0001i\u0005i\u060f\bi\ni\fi\u0612\ti\u0001i\u0001i\u0001j\u0001j\u0001"+
		"j\u0001j\u0003j\u061a\bj\u0001j\u0001j\u0001j\u0001j\u0001j\u0003j\u0621"+
		"\bj\u0001k\u0001k\u0001k\u0001k\u0001k\u0001k\u0005k\u0629\bk\nk\fk\u062c"+
		"\tk\u0001k\u0001k\u0005k\u0630\bk\nk\fk\u0633\tk\u0001k\u0001k\u0001k"+
		"\u0005k\u0638\bk\nk\fk\u063b\tk\u0003k\u063d\bk\u0001k\u0001k\u0001k\u0005"+
		"k\u0642\bk\nk\fk\u0645\tk\u0001l\u0001l\u0005l\u0649\bl\nl\fl\u064c\t"+
		"l\u0003l\u064e\bl\u0001m\u0001m\u0001m\u0003m\u0653\bm\u0001m\u0005m\u0656"+
		"\bm\nm\fm\u0659\tm\u0001m\u0001m\u0003m\u065d\bm\u0001n\u0001n\u0001n"+
		"\u0001n\u0001n\u0001n\u0001n\u0003n\u0666\bn\u0003n\u0668\bn\u0001o\u0001"+
		"o\u0003o\u066c\bo\u0001o\u0001o\u0001o\u0003o\u0671\bo\u0005o\u0673\b"+
		"o\no\fo\u0676\to\u0001o\u0003o\u0679\bo\u0001p\u0001p\u0003p\u067d\bp"+
		"\u0001p\u0001p\u0001q\u0001q\u0001q\u0001q\u0005q\u0685\bq\nq\fq\u0688"+
		"\tq\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0005q\u0691\bq\n"+
		"q\fq\u0694\tq\u0001q\u0001q\u0005q\u0698\bq\nq\fq\u069b\tq\u0003q\u069d"+
		"\bq\u0001r\u0001r\u0003r\u06a1\br\u0001s\u0001s\u0001s\u0001t\u0001t\u0001"+
		"t\u0003t\u06a9\bt\u0001u\u0001u\u0001u\u0003u\u06ae\bu\u0001v\u0001v\u0001"+
		"v\u0001v\u0001w\u0001w\u0001w\u0005w\u06b7\bw\nw\fw\u06ba\tw\u0001x\u0005"+
		"x\u06bd\bx\nx\fx\u06c0\tx\u0001x\u0001x\u0003x\u06c4\bx\u0001x\u0005x"+
		"\u06c7\bx\nx\fx\u06ca\tx\u0001x\u0001x\u0005x\u06ce\bx\nx\fx\u06d1\tx"+
		"\u0001y\u0001y\u0001z\u0001z\u0001z\u0001z\u0005z\u06d9\bz\nz\fz\u06dc"+
		"\tz\u0001z\u0001z\u0001{\u0001{\u0001{\u0003{\u06e3\b{\u0001{\u0001{\u0003"+
		"{\u06e7\b{\u0003{\u06e9\b{\u0001|\u0001|\u0001|\u0001|\u0001|\u0003|\u06f0"+
		"\b|\u0001}\u0001}\u0003}\u06f4\b}\u0001}\u0001}\u0001~\u0001~\u0003~\u06fa"+
		"\b~\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0004"+
		"\u007f\u0701\b\u007f\u000b\u007f\f\u007f\u0702\u0001\u007f\u0003\u007f"+
		"\u0706\b\u007f\u0001\u0080\u0004\u0080\u0709\b\u0080\u000b\u0080\f\u0080"+
		"\u070a\u0001\u0080\u0001\u0702\u0002\u00c6\u00d6\u0081\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e"+
		"\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6"+
		"\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce"+
		"\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6"+
		"\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe"+
		"\u0100\u0000\u0010\u0002\u0000\u0011\u0011((\u0001\u0000CF\u0001\u0000"+
		"GH\u0002\u0000&&<<\u0002\u00003A\u0081\u0081\u0003\u00003<?A\u0081\u0081"+
		"\u0001\u0000dg\u0001\u0000Z[\u0002\u0000himm\u0001\u0000fg\u0002\u0000"+
		"XY_`\u0002\u0000^^aa\u0002\u0000WWnx\u0001\u0000de\u0002\u0000]]yy\b\u0000"+
		"\u0003\u0003\u0005\u0005\b\b\u000e\u000e\u0014\u0014\u001b\u001b\u001d"+
		"\u001d%%\u07d9\u0000\u0114\u0001\u0000\u0000\u0000\u0002\u0119\u0001\u0000"+
		"\u0000\u0000\u0004\u0120\u0001\u0000\u0000\u0000\u0006\u0139\u0001\u0000"+
		"\u0000\u0000\b\u0140\u0001\u0000\u0000\u0000\n\u014c\u0001\u0000\u0000"+
		"\u0000\f\u0150\u0001\u0000\u0000\u0000\u000e\u0152\u0001\u0000\u0000\u0000"+
		"\u0010\u0165\u0001\u0000\u0000\u0000\u0012\u0173\u0001\u0000\u0000\u0000"+
		"\u0014\u0181\u0001\u0000\u0000\u0000\u0016\u0189\u0001\u0000\u0000\u0000"+
		"\u0018\u019b\u0001\u0000\u0000\u0000\u001a\u01a6\u0001\u0000\u0000\u0000"+
		"\u001c\u01b0\u0001\u0000\u0000\u0000\u001e\u01b7\u0001\u0000\u0000\u0000"+
		" \u01c6\u0001\u0000\u0000\u0000\"\u01cf\u0001\u0000\u0000\u0000$\u01e4"+
		"\u0001\u0000\u0000\u0000&\u01f0\u0001\u0000\u0000\u0000(\u01f2\u0001\u0000"+
		"\u0000\u0000*\u0204\u0001\u0000\u0000\u0000,\u0208\u0001\u0000\u0000\u0000"+
		".\u020a\u0001\u0000\u0000\u00000\u020d\u0001\u0000\u0000\u00002\u0210"+
		"\u0001\u0000\u0000\u00004\u021b\u0001\u0000\u0000\u00006\u0221\u0001\u0000"+
		"\u0000\u00008\u022d\u0001\u0000\u0000\u0000:\u0237\u0001\u0000\u0000\u0000"+
		"<\u0239\u0001\u0000\u0000\u0000>\u0244\u0001\u0000\u0000\u0000@\u0252"+
		"\u0001\u0000\u0000\u0000B\u025d\u0001\u0000\u0000\u0000D\u0262\u0001\u0000"+
		"\u0000\u0000F\u026b\u0001\u0000\u0000\u0000H\u027e\u0001\u0000\u0000\u0000"+
		"J\u0286\u0001\u0000\u0000\u0000L\u028b\u0001\u0000\u0000\u0000N\u0295"+
		"\u0001\u0000\u0000\u0000P\u0297\u0001\u0000\u0000\u0000R\u02af\u0001\u0000"+
		"\u0000\u0000T\u02c2\u0001\u0000\u0000\u0000V\u02c4\u0001\u0000\u0000\u0000"+
		"X\u02cc\u0001\u0000\u0000\u0000Z\u02dc\u0001\u0000\u0000\u0000\\\u02f4"+
		"\u0001\u0000\u0000\u0000^\u02f9\u0001\u0000\u0000\u0000`\u0302\u0001\u0000"+
		"\u0000\u0000b\u030f\u0001\u0000\u0000\u0000d\u031a\u0001\u0000\u0000\u0000"+
		"f\u0320\u0001\u0000\u0000\u0000h\u032f\u0001\u0000\u0000\u0000j\u0331"+
		"\u0001\u0000\u0000\u0000l\u0333\u0001\u0000\u0000\u0000n\u033a\u0001\u0000"+
		"\u0000\u0000p\u0343\u0001\u0000\u0000\u0000r\u034d\u0001\u0000\u0000\u0000"+
		"t\u0355\u0001\u0000\u0000\u0000v\u035c\u0001\u0000\u0000\u0000x\u035e"+
		"\u0001\u0000\u0000\u0000z\u036e\u0001\u0000\u0000\u0000|\u0373\u0001\u0000"+
		"\u0000\u0000~\u0384\u0001\u0000\u0000\u0000\u0080\u039e\u0001\u0000\u0000"+
		"\u0000\u0082\u03a2\u0001\u0000\u0000\u0000\u0084\u03a4\u0001\u0000\u0000"+
		"\u0000\u0086\u03aa\u0001\u0000\u0000\u0000\u0088\u03ac\u0001\u0000\u0000"+
		"\u0000\u008a\u03b0\u0001\u0000\u0000\u0000\u008c\u03b6\u0001\u0000\u0000"+
		"\u0000\u008e\u03e3\u0001\u0000\u0000\u0000\u0090\u03e5\u0001\u0000\u0000"+
		"\u0000\u0092\u03e7\u0001\u0000\u0000\u0000\u0094\u03f3\u0001\u0000\u0000"+
		"\u0000\u0096\u03f9\u0001\u0000\u0000\u0000\u0098\u0401\u0001\u0000\u0000"+
		"\u0000\u009a\u0404\u0001\u0000\u0000\u0000\u009c\u040e\u0001\u0000\u0000"+
		"\u0000\u009e\u041c\u0001\u0000\u0000\u0000\u00a0\u0421\u0001\u0000\u0000"+
		"\u0000\u00a2\u042e\u0001\u0000\u0000\u0000\u00a4\u0430\u0001\u0000\u0000"+
		"\u0000\u00a6\u0435\u0001\u0000\u0000\u0000\u00a8\u04ae\u0001\u0000\u0000"+
		"\u0000\u00aa\u04b0\u0001\u0000\u0000\u0000\u00ac\u04bd\u0001\u0000\u0000"+
		"\u0000\u00ae\u04c5\u0001\u0000\u0000\u0000\u00b0\u04c8\u0001\u0000\u0000"+
		"\u0000\u00b2\u04cf\u0001\u0000\u0000\u0000\u00b4\u04e8\u0001\u0000\u0000"+
		"\u0000\u00b6\u04eb\u0001\u0000\u0000\u0000\u00b8\u04ff\u0001\u0000\u0000"+
		"\u0000\u00ba\u050d\u0001\u0000\u0000\u0000\u00bc\u0511\u0001\u0000\u0000"+
		"\u0000\u00be\u0516\u0001\u0000\u0000\u0000\u00c0\u0521\u0001\u0000\u0000"+
		"\u0000\u00c2\u0525\u0001\u0000\u0000\u0000\u00c4\u0540\u0001\u0000\u0000"+
		"\u0000\u00c6\u056f\u0001\u0000\u0000\u0000\u00c8\u05ca\u0001\u0000\u0000"+
		"\u0000\u00ca\u05d6\u0001\u0000\u0000\u0000\u00cc\u05f0\u0001\u0000\u0000"+
		"\u0000\u00ce\u05f4\u0001\u0000\u0000\u0000\u00d0\u0608\u0001\u0000\u0000"+
		"\u0000\u00d2\u060a\u0001\u0000\u0000\u0000\u00d4\u0620\u0001\u0000\u0000"+
		"\u0000\u00d6\u063c\u0001\u0000\u0000\u0000\u00d8\u064d\u0001\u0000\u0000"+
		"\u0000\u00da\u0652\u0001\u0000\u0000\u0000\u00dc\u0667\u0001\u0000\u0000"+
		"\u0000\u00de\u0678\u0001\u0000\u0000\u0000\u00e0\u067a\u0001\u0000\u0000"+
		"\u0000\u00e2\u0680\u0001\u0000\u0000\u0000\u00e4\u069e\u0001\u0000\u0000"+
		"\u0000\u00e6\u06a2\u0001\u0000\u0000\u0000\u00e8\u06a8\u0001\u0000\u0000"+
		"\u0000\u00ea\u06ad\u0001\u0000\u0000\u0000\u00ec\u06af\u0001\u0000\u0000"+
		"\u0000\u00ee\u06b3\u0001\u0000\u0000\u0000\u00f0\u06be\u0001\u0000\u0000"+
		"\u0000\u00f2\u06d2\u0001\u0000\u0000\u0000\u00f4\u06d4\u0001\u0000\u0000"+
		"\u0000\u00f6\u06e8\u0001\u0000\u0000\u0000\u00f8\u06ef\u0001\u0000\u0000"+
		"\u0000\u00fa\u06f1\u0001\u0000\u0000\u0000\u00fc\u06f9\u0001\u0000\u0000"+
		"\u0000\u00fe\u0705\u0001\u0000\u0000\u0000\u0100\u0708\u0001\u0000\u0000"+
		"\u0000\u0102\u0104\u0003\u0002\u0001\u0000\u0103\u0102\u0001\u0000\u0000"+
		"\u0000\u0103\u0104\u0001\u0000\u0000\u0000\u0104\u0108\u0001\u0000\u0000"+
		"\u0000\u0105\u0107\u0003\u0004\u0002\u0000\u0106\u0105\u0001\u0000\u0000"+
		"\u0000\u0107\u010a\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000\u0000"+
		"\u0000\u0108\u0109\u0001\u0000\u0000\u0000\u0109\u010e\u0001\u0000\u0000"+
		"\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010b\u010d\u0003\u0006\u0003"+
		"\u0000\u010c\u010b\u0001\u0000\u0000\u0000\u010d\u0110\u0001\u0000\u0000"+
		"\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000"+
		"\u0000\u010f\u0115\u0001\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000"+
		"\u0000\u0111\u0112\u0003\u008aE\u0000\u0112\u0113\u0005\u0000\u0000\u0001"+
		"\u0113\u0115\u0001\u0000\u0000\u0000\u0114\u0103\u0001\u0000\u0000\u0000"+
		"\u0114\u0111\u0001\u0000\u0000\u0000\u0115\u0001\u0001\u0000\u0000\u0000"+
		"\u0116\u0118\u0003p8\u0000\u0117\u0116\u0001\u0000\u0000\u0000\u0118\u011b"+
		"\u0001\u0000\u0000\u0000\u0119\u0117\u0001\u0000\u0000\u0000\u0119\u011a"+
		"\u0001\u0000\u0000\u0000\u011a\u011c\u0001\u0000\u0000\u0000\u011b\u0119"+
		"\u0001\u0000\u0000\u0000\u011c\u011d\u0005 \u0000\u0000\u011d\u011e\u0003"+
		"f3\u0000\u011e\u011f\u0005T\u0000\u0000\u011f\u0003\u0001\u0000\u0000"+
		"\u0000\u0120\u0122\u0005\u0019\u0000\u0000\u0121\u0123\u0005&\u0000\u0000"+
		"\u0122\u0121\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000"+
		"\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0127\u0003f3\u0000\u0125\u0126"+
		"\u0005V\u0000\u0000\u0126\u0128\u0005h\u0000\u0000\u0127\u0125\u0001\u0000"+
		"\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000\u0128\u0129\u0001\u0000"+
		"\u0000\u0000\u0129\u012a\u0005T\u0000\u0000\u012a\u0005\u0001\u0000\u0000"+
		"\u0000\u012b\u012d\u0003\n\u0005\u0000\u012c\u012b\u0001\u0000\u0000\u0000"+
		"\u012d\u0130\u0001\u0000\u0000\u0000\u012e\u012c\u0001\u0000\u0000\u0000"+
		"\u012e\u012f\u0001\u0000\u0000\u0000\u012f\u0136\u0001\u0000\u0000\u0000"+
		"\u0130\u012e\u0001\u0000\u0000\u0000\u0131\u0137\u0003\u000e\u0007\u0000"+
		"\u0132\u0137\u0003\u0016\u000b\u0000\u0133\u0137\u0003\u001e\u000f\u0000"+
		"\u0134\u0137\u0003z=\u0000\u0135\u0137\u0003\u0092I\u0000\u0136\u0131"+
		"\u0001\u0000\u0000\u0000\u0136\u0132\u0001\u0000\u0000\u0000\u0136\u0133"+
		"\u0001\u0000\u0000\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0135"+
		"\u0001\u0000\u0000\u0000\u0137\u013a\u0001\u0000\u0000\u0000\u0138\u013a"+
		"\u0005T\u0000\u0000\u0139\u012e\u0001\u0000\u0000\u0000\u0139\u0138\u0001"+
		"\u0000\u0000\u0000\u013a\u0007\u0001\u0000\u0000\u0000\u013b\u0141\u0003"+
		"\n\u0005\u0000\u013c\u0141\u0005\u001e\u0000\u0000\u013d\u0141\u0005*"+
		"\u0000\u0000\u013e\u0141\u0005.\u0000\u0000\u013f\u0141\u00051\u0000\u0000"+
		"\u0140\u013b\u0001\u0000\u0000\u0000\u0140\u013c\u0001\u0000\u0000\u0000"+
		"\u0140\u013d\u0001\u0000\u0000\u0000\u0140\u013e\u0001\u0000\u0000\u0000"+
		"\u0140\u013f\u0001\u0000\u0000\u0000\u0141\t\u0001\u0000\u0000\u0000\u0142"+
		"\u014d\u0003p8\u0000\u0143\u014d\u0005#\u0000\u0000\u0144\u014d\u0005"+
		"\"\u0000\u0000\u0145\u014d\u0005!\u0000\u0000\u0146\u014d\u0005&\u0000"+
		"\u0000\u0147\u014d\u0005\u0001\u0000\u0000\u0148\u014d\u0005\u0012\u0000"+
		"\u0000\u0149\u014d\u0005\'\u0000\u0000\u014a\u014d\u0005@\u0000\u0000"+
		"\u014b\u014d\u0005B\u0000\u0000\u014c\u0142\u0001\u0000\u0000\u0000\u014c"+
		"\u0143\u0001\u0000\u0000\u0000\u014c\u0144\u0001\u0000\u0000\u0000\u014c"+
		"\u0145\u0001\u0000\u0000\u0000\u014c\u0146\u0001\u0000\u0000\u0000\u014c"+
		"\u0147\u0001\u0000\u0000\u0000\u014c\u0148\u0001\u0000\u0000\u0000\u014c"+
		"\u0149\u0001\u0000\u0000\u0000\u014c\u014a\u0001\u0000\u0000\u0000\u014c"+
		"\u014b\u0001\u0000\u0000\u0000\u014d\u000b\u0001\u0000\u0000\u0000\u014e"+
		"\u0151\u0005\u0012\u0000\u0000\u014f\u0151\u0003p8\u0000\u0150\u014e\u0001"+
		"\u0000\u0000\u0000\u0150\u014f\u0001\u0000\u0000\u0000\u0151\r\u0001\u0000"+
		"\u0000\u0000\u0152\u0153\u0005\t\u0000\u0000\u0153\u0155\u0003\u00a2Q"+
		"\u0000\u0154\u0156\u0003\u0010\b\u0000\u0155\u0154\u0001\u0000\u0000\u0000"+
		"\u0155\u0156\u0001\u0000\u0000\u0000\u0156\u0159\u0001\u0000\u0000\u0000"+
		"\u0157\u0158\u0005\u0011\u0000\u0000\u0158\u015a\u0003\u00f0x\u0000\u0159"+
		"\u0157\u0001\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a"+
		"\u015d\u0001\u0000\u0000\u0000\u015b\u015c\u0005\u0018\u0000\u0000\u015c"+
		"\u015e\u0003\u00eew\u0000\u015d\u015b\u0001\u0000\u0000\u0000\u015d\u015e"+
		"\u0001\u0000\u0000\u0000\u015e\u0161\u0001\u0000\u0000\u0000\u015f\u0160"+
		"\u0005A\u0000\u0000\u0160\u0162\u0003\u00eew\u0000\u0161\u015f\u0001\u0000"+
		"\u0000\u0000\u0161\u0162\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000"+
		"\u0000\u0000\u0163\u0164\u0003 \u0010\u0000\u0164\u000f\u0001\u0000\u0000"+
		"\u0000\u0165\u0166\u0005Y\u0000\u0000\u0166\u016b\u0003\u0012\t\u0000"+
		"\u0167\u0168\u0005U\u0000\u0000\u0168\u016a\u0003\u0012\t\u0000\u0169"+
		"\u0167\u0001\u0000\u0000\u0000\u016a\u016d\u0001\u0000\u0000\u0000\u016b"+
		"\u0169\u0001\u0000\u0000\u0000\u016b\u016c\u0001\u0000\u0000\u0000\u016c"+
		"\u016e\u0001\u0000\u0000\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016e"+
		"\u016f\u0005X\u0000\u0000\u016f\u0011\u0001\u0000\u0000\u0000\u0170\u0172"+
		"\u0003p8\u0000\u0171\u0170\u0001\u0000\u0000\u0000\u0172\u0175\u0001\u0000"+
		"\u0000\u0000\u0173\u0171\u0001\u0000\u0000\u0000\u0173\u0174\u0001\u0000"+
		"\u0000\u0000\u0174\u0176\u0001\u0000\u0000\u0000\u0175\u0173\u0001\u0000"+
		"\u0000\u0000\u0176\u017f\u0003\u00a2Q\u0000\u0177\u017b\u0005\u0011\u0000"+
		"\u0000\u0178\u017a\u0003p8\u0000\u0179\u0178\u0001\u0000\u0000\u0000\u017a"+
		"\u017d\u0001\u0000\u0000\u0000\u017b\u0179\u0001\u0000\u0000\u0000\u017b"+
		"\u017c\u0001\u0000\u0000\u0000\u017c\u017e\u0001\u0000\u0000\u0000\u017d"+
		"\u017b\u0001\u0000\u0000\u0000\u017e\u0180\u0003\u0014\n\u0000\u017f\u0177"+
		"\u0001\u0000\u0000\u0000\u017f\u0180\u0001\u0000\u0000\u0000\u0180\u0013"+
		"\u0001\u0000\u0000\u0000\u0181\u0186\u0003\u00f0x\u0000\u0182\u0183\u0005"+
		"j\u0000\u0000\u0183\u0185\u0003\u00f0x\u0000\u0184\u0182\u0001\u0000\u0000"+
		"\u0000\u0185\u0188\u0001\u0000\u0000\u0000\u0186\u0184\u0001\u0000\u0000"+
		"\u0000\u0186\u0187\u0001\u0000\u0000\u0000\u0187\u0015\u0001\u0000\u0000"+
		"\u0000\u0188\u0186\u0001\u0000\u0000\u0000\u0189\u018a\u0005\u0010\u0000"+
		"\u0000\u018a\u018d\u0003\u00a2Q\u0000\u018b\u018c\u0005\u0018\u0000\u0000"+
		"\u018c\u018e\u0003\u00eew\u0000\u018d\u018b\u0001\u0000\u0000\u0000\u018d"+
		"\u018e\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f"+
		"\u0191\u0005P\u0000\u0000\u0190\u0192\u0003\u0018\f\u0000\u0191\u0190"+
		"\u0001\u0000\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u0194"+
		"\u0001\u0000\u0000\u0000\u0193\u0195\u0005U\u0000\u0000\u0194\u0193\u0001"+
		"\u0000\u0000\u0000\u0194\u0195\u0001\u0000\u0000\u0000\u0195\u0197\u0001"+
		"\u0000\u0000\u0000\u0196\u0198\u0003\u001c\u000e\u0000\u0197\u0196\u0001"+
		"\u0000\u0000\u0000\u0197\u0198\u0001\u0000\u0000\u0000\u0198\u0199\u0001"+
		"\u0000\u0000\u0000\u0199\u019a\u0005Q\u0000\u0000\u019a\u0017\u0001\u0000"+
		"\u0000\u0000\u019b\u01a0\u0003\u001a\r\u0000\u019c\u019d\u0005U\u0000"+
		"\u0000\u019d\u019f\u0003\u001a\r\u0000\u019e\u019c\u0001\u0000\u0000\u0000"+
		"\u019f\u01a2\u0001\u0000\u0000\u0000\u01a0\u019e\u0001\u0000\u0000\u0000"+
		"\u01a0\u01a1\u0001\u0000\u0000\u0000\u01a1\u0019\u0001\u0000\u0000\u0000"+
		"\u01a2\u01a0\u0001\u0000\u0000\u0000\u01a3\u01a5\u0003p8\u0000\u01a4\u01a3"+
		"\u0001\u0000\u0000\u0000\u01a5\u01a8\u0001\u0000\u0000\u0000\u01a6\u01a4"+
		"\u0001\u0000\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a9"+
		"\u0001\u0000\u0000\u0000\u01a8\u01a6\u0001\u0000\u0000\u0000\u01a9\u01ab"+
		"\u0003\u00a2Q\u0000\u01aa\u01ac\u0003\u00fa}\u0000\u01ab\u01aa\u0001\u0000"+
		"\u0000\u0000\u01ab\u01ac\u0001\u0000\u0000\u0000\u01ac\u01ae\u0001\u0000"+
		"\u0000\u0000\u01ad\u01af\u0003 \u0010\u0000\u01ae\u01ad\u0001\u0000\u0000"+
		"\u0000\u01ae\u01af\u0001\u0000\u0000\u0000\u01af\u001b\u0001\u0000\u0000"+
		"\u0000\u01b0\u01b4\u0005T\u0000\u0000\u01b1\u01b3\u0003$\u0012\u0000\u01b2"+
		"\u01b1\u0001\u0000\u0000\u0000\u01b3\u01b6\u0001\u0000\u0000\u0000\u01b4"+
		"\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b5\u0001\u0000\u0000\u0000\u01b5"+
		"\u001d\u0001\u0000\u0000\u0000\u01b6\u01b4\u0001\u0000\u0000\u0000\u01b7"+
		"\u01b8\u0005\u001c\u0000\u0000\u01b8\u01ba\u0003\u00a2Q\u0000\u01b9\u01bb"+
		"\u0003\u0010\b\u0000\u01ba\u01b9\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001"+
		"\u0000\u0000\u0000\u01bb\u01be\u0001\u0000\u0000\u0000\u01bc\u01bd\u0005"+
		"\u0011\u0000\u0000\u01bd\u01bf\u0003\u00eew\u0000\u01be\u01bc\u0001\u0000"+
		"\u0000\u0000\u01be\u01bf\u0001\u0000\u0000\u0000\u01bf\u01c2\u0001\u0000"+
		"\u0000\u0000\u01c0\u01c1\u0005A\u0000\u0000\u01c1\u01c3\u0003\u00eew\u0000"+
		"\u01c2\u01c0\u0001\u0000\u0000\u0000\u01c2\u01c3\u0001\u0000\u0000\u0000"+
		"\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01c5\u0003\"\u0011\u0000\u01c5"+
		"\u001f\u0001\u0000\u0000\u0000\u01c6\u01ca\u0005P\u0000\u0000\u01c7\u01c9"+
		"\u0003$\u0012\u0000\u01c8\u01c7\u0001\u0000\u0000\u0000\u01c9\u01cc\u0001"+
		"\u0000\u0000\u0000\u01ca\u01c8\u0001\u0000\u0000\u0000\u01ca\u01cb\u0001"+
		"\u0000\u0000\u0000\u01cb\u01cd\u0001\u0000\u0000\u0000\u01cc\u01ca\u0001"+
		"\u0000\u0000\u0000\u01cd\u01ce\u0005Q\u0000\u0000\u01ce!\u0001\u0000\u0000"+
		"\u0000\u01cf\u01d3\u0005P\u0000\u0000\u01d0\u01d2\u00038\u001c\u0000\u01d1"+
		"\u01d0\u0001\u0000\u0000\u0000\u01d2\u01d5\u0001\u0000\u0000\u0000\u01d3"+
		"\u01d1\u0001\u0000\u0000\u0000\u01d3\u01d4\u0001\u0000\u0000\u0000\u01d4"+
		"\u01d6\u0001\u0000\u0000\u0000\u01d5\u01d3\u0001\u0000\u0000\u0000\u01d6"+
		"\u01d7\u0005Q\u0000\u0000\u01d7#\u0001\u0000\u0000\u0000\u01d8\u01e5\u0005"+
		"T\u0000\u0000\u01d9\u01db\u0005&\u0000\u0000\u01da\u01d9\u0001\u0000\u0000"+
		"\u0000\u01da\u01db\u0001\u0000\u0000\u0000\u01db\u01dc\u0001\u0000\u0000"+
		"\u0000\u01dc\u01e5\u0003\u009cN\u0000\u01dd\u01df\u0003\b\u0004\u0000"+
		"\u01de\u01dd\u0001\u0000\u0000\u0000\u01df\u01e2\u0001\u0000\u0000\u0000"+
		"\u01e0\u01de\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000"+
		"\u01e1\u01e3\u0001\u0000\u0000\u0000\u01e2\u01e0\u0001\u0000\u0000\u0000"+
		"\u01e3\u01e5\u0003&\u0013\u0000\u01e4\u01d8\u0001\u0000\u0000\u0000\u01e4"+
		"\u01da\u0001\u0000\u0000\u0000\u01e4\u01e0\u0001\u0000\u0000\u0000\u01e5"+
		"%\u0001\u0000\u0000\u0000\u01e6\u01f1\u0003\u0092I\u0000\u01e7\u01f1\u0003"+
		"(\u0014\u0000\u01e8\u01f1\u0003.\u0017\u0000\u01e9\u01f1\u00036\u001b"+
		"\u0000\u01ea\u01f1\u00032\u0019\u0000\u01eb\u01f1\u00030\u0018\u0000\u01ec"+
		"\u01f1\u0003\u001e\u000f\u0000\u01ed\u01f1\u0003z=\u0000\u01ee\u01f1\u0003"+
		"\u000e\u0007\u0000\u01ef\u01f1\u0003\u0016\u000b\u0000\u01f0\u01e6\u0001"+
		"\u0000\u0000\u0000\u01f0\u01e7\u0001\u0000\u0000\u0000\u01f0\u01e8\u0001"+
		"\u0000\u0000\u0000\u01f0\u01e9\u0001\u0000\u0000\u0000\u01f0\u01ea\u0001"+
		"\u0000\u0000\u0000\u01f0\u01eb\u0001\u0000\u0000\u0000\u01f0\u01ec\u0001"+
		"\u0000\u0000\u0000\u01f0\u01ed\u0001\u0000\u0000\u0000\u01f0\u01ee\u0001"+
		"\u0000\u0000\u0000\u01f0\u01ef\u0001\u0000\u0000\u0000\u01f1\'\u0001\u0000"+
		"\u0000\u0000\u01f2\u01f3\u0003,\u0016\u0000\u01f3\u01f4\u0003\u00a2Q\u0000"+
		"\u01f4\u01f9\u0003X,\u0000\u01f5\u01f6\u0005R\u0000\u0000\u01f6\u01f8"+
		"\u0005S\u0000\u0000\u01f7\u01f5\u0001\u0000\u0000\u0000\u01f8\u01fb\u0001"+
		"\u0000\u0000\u0000\u01f9\u01f7\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001"+
		"\u0000\u0000\u0000\u01fa\u01fe\u0001\u0000\u0000\u0000\u01fb\u01f9\u0001"+
		"\u0000\u0000\u0000\u01fc\u01fd\u0005-\u0000\u0000\u01fd\u01ff\u0003V+"+
		"\u0000\u01fe\u01fc\u0001\u0000\u0000\u0000\u01fe\u01ff\u0001\u0000\u0000"+
		"\u0000\u01ff\u0200\u0001\u0000\u0000\u0000\u0200\u0201\u0003*\u0015\u0000"+
		"\u0201)\u0001\u0000\u0000\u0000\u0202\u0205\u0003\u009cN\u0000\u0203\u0205"+
		"\u0005T\u0000\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0204\u0203\u0001"+
		"\u0000\u0000\u0000\u0205+\u0001\u0000\u0000\u0000\u0206\u0209\u0003\u00f0"+
		"x\u0000\u0207\u0209\u00050\u0000\u0000\u0208\u0206\u0001\u0000\u0000\u0000"+
		"\u0208\u0207\u0001\u0000\u0000\u0000\u0209-\u0001\u0000\u0000\u0000\u020a"+
		"\u020b\u0003\u0010\b\u0000\u020b\u020c\u0003(\u0014\u0000\u020c/\u0001"+
		"\u0000\u0000\u0000\u020d\u020e\u0003\u0010\b\u0000\u020e\u020f\u00032"+
		"\u0019\u0000\u020f1\u0001\u0000\u0000\u0000\u0210\u0211\u0003\u00a2Q\u0000"+
		"\u0211\u0214\u0003X,\u0000\u0212\u0213\u0005-\u0000\u0000\u0213\u0215"+
		"\u0003V+\u0000\u0214\u0212\u0001\u0000\u0000\u0000\u0214\u0215\u0001\u0000"+
		"\u0000\u0000\u0215\u0216\u0001\u0000\u0000\u0000\u0216\u0217\u0003\u009c"+
		"N\u0000\u02173\u0001\u0000\u0000\u0000\u0218\u021a\u0003\b\u0004\u0000"+
		"\u0219\u0218\u0001\u0000\u0000\u0000\u021a\u021d\u0001\u0000\u0000\u0000"+
		"\u021b\u0219\u0001\u0000\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000"+
		"\u021c\u021e\u0001\u0000\u0000\u0000\u021d\u021b\u0001\u0000\u0000\u0000"+
		"\u021e\u021f\u0003\u00a2Q\u0000\u021f\u0220\u0003\u009cN\u0000\u02205"+
		"\u0001\u0000\u0000\u0000\u0221\u0222\u0003\u00f0x\u0000\u0222\u0223\u0003"+
		"H$\u0000\u0223\u0224\u0005T\u0000\u0000\u02247\u0001\u0000\u0000\u0000"+
		"\u0225\u0227\u0003\b\u0004\u0000\u0226\u0225\u0001\u0000\u0000\u0000\u0227"+
		"\u022a\u0001\u0000\u0000\u0000\u0228\u0226\u0001\u0000\u0000\u0000\u0228"+
		"\u0229\u0001\u0000\u0000\u0000\u0229\u022b\u0001\u0000\u0000\u0000\u022a"+
		"\u0228\u0001\u0000\u0000\u0000\u022b\u022e\u0003:\u001d\u0000\u022c\u022e"+
		"\u0005T\u0000\u0000\u022d\u0228\u0001\u0000\u0000\u0000\u022d\u022c\u0001"+
		"\u0000\u0000\u0000\u022e9\u0001\u0000\u0000\u0000\u022f\u0238\u0003<\u001e"+
		"\u0000\u0230\u0238\u0003@ \u0000\u0231\u0238\u0003D\"\u0000\u0232\u0238"+
		"\u0003\u001e\u000f\u0000\u0233\u0238\u0003z=\u0000\u0234\u0238\u0003\u000e"+
		"\u0007\u0000\u0235\u0238\u0003\u0016\u000b\u0000\u0236\u0238\u0003\u0092"+
		"I\u0000\u0237\u022f\u0001\u0000\u0000\u0000\u0237\u0230\u0001\u0000\u0000"+
		"\u0000\u0237\u0231\u0001\u0000\u0000\u0000\u0237\u0232\u0001\u0000\u0000"+
		"\u0000\u0237\u0233\u0001\u0000\u0000\u0000\u0237\u0234\u0001\u0000\u0000"+
		"\u0000\u0237\u0235\u0001\u0000\u0000\u0000\u0237\u0236\u0001\u0000\u0000"+
		"\u0000\u0238;\u0001\u0000\u0000\u0000\u0239\u023a\u0003\u00f0x\u0000\u023a"+
		"\u023f\u0003>\u001f\u0000\u023b\u023c\u0005U\u0000\u0000\u023c\u023e\u0003"+
		">\u001f\u0000\u023d\u023b\u0001\u0000\u0000\u0000\u023e\u0241\u0001\u0000"+
		"\u0000\u0000\u023f\u023d\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000"+
		"\u0000\u0000\u0240\u0242\u0001\u0000\u0000\u0000\u0241\u023f\u0001\u0000"+
		"\u0000\u0000\u0242\u0243\u0005T\u0000\u0000\u0243=\u0001\u0000\u0000\u0000"+
		"\u0244\u0249\u0003\u00a2Q\u0000\u0245\u0246\u0005R\u0000\u0000\u0246\u0248"+
		"\u0005S\u0000\u0000\u0247\u0245\u0001\u0000\u0000\u0000\u0248\u024b\u0001"+
		"\u0000\u0000\u0000\u0249\u0247\u0001\u0000\u0000\u0000\u0249\u024a\u0001"+
		"\u0000\u0000\u0000\u024a\u024c\u0001\u0000\u0000\u0000\u024b\u0249\u0001"+
		"\u0000\u0000\u0000\u024c\u024d\u0005W\u0000\u0000\u024d\u024e\u0003N\'"+
		"\u0000\u024e?\u0001\u0000\u0000\u0000\u024f\u0251\u0003B!\u0000\u0250"+
		"\u024f\u0001\u0000\u0000\u0000\u0251\u0254\u0001\u0000\u0000\u0000\u0252"+
		"\u0250\u0001\u0000\u0000\u0000\u0252\u0253\u0001\u0000\u0000\u0000\u0253"+
		"\u0255\u0001\u0000\u0000\u0000\u0254\u0252\u0001\u0000\u0000\u0000\u0255"+
		"\u0256\u0003F#\u0000\u0256A\u0001\u0000\u0000\u0000\u0257\u025e\u0003"+
		"p8\u0000\u0258\u025e\u0005#\u0000\u0000\u0259\u025e\u0005\u0001\u0000"+
		"\u0000\u025a\u025e\u0005\f\u0000\u0000\u025b\u025e\u0005&\u0000\u0000"+
		"\u025c\u025e\u0005\'\u0000\u0000\u025d\u0257\u0001\u0000\u0000\u0000\u025d"+
		"\u0258\u0001\u0000\u0000\u0000\u025d\u0259\u0001\u0000\u0000\u0000\u025d"+
		"\u025a\u0001\u0000\u0000\u0000\u025d\u025b\u0001\u0000\u0000\u0000\u025d"+
		"\u025c\u0001\u0000\u0000\u0000\u025eC\u0001\u0000\u0000\u0000\u025f\u0261"+
		"\u0003B!\u0000\u0260\u025f\u0001\u0000\u0000\u0000\u0261\u0264\u0001\u0000"+
		"\u0000\u0000\u0262\u0260\u0001\u0000\u0000\u0000\u0262\u0263\u0001\u0000"+
		"\u0000\u0000\u0263\u0265\u0001\u0000\u0000\u0000\u0264\u0262\u0001\u0000"+
		"\u0000\u0000\u0265\u0266\u0003\u0010\b\u0000\u0266\u0267\u0003F#\u0000"+
		"\u0267E\u0001\u0000\u0000\u0000\u0268\u026a\u0003p8\u0000\u0269\u0268"+
		"\u0001\u0000\u0000\u0000\u026a\u026d\u0001\u0000\u0000\u0000\u026b\u0269"+
		"\u0001\u0000\u0000\u0000\u026b\u026c\u0001\u0000\u0000\u0000\u026c\u026e"+
		"\u0001\u0000\u0000\u0000\u026d\u026b\u0001\u0000\u0000\u0000\u026e\u026f"+
		"\u0003,\u0016\u0000\u026f\u0270\u0003\u00a2Q\u0000\u0270\u0275\u0003X"+
		",\u0000\u0271\u0272\u0005R\u0000\u0000\u0272\u0274\u0005S\u0000\u0000"+
		"\u0273\u0271\u0001\u0000\u0000\u0000\u0274\u0277\u0001\u0000\u0000\u0000"+
		"\u0275\u0273\u0001\u0000\u0000\u0000\u0275\u0276\u0001\u0000\u0000\u0000"+
		"\u0276\u027a\u0001\u0000\u0000\u0000\u0277\u0275\u0001\u0000\u0000\u0000"+
		"\u0278\u0279\u0005-\u0000\u0000\u0279\u027b\u0003V+\u0000\u027a\u0278"+
		"\u0001\u0000\u0000\u0000\u027a\u027b\u0001\u0000\u0000\u0000\u027b\u027c"+
		"\u0001\u0000\u0000\u0000\u027c\u027d\u0003*\u0015\u0000\u027dG\u0001\u0000"+
		"\u0000\u0000\u027e\u0283\u0003J%\u0000\u027f\u0280\u0005U\u0000\u0000"+
		"\u0280\u0282\u0003J%\u0000\u0281\u027f\u0001\u0000\u0000\u0000\u0282\u0285"+
		"\u0001\u0000\u0000\u0000\u0283\u0281\u0001\u0000\u0000\u0000\u0283\u0284"+
		"\u0001\u0000\u0000\u0000\u0284I\u0001\u0000\u0000\u0000\u0285\u0283\u0001"+
		"\u0000\u0000\u0000\u0286\u0289\u0003L&\u0000\u0287\u0288\u0005W\u0000"+
		"\u0000\u0288\u028a\u0003N\'\u0000\u0289\u0287\u0001\u0000\u0000\u0000"+
		"\u0289\u028a\u0001\u0000\u0000\u0000\u028aK\u0001\u0000\u0000\u0000\u028b"+
		"\u0290\u0003\u00a2Q\u0000\u028c\u028d\u0005R\u0000\u0000\u028d\u028f\u0005"+
		"S\u0000\u0000\u028e\u028c\u0001\u0000\u0000\u0000\u028f\u0292\u0001\u0000"+
		"\u0000\u0000\u0290\u028e\u0001\u0000\u0000\u0000\u0290\u0291\u0001\u0000"+
		"\u0000\u0000\u0291M\u0001\u0000\u0000\u0000\u0292\u0290\u0001\u0000\u0000"+
		"\u0000\u0293\u0296\u0003P(\u0000\u0294\u0296\u0003\u00c6c\u0000\u0295"+
		"\u0293\u0001\u0000\u0000\u0000\u0295\u0294\u0001\u0000\u0000\u0000\u0296"+
		"O\u0001\u0000\u0000\u0000\u0297\u02a3\u0005P\u0000\u0000\u0298\u029d\u0003"+
		"N\'\u0000\u0299\u029a\u0005U\u0000\u0000\u029a\u029c\u0003N\'\u0000\u029b"+
		"\u0299\u0001\u0000\u0000\u0000\u029c\u029f\u0001\u0000\u0000\u0000\u029d"+
		"\u029b\u0001\u0000\u0000\u0000\u029d\u029e\u0001\u0000\u0000\u0000\u029e"+
		"\u02a1\u0001\u0000\u0000\u0000\u029f\u029d\u0001\u0000\u0000\u0000\u02a0"+
		"\u02a2\u0005U\u0000\u0000\u02a1\u02a0\u0001\u0000\u0000\u0000\u02a1\u02a2"+
		"\u0001\u0000\u0000\u0000\u02a2\u02a4\u0001\u0000\u0000\u0000\u02a3\u0298"+
		"\u0001\u0000\u0000\u0000\u02a3\u02a4\u0001\u0000\u0000\u0000\u02a4\u02a5"+
		"\u0001\u0000\u0000\u0000\u02a5\u02a6\u0005Q\u0000\u0000\u02a6Q\u0001\u0000"+
		"\u0000\u0000\u02a7\u02a9\u0003\u00a2Q\u0000\u02a8\u02aa\u0003\u00f4z\u0000"+
		"\u02a9\u02a8\u0001\u0000\u0000\u0000\u02a9\u02aa\u0001\u0000\u0000\u0000"+
		"\u02aa\u02ab\u0001\u0000\u0000\u0000\u02ab\u02ac\u0005V\u0000\u0000\u02ac"+
		"\u02ae\u0001\u0000\u0000\u0000\u02ad\u02a7\u0001\u0000\u0000\u0000\u02ae"+
		"\u02b1\u0001\u0000\u0000\u0000\u02af\u02ad\u0001\u0000\u0000\u0000\u02af"+
		"\u02b0\u0001\u0000\u0000\u0000\u02b0\u02b2\u0001\u0000\u0000\u0000\u02b1"+
		"\u02af\u0001\u0000\u0000\u0000\u02b2\u02b4\u0003\u00a4R\u0000\u02b3\u02b5"+
		"\u0003\u00f4z\u0000\u02b4\u02b3\u0001\u0000\u0000\u0000\u02b4\u02b5\u0001"+
		"\u0000\u0000\u0000\u02b5S\u0001\u0000\u0000\u0000\u02b6\u02c3\u0003\u00f0"+
		"x\u0000\u02b7\u02b9\u0003p8\u0000\u02b8\u02b7\u0001\u0000\u0000\u0000"+
		"\u02b9\u02bc\u0001\u0000\u0000\u0000\u02ba\u02b8\u0001\u0000\u0000\u0000"+
		"\u02ba\u02bb\u0001\u0000\u0000\u0000\u02bb\u02bd\u0001\u0000\u0000\u0000"+
		"\u02bc\u02ba\u0001\u0000\u0000\u0000\u02bd\u02c0\u0005\\\u0000\u0000\u02be"+
		"\u02bf\u0007\u0000\u0000\u0000\u02bf\u02c1\u0003\u00f0x\u0000\u02c0\u02be"+
		"\u0001\u0000\u0000\u0000\u02c0\u02c1\u0001\u0000\u0000\u0000\u02c1\u02c3"+
		"\u0001\u0000\u0000\u0000\u02c2\u02b6\u0001\u0000\u0000\u0000\u02c2\u02ba"+
		"\u0001\u0000\u0000\u0000\u02c3U\u0001\u0000\u0000\u0000\u02c4\u02c9\u0003"+
		"f3\u0000\u02c5\u02c6\u0005U\u0000\u0000\u02c6\u02c8\u0003f3\u0000\u02c7"+
		"\u02c5\u0001\u0000\u0000\u0000\u02c8\u02cb\u0001\u0000\u0000\u0000\u02c9"+
		"\u02c7\u0001\u0000\u0000\u0000\u02c9\u02ca\u0001\u0000\u0000\u0000\u02ca"+
		"W\u0001\u0000\u0000\u0000\u02cb\u02c9\u0001\u0000\u0000\u0000\u02cc\u02d8"+
		"\u0005N\u0000\u0000\u02cd\u02cf\u0003Z-\u0000\u02ce\u02cd\u0001\u0000"+
		"\u0000\u0000\u02ce\u02cf\u0001\u0000\u0000\u0000\u02cf\u02d9\u0001\u0000"+
		"\u0000\u0000\u02d0\u02d3\u0003Z-\u0000\u02d1\u02d2\u0005U\u0000\u0000"+
		"\u02d2\u02d4\u0003\\.\u0000\u02d3\u02d1\u0001\u0000\u0000\u0000\u02d3"+
		"\u02d4\u0001\u0000\u0000\u0000\u02d4\u02d9\u0001\u0000\u0000\u0000\u02d5"+
		"\u02d7\u0003\\.\u0000\u02d6\u02d5\u0001\u0000\u0000\u0000\u02d6\u02d7"+
		"\u0001\u0000\u0000\u0000\u02d7\u02d9\u0001\u0000\u0000\u0000\u02d8\u02ce"+
		"\u0001\u0000\u0000\u0000\u02d8\u02d0\u0001\u0000\u0000\u0000\u02d8\u02d6"+
		"\u0001\u0000\u0000\u0000\u02d9\u02da\u0001\u0000\u0000\u0000\u02da\u02db"+
		"\u0005O\u0000\u0000\u02dbY\u0001\u0000\u0000\u0000\u02dc\u02e2\u0003\u00f0"+
		"x\u0000\u02dd\u02de\u0003\u00a2Q\u0000\u02de\u02df\u0005V\u0000\u0000"+
		"\u02df\u02e1\u0001\u0000\u0000\u0000\u02e0\u02dd\u0001\u0000\u0000\u0000"+
		"\u02e1\u02e4\u0001\u0000\u0000\u0000\u02e2\u02e0\u0001\u0000\u0000\u0000"+
		"\u02e2\u02e3\u0001\u0000\u0000\u0000\u02e3\u02e5\u0001\u0000\u0000\u0000"+
		"\u02e4\u02e2\u0001\u0000\u0000\u0000\u02e5\u02e6\u0005+\u0000\u0000\u02e6"+
		"[\u0001\u0000\u0000\u0000\u02e7\u02ec\u0003^/\u0000\u02e8\u02e9\u0005"+
		"U\u0000\u0000\u02e9\u02eb\u0003^/\u0000\u02ea\u02e8\u0001\u0000\u0000"+
		"\u0000\u02eb\u02ee\u0001\u0000\u0000\u0000\u02ec\u02ea\u0001\u0000\u0000"+
		"\u0000\u02ec\u02ed\u0001\u0000\u0000\u0000\u02ed\u02f1\u0001\u0000\u0000"+
		"\u0000\u02ee\u02ec\u0001\u0000\u0000\u0000\u02ef\u02f0\u0005U\u0000\u0000"+
		"\u02f0\u02f2\u0003`0\u0000\u02f1\u02ef\u0001\u0000\u0000\u0000\u02f1\u02f2"+
		"\u0001\u0000\u0000\u0000\u02f2\u02f5\u0001\u0000\u0000\u0000\u02f3\u02f5"+
		"\u0003`0\u0000\u02f4\u02e7\u0001\u0000\u0000\u0000\u02f4\u02f3\u0001\u0000"+
		"\u0000\u0000\u02f5]\u0001\u0000\u0000\u0000\u02f6\u02f8\u0003\f\u0006"+
		"\u0000\u02f7\u02f6\u0001\u0000\u0000\u0000\u02f8\u02fb\u0001\u0000\u0000"+
		"\u0000\u02f9\u02f7\u0001\u0000\u0000\u0000\u02f9\u02fa\u0001\u0000\u0000"+
		"\u0000\u02fa\u02fc\u0001\u0000\u0000\u0000\u02fb\u02f9\u0001\u0000\u0000"+
		"\u0000\u02fc\u02fd\u0003\u00f0x\u0000\u02fd\u02fe\u0003L&\u0000\u02fe"+
		"_\u0001\u0000\u0000\u0000\u02ff\u0301\u0003\f\u0006\u0000\u0300\u02ff"+
		"\u0001\u0000\u0000\u0000\u0301\u0304\u0001\u0000\u0000\u0000\u0302\u0300"+
		"\u0001\u0000\u0000\u0000\u0302\u0303\u0001\u0000\u0000\u0000\u0303\u0305"+
		"\u0001\u0000\u0000\u0000\u0304\u0302\u0001\u0000\u0000\u0000\u0305\u0309"+
		"\u0003\u00f0x\u0000\u0306\u0308\u0003p8\u0000\u0307\u0306\u0001\u0000"+
		"\u0000\u0000\u0308\u030b\u0001\u0000\u0000\u0000\u0309\u0307\u0001\u0000"+
		"\u0000\u0000\u0309\u030a\u0001\u0000\u0000\u0000\u030a\u030c\u0001\u0000"+
		"\u0000\u0000\u030b\u0309\u0001\u0000\u0000\u0000\u030c\u030d\u0005|\u0000"+
		"\u0000\u030d\u030e\u0003L&\u0000\u030ea\u0001\u0000\u0000\u0000\u030f"+
		"\u0314\u0003d2\u0000\u0310\u0311\u0005U\u0000\u0000\u0311\u0313\u0003"+
		"d2\u0000\u0312\u0310\u0001\u0000\u0000\u0000\u0313\u0316\u0001\u0000\u0000"+
		"\u0000\u0314\u0312\u0001\u0000\u0000\u0000\u0314\u0315\u0001\u0000\u0000"+
		"\u0000\u0315c\u0001\u0000\u0000\u0000\u0316\u0314\u0001\u0000\u0000\u0000"+
		"\u0317\u0319\u0003\f\u0006\u0000\u0318\u0317\u0001\u0000\u0000\u0000\u0319"+
		"\u031c\u0001\u0000\u0000\u0000\u031a\u0318\u0001\u0000\u0000\u0000\u031a"+
		"\u031b\u0001\u0000\u0000\u0000\u031b\u031d\u0001\u0000\u0000\u0000\u031c"+
		"\u031a\u0001\u0000\u0000\u0000\u031d\u031e\u0005=\u0000\u0000\u031e\u031f"+
		"\u0003\u00a2Q\u0000\u031fe\u0001\u0000\u0000\u0000\u0320\u0325\u0003\u00a2"+
		"Q\u0000\u0321\u0322\u0005V\u0000\u0000\u0322\u0324\u0003\u00a2Q\u0000"+
		"\u0323\u0321\u0001\u0000\u0000\u0000\u0324\u0327\u0001\u0000\u0000\u0000"+
		"\u0325\u0323\u0001\u0000\u0000\u0000\u0325\u0326\u0001\u0000\u0000\u0000"+
		"\u0326g\u0001\u0000\u0000\u0000\u0327\u0325\u0001\u0000\u0000\u0000\u0328"+
		"\u0330\u0003j5\u0000\u0329\u0330\u0003l6\u0000\u032a\u0330\u0005J\u0000"+
		"\u0000\u032b\u0330\u0005K\u0000\u0000\u032c\u0330\u0005I\u0000\u0000\u032d"+
		"\u0330\u0005M\u0000\u0000\u032e\u0330\u0005L\u0000\u0000\u032f\u0328\u0001"+
		"\u0000\u0000\u0000\u032f\u0329\u0001\u0000\u0000\u0000\u032f\u032a\u0001"+
		"\u0000\u0000\u0000\u032f\u032b\u0001\u0000\u0000\u0000\u032f\u032c\u0001"+
		"\u0000\u0000\u0000\u032f\u032d\u0001\u0000\u0000\u0000\u032f\u032e\u0001"+
		"\u0000\u0000\u0000\u0330i\u0001\u0000\u0000\u0000\u0331\u0332\u0007\u0001"+
		"\u0000\u0000\u0332k\u0001\u0000\u0000\u0000\u0333\u0334\u0007\u0002\u0000"+
		"\u0000\u0334m\u0001\u0000\u0000\u0000\u0335\u0336\u0003\u00a2Q\u0000\u0336"+
		"\u0337\u0005V\u0000\u0000\u0337\u0339\u0001\u0000\u0000\u0000\u0338\u0335"+
		"\u0001\u0000\u0000\u0000\u0339\u033c\u0001\u0000\u0000\u0000\u033a\u0338"+
		"\u0001\u0000\u0000\u0000\u033a\u033b\u0001\u0000\u0000\u0000\u033b\u033d"+
		"\u0001\u0000\u0000\u0000\u033c\u033a\u0001\u0000\u0000\u0000\u033d\u033e"+
		"\u0005{\u0000\u0000\u033e\u033f\u0003\u00a2Q\u0000\u033fo\u0001\u0000"+
		"\u0000\u0000\u0340\u0341\u0005{\u0000\u0000\u0341\u0344\u0003f3\u0000"+
		"\u0342\u0344\u0003n7\u0000\u0343\u0340\u0001\u0000\u0000\u0000\u0343\u0342"+
		"\u0001\u0000\u0000\u0000\u0344\u034b\u0001\u0000\u0000\u0000\u0345\u0348"+
		"\u0005N\u0000\u0000\u0346\u0349\u0003r9\u0000\u0347\u0349\u0003v;\u0000"+
		"\u0348\u0346\u0001\u0000\u0000\u0000\u0348\u0347\u0001\u0000\u0000\u0000"+
		"\u0348\u0349\u0001\u0000\u0000\u0000\u0349\u034a\u0001\u0000\u0000\u0000"+
		"\u034a\u034c\u0005O\u0000\u0000\u034b\u0345\u0001\u0000\u0000\u0000\u034b"+
		"\u034c\u0001\u0000\u0000\u0000\u034cq\u0001\u0000\u0000\u0000\u034d\u0352"+
		"\u0003t:\u0000\u034e\u034f\u0005U\u0000\u0000\u034f\u0351\u0003t:\u0000"+
		"\u0350\u034e\u0001\u0000\u0000\u0000\u0351\u0354\u0001\u0000\u0000\u0000"+
		"\u0352\u0350\u0001\u0000\u0000\u0000\u0352\u0353\u0001\u0000\u0000\u0000"+
		"\u0353s\u0001\u0000\u0000\u0000\u0354\u0352\u0001\u0000\u0000\u0000\u0355"+
		"\u0356\u0003\u00a2Q\u0000\u0356\u0357\u0005W\u0000\u0000\u0357\u0358\u0003"+
		"v;\u0000\u0358u\u0001\u0000\u0000\u0000\u0359\u035d\u0003\u00c6c\u0000"+
		"\u035a\u035d\u0003p8\u0000\u035b\u035d\u0003x<\u0000\u035c\u0359\u0001"+
		"\u0000\u0000\u0000\u035c\u035a\u0001\u0000\u0000\u0000\u035c\u035b\u0001"+
		"\u0000\u0000\u0000\u035dw\u0001\u0000\u0000\u0000\u035e\u0367\u0005P\u0000"+
		"\u0000\u035f\u0364\u0003v;\u0000\u0360\u0361\u0005U\u0000\u0000\u0361"+
		"\u0363\u0003v;\u0000\u0362\u0360\u0001\u0000\u0000\u0000\u0363\u0366\u0001"+
		"\u0000\u0000\u0000\u0364\u0362\u0001\u0000\u0000\u0000\u0364\u0365\u0001"+
		"\u0000\u0000\u0000\u0365\u0368\u0001\u0000\u0000\u0000\u0366\u0364\u0001"+
		"\u0000\u0000\u0000\u0367\u035f\u0001\u0000\u0000\u0000\u0367\u0368\u0001"+
		"\u0000\u0000\u0000\u0368\u036a\u0001\u0000\u0000\u0000\u0369\u036b\u0005"+
		"U\u0000\u0000\u036a\u0369\u0001\u0000\u0000\u0000\u036a\u036b\u0001\u0000"+
		"\u0000\u0000\u036b\u036c\u0001\u0000\u0000\u0000\u036c\u036d\u0005Q\u0000"+
		"\u0000\u036dy\u0001\u0000\u0000\u0000\u036e\u036f\u0005{\u0000\u0000\u036f"+
		"\u0370\u0005\u001c\u0000\u0000\u0370\u0371\u0003\u00a2Q\u0000\u0371\u0372"+
		"\u0003|>\u0000\u0372{\u0001\u0000\u0000\u0000\u0373\u0377\u0005P\u0000"+
		"\u0000\u0374\u0376\u0003~?\u0000\u0375\u0374\u0001\u0000\u0000\u0000\u0376"+
		"\u0379\u0001\u0000\u0000\u0000\u0377\u0375\u0001\u0000\u0000\u0000\u0377"+
		"\u0378\u0001\u0000\u0000\u0000\u0378\u037a\u0001\u0000\u0000\u0000\u0379"+
		"\u0377\u0001\u0000\u0000\u0000\u037a\u037b\u0005Q\u0000\u0000\u037b}\u0001"+
		"\u0000\u0000\u0000\u037c\u037e\u0003\b\u0004\u0000\u037d\u037c\u0001\u0000"+
		"\u0000\u0000\u037e\u0381\u0001\u0000\u0000\u0000\u037f\u037d\u0001\u0000"+
		"\u0000\u0000\u037f\u0380\u0001\u0000\u0000\u0000\u0380\u0382\u0001\u0000"+
		"\u0000\u0000\u0381\u037f\u0001\u0000\u0000\u0000\u0382\u0385\u0003\u0080"+
		"@\u0000\u0383\u0385\u0005T\u0000\u0000\u0384\u037f\u0001\u0000\u0000\u0000"+
		"\u0384\u0383\u0001\u0000\u0000\u0000\u0385\u007f\u0001\u0000\u0000\u0000"+
		"\u0386\u0387\u0003\u00f0x\u0000\u0387\u0388\u0003\u0082A\u0000\u0388\u0389"+
		"\u0005T\u0000\u0000\u0389\u039f\u0001\u0000\u0000\u0000\u038a\u038c\u0003"+
		"\u000e\u0007\u0000\u038b\u038d\u0005T\u0000\u0000\u038c\u038b\u0001\u0000"+
		"\u0000\u0000\u038c\u038d\u0001\u0000\u0000\u0000\u038d\u039f\u0001\u0000"+
		"\u0000\u0000\u038e\u0390\u0003\u001e\u000f\u0000\u038f\u0391\u0005T\u0000"+
		"\u0000\u0390\u038f\u0001\u0000\u0000\u0000\u0390\u0391\u0001\u0000\u0000"+
		"\u0000\u0391\u039f\u0001\u0000\u0000\u0000\u0392\u0394\u0003\u0016\u000b"+
		"\u0000\u0393\u0395\u0005T\u0000\u0000\u0394\u0393\u0001\u0000\u0000\u0000"+
		"\u0394\u0395\u0001\u0000\u0000\u0000\u0395\u039f\u0001\u0000\u0000\u0000"+
		"\u0396\u0398\u0003z=\u0000\u0397\u0399\u0005T\u0000\u0000\u0398\u0397"+
		"\u0001\u0000\u0000\u0000\u0398\u0399\u0001\u0000\u0000\u0000\u0399\u039f"+
		"\u0001\u0000\u0000\u0000\u039a\u039c\u0003\u0092I\u0000\u039b\u039d\u0005"+
		"T\u0000\u0000\u039c\u039b\u0001\u0000\u0000\u0000\u039c\u039d\u0001\u0000"+
		"\u0000\u0000\u039d\u039f\u0001\u0000\u0000\u0000\u039e\u0386\u0001\u0000"+
		"\u0000\u0000\u039e\u038a\u0001\u0000\u0000\u0000\u039e\u038e\u0001\u0000"+
		"\u0000\u0000\u039e\u0392\u0001\u0000\u0000\u0000\u039e\u0396\u0001\u0000"+
		"\u0000\u0000\u039e\u039a\u0001\u0000\u0000\u0000\u039f\u0081\u0001\u0000"+
		"\u0000\u0000\u03a0\u03a3\u0003\u0084B\u0000\u03a1\u03a3\u0003\u0086C\u0000"+
		"\u03a2\u03a0\u0001\u0000\u0000\u0000\u03a2\u03a1\u0001\u0000\u0000\u0000"+
		"\u03a3\u0083\u0001\u0000\u0000\u0000\u03a4\u03a5\u0003\u00a2Q\u0000\u03a5"+
		"\u03a6\u0005N\u0000\u0000\u03a6\u03a8\u0005O\u0000\u0000\u03a7\u03a9\u0003"+
		"\u0088D\u0000\u03a8\u03a7\u0001\u0000\u0000\u0000\u03a8\u03a9\u0001\u0000"+
		"\u0000\u0000\u03a9\u0085\u0001\u0000\u0000\u0000\u03aa\u03ab\u0003H$\u0000"+
		"\u03ab\u0087\u0001\u0000\u0000\u0000\u03ac\u03ad\u0005\f\u0000\u0000\u03ad"+
		"\u03ae\u0003v;\u0000\u03ae\u0089\u0001\u0000\u0000\u0000\u03af\u03b1\u0005"+
		"4\u0000\u0000\u03b0\u03af\u0001\u0000\u0000\u0000\u03b0\u03b1\u0001\u0000"+
		"\u0000\u0000\u03b1\u03b2\u0001\u0000\u0000\u0000\u03b2\u03b3\u00053\u0000"+
		"\u0000\u03b3\u03b4\u0003f3\u0000\u03b4\u03b5\u0003\u008cF\u0000\u03b5"+
		"\u008b\u0001\u0000\u0000\u0000\u03b6\u03ba\u0005P\u0000\u0000\u03b7\u03b9"+
		"\u0003\u008eG\u0000\u03b8\u03b7\u0001\u0000\u0000\u0000\u03b9\u03bc\u0001"+
		"\u0000\u0000\u0000\u03ba\u03b8\u0001\u0000\u0000\u0000\u03ba\u03bb\u0001"+
		"\u0000\u0000\u0000\u03bb\u03bd\u0001\u0000\u0000\u0000\u03bc\u03ba\u0001"+
		"\u0000\u0000\u0000\u03bd\u03be\u0005Q\u0000\u0000\u03be\u008d\u0001\u0000"+
		"\u0000\u0000\u03bf\u03c3\u00055\u0000\u0000\u03c0\u03c2\u0003\u0090H\u0000"+
		"\u03c1\u03c0\u0001\u0000\u0000\u0000\u03c2\u03c5\u0001\u0000\u0000\u0000"+
		"\u03c3\u03c1\u0001\u0000\u0000\u0000\u03c3\u03c4\u0001\u0000\u0000\u0000"+
		"\u03c4\u03c6\u0001\u0000\u0000\u0000\u03c5\u03c3\u0001\u0000\u0000\u0000"+
		"\u03c6\u03c7\u0003f3\u0000\u03c7\u03c8\u0005T\u0000\u0000\u03c8\u03e4"+
		"\u0001\u0000\u0000\u0000\u03c9\u03ca\u00056\u0000\u0000\u03ca\u03cd\u0003"+
		"f3\u0000\u03cb\u03cc\u00058\u0000\u0000\u03cc\u03ce\u0003f3\u0000\u03cd"+
		"\u03cb\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000\u03ce"+
		"\u03cf\u0001\u0000\u0000\u0000\u03cf\u03d0\u0005T\u0000\u0000\u03d0\u03e4"+
		"\u0001\u0000\u0000\u0000\u03d1\u03d2\u00057\u0000\u0000\u03d2\u03d5\u0003"+
		"f3\u0000\u03d3\u03d4\u00058\u0000\u0000\u03d4\u03d6\u0003f3\u0000\u03d5"+
		"\u03d3\u0001\u0000\u0000\u0000\u03d5\u03d6\u0001\u0000\u0000\u0000\u03d6"+
		"\u03d7\u0001\u0000\u0000\u0000\u03d7\u03d8\u0005T\u0000\u0000\u03d8\u03e4"+
		"\u0001\u0000\u0000\u0000\u03d9\u03da\u00059\u0000\u0000\u03da\u03db\u0003"+
		"f3\u0000\u03db\u03dc\u0005T\u0000\u0000\u03dc\u03e4\u0001\u0000\u0000"+
		"\u0000\u03dd\u03de\u0005:\u0000\u0000\u03de\u03df\u0003f3\u0000\u03df"+
		"\u03e0\u0005;\u0000\u0000\u03e0\u03e1\u0003f3\u0000\u03e1\u03e2\u0005"+
		"T\u0000\u0000\u03e2\u03e4\u0001\u0000\u0000\u0000\u03e3\u03bf\u0001\u0000"+
		"\u0000\u0000\u03e3\u03c9\u0001\u0000\u0000\u0000\u03e3\u03d1\u0001\u0000"+
		"\u0000\u0000\u03e3\u03d9\u0001\u0000\u0000\u0000\u03e3\u03dd\u0001\u0000"+
		"\u0000\u0000\u03e4\u008f\u0001\u0000\u0000\u0000\u03e5\u03e6\u0007\u0003"+
		"\u0000\u0000\u03e6\u0091\u0001\u0000\u0000\u0000\u03e7\u03e8\u0005?\u0000"+
		"\u0000\u03e8\u03ea\u0003\u00a2Q\u0000\u03e9\u03eb\u0003\u0010\b\u0000"+
		"\u03ea\u03e9\u0001\u0000\u0000\u0000\u03ea\u03eb\u0001\u0000\u0000\u0000"+
		"\u03eb\u03ec\u0001\u0000\u0000\u0000\u03ec\u03ef\u0003\u0094J\u0000\u03ed"+
		"\u03ee\u0005\u0018\u0000\u0000\u03ee\u03f0\u0003\u00eew\u0000\u03ef\u03ed"+
		"\u0001\u0000\u0000\u0000\u03ef\u03f0\u0001\u0000\u0000\u0000\u03f0\u03f1"+
		"\u0001\u0000\u0000\u0000\u03f1\u03f2\u0003\u009aM\u0000\u03f2\u0093\u0001"+
		"\u0000\u0000\u0000\u03f3\u03f5\u0005N\u0000\u0000\u03f4\u03f6\u0003\u0096"+
		"K\u0000\u03f5\u03f4\u0001\u0000\u0000\u0000\u03f5\u03f6\u0001\u0000\u0000"+
		"\u0000\u03f6\u03f7\u0001\u0000\u0000\u0000\u03f7\u03f8\u0005O\u0000\u0000"+
		"\u03f8\u0095\u0001\u0000\u0000\u0000\u03f9\u03fe\u0003\u0098L\u0000\u03fa"+
		"\u03fb\u0005U\u0000\u0000\u03fb\u03fd\u0003\u0098L\u0000\u03fc\u03fa\u0001"+
		"\u0000\u0000\u0000\u03fd\u0400\u0001\u0000\u0000\u0000\u03fe\u03fc\u0001"+
		"\u0000\u0000\u0000\u03fe\u03ff\u0001\u0000\u0000\u0000\u03ff\u0097\u0001"+
		"\u0000\u0000\u0000\u0400\u03fe\u0001\u0000\u0000\u0000\u0401\u0402\u0003"+
		"\u00f0x\u0000\u0402\u0403\u0003\u00a2Q\u0000\u0403\u0099\u0001\u0000\u0000"+
		"\u0000\u0404\u0409\u0005P\u0000\u0000\u0405\u0408\u0003$\u0012\u0000\u0406"+
		"\u0408\u00034\u001a\u0000\u0407\u0405\u0001\u0000\u0000\u0000\u0407\u0406"+
		"\u0001\u0000\u0000\u0000\u0408\u040b\u0001\u0000\u0000\u0000\u0409\u0407"+
		"\u0001\u0000\u0000\u0000\u0409\u040a\u0001\u0000\u0000\u0000\u040a\u040c"+
		"\u0001\u0000\u0000\u0000\u040b\u0409\u0001\u0000\u0000\u0000\u040c\u040d"+
		"\u0005Q\u0000\u0000\u040d\u009b\u0001\u0000\u0000\u0000\u040e\u0412\u0005"+
		"P\u0000\u0000\u040f\u0411\u0003\u009eO\u0000\u0410\u040f\u0001\u0000\u0000"+
		"\u0000\u0411\u0414\u0001\u0000\u0000\u0000\u0412\u0410\u0001\u0000\u0000"+
		"\u0000\u0412\u0413\u0001\u0000\u0000\u0000\u0413\u0415\u0001\u0000\u0000"+
		"\u0000\u0414\u0412\u0001\u0000\u0000\u0000\u0415\u0416\u0005Q\u0000\u0000"+
		"\u0416\u009d\u0001\u0000\u0000\u0000\u0417\u0418\u0003\u00a0P\u0000\u0418"+
		"\u0419\u0005T\u0000\u0000\u0419\u041d\u0001\u0000\u0000\u0000\u041a\u041d"+
		"\u0003\u00a6S\u0000\u041b\u041d\u0003\u00a8T\u0000\u041c\u0417\u0001\u0000"+
		"\u0000\u0000\u041c\u041a\u0001\u0000\u0000\u0000\u041c\u041b\u0001\u0000"+
		"\u0000\u0000\u041d\u009f\u0001\u0000\u0000\u0000\u041e\u0420\u0003\f\u0006"+
		"\u0000\u041f\u041e\u0001\u0000\u0000\u0000\u0420\u0423\u0001\u0000\u0000"+
		"\u0000\u0421\u041f\u0001\u0000\u0000\u0000\u0421\u0422\u0001\u0000\u0000"+
		"\u0000\u0422\u042c\u0001\u0000\u0000\u0000\u0423\u0421\u0001\u0000\u0000"+
		"\u0000\u0424\u0425\u0005=\u0000\u0000\u0425\u0426\u0003\u00a2Q\u0000\u0426"+
		"\u0427\u0005W\u0000\u0000\u0427\u0428\u0003\u00c6c\u0000\u0428\u042d\u0001"+
		"\u0000\u0000\u0000\u0429\u042a\u0003\u00f0x\u0000\u042a\u042b\u0003H$"+
		"\u0000\u042b\u042d\u0001\u0000\u0000\u0000\u042c\u0424\u0001\u0000\u0000"+
		"\u0000\u042c\u0429\u0001\u0000\u0000\u0000\u042d\u00a1\u0001\u0000\u0000"+
		"\u0000\u042e\u042f\u0007\u0004\u0000\u0000\u042f\u00a3\u0001\u0000\u0000"+
		"\u0000\u0430\u0431\u0007\u0005\u0000\u0000\u0431\u00a5\u0001\u0000\u0000"+
		"\u0000\u0432\u0434\u0003\n\u0005\u0000\u0433\u0432\u0001\u0000\u0000\u0000"+
		"\u0434\u0437\u0001\u0000\u0000\u0000\u0435\u0433\u0001\u0000\u0000\u0000"+
		"\u0435\u0436\u0001\u0000\u0000\u0000\u0436\u043b\u0001\u0000\u0000\u0000"+
		"\u0437\u0435\u0001\u0000\u0000\u0000\u0438\u043c\u0003\u000e\u0007\u0000"+
		"\u0439\u043c\u0003\u001e\u000f\u0000\u043a\u043c\u0003\u0092I\u0000\u043b"+
		"\u0438\u0001\u0000\u0000\u0000\u043b\u0439\u0001\u0000\u0000\u0000\u043b"+
		"\u043a\u0001\u0000\u0000\u0000\u043c\u00a7\u0001\u0000\u0000\u0000\u043d"+
		"\u04af\u0003\u009cN\u0000\u043e\u043f\u0005\u0002\u0000\u0000\u043f\u0442"+
		"\u0003\u00c6c\u0000\u0440\u0441\u0005]\u0000\u0000\u0441\u0443\u0003\u00c6"+
		"c\u0000\u0442\u0440\u0001\u0000\u0000\u0000\u0442\u0443\u0001\u0000\u0000"+
		"\u0000\u0443\u0444\u0001\u0000\u0000\u0000\u0444\u0445\u0005T\u0000\u0000"+
		"\u0445\u04af\u0001\u0000\u0000\u0000\u0446\u0447\u0005\u0016\u0000\u0000"+
		"\u0447\u0448\u0003\u00c0`\u0000\u0448\u044b\u0003\u00a8T\u0000\u0449\u044a"+
		"\u0005\u000f\u0000\u0000\u044a\u044c\u0003\u00a8T\u0000\u044b\u0449\u0001"+
		"\u0000\u0000\u0000\u044b\u044c\u0001\u0000\u0000\u0000\u044c\u04af\u0001"+
		"\u0000\u0000\u0000\u044d\u044e\u0005\u0015\u0000\u0000\u044e\u044f\u0005"+
		"N\u0000\u0000\u044f\u0450\u0003\u00ba]\u0000\u0450\u0451\u0005O\u0000"+
		"\u0000\u0451\u0452\u0003\u00a8T\u0000\u0452\u04af\u0001\u0000\u0000\u0000"+
		"\u0453\u0454\u00052\u0000\u0000\u0454\u0455\u0003\u00c0`\u0000\u0455\u0456"+
		"\u0003\u00a8T\u0000\u0456\u04af\u0001\u0000\u0000\u0000\u0457\u0458\u0005"+
		"\r\u0000\u0000\u0458\u0459\u0003\u00a8T\u0000\u0459\u045a\u00052\u0000"+
		"\u0000\u045a\u045b\u0003\u00c0`\u0000\u045b\u045c\u0005T\u0000\u0000\u045c"+
		"\u04af\u0001\u0000\u0000\u0000\u045d\u045e\u0005/\u0000\u0000\u045e\u0468"+
		"\u0003\u009cN\u0000\u045f\u0461\u0003\u00aaU\u0000\u0460\u045f\u0001\u0000"+
		"\u0000\u0000\u0461\u0462\u0001\u0000\u0000\u0000\u0462\u0460\u0001\u0000"+
		"\u0000\u0000\u0462\u0463\u0001\u0000\u0000\u0000\u0463\u0465\u0001\u0000"+
		"\u0000\u0000\u0464\u0466\u0003\u00aeW\u0000\u0465\u0464\u0001\u0000\u0000"+
		"\u0000\u0465\u0466\u0001\u0000\u0000\u0000\u0466\u0469\u0001\u0000\u0000"+
		"\u0000\u0467\u0469\u0003\u00aeW\u0000\u0468\u0460\u0001\u0000\u0000\u0000"+
		"\u0468\u0467\u0001\u0000\u0000\u0000\u0469\u04af\u0001\u0000\u0000\u0000"+
		"\u046a\u046b\u0005/\u0000\u0000\u046b\u046c\u0003\u00b0X\u0000\u046c\u0470"+
		"\u0003\u009cN\u0000\u046d\u046f\u0003\u00aaU\u0000\u046e\u046d\u0001\u0000"+
		"\u0000\u0000\u046f\u0472\u0001\u0000\u0000\u0000\u0470\u046e\u0001\u0000"+
		"\u0000\u0000\u0470\u0471\u0001\u0000\u0000\u0000\u0471\u0474\u0001\u0000"+
		"\u0000\u0000\u0472\u0470\u0001\u0000\u0000\u0000\u0473\u0475\u0003\u00ae"+
		"W\u0000\u0474\u0473\u0001\u0000\u0000\u0000\u0474\u0475\u0001\u0000\u0000"+
		"\u0000\u0475\u04af\u0001\u0000\u0000\u0000\u0476\u0477\u0005)\u0000\u0000"+
		"\u0477\u0478\u0003\u00c0`\u0000\u0478\u047c\u0005P\u0000\u0000\u0479\u047b"+
		"\u0003\u00b6[\u0000\u047a\u0479\u0001\u0000\u0000\u0000\u047b\u047e\u0001"+
		"\u0000\u0000\u0000\u047c\u047a\u0001\u0000\u0000\u0000\u047c\u047d\u0001"+
		"\u0000\u0000\u0000\u047d\u0482\u0001\u0000\u0000\u0000\u047e\u047c\u0001"+
		"\u0000\u0000\u0000\u047f\u0481\u0003\u00b8\\\u0000\u0480\u047f\u0001\u0000"+
		"\u0000\u0000\u0481\u0484\u0001\u0000\u0000\u0000\u0482\u0480\u0001\u0000"+
		"\u0000\u0000\u0482\u0483\u0001\u0000\u0000\u0000\u0483\u0485\u0001\u0000"+
		"\u0000\u0000\u0484\u0482\u0001\u0000\u0000\u0000\u0485\u0486\u0005Q\u0000"+
		"\u0000\u0486\u04af\u0001\u0000\u0000\u0000\u0487\u0488\u0005*\u0000\u0000"+
		"\u0488\u0489\u0003\u00c0`\u0000\u0489\u048a\u0003\u009cN\u0000\u048a\u04af"+
		"\u0001\u0000\u0000\u0000\u048b\u048d\u0005$\u0000\u0000\u048c\u048e\u0003"+
		"\u00c6c\u0000\u048d\u048c\u0001\u0000\u0000\u0000\u048d\u048e\u0001\u0000"+
		"\u0000\u0000\u048e\u048f\u0001\u0000\u0000\u0000\u048f\u04af\u0005T\u0000"+
		"\u0000\u0490\u0491\u0005,\u0000\u0000\u0491\u0492\u0003\u00c6c\u0000\u0492"+
		"\u0493\u0005T\u0000\u0000\u0493\u04af\u0001\u0000\u0000\u0000\u0494\u0496"+
		"\u0005\u0004\u0000\u0000\u0495\u0497\u0003\u00a2Q\u0000\u0496\u0495\u0001"+
		"\u0000\u0000\u0000\u0496\u0497\u0001\u0000\u0000\u0000\u0497\u0498\u0001"+
		"\u0000\u0000\u0000\u0498\u04af\u0005T\u0000\u0000\u0499\u049b\u0005\u000b"+
		"\u0000\u0000\u049a\u049c\u0003\u00a2Q\u0000\u049b\u049a\u0001\u0000\u0000"+
		"\u0000\u049b\u049c\u0001\u0000\u0000\u0000\u049c\u049d\u0001\u0000\u0000"+
		"\u0000\u049d\u04af\u0005T\u0000\u0000\u049e\u049f\u0005>\u0000\u0000\u049f"+
		"\u04a0\u0003\u00c6c\u0000\u04a0\u04a1\u0005T\u0000\u0000\u04a1\u04af\u0001"+
		"\u0000\u0000\u0000\u04a2\u04af\u0005T\u0000\u0000\u04a3\u04a4\u0003\u00c6"+
		"c\u0000\u04a4\u04a5\u0005T\u0000\u0000\u04a5\u04af\u0001\u0000\u0000\u0000"+
		"\u04a6\u04a8\u0003\u00d2i\u0000\u04a7\u04a9\u0005T\u0000\u0000\u04a8\u04a7"+
		"\u0001\u0000\u0000\u0000\u04a8\u04a9\u0001\u0000\u0000\u0000\u04a9\u04af"+
		"\u0001\u0000\u0000\u0000\u04aa\u04ab\u0003\u00a2Q\u0000\u04ab\u04ac\u0005"+
		"]\u0000\u0000\u04ac\u04ad\u0003\u00a8T\u0000\u04ad\u04af\u0001\u0000\u0000"+
		"\u0000\u04ae\u043d\u0001\u0000\u0000\u0000\u04ae\u043e\u0001\u0000\u0000"+
		"\u0000\u04ae\u0446\u0001\u0000\u0000\u0000\u04ae\u044d\u0001\u0000\u0000"+
		"\u0000\u04ae\u0453\u0001\u0000\u0000\u0000\u04ae\u0457\u0001\u0000\u0000"+
		"\u0000\u04ae\u045d\u0001\u0000\u0000\u0000\u04ae\u046a\u0001\u0000\u0000"+
		"\u0000\u04ae\u0476\u0001\u0000\u0000\u0000\u04ae\u0487\u0001\u0000\u0000"+
		"\u0000\u04ae\u048b\u0001\u0000\u0000\u0000\u04ae\u0490\u0001\u0000\u0000"+
		"\u0000\u04ae\u0494\u0001\u0000\u0000\u0000\u04ae\u0499\u0001\u0000\u0000"+
		"\u0000\u04ae\u049e\u0001\u0000\u0000\u0000\u04ae\u04a2\u0001\u0000\u0000"+
		"\u0000\u04ae\u04a3\u0001\u0000\u0000\u0000\u04ae\u04a6\u0001\u0000\u0000"+
		"\u0000\u04ae\u04aa\u0001\u0000\u0000\u0000\u04af\u00a9\u0001\u0000\u0000"+
		"\u0000\u04b0\u04b1\u0005\u0007\u0000\u0000\u04b1\u04b5\u0005N\u0000\u0000"+
		"\u04b2\u04b4\u0003\f\u0006\u0000\u04b3\u04b2\u0001\u0000\u0000\u0000\u04b4"+
		"\u04b7\u0001\u0000\u0000\u0000\u04b5\u04b3\u0001\u0000\u0000\u0000\u04b5"+
		"\u04b6\u0001\u0000\u0000\u0000\u04b6\u04b8\u0001\u0000\u0000\u0000\u04b7"+
		"\u04b5\u0001\u0000\u0000\u0000\u04b8\u04b9\u0003\u00acV\u0000\u04b9\u04ba"+
		"\u0003\u00a2Q\u0000\u04ba\u04bb\u0005O\u0000\u0000\u04bb\u04bc\u0003\u009c"+
		"N\u0000\u04bc\u00ab\u0001\u0000\u0000\u0000\u04bd\u04c2\u0003f3\u0000"+
		"\u04be\u04bf\u0005k\u0000\u0000\u04bf\u04c1\u0003f3\u0000\u04c0\u04be"+
		"\u0001\u0000\u0000\u0000\u04c1\u04c4\u0001\u0000\u0000\u0000\u04c2\u04c0"+
		"\u0001\u0000\u0000\u0000\u04c2\u04c3\u0001\u0000\u0000\u0000\u04c3\u00ad"+
		"\u0001\u0000\u0000\u0000\u04c4\u04c2\u0001\u0000\u0000\u0000\u04c5\u04c6"+
		"\u0005\u0013\u0000\u0000\u04c6\u04c7\u0003\u009cN\u0000\u04c7\u00af\u0001"+
		"\u0000\u0000\u0000\u04c8\u04c9\u0005N\u0000\u0000\u04c9\u04cb\u0003\u00b2"+
		"Y\u0000\u04ca\u04cc\u0005T\u0000\u0000\u04cb\u04ca\u0001\u0000\u0000\u0000"+
		"\u04cb\u04cc\u0001\u0000\u0000\u0000\u04cc\u04cd\u0001\u0000\u0000\u0000"+
		"\u04cd\u04ce\u0005O\u0000\u0000\u04ce\u00b1\u0001\u0000\u0000\u0000\u04cf"+
		"\u04d4\u0003\u00b4Z\u0000\u04d0\u04d1\u0005T\u0000\u0000\u04d1\u04d3\u0003"+
		"\u00b4Z\u0000\u04d2\u04d0\u0001\u0000\u0000\u0000\u04d3\u04d6\u0001\u0000"+
		"\u0000\u0000\u04d4\u04d2\u0001\u0000\u0000\u0000\u04d4\u04d5\u0001\u0000"+
		"\u0000\u0000\u04d5\u00b3\u0001\u0000\u0000\u0000\u04d6\u04d4\u0001\u0000"+
		"\u0000\u0000\u04d7\u04d9\u0003\f\u0006\u0000\u04d8\u04d7\u0001\u0000\u0000"+
		"\u0000\u04d9\u04dc\u0001\u0000\u0000\u0000\u04da\u04d8\u0001\u0000\u0000"+
		"\u0000\u04da\u04db\u0001\u0000\u0000\u0000\u04db\u04e2\u0001\u0000\u0000"+
		"\u0000\u04dc\u04da\u0001\u0000\u0000\u0000\u04dd\u04de\u0003R)\u0000\u04de"+
		"\u04df\u0003L&\u0000\u04df\u04e3\u0001\u0000\u0000\u0000\u04e0\u04e1\u0005"+
		"=\u0000\u0000\u04e1\u04e3\u0003\u00a2Q\u0000\u04e2\u04dd\u0001\u0000\u0000"+
		"\u0000\u04e2\u04e0\u0001\u0000\u0000\u0000\u04e3\u04e4\u0001\u0000\u0000"+
		"\u0000\u04e4\u04e5\u0005W\u0000\u0000\u04e5\u04e6\u0003\u00c6c\u0000\u04e6"+
		"\u04e9\u0001\u0000\u0000\u0000\u04e7\u04e9\u0003\u00a2Q\u0000\u04e8\u04da"+
		"\u0001\u0000\u0000\u0000\u04e8\u04e7\u0001\u0000\u0000\u0000\u04e9\u00b5"+
		"\u0001\u0000\u0000\u0000\u04ea\u04ec\u0003\u00b8\\\u0000\u04eb\u04ea\u0001"+
		"\u0000\u0000\u0000\u04ec\u04ed\u0001\u0000\u0000\u0000\u04ed\u04eb\u0001"+
		"\u0000\u0000\u0000\u04ed\u04ee\u0001\u0000\u0000\u0000\u04ee\u04f0\u0001"+
		"\u0000\u0000\u0000\u04ef\u04f1\u0003\u009eO\u0000\u04f0\u04ef\u0001\u0000"+
		"\u0000\u0000\u04f1\u04f2\u0001\u0000\u0000\u0000\u04f2\u04f0\u0001\u0000"+
		"\u0000\u0000\u04f2\u04f3\u0001\u0000\u0000\u0000\u04f3\u00b7\u0001\u0000"+
		"\u0000\u0000\u04f4\u04fa\u0005\u0006\u0000\u0000\u04f5\u04fb\u0003\u00c6"+
		"c\u0000\u04f6\u04fb\u0005\u0081\u0000\u0000\u04f7\u04f8\u0003\u00f0x\u0000"+
		"\u04f8\u04f9\u0003\u00a2Q\u0000\u04f9\u04fb\u0001\u0000\u0000\u0000\u04fa"+
		"\u04f5\u0001\u0000\u0000\u0000\u04fa\u04f6\u0001\u0000\u0000\u0000\u04fa"+
		"\u04f7\u0001\u0000\u0000\u0000\u04fb\u04fc\u0001\u0000\u0000\u0000\u04fc"+
		"\u0500\u0005]\u0000\u0000\u04fd\u04fe\u0005\f\u0000\u0000\u04fe\u0500"+
		"\u0005]\u0000\u0000\u04ff\u04f4\u0001\u0000\u0000\u0000\u04ff\u04fd\u0001"+
		"\u0000\u0000\u0000\u0500\u00b9\u0001\u0000\u0000\u0000\u0501\u050e\u0003"+
		"\u00be_\u0000\u0502\u0504\u0003\u00bc^\u0000\u0503\u0502\u0001\u0000\u0000"+
		"\u0000\u0503\u0504\u0001\u0000\u0000\u0000\u0504\u0505\u0001\u0000\u0000"+
		"\u0000\u0505\u0507\u0005T\u0000\u0000\u0506\u0508\u0003\u00c6c\u0000\u0507"+
		"\u0506\u0001\u0000\u0000\u0000\u0507\u0508\u0001\u0000\u0000\u0000\u0508"+
		"\u0509\u0001\u0000\u0000\u0000\u0509\u050b\u0005T\u0000\u0000\u050a\u050c"+
		"\u0003\u00c2a\u0000\u050b\u050a\u0001\u0000\u0000\u0000\u050b\u050c\u0001"+
		"\u0000\u0000\u0000\u050c\u050e\u0001\u0000\u0000\u0000\u050d\u0501\u0001"+
		"\u0000\u0000\u0000\u050d\u0503\u0001\u0000\u0000\u0000\u050e\u00bb\u0001"+
		"\u0000\u0000\u0000\u050f\u0512\u0003\u00a0P\u0000\u0510\u0512\u0003\u00c2"+
		"a\u0000\u0511\u050f\u0001\u0000\u0000\u0000\u0511\u0510\u0001\u0000\u0000"+
		"\u0000\u0512\u00bd\u0001\u0000\u0000\u0000\u0513\u0515\u0003\f\u0006\u0000"+
		"\u0514\u0513\u0001\u0000\u0000\u0000\u0515\u0518\u0001\u0000\u0000\u0000"+
		"\u0516\u0514\u0001\u0000\u0000\u0000\u0516\u0517\u0001\u0000\u0000\u0000"+
		"\u0517\u051b\u0001\u0000\u0000\u0000\u0518\u0516\u0001\u0000\u0000\u0000"+
		"\u0519\u051c\u0003\u00f0x\u0000\u051a\u051c\u0005=\u0000\u0000\u051b\u0519"+
		"\u0001\u0000\u0000\u0000\u051b\u051a\u0001\u0000\u0000\u0000\u051c\u051d"+
		"\u0001\u0000\u0000\u0000\u051d\u051e\u0003L&\u0000\u051e\u051f\u0005]"+
		"\u0000\u0000\u051f\u0520\u0003\u00c6c\u0000\u0520\u00bf\u0001\u0000\u0000"+
		"\u0000\u0521\u0522\u0005N\u0000\u0000\u0522\u0523\u0003\u00c6c\u0000\u0523"+
		"\u0524\u0005O\u0000\u0000\u0524\u00c1\u0001\u0000\u0000\u0000\u0525\u052a"+
		"\u0003\u00c6c\u0000\u0526\u0527\u0005U\u0000\u0000\u0527\u0529\u0003\u00c6"+
		"c\u0000\u0528\u0526\u0001\u0000\u0000\u0000\u0529\u052c\u0001\u0000\u0000"+
		"\u0000\u052a\u0528\u0001\u0000\u0000\u0000\u052a\u052b\u0001\u0000\u0000"+
		"\u0000\u052b\u00c3\u0001\u0000\u0000\u0000\u052c\u052a\u0001\u0000\u0000"+
		"\u0000\u052d\u052e\u0003\u00a2Q\u0000\u052e\u0530\u0005N\u0000\u0000\u052f"+
		"\u0531\u0003\u00c2a\u0000\u0530\u052f\u0001\u0000\u0000\u0000\u0530\u0531"+
		"\u0001\u0000\u0000\u0000\u0531\u0532\u0001\u0000\u0000\u0000\u0532\u0533"+
		"\u0005O\u0000\u0000\u0533\u0541\u0001\u0000\u0000\u0000\u0534\u0535\u0005"+
		"+\u0000\u0000\u0535\u0537\u0005N\u0000\u0000\u0536\u0538\u0003\u00c2a"+
		"\u0000\u0537\u0536\u0001\u0000\u0000\u0000\u0537\u0538\u0001\u0000\u0000"+
		"\u0000\u0538\u0539\u0001\u0000\u0000\u0000\u0539\u0541\u0005O\u0000\u0000"+
		"\u053a\u053b\u0005(\u0000\u0000\u053b\u053d\u0005N\u0000\u0000\u053c\u053e"+
		"\u0003\u00c2a\u0000\u053d\u053c\u0001\u0000\u0000\u0000\u053d\u053e\u0001"+
		"\u0000\u0000\u0000\u053e\u053f\u0001\u0000\u0000\u0000\u053f\u0541\u0005"+
		"O\u0000\u0000\u0540\u052d\u0001\u0000\u0000\u0000\u0540\u0534\u0001\u0000"+
		"\u0000\u0000\u0540\u053a\u0001\u0000\u0000\u0000\u0541\u00c5\u0001\u0000"+
		"\u0000\u0000\u0542\u0543\u0006c\uffff\uffff\u0000\u0543\u0570\u0003\u00d0"+
		"h\u0000\u0544\u0570\u0003\u00c4b\u0000\u0545\u0546\u0005\u001f\u0000\u0000"+
		"\u0546\u0570\u0003\u00dcn\u0000\u0547\u054b\u0005N\u0000\u0000\u0548\u054a"+
		"\u0003p8\u0000\u0549\u0548\u0001\u0000\u0000\u0000\u054a\u054d\u0001\u0000"+
		"\u0000\u0000\u054b\u0549\u0001\u0000\u0000\u0000\u054b\u054c\u0001\u0000"+
		"\u0000\u0000\u054c\u054e\u0001\u0000\u0000\u0000\u054d\u054b\u0001\u0000"+
		"\u0000\u0000\u054e\u0553\u0003\u00f0x\u0000\u054f\u0550\u0005j\u0000\u0000"+
		"\u0550\u0552\u0003\u00f0x\u0000\u0551\u054f\u0001\u0000\u0000\u0000\u0552"+
		"\u0555\u0001\u0000\u0000\u0000\u0553\u0551\u0001\u0000\u0000\u0000\u0553"+
		"\u0554\u0001\u0000\u0000\u0000\u0554\u0556\u0001\u0000\u0000\u0000\u0555"+
		"\u0553\u0001\u0000\u0000\u0000\u0556\u0557\u0005O\u0000\u0000\u0557\u0558"+
		"\u0003\u00c6c\u0016\u0558\u0570\u0001\u0000\u0000\u0000\u0559\u055a\u0007"+
		"\u0006\u0000\u0000\u055a\u0570\u0003\u00c6c\u0014\u055b\u055c\u0007\u0007"+
		"\u0000\u0000\u055c\u0570\u0003\u00c6c\u0013\u055d\u0570\u0003\u00cae\u0000"+
		"\u055e\u0570\u0003\u00d2i\u0000\u055f\u0560\u0003\u00f0x\u0000\u0560\u0566"+
		"\u0005z\u0000\u0000\u0561\u0563\u0003\u00f4z\u0000\u0562\u0561\u0001\u0000"+
		"\u0000\u0000\u0562\u0563\u0001\u0000\u0000\u0000\u0563\u0564\u0001\u0000"+
		"\u0000\u0000\u0564\u0567\u0003\u00a2Q\u0000\u0565\u0567\u0005\u001f\u0000"+
		"\u0000\u0566\u0562\u0001\u0000\u0000\u0000\u0566\u0565\u0001\u0000\u0000"+
		"\u0000\u0567\u0570\u0001\u0000\u0000\u0000\u0568\u0569\u0003\u00dam\u0000"+
		"\u0569\u056b\u0005z\u0000\u0000\u056a\u056c\u0003\u00f4z\u0000\u056b\u056a"+
		"\u0001\u0000\u0000\u0000\u056b\u056c\u0001\u0000\u0000\u0000\u056c\u056d"+
		"\u0001\u0000\u0000\u0000\u056d\u056e\u0005\u001f\u0000\u0000\u056e\u0570"+
		"\u0001\u0000\u0000\u0000\u056f\u0542\u0001\u0000\u0000\u0000\u056f\u0544"+
		"\u0001\u0000\u0000\u0000\u056f\u0545\u0001\u0000\u0000\u0000\u056f\u0547"+
		"\u0001\u0000\u0000\u0000\u056f\u0559\u0001\u0000\u0000\u0000\u056f\u055b"+
		"\u0001\u0000\u0000\u0000\u056f\u055d\u0001\u0000\u0000\u0000\u056f\u055e"+
		"\u0001\u0000\u0000\u0000\u056f\u055f\u0001\u0000\u0000\u0000\u056f\u0568"+
		"\u0001\u0000\u0000\u0000\u0570\u05c4\u0001\u0000\u0000\u0000\u0571\u0572"+
		"\n\u0012\u0000\u0000\u0572\u0573\u0007\b\u0000\u0000\u0573\u05c3\u0003"+
		"\u00c6c\u0013\u0574\u0575\n\u0011\u0000\u0000\u0575\u0576\u0007\t\u0000"+
		"\u0000\u0576\u05c3\u0003\u00c6c\u0012\u0577\u057f\n\u0010\u0000\u0000"+
		"\u0578\u0579\u0005Y\u0000\u0000\u0579\u0580\u0005Y\u0000\u0000\u057a\u057b"+
		"\u0005X\u0000\u0000\u057b\u057c\u0005X\u0000\u0000\u057c\u0580\u0005X"+
		"\u0000\u0000\u057d\u057e\u0005X\u0000\u0000\u057e\u0580\u0005X\u0000\u0000"+
		"\u057f\u0578\u0001\u0000\u0000\u0000\u057f\u057a\u0001\u0000\u0000\u0000"+
		"\u057f\u057d\u0001\u0000\u0000\u0000\u0580\u0581\u0001\u0000\u0000\u0000"+
		"\u0581\u05c3\u0003\u00c6c\u0011\u0582\u0583\n\u000f\u0000\u0000\u0583"+
		"\u0584\u0007\n\u0000\u0000\u0584\u05c3\u0003\u00c6c\u0010\u0585\u0586"+
		"\n\r\u0000\u0000\u0586\u0587\u0007\u000b\u0000\u0000\u0587\u05c3\u0003"+
		"\u00c6c\u000e\u0588\u0589\n\f\u0000\u0000\u0589\u058a\u0005j\u0000\u0000"+
		"\u058a\u05c3\u0003\u00c6c\r\u058b\u058c\n\u000b\u0000\u0000\u058c\u058d"+
		"\u0005l\u0000\u0000\u058d\u05c3\u0003\u00c6c\f\u058e\u058f\n\n\u0000\u0000"+
		"\u058f\u0590\u0005k\u0000\u0000\u0590\u05c3\u0003\u00c6c\u000b\u0591\u0592"+
		"\n\t\u0000\u0000\u0592\u0593\u0005b\u0000\u0000\u0593\u05c3\u0003\u00c6"+
		"c\n\u0594\u0595\n\b\u0000\u0000\u0595\u0596\u0005c\u0000\u0000\u0596\u05c3"+
		"\u0003\u00c6c\t\u0597\u0598\n\u0007\u0000\u0000\u0598\u0599\u0005\\\u0000"+
		"\u0000\u0599\u059a\u0003\u00c6c\u0000\u059a\u059b\u0005]\u0000\u0000\u059b"+
		"\u059c\u0003\u00c6c\u0007\u059c\u05c3\u0001\u0000\u0000\u0000\u059d\u059e"+
		"\n\u0006\u0000\u0000\u059e\u059f\u0007\f\u0000\u0000\u059f\u05c3\u0003"+
		"\u00c6c\u0006\u05a0\u05a1\n\u001a\u0000\u0000\u05a1\u05ad\u0005V\u0000"+
		"\u0000\u05a2\u05ae\u0003\u00a2Q\u0000\u05a3\u05ae\u0003\u00c4b\u0000\u05a4"+
		"\u05ae\u0005+\u0000\u0000\u05a5\u05a7\u0005\u001f\u0000\u0000\u05a6\u05a8"+
		"\u0003\u00ecv\u0000\u05a7\u05a6\u0001\u0000\u0000\u0000\u05a7\u05a8\u0001"+
		"\u0000\u0000\u0000\u05a8\u05a9\u0001\u0000\u0000\u0000\u05a9\u05ae\u0003"+
		"\u00e0p\u0000\u05aa\u05ab\u0005(\u0000\u0000\u05ab\u05ae\u0003\u00f6{"+
		"\u0000\u05ac\u05ae\u0003\u00e6s\u0000\u05ad\u05a2\u0001\u0000\u0000\u0000"+
		"\u05ad\u05a3\u0001\u0000\u0000\u0000\u05ad\u05a4\u0001\u0000\u0000\u0000"+
		"\u05ad\u05a5\u0001\u0000\u0000\u0000\u05ad\u05aa\u0001\u0000\u0000\u0000"+
		"\u05ad\u05ac\u0001\u0000\u0000\u0000\u05ae\u05c3\u0001\u0000\u0000\u0000"+
		"\u05af\u05b0\n\u0019\u0000\u0000\u05b0\u05b1\u0005R\u0000\u0000\u05b1"+
		"\u05b2\u0003\u00c6c\u0000\u05b2\u05b3\u0005S\u0000\u0000\u05b3\u05c3\u0001"+
		"\u0000\u0000\u0000\u05b4\u05b5\n\u0015\u0000\u0000\u05b5\u05c3\u0007\r"+
		"\u0000\u0000\u05b6\u05b7\n\u000e\u0000\u0000\u05b7\u05ba\u0005\u001a\u0000"+
		"\u0000\u05b8\u05bb\u0003\u00f0x\u0000\u05b9\u05bb\u0003\u00c8d\u0000\u05ba"+
		"\u05b8\u0001\u0000\u0000\u0000\u05ba\u05b9\u0001\u0000\u0000\u0000\u05bb"+
		"\u05c3\u0001\u0000\u0000\u0000\u05bc\u05bd\n\u0003\u0000\u0000\u05bd\u05bf"+
		"\u0005z\u0000\u0000\u05be\u05c0\u0003\u00f4z\u0000\u05bf\u05be\u0001\u0000"+
		"\u0000\u0000\u05bf\u05c0\u0001\u0000\u0000\u0000\u05c0\u05c1\u0001\u0000"+
		"\u0000\u0000\u05c1\u05c3\u0003\u00a2Q\u0000\u05c2\u0571\u0001\u0000\u0000"+
		"\u0000\u05c2\u0574\u0001\u0000\u0000\u0000\u05c2\u0577\u0001\u0000\u0000"+
		"\u0000\u05c2\u0582\u0001\u0000\u0000\u0000\u05c2\u0585\u0001\u0000\u0000"+
		"\u0000\u05c2\u0588\u0001\u0000\u0000\u0000\u05c2\u058b\u0001\u0000\u0000"+
		"\u0000\u05c2\u058e\u0001\u0000\u0000\u0000\u05c2\u0591\u0001\u0000\u0000"+
		"\u0000\u05c2\u0594\u0001\u0000\u0000\u0000\u05c2\u0597\u0001\u0000\u0000"+
		"\u0000\u05c2\u059d\u0001\u0000\u0000\u0000\u05c2\u05a0\u0001\u0000\u0000"+
		"\u0000\u05c2\u05af\u0001\u0000\u0000\u0000\u05c2\u05b4\u0001\u0000\u0000"+
		"\u0000\u05c2\u05b6\u0001\u0000\u0000\u0000\u05c2\u05bc\u0001\u0000\u0000"+
		"\u0000\u05c3\u05c6\u0001\u0000\u0000\u0000\u05c4\u05c2\u0001\u0000\u0000"+
		"\u0000\u05c4\u05c5\u0001\u0000\u0000\u0000\u05c5\u00c7\u0001\u0000\u0000"+
		"\u0000\u05c6\u05c4\u0001\u0000\u0000\u0000\u05c7\u05c9\u0003\f\u0006\u0000"+
		"\u05c8\u05c7\u0001\u0000\u0000\u0000\u05c9\u05cc\u0001\u0000\u0000\u0000"+
		"\u05ca\u05c8\u0001\u0000\u0000\u0000\u05ca\u05cb\u0001\u0000\u0000\u0000"+
		"\u05cb\u05cd\u0001\u0000\u0000\u0000\u05cc\u05ca\u0001\u0000\u0000\u0000"+
		"\u05cd\u05d1\u0003\u00f0x\u0000\u05ce\u05d0\u0003p8\u0000\u05cf\u05ce"+
		"\u0001\u0000\u0000\u0000\u05d0\u05d3\u0001\u0000\u0000\u0000\u05d1\u05cf"+
		"\u0001\u0000\u0000\u0000\u05d1\u05d2\u0001\u0000\u0000\u0000\u05d2\u05d4"+
		"\u0001\u0000\u0000\u0000\u05d3\u05d1\u0001\u0000\u0000\u0000\u05d4\u05d5"+
		"\u0003\u00a2Q\u0000\u05d5\u00c9\u0001\u0000\u0000\u0000\u05d6\u05d7\u0003"+
		"\u00ccf\u0000\u05d7\u05d8\u0005y\u0000\u0000\u05d8\u05d9\u0003\u00ceg"+
		"\u0000\u05d9\u00cb\u0001\u0000\u0000\u0000\u05da\u05f1\u0003\u00a2Q\u0000"+
		"\u05db\u05dd\u0005N\u0000\u0000\u05dc\u05de\u0003\\.\u0000\u05dd\u05dc"+
		"\u0001\u0000\u0000\u0000\u05dd\u05de\u0001\u0000\u0000\u0000\u05de\u05df"+
		"\u0001\u0000\u0000\u0000\u05df\u05f1\u0005O\u0000\u0000\u05e0\u05e1\u0005"+
		"N\u0000\u0000\u05e1\u05e6\u0003\u00a2Q\u0000\u05e2\u05e3\u0005U\u0000"+
		"\u0000\u05e3\u05e5\u0003\u00a2Q\u0000\u05e4\u05e2\u0001\u0000\u0000\u0000"+
		"\u05e5\u05e8\u0001\u0000\u0000\u0000\u05e6\u05e4\u0001\u0000\u0000\u0000"+
		"\u05e6\u05e7\u0001\u0000\u0000\u0000\u05e7\u05e9\u0001\u0000\u0000\u0000"+
		"\u05e8\u05e6\u0001\u0000\u0000\u0000\u05e9\u05ea\u0005O\u0000\u0000\u05ea"+
		"\u05f1\u0001\u0000\u0000\u0000\u05eb\u05ed\u0005N\u0000\u0000\u05ec\u05ee"+
		"\u0003b1\u0000\u05ed\u05ec\u0001\u0000\u0000\u0000\u05ed\u05ee\u0001\u0000"+
		"\u0000\u0000\u05ee\u05ef\u0001\u0000\u0000\u0000\u05ef\u05f1\u0005O\u0000"+
		"\u0000\u05f0\u05da\u0001\u0000\u0000\u0000\u05f0\u05db\u0001\u0000\u0000"+
		"\u0000\u05f0\u05e0\u0001\u0000\u0000\u0000\u05f0\u05eb\u0001\u0000\u0000"+
		"\u0000\u05f1\u00cd\u0001\u0000\u0000\u0000\u05f2\u05f5\u0003\u00c6c\u0000"+
		"\u05f3\u05f5\u0003\u009cN\u0000\u05f4\u05f2\u0001\u0000\u0000\u0000\u05f4"+
		"\u05f3\u0001\u0000\u0000\u0000\u05f5\u00cf\u0001\u0000\u0000\u0000\u05f6"+
		"\u05f7\u0005N\u0000\u0000\u05f7\u05f8\u0003\u00c6c\u0000\u05f8\u05f9\u0005"+
		"O\u0000\u0000\u05f9\u0609\u0001\u0000\u0000\u0000\u05fa\u0609\u0005+\u0000"+
		"\u0000\u05fb\u0609\u0005(\u0000\u0000\u05fc\u0609\u0003h4\u0000\u05fd"+
		"\u0609\u0003\u00a2Q\u0000\u05fe\u05ff\u0003,\u0016\u0000\u05ff\u0600\u0005"+
		"V\u0000\u0000\u0600\u0601\u0005\t\u0000\u0000\u0601\u0609\u0001\u0000"+
		"\u0000\u0000\u0602\u0606\u0003\u00ecv\u0000\u0603\u0607\u0003\u00f8|\u0000"+
		"\u0604\u0605\u0005+\u0000\u0000\u0605\u0607\u0003\u00fa}\u0000\u0606\u0603"+
		"\u0001\u0000\u0000\u0000\u0606\u0604\u0001\u0000\u0000\u0000\u0607\u0609"+
		"\u0001\u0000\u0000\u0000\u0608\u05f6\u0001\u0000\u0000\u0000\u0608\u05fa"+
		"\u0001\u0000\u0000\u0000\u0608\u05fb\u0001\u0000\u0000\u0000\u0608\u05fc"+
		"\u0001\u0000\u0000\u0000\u0608\u05fd\u0001\u0000\u0000\u0000\u0608\u05fe"+
		"\u0001\u0000\u0000\u0000\u0608\u0602\u0001\u0000\u0000\u0000\u0609\u00d1"+
		"\u0001\u0000\u0000\u0000\u060a\u060b\u0005)\u0000\u0000\u060b\u060c\u0003"+
		"\u00c0`\u0000\u060c\u0610\u0005P\u0000\u0000\u060d\u060f\u0003\u00d4j"+
		"\u0000\u060e\u060d\u0001\u0000\u0000\u0000\u060f\u0612\u0001\u0000\u0000"+
		"\u0000\u0610\u060e\u0001\u0000\u0000\u0000\u0610\u0611\u0001\u0000\u0000"+
		"\u0000\u0611\u0613\u0001\u0000\u0000\u0000\u0612\u0610\u0001\u0000\u0000"+
		"\u0000\u0613\u0614\u0005Q\u0000\u0000\u0614\u00d3\u0001\u0000\u0000\u0000"+
		"\u0615\u0619\u0005\u0006\u0000\u0000\u0616\u061a\u0003\u00c2a\u0000\u0617"+
		"\u061a\u0005M\u0000\u0000\u0618\u061a\u0003\u00d6k\u0000\u0619\u0616\u0001"+
		"\u0000\u0000\u0000\u0619\u0617\u0001\u0000\u0000\u0000\u0619\u0618\u0001"+
		"\u0000\u0000\u0000\u061a\u061b\u0001\u0000\u0000\u0000\u061b\u061c\u0007"+
		"\u000e\u0000\u0000\u061c\u0621\u0003\u00d8l\u0000\u061d\u061e\u0005\f"+
		"\u0000\u0000\u061e\u061f\u0007\u000e\u0000\u0000\u061f\u0621\u0003\u00d8"+
		"l\u0000\u0620\u0615\u0001\u0000\u0000\u0000\u0620\u061d\u0001\u0000\u0000"+
		"\u0000\u0621\u00d5\u0001\u0000\u0000\u0000\u0622\u0623\u0006k\uffff\uffff"+
		"\u0000\u0623\u0624\u0005N\u0000\u0000\u0624\u0625\u0003\u00d6k\u0000\u0625"+
		"\u0626\u0005O\u0000\u0000\u0626\u063d\u0001\u0000\u0000\u0000\u0627\u0629"+
		"\u0003\f\u0006\u0000\u0628\u0627\u0001\u0000\u0000\u0000\u0629\u062c\u0001"+
		"\u0000\u0000\u0000\u062a\u0628\u0001\u0000\u0000\u0000\u062a\u062b\u0001"+
		"\u0000\u0000\u0000\u062b\u062d\u0001\u0000\u0000\u0000\u062c\u062a\u0001"+
		"\u0000\u0000\u0000\u062d\u0631\u0003\u00f0x\u0000\u062e\u0630\u0003p8"+
		"\u0000\u062f\u062e\u0001\u0000\u0000\u0000\u0630\u0633\u0001\u0000\u0000"+
		"\u0000\u0631\u062f\u0001\u0000\u0000\u0000\u0631\u0632\u0001\u0000\u0000"+
		"\u0000\u0632\u0634\u0001\u0000\u0000\u0000\u0633\u0631\u0001\u0000\u0000"+
		"\u0000\u0634\u0639\u0003\u00a2Q\u0000\u0635\u0636\u0005b\u0000\u0000\u0636"+
		"\u0638\u0003\u00c6c\u0000\u0637\u0635\u0001\u0000\u0000\u0000\u0638\u063b"+
		"\u0001\u0000\u0000\u0000\u0639\u0637\u0001\u0000\u0000\u0000\u0639\u063a"+
		"\u0001\u0000\u0000\u0000\u063a\u063d\u0001\u0000\u0000\u0000\u063b\u0639"+
		"\u0001\u0000\u0000\u0000\u063c\u0622\u0001\u0000\u0000\u0000\u063c\u062a"+
		"\u0001\u0000\u0000\u0000\u063d\u0643\u0001\u0000\u0000\u0000\u063e\u063f"+
		"\n\u0001\u0000\u0000\u063f\u0640\u0005b\u0000\u0000\u0640\u0642\u0003"+
		"\u00c6c\u0000\u0641\u063e\u0001\u0000\u0000\u0000\u0642\u0645\u0001\u0000"+
		"\u0000\u0000\u0643\u0641\u0001\u0000\u0000\u0000\u0643\u0644\u0001\u0000"+
		"\u0000\u0000\u0644\u00d7\u0001\u0000\u0000\u0000\u0645\u0643\u0001\u0000"+
		"\u0000\u0000\u0646\u064e\u0003\u009cN\u0000\u0647\u0649\u0003\u009eO\u0000"+
		"\u0648\u0647\u0001\u0000\u0000\u0000\u0649\u064c\u0001\u0000\u0000\u0000"+
		"\u064a\u0648\u0001\u0000\u0000\u0000\u064a\u064b\u0001\u0000\u0000\u0000"+
		"\u064b\u064e\u0001\u0000\u0000\u0000\u064c\u064a\u0001\u0000\u0000\u0000"+
		"\u064d\u0646\u0001\u0000\u0000\u0000\u064d\u064a\u0001\u0000\u0000\u0000"+
		"\u064e\u00d9\u0001\u0000\u0000\u0000\u064f\u0650\u0003R)\u0000\u0650\u0651"+
		"\u0005V\u0000\u0000\u0651\u0653\u0001\u0000\u0000\u0000\u0652\u064f\u0001"+
		"\u0000\u0000\u0000\u0652\u0653\u0001\u0000\u0000\u0000\u0653\u0657\u0001"+
		"\u0000\u0000\u0000\u0654\u0656\u0003p8\u0000\u0655\u0654\u0001\u0000\u0000"+
		"\u0000\u0656\u0659\u0001\u0000\u0000\u0000\u0657\u0655\u0001\u0000\u0000"+
		"\u0000\u0657\u0658\u0001\u0000\u0000\u0000\u0658\u065a\u0001\u0000\u0000"+
		"\u0000\u0659\u0657\u0001\u0000\u0000\u0000\u065a\u065c\u0003\u00a2Q\u0000"+
		"\u065b\u065d\u0003\u00f4z\u0000\u065c\u065b\u0001\u0000\u0000\u0000\u065c"+
		"\u065d\u0001\u0000\u0000\u0000\u065d\u00db\u0001\u0000\u0000\u0000\u065e"+
		"\u065f\u0003\u00ecv\u0000\u065f\u0660\u0003\u00deo\u0000\u0660\u0661\u0003"+
		"\u00e4r\u0000\u0661\u0668\u0001\u0000\u0000\u0000\u0662\u0665\u0003\u00de"+
		"o\u0000\u0663\u0666\u0003\u00e2q\u0000\u0664\u0666\u0003\u00e4r\u0000"+
		"\u0665\u0663\u0001\u0000\u0000\u0000\u0665\u0664\u0001\u0000\u0000\u0000"+
		"\u0666\u0668\u0001\u0000\u0000\u0000\u0667\u065e\u0001\u0000\u0000\u0000"+
		"\u0667\u0662\u0001\u0000\u0000\u0000\u0668\u00dd\u0001\u0000\u0000\u0000"+
		"\u0669\u066b\u0003\u00a2Q\u0000\u066a\u066c\u0003\u00e8t\u0000\u066b\u066a"+
		"\u0001\u0000\u0000\u0000\u066b\u066c\u0001\u0000\u0000\u0000\u066c\u0674"+
		"\u0001\u0000\u0000\u0000\u066d\u066e\u0005V\u0000\u0000\u066e\u0670\u0003"+
		"\u00a2Q\u0000\u066f\u0671\u0003\u00e8t\u0000\u0670\u066f\u0001\u0000\u0000"+
		"\u0000\u0670\u0671\u0001\u0000\u0000\u0000\u0671\u0673\u0001\u0000\u0000"+
		"\u0000\u0672\u066d\u0001\u0000\u0000\u0000\u0673\u0676\u0001\u0000\u0000"+
		"\u0000\u0674\u0672\u0001\u0000\u0000\u0000\u0674\u0675\u0001\u0000\u0000"+
		"\u0000\u0675\u0679\u0001\u0000\u0000\u0000\u0676\u0674\u0001\u0000\u0000"+
		"\u0000\u0677\u0679\u0003\u00f2y\u0000\u0678\u0669\u0001\u0000\u0000\u0000"+
		"\u0678\u0677\u0001\u0000\u0000\u0000\u0679\u00df\u0001\u0000\u0000\u0000"+
		"\u067a\u067c\u0003\u00a2Q\u0000\u067b\u067d\u0003\u00eau\u0000\u067c\u067b"+
		"\u0001\u0000\u0000\u0000\u067c\u067d\u0001\u0000\u0000\u0000\u067d\u067e"+
		"\u0001\u0000\u0000\u0000\u067e\u067f\u0003\u00e4r\u0000\u067f\u00e1\u0001"+
		"\u0000\u0000\u0000\u0680\u069c\u0005R\u0000\u0000\u0681\u0686\u0005S\u0000"+
		"\u0000\u0682\u0683\u0005R\u0000\u0000\u0683\u0685\u0005S\u0000\u0000\u0684"+
		"\u0682\u0001\u0000\u0000\u0000\u0685\u0688\u0001\u0000\u0000\u0000\u0686"+
		"\u0684\u0001\u0000\u0000\u0000\u0686\u0687\u0001\u0000\u0000\u0000\u0687"+
		"\u0689\u0001\u0000\u0000\u0000\u0688\u0686\u0001\u0000\u0000\u0000\u0689"+
		"\u069d\u0003P(\u0000\u068a\u068b\u0003\u00c6c\u0000\u068b\u0692\u0005"+
		"S\u0000\u0000\u068c\u068d\u0005R\u0000\u0000\u068d\u068e\u0003\u00c6c"+
		"\u0000\u068e\u068f\u0005S\u0000\u0000\u068f\u0691\u0001\u0000\u0000\u0000"+
		"\u0690\u068c\u0001\u0000\u0000\u0000\u0691\u0694\u0001\u0000\u0000\u0000"+
		"\u0692\u0690\u0001\u0000\u0000\u0000\u0692\u0693\u0001\u0000\u0000\u0000"+
		"\u0693\u0699\u0001\u0000\u0000\u0000\u0694\u0692\u0001\u0000\u0000\u0000"+
		"\u0695\u0696\u0005R\u0000\u0000\u0696\u0698\u0005S\u0000\u0000\u0697\u0695"+
		"\u0001\u0000\u0000\u0000\u0698\u069b\u0001\u0000\u0000\u0000\u0699\u0697"+
		"\u0001\u0000\u0000\u0000\u0699\u069a\u0001\u0000\u0000\u0000\u069a\u069d"+
		"\u0001\u0000\u0000\u0000\u069b\u0699\u0001\u0000\u0000\u0000\u069c\u0681"+
		"\u0001\u0000\u0000\u0000\u069c\u068a\u0001\u0000\u0000\u0000\u069d\u00e3"+
		"\u0001\u0000\u0000\u0000\u069e\u06a0\u0003\u00fa}\u0000\u069f\u06a1\u0003"+
		" \u0010\u0000\u06a0\u069f\u0001\u0000\u0000\u0000\u06a0\u06a1\u0001\u0000"+
		"\u0000\u0000\u06a1\u00e5\u0001\u0000\u0000\u0000\u06a2\u06a3\u0003\u00ec"+
		"v\u0000\u06a3\u06a4\u0003\u00f8|\u0000\u06a4\u00e7\u0001\u0000\u0000\u0000"+
		"\u06a5\u06a6\u0005Y\u0000\u0000\u06a6\u06a9\u0005X\u0000\u0000\u06a7\u06a9"+
		"\u0003\u00f4z\u0000\u06a8\u06a5\u0001\u0000\u0000\u0000\u06a8\u06a7\u0001"+
		"\u0000\u0000\u0000\u06a9\u00e9\u0001\u0000\u0000\u0000\u06aa\u06ab\u0005"+
		"Y\u0000\u0000\u06ab\u06ae\u0005X\u0000\u0000\u06ac\u06ae\u0003\u00ecv"+
		"\u0000\u06ad\u06aa\u0001\u0000\u0000\u0000\u06ad\u06ac\u0001\u0000\u0000"+
		"\u0000\u06ae\u00eb\u0001\u0000\u0000\u0000\u06af\u06b0\u0005Y\u0000\u0000"+
		"\u06b0\u06b1\u0003\u00eew\u0000\u06b1\u06b2\u0005X\u0000\u0000\u06b2\u00ed"+
		"\u0001\u0000\u0000\u0000\u06b3\u06b8\u0003\u00f0x\u0000\u06b4\u06b5\u0005"+
		"U\u0000\u0000\u06b5\u06b7\u0003\u00f0x\u0000\u06b6\u06b4\u0001\u0000\u0000"+
		"\u0000\u06b7\u06ba\u0001\u0000\u0000\u0000\u06b8\u06b6\u0001\u0000\u0000"+
		"\u0000\u06b8\u06b9\u0001\u0000\u0000\u0000\u06b9\u00ef\u0001\u0000\u0000"+
		"\u0000\u06ba\u06b8\u0001\u0000\u0000\u0000\u06bb\u06bd\u0003p8\u0000\u06bc"+
		"\u06bb\u0001\u0000\u0000\u0000\u06bd\u06c0\u0001\u0000\u0000\u0000\u06be"+
		"\u06bc\u0001\u0000\u0000\u0000\u06be\u06bf\u0001\u0000\u0000\u0000\u06bf"+
		"\u06c3\u0001\u0000\u0000\u0000\u06c0\u06be\u0001\u0000\u0000\u0000\u06c1"+
		"\u06c4\u0003R)\u0000\u06c2\u06c4\u0003\u00f2y\u0000\u06c3\u06c1\u0001"+
		"\u0000\u0000\u0000\u06c3\u06c2\u0001\u0000\u0000\u0000\u06c4\u06cf\u0001"+
		"\u0000\u0000\u0000\u06c5\u06c7\u0003p8\u0000\u06c6\u06c5\u0001\u0000\u0000"+
		"\u0000\u06c7\u06ca\u0001\u0000\u0000\u0000\u06c8\u06c6\u0001\u0000\u0000"+
		"\u0000\u06c8\u06c9\u0001\u0000\u0000\u0000\u06c9\u06cb\u0001\u0000\u0000"+
		"\u0000\u06ca\u06c8\u0001\u0000\u0000\u0000\u06cb\u06cc\u0005R\u0000\u0000"+
		"\u06cc\u06ce\u0005S\u0000\u0000\u06cd\u06c8\u0001\u0000\u0000\u0000\u06ce"+
		"\u06d1\u0001\u0000\u0000\u0000\u06cf\u06cd\u0001\u0000\u0000\u0000\u06cf"+
		"\u06d0\u0001\u0000\u0000\u0000\u06d0\u00f1\u0001\u0000\u0000\u0000\u06d1"+
		"\u06cf\u0001\u0000\u0000\u0000\u06d2\u06d3\u0007\u000f\u0000\u0000\u06d3"+
		"\u00f3\u0001\u0000\u0000\u0000\u06d4\u06d5\u0005Y\u0000\u0000\u06d5\u06da"+
		"\u0003T*\u0000\u06d6\u06d7\u0005U\u0000\u0000\u06d7\u06d9\u0003T*\u0000"+
		"\u06d8\u06d6\u0001\u0000\u0000\u0000\u06d9\u06dc\u0001\u0000\u0000\u0000"+
		"\u06da\u06d8\u0001\u0000\u0000\u0000\u06da\u06db\u0001\u0000\u0000\u0000"+
		"\u06db\u06dd\u0001\u0000\u0000\u0000\u06dc\u06da\u0001\u0000\u0000\u0000"+
		"\u06dd\u06de\u0005X\u0000\u0000\u06de\u00f5\u0001\u0000\u0000\u0000\u06df"+
		"\u06e9\u0003\u00fa}\u0000\u06e0\u06e2\u0005V\u0000\u0000\u06e1\u06e3\u0003"+
		"\u00f4z\u0000\u06e2\u06e1\u0001\u0000\u0000\u0000\u06e2\u06e3\u0001\u0000"+
		"\u0000\u0000\u06e3\u06e4\u0001\u0000\u0000\u0000\u06e4\u06e6\u0003\u00a2"+
		"Q\u0000\u06e5\u06e7\u0003\u00fa}\u0000\u06e6\u06e5\u0001\u0000\u0000\u0000"+
		"\u06e6\u06e7\u0001\u0000\u0000\u0000\u06e7\u06e9\u0001\u0000\u0000\u0000"+
		"\u06e8\u06df\u0001\u0000\u0000\u0000\u06e8\u06e0\u0001\u0000\u0000\u0000"+
		"\u06e9\u00f7\u0001\u0000\u0000\u0000\u06ea\u06eb\u0005(\u0000\u0000\u06eb"+
		"\u06f0\u0003\u00f6{\u0000\u06ec\u06ed\u0003\u00a2Q\u0000\u06ed\u06ee\u0003"+
		"\u00fa}\u0000\u06ee\u06f0\u0001\u0000\u0000\u0000\u06ef\u06ea\u0001\u0000"+
		"\u0000\u0000\u06ef\u06ec\u0001\u0000\u0000\u0000\u06f0\u00f9\u0001\u0000"+
		"\u0000\u0000\u06f1\u06f3\u0005N\u0000\u0000\u06f2\u06f4\u0003\u00c2a\u0000"+
		"\u06f3\u06f2\u0001\u0000\u0000\u0000\u06f3\u06f4\u0001\u0000\u0000\u0000"+
		"\u06f4\u06f5\u0001\u0000\u0000\u0000\u06f5\u06f6\u0005O\u0000\u0000\u06f6"+
		"\u00fb\u0001\u0000\u0000\u0000\u06f7\u06fa\u0003$\u0012\u0000\u06f8\u06fa"+
		"\u00038\u001c\u0000\u06f9\u06f7\u0001\u0000\u0000\u0000\u06f9\u06f8\u0001"+
		"\u0000\u0000\u0000\u06fa\u00fd\u0001\u0000\u0000\u0000\u06fb\u0701\u0003"+
		"\u0006\u0003\u0000\u06fc\u0701\u0003\u00fc~\u0000\u06fd\u0701\u0003\u0004"+
		"\u0002\u0000\u06fe\u0701\u0003\u0002\u0001\u0000\u06ff\u0701\u0003\u0100"+
		"\u0080\u0000\u0700\u06fb\u0001\u0000\u0000\u0000\u0700\u06fc\u0001\u0000"+
		"\u0000\u0000\u0700\u06fd\u0001\u0000\u0000\u0000\u0700\u06fe\u0001\u0000"+
		"\u0000\u0000\u0700\u06ff\u0001\u0000\u0000\u0000\u0701\u0702\u0001\u0000"+
		"\u0000\u0000\u0702\u0703\u0001\u0000\u0000\u0000\u0702\u0700\u0001\u0000"+
		"\u0000\u0000\u0703\u0706\u0001\u0000\u0000\u0000\u0704\u0706\u0005\u0000"+
		"\u0000\u0001\u0705\u0700\u0001\u0000\u0000\u0000\u0705\u0704\u0001\u0000"+
		"\u0000\u0000\u0706\u00ff\u0001\u0000\u0000\u0000\u0707\u0709\t\u0000\u0000"+
		"\u0000\u0708\u0707\u0001\u0000\u0000\u0000\u0709\u070a\u0001\u0000\u0000"+
		"\u0000\u070a\u0708\u0001\u0000\u0000\u0000\u070a\u070b\u0001\u0000\u0000"+
		"\u0000\u070b\u0101\u0001\u0000\u0000\u0000\u00e2\u0103\u0108\u010e\u0114"+
		"\u0119\u0122\u0127\u012e\u0136\u0139\u0140\u014c\u0150\u0155\u0159\u015d"+
		"\u0161\u016b\u0173\u017b\u017f\u0186\u018d\u0191\u0194\u0197\u01a0\u01a6"+
		"\u01ab\u01ae\u01b4\u01ba\u01be\u01c2\u01ca\u01d3\u01da\u01e0\u01e4\u01f0"+
		"\u01f9\u01fe\u0204\u0208\u0214\u021b\u0228\u022d\u0237\u023f\u0249\u0252"+
		"\u025d\u0262\u026b\u0275\u027a\u0283\u0289\u0290\u0295\u029d\u02a1\u02a3"+
		"\u02a9\u02af\u02b4\u02ba\u02c0\u02c2\u02c9\u02ce\u02d3\u02d6\u02d8\u02e2"+
		"\u02ec\u02f1\u02f4\u02f9\u0302\u0309\u0314\u031a\u0325\u032f\u033a\u0343"+
		"\u0348\u034b\u0352\u035c\u0364\u0367\u036a\u0377\u037f\u0384\u038c\u0390"+
		"\u0394\u0398\u039c\u039e\u03a2\u03a8\u03b0\u03ba\u03c3\u03cd\u03d5\u03e3"+
		"\u03ea\u03ef\u03f5\u03fe\u0407\u0409\u0412\u041c\u0421\u042c\u0435\u043b"+
		"\u0442\u044b\u0462\u0465\u0468\u0470\u0474\u047c\u0482\u048d\u0496\u049b"+
		"\u04a8\u04ae\u04b5\u04c2\u04cb\u04d4\u04da\u04e2\u04e8\u04ed\u04f2\u04fa"+
		"\u04ff\u0503\u0507\u050b\u050d\u0511\u0516\u051b\u052a\u0530\u0537\u053d"+
		"\u0540\u054b\u0553\u0562\u0566\u056b\u056f\u057f\u05a7\u05ad\u05ba\u05bf"+
		"\u05c2\u05c4\u05ca\u05d1\u05dd\u05e6\u05ed\u05f0\u05f4\u0606\u0608\u0610"+
		"\u0619\u0620\u062a\u0631\u0639\u063c\u0643\u064a\u064d\u0652\u0657\u065c"+
		"\u0665\u0667\u066b\u0670\u0674\u0678\u067c\u0686\u0692\u0699\u069c\u06a0"+
		"\u06a8\u06ad\u06b8\u06be\u06c3\u06c8\u06cf\u06da\u06e2\u06e6\u06e8\u06ef"+
		"\u06f3\u06f9\u0700\u0702\u0705\u070a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}