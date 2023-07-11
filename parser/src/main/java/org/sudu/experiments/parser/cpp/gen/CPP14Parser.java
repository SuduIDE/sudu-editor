// Generated from parser-generator/src/main/resources/grammar/cpp/CPP14Parser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.cpp.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CPP14Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IntegerLiteral=1, CharacterLiteral=2, FloatingLiteral=3, StringLiteral=4, 
		BooleanLiteral=5, PointerLiteral=6, UserDefinedLiteral=7, MultiLineMacro=8, 
		Directive=9, Alignas=10, Alignof=11, Asm=12, Auto=13, Bool=14, Break=15, 
		Case=16, Catch=17, Char=18, Char16=19, Char32=20, Class=21, Const=22, 
		Constexpr=23, Const_cast=24, Continue=25, Decltype=26, Default=27, Delete=28, 
		Do=29, Double=30, Dynamic_cast=31, Else=32, Enum=33, Explicit=34, Export=35, 
		Extern=36, False_=37, Final=38, Float=39, For=40, Friend=41, Goto=42, 
		If=43, Inline=44, Int=45, Long=46, Mutable=47, Namespace=48, New=49, Noexcept=50, 
		Nullptr=51, Operator=52, Override=53, Private=54, Protected=55, Public=56, 
		Register=57, Reinterpret_cast=58, Return=59, Short=60, Signed=61, Sizeof=62, 
		Static=63, Static_assert=64, Static_cast=65, Struct=66, Switch=67, Template=68, 
		This=69, Thread_local=70, Throw=71, True_=72, Try=73, Typedef=74, Typeid_=75, 
		Typename_=76, Union=77, Unsigned=78, Using=79, Virtual=80, Void=81, Volatile=82, 
		Wchar=83, While=84, LeftParen=85, RightParen=86, LeftBracket=87, RightBracket=88, 
		LeftBrace=89, RightBrace=90, Plus=91, Minus=92, Star=93, Div=94, Mod=95, 
		Caret=96, And=97, Or=98, Tilde=99, Not=100, Assign=101, Less=102, Greater=103, 
		PlusAssign=104, MinusAssign=105, StarAssign=106, DivAssign=107, ModAssign=108, 
		XorAssign=109, AndAssign=110, OrAssign=111, LeftShiftAssign=112, RightShiftAssign=113, 
		Equal=114, NotEqual=115, LessEqual=116, GreaterEqual=117, AndAnd=118, 
		OrOr=119, PlusPlus=120, MinusMinus=121, Comma=122, ArrowStar=123, Arrow=124, 
		Question=125, Colon=126, Doublecolon=127, Semi=128, Dot=129, DotStar=130, 
		Ellipsis=131, Identifier=132, DecimalLiteral=133, OctalLiteral=134, HexadecimalLiteral=135, 
		BinaryLiteral=136, Integersuffix=137, UserDefinedIntegerLiteral=138, UserDefinedFloatingLiteral=139, 
		UserDefinedStringLiteral=140, UserDefinedCharacterLiteral=141, Whitespace=142, 
		Newline=143, BlockComment=144, LineComment=145;
	public static final int
		RULE_translationUnit = 0, RULE_primaryExpression = 1, RULE_idExpression = 2, 
		RULE_unqualifiedId = 3, RULE_qualifiedId = 4, RULE_nestedNameSpecifier = 5, 
		RULE_lambdaExpression = 6, RULE_lambdaIntroducer = 7, RULE_lambdaCapture = 8, 
		RULE_captureDefault = 9, RULE_captureList = 10, RULE_capture = 11, RULE_simpleCapture = 12, 
		RULE_initcapture = 13, RULE_lambdaDeclarator = 14, RULE_postfixExpression = 15, 
		RULE_typeIdOfTheTypeId = 16, RULE_expressionList = 17, RULE_pseudoDestructorName = 18, 
		RULE_unaryExpression = 19, RULE_unaryOperator = 20, RULE_newExpression = 21, 
		RULE_newPlacement = 22, RULE_newTypeId = 23, RULE_newDeclarator = 24, 
		RULE_noPointerNewDeclarator = 25, RULE_newInitializer = 26, RULE_deleteExpression = 27, 
		RULE_noExceptExpression = 28, RULE_castExpression = 29, RULE_pointerMemberExpression = 30, 
		RULE_multiplicativeExpression = 31, RULE_additiveExpression = 32, RULE_shiftExpression = 33, 
		RULE_shiftOperator = 34, RULE_relationalExpression = 35, RULE_equalityExpression = 36, 
		RULE_andExpression = 37, RULE_exclusiveOrExpression = 38, RULE_inclusiveOrExpression = 39, 
		RULE_logicalAndExpression = 40, RULE_logicalOrExpression = 41, RULE_conditionalExpression = 42, 
		RULE_assignmentExpression = 43, RULE_assignmentOperator = 44, RULE_expression = 45, 
		RULE_constantExpression = 46, RULE_statement = 47, RULE_labeledStatement = 48, 
		RULE_expressionStatement = 49, RULE_compoundStatement = 50, RULE_statementSeq = 51, 
		RULE_selectionStatement = 52, RULE_condition = 53, RULE_iterationStatement = 54, 
		RULE_forInitStatement = 55, RULE_forRangeDeclaration = 56, RULE_forRangeInitializer = 57, 
		RULE_jumpStatement = 58, RULE_declarationStatement = 59, RULE_declarationseq = 60, 
		RULE_declaration = 61, RULE_blockDeclaration = 62, RULE_aliasDeclaration = 63, 
		RULE_simpleDeclaration = 64, RULE_staticAssertDeclaration = 65, RULE_emptyDeclaration = 66, 
		RULE_attributeDeclaration = 67, RULE_declSpecifier = 68, RULE_declSpecifierSeq = 69, 
		RULE_storageClassSpecifier = 70, RULE_functionSpecifier = 71, RULE_typedefName = 72, 
		RULE_typeSpecifier = 73, RULE_trailingTypeSpecifier = 74, RULE_typeSpecifierSeq = 75, 
		RULE_trailingTypeSpecifierSeq = 76, RULE_simpleTypeLengthModifier = 77, 
		RULE_simpleTypeSignednessModifier = 78, RULE_simpleTypeSpecifier = 79, 
		RULE_theTypeName = 80, RULE_decltypeSpecifier = 81, RULE_elaboratedTypeSpecifier = 82, 
		RULE_enumName = 83, RULE_enumSpecifier = 84, RULE_enumHead = 85, RULE_opaqueEnumDeclaration = 86, 
		RULE_enumkey = 87, RULE_enumbase = 88, RULE_enumeratorList = 89, RULE_enumeratorDefinition = 90, 
		RULE_enumerator = 91, RULE_namespaceName = 92, RULE_originalNamespaceName = 93, 
		RULE_namespaceDefinition = 94, RULE_namespaceAlias = 95, RULE_namespaceAliasDefinition = 96, 
		RULE_qualifiednamespacespecifier = 97, RULE_usingDeclaration = 98, RULE_usingDirective = 99, 
		RULE_asmDefinition = 100, RULE_linkageSpecification = 101, RULE_attributeSpecifierSeq = 102, 
		RULE_attributeSpecifier = 103, RULE_alignmentspecifier = 104, RULE_attributeList = 105, 
		RULE_attribute = 106, RULE_attributeNamespace = 107, RULE_attributeArgumentClause = 108, 
		RULE_balancedTokenSeq = 109, RULE_balancedtoken = 110, RULE_initDeclaratorList = 111, 
		RULE_initDeclarator = 112, RULE_declarator = 113, RULE_pointerDeclarator = 114, 
		RULE_noPointerDeclarator = 115, RULE_parametersAndQualifiers = 116, RULE_trailingReturnType = 117, 
		RULE_pointerOperator = 118, RULE_cvqualifierseq = 119, RULE_cvQualifier = 120, 
		RULE_refqualifier = 121, RULE_declaratorid = 122, RULE_theTypeId = 123, 
		RULE_abstractDeclarator = 124, RULE_pointerAbstractDeclarator = 125, RULE_noPointerAbstractDeclarator = 126, 
		RULE_abstractPackDeclarator = 127, RULE_noPointerAbstractPackDeclarator = 128, 
		RULE_parameterDeclarationClause = 129, RULE_parameterDeclarationList = 130, 
		RULE_parameterDeclaration = 131, RULE_functionDefinition = 132, RULE_functionBody = 133, 
		RULE_initializer = 134, RULE_braceOrEqualInitializer = 135, RULE_initializerClause = 136, 
		RULE_initializerList = 137, RULE_bracedInitList = 138, RULE_className = 139, 
		RULE_classSpecifier = 140, RULE_classHead = 141, RULE_classHeadName = 142, 
		RULE_classVirtSpecifier = 143, RULE_classKey = 144, RULE_memberSpecification = 145, 
		RULE_memberdeclaration = 146, RULE_memberDeclaratorList = 147, RULE_memberDeclarator = 148, 
		RULE_virtualSpecifierSeq = 149, RULE_virtualSpecifier = 150, RULE_pureSpecifier = 151, 
		RULE_baseClause = 152, RULE_baseSpecifierList = 153, RULE_baseSpecifier = 154, 
		RULE_classOrDeclType = 155, RULE_baseTypeSpecifier = 156, RULE_accessSpecifier = 157, 
		RULE_conversionFunctionId = 158, RULE_conversionTypeId = 159, RULE_conversionDeclarator = 160, 
		RULE_constructorInitializer = 161, RULE_memInitializerList = 162, RULE_memInitializer = 163, 
		RULE_meminitializerid = 164, RULE_operatorFunctionId = 165, RULE_literalOperatorId = 166, 
		RULE_templateDeclaration = 167, RULE_templateparameterList = 168, RULE_templateParameter = 169, 
		RULE_typeParameter = 170, RULE_simpleTemplateId = 171, RULE_templateId = 172, 
		RULE_templateName = 173, RULE_templateArgumentList = 174, RULE_templateArgument = 175, 
		RULE_typeNameSpecifier = 176, RULE_explicitInstantiation = 177, RULE_explicitSpecialization = 178, 
		RULE_tryBlock = 179, RULE_functionTryBlock = 180, RULE_handlerSeq = 181, 
		RULE_handler = 182, RULE_exceptionDeclaration = 183, RULE_throwExpression = 184, 
		RULE_exceptionSpecification = 185, RULE_dynamicExceptionSpecification = 186, 
		RULE_typeIdList = 187, RULE_noeExceptSpecification = 188, RULE_theOperator = 189, 
		RULE_literal = 190, RULE_translationUnitOrAny = 191, RULE_unknownInterval = 192, 
		RULE_anySeq = 193;
	private static String[] makeRuleNames() {
		return new String[] {
			"translationUnit", "primaryExpression", "idExpression", "unqualifiedId", 
			"qualifiedId", "nestedNameSpecifier", "lambdaExpression", "lambdaIntroducer", 
			"lambdaCapture", "captureDefault", "captureList", "capture", "simpleCapture", 
			"initcapture", "lambdaDeclarator", "postfixExpression", "typeIdOfTheTypeId", 
			"expressionList", "pseudoDestructorName", "unaryExpression", "unaryOperator", 
			"newExpression", "newPlacement", "newTypeId", "newDeclarator", "noPointerNewDeclarator", 
			"newInitializer", "deleteExpression", "noExceptExpression", "castExpression", 
			"pointerMemberExpression", "multiplicativeExpression", "additiveExpression", 
			"shiftExpression", "shiftOperator", "relationalExpression", "equalityExpression", 
			"andExpression", "exclusiveOrExpression", "inclusiveOrExpression", "logicalAndExpression", 
			"logicalOrExpression", "conditionalExpression", "assignmentExpression", 
			"assignmentOperator", "expression", "constantExpression", "statement", 
			"labeledStatement", "expressionStatement", "compoundStatement", "statementSeq", 
			"selectionStatement", "condition", "iterationStatement", "forInitStatement", 
			"forRangeDeclaration", "forRangeInitializer", "jumpStatement", "declarationStatement", 
			"declarationseq", "declaration", "blockDeclaration", "aliasDeclaration", 
			"simpleDeclaration", "staticAssertDeclaration", "emptyDeclaration", "attributeDeclaration", 
			"declSpecifier", "declSpecifierSeq", "storageClassSpecifier", "functionSpecifier", 
			"typedefName", "typeSpecifier", "trailingTypeSpecifier", "typeSpecifierSeq", 
			"trailingTypeSpecifierSeq", "simpleTypeLengthModifier", "simpleTypeSignednessModifier", 
			"simpleTypeSpecifier", "theTypeName", "decltypeSpecifier", "elaboratedTypeSpecifier", 
			"enumName", "enumSpecifier", "enumHead", "opaqueEnumDeclaration", "enumkey", 
			"enumbase", "enumeratorList", "enumeratorDefinition", "enumerator", "namespaceName", 
			"originalNamespaceName", "namespaceDefinition", "namespaceAlias", "namespaceAliasDefinition", 
			"qualifiednamespacespecifier", "usingDeclaration", "usingDirective", 
			"asmDefinition", "linkageSpecification", "attributeSpecifierSeq", "attributeSpecifier", 
			"alignmentspecifier", "attributeList", "attribute", "attributeNamespace", 
			"attributeArgumentClause", "balancedTokenSeq", "balancedtoken", "initDeclaratorList", 
			"initDeclarator", "declarator", "pointerDeclarator", "noPointerDeclarator", 
			"parametersAndQualifiers", "trailingReturnType", "pointerOperator", "cvqualifierseq", 
			"cvQualifier", "refqualifier", "declaratorid", "theTypeId", "abstractDeclarator", 
			"pointerAbstractDeclarator", "noPointerAbstractDeclarator", "abstractPackDeclarator", 
			"noPointerAbstractPackDeclarator", "parameterDeclarationClause", "parameterDeclarationList", 
			"parameterDeclaration", "functionDefinition", "functionBody", "initializer", 
			"braceOrEqualInitializer", "initializerClause", "initializerList", "bracedInitList", 
			"className", "classSpecifier", "classHead", "classHeadName", "classVirtSpecifier", 
			"classKey", "memberSpecification", "memberdeclaration", "memberDeclaratorList", 
			"memberDeclarator", "virtualSpecifierSeq", "virtualSpecifier", "pureSpecifier", 
			"baseClause", "baseSpecifierList", "baseSpecifier", "classOrDeclType", 
			"baseTypeSpecifier", "accessSpecifier", "conversionFunctionId", "conversionTypeId", 
			"conversionDeclarator", "constructorInitializer", "memInitializerList", 
			"memInitializer", "meminitializerid", "operatorFunctionId", "literalOperatorId", 
			"templateDeclaration", "templateparameterList", "templateParameter", 
			"typeParameter", "simpleTemplateId", "templateId", "templateName", "templateArgumentList", 
			"templateArgument", "typeNameSpecifier", "explicitInstantiation", "explicitSpecialization", 
			"tryBlock", "functionTryBlock", "handlerSeq", "handler", "exceptionDeclaration", 
			"throwExpression", "exceptionSpecification", "dynamicExceptionSpecification", 
			"typeIdList", "noeExceptSpecification", "theOperator", "literal", "translationUnitOrAny", 
			"unknownInterval", "anySeq"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "'alignas'", 
			"'alignof'", "'asm'", "'auto'", "'bool'", "'break'", "'case'", "'catch'", 
			"'char'", "'char16_t'", "'char32_t'", "'class'", "'const'", "'constexpr'", 
			"'const_cast'", "'continue'", "'decltype'", "'default'", "'delete'", 
			"'do'", "'double'", "'dynamic_cast'", "'else'", "'enum'", "'explicit'", 
			"'export'", "'extern'", "'false'", "'final'", "'float'", "'for'", "'friend'", 
			"'goto'", "'if'", "'inline'", "'int'", "'long'", "'mutable'", "'namespace'", 
			"'new'", "'noexcept'", "'nullptr'", "'operator'", "'override'", "'private'", 
			"'protected'", "'public'", "'register'", "'reinterpret_cast'", "'return'", 
			"'short'", "'signed'", "'sizeof'", "'static'", "'static_assert'", "'static_cast'", 
			"'struct'", "'switch'", "'template'", "'this'", "'thread_local'", "'throw'", 
			"'true'", "'try'", "'typedef'", "'typeid'", "'typename'", "'union'", 
			"'unsigned'", "'using'", "'virtual'", "'void'", "'volatile'", "'wchar_t'", 
			"'while'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'+'", "'-'", "'*'", 
			"'/'", "'%'", "'^'", "'&'", "'|'", "'~'", null, "'='", "'<'", "'>'", 
			"'+='", "'-='", "'*='", "'/='", "'%='", "'^='", "'&='", "'|='", "'<<='", 
			"'>>='", "'=='", "'!='", "'<='", "'>='", null, null, "'++'", "'--'", 
			"','", "'->*'", "'->'", "'?'", "':'", "'::'", "';'", "'.'", "'.*'", "'...'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IntegerLiteral", "CharacterLiteral", "FloatingLiteral", "StringLiteral", 
			"BooleanLiteral", "PointerLiteral", "UserDefinedLiteral", "MultiLineMacro", 
			"Directive", "Alignas", "Alignof", "Asm", "Auto", "Bool", "Break", "Case", 
			"Catch", "Char", "Char16", "Char32", "Class", "Const", "Constexpr", "Const_cast", 
			"Continue", "Decltype", "Default", "Delete", "Do", "Double", "Dynamic_cast", 
			"Else", "Enum", "Explicit", "Export", "Extern", "False_", "Final", "Float", 
			"For", "Friend", "Goto", "If", "Inline", "Int", "Long", "Mutable", "Namespace", 
			"New", "Noexcept", "Nullptr", "Operator", "Override", "Private", "Protected", 
			"Public", "Register", "Reinterpret_cast", "Return", "Short", "Signed", 
			"Sizeof", "Static", "Static_assert", "Static_cast", "Struct", "Switch", 
			"Template", "This", "Thread_local", "Throw", "True_", "Try", "Typedef", 
			"Typeid_", "Typename_", "Union", "Unsigned", "Using", "Virtual", "Void", 
			"Volatile", "Wchar", "While", "LeftParen", "RightParen", "LeftBracket", 
			"RightBracket", "LeftBrace", "RightBrace", "Plus", "Minus", "Star", "Div", 
			"Mod", "Caret", "And", "Or", "Tilde", "Not", "Assign", "Less", "Greater", 
			"PlusAssign", "MinusAssign", "StarAssign", "DivAssign", "ModAssign", 
			"XorAssign", "AndAssign", "OrAssign", "LeftShiftAssign", "RightShiftAssign", 
			"Equal", "NotEqual", "LessEqual", "GreaterEqual", "AndAnd", "OrOr", "PlusPlus", 
			"MinusMinus", "Comma", "ArrowStar", "Arrow", "Question", "Colon", "Doublecolon", 
			"Semi", "Dot", "DotStar", "Ellipsis", "Identifier", "DecimalLiteral", 
			"OctalLiteral", "HexadecimalLiteral", "BinaryLiteral", "Integersuffix", 
			"UserDefinedIntegerLiteral", "UserDefinedFloatingLiteral", "UserDefinedStringLiteral", 
			"UserDefinedCharacterLiteral", "Whitespace", "Newline", "BlockComment", 
			"LineComment"
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
	public String getGrammarFileName() { return "CPP14Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CPP14Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TranslationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CPP14Parser.EOF, 0); }
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public TranslationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTranslationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTranslationUnit(this);
		}
	}

	public final TranslationUnitContext translationUnit() throws RecognitionException {
		TranslationUnitContext _localctx = new TranslationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_translationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0)) {
				{
				setState(388);
				declarationseq();
				}
			}

			setState(391);
			match(EOF);
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
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public TerminalNode This() { return getToken(CPP14Parser.This, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPrimaryExpression(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_primaryExpression);
		try {
			int _alt;
			setState(405);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(394); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(393);
						literal();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(396); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case This:
				enterOuterAlt(_localctx, 2);
				{
				setState(398);
				match(This);
				}
				break;
			case LeftParen:
				enterOuterAlt(_localctx, 3);
				{
				setState(399);
				match(LeftParen);
				setState(400);
				expression();
				setState(401);
				match(RightParen);
				}
				break;
			case Decltype:
			case Operator:
			case Tilde:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 4);
				{
				setState(403);
				idExpression();
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 5);
				{
				setState(404);
				lambdaExpression();
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
	public static class IdExpressionContext extends ParserRuleContext {
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public QualifiedIdContext qualifiedId() {
			return getRuleContext(QualifiedIdContext.class,0);
		}
		public IdExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterIdExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitIdExpression(this);
		}
	}

	public final IdExpressionContext idExpression() throws RecognitionException {
		IdExpressionContext _localctx = new IdExpressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_idExpression);
		try {
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(407);
				unqualifiedId();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(408);
				qualifiedId();
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
	public static class UnqualifiedIdContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OperatorFunctionIdContext operatorFunctionId() {
			return getRuleContext(OperatorFunctionIdContext.class,0);
		}
		public ConversionFunctionIdContext conversionFunctionId() {
			return getRuleContext(ConversionFunctionIdContext.class,0);
		}
		public LiteralOperatorIdContext literalOperatorId() {
			return getRuleContext(LiteralOperatorIdContext.class,0);
		}
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public TemplateIdContext templateId() {
			return getRuleContext(TemplateIdContext.class,0);
		}
		public UnqualifiedIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unqualifiedId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnqualifiedId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnqualifiedId(this);
		}
	}

	public final UnqualifiedIdContext unqualifiedId() throws RecognitionException {
		UnqualifiedIdContext _localctx = new UnqualifiedIdContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unqualifiedId);
		try {
			setState(421);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				match(Identifier);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(412);
				operatorFunctionId();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(413);
				conversionFunctionId();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(414);
				literalOperatorId();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(415);
				match(Tilde);
				setState(418);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(416);
					className();
					}
					break;
				case Decltype:
					{
					setState(417);
					decltypeSpecifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(420);
				templateId();
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
	public static class QualifiedIdContext extends ParserRuleContext {
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public QualifiedIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterQualifiedId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitQualifiedId(this);
		}
	}

	public final QualifiedIdContext qualifiedId() throws RecognitionException {
		QualifiedIdContext _localctx = new QualifiedIdContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_qualifiedId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			nestedNameSpecifier(0);
			setState(425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Template) {
				{
				setState(424);
				match(Template);
				}
			}

			setState(427);
			unqualifiedId();
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
	public static class NestedNameSpecifierContext extends ParserRuleContext {
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TheTypeNameContext theTypeName() {
			return getRuleContext(TheTypeNameContext.class,0);
		}
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public NestedNameSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedNameSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNestedNameSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNestedNameSpecifier(this);
		}
	}

	public final NestedNameSpecifierContext nestedNameSpecifier() throws RecognitionException {
		return nestedNameSpecifier(0);
	}

	private NestedNameSpecifierContext nestedNameSpecifier(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NestedNameSpecifierContext _localctx = new NestedNameSpecifierContext(_ctx, _parentState);
		NestedNameSpecifierContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_nestedNameSpecifier, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(433);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(430);
				theTypeName();
				}
				break;

			case 2:
				{
				setState(431);
				namespaceName();
				}
				break;

			case 3:
				{
				setState(432);
				decltypeSpecifier();
				}
				break;
			}
			setState(435);
			match(Doublecolon);
			}
			_ctx.stop = _input.LT(-1);
			setState(448);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NestedNameSpecifierContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_nestedNameSpecifier);
					setState(437);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(443);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(438);
						match(Identifier);
						}
						break;

					case 2:
						{
						setState(440);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==Template) {
							{
							setState(439);
							match(Template);
							}
						}

						setState(442);
						simpleTemplateId();
						}
						break;
					}
					setState(445);
					match(Doublecolon);
					}
					} 
				}
				setState(450);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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
	public static class LambdaExpressionContext extends ParserRuleContext {
		public LambdaIntroducerContext lambdaIntroducer() {
			return getRuleContext(LambdaIntroducerContext.class,0);
		}
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public LambdaDeclaratorContext lambdaDeclarator() {
			return getRuleContext(LambdaDeclaratorContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaExpression(this);
		}
	}

	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_lambdaExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			lambdaIntroducer();
			setState(453);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen) {
				{
				setState(452);
				lambdaDeclarator();
				}
			}

			setState(455);
			compoundStatement();
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
	public static class LambdaIntroducerContext extends ParserRuleContext {
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public LambdaCaptureContext lambdaCapture() {
			return getRuleContext(LambdaCaptureContext.class,0);
		}
		public LambdaIntroducerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaIntroducer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaIntroducer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaIntroducer(this);
		}
	}

	public final LambdaIntroducerContext lambdaIntroducer() throws RecognitionException {
		LambdaIntroducerContext _localctx = new LambdaIntroducerContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_lambdaIntroducer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			match(LeftBracket);
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -9223372032291373055L) != 0)) {
				{
				setState(458);
				lambdaCapture();
				}
			}

			setState(461);
			match(RightBracket);
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
	public static class LambdaCaptureContext extends ParserRuleContext {
		public CaptureListContext captureList() {
			return getRuleContext(CaptureListContext.class,0);
		}
		public CaptureDefaultContext captureDefault() {
			return getRuleContext(CaptureDefaultContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public LambdaCaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaCapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaCapture(this);
		}
	}

	public final LambdaCaptureContext lambdaCapture() throws RecognitionException {
		LambdaCaptureContext _localctx = new LambdaCaptureContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_lambdaCapture);
		int _la;
		try {
			setState(469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(463);
				captureList();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(464);
				captureDefault();
				setState(467);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(465);
					match(Comma);
					setState(466);
					captureList();
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
	public static class CaptureDefaultContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public CaptureDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCaptureDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCaptureDefault(this);
		}
	}

	public final CaptureDefaultContext captureDefault() throws RecognitionException {
		CaptureDefaultContext _localctx = new CaptureDefaultContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_captureDefault);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			_la = _input.LA(1);
			if ( !(_la==And || _la==Assign) ) {
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
	public static class CaptureListContext extends ParserRuleContext {
		public List<CaptureContext> capture() {
			return getRuleContexts(CaptureContext.class);
		}
		public CaptureContext capture(int i) {
			return getRuleContext(CaptureContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public CaptureListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCaptureList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCaptureList(this);
		}
	}

	public final CaptureListContext captureList() throws RecognitionException {
		CaptureListContext _localctx = new CaptureListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_captureList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(473);
			capture();
			setState(478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(474);
				match(Comma);
				setState(475);
				capture();
				}
				}
				setState(480);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(482);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(481);
				match(Ellipsis);
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
	public static class CaptureContext extends ParserRuleContext {
		public SimpleCaptureContext simpleCapture() {
			return getRuleContext(SimpleCaptureContext.class,0);
		}
		public InitcaptureContext initcapture() {
			return getRuleContext(InitcaptureContext.class,0);
		}
		public CaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_capture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCapture(this);
		}
	}

	public final CaptureContext capture() throws RecognitionException {
		CaptureContext _localctx = new CaptureContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_capture);
		try {
			setState(486);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(484);
				simpleCapture();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(485);
				initcapture();
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
	public static class SimpleCaptureContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode This() { return getToken(CPP14Parser.This, 0); }
		public SimpleCaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleCapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleCapture(this);
		}
	}

	public final SimpleCaptureContext simpleCapture() throws RecognitionException {
		SimpleCaptureContext _localctx = new SimpleCaptureContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_simpleCapture);
		int _la;
		try {
			setState(493);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case And:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==And) {
					{
					setState(488);
					match(And);
					}
				}

				setState(491);
				match(Identifier);
				}
				break;
			case This:
				enterOuterAlt(_localctx, 2);
				{
				setState(492);
				match(This);
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
	public static class InitcaptureContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public InitcaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initcapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitcapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitcapture(this);
		}
	}

	public final InitcaptureContext initcapture() throws RecognitionException {
		InitcaptureContext _localctx = new InitcaptureContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_initcapture);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==And) {
				{
				setState(495);
				match(And);
				}
			}

			setState(498);
			match(Identifier);
			setState(499);
			initializer();
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
	public static class LambdaDeclaratorContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ParameterDeclarationClauseContext parameterDeclarationClause() {
			return getRuleContext(ParameterDeclarationClauseContext.class,0);
		}
		public TerminalNode Mutable() { return getToken(CPP14Parser.Mutable, 0); }
		public ExceptionSpecificationContext exceptionSpecification() {
			return getRuleContext(ExceptionSpecificationContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public LambdaDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaDeclarator(this);
		}
	}

	public final LambdaDeclaratorContext lambdaDeclarator() throws RecognitionException {
		LambdaDeclaratorContext _localctx = new LambdaDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_lambdaDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(LeftParen);
			setState(503);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1237504995584196377L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 297237575406461917L) != 0)) {
				{
				setState(502);
				parameterDeclarationClause();
				}
			}

			setState(505);
			match(RightParen);
			setState(507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Mutable) {
				{
				setState(506);
				match(Mutable);
				}
			}

			setState(510);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Noexcept || _la==Throw) {
				{
				setState(509);
				exceptionSpecification();
				}
			}

			setState(513);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(512);
				attributeSpecifierSeq();
				}
			}

			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Arrow) {
				{
				setState(515);
				trailingReturnType();
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
	public static class PostfixExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public TypeNameSpecifierContext typeNameSpecifier() {
			return getRuleContext(TypeNameSpecifierContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Dynamic_cast() { return getToken(CPP14Parser.Dynamic_cast, 0); }
		public TerminalNode Static_cast() { return getToken(CPP14Parser.Static_cast, 0); }
		public TerminalNode Reinterpret_cast() { return getToken(CPP14Parser.Reinterpret_cast, 0); }
		public TerminalNode Const_cast() { return getToken(CPP14Parser.Const_cast, 0); }
		public TypeIdOfTheTypeIdContext typeIdOfTheTypeId() {
			return getRuleContext(TypeIdOfTheTypeIdContext.class,0);
		}
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public TerminalNode Dot() { return getToken(CPP14Parser.Dot, 0); }
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public PseudoDestructorNameContext pseudoDestructorName() {
			return getRuleContext(PseudoDestructorNameContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPostfixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPostfixExpression(this);
		}
	}

	public final PostfixExpressionContext postfixExpression() throws RecognitionException {
		return postfixExpression(0);
	}

	private PostfixExpressionContext postfixExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PostfixExpressionContext _localctx = new PostfixExpressionContext(_ctx, _parentState);
		PostfixExpressionContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_postfixExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(519);
				primaryExpression();
				}
				break;

			case 2:
				{
				setState(522);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Decltype:
				case Double:
				case Float:
				case Int:
				case Long:
				case Short:
				case Signed:
				case Unsigned:
				case Void:
				case Wchar:
				case Doublecolon:
				case Identifier:
					{
					setState(520);
					simpleTypeSpecifier();
					}
					break;
				case Typename_:
					{
					setState(521);
					typeNameSpecifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(530);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LeftParen:
					{
					setState(524);
					match(LeftParen);
					setState(526);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0) || _la==Identifier) {
						{
						setState(525);
						expressionList();
						}
					}

					setState(528);
					match(RightParen);
					}
					break;
				case LeftBrace:
					{
					setState(529);
					bracedInitList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 3:
				{
				setState(532);
				_la = _input.LA(1);
				if ( !(((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 2216203124865L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(533);
				match(Less);
				setState(534);
				theTypeId();
				setState(535);
				match(Greater);
				setState(536);
				match(LeftParen);
				setState(537);
				expression();
				setState(538);
				match(RightParen);
				}
				break;

			case 4:
				{
				setState(540);
				typeIdOfTheTypeId();
				setState(541);
				match(LeftParen);
				setState(544);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(542);
					expression();
					}
					break;

				case 2:
					{
					setState(543);
					theTypeId();
					}
					break;
				}
				setState(546);
				match(RightParen);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(577);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(575);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
					case 1:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(550);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(551);
						match(LeftBracket);
						setState(554);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case IntegerLiteral:
						case CharacterLiteral:
						case FloatingLiteral:
						case StringLiteral:
						case BooleanLiteral:
						case PointerLiteral:
						case UserDefinedLiteral:
						case Alignof:
						case Auto:
						case Bool:
						case Char:
						case Char16:
						case Char32:
						case Const_cast:
						case Decltype:
						case Delete:
						case Double:
						case Dynamic_cast:
						case Float:
						case Int:
						case Long:
						case New:
						case Noexcept:
						case Operator:
						case Reinterpret_cast:
						case Short:
						case Signed:
						case Sizeof:
						case Static_cast:
						case This:
						case Throw:
						case Typeid_:
						case Typename_:
						case Unsigned:
						case Void:
						case Wchar:
						case LeftParen:
						case LeftBracket:
						case Plus:
						case Minus:
						case Star:
						case And:
						case Or:
						case Tilde:
						case Not:
						case PlusPlus:
						case MinusMinus:
						case Doublecolon:
						case Identifier:
							{
							setState(552);
							expression();
							}
							break;
						case LeftBrace:
							{
							setState(553);
							bracedInitList();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(556);
						match(RightBracket);
						}
						break;

					case 2:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(558);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(559);
						match(LeftParen);
						setState(561);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0) || _la==Identifier) {
							{
							setState(560);
							expressionList();
							}
						}

						setState(563);
						match(RightParen);
						}
						break;

					case 3:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(564);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(565);
						_la = _input.LA(1);
						if ( !(_la==Arrow || _la==Dot) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(571);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
						case 1:
							{
							setState(567);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==Template) {
								{
								setState(566);
								match(Template);
								}
							}

							setState(569);
							idExpression();
							}
							break;

						case 2:
							{
							setState(570);
							pseudoDestructorName();
							}
							break;
						}
						}
						break;

					case 4:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(573);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(574);
						_la = _input.LA(1);
						if ( !(_la==PlusPlus || _la==MinusMinus) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(579);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
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
	public static class TypeIdOfTheTypeIdContext extends ParserRuleContext {
		public TerminalNode Typeid_() { return getToken(CPP14Parser.Typeid_, 0); }
		public TypeIdOfTheTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeIdOfTheTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeIdOfTheTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeIdOfTheTypeId(this);
		}
	}

	public final TypeIdOfTheTypeIdContext typeIdOfTheTypeId() throws RecognitionException {
		TypeIdOfTheTypeIdContext _localctx = new TypeIdOfTheTypeIdContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_typeIdOfTheTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			match(Typeid_);
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
		public InitializerListContext initializerList() {
			return getRuleContext(InitializerListContext.class,0);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_expressionList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			initializerList();
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
	public static class PseudoDestructorNameContext extends ParserRuleContext {
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public List<TheTypeNameContext> theTypeName() {
			return getRuleContexts(TheTypeNameContext.class);
		}
		public TheTypeNameContext theTypeName(int i) {
			return getRuleContext(TheTypeNameContext.class,i);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public PseudoDestructorNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudoDestructorName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPseudoDestructorName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPseudoDestructorName(this);
		}
	}

	public final PseudoDestructorNameContext pseudoDestructorName() throws RecognitionException {
		PseudoDestructorNameContext _localctx = new PseudoDestructorNameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_pseudoDestructorName);
		int _la;
		try {
			setState(603);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(585);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(584);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(590);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(587);
					theTypeName();
					setState(588);
					match(Doublecolon);
					}
				}

				setState(592);
				match(Tilde);
				setState(593);
				theTypeName();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(594);
				nestedNameSpecifier(0);
				setState(595);
				match(Template);
				setState(596);
				simpleTemplateId();
				setState(597);
				match(Doublecolon);
				setState(598);
				match(Tilde);
				setState(599);
				theTypeName();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(601);
				match(Tilde);
				setState(602);
				decltypeSpecifier();
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
	public static class UnaryExpressionContext extends ParserRuleContext {
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public UnaryOperatorContext unaryOperator() {
			return getRuleContext(UnaryOperatorContext.class,0);
		}
		public TerminalNode Sizeof() { return getToken(CPP14Parser.Sizeof, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Alignof() { return getToken(CPP14Parser.Alignof, 0); }
		public NoExceptExpressionContext noExceptExpression() {
			return getRuleContext(NoExceptExpressionContext.class,0);
		}
		public NewExpressionContext newExpression() {
			return getRuleContext(NewExpressionContext.class,0);
		}
		public DeleteExpressionContext deleteExpression() {
			return getRuleContext(DeleteExpressionContext.class,0);
		}
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnaryExpression(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_unaryExpression);
		try {
			setState(632);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(605);
				postfixExpression(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(610);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PlusPlus:
					{
					setState(606);
					match(PlusPlus);
					}
					break;
				case MinusMinus:
					{
					setState(607);
					match(MinusMinus);
					}
					break;
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
					{
					setState(608);
					unaryOperator();
					}
					break;
				case Sizeof:
					{
					setState(609);
					match(Sizeof);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(612);
				unaryExpression();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(613);
				match(Sizeof);
				setState(622);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LeftParen:
					{
					setState(614);
					match(LeftParen);
					setState(615);
					theTypeId();
					setState(616);
					match(RightParen);
					}
					break;
				case Ellipsis:
					{
					setState(618);
					match(Ellipsis);
					setState(619);
					match(LeftParen);
					setState(620);
					match(Identifier);
					setState(621);
					match(RightParen);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(624);
				match(Alignof);
				setState(625);
				match(LeftParen);
				setState(626);
				theTypeId();
				setState(627);
				match(RightParen);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(629);
				noExceptExpression();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(630);
				newExpression();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(631);
				deleteExpression();
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
	public static class UnaryOperatorContext extends ParserRuleContext {
		public TerminalNode Or() { return getToken(CPP14Parser.Or, 0); }
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Plus() { return getToken(CPP14Parser.Plus, 0); }
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public TerminalNode Minus() { return getToken(CPP14Parser.Minus, 0); }
		public TerminalNode Not() { return getToken(CPP14Parser.Not, 0); }
		public UnaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnaryOperator(this);
		}
	}

	public final UnaryOperatorContext unaryOperator() throws RecognitionException {
		UnaryOperatorContext _localctx = new UnaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unaryOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			_la = _input.LA(1);
			if ( !(((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 967L) != 0)) ) {
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
	public static class NewExpressionContext extends ParserRuleContext {
		public TerminalNode New() { return getToken(CPP14Parser.New, 0); }
		public NewTypeIdContext newTypeId() {
			return getRuleContext(NewTypeIdContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public NewPlacementContext newPlacement() {
			return getRuleContext(NewPlacementContext.class,0);
		}
		public NewInitializerContext newInitializer() {
			return getRuleContext(NewInitializerContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NewExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewExpression(this);
		}
	}

	public final NewExpressionContext newExpression() throws RecognitionException {
		NewExpressionContext _localctx = new NewExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_newExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Doublecolon) {
				{
				setState(636);
				match(Doublecolon);
				}
			}

			setState(639);
			match(New);
			setState(641);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(640);
				newPlacement();
				}
				break;
			}
			setState(648);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case Doublecolon:
			case Identifier:
				{
				setState(643);
				newTypeId();
				}
				break;
			case LeftParen:
				{
				{
				setState(644);
				match(LeftParen);
				setState(645);
				theTypeId();
				setState(646);
				match(RightParen);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(651);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen || _la==LeftBrace) {
				{
				setState(650);
				newInitializer();
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
	public static class NewPlacementContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NewPlacementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newPlacement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewPlacement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewPlacement(this);
		}
	}

	public final NewPlacementContext newPlacement() throws RecognitionException {
		NewPlacementContext _localctx = new NewPlacementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_newPlacement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			match(LeftParen);
			setState(654);
			expressionList();
			setState(655);
			match(RightParen);
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
	public static class NewTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public NewDeclaratorContext newDeclarator() {
			return getRuleContext(NewDeclaratorContext.class,0);
		}
		public NewTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewTypeId(this);
		}
	}

	public final NewTypeIdContext newTypeId() throws RecognitionException {
		NewTypeIdContext _localctx = new NewTypeIdContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_newTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(657);
			typeSpecifierSeq();
			setState(659);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(658);
				newDeclarator();
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
	public static class NewDeclaratorContext extends ParserRuleContext {
		public PointerOperatorContext pointerOperator() {
			return getRuleContext(PointerOperatorContext.class,0);
		}
		public NewDeclaratorContext newDeclarator() {
			return getRuleContext(NewDeclaratorContext.class,0);
		}
		public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
			return getRuleContext(NoPointerNewDeclaratorContext.class,0);
		}
		public NewDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewDeclarator(this);
		}
	}

	public final NewDeclaratorContext newDeclarator() throws RecognitionException {
		NewDeclaratorContext _localctx = new NewDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_newDeclarator);
		try {
			setState(666);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Star:
			case And:
			case AndAnd:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(661);
				pointerOperator();
				setState(663);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(662);
					newDeclarator();
					}
					break;
				}
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(665);
				noPointerNewDeclarator(0);
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
	public static class NoPointerNewDeclaratorContext extends ParserRuleContext {
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
			return getRuleContext(NoPointerNewDeclaratorContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public NoPointerNewDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerNewDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerNewDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerNewDeclarator(this);
		}
	}

	public final NoPointerNewDeclaratorContext noPointerNewDeclarator() throws RecognitionException {
		return noPointerNewDeclarator(0);
	}

	private NoPointerNewDeclaratorContext noPointerNewDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerNewDeclaratorContext _localctx = new NoPointerNewDeclaratorContext(_ctx, _parentState);
		NoPointerNewDeclaratorContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_noPointerNewDeclarator, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(669);
			match(LeftBracket);
			setState(670);
			expression();
			setState(671);
			match(RightBracket);
			setState(673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(672);
				attributeSpecifierSeq();
				}
				break;
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(684);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerNewDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerNewDeclarator);
					setState(675);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(676);
					match(LeftBracket);
					setState(677);
					constantExpression();
					setState(678);
					match(RightBracket);
					setState(680);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
					case 1:
						{
						setState(679);
						attributeSpecifierSeq();
						}
						break;
					}
					}
					} 
				}
				setState(686);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
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
	public static class NewInitializerContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public NewInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewInitializer(this);
		}
	}

	public final NewInitializerContext newInitializer() throws RecognitionException {
		NewInitializerContext _localctx = new NewInitializerContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_newInitializer);
		int _la;
		try {
			setState(693);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				enterOuterAlt(_localctx, 1);
				{
				setState(687);
				match(LeftParen);
				setState(689);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0) || _la==Identifier) {
					{
					setState(688);
					expressionList();
					}
				}

				setState(691);
				match(RightParen);
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(692);
				bracedInitList();
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
	public static class DeleteExpressionContext extends ParserRuleContext {
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public CastExpressionContext castExpression() {
			return getRuleContext(CastExpressionContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public DeleteExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeleteExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeleteExpression(this);
		}
	}

	public final DeleteExpressionContext deleteExpression() throws RecognitionException {
		DeleteExpressionContext _localctx = new DeleteExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_deleteExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Doublecolon) {
				{
				setState(695);
				match(Doublecolon);
				}
			}

			setState(698);
			match(Delete);
			setState(701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(699);
				match(LeftBracket);
				setState(700);
				match(RightBracket);
				}
				break;
			}
			setState(703);
			castExpression();
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
	public static class NoExceptExpressionContext extends ParserRuleContext {
		public TerminalNode Noexcept() { return getToken(CPP14Parser.Noexcept, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoExceptExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noExceptExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoExceptExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoExceptExpression(this);
		}
	}

	public final NoExceptExpressionContext noExceptExpression() throws RecognitionException {
		NoExceptExpressionContext _localctx = new NoExceptExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_noExceptExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(705);
			match(Noexcept);
			setState(706);
			match(LeftParen);
			setState(707);
			expression();
			setState(708);
			match(RightParen);
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
	public static class CastExpressionContext extends ParserRuleContext {
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public CastExpressionContext castExpression() {
			return getRuleContext(CastExpressionContext.class,0);
		}
		public CastExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCastExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCastExpression(this);
		}
	}

	public final CastExpressionContext castExpression() throws RecognitionException {
		CastExpressionContext _localctx = new CastExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_castExpression);
		try {
			setState(716);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(710);
				unaryExpression();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(711);
				match(LeftParen);
				setState(712);
				theTypeId();
				setState(713);
				match(RightParen);
				setState(714);
				castExpression();
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
	public static class PointerMemberExpressionContext extends ParserRuleContext {
		public List<CastExpressionContext> castExpression() {
			return getRuleContexts(CastExpressionContext.class);
		}
		public CastExpressionContext castExpression(int i) {
			return getRuleContext(CastExpressionContext.class,i);
		}
		public List<TerminalNode> DotStar() { return getTokens(CPP14Parser.DotStar); }
		public TerminalNode DotStar(int i) {
			return getToken(CPP14Parser.DotStar, i);
		}
		public List<TerminalNode> ArrowStar() { return getTokens(CPP14Parser.ArrowStar); }
		public TerminalNode ArrowStar(int i) {
			return getToken(CPP14Parser.ArrowStar, i);
		}
		public PointerMemberExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerMemberExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerMemberExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerMemberExpression(this);
		}
	}

	public final PointerMemberExpressionContext pointerMemberExpression() throws RecognitionException {
		PointerMemberExpressionContext _localctx = new PointerMemberExpressionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_pointerMemberExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(718);
			castExpression();
			setState(723);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ArrowStar || _la==DotStar) {
				{
				{
				setState(719);
				_la = _input.LA(1);
				if ( !(_la==ArrowStar || _la==DotStar) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(720);
				castExpression();
				}
				}
				setState(725);
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
	public static class MultiplicativeExpressionContext extends ParserRuleContext {
		public List<PointerMemberExpressionContext> pointerMemberExpression() {
			return getRuleContexts(PointerMemberExpressionContext.class);
		}
		public PointerMemberExpressionContext pointerMemberExpression(int i) {
			return getRuleContext(PointerMemberExpressionContext.class,i);
		}
		public List<TerminalNode> Star() { return getTokens(CPP14Parser.Star); }
		public TerminalNode Star(int i) {
			return getToken(CPP14Parser.Star, i);
		}
		public List<TerminalNode> Div() { return getTokens(CPP14Parser.Div); }
		public TerminalNode Div(int i) {
			return getToken(CPP14Parser.Div, i);
		}
		public List<TerminalNode> Mod() { return getTokens(CPP14Parser.Mod); }
		public TerminalNode Mod(int i) {
			return getToken(CPP14Parser.Mod, i);
		}
		public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMultiplicativeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMultiplicativeExpression(this);
		}
	}

	public final MultiplicativeExpressionContext multiplicativeExpression() throws RecognitionException {
		MultiplicativeExpressionContext _localctx = new MultiplicativeExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_multiplicativeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			pointerMemberExpression();
			setState(731);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 7L) != 0)) {
				{
				{
				setState(727);
				_la = _input.LA(1);
				if ( !(((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 7L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(728);
				pointerMemberExpression();
				}
				}
				setState(733);
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
	public static class AdditiveExpressionContext extends ParserRuleContext {
		public List<MultiplicativeExpressionContext> multiplicativeExpression() {
			return getRuleContexts(MultiplicativeExpressionContext.class);
		}
		public MultiplicativeExpressionContext multiplicativeExpression(int i) {
			return getRuleContext(MultiplicativeExpressionContext.class,i);
		}
		public List<TerminalNode> Plus() { return getTokens(CPP14Parser.Plus); }
		public TerminalNode Plus(int i) {
			return getToken(CPP14Parser.Plus, i);
		}
		public List<TerminalNode> Minus() { return getTokens(CPP14Parser.Minus); }
		public TerminalNode Minus(int i) {
			return getToken(CPP14Parser.Minus, i);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAdditiveExpression(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_additiveExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(734);
			multiplicativeExpression();
			setState(739);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Plus || _la==Minus) {
				{
				{
				setState(735);
				_la = _input.LA(1);
				if ( !(_la==Plus || _la==Minus) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(736);
				multiplicativeExpression();
				}
				}
				setState(741);
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
	public static class ShiftExpressionContext extends ParserRuleContext {
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public List<ShiftOperatorContext> shiftOperator() {
			return getRuleContexts(ShiftOperatorContext.class);
		}
		public ShiftOperatorContext shiftOperator(int i) {
			return getRuleContext(ShiftOperatorContext.class,i);
		}
		public ShiftExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shiftExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterShiftExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitShiftExpression(this);
		}
	}

	public final ShiftExpressionContext shiftExpression() throws RecognitionException {
		ShiftExpressionContext _localctx = new ShiftExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_shiftExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
			additiveExpression();
			setState(748);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(743);
					shiftOperator();
					setState(744);
					additiveExpression();
					}
					} 
				}
				setState(750);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
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
	public static class ShiftOperatorContext extends ParserRuleContext {
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public ShiftOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shiftOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterShiftOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitShiftOperator(this);
		}
	}

	public final ShiftOperatorContext shiftOperator() throws RecognitionException {
		ShiftOperatorContext _localctx = new ShiftOperatorContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_shiftOperator);
		try {
			setState(755);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Greater:
				enterOuterAlt(_localctx, 1);
				{
				setState(751);
				match(Greater);
				setState(752);
				match(Greater);
				}
				break;
			case Less:
				enterOuterAlt(_localctx, 2);
				{
				setState(753);
				match(Less);
				setState(754);
				match(Less);
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
	public static class RelationalExpressionContext extends ParserRuleContext {
		public List<ShiftExpressionContext> shiftExpression() {
			return getRuleContexts(ShiftExpressionContext.class);
		}
		public ShiftExpressionContext shiftExpression(int i) {
			return getRuleContext(ShiftExpressionContext.class,i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> LessEqual() { return getTokens(CPP14Parser.LessEqual); }
		public TerminalNode LessEqual(int i) {
			return getToken(CPP14Parser.LessEqual, i);
		}
		public List<TerminalNode> GreaterEqual() { return getTokens(CPP14Parser.GreaterEqual); }
		public TerminalNode GreaterEqual(int i) {
			return getToken(CPP14Parser.GreaterEqual, i);
		}
		public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterRelationalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitRelationalExpression(this);
		}
	}

	public final RelationalExpressionContext relationalExpression() throws RecognitionException {
		RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_relationalExpression);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(757);
			shiftExpression();
			setState(762);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(758);
					_la = _input.LA(1);
					if ( !(((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & 49155L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(759);
					shiftExpression();
					}
					} 
				}
				setState(764);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
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
	public static class EqualityExpressionContext extends ParserRuleContext {
		public List<RelationalExpressionContext> relationalExpression() {
			return getRuleContexts(RelationalExpressionContext.class);
		}
		public RelationalExpressionContext relationalExpression(int i) {
			return getRuleContext(RelationalExpressionContext.class,i);
		}
		public List<TerminalNode> Equal() { return getTokens(CPP14Parser.Equal); }
		public TerminalNode Equal(int i) {
			return getToken(CPP14Parser.Equal, i);
		}
		public List<TerminalNode> NotEqual() { return getTokens(CPP14Parser.NotEqual); }
		public TerminalNode NotEqual(int i) {
			return getToken(CPP14Parser.NotEqual, i);
		}
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEqualityExpression(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_equalityExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(765);
			relationalExpression();
			setState(770);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Equal || _la==NotEqual) {
				{
				{
				setState(766);
				_la = _input.LA(1);
				if ( !(_la==Equal || _la==NotEqual) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(767);
				relationalExpression();
				}
				}
				setState(772);
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
	public static class AndExpressionContext extends ParserRuleContext {
		public List<EqualityExpressionContext> equalityExpression() {
			return getRuleContexts(EqualityExpressionContext.class);
		}
		public EqualityExpressionContext equalityExpression(int i) {
			return getRuleContext(EqualityExpressionContext.class,i);
		}
		public List<TerminalNode> And() { return getTokens(CPP14Parser.And); }
		public TerminalNode And(int i) {
			return getToken(CPP14Parser.And, i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAndExpression(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			equalityExpression();
			setState(778);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==And) {
				{
				{
				setState(774);
				match(And);
				setState(775);
				equalityExpression();
				}
				}
				setState(780);
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
	public static class ExclusiveOrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}
		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class,i);
		}
		public List<TerminalNode> Caret() { return getTokens(CPP14Parser.Caret); }
		public TerminalNode Caret(int i) {
			return getToken(CPP14Parser.Caret, i);
		}
		public ExclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusiveOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExclusiveOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExclusiveOrExpression(this);
		}
	}

	public final ExclusiveOrExpressionContext exclusiveOrExpression() throws RecognitionException {
		ExclusiveOrExpressionContext _localctx = new ExclusiveOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_exclusiveOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
			andExpression();
			setState(786);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Caret) {
				{
				{
				setState(782);
				match(Caret);
				setState(783);
				andExpression();
				}
				}
				setState(788);
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
	public static class InclusiveOrExpressionContext extends ParserRuleContext {
		public List<ExclusiveOrExpressionContext> exclusiveOrExpression() {
			return getRuleContexts(ExclusiveOrExpressionContext.class);
		}
		public ExclusiveOrExpressionContext exclusiveOrExpression(int i) {
			return getRuleContext(ExclusiveOrExpressionContext.class,i);
		}
		public List<TerminalNode> Or() { return getTokens(CPP14Parser.Or); }
		public TerminalNode Or(int i) {
			return getToken(CPP14Parser.Or, i);
		}
		public InclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inclusiveOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInclusiveOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInclusiveOrExpression(this);
		}
	}

	public final InclusiveOrExpressionContext inclusiveOrExpression() throws RecognitionException {
		InclusiveOrExpressionContext _localctx = new InclusiveOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_inclusiveOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(789);
			exclusiveOrExpression();
			setState(794);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Or) {
				{
				{
				setState(790);
				match(Or);
				setState(791);
				exclusiveOrExpression();
				}
				}
				setState(796);
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
	public static class LogicalAndExpressionContext extends ParserRuleContext {
		public List<InclusiveOrExpressionContext> inclusiveOrExpression() {
			return getRuleContexts(InclusiveOrExpressionContext.class);
		}
		public InclusiveOrExpressionContext inclusiveOrExpression(int i) {
			return getRuleContext(InclusiveOrExpressionContext.class,i);
		}
		public List<TerminalNode> AndAnd() { return getTokens(CPP14Parser.AndAnd); }
		public TerminalNode AndAnd(int i) {
			return getToken(CPP14Parser.AndAnd, i);
		}
		public LogicalAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLogicalAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLogicalAndExpression(this);
		}
	}

	public final LogicalAndExpressionContext logicalAndExpression() throws RecognitionException {
		LogicalAndExpressionContext _localctx = new LogicalAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_logicalAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			inclusiveOrExpression();
			setState(802);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AndAnd) {
				{
				{
				setState(798);
				match(AndAnd);
				setState(799);
				inclusiveOrExpression();
				}
				}
				setState(804);
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
	public static class LogicalOrExpressionContext extends ParserRuleContext {
		public List<LogicalAndExpressionContext> logicalAndExpression() {
			return getRuleContexts(LogicalAndExpressionContext.class);
		}
		public LogicalAndExpressionContext logicalAndExpression(int i) {
			return getRuleContext(LogicalAndExpressionContext.class,i);
		}
		public List<TerminalNode> OrOr() { return getTokens(CPP14Parser.OrOr); }
		public TerminalNode OrOr(int i) {
			return getToken(CPP14Parser.OrOr, i);
		}
		public LogicalOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLogicalOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLogicalOrExpression(this);
		}
	}

	public final LogicalOrExpressionContext logicalOrExpression() throws RecognitionException {
		LogicalOrExpressionContext _localctx = new LogicalOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_logicalOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(805);
			logicalAndExpression();
			setState(810);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OrOr) {
				{
				{
				setState(806);
				match(OrOr);
				setState(807);
				logicalAndExpression();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalExpressionContext extends ParserRuleContext {
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public TerminalNode Question() { return getToken(CPP14Parser.Question, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConditionalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConditionalExpression(this);
		}
	}

	public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
		ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_conditionalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
			logicalOrExpression();
			setState(819);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Question) {
				{
				setState(814);
				match(Question);
				setState(815);
				expression();
				setState(816);
				match(Colon);
				setState(817);
				assignmentExpression();
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
	public static class AssignmentExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public AssignmentOperatorContext assignmentOperator() {
			return getRuleContext(AssignmentOperatorContext.class,0);
		}
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public ThrowExpressionContext throwExpression() {
			return getRuleContext(ThrowExpressionContext.class,0);
		}
		public AssignmentExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAssignmentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAssignmentExpression(this);
		}
	}

	public final AssignmentExpressionContext assignmentExpression() throws RecognitionException {
		AssignmentExpressionContext _localctx = new AssignmentExpressionContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_assignmentExpression);
		try {
			setState(827);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(821);
				conditionalExpression();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(822);
				logicalOrExpression();
				setState(823);
				assignmentOperator();
				setState(824);
				initializerClause();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
				throwExpression();
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
	public static class AssignmentOperatorContext extends ParserRuleContext {
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode StarAssign() { return getToken(CPP14Parser.StarAssign, 0); }
		public TerminalNode DivAssign() { return getToken(CPP14Parser.DivAssign, 0); }
		public TerminalNode ModAssign() { return getToken(CPP14Parser.ModAssign, 0); }
		public TerminalNode PlusAssign() { return getToken(CPP14Parser.PlusAssign, 0); }
		public TerminalNode MinusAssign() { return getToken(CPP14Parser.MinusAssign, 0); }
		public TerminalNode RightShiftAssign() { return getToken(CPP14Parser.RightShiftAssign, 0); }
		public TerminalNode LeftShiftAssign() { return getToken(CPP14Parser.LeftShiftAssign, 0); }
		public TerminalNode AndAssign() { return getToken(CPP14Parser.AndAssign, 0); }
		public TerminalNode XorAssign() { return getToken(CPP14Parser.XorAssign, 0); }
		public TerminalNode OrAssign() { return getToken(CPP14Parser.OrAssign, 0); }
		public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAssignmentOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAssignmentOperator(this);
		}
	}

	public final AssignmentOperatorContext assignmentOperator() throws RecognitionException {
		AssignmentOperatorContext _localctx = new AssignmentOperatorContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_assignmentOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(829);
			_la = _input.LA(1);
			if ( !(((((_la - 101)) & ~0x3f) == 0 && ((1L << (_la - 101)) & 8185L) != 0)) ) {
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
	public static class ExpressionContext extends ParserRuleContext {
		public List<AssignmentExpressionContext> assignmentExpression() {
			return getRuleContexts(AssignmentExpressionContext.class);
		}
		public AssignmentExpressionContext assignmentExpression(int i) {
			return getRuleContext(AssignmentExpressionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(831);
			assignmentExpression();
			setState(836);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(832);
				match(Comma);
				setState(833);
				assignmentExpression();
				}
				}
				setState(838);
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
	public static class ConstantExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConstantExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConstantExpression(this);
		}
	}

	public final ConstantExpressionContext constantExpression() throws RecognitionException {
		ConstantExpressionContext _localctx = new ConstantExpressionContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_constantExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(839);
			conditionalExpression();
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
		public LabeledStatementContext labeledStatement() {
			return getRuleContext(LabeledStatementContext.class,0);
		}
		public DeclarationStatementContext declarationStatement() {
			return getRuleContext(DeclarationStatementContext.class,0);
		}
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public SelectionStatementContext selectionStatement() {
			return getRuleContext(SelectionStatementContext.class,0);
		}
		public IterationStatementContext iterationStatement() {
			return getRuleContext(IterationStatementContext.class,0);
		}
		public JumpStatementContext jumpStatement() {
			return getRuleContext(JumpStatementContext.class,0);
		}
		public TryBlockContext tryBlock() {
			return getRuleContext(TryBlockContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_statement);
		try {
			setState(854);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(841);
				labeledStatement();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(842);
				declarationStatement();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(844);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
				case 1:
					{
					setState(843);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(852);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IntegerLiteral:
				case CharacterLiteral:
				case FloatingLiteral:
				case StringLiteral:
				case BooleanLiteral:
				case PointerLiteral:
				case UserDefinedLiteral:
				case Alignof:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Const_cast:
				case Decltype:
				case Delete:
				case Double:
				case Dynamic_cast:
				case Float:
				case Int:
				case Long:
				case New:
				case Noexcept:
				case Operator:
				case Reinterpret_cast:
				case Short:
				case Signed:
				case Sizeof:
				case Static_cast:
				case This:
				case Throw:
				case Typeid_:
				case Typename_:
				case Unsigned:
				case Void:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
				case PlusPlus:
				case MinusMinus:
				case Doublecolon:
				case Semi:
				case Identifier:
					{
					setState(846);
					expressionStatement();
					}
					break;
				case LeftBrace:
					{
					setState(847);
					compoundStatement();
					}
					break;
				case If:
				case Switch:
					{
					setState(848);
					selectionStatement();
					}
					break;
				case Do:
				case For:
				case While:
					{
					setState(849);
					iterationStatement();
					}
					break;
				case Break:
				case Continue:
				case Goto:
				case Return:
					{
					setState(850);
					jumpStatement();
					}
					break;
				case Try:
					{
					setState(851);
					tryBlock();
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
	public static class LabeledStatementContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Case() { return getToken(CPP14Parser.Case, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Default() { return getToken(CPP14Parser.Default, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public LabeledStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labeledStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLabeledStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLabeledStatement(this);
		}
	}

	public final LabeledStatementContext labeledStatement() throws RecognitionException {
		LabeledStatementContext _localctx = new LabeledStatementContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_labeledStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(857);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(856);
				attributeSpecifierSeq();
				}
			}

			setState(863);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				{
				setState(859);
				match(Identifier);
				}
				break;
			case Case:
				{
				setState(860);
				match(Case);
				setState(861);
				constantExpression();
				}
				break;
			case Default:
				{
				setState(862);
				match(Default);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(865);
			match(Colon);
			setState(866);
			statement();
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
	public static class ExpressionStatementContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpressionStatement(this);
		}
	}

	public final ExpressionStatementContext expressionStatement() throws RecognitionException {
		ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_expressionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(869);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0) || _la==Identifier) {
				{
				setState(868);
				expression();
				}
			}

			setState(871);
			match(Semi);
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
	public static class CompoundStatementContext extends ParserRuleContext {
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public StatementSeqContext statementSeq() {
			return getRuleContext(StatementSeqContext.class,0);
		}
		public CompoundStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCompoundStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCompoundStatement(this);
		}
	}

	public final CompoundStatementContext compoundStatement() throws RecognitionException {
		CompoundStatementContext _localctx = new CompoundStatementContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_compoundStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(873);
			match(LeftBrace);
			setState(875);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -137360239606498050L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -8989184726396829969L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 25L) != 0)) {
				{
				setState(874);
				statementSeq();
				}
			}

			setState(877);
			match(RightBrace);
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
	public static class StatementSeqContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStatementSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStatementSeq(this);
		}
	}

	public final StatementSeqContext statementSeq() throws RecognitionException {
		StatementSeqContext _localctx = new StatementSeqContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_statementSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(879);
				statement();
				}
				}
				setState(882); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -137360239606498050L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -8989184726396829969L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 25L) != 0) );
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
	public static class SelectionStatementContext extends ParserRuleContext {
		public TerminalNode If() { return getToken(CPP14Parser.If, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode Else() { return getToken(CPP14Parser.Else, 0); }
		public TerminalNode Switch() { return getToken(CPP14Parser.Switch, 0); }
		public SelectionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSelectionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSelectionStatement(this);
		}
	}

	public final SelectionStatementContext selectionStatement() throws RecognitionException {
		SelectionStatementContext _localctx = new SelectionStatementContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_selectionStatement);
		try {
			setState(899);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case If:
				enterOuterAlt(_localctx, 1);
				{
				setState(884);
				match(If);
				setState(885);
				match(LeftParen);
				setState(886);
				condition();
				setState(887);
				match(RightParen);
				setState(888);
				statement();
				setState(891);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(889);
					match(Else);
					setState(890);
					statement();
					}
					break;
				}
				}
				break;
			case Switch:
				enterOuterAlt(_localctx, 2);
				{
				setState(893);
				match(Switch);
				setState(894);
				match(LeftParen);
				setState(895);
				condition();
				setState(896);
				match(RightParen);
				setState(897);
				statement();
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
	public static class ConditionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_condition);
		int _la;
		try {
			setState(912);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(901);
				expression();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(903);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(902);
					attributeSpecifierSeq();
					}
				}

				setState(905);
				declSpecifierSeq();
				setState(906);
				declarator();
				setState(910);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Assign:
					{
					setState(907);
					match(Assign);
					setState(908);
					initializerClause();
					}
					break;
				case LeftBrace:
					{
					setState(909);
					bracedInitList();
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
	public static class IterationStatementContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(CPP14Parser.While, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode Do() { return getToken(CPP14Parser.Do, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode For() { return getToken(CPP14Parser.For, 0); }
		public ForInitStatementContext forInitStatement() {
			return getRuleContext(ForInitStatementContext.class,0);
		}
		public ForRangeDeclarationContext forRangeDeclaration() {
			return getRuleContext(ForRangeDeclarationContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public ForRangeInitializerContext forRangeInitializer() {
			return getRuleContext(ForRangeInitializerContext.class,0);
		}
		public IterationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iterationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterIterationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitIterationStatement(this);
		}
	}

	public final IterationStatementContext iterationStatement() throws RecognitionException {
		IterationStatementContext _localctx = new IterationStatementContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_iterationStatement);
		int _la;
		try {
			setState(947);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case While:
				enterOuterAlt(_localctx, 1);
				{
				setState(914);
				match(While);
				setState(915);
				match(LeftParen);
				setState(916);
				condition();
				setState(917);
				match(RightParen);
				setState(918);
				statement();
				}
				break;
			case Do:
				enterOuterAlt(_localctx, 2);
				{
				setState(920);
				match(Do);
				setState(921);
				statement();
				setState(922);
				match(While);
				setState(923);
				match(LeftParen);
				setState(924);
				expression();
				setState(925);
				match(RightParen);
				setState(926);
				match(Semi);
				}
				break;
			case For:
				enterOuterAlt(_localctx, 3);
				{
				setState(928);
				match(For);
				setState(929);
				match(LeftParen);
				setState(942);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(930);
					forInitStatement();
					setState(932);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -714116761242538754L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384301683L) != 0) || _la==Identifier) {
						{
						setState(931);
						condition();
						}
					}

					setState(934);
					match(Semi);
					setState(936);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0) || _la==Identifier) {
						{
						setState(935);
						expression();
						}
					}

					}
					break;

				case 2:
					{
					setState(938);
					forRangeDeclaration();
					setState(939);
					match(Colon);
					setState(940);
					forRangeInitializer();
					}
					break;
				}
				setState(944);
				match(RightParen);
				setState(945);
				statement();
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
	public static class ForInitStatementContext extends ParserRuleContext {
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public SimpleDeclarationContext simpleDeclaration() {
			return getRuleContext(SimpleDeclarationContext.class,0);
		}
		public ForInitStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInitStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForInitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForInitStatement(this);
		}
	}

	public final ForInitStatementContext forInitStatement() throws RecognitionException {
		ForInitStatementContext _localctx = new ForInitStatementContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_forInitStatement);
		try {
			setState(951);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(949);
				expressionStatement();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(950);
				simpleDeclaration();
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
	public static class ForRangeDeclarationContext extends ParserRuleContext {
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ForRangeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForRangeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForRangeDeclaration(this);
		}
	}

	public final ForRangeDeclarationContext forRangeDeclaration() throws RecognitionException {
		ForRangeDeclarationContext _localctx = new ForRangeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_forRangeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(953);
				attributeSpecifierSeq();
				}
			}

			setState(956);
			declSpecifierSeq();
			setState(957);
			declarator();
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
	public static class ForRangeInitializerContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ForRangeInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForRangeInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForRangeInitializer(this);
		}
	}

	public final ForRangeInitializerContext forRangeInitializer() throws RecognitionException {
		ForRangeInitializerContext _localctx = new ForRangeInitializerContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_forRangeInitializer);
		try {
			setState(961);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case Alignof:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Const_cast:
			case Decltype:
			case Delete:
			case Double:
			case Dynamic_cast:
			case Float:
			case Int:
			case Long:
			case New:
			case Noexcept:
			case Operator:
			case Reinterpret_cast:
			case Short:
			case Signed:
			case Sizeof:
			case Static_cast:
			case This:
			case Throw:
			case Typeid_:
			case Typename_:
			case Unsigned:
			case Void:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Plus:
			case Minus:
			case Star:
			case And:
			case Or:
			case Tilde:
			case Not:
			case PlusPlus:
			case MinusMinus:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(959);
				expression();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(960);
				bracedInitList();
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
	public static class JumpStatementContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Break() { return getToken(CPP14Parser.Break, 0); }
		public TerminalNode Continue() { return getToken(CPP14Parser.Continue, 0); }
		public TerminalNode Return() { return getToken(CPP14Parser.Return, 0); }
		public TerminalNode Goto() { return getToken(CPP14Parser.Goto, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public JumpStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterJumpStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitJumpStatement(this);
		}
	}

	public final JumpStatementContext jumpStatement() throws RecognitionException {
		JumpStatementContext _localctx = new JumpStatementContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_jumpStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(972);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Break:
				{
				setState(963);
				match(Break);
				}
				break;
			case Continue:
				{
				setState(964);
				match(Continue);
				}
				break;
			case Return:
				{
				setState(965);
				match(Return);
				setState(968);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IntegerLiteral:
				case CharacterLiteral:
				case FloatingLiteral:
				case StringLiteral:
				case BooleanLiteral:
				case PointerLiteral:
				case UserDefinedLiteral:
				case Alignof:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Const_cast:
				case Decltype:
				case Delete:
				case Double:
				case Dynamic_cast:
				case Float:
				case Int:
				case Long:
				case New:
				case Noexcept:
				case Operator:
				case Reinterpret_cast:
				case Short:
				case Signed:
				case Sizeof:
				case Static_cast:
				case This:
				case Throw:
				case Typeid_:
				case Typename_:
				case Unsigned:
				case Void:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
				case PlusPlus:
				case MinusMinus:
				case Doublecolon:
				case Identifier:
					{
					setState(966);
					expression();
					}
					break;
				case LeftBrace:
					{
					setState(967);
					bracedInitList();
					}
					break;
				case Semi:
					break;
				default:
					break;
				}
				}
				break;
			case Goto:
				{
				setState(970);
				match(Goto);
				setState(971);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(974);
			match(Semi);
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
	public static class DeclarationStatementContext extends ParserRuleContext {
		public BlockDeclarationContext blockDeclaration() {
			return getRuleContext(BlockDeclarationContext.class,0);
		}
		public DeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarationStatement(this);
		}
	}

	public final DeclarationStatementContext declarationStatement() throws RecognitionException {
		DeclarationStatementContext _localctx = new DeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_declarationStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(976);
			blockDeclaration();
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
	public static class DeclarationseqContext extends ParserRuleContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public DeclarationseqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationseq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarationseq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarationseq(this);
		}
	}

	public final DeclarationseqContext declarationseq() throws RecognitionException {
		DeclarationseqContext _localctx = new DeclarationseqContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_declarationseq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(979); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(978);
				declaration();
				}
				}
				setState(981); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0) );
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
	public static class DeclarationContext extends ParserRuleContext {
		public BlockDeclarationContext blockDeclaration() {
			return getRuleContext(BlockDeclarationContext.class,0);
		}
		public FunctionDefinitionContext functionDefinition() {
			return getRuleContext(FunctionDefinitionContext.class,0);
		}
		public TemplateDeclarationContext templateDeclaration() {
			return getRuleContext(TemplateDeclarationContext.class,0);
		}
		public ExplicitInstantiationContext explicitInstantiation() {
			return getRuleContext(ExplicitInstantiationContext.class,0);
		}
		public ExplicitSpecializationContext explicitSpecialization() {
			return getRuleContext(ExplicitSpecializationContext.class,0);
		}
		public LinkageSpecificationContext linkageSpecification() {
			return getRuleContext(LinkageSpecificationContext.class,0);
		}
		public NamespaceDefinitionContext namespaceDefinition() {
			return getRuleContext(NamespaceDefinitionContext.class,0);
		}
		public EmptyDeclarationContext emptyDeclaration() {
			return getRuleContext(EmptyDeclarationContext.class,0);
		}
		public AttributeDeclarationContext attributeDeclaration() {
			return getRuleContext(AttributeDeclarationContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclaration(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_declaration);
		try {
			setState(992);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(983);
				blockDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(984);
				functionDefinition();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(985);
				templateDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(986);
				explicitInstantiation();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(987);
				explicitSpecialization();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(988);
				linkageSpecification();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(989);
				namespaceDefinition();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(990);
				emptyDeclaration();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(991);
				attributeDeclaration();
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
	public static class BlockDeclarationContext extends ParserRuleContext {
		public SimpleDeclarationContext simpleDeclaration() {
			return getRuleContext(SimpleDeclarationContext.class,0);
		}
		public AsmDefinitionContext asmDefinition() {
			return getRuleContext(AsmDefinitionContext.class,0);
		}
		public NamespaceAliasDefinitionContext namespaceAliasDefinition() {
			return getRuleContext(NamespaceAliasDefinitionContext.class,0);
		}
		public UsingDeclarationContext usingDeclaration() {
			return getRuleContext(UsingDeclarationContext.class,0);
		}
		public UsingDirectiveContext usingDirective() {
			return getRuleContext(UsingDirectiveContext.class,0);
		}
		public StaticAssertDeclarationContext staticAssertDeclaration() {
			return getRuleContext(StaticAssertDeclarationContext.class,0);
		}
		public AliasDeclarationContext aliasDeclaration() {
			return getRuleContext(AliasDeclarationContext.class,0);
		}
		public OpaqueEnumDeclarationContext opaqueEnumDeclaration() {
			return getRuleContext(OpaqueEnumDeclarationContext.class,0);
		}
		public BlockDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBlockDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBlockDeclaration(this);
		}
	}

	public final BlockDeclarationContext blockDeclaration() throws RecognitionException {
		BlockDeclarationContext _localctx = new BlockDeclarationContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_blockDeclaration);
		try {
			setState(1002);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				simpleDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(995);
				asmDefinition();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(996);
				namespaceAliasDefinition();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(997);
				usingDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(998);
				usingDirective();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(999);
				staticAssertDeclaration();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1000);
				aliasDeclaration();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1001);
				opaqueEnumDeclaration();
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
	public static class AliasDeclarationContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public AliasDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAliasDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAliasDeclaration(this);
		}
	}

	public final AliasDeclarationContext aliasDeclaration() throws RecognitionException {
		AliasDeclarationContext _localctx = new AliasDeclarationContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_aliasDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1004);
			match(Using);
			setState(1005);
			match(Identifier);
			setState(1007);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1006);
				attributeSpecifierSeq();
				}
			}

			setState(1009);
			match(Assign);
			setState(1010);
			theTypeId();
			setState(1011);
			match(Semi);
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
	public static class SimpleDeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public InitDeclaratorListContext initDeclaratorList() {
			return getRuleContext(InitDeclaratorListContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public SimpleDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleDeclaration(this);
		}
	}

	public final SimpleDeclarationContext simpleDeclaration() throws RecognitionException {
		SimpleDeclarationContext _localctx = new SimpleDeclarationContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_simpleDeclaration);
		int _la;
		try {
			setState(1027);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Decltype:
			case Double:
			case Enum:
			case Explicit:
			case Extern:
			case Float:
			case Friend:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Operator:
			case Register:
			case Short:
			case Signed:
			case Static:
			case Struct:
			case Thread_local:
			case Typedef:
			case Typename_:
			case Union:
			case Unsigned:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case LeftParen:
			case Star:
			case And:
			case Tilde:
			case AndAnd:
			case Doublecolon:
			case Semi:
			case Ellipsis:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1014);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
				case 1:
					{
					setState(1013);
					declSpecifierSeq();
					}
					break;
				}
				setState(1017);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Operator || ((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 215512868999425L) != 0)) {
					{
					setState(1016);
					initDeclaratorList();
					}
				}

				setState(1019);
				match(Semi);
				}
				break;
			case Alignas:
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(1020);
				attributeSpecifierSeq();
				setState(1022);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
				case 1:
					{
					setState(1021);
					declSpecifierSeq();
					}
					break;
				}
				setState(1024);
				initDeclaratorList();
				setState(1025);
				match(Semi);
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
	public static class StaticAssertDeclarationContext extends ParserRuleContext {
		public TerminalNode Static_assert() { return getToken(CPP14Parser.Static_assert, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public StaticAssertDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_staticAssertDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStaticAssertDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStaticAssertDeclaration(this);
		}
	}

	public final StaticAssertDeclarationContext staticAssertDeclaration() throws RecognitionException {
		StaticAssertDeclarationContext _localctx = new StaticAssertDeclarationContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_staticAssertDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			match(Static_assert);
			setState(1030);
			match(LeftParen);
			setState(1031);
			constantExpression();
			setState(1032);
			match(Comma);
			setState(1033);
			match(StringLiteral);
			setState(1034);
			match(RightParen);
			setState(1035);
			match(Semi);
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
	public static class EmptyDeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public EmptyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEmptyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEmptyDeclaration(this);
		}
	}

	public final EmptyDeclarationContext emptyDeclaration() throws RecognitionException {
		EmptyDeclarationContext _localctx = new EmptyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_emptyDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1037);
			match(Semi);
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
	public static class AttributeDeclarationContext extends ParserRuleContext {
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeDeclaration(this);
		}
	}

	public final AttributeDeclarationContext attributeDeclaration() throws RecognitionException {
		AttributeDeclarationContext _localctx = new AttributeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			attributeSpecifierSeq();
			setState(1040);
			match(Semi);
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
	public static class DeclSpecifierContext extends ParserRuleContext {
		public StorageClassSpecifierContext storageClassSpecifier() {
			return getRuleContext(StorageClassSpecifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public FunctionSpecifierContext functionSpecifier() {
			return getRuleContext(FunctionSpecifierContext.class,0);
		}
		public TerminalNode Friend() { return getToken(CPP14Parser.Friend, 0); }
		public TerminalNode Typedef() { return getToken(CPP14Parser.Typedef, 0); }
		public TerminalNode Constexpr() { return getToken(CPP14Parser.Constexpr, 0); }
		public DeclSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclSpecifier(this);
		}
	}

	public final DeclSpecifierContext declSpecifier() throws RecognitionException {
		DeclSpecifierContext _localctx = new DeclSpecifierContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_declSpecifier);
		try {
			setState(1048);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Extern:
			case Mutable:
			case Register:
			case Static:
			case Thread_local:
				enterOuterAlt(_localctx, 1);
				{
				setState(1042);
				storageClassSpecifier();
				}
				break;
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1043);
				typeSpecifier();
				}
				break;
			case Explicit:
			case Inline:
			case Virtual:
				enterOuterAlt(_localctx, 3);
				{
				setState(1044);
				functionSpecifier();
				}
				break;
			case Friend:
				enterOuterAlt(_localctx, 4);
				{
				setState(1045);
				match(Friend);
				}
				break;
			case Typedef:
				enterOuterAlt(_localctx, 5);
				{
				setState(1046);
				match(Typedef);
				}
				break;
			case Constexpr:
				enterOuterAlt(_localctx, 6);
				{
				setState(1047);
				match(Constexpr);
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
	public static class DeclSpecifierSeqContext extends ParserRuleContext {
		public List<DeclSpecifierContext> declSpecifier() {
			return getRuleContexts(DeclSpecifierContext.class);
		}
		public DeclSpecifierContext declSpecifier(int i) {
			return getRuleContext(DeclSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclSpecifierSeq(this);
		}
	}

	public final DeclSpecifierSeqContext declSpecifierSeq() throws RecognitionException {
		DeclSpecifierSeqContext _localctx = new DeclSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_declSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1051); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(1050);
					declSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1053); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1055);
				attributeSpecifierSeq();
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
	public static class StorageClassSpecifierContext extends ParserRuleContext {
		public TerminalNode Register() { return getToken(CPP14Parser.Register, 0); }
		public TerminalNode Static() { return getToken(CPP14Parser.Static, 0); }
		public TerminalNode Thread_local() { return getToken(CPP14Parser.Thread_local, 0); }
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public TerminalNode Mutable() { return getToken(CPP14Parser.Mutable, 0); }
		public StorageClassSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_storageClassSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStorageClassSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStorageClassSpecifier(this);
		}
	}

	public final StorageClassSpecifierContext storageClassSpecifier() throws RecognitionException {
		StorageClassSpecifierContext _localctx = new StorageClassSpecifierContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_storageClassSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			_la = _input.LA(1);
			if ( !(((((_la - 36)) & ~0x3f) == 0 && ((1L << (_la - 36)) & 17316186113L) != 0)) ) {
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
	public static class FunctionSpecifierContext extends ParserRuleContext {
		public TerminalNode Inline() { return getToken(CPP14Parser.Inline, 0); }
		public TerminalNode Virtual() { return getToken(CPP14Parser.Virtual, 0); }
		public TerminalNode Explicit() { return getToken(CPP14Parser.Explicit, 0); }
		public FunctionSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionSpecifier(this);
		}
	}

	public final FunctionSpecifierContext functionSpecifier() throws RecognitionException {
		FunctionSpecifierContext _localctx = new FunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_functionSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1060);
			_la = _input.LA(1);
			if ( !(((((_la - 34)) & ~0x3f) == 0 && ((1L << (_la - 34)) & 70368744178689L) != 0)) ) {
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
	public static class TypedefNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TypedefNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedefName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypedefName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypedefName(this);
		}
	}

	public final TypedefNameContext typedefName() throws RecognitionException {
		TypedefNameContext _localctx = new TypedefNameContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_typedefName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1062);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TypeSpecifierContext extends ParserRuleContext {
		public TrailingTypeSpecifierContext trailingTypeSpecifier() {
			return getRuleContext(TrailingTypeSpecifierContext.class,0);
		}
		public ClassSpecifierContext classSpecifier() {
			return getRuleContext(ClassSpecifierContext.class,0);
		}
		public EnumSpecifierContext enumSpecifier() {
			return getRuleContext(EnumSpecifierContext.class,0);
		}
		public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeSpecifier(this);
		}
	}

	public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
		TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_typeSpecifier);
		try {
			setState(1067);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1064);
				trailingTypeSpecifier();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1065);
				classSpecifier();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1066);
				enumSpecifier();
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
	public static class TrailingTypeSpecifierContext extends ParserRuleContext {
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public ElaboratedTypeSpecifierContext elaboratedTypeSpecifier() {
			return getRuleContext(ElaboratedTypeSpecifierContext.class,0);
		}
		public TypeNameSpecifierContext typeNameSpecifier() {
			return getRuleContext(TypeNameSpecifierContext.class,0);
		}
		public CvQualifierContext cvQualifier() {
			return getRuleContext(CvQualifierContext.class,0);
		}
		public TrailingTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingTypeSpecifier(this);
		}
	}

	public final TrailingTypeSpecifierContext trailingTypeSpecifier() throws RecognitionException {
		TrailingTypeSpecifierContext _localctx = new TrailingTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_trailingTypeSpecifier);
		try {
			setState(1073);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Decltype:
			case Double:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Unsigned:
			case Void:
			case Wchar:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1069);
				simpleTypeSpecifier();
				}
				break;
			case Class:
			case Enum:
			case Struct:
				enterOuterAlt(_localctx, 2);
				{
				setState(1070);
				elaboratedTypeSpecifier();
				}
				break;
			case Typename_:
				enterOuterAlt(_localctx, 3);
				{
				setState(1071);
				typeNameSpecifier();
				}
				break;
			case Const:
			case Volatile:
				enterOuterAlt(_localctx, 4);
				{
				setState(1072);
				cvQualifier();
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
	public static class TypeSpecifierSeqContext extends ParserRuleContext {
		public List<TypeSpecifierContext> typeSpecifier() {
			return getRuleContexts(TypeSpecifierContext.class);
		}
		public TypeSpecifierContext typeSpecifier(int i) {
			return getRuleContext(TypeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeSpecifierSeq(this);
		}
	}

	public final TypeSpecifierSeqContext typeSpecifierSeq() throws RecognitionException {
		TypeSpecifierSeqContext _localctx = new TypeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_typeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1076); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1075);
					typeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1078); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1081);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1080);
				attributeSpecifierSeq();
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
	public static class TrailingTypeSpecifierSeqContext extends ParserRuleContext {
		public List<TrailingTypeSpecifierContext> trailingTypeSpecifier() {
			return getRuleContexts(TrailingTypeSpecifierContext.class);
		}
		public TrailingTypeSpecifierContext trailingTypeSpecifier(int i) {
			return getRuleContext(TrailingTypeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TrailingTypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingTypeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingTypeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingTypeSpecifierSeq(this);
		}
	}

	public final TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq() throws RecognitionException {
		TrailingTypeSpecifierSeqContext _localctx = new TrailingTypeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_trailingTypeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1084); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1083);
					trailingTypeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1086); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1089);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1088);
				attributeSpecifierSeq();
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
	public static class SimpleTypeLengthModifierContext extends ParserRuleContext {
		public TerminalNode Short() { return getToken(CPP14Parser.Short, 0); }
		public TerminalNode Long() { return getToken(CPP14Parser.Long, 0); }
		public SimpleTypeLengthModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeLengthModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeLengthModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeLengthModifier(this);
		}
	}

	public final SimpleTypeLengthModifierContext simpleTypeLengthModifier() throws RecognitionException {
		SimpleTypeLengthModifierContext _localctx = new SimpleTypeLengthModifierContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_simpleTypeLengthModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091);
			_la = _input.LA(1);
			if ( !(_la==Long || _la==Short) ) {
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
	public static class SimpleTypeSignednessModifierContext extends ParserRuleContext {
		public TerminalNode Unsigned() { return getToken(CPP14Parser.Unsigned, 0); }
		public TerminalNode Signed() { return getToken(CPP14Parser.Signed, 0); }
		public SimpleTypeSignednessModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeSignednessModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeSignednessModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeSignednessModifier(this);
		}
	}

	public final SimpleTypeSignednessModifierContext simpleTypeSignednessModifier() throws RecognitionException {
		SimpleTypeSignednessModifierContext _localctx = new SimpleTypeSignednessModifierContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_simpleTypeSignednessModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			_la = _input.LA(1);
			if ( !(_la==Signed || _la==Unsigned) ) {
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
	public static class SimpleTypeSpecifierContext extends ParserRuleContext {
		public TheTypeNameContext theTypeName() {
			return getRuleContext(TheTypeNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public SimpleTypeSignednessModifierContext simpleTypeSignednessModifier() {
			return getRuleContext(SimpleTypeSignednessModifierContext.class,0);
		}
		public List<SimpleTypeLengthModifierContext> simpleTypeLengthModifier() {
			return getRuleContexts(SimpleTypeLengthModifierContext.class);
		}
		public SimpleTypeLengthModifierContext simpleTypeLengthModifier(int i) {
			return getRuleContext(SimpleTypeLengthModifierContext.class,i);
		}
		public TerminalNode Char() { return getToken(CPP14Parser.Char, 0); }
		public TerminalNode Char16() { return getToken(CPP14Parser.Char16, 0); }
		public TerminalNode Char32() { return getToken(CPP14Parser.Char32, 0); }
		public TerminalNode Wchar() { return getToken(CPP14Parser.Wchar, 0); }
		public TerminalNode Bool() { return getToken(CPP14Parser.Bool, 0); }
		public TerminalNode Int() { return getToken(CPP14Parser.Int, 0); }
		public TerminalNode Float() { return getToken(CPP14Parser.Float, 0); }
		public TerminalNode Double() { return getToken(CPP14Parser.Double, 0); }
		public TerminalNode Void() { return getToken(CPP14Parser.Void, 0); }
		public TerminalNode Auto() { return getToken(CPP14Parser.Auto, 0); }
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public SimpleTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeSpecifier(this);
		}
	}

	public final SimpleTypeSpecifierContext simpleTypeSpecifier() throws RecognitionException {
		SimpleTypeSpecifierContext _localctx = new SimpleTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_simpleTypeSpecifier);
		int _la;
		try {
			int _alt;
			setState(1147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1096);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
				case 1:
					{
					setState(1095);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1098);
				theTypeName();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1099);
				nestedNameSpecifier(0);
				setState(1100);
				match(Template);
				setState(1101);
				simpleTemplateId();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1103);
				simpleTypeSignednessModifier();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1104);
					simpleTypeSignednessModifier();
					}
				}

				setState(1108); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1107);
						simpleTypeLengthModifier();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1110); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,114,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1112);
					simpleTypeSignednessModifier();
					}
				}

				setState(1115);
				match(Char);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1116);
					simpleTypeSignednessModifier();
					}
				}

				setState(1119);
				match(Char16);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1120);
					simpleTypeSignednessModifier();
					}
				}

				setState(1123);
				match(Char32);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1125);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1124);
					simpleTypeSignednessModifier();
					}
				}

				setState(1127);
				match(Wchar);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1128);
				match(Bool);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1129);
					simpleTypeSignednessModifier();
					}
				}

				setState(1135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Long || _la==Short) {
					{
					{
					setState(1132);
					simpleTypeLengthModifier();
					}
					}
					setState(1137);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1138);
				match(Int);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1139);
				match(Float);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1141);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Long || _la==Short) {
					{
					setState(1140);
					simpleTypeLengthModifier();
					}
				}

				setState(1143);
				match(Double);
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1144);
				match(Void);
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1145);
				match(Auto);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1146);
				decltypeSpecifier();
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
	public static class TheTypeNameContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public EnumNameContext enumName() {
			return getRuleContext(EnumNameContext.class,0);
		}
		public TypedefNameContext typedefName() {
			return getRuleContext(TypedefNameContext.class,0);
		}
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TheTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheTypeName(this);
		}
	}

	public final TheTypeNameContext theTypeName() throws RecognitionException {
		TheTypeNameContext _localctx = new TheTypeNameContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_theTypeName);
		try {
			setState(1153);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1149);
				className();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1150);
				enumName();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1151);
				typedefName();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1152);
				simpleTemplateId();
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
	public static class DecltypeSpecifierContext extends ParserRuleContext {
		public TerminalNode Decltype() { return getToken(CPP14Parser.Decltype, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Auto() { return getToken(CPP14Parser.Auto, 0); }
		public DecltypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decltypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDecltypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDecltypeSpecifier(this);
		}
	}

	public final DecltypeSpecifierContext decltypeSpecifier() throws RecognitionException {
		DecltypeSpecifierContext _localctx = new DecltypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_decltypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1155);
			match(Decltype);
			setState(1156);
			match(LeftParen);
			setState(1159);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1157);
				expression();
				}
				break;

			case 2:
				{
				setState(1158);
				match(Auto);
				}
				break;
			}
			setState(1161);
			match(RightParen);
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
	public static class ElaboratedTypeSpecifierContext extends ParserRuleContext {
		public ClassKeyContext classKey() {
			return getRuleContext(ClassKeyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Enum() { return getToken(CPP14Parser.Enum, 0); }
		public ElaboratedTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elaboratedTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterElaboratedTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitElaboratedTypeSpecifier(this);
		}
	}

	public final ElaboratedTypeSpecifierContext elaboratedTypeSpecifier() throws RecognitionException {
		ElaboratedTypeSpecifierContext _localctx = new ElaboratedTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_elaboratedTypeSpecifier);
		int _la;
		try {
			setState(1185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Struct:
				enterOuterAlt(_localctx, 1);
				{
				setState(1163);
				classKey();
				setState(1178);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
				case 1:
					{
					setState(1165);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Alignas || _la==LeftBracket) {
						{
						setState(1164);
						attributeSpecifierSeq();
						}
					}

					setState(1168);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
					case 1:
						{
						setState(1167);
						nestedNameSpecifier(0);
						}
						break;
					}
					setState(1170);
					match(Identifier);
					}
					break;

				case 2:
					{
					setState(1171);
					simpleTemplateId();
					}
					break;

				case 3:
					{
					setState(1172);
					nestedNameSpecifier(0);
					setState(1174);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Template) {
						{
						setState(1173);
						match(Template);
						}
					}

					setState(1176);
					simpleTemplateId();
					}
					break;
				}
				}
				break;
			case Enum:
				enterOuterAlt(_localctx, 2);
				{
				setState(1180);
				match(Enum);
				setState(1182);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
				case 1:
					{
					setState(1181);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1184);
				match(Identifier);
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
	public static class EnumNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumName(this);
		}
	}

	public final EnumNameContext enumName() throws RecognitionException {
		EnumNameContext _localctx = new EnumNameContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_enumName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1187);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EnumSpecifierContext extends ParserRuleContext {
		public EnumHeadContext enumHead() {
			return getRuleContext(EnumHeadContext.class,0);
		}
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public EnumeratorListContext enumeratorList() {
			return getRuleContext(EnumeratorListContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public EnumSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumSpecifier(this);
		}
	}

	public final EnumSpecifierContext enumSpecifier() throws RecognitionException {
		EnumSpecifierContext _localctx = new EnumSpecifierContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_enumSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1189);
			enumHead();
			setState(1190);
			match(LeftBrace);
			setState(1195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(1191);
				enumeratorList();
				setState(1193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1192);
					match(Comma);
					}
				}

				}
			}

			setState(1197);
			match(RightBrace);
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
	public static class EnumHeadContext extends ParserRuleContext {
		public EnumkeyContext enumkey() {
			return getRuleContext(EnumkeyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumbaseContext enumbase() {
			return getRuleContext(EnumbaseContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public EnumHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumHead(this);
		}
	}

	public final EnumHeadContext enumHead() throws RecognitionException {
		EnumHeadContext _localctx = new EnumHeadContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_enumHead);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			enumkey();
			setState(1201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1200);
				attributeSpecifierSeq();
				}
			}

			setState(1207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
				{
				setState(1204);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
				case 1:
					{
					setState(1203);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1206);
				match(Identifier);
				}
			}

			setState(1210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1209);
				enumbase();
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
	public static class OpaqueEnumDeclarationContext extends ParserRuleContext {
		public EnumkeyContext enumkey() {
			return getRuleContext(EnumkeyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public EnumbaseContext enumbase() {
			return getRuleContext(EnumbaseContext.class,0);
		}
		public OpaqueEnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opaqueEnumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOpaqueEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOpaqueEnumDeclaration(this);
		}
	}

	public final OpaqueEnumDeclarationContext opaqueEnumDeclaration() throws RecognitionException {
		OpaqueEnumDeclarationContext _localctx = new OpaqueEnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_opaqueEnumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			enumkey();
			setState(1214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1213);
				attributeSpecifierSeq();
				}
			}

			setState(1216);
			match(Identifier);
			setState(1218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1217);
				enumbase();
				}
			}

			setState(1220);
			match(Semi);
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
	public static class EnumkeyContext extends ParserRuleContext {
		public TerminalNode Enum() { return getToken(CPP14Parser.Enum, 0); }
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Struct() { return getToken(CPP14Parser.Struct, 0); }
		public EnumkeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumkey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumkey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumkey(this);
		}
	}

	public final EnumkeyContext enumkey() throws RecognitionException {
		EnumkeyContext _localctx = new EnumkeyContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_enumkey);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1222);
			match(Enum);
			setState(1224);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Class || _la==Struct) {
				{
				setState(1223);
				_la = _input.LA(1);
				if ( !(_la==Class || _la==Struct) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
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
	public static class EnumbaseContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public EnumbaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumbase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumbase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumbase(this);
		}
	}

	public final EnumbaseContext enumbase() throws RecognitionException {
		EnumbaseContext _localctx = new EnumbaseContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_enumbase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1226);
			match(Colon);
			setState(1227);
			typeSpecifierSeq();
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
	public static class EnumeratorListContext extends ParserRuleContext {
		public List<EnumeratorDefinitionContext> enumeratorDefinition() {
			return getRuleContexts(EnumeratorDefinitionContext.class);
		}
		public EnumeratorDefinitionContext enumeratorDefinition(int i) {
			return getRuleContext(EnumeratorDefinitionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public EnumeratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumeratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumeratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumeratorList(this);
		}
	}

	public final EnumeratorListContext enumeratorList() throws RecognitionException {
		EnumeratorListContext _localctx = new EnumeratorListContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_enumeratorList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1229);
			enumeratorDefinition();
			setState(1234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1230);
					match(Comma);
					setState(1231);
					enumeratorDefinition();
					}
					} 
				}
				setState(1236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
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
	public static class EnumeratorDefinitionContext extends ParserRuleContext {
		public EnumeratorContext enumerator() {
			return getRuleContext(EnumeratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public EnumeratorDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumeratorDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumeratorDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumeratorDefinition(this);
		}
	}

	public final EnumeratorDefinitionContext enumeratorDefinition() throws RecognitionException {
		EnumeratorDefinitionContext _localctx = new EnumeratorDefinitionContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_enumeratorDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1237);
			enumerator();
			setState(1240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(1238);
				match(Assign);
				setState(1239);
				constantExpression();
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
	public static class EnumeratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumeratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumerator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumerator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumerator(this);
		}
	}

	public final EnumeratorContext enumerator() throws RecognitionException {
		EnumeratorContext _localctx = new EnumeratorContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_enumerator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1242);
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

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceNameContext extends ParserRuleContext {
		public OriginalNamespaceNameContext originalNamespaceName() {
			return getRuleContext(OriginalNamespaceNameContext.class,0);
		}
		public NamespaceAliasContext namespaceAlias() {
			return getRuleContext(NamespaceAliasContext.class,0);
		}
		public NamespaceNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceName(this);
		}
	}

	public final NamespaceNameContext namespaceName() throws RecognitionException {
		NamespaceNameContext _localctx = new NamespaceNameContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_namespaceName);
		try {
			setState(1246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1244);
				originalNamespaceName();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1245);
				namespaceAlias();
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
	public static class OriginalNamespaceNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OriginalNamespaceNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_originalNamespaceName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOriginalNamespaceName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOriginalNamespaceName(this);
		}
	}

	public final OriginalNamespaceNameContext originalNamespaceName() throws RecognitionException {
		OriginalNamespaceNameContext _localctx = new OriginalNamespaceNameContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_originalNamespaceName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1248);
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

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceDefinitionContext extends ParserRuleContext {
		public DeclarationseqContext namespaceBody;
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public TerminalNode Inline() { return getToken(CPP14Parser.Inline, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OriginalNamespaceNameContext originalNamespaceName() {
			return getRuleContext(OriginalNamespaceNameContext.class,0);
		}
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public NamespaceDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceDefinition(this);
		}
	}

	public final NamespaceDefinitionContext namespaceDefinition() throws RecognitionException {
		NamespaceDefinitionContext _localctx = new NamespaceDefinitionContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_namespaceDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Inline) {
				{
				setState(1250);
				match(Inline);
				}
			}

			setState(1253);
			match(Namespace);
			setState(1256);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				{
				setState(1254);
				match(Identifier);
				}
				break;

			case 2:
				{
				setState(1255);
				originalNamespaceName();
				}
				break;
			}
			setState(1258);
			match(LeftBrace);
			setState(1260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0)) {
				{
				setState(1259);
				((NamespaceDefinitionContext)_localctx).namespaceBody = declarationseq();
				}
			}

			setState(1262);
			match(RightBrace);
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
	public static class NamespaceAliasContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public NamespaceAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceAlias(this);
		}
	}

	public final NamespaceAliasContext namespaceAlias() throws RecognitionException {
		NamespaceAliasContext _localctx = new NamespaceAliasContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_namespaceAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1264);
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

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceAliasDefinitionContext extends ParserRuleContext {
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public QualifiednamespacespecifierContext qualifiednamespacespecifier() {
			return getRuleContext(QualifiednamespacespecifierContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public NamespaceAliasDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceAliasDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceAliasDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceAliasDefinition(this);
		}
	}

	public final NamespaceAliasDefinitionContext namespaceAliasDefinition() throws RecognitionException {
		NamespaceAliasDefinitionContext _localctx = new NamespaceAliasDefinitionContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_namespaceAliasDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1266);
			match(Namespace);
			setState(1267);
			match(Identifier);
			setState(1268);
			match(Assign);
			setState(1269);
			qualifiednamespacespecifier();
			setState(1270);
			match(Semi);
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
	public static class QualifiednamespacespecifierContext extends ParserRuleContext {
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public QualifiednamespacespecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiednamespacespecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterQualifiednamespacespecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitQualifiednamespacespecifier(this);
		}
	}

	public final QualifiednamespacespecifierContext qualifiednamespacespecifier() throws RecognitionException {
		QualifiednamespacespecifierContext _localctx = new QualifiednamespacespecifierContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_qualifiednamespacespecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1273);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
			case 1:
				{
				setState(1272);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1275);
			namespaceName();
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
	public static class UsingDeclarationContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public UsingDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usingDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUsingDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUsingDeclaration(this);
		}
	}

	public final UsingDeclarationContext usingDeclaration() throws RecognitionException {
		UsingDeclarationContext _localctx = new UsingDeclarationContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_usingDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1277);
			match(Using);
			setState(1283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				{
				setState(1279);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Typename_) {
					{
					setState(1278);
					match(Typename_);
					}
				}

				setState(1281);
				nestedNameSpecifier(0);
				}
				}
				break;

			case 2:
				{
				setState(1282);
				match(Doublecolon);
				}
				break;
			}
			setState(1285);
			unqualifiedId();
			setState(1286);
			match(Semi);
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
	public static class UsingDirectiveContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public UsingDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usingDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUsingDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUsingDirective(this);
		}
	}

	public final UsingDirectiveContext usingDirective() throws RecognitionException {
		UsingDirectiveContext _localctx = new UsingDirectiveContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_usingDirective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1288);
				attributeSpecifierSeq();
				}
			}

			setState(1291);
			match(Using);
			setState(1292);
			match(Namespace);
			setState(1294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1293);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1296);
			namespaceName();
			setState(1297);
			match(Semi);
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
	public static class AsmDefinitionContext extends ParserRuleContext {
		public TerminalNode Asm() { return getToken(CPP14Parser.Asm, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AsmDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAsmDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAsmDefinition(this);
		}
	}

	public final AsmDefinitionContext asmDefinition() throws RecognitionException {
		AsmDefinitionContext _localctx = new AsmDefinitionContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_asmDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1299);
			match(Asm);
			setState(1300);
			match(LeftParen);
			setState(1301);
			match(StringLiteral);
			setState(1302);
			match(RightParen);
			setState(1303);
			match(Semi);
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
	public static class LinkageSpecificationContext extends ParserRuleContext {
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public LinkageSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkageSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLinkageSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLinkageSpecification(this);
		}
	}

	public final LinkageSpecificationContext linkageSpecification() throws RecognitionException {
		LinkageSpecificationContext _localctx = new LinkageSpecificationContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_linkageSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1305);
			match(Extern);
			setState(1306);
			match(StringLiteral);
			setState(1313);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
				{
				setState(1307);
				match(LeftBrace);
				setState(1309);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0)) {
					{
					setState(1308);
					declarationseq();
					}
				}

				setState(1311);
				match(RightBrace);
				}
				break;
			case Alignas:
			case Asm:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Decltype:
			case Double:
			case Enum:
			case Explicit:
			case Extern:
			case Float:
			case Friend:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Namespace:
			case Operator:
			case Register:
			case Short:
			case Signed:
			case Static:
			case Static_assert:
			case Struct:
			case Template:
			case Thread_local:
			case Typedef:
			case Typename_:
			case Union:
			case Unsigned:
			case Using:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Star:
			case And:
			case Tilde:
			case AndAnd:
			case Doublecolon:
			case Semi:
			case Ellipsis:
			case Identifier:
				{
				setState(1312);
				declaration();
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
	public static class AttributeSpecifierSeqContext extends ParserRuleContext {
		public List<AttributeSpecifierContext> attributeSpecifier() {
			return getRuleContexts(AttributeSpecifierContext.class);
		}
		public AttributeSpecifierContext attributeSpecifier(int i) {
			return getRuleContext(AttributeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeSpecifierSeq(this);
		}
	}

	public final AttributeSpecifierSeqContext attributeSpecifierSeq() throws RecognitionException {
		AttributeSpecifierSeqContext _localctx = new AttributeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_attributeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1316); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1315);
					attributeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1318); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,153,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeSpecifierContext extends ParserRuleContext {
		public List<TerminalNode> LeftBracket() { return getTokens(CPP14Parser.LeftBracket); }
		public TerminalNode LeftBracket(int i) {
			return getToken(CPP14Parser.LeftBracket, i);
		}
		public List<TerminalNode> RightBracket() { return getTokens(CPP14Parser.RightBracket); }
		public TerminalNode RightBracket(int i) {
			return getToken(CPP14Parser.RightBracket, i);
		}
		public AttributeListContext attributeList() {
			return getRuleContext(AttributeListContext.class,0);
		}
		public AlignmentspecifierContext alignmentspecifier() {
			return getRuleContext(AlignmentspecifierContext.class,0);
		}
		public AttributeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeSpecifier(this);
		}
	}

	public final AttributeSpecifierContext attributeSpecifier() throws RecognitionException {
		AttributeSpecifierContext _localctx = new AttributeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_attributeSpecifier);
		int _la;
		try {
			setState(1328);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBracket:
				enterOuterAlt(_localctx, 1);
				{
				setState(1320);
				match(LeftBracket);
				setState(1321);
				match(LeftBracket);
				setState(1323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1322);
					attributeList();
					}
				}

				setState(1325);
				match(RightBracket);
				setState(1326);
				match(RightBracket);
				}
				break;
			case Alignas:
				enterOuterAlt(_localctx, 2);
				{
				setState(1327);
				alignmentspecifier();
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
	public static class AlignmentspecifierContext extends ParserRuleContext {
		public TerminalNode Alignas() { return getToken(CPP14Parser.Alignas, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public AlignmentspecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alignmentspecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAlignmentspecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAlignmentspecifier(this);
		}
	}

	public final AlignmentspecifierContext alignmentspecifier() throws RecognitionException {
		AlignmentspecifierContext _localctx = new AlignmentspecifierContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_alignmentspecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1330);
			match(Alignas);
			setState(1331);
			match(LeftParen);
			setState(1334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1332);
				theTypeId();
				}
				break;

			case 2:
				{
				setState(1333);
				constantExpression();
				}
				break;
			}
			setState(1337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1336);
				match(Ellipsis);
				}
			}

			setState(1339);
			match(RightParen);
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
	public static class AttributeListContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public AttributeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeList(this);
		}
	}

	public final AttributeListContext attributeList() throws RecognitionException {
		AttributeListContext _localctx = new AttributeListContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_attributeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			attribute();
			setState(1346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1342);
				match(Comma);
				setState(1343);
				attribute();
				}
				}
				setState(1348);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1350);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1349);
				match(Ellipsis);
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
	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeNamespaceContext attributeNamespace() {
			return getRuleContext(AttributeNamespaceContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public AttributeArgumentClauseContext attributeArgumentClause() {
			return getRuleContext(AttributeArgumentClauseContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1355);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				{
				setState(1352);
				attributeNamespace();
				setState(1353);
				match(Doublecolon);
				}
				break;
			}
			setState(1357);
			match(Identifier);
			setState(1359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen) {
				{
				setState(1358);
				attributeArgumentClause();
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
	public static class AttributeNamespaceContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeNamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeNamespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeNamespace(this);
		}
	}

	public final AttributeNamespaceContext attributeNamespace() throws RecognitionException {
		AttributeNamespaceContext _localctx = new AttributeNamespaceContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_attributeNamespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1361);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeArgumentClauseContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BalancedTokenSeqContext balancedTokenSeq() {
			return getRuleContext(BalancedTokenSeqContext.class,0);
		}
		public AttributeArgumentClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeArgumentClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeArgumentClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeArgumentClause(this);
		}
	}

	public final AttributeArgumentClauseContext attributeArgumentClause() throws RecognitionException {
		AttributeArgumentClauseContext _localctx = new AttributeArgumentClauseContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_attributeArgumentClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1363);
			match(LeftParen);
			setState(1365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -88080385L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 262143L) != 0)) {
				{
				setState(1364);
				balancedTokenSeq();
				}
			}

			setState(1367);
			match(RightParen);
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
	public static class BalancedTokenSeqContext extends ParserRuleContext {
		public List<BalancedtokenContext> balancedtoken() {
			return getRuleContexts(BalancedtokenContext.class);
		}
		public BalancedtokenContext balancedtoken(int i) {
			return getRuleContext(BalancedtokenContext.class,i);
		}
		public BalancedTokenSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_balancedTokenSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBalancedTokenSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBalancedTokenSeq(this);
		}
	}

	public final BalancedTokenSeqContext balancedTokenSeq() throws RecognitionException {
		BalancedTokenSeqContext _localctx = new BalancedTokenSeqContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_balancedTokenSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1369);
				balancedtoken();
				}
				}
				setState(1372); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -2L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -88080385L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 262143L) != 0) );
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
	public static class BalancedtokenContext extends ParserRuleContext {
		public List<TerminalNode> LeftParen() { return getTokens(CPP14Parser.LeftParen); }
		public TerminalNode LeftParen(int i) {
			return getToken(CPP14Parser.LeftParen, i);
		}
		public BalancedTokenSeqContext balancedTokenSeq() {
			return getRuleContext(BalancedTokenSeqContext.class,0);
		}
		public List<TerminalNode> RightParen() { return getTokens(CPP14Parser.RightParen); }
		public TerminalNode RightParen(int i) {
			return getToken(CPP14Parser.RightParen, i);
		}
		public List<TerminalNode> LeftBracket() { return getTokens(CPP14Parser.LeftBracket); }
		public TerminalNode LeftBracket(int i) {
			return getToken(CPP14Parser.LeftBracket, i);
		}
		public List<TerminalNode> RightBracket() { return getTokens(CPP14Parser.RightBracket); }
		public TerminalNode RightBracket(int i) {
			return getToken(CPP14Parser.RightBracket, i);
		}
		public List<TerminalNode> LeftBrace() { return getTokens(CPP14Parser.LeftBrace); }
		public TerminalNode LeftBrace(int i) {
			return getToken(CPP14Parser.LeftBrace, i);
		}
		public List<TerminalNode> RightBrace() { return getTokens(CPP14Parser.RightBrace); }
		public TerminalNode RightBrace(int i) {
			return getToken(CPP14Parser.RightBrace, i);
		}
		public BalancedtokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_balancedtoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBalancedtoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBalancedtoken(this);
		}
	}

	public final BalancedtokenContext balancedtoken() throws RecognitionException {
		BalancedtokenContext _localctx = new BalancedtokenContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_balancedtoken);
		int _la;
		try {
			int _alt;
			setState(1391);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				enterOuterAlt(_localctx, 1);
				{
				setState(1374);
				match(LeftParen);
				setState(1375);
				balancedTokenSeq();
				setState(1376);
				match(RightParen);
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(1378);
				match(LeftBracket);
				setState(1379);
				balancedTokenSeq();
				setState(1380);
				match(RightBracket);
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 3);
				{
				setState(1382);
				match(LeftBrace);
				setState(1383);
				balancedTokenSeq();
				setState(1384);
				match(RightBrace);
				}
				break;
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case MultiLineMacro:
			case Directive:
			case Alignas:
			case Alignof:
			case Asm:
			case Auto:
			case Bool:
			case Break:
			case Case:
			case Catch:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Const_cast:
			case Continue:
			case Decltype:
			case Default:
			case Delete:
			case Do:
			case Double:
			case Dynamic_cast:
			case Else:
			case Enum:
			case Explicit:
			case Export:
			case Extern:
			case False_:
			case Final:
			case Float:
			case For:
			case Friend:
			case Goto:
			case If:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Namespace:
			case New:
			case Noexcept:
			case Nullptr:
			case Operator:
			case Override:
			case Private:
			case Protected:
			case Public:
			case Register:
			case Reinterpret_cast:
			case Return:
			case Short:
			case Signed:
			case Sizeof:
			case Static:
			case Static_assert:
			case Static_cast:
			case Struct:
			case Switch:
			case Template:
			case This:
			case Thread_local:
			case Throw:
			case True_:
			case Try:
			case Typedef:
			case Typeid_:
			case Typename_:
			case Union:
			case Unsigned:
			case Using:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case While:
			case Plus:
			case Minus:
			case Star:
			case Div:
			case Mod:
			case Caret:
			case And:
			case Or:
			case Tilde:
			case Not:
			case Assign:
			case Less:
			case Greater:
			case PlusAssign:
			case MinusAssign:
			case StarAssign:
			case DivAssign:
			case ModAssign:
			case XorAssign:
			case AndAssign:
			case OrAssign:
			case LeftShiftAssign:
			case RightShiftAssign:
			case Equal:
			case NotEqual:
			case LessEqual:
			case GreaterEqual:
			case AndAnd:
			case OrOr:
			case PlusPlus:
			case MinusMinus:
			case Comma:
			case ArrowStar:
			case Arrow:
			case Question:
			case Colon:
			case Doublecolon:
			case Semi:
			case Dot:
			case DotStar:
			case Ellipsis:
			case Identifier:
			case DecimalLiteral:
			case OctalLiteral:
			case HexadecimalLiteral:
			case BinaryLiteral:
			case Integersuffix:
			case UserDefinedIntegerLiteral:
			case UserDefinedFloatingLiteral:
			case UserDefinedStringLiteral:
			case UserDefinedCharacterLiteral:
			case Whitespace:
			case Newline:
			case BlockComment:
			case LineComment:
				enterOuterAlt(_localctx, 4);
				{
				setState(1387); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1386);
						_la = _input.LA(1);
						if ( _la <= 0 || (((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 63L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1389); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class InitDeclaratorListContext extends ParserRuleContext {
		public List<InitDeclaratorContext> initDeclarator() {
			return getRuleContexts(InitDeclaratorContext.class);
		}
		public InitDeclaratorContext initDeclarator(int i) {
			return getRuleContext(InitDeclaratorContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public InitDeclaratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initDeclaratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitDeclaratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitDeclaratorList(this);
		}
	}

	public final InitDeclaratorListContext initDeclaratorList() throws RecognitionException {
		InitDeclaratorListContext _localctx = new InitDeclaratorListContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_initDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1393);
			initDeclarator();
			setState(1398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1394);
				match(Comma);
				setState(1395);
				initDeclarator();
				}
				}
				setState(1400);
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
	public static class InitDeclaratorContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public InitDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitDeclarator(this);
		}
	}

	public final InitDeclaratorContext initDeclarator() throws RecognitionException {
		InitDeclaratorContext _localctx = new InitDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_initDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1401);
			declarator();
			setState(1403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 65553L) != 0)) {
				{
				setState(1402);
				initializer();
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
	public static class DeclaratorContext extends ParserRuleContext {
		public PointerDeclaratorContext pointerDeclarator() {
			return getRuleContext(PointerDeclaratorContext.class,0);
		}
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public DeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarator(this);
		}
	}

	public final DeclaratorContext declarator() throws RecognitionException {
		DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_declarator);
		try {
			setState(1410);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1405);
				pointerDeclarator();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1406);
				noPointerDeclarator(0);
				setState(1407);
				parametersAndQualifiers();
				setState(1408);
				trailingReturnType();
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
	public static class PointerDeclaratorContext extends ParserRuleContext {
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public List<TerminalNode> Const() { return getTokens(CPP14Parser.Const); }
		public TerminalNode Const(int i) {
			return getToken(CPP14Parser.Const, i);
		}
		public PointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerDeclarator(this);
		}
	}

	public final PointerDeclaratorContext pointerDeclarator() throws RecognitionException {
		PointerDeclaratorContext _localctx = new PointerDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_pointerDeclarator);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,170,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1412);
					pointerOperator();
					setState(1414);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Const) {
						{
						setState(1413);
						match(Const);
						}
					}

					}
					} 
				}
				setState(1420);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,170,_ctx);
			}
			setState(1421);
			noPointerDeclarator(0);
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
	public static class NoPointerDeclaratorContext extends ParserRuleContext {
		public DeclaratoridContext declaratorid() {
			return getRuleContext(DeclaratoridContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public PointerDeclaratorContext pointerDeclarator() {
			return getRuleContext(PointerDeclaratorContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public NoPointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerDeclarator(this);
		}
	}

	public final NoPointerDeclaratorContext noPointerDeclarator() throws RecognitionException {
		return noPointerDeclarator(0);
	}

	private NoPointerDeclaratorContext noPointerDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerDeclaratorContext _localctx = new NoPointerDeclaratorContext(_ctx, _parentState);
		NoPointerDeclaratorContext _prevctx = _localctx;
		int _startState = 230;
		enterRecursionRule(_localctx, 230, RULE_noPointerDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1432);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Operator:
			case Tilde:
			case Doublecolon:
			case Ellipsis:
			case Identifier:
				{
				setState(1424);
				declaratorid();
				setState(1426);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,171,_ctx) ) {
				case 1:
					{
					setState(1425);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;
			case LeftParen:
				{
				setState(1428);
				match(LeftParen);
				setState(1429);
				pointerDeclarator();
				setState(1430);
				match(RightParen);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(1448);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,176,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerDeclarator);
					setState(1434);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(1444);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LeftParen:
						{
						setState(1435);
						parametersAndQualifiers();
						}
						break;
					case LeftBracket:
						{
						setState(1436);
						match(LeftBracket);
						setState(1438);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0) || _la==Identifier) {
							{
							setState(1437);
							constantExpression();
							}
						}

						setState(1440);
						match(RightBracket);
						setState(1442);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
						case 1:
							{
							setState(1441);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1450);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,176,_ctx);
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
	public static class ParametersAndQualifiersContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ParameterDeclarationClauseContext parameterDeclarationClause() {
			return getRuleContext(ParameterDeclarationClauseContext.class,0);
		}
		public CvqualifierseqContext cvqualifierseq() {
			return getRuleContext(CvqualifierseqContext.class,0);
		}
		public RefqualifierContext refqualifier() {
			return getRuleContext(RefqualifierContext.class,0);
		}
		public ExceptionSpecificationContext exceptionSpecification() {
			return getRuleContext(ExceptionSpecificationContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ParametersAndQualifiersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametersAndQualifiers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParametersAndQualifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParametersAndQualifiers(this);
		}
	}

	public final ParametersAndQualifiersContext parametersAndQualifiers() throws RecognitionException {
		ParametersAndQualifiersContext _localctx = new ParametersAndQualifiersContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_parametersAndQualifiers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1451);
			match(LeftParen);
			setState(1453);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1237504995584196377L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 297237575406461917L) != 0)) {
				{
				setState(1452);
				parameterDeclarationClause();
				}
			}

			setState(1455);
			match(RightParen);
			setState(1457);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				{
				setState(1456);
				cvqualifierseq();
				}
				break;
			}
			setState(1460);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				{
				setState(1459);
				refqualifier();
				}
				break;
			}
			setState(1463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,180,_ctx) ) {
			case 1:
				{
				setState(1462);
				exceptionSpecification();
				}
				break;
			}
			setState(1466);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,181,_ctx) ) {
			case 1:
				{
				setState(1465);
				attributeSpecifierSeq();
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
	public static class TrailingReturnTypeContext extends ParserRuleContext {
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq() {
			return getRuleContext(TrailingTypeSpecifierSeqContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TrailingReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingReturnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingReturnType(this);
		}
	}

	public final TrailingReturnTypeContext trailingReturnType() throws RecognitionException {
		TrailingReturnTypeContext _localctx = new TrailingReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_trailingReturnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1468);
			match(Arrow);
			setState(1469);
			trailingTypeSpecifierSeq();
			setState(1471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
			case 1:
				{
				setState(1470);
				abstractDeclarator();
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
	public static class PointerOperatorContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public CvqualifierseqContext cvqualifierseq() {
			return getRuleContext(CvqualifierseqContext.class,0);
		}
		public PointerOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerOperator(this);
		}
	}

	public final PointerOperatorContext pointerOperator() throws RecognitionException {
		PointerOperatorContext _localctx = new PointerOperatorContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_pointerOperator);
		int _la;
		try {
			setState(1487);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case And:
			case AndAnd:
				enterOuterAlt(_localctx, 1);
				{
				setState(1473);
				_la = _input.LA(1);
				if ( !(_la==And || _la==AndAnd) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1475);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,183,_ctx) ) {
				case 1:
					{
					setState(1474);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;
			case Decltype:
			case Star:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1478);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1477);
					nestedNameSpecifier(0);
					}
				}

				setState(1480);
				match(Star);
				setState(1482);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,185,_ctx) ) {
				case 1:
					{
					setState(1481);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(1485);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
				case 1:
					{
					setState(1484);
					cvqualifierseq();
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
	public static class CvqualifierseqContext extends ParserRuleContext {
		public List<CvQualifierContext> cvQualifier() {
			return getRuleContexts(CvQualifierContext.class);
		}
		public CvQualifierContext cvQualifier(int i) {
			return getRuleContext(CvQualifierContext.class,i);
		}
		public CvqualifierseqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cvqualifierseq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCvqualifierseq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCvqualifierseq(this);
		}
	}

	public final CvqualifierseqContext cvqualifierseq() throws RecognitionException {
		CvqualifierseqContext _localctx = new CvqualifierseqContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_cvqualifierseq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1490); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1489);
					cvQualifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1492); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,188,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class CvQualifierContext extends ParserRuleContext {
		public TerminalNode Const() { return getToken(CPP14Parser.Const, 0); }
		public TerminalNode Volatile() { return getToken(CPP14Parser.Volatile, 0); }
		public CvQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cvQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCvQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCvQualifier(this);
		}
	}

	public final CvQualifierContext cvQualifier() throws RecognitionException {
		CvQualifierContext _localctx = new CvQualifierContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_cvQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1494);
			_la = _input.LA(1);
			if ( !(_la==Const || _la==Volatile) ) {
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
	public static class RefqualifierContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public RefqualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refqualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterRefqualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitRefqualifier(this);
		}
	}

	public final RefqualifierContext refqualifier() throws RecognitionException {
		RefqualifierContext _localctx = new RefqualifierContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_refqualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1496);
			_la = _input.LA(1);
			if ( !(_la==And || _la==AndAnd) ) {
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
	public static class DeclaratoridContext extends ParserRuleContext {
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public DeclaratoridContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaratorid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclaratorid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclaratorid(this);
		}
	}

	public final DeclaratoridContext declaratorid() throws RecognitionException {
		DeclaratoridContext _localctx = new DeclaratoridContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_declaratorid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1499);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1498);
				match(Ellipsis);
				}
			}

			setState(1501);
			idExpression();
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
	public static class TheTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TheTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheTypeId(this);
		}
	}

	public final TheTypeIdContext theTypeId() throws RecognitionException {
		TheTypeIdContext _localctx = new TheTypeIdContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_theTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1503);
			typeSpecifierSeq();
			setState(1505);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,190,_ctx) ) {
			case 1:
				{
				setState(1504);
				abstractDeclarator();
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
	public static class AbstractDeclaratorContext extends ParserRuleContext {
		public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
			return getRuleContext(PointerAbstractDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,0);
		}
		public AbstractPackDeclaratorContext abstractPackDeclarator() {
			return getRuleContext(AbstractPackDeclaratorContext.class,0);
		}
		public AbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAbstractDeclarator(this);
		}
	}

	public final AbstractDeclaratorContext abstractDeclarator() throws RecognitionException {
		AbstractDeclaratorContext _localctx = new AbstractDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_abstractDeclarator);
		try {
			setState(1515);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1507);
				pointerAbstractDeclarator();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1509);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,191,_ctx) ) {
				case 1:
					{
					setState(1508);
					noPointerAbstractDeclarator(0);
					}
					break;
				}
				setState(1511);
				parametersAndQualifiers();
				setState(1512);
				trailingReturnType();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1514);
				abstractPackDeclarator();
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
	public static class PointerAbstractDeclaratorContext extends ParserRuleContext {
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public PointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerAbstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerAbstractDeclarator(this);
		}
	}

	public final PointerAbstractDeclaratorContext pointerAbstractDeclarator() throws RecognitionException {
		PointerAbstractDeclaratorContext _localctx = new PointerAbstractDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_pointerAbstractDeclarator);
		int _la;
		try {
			setState(1526);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
			case LeftBracket:
				enterOuterAlt(_localctx, 1);
				{
				setState(1517);
				noPointerAbstractDeclarator(0);
				}
				break;
			case Decltype:
			case Star:
			case And:
			case AndAnd:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1519); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1518);
					pointerOperator();
					}
					}
					setState(1521); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Decltype || ((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 566969237521L) != 0) );
				setState(1524);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,194,_ctx) ) {
				case 1:
					{
					setState(1523);
					noPointerAbstractDeclarator(0);
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
	public static class NoPointerAbstractDeclaratorContext extends ParserRuleContext {
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
			return getRuleContext(PointerAbstractDeclaratorContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public List<NoPointerAbstractDeclaratorContext> noPointerAbstractDeclarator() {
			return getRuleContexts(NoPointerAbstractDeclaratorContext.class);
		}
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int i) {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,i);
		}
		public NoPointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerAbstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerAbstractDeclarator(this);
		}
	}

	public final NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() throws RecognitionException {
		return noPointerAbstractDeclarator(0);
	}

	private NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerAbstractDeclaratorContext _localctx = new NoPointerAbstractDeclaratorContext(_ctx, _parentState);
		NoPointerAbstractDeclaratorContext _prevctx = _localctx;
		int _startState = 252;
		enterRecursionRule(_localctx, 252, RULE_noPointerAbstractDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1542);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,198,_ctx) ) {
			case 1:
				{
				setState(1529);
				parametersAndQualifiers();
				}
				break;

			case 2:
				{
				setState(1530);
				match(LeftBracket);
				setState(1532);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0) || _la==Identifier) {
					{
					setState(1531);
					constantExpression();
					}
				}

				setState(1534);
				match(RightBracket);
				setState(1536);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,197,_ctx) ) {
				case 1:
					{
					setState(1535);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;

			case 3:
				{
				setState(1538);
				match(LeftParen);
				setState(1539);
				pointerAbstractDeclarator();
				setState(1540);
				match(RightParen);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1559);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,202,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerAbstractDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerAbstractDeclarator);
					setState(1544);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(1555);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
					case 1:
						{
						setState(1545);
						parametersAndQualifiers();
						}
						break;

					case 2:
						{
						setState(1546);
						noPointerAbstractDeclarator(0);
						setState(1547);
						match(LeftBracket);
						setState(1549);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0) || _la==Identifier) {
							{
							setState(1548);
							constantExpression();
							}
						}

						setState(1551);
						match(RightBracket);
						setState(1553);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,200,_ctx) ) {
						case 1:
							{
							setState(1552);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					}
					}
					} 
				}
				setState(1561);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,202,_ctx);
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
	public static class AbstractPackDeclaratorContext extends ParserRuleContext {
		public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
			return getRuleContext(NoPointerAbstractPackDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public AbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractPackDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAbstractPackDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAbstractPackDeclarator(this);
		}
	}

	public final AbstractPackDeclaratorContext abstractPackDeclarator() throws RecognitionException {
		AbstractPackDeclaratorContext _localctx = new AbstractPackDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_abstractPackDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1565);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Decltype || ((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 566969237521L) != 0)) {
				{
				{
				setState(1562);
				pointerOperator();
				}
				}
				setState(1567);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1568);
			noPointerAbstractPackDeclarator(0);
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
	public static class NoPointerAbstractPackDeclaratorContext extends ParserRuleContext {
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
			return getRuleContext(NoPointerAbstractPackDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NoPointerAbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerAbstractPackDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerAbstractPackDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerAbstractPackDeclarator(this);
		}
	}

	public final NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() throws RecognitionException {
		return noPointerAbstractPackDeclarator(0);
	}

	private NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerAbstractPackDeclaratorContext _localctx = new NoPointerAbstractPackDeclaratorContext(_ctx, _parentState);
		NoPointerAbstractPackDeclaratorContext _prevctx = _localctx;
		int _startState = 256;
		enterRecursionRule(_localctx, 256, RULE_noPointerAbstractPackDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1571);
			match(Ellipsis);
			}
			_ctx.stop = _input.LT(-1);
			setState(1587);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,207,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerAbstractPackDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerAbstractPackDeclarator);
					setState(1573);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(1583);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LeftParen:
						{
						setState(1574);
						parametersAndQualifiers();
						}
						break;
					case LeftBracket:
						{
						setState(1575);
						match(LeftBracket);
						setState(1577);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0) || _la==Identifier) {
							{
							setState(1576);
							constantExpression();
							}
						}

						setState(1579);
						match(RightBracket);
						setState(1581);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,205,_ctx) ) {
						case 1:
							{
							setState(1580);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1589);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,207,_ctx);
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
	public static class ParameterDeclarationClauseContext extends ParserRuleContext {
		public ParameterDeclarationListContext parameterDeclarationList() {
			return getRuleContext(ParameterDeclarationListContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public ParameterDeclarationClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclarationClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclarationClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclarationClause(this);
		}
	}

	public final ParameterDeclarationClauseContext parameterDeclarationClause() throws RecognitionException {
		ParameterDeclarationClauseContext _localctx = new ParameterDeclarationClauseContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_parameterDeclarationClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1590);
			parameterDeclarationList();
			setState(1595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Comma || _la==Ellipsis) {
				{
				setState(1592);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1591);
					match(Comma);
					}
				}

				setState(1594);
				match(Ellipsis);
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
	public static class ParameterDeclarationListContext extends ParserRuleContext {
		public List<ParameterDeclarationContext> parameterDeclaration() {
			return getRuleContexts(ParameterDeclarationContext.class);
		}
		public ParameterDeclarationContext parameterDeclaration(int i) {
			return getRuleContext(ParameterDeclarationContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public ParameterDeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclarationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclarationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclarationList(this);
		}
	}

	public final ParameterDeclarationListContext parameterDeclarationList() throws RecognitionException {
		ParameterDeclarationListContext _localctx = new ParameterDeclarationListContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_parameterDeclarationList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1597);
			parameterDeclaration();
			setState(1602);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,210,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1598);
					match(Comma);
					setState(1599);
					parameterDeclaration();
					}
					} 
				}
				setState(1604);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,210,_ctx);
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
	public static class ParameterDeclarationContext extends ParserRuleContext {
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclaration(this);
		}
	}

	public final ParameterDeclarationContext parameterDeclaration() throws RecognitionException {
		ParameterDeclarationContext _localctx = new ParameterDeclarationContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_parameterDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1606);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1605);
				attributeSpecifierSeq();
				}
			}

			setState(1608);
			declSpecifierSeq();
			{
			setState(1613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				{
				setState(1609);
				declarator();
				}
				break;

			case 2:
				{
				setState(1611);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
				case 1:
					{
					setState(1610);
					abstractDeclarator();
					}
					break;
				}
				}
				break;
			}
			setState(1617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(1615);
				match(Assign);
				setState(1616);
				initializerClause();
				}
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
	public static class FunctionDefinitionContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public VirtualSpecifierSeqContext virtualSpecifierSeq() {
			return getRuleContext(VirtualSpecifierSeqContext.class,0);
		}
		public FunctionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionDefinition(this);
		}
	}

	public final FunctionDefinitionContext functionDefinition() throws RecognitionException {
		FunctionDefinitionContext _localctx = new FunctionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_functionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1620);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1619);
				attributeSpecifierSeq();
				}
			}

			setState(1623);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
			case 1:
				{
				setState(1622);
				declSpecifierSeq();
				}
				break;
			}
			setState(1625);
			declarator();
			setState(1627);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Final || _la==Override) {
				{
				setState(1626);
				virtualSpecifierSeq();
				}
			}

			setState(1629);
			functionBody();
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
	public static class FunctionBodyContext extends ParserRuleContext {
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public ConstructorInitializerContext constructorInitializer() {
			return getRuleContext(ConstructorInitializerContext.class,0);
		}
		public FunctionTryBlockContext functionTryBlock() {
			return getRuleContext(FunctionTryBlockContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Default() { return getToken(CPP14Parser.Default, 0); }
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionBody(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_functionBody);
		int _la;
		try {
			setState(1639);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
			case Colon:
				enterOuterAlt(_localctx, 1);
				{
				setState(1632);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Colon) {
					{
					setState(1631);
					constructorInitializer();
					}
				}

				setState(1634);
				compoundStatement();
				}
				break;
			case Try:
				enterOuterAlt(_localctx, 2);
				{
				setState(1635);
				functionTryBlock();
				}
				break;
			case Assign:
				enterOuterAlt(_localctx, 3);
				{
				setState(1636);
				match(Assign);
				setState(1637);
				_la = _input.LA(1);
				if ( !(_la==Default || _la==Delete) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1638);
				match(Semi);
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
	public static class InitializerContext extends ParserRuleContext {
		public BraceOrEqualInitializerContext braceOrEqualInitializer() {
			return getRuleContext(BraceOrEqualInitializerContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public InitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializer(this);
		}
	}

	public final InitializerContext initializer() throws RecognitionException {
		InitializerContext _localctx = new InitializerContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_initializer);
		try {
			setState(1646);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
			case Assign:
				enterOuterAlt(_localctx, 1);
				{
				setState(1641);
				braceOrEqualInitializer();
				}
				break;
			case LeftParen:
				enterOuterAlt(_localctx, 2);
				{
				setState(1642);
				match(LeftParen);
				setState(1643);
				expressionList();
				setState(1644);
				match(RightParen);
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
	public static class BraceOrEqualInitializerContext extends ParserRuleContext {
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public BraceOrEqualInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_braceOrEqualInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBraceOrEqualInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBraceOrEqualInitializer(this);
		}
	}

	public final BraceOrEqualInitializerContext braceOrEqualInitializer() throws RecognitionException {
		BraceOrEqualInitializerContext _localctx = new BraceOrEqualInitializerContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_braceOrEqualInitializer);
		try {
			setState(1651);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Assign:
				enterOuterAlt(_localctx, 1);
				{
				setState(1648);
				match(Assign);
				setState(1649);
				initializerClause();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(1650);
				bracedInitList();
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
	public static class InitializerClauseContext extends ParserRuleContext {
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public InitializerClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializerClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializerClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializerClause(this);
		}
	}

	public final InitializerClauseContext initializerClause() throws RecognitionException {
		InitializerClauseContext _localctx = new InitializerClauseContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_initializerClause);
		try {
			setState(1655);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case Alignof:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Const_cast:
			case Decltype:
			case Delete:
			case Double:
			case Dynamic_cast:
			case Float:
			case Int:
			case Long:
			case New:
			case Noexcept:
			case Operator:
			case Reinterpret_cast:
			case Short:
			case Signed:
			case Sizeof:
			case Static_cast:
			case This:
			case Throw:
			case Typeid_:
			case Typename_:
			case Unsigned:
			case Void:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Plus:
			case Minus:
			case Star:
			case And:
			case Or:
			case Tilde:
			case Not:
			case PlusPlus:
			case MinusMinus:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1653);
				assignmentExpression();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(1654);
				bracedInitList();
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
	public static class InitializerListContext extends ParserRuleContext {
		public List<InitializerClauseContext> initializerClause() {
			return getRuleContexts(InitializerClauseContext.class);
		}
		public InitializerClauseContext initializerClause(int i) {
			return getRuleContext(InitializerClauseContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public InitializerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializerList(this);
		}
	}

	public final InitializerListContext initializerList() throws RecognitionException {
		InitializerListContext _localctx = new InitializerListContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_initializerList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1657);
			initializerClause();
			setState(1659);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1658);
				match(Ellipsis);
				}
			}

			setState(1668);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1661);
					match(Comma);
					setState(1662);
					initializerClause();
					setState(1664);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Ellipsis) {
						{
						setState(1663);
						match(Ellipsis);
						}
					}

					}
					} 
				}
				setState(1670);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
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
	public static class BracedInitListContext extends ParserRuleContext {
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public InitializerListContext initializerList() {
			return getRuleContext(InitializerListContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public BracedInitListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracedInitList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBracedInitList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBracedInitList(this);
		}
	}

	public final BracedInitListContext bracedInitList() throws RecognitionException {
		BracedInitListContext _localctx = new BracedInitListContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_bracedInitList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1671);
			match(LeftBrace);
			setState(1676);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0) || _la==Identifier) {
				{
				setState(1672);
				initializerList();
				setState(1674);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1673);
					match(Comma);
					}
				}

				}
			}

			setState(1678);
			match(RightBrace);
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
	public static class ClassNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public ClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_className; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassName(this);
		}
	}

	public final ClassNameContext className() throws RecognitionException {
		ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_className);
		try {
			setState(1682);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1680);
				match(Identifier);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1681);
				simpleTemplateId();
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
	public static class ClassSpecifierContext extends ParserRuleContext {
		public ClassHeadContext classHead() {
			return getRuleContext(ClassHeadContext.class,0);
		}
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public MemberSpecificationContext memberSpecification() {
			return getRuleContext(MemberSpecificationContext.class,0);
		}
		public ClassSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassSpecifier(this);
		}
	}

	public final ClassSpecifierContext classSpecifier() throws RecognitionException {
		ClassSpecifierContext _localctx = new ClassSpecifierContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_classSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1684);
			classHead();
			setState(1685);
			match(LeftBrace);
			setState(1687);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543877313594212121L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 463888353847684093L) != 0)) {
				{
				setState(1686);
				memberSpecification();
				}
			}

			setState(1689);
			match(RightBrace);
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
	public static class ClassHeadContext extends ParserRuleContext {
		public ClassKeyContext classKey() {
			return getRuleContext(ClassKeyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ClassHeadNameContext classHeadName() {
			return getRuleContext(ClassHeadNameContext.class,0);
		}
		public BaseClauseContext baseClause() {
			return getRuleContext(BaseClauseContext.class,0);
		}
		public ClassVirtSpecifierContext classVirtSpecifier() {
			return getRuleContext(ClassVirtSpecifierContext.class,0);
		}
		public TerminalNode Union() { return getToken(CPP14Parser.Union, 0); }
		public ClassHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassHead(this);
		}
	}

	public final ClassHeadContext classHead() throws RecognitionException {
		ClassHeadContext _localctx = new ClassHeadContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_classHead);
		int _la;
		try {
			setState(1714);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Struct:
				enterOuterAlt(_localctx, 1);
				{
				setState(1691);
				classKey();
				setState(1693);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1692);
					attributeSpecifierSeq();
					}
				}

				setState(1699);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1695);
					classHeadName();
					setState(1697);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final) {
						{
						setState(1696);
						classVirtSpecifier();
						}
					}

					}
				}

				setState(1702);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Colon) {
					{
					setState(1701);
					baseClause();
					}
				}

				}
				break;
			case Union:
				enterOuterAlt(_localctx, 2);
				{
				setState(1704);
				match(Union);
				setState(1706);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1705);
					attributeSpecifierSeq();
					}
				}

				setState(1712);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1708);
					classHeadName();
					setState(1710);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final) {
						{
						setState(1709);
						classVirtSpecifier();
						}
					}

					}
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
	public static class ClassHeadNameContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public ClassHeadNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classHeadName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassHeadName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassHeadName(this);
		}
	}

	public final ClassHeadNameContext classHeadName() throws RecognitionException {
		ClassHeadNameContext _localctx = new ClassHeadNameContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_classHeadName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1717);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,238,_ctx) ) {
			case 1:
				{
				setState(1716);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1719);
			className();
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
	public static class ClassVirtSpecifierContext extends ParserRuleContext {
		public TerminalNode Final() { return getToken(CPP14Parser.Final, 0); }
		public ClassVirtSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classVirtSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassVirtSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassVirtSpecifier(this);
		}
	}

	public final ClassVirtSpecifierContext classVirtSpecifier() throws RecognitionException {
		ClassVirtSpecifierContext _localctx = new ClassVirtSpecifierContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_classVirtSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1721);
			match(Final);
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
	public static class ClassKeyContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Struct() { return getToken(CPP14Parser.Struct, 0); }
		public ClassKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassKey(this);
		}
	}

	public final ClassKeyContext classKey() throws RecognitionException {
		ClassKeyContext _localctx = new ClassKeyContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_classKey);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1723);
			_la = _input.LA(1);
			if ( !(_la==Class || _la==Struct) ) {
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
	public static class MemberSpecificationContext extends ParserRuleContext {
		public List<MemberdeclarationContext> memberdeclaration() {
			return getRuleContexts(MemberdeclarationContext.class);
		}
		public MemberdeclarationContext memberdeclaration(int i) {
			return getRuleContext(MemberdeclarationContext.class,i);
		}
		public List<AccessSpecifierContext> accessSpecifier() {
			return getRuleContexts(AccessSpecifierContext.class);
		}
		public AccessSpecifierContext accessSpecifier(int i) {
			return getRuleContext(AccessSpecifierContext.class,i);
		}
		public List<TerminalNode> Colon() { return getTokens(CPP14Parser.Colon); }
		public TerminalNode Colon(int i) {
			return getToken(CPP14Parser.Colon, i);
		}
		public MemberSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberSpecification(this);
		}
	}

	public final MemberSpecificationContext memberSpecification() throws RecognitionException {
		MemberSpecificationContext _localctx = new MemberSpecificationContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_memberSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1729); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(1729);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Alignas:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Class:
				case Const:
				case Constexpr:
				case Decltype:
				case Double:
				case Enum:
				case Explicit:
				case Extern:
				case Float:
				case Friend:
				case Inline:
				case Int:
				case Long:
				case Mutable:
				case Operator:
				case Register:
				case Short:
				case Signed:
				case Static:
				case Static_assert:
				case Struct:
				case Template:
				case Thread_local:
				case Typedef:
				case Typename_:
				case Union:
				case Unsigned:
				case Using:
				case Virtual:
				case Void:
				case Volatile:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Star:
				case And:
				case Tilde:
				case AndAnd:
				case Colon:
				case Doublecolon:
				case Semi:
				case Ellipsis:
				case Identifier:
					{
					setState(1725);
					memberdeclaration();
					}
					break;
				case Private:
				case Protected:
				case Public:
					{
					setState(1726);
					accessSpecifier();
					setState(1727);
					match(Colon);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1731); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543877313594212121L) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 463888353847684093L) != 0) );
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
	public static class MemberdeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public MemberDeclaratorListContext memberDeclaratorList() {
			return getRuleContext(MemberDeclaratorListContext.class,0);
		}
		public FunctionDefinitionContext functionDefinition() {
			return getRuleContext(FunctionDefinitionContext.class,0);
		}
		public UsingDeclarationContext usingDeclaration() {
			return getRuleContext(UsingDeclarationContext.class,0);
		}
		public StaticAssertDeclarationContext staticAssertDeclaration() {
			return getRuleContext(StaticAssertDeclarationContext.class,0);
		}
		public TemplateDeclarationContext templateDeclaration() {
			return getRuleContext(TemplateDeclarationContext.class,0);
		}
		public AliasDeclarationContext aliasDeclaration() {
			return getRuleContext(AliasDeclarationContext.class,0);
		}
		public EmptyDeclarationContext emptyDeclaration() {
			return getRuleContext(EmptyDeclarationContext.class,0);
		}
		public MemberdeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberdeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberdeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberdeclaration(this);
		}
	}

	public final MemberdeclarationContext memberdeclaration() throws RecognitionException {
		MemberdeclarationContext _localctx = new MemberdeclarationContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_memberdeclaration);
		int _la;
		try {
			setState(1749);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,244,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1734);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,241,_ctx) ) {
				case 1:
					{
					setState(1733);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(1737);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,242,_ctx) ) {
				case 1:
					{
					setState(1736);
					declSpecifierSeq();
					}
					break;
				}
				setState(1740);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599694480384L) != 0) || ((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 217711892254981L) != 0)) {
					{
					setState(1739);
					memberDeclaratorList();
					}
				}

				setState(1742);
				match(Semi);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1743);
				functionDefinition();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1744);
				usingDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1745);
				staticAssertDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1746);
				templateDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1747);
				aliasDeclaration();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1748);
				emptyDeclaration();
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
	public static class MemberDeclaratorListContext extends ParserRuleContext {
		public List<MemberDeclaratorContext> memberDeclarator() {
			return getRuleContexts(MemberDeclaratorContext.class);
		}
		public MemberDeclaratorContext memberDeclarator(int i) {
			return getRuleContext(MemberDeclaratorContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public MemberDeclaratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberDeclaratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberDeclaratorList(this);
		}
	}

	public final MemberDeclaratorListContext memberDeclaratorList() throws RecognitionException {
		MemberDeclaratorListContext _localctx = new MemberDeclaratorListContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_memberDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1751);
			memberDeclarator();
			setState(1756);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1752);
				match(Comma);
				setState(1753);
				memberDeclarator();
				}
				}
				setState(1758);
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
	public static class MemberDeclaratorContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public VirtualSpecifierSeqContext virtualSpecifierSeq() {
			return getRuleContext(VirtualSpecifierSeqContext.class,0);
		}
		public PureSpecifierContext pureSpecifier() {
			return getRuleContext(PureSpecifierContext.class,0);
		}
		public BraceOrEqualInitializerContext braceOrEqualInitializer() {
			return getRuleContext(BraceOrEqualInitializerContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public MemberDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberDeclarator(this);
		}
	}

	public final MemberDeclaratorContext memberDeclarator() throws RecognitionException {
		MemberDeclaratorContext _localctx = new MemberDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_memberDeclarator);
		int _la;
		try {
			setState(1779);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,252,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1759);
				declarator();
				setState(1769);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,249,_ctx) ) {
				case 1:
					{
					setState(1761);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final || _la==Override) {
						{
						setState(1760);
						virtualSpecifierSeq();
						}
					}

					setState(1764);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Assign) {
						{
						setState(1763);
						pureSpecifier();
						}
					}

					}
					break;

				case 2:
					{
					setState(1767);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LeftBrace || _la==Assign) {
						{
						setState(1766);
						braceOrEqualInitializer();
						}
					}

					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1772);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1771);
					match(Identifier);
					}
				}

				setState(1775);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1774);
					attributeSpecifierSeq();
					}
				}

				setState(1777);
				match(Colon);
				setState(1778);
				constantExpression();
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
	public static class VirtualSpecifierSeqContext extends ParserRuleContext {
		public List<VirtualSpecifierContext> virtualSpecifier() {
			return getRuleContexts(VirtualSpecifierContext.class);
		}
		public VirtualSpecifierContext virtualSpecifier(int i) {
			return getRuleContext(VirtualSpecifierContext.class,i);
		}
		public VirtualSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_virtualSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterVirtualSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitVirtualSpecifierSeq(this);
		}
	}

	public final VirtualSpecifierSeqContext virtualSpecifierSeq() throws RecognitionException {
		VirtualSpecifierSeqContext _localctx = new VirtualSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_virtualSpecifierSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1782); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1781);
				virtualSpecifier();
				}
				}
				setState(1784); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Final || _la==Override );
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
	public static class VirtualSpecifierContext extends ParserRuleContext {
		public TerminalNode Override() { return getToken(CPP14Parser.Override, 0); }
		public TerminalNode Final() { return getToken(CPP14Parser.Final, 0); }
		public VirtualSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_virtualSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterVirtualSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitVirtualSpecifier(this);
		}
	}

	public final VirtualSpecifierContext virtualSpecifier() throws RecognitionException {
		VirtualSpecifierContext _localctx = new VirtualSpecifierContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_virtualSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1786);
			_la = _input.LA(1);
			if ( !(_la==Final || _la==Override) ) {
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
	public static class PureSpecifierContext extends ParserRuleContext {
		public Token val;
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode OctalLiteral() { return getToken(CPP14Parser.OctalLiteral, 0); }
		public PureSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pureSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPureSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPureSpecifier(this);
		}
	}

	public final PureSpecifierContext pureSpecifier() throws RecognitionException {
		PureSpecifierContext _localctx = new PureSpecifierContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_pureSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1788);
			match(Assign);
			setState(1789);
			((PureSpecifierContext)_localctx).val = match(OctalLiteral);
			if((((PureSpecifierContext)_localctx).val!=null?((PureSpecifierContext)_localctx).val.getText():null).compareTo("0")!=0) throw new InputMismatchException(this);
					
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
	public static class BaseClauseContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public BaseSpecifierListContext baseSpecifierList() {
			return getRuleContext(BaseSpecifierListContext.class,0);
		}
		public BaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseClause(this);
		}
	}

	public final BaseClauseContext baseClause() throws RecognitionException {
		BaseClauseContext _localctx = new BaseClauseContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_baseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1792);
			match(Colon);
			setState(1793);
			baseSpecifierList();
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
	public static class BaseSpecifierListContext extends ParserRuleContext {
		public List<BaseSpecifierContext> baseSpecifier() {
			return getRuleContexts(BaseSpecifierContext.class);
		}
		public BaseSpecifierContext baseSpecifier(int i) {
			return getRuleContext(BaseSpecifierContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public BaseSpecifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseSpecifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseSpecifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseSpecifierList(this);
		}
	}

	public final BaseSpecifierListContext baseSpecifierList() throws RecognitionException {
		BaseSpecifierListContext _localctx = new BaseSpecifierListContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_baseSpecifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1795);
			baseSpecifier();
			setState(1797);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1796);
				match(Ellipsis);
				}
			}

			setState(1806);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1799);
				match(Comma);
				setState(1800);
				baseSpecifier();
				setState(1802);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1801);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1808);
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
	public static class BaseSpecifierContext extends ParserRuleContext {
		public BaseTypeSpecifierContext baseTypeSpecifier() {
			return getRuleContext(BaseTypeSpecifierContext.class,0);
		}
		public TerminalNode Virtual() { return getToken(CPP14Parser.Virtual, 0); }
		public AccessSpecifierContext accessSpecifier() {
			return getRuleContext(AccessSpecifierContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public BaseSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseSpecifier(this);
		}
	}

	public final BaseSpecifierContext baseSpecifier() throws RecognitionException {
		BaseSpecifierContext _localctx = new BaseSpecifierContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_baseSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1810);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1809);
				attributeSpecifierSeq();
				}
			}

			setState(1824);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Doublecolon:
			case Identifier:
				{
				setState(1812);
				baseTypeSpecifier();
				}
				break;
			case Virtual:
				{
				setState(1813);
				match(Virtual);
				setState(1815);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 126100789566373888L) != 0)) {
					{
					setState(1814);
					accessSpecifier();
					}
				}

				setState(1817);
				baseTypeSpecifier();
				}
				break;
			case Private:
			case Protected:
			case Public:
				{
				setState(1818);
				accessSpecifier();
				setState(1820);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Virtual) {
					{
					setState(1819);
					match(Virtual);
					}
				}

				setState(1822);
				baseTypeSpecifier();
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
	public static class ClassOrDeclTypeContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public ClassOrDeclTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrDeclType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassOrDeclType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassOrDeclType(this);
		}
	}

	public final ClassOrDeclTypeContext classOrDeclType() throws RecognitionException {
		ClassOrDeclTypeContext _localctx = new ClassOrDeclTypeContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_classOrDeclType);
		try {
			setState(1831);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,262,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1827);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,261,_ctx) ) {
				case 1:
					{
					setState(1826);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1829);
				className();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1830);
				decltypeSpecifier();
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
	public static class BaseTypeSpecifierContext extends ParserRuleContext {
		public ClassOrDeclTypeContext classOrDeclType() {
			return getRuleContext(ClassOrDeclTypeContext.class,0);
		}
		public BaseTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseTypeSpecifier(this);
		}
	}

	public final BaseTypeSpecifierContext baseTypeSpecifier() throws RecognitionException {
		BaseTypeSpecifierContext _localctx = new BaseTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_baseTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1833);
			classOrDeclType();
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
	public static class AccessSpecifierContext extends ParserRuleContext {
		public TerminalNode Private() { return getToken(CPP14Parser.Private, 0); }
		public TerminalNode Protected() { return getToken(CPP14Parser.Protected, 0); }
		public TerminalNode Public() { return getToken(CPP14Parser.Public, 0); }
		public AccessSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAccessSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAccessSpecifier(this);
		}
	}

	public final AccessSpecifierContext accessSpecifier() throws RecognitionException {
		AccessSpecifierContext _localctx = new AccessSpecifierContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_accessSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1835);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 126100789566373888L) != 0)) ) {
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
	public static class ConversionFunctionIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public ConversionTypeIdContext conversionTypeId() {
			return getRuleContext(ConversionTypeIdContext.class,0);
		}
		public ConversionFunctionIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionFunctionId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionFunctionId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionFunctionId(this);
		}
	}

	public final ConversionFunctionIdContext conversionFunctionId() throws RecognitionException {
		ConversionFunctionIdContext _localctx = new ConversionFunctionIdContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_conversionFunctionId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1837);
			match(Operator);
			setState(1838);
			conversionTypeId();
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
	public static class ConversionTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public ConversionDeclaratorContext conversionDeclarator() {
			return getRuleContext(ConversionDeclaratorContext.class,0);
		}
		public ConversionTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionTypeId(this);
		}
	}

	public final ConversionTypeIdContext conversionTypeId() throws RecognitionException {
		ConversionTypeIdContext _localctx = new ConversionTypeIdContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_conversionTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1840);
			typeSpecifierSeq();
			setState(1842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,263,_ctx) ) {
			case 1:
				{
				setState(1841);
				conversionDeclarator();
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
	public static class ConversionDeclaratorContext extends ParserRuleContext {
		public PointerOperatorContext pointerOperator() {
			return getRuleContext(PointerOperatorContext.class,0);
		}
		public ConversionDeclaratorContext conversionDeclarator() {
			return getRuleContext(ConversionDeclaratorContext.class,0);
		}
		public ConversionDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionDeclarator(this);
		}
	}

	public final ConversionDeclaratorContext conversionDeclarator() throws RecognitionException {
		ConversionDeclaratorContext _localctx = new ConversionDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_conversionDeclarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1844);
			pointerOperator();
			setState(1846);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,264,_ctx) ) {
			case 1:
				{
				setState(1845);
				conversionDeclarator();
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
	public static class ConstructorInitializerContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public MemInitializerListContext memInitializerList() {
			return getRuleContext(MemInitializerListContext.class,0);
		}
		public ConstructorInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConstructorInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConstructorInitializer(this);
		}
	}

	public final ConstructorInitializerContext constructorInitializer() throws RecognitionException {
		ConstructorInitializerContext _localctx = new ConstructorInitializerContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_constructorInitializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1848);
			match(Colon);
			setState(1849);
			memInitializerList();
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
	public static class MemInitializerListContext extends ParserRuleContext {
		public List<MemInitializerContext> memInitializer() {
			return getRuleContexts(MemInitializerContext.class);
		}
		public MemInitializerContext memInitializer(int i) {
			return getRuleContext(MemInitializerContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public MemInitializerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memInitializerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemInitializerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemInitializerList(this);
		}
	}

	public final MemInitializerListContext memInitializerList() throws RecognitionException {
		MemInitializerListContext _localctx = new MemInitializerListContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_memInitializerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1851);
			memInitializer();
			setState(1853);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1852);
				match(Ellipsis);
				}
			}

			setState(1862);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1855);
				match(Comma);
				setState(1856);
				memInitializer();
				setState(1858);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1857);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1864);
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
	public static class MemInitializerContext extends ParserRuleContext {
		public MeminitializeridContext meminitializerid() {
			return getRuleContext(MeminitializeridContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MemInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemInitializer(this);
		}
	}

	public final MemInitializerContext memInitializer() throws RecognitionException {
		MemInitializerContext _localctx = new MemInitializerContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_memInitializer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1865);
			meminitializerid();
			setState(1872);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				{
				setState(1866);
				match(LeftParen);
				setState(1868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0) || _la==Identifier) {
					{
					setState(1867);
					expressionList();
					}
				}

				setState(1870);
				match(RightParen);
				}
				break;
			case LeftBrace:
				{
				setState(1871);
				bracedInitList();
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
	public static class MeminitializeridContext extends ParserRuleContext {
		public ClassOrDeclTypeContext classOrDeclType() {
			return getRuleContext(ClassOrDeclTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public MeminitializeridContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_meminitializerid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMeminitializerid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMeminitializerid(this);
		}
	}

	public final MeminitializeridContext meminitializerid() throws RecognitionException {
		MeminitializeridContext _localctx = new MeminitializeridContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_meminitializerid);
		try {
			setState(1876);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,270,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1874);
				classOrDeclType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1875);
				match(Identifier);
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
	public static class OperatorFunctionIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public TheOperatorContext theOperator() {
			return getRuleContext(TheOperatorContext.class,0);
		}
		public OperatorFunctionIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operatorFunctionId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOperatorFunctionId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOperatorFunctionId(this);
		}
	}

	public final OperatorFunctionIdContext operatorFunctionId() throws RecognitionException {
		OperatorFunctionIdContext _localctx = new OperatorFunctionIdContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_operatorFunctionId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1878);
			match(Operator);
			setState(1879);
			theOperator();
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
	public static class LiteralOperatorIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode UserDefinedStringLiteral() { return getToken(CPP14Parser.UserDefinedStringLiteral, 0); }
		public LiteralOperatorIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalOperatorId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLiteralOperatorId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLiteralOperatorId(this);
		}
	}

	public final LiteralOperatorIdContext literalOperatorId() throws RecognitionException {
		LiteralOperatorIdContext _localctx = new LiteralOperatorIdContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_literalOperatorId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1881);
			match(Operator);
			setState(1885);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringLiteral:
				{
				setState(1882);
				match(StringLiteral);
				setState(1883);
				match(Identifier);
				}
				break;
			case UserDefinedStringLiteral:
				{
				setState(1884);
				match(UserDefinedStringLiteral);
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
	public static class TemplateDeclarationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TemplateparameterListContext templateparameterList() {
			return getRuleContext(TemplateparameterListContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TemplateDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateDeclaration(this);
		}
	}

	public final TemplateDeclarationContext templateDeclaration() throws RecognitionException {
		TemplateDeclarationContext _localctx = new TemplateDeclarationContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_templateDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1887);
			match(Template);
			setState(1888);
			match(Less);
			setState(1889);
			templateparameterList();
			setState(1890);
			match(Greater);
			setState(1891);
			declaration();
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
	public static class TemplateparameterListContext extends ParserRuleContext {
		public List<TemplateParameterContext> templateParameter() {
			return getRuleContexts(TemplateParameterContext.class);
		}
		public TemplateParameterContext templateParameter(int i) {
			return getRuleContext(TemplateParameterContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TemplateparameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateparameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateparameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateparameterList(this);
		}
	}

	public final TemplateparameterListContext templateparameterList() throws RecognitionException {
		TemplateparameterListContext _localctx = new TemplateparameterListContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_templateparameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1893);
			templateParameter();
			setState(1898);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1894);
				match(Comma);
				setState(1895);
				templateParameter();
				}
				}
				setState(1900);
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
	public static class TemplateParameterContext extends ParserRuleContext {
		public TypeParameterContext typeParameter() {
			return getRuleContext(TypeParameterContext.class,0);
		}
		public ParameterDeclarationContext parameterDeclaration() {
			return getRuleContext(ParameterDeclarationContext.class,0);
		}
		public TemplateParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateParameter(this);
		}
	}

	public final TemplateParameterContext templateParameter() throws RecognitionException {
		TemplateParameterContext _localctx = new TemplateParameterContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_templateParameter);
		try {
			setState(1903);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,273,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1901);
				typeParameter();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1902);
				parameterDeclaration();
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
	public static class TypeParameterContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TemplateparameterListContext templateparameterList() {
			return getRuleContext(TemplateparameterListContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeParameter(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_typeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1914);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Template:
				{
				setState(1910);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Template) {
					{
					setState(1905);
					match(Template);
					setState(1906);
					match(Less);
					setState(1907);
					templateparameterList();
					setState(1908);
					match(Greater);
					}
				}

				setState(1912);
				match(Class);
				}
				break;
			case Typename_:
				{
				setState(1913);
				match(Typename_);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1927);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,279,_ctx) ) {
			case 1:
				{
				{
				setState(1917);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1916);
					match(Ellipsis);
					}
				}

				setState(1920);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1919);
					match(Identifier);
					}
				}

				}
				}
				break;

			case 2:
				{
				{
				setState(1923);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1922);
					match(Identifier);
					}
				}

				setState(1925);
				match(Assign);
				setState(1926);
				theTypeId();
				}
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
	public static class SimpleTemplateIdContext extends ParserRuleContext {
		public TemplateNameContext templateName() {
			return getRuleContext(TemplateNameContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public TemplateArgumentListContext templateArgumentList() {
			return getRuleContext(TemplateArgumentListContext.class,0);
		}
		public SimpleTemplateIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTemplateId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTemplateId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTemplateId(this);
		}
	}

	public final SimpleTemplateIdContext simpleTemplateId() throws RecognitionException {
		SimpleTemplateIdContext _localctx = new SimpleTemplateIdContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_simpleTemplateId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1929);
			templateName();
			setState(1930);
			match(Less);
			setState(1932);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979472930990334L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384268307L) != 0) || _la==Identifier) {
				{
				setState(1931);
				templateArgumentList();
				}
			}

			setState(1934);
			match(Greater);
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
	public static class TemplateIdContext extends ParserRuleContext {
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public OperatorFunctionIdContext operatorFunctionId() {
			return getRuleContext(OperatorFunctionIdContext.class,0);
		}
		public LiteralOperatorIdContext literalOperatorId() {
			return getRuleContext(LiteralOperatorIdContext.class,0);
		}
		public TemplateArgumentListContext templateArgumentList() {
			return getRuleContext(TemplateArgumentListContext.class,0);
		}
		public TemplateIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateId(this);
		}
	}

	public final TemplateIdContext templateId() throws RecognitionException {
		TemplateIdContext _localctx = new TemplateIdContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_templateId);
		int _la;
		try {
			setState(1947);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1936);
				simpleTemplateId();
				}
				break;
			case Operator:
				enterOuterAlt(_localctx, 2);
				{
				setState(1939);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,281,_ctx) ) {
				case 1:
					{
					setState(1937);
					operatorFunctionId();
					}
					break;

				case 2:
					{
					setState(1938);
					literalOperatorId();
					}
					break;
				}
				setState(1941);
				match(Less);
				setState(1943);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979472930990334L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384268307L) != 0) || _la==Identifier) {
					{
					setState(1942);
					templateArgumentList();
					}
				}

				setState(1945);
				match(Greater);
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
	public static class TemplateNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TemplateNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateName(this);
		}
	}

	public final TemplateNameContext templateName() throws RecognitionException {
		TemplateNameContext _localctx = new TemplateNameContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_templateName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1949);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateArgumentListContext extends ParserRuleContext {
		public List<TemplateArgumentContext> templateArgument() {
			return getRuleContexts(TemplateArgumentContext.class);
		}
		public TemplateArgumentContext templateArgument(int i) {
			return getRuleContext(TemplateArgumentContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TemplateArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateArgumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateArgumentList(this);
		}
	}

	public final TemplateArgumentListContext templateArgumentList() throws RecognitionException {
		TemplateArgumentListContext _localctx = new TemplateArgumentListContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_templateArgumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1951);
			templateArgument();
			setState(1953);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1952);
				match(Ellipsis);
				}
			}

			setState(1962);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1955);
				match(Comma);
				setState(1956);
				templateArgument();
				setState(1958);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1957);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1964);
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
	public static class TemplateArgumentContext extends ParserRuleContext {
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public TemplateArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateArgument(this);
		}
	}

	public final TemplateArgumentContext templateArgument() throws RecognitionException {
		TemplateArgumentContext _localctx = new TemplateArgumentContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_templateArgument);
		try {
			setState(1968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,287,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1965);
				theTypeId();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1966);
				constantExpression();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1967);
				idExpression();
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
	public static class TypeNameSpecifierContext extends ParserRuleContext {
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TypeNameSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeNameSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeNameSpecifier(this);
		}
	}

	public final TypeNameSpecifierContext typeNameSpecifier() throws RecognitionException {
		TypeNameSpecifierContext _localctx = new TypeNameSpecifierContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_typeNameSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1970);
			match(Typename_);
			setState(1971);
			nestedNameSpecifier(0);
			setState(1977);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,289,_ctx) ) {
			case 1:
				{
				setState(1972);
				match(Identifier);
				}
				break;

			case 2:
				{
				setState(1974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Template) {
					{
					setState(1973);
					match(Template);
					}
				}

				setState(1976);
				simpleTemplateId();
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
	public static class ExplicitInstantiationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public ExplicitInstantiationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitInstantiation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExplicitInstantiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExplicitInstantiation(this);
		}
	}

	public final ExplicitInstantiationContext explicitInstantiation() throws RecognitionException {
		ExplicitInstantiationContext _localctx = new ExplicitInstantiationContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_explicitInstantiation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1980);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Extern) {
				{
				setState(1979);
				match(Extern);
				}
			}

			setState(1982);
			match(Template);
			setState(1983);
			declaration();
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
	public static class ExplicitSpecializationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public ExplicitSpecializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitSpecialization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExplicitSpecialization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExplicitSpecialization(this);
		}
	}

	public final ExplicitSpecializationContext explicitSpecialization() throws RecognitionException {
		ExplicitSpecializationContext _localctx = new ExplicitSpecializationContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_explicitSpecialization);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1985);
			match(Template);
			setState(1986);
			match(Less);
			setState(1987);
			match(Greater);
			setState(1988);
			declaration();
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
	public static class TryBlockContext extends ParserRuleContext {
		public TerminalNode Try() { return getToken(CPP14Parser.Try, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerSeqContext handlerSeq() {
			return getRuleContext(HandlerSeqContext.class,0);
		}
		public TryBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTryBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTryBlock(this);
		}
	}

	public final TryBlockContext tryBlock() throws RecognitionException {
		TryBlockContext _localctx = new TryBlockContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_tryBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1990);
			match(Try);
			setState(1991);
			compoundStatement();
			setState(1992);
			handlerSeq();
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
	public static class FunctionTryBlockContext extends ParserRuleContext {
		public TerminalNode Try() { return getToken(CPP14Parser.Try, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerSeqContext handlerSeq() {
			return getRuleContext(HandlerSeqContext.class,0);
		}
		public ConstructorInitializerContext constructorInitializer() {
			return getRuleContext(ConstructorInitializerContext.class,0);
		}
		public FunctionTryBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionTryBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionTryBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionTryBlock(this);
		}
	}

	public final FunctionTryBlockContext functionTryBlock() throws RecognitionException {
		FunctionTryBlockContext _localctx = new FunctionTryBlockContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_functionTryBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1994);
			match(Try);
			setState(1996);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1995);
				constructorInitializer();
				}
			}

			setState(1998);
			compoundStatement();
			setState(1999);
			handlerSeq();
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
	public static class HandlerSeqContext extends ParserRuleContext {
		public List<HandlerContext> handler() {
			return getRuleContexts(HandlerContext.class);
		}
		public HandlerContext handler(int i) {
			return getRuleContext(HandlerContext.class,i);
		}
		public HandlerSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_handlerSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterHandlerSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitHandlerSeq(this);
		}
	}

	public final HandlerSeqContext handlerSeq() throws RecognitionException {
		HandlerSeqContext _localctx = new HandlerSeqContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_handlerSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2002); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(2001);
					handler();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(2004); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,292,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class HandlerContext extends ParserRuleContext {
		public TerminalNode Catch() { return getToken(CPP14Parser.Catch, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExceptionDeclarationContext exceptionDeclaration() {
			return getRuleContext(ExceptionDeclarationContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_handler; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterHandler(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitHandler(this);
		}
	}

	public final HandlerContext handler() throws RecognitionException {
		HandlerContext _localctx = new HandlerContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_handler);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2006);
			match(Catch);
			setState(2007);
			match(LeftParen);
			setState(2008);
			exceptionDeclaration();
			setState(2009);
			match(RightParen);
			setState(2010);
			compoundStatement();
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
	public static class ExceptionDeclarationContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public ExceptionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExceptionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExceptionDeclaration(this);
		}
	}

	public final ExceptionDeclarationContext exceptionDeclaration() throws RecognitionException {
		ExceptionDeclarationContext _localctx = new ExceptionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_exceptionDeclaration);
		int _la;
		try {
			setState(2021);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Alignas:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case LeftBracket:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(2013);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(2012);
					attributeSpecifierSeq();
					}
				}

				setState(2015);
				typeSpecifierSeq();
				setState(2018);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,294,_ctx) ) {
				case 1:
					{
					setState(2016);
					declarator();
					}
					break;

				case 2:
					{
					setState(2017);
					abstractDeclarator();
					}
					break;
				}
				}
				break;
			case Ellipsis:
				enterOuterAlt(_localctx, 2);
				{
				setState(2020);
				match(Ellipsis);
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
	public static class ThrowExpressionContext extends ParserRuleContext {
		public TerminalNode Throw() { return getToken(CPP14Parser.Throw, 0); }
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public ThrowExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterThrowExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitThrowExpression(this);
		}
	}

	public final ThrowExpressionContext throwExpression() throws RecognitionException {
		ThrowExpressionContext _localctx = new ThrowExpressionContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_throwExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2023);
			match(Throw);
			setState(2025);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0) || _la==Identifier) {
				{
				setState(2024);
				assignmentExpression();
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
	public static class ExceptionSpecificationContext extends ParserRuleContext {
		public DynamicExceptionSpecificationContext dynamicExceptionSpecification() {
			return getRuleContext(DynamicExceptionSpecificationContext.class,0);
		}
		public NoeExceptSpecificationContext noeExceptSpecification() {
			return getRuleContext(NoeExceptSpecificationContext.class,0);
		}
		public ExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExceptionSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExceptionSpecification(this);
		}
	}

	public final ExceptionSpecificationContext exceptionSpecification() throws RecognitionException {
		ExceptionSpecificationContext _localctx = new ExceptionSpecificationContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_exceptionSpecification);
		try {
			setState(2029);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Throw:
				enterOuterAlt(_localctx, 1);
				{
				setState(2027);
				dynamicExceptionSpecification();
				}
				break;
			case Noexcept:
				enterOuterAlt(_localctx, 2);
				{
				setState(2028);
				noeExceptSpecification();
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
	public static class DynamicExceptionSpecificationContext extends ParserRuleContext {
		public TerminalNode Throw() { return getToken(CPP14Parser.Throw, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TypeIdListContext typeIdList() {
			return getRuleContext(TypeIdListContext.class,0);
		}
		public DynamicExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dynamicExceptionSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDynamicExceptionSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDynamicExceptionSpecification(this);
		}
	}

	public final DynamicExceptionSpecificationContext dynamicExceptionSpecification() throws RecognitionException {
		DynamicExceptionSpecificationContext _localctx = new DynamicExceptionSpecificationContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_dynamicExceptionSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2031);
			match(Throw);
			setState(2032);
			match(LeftParen);
			setState(2034);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 13)) & ~0x3f) == 0 && ((1L << (_la - 13)) & -9213942612181769245L) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & 37154696925806707L) != 0)) {
				{
				setState(2033);
				typeIdList();
				}
			}

			setState(2036);
			match(RightParen);
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
	public static class TypeIdListContext extends ParserRuleContext {
		public List<TheTypeIdContext> theTypeId() {
			return getRuleContexts(TheTypeIdContext.class);
		}
		public TheTypeIdContext theTypeId(int i) {
			return getRuleContext(TheTypeIdContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TypeIdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeIdList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeIdList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeIdList(this);
		}
	}

	public final TypeIdListContext typeIdList() throws RecognitionException {
		TypeIdListContext _localctx = new TypeIdListContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_typeIdList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2038);
			theTypeId();
			setState(2040);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(2039);
				match(Ellipsis);
				}
			}

			setState(2049);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(2042);
				match(Comma);
				setState(2043);
				theTypeId();
				setState(2045);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(2044);
					match(Ellipsis);
					}
				}

				}
				}
				setState(2051);
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
	public static class NoeExceptSpecificationContext extends ParserRuleContext {
		public TerminalNode Noexcept() { return getToken(CPP14Parser.Noexcept, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoeExceptSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noeExceptSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoeExceptSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoeExceptSpecification(this);
		}
	}

	public final NoeExceptSpecificationContext noeExceptSpecification() throws RecognitionException {
		NoeExceptSpecificationContext _localctx = new NoeExceptSpecificationContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_noeExceptSpecification);
		try {
			setState(2058);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,302,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2052);
				match(Noexcept);
				setState(2053);
				match(LeftParen);
				setState(2054);
				constantExpression();
				setState(2055);
				match(RightParen);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2057);
				match(Noexcept);
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
	public static class TheOperatorContext extends ParserRuleContext {
		public TerminalNode New() { return getToken(CPP14Parser.New, 0); }
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public TerminalNode Plus() { return getToken(CPP14Parser.Plus, 0); }
		public TerminalNode Minus() { return getToken(CPP14Parser.Minus, 0); }
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public TerminalNode Div() { return getToken(CPP14Parser.Div, 0); }
		public TerminalNode Mod() { return getToken(CPP14Parser.Mod, 0); }
		public TerminalNode Caret() { return getToken(CPP14Parser.Caret, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Or() { return getToken(CPP14Parser.Or, 0); }
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public TerminalNode Not() { return getToken(CPP14Parser.Not, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public TerminalNode GreaterEqual() { return getToken(CPP14Parser.GreaterEqual, 0); }
		public TerminalNode PlusAssign() { return getToken(CPP14Parser.PlusAssign, 0); }
		public TerminalNode MinusAssign() { return getToken(CPP14Parser.MinusAssign, 0); }
		public TerminalNode StarAssign() { return getToken(CPP14Parser.StarAssign, 0); }
		public TerminalNode ModAssign() { return getToken(CPP14Parser.ModAssign, 0); }
		public TerminalNode XorAssign() { return getToken(CPP14Parser.XorAssign, 0); }
		public TerminalNode AndAssign() { return getToken(CPP14Parser.AndAssign, 0); }
		public TerminalNode OrAssign() { return getToken(CPP14Parser.OrAssign, 0); }
		public TerminalNode RightShiftAssign() { return getToken(CPP14Parser.RightShiftAssign, 0); }
		public TerminalNode LeftShiftAssign() { return getToken(CPP14Parser.LeftShiftAssign, 0); }
		public TerminalNode Equal() { return getToken(CPP14Parser.Equal, 0); }
		public TerminalNode NotEqual() { return getToken(CPP14Parser.NotEqual, 0); }
		public TerminalNode LessEqual() { return getToken(CPP14Parser.LessEqual, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public TerminalNode OrOr() { return getToken(CPP14Parser.OrOr, 0); }
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public TerminalNode ArrowStar() { return getToken(CPP14Parser.ArrowStar, 0); }
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TheOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheOperator(this);
		}
	}

	public final TheOperatorContext theOperator() throws RecognitionException {
		TheOperatorContext _localctx = new TheOperatorContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_theOperator);
		try {
			setState(2111);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,305,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2060);
				match(New);
				setState(2063);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,303,_ctx) ) {
				case 1:
					{
					setState(2061);
					match(LeftBracket);
					setState(2062);
					match(RightBracket);
					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2065);
				match(Delete);
				setState(2068);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,304,_ctx) ) {
				case 1:
					{
					setState(2066);
					match(LeftBracket);
					setState(2067);
					match(RightBracket);
					}
					break;
				}
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2070);
				match(Plus);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2071);
				match(Minus);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2072);
				match(Star);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2073);
				match(Div);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2074);
				match(Mod);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(2075);
				match(Caret);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(2076);
				match(And);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(2077);
				match(Or);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(2078);
				match(Tilde);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(2079);
				match(Not);
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(2080);
				match(Assign);
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(2081);
				match(Greater);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(2082);
				match(Less);
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(2083);
				match(GreaterEqual);
				}
				break;

			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(2084);
				match(PlusAssign);
				}
				break;

			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(2085);
				match(MinusAssign);
				}
				break;

			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(2086);
				match(StarAssign);
				}
				break;

			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(2087);
				match(ModAssign);
				}
				break;

			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(2088);
				match(XorAssign);
				}
				break;

			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(2089);
				match(AndAssign);
				}
				break;

			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(2090);
				match(OrAssign);
				}
				break;

			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(2091);
				match(Less);
				setState(2092);
				match(Less);
				}
				break;

			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(2093);
				match(Greater);
				setState(2094);
				match(Greater);
				}
				break;

			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(2095);
				match(RightShiftAssign);
				}
				break;

			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(2096);
				match(LeftShiftAssign);
				}
				break;

			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(2097);
				match(Equal);
				}
				break;

			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(2098);
				match(NotEqual);
				}
				break;

			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(2099);
				match(LessEqual);
				}
				break;

			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(2100);
				match(AndAnd);
				}
				break;

			case 32:
				enterOuterAlt(_localctx, 32);
				{
				setState(2101);
				match(OrOr);
				}
				break;

			case 33:
				enterOuterAlt(_localctx, 33);
				{
				setState(2102);
				match(PlusPlus);
				}
				break;

			case 34:
				enterOuterAlt(_localctx, 34);
				{
				setState(2103);
				match(MinusMinus);
				}
				break;

			case 35:
				enterOuterAlt(_localctx, 35);
				{
				setState(2104);
				match(Comma);
				}
				break;

			case 36:
				enterOuterAlt(_localctx, 36);
				{
				setState(2105);
				match(ArrowStar);
				}
				break;

			case 37:
				enterOuterAlt(_localctx, 37);
				{
				setState(2106);
				match(Arrow);
				}
				break;

			case 38:
				enterOuterAlt(_localctx, 38);
				{
				setState(2107);
				match(LeftParen);
				setState(2108);
				match(RightParen);
				}
				break;

			case 39:
				enterOuterAlt(_localctx, 39);
				{
				setState(2109);
				match(LeftBracket);
				setState(2110);
				match(RightBracket);
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
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(CPP14Parser.IntegerLiteral, 0); }
		public TerminalNode CharacterLiteral() { return getToken(CPP14Parser.CharacterLiteral, 0); }
		public TerminalNode FloatingLiteral() { return getToken(CPP14Parser.FloatingLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(CPP14Parser.BooleanLiteral, 0); }
		public TerminalNode PointerLiteral() { return getToken(CPP14Parser.PointerLiteral, 0); }
		public TerminalNode UserDefinedLiteral() { return getToken(CPP14Parser.UserDefinedLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2113);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 254L) != 0)) ) {
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
	public static class TranslationUnitOrAnyContext extends ParserRuleContext {
		public TranslationUnitContext translationUnit() {
			return getRuleContext(TranslationUnitContext.class,0);
		}
		public List<AnySeqContext> anySeq() {
			return getRuleContexts(AnySeqContext.class);
		}
		public AnySeqContext anySeq(int i) {
			return getRuleContext(AnySeqContext.class,i);
		}
		public TranslationUnitOrAnyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translationUnitOrAny; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTranslationUnitOrAny(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTranslationUnitOrAny(this);
		}
	}

	public final TranslationUnitOrAnyContext translationUnitOrAny() throws RecognitionException {
		TranslationUnitOrAnyContext _localctx = new TranslationUnitOrAnyContext(_ctx, getState());
		enterRule(_localctx, 382, RULE_translationUnitOrAny);
		try {
			int _alt;
			setState(2121);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,307,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2115);
				translationUnit();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2117); 
				_errHandler.sync(this);
				_alt = 1+1;
				do {
					switch (_alt) {
					case 1+1:
						{
						{
						setState(2116);
						anySeq();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2119); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,306,_ctx);
				} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<AnySeqContext> anySeq() {
			return getRuleContexts(AnySeqContext.class);
		}
		public AnySeqContext anySeq(int i) {
			return getRuleContext(AnySeqContext.class,i);
		}
		public TerminalNode EOF() { return getToken(CPP14Parser.EOF, 0); }
		public UnknownIntervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknownInterval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnknownInterval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnknownInterval(this);
		}
	}

	public final UnknownIntervalContext unknownInterval() throws RecognitionException {
		UnknownIntervalContext _localctx = new UnknownIntervalContext(_ctx, getState());
		enterRule(_localctx, 384, RULE_unknownInterval);
		try {
			int _alt;
			setState(2130);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case MultiLineMacro:
			case Directive:
			case Alignas:
			case Alignof:
			case Asm:
			case Auto:
			case Bool:
			case Break:
			case Case:
			case Catch:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Const_cast:
			case Continue:
			case Decltype:
			case Default:
			case Delete:
			case Do:
			case Double:
			case Dynamic_cast:
			case Else:
			case Enum:
			case Explicit:
			case Export:
			case Extern:
			case False_:
			case Final:
			case Float:
			case For:
			case Friend:
			case Goto:
			case If:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Namespace:
			case New:
			case Noexcept:
			case Nullptr:
			case Operator:
			case Override:
			case Private:
			case Protected:
			case Public:
			case Register:
			case Reinterpret_cast:
			case Return:
			case Short:
			case Signed:
			case Sizeof:
			case Static:
			case Static_assert:
			case Static_cast:
			case Struct:
			case Switch:
			case Template:
			case This:
			case Thread_local:
			case Throw:
			case True_:
			case Try:
			case Typedef:
			case Typeid_:
			case Typename_:
			case Union:
			case Unsigned:
			case Using:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case While:
			case LeftParen:
			case RightParen:
			case LeftBracket:
			case RightBracket:
			case LeftBrace:
			case RightBrace:
			case Plus:
			case Minus:
			case Star:
			case Div:
			case Mod:
			case Caret:
			case And:
			case Or:
			case Tilde:
			case Not:
			case Assign:
			case Less:
			case Greater:
			case PlusAssign:
			case MinusAssign:
			case StarAssign:
			case DivAssign:
			case ModAssign:
			case XorAssign:
			case AndAssign:
			case OrAssign:
			case LeftShiftAssign:
			case RightShiftAssign:
			case Equal:
			case NotEqual:
			case LessEqual:
			case GreaterEqual:
			case AndAnd:
			case OrOr:
			case PlusPlus:
			case MinusMinus:
			case Comma:
			case ArrowStar:
			case Arrow:
			case Question:
			case Colon:
			case Doublecolon:
			case Semi:
			case Dot:
			case DotStar:
			case Ellipsis:
			case Identifier:
			case DecimalLiteral:
			case OctalLiteral:
			case HexadecimalLiteral:
			case BinaryLiteral:
			case Integersuffix:
			case UserDefinedIntegerLiteral:
			case UserDefinedFloatingLiteral:
			case UserDefinedStringLiteral:
			case UserDefinedCharacterLiteral:
			case Whitespace:
			case Newline:
			case BlockComment:
			case LineComment:
				enterOuterAlt(_localctx, 1);
				{
				setState(2125); 
				_errHandler.sync(this);
				_alt = 1+1;
				do {
					switch (_alt) {
					case 1+1:
						{
						setState(2125);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,308,_ctx) ) {
						case 1:
							{
							setState(2123);
							declaration();
							}
							break;

						case 2:
							{
							setState(2124);
							anySeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2127); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,309,_ctx);
				} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case EOF:
				enterOuterAlt(_localctx, 2);
				{
				setState(2129);
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
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAnySeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAnySeq(this);
		}
	}

	public final AnySeqContext anySeq() throws RecognitionException {
		AnySeqContext _localctx = new AnySeqContext(_ctx, getState());
		enterRule(_localctx, 386, RULE_anySeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2133); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(2132);
					matchWildcard();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(2135); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,311,_ctx);
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
		case 5:
			return nestedNameSpecifier_sempred((NestedNameSpecifierContext)_localctx, predIndex);

		case 15:
			return postfixExpression_sempred((PostfixExpressionContext)_localctx, predIndex);

		case 25:
			return noPointerNewDeclarator_sempred((NoPointerNewDeclaratorContext)_localctx, predIndex);

		case 115:
			return noPointerDeclarator_sempred((NoPointerDeclaratorContext)_localctx, predIndex);

		case 126:
			return noPointerAbstractDeclarator_sempred((NoPointerAbstractDeclaratorContext)_localctx, predIndex);

		case 128:
			return noPointerAbstractPackDeclarator_sempred((NoPointerAbstractPackDeclaratorContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean nestedNameSpecifier_sempred(NestedNameSpecifierContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean postfixExpression_sempred(PostfixExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 7);

		case 2:
			return precpred(_ctx, 6);

		case 3:
			return precpred(_ctx, 4);

		case 4:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean noPointerNewDeclarator_sempred(NoPointerNewDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean noPointerDeclarator_sempred(NoPointerDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean noPointerAbstractDeclarator_sempred(NoPointerAbstractDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean noPointerAbstractPackDeclarator_sempred(NoPointerAbstractPackDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0091\u085a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\u0080\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007"+
		"\u0083\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007"+
		"\u0086\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007"+
		"\u0089\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007"+
		"\u008c\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007"+
		"\u008f\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007"+
		"\u0092\u0002\u0093\u0007\u0093\u0002\u0094\u0007\u0094\u0002\u0095\u0007"+
		"\u0095\u0002\u0096\u0007\u0096\u0002\u0097\u0007\u0097\u0002\u0098\u0007"+
		"\u0098\u0002\u0099\u0007\u0099\u0002\u009a\u0007\u009a\u0002\u009b\u0007"+
		"\u009b\u0002\u009c\u0007\u009c\u0002\u009d\u0007\u009d\u0002\u009e\u0007"+
		"\u009e\u0002\u009f\u0007\u009f\u0002\u00a0\u0007\u00a0\u0002\u00a1\u0007"+
		"\u00a1\u0002\u00a2\u0007\u00a2\u0002\u00a3\u0007\u00a3\u0002\u00a4\u0007"+
		"\u00a4\u0002\u00a5\u0007\u00a5\u0002\u00a6\u0007\u00a6\u0002\u00a7\u0007"+
		"\u00a7\u0002\u00a8\u0007\u00a8\u0002\u00a9\u0007\u00a9\u0002\u00aa\u0007"+
		"\u00aa\u0002\u00ab\u0007\u00ab\u0002\u00ac\u0007\u00ac\u0002\u00ad\u0007"+
		"\u00ad\u0002\u00ae\u0007\u00ae\u0002\u00af\u0007\u00af\u0002\u00b0\u0007"+
		"\u00b0\u0002\u00b1\u0007\u00b1\u0002\u00b2\u0007\u00b2\u0002\u00b3\u0007"+
		"\u00b3\u0002\u00b4\u0007\u00b4\u0002\u00b5\u0007\u00b5\u0002\u00b6\u0007"+
		"\u00b6\u0002\u00b7\u0007\u00b7\u0002\u00b8\u0007\u00b8\u0002\u00b9\u0007"+
		"\u00b9\u0002\u00ba\u0007\u00ba\u0002\u00bb\u0007\u00bb\u0002\u00bc\u0007"+
		"\u00bc\u0002\u00bd\u0007\u00bd\u0002\u00be\u0007\u00be\u0002\u00bf\u0007"+
		"\u00bf\u0002\u00c0\u0007\u00c0\u0002\u00c1\u0007\u00c1\u0001\u0000\u0003"+
		"\u0000\u0186\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0004\u0001\u018b"+
		"\b\u0001\u000b\u0001\f\u0001\u018c\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0196\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0003\u0002\u019a\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u01a3\b\u0003\u0001\u0003\u0003\u0003\u01a6\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0003\u0004\u01aa\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u01b2\b\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u01b9\b\u0005\u0001"+
		"\u0005\u0003\u0005\u01bc\b\u0005\u0001\u0005\u0005\u0005\u01bf\b\u0005"+
		"\n\u0005\f\u0005\u01c2\t\u0005\u0001\u0006\u0001\u0006\u0003\u0006\u01c6"+
		"\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0003\u0007\u01cc"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u01d4\b\b\u0003\b\u01d6\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n"+
		"\u0005\n\u01dd\b\n\n\n\f\n\u01e0\t\n\u0001\n\u0003\n\u01e3\b\n\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u01e7\b\u000b\u0001\f\u0003\f\u01ea\b\f\u0001"+
		"\f\u0001\f\u0003\f\u01ee\b\f\u0001\r\u0003\r\u01f1\b\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0003\u000e\u01f8\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0003\u000e\u01fc\b\u000e\u0001\u000e\u0003\u000e\u01ff\b"+
		"\u000e\u0001\u000e\u0003\u000e\u0202\b\u000e\u0001\u000e\u0003\u000e\u0205"+
		"\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u020b"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u020f\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u0213\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0221\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u0225\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u022b\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0003\u000f\u0232\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0003\u000f\u0238\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u023c\b\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u0240\b"+
		"\u000f\n\u000f\f\u000f\u0243\t\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0003\u0012\u024a\b\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u024f\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u025c\b\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0263\b\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u026f\b\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u0279\b\u0013\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0003\u0015\u027e\b\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0282\b"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003"+
		"\u0015\u0289\b\u0015\u0001\u0015\u0003\u0015\u028c\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0003\u0017"+
		"\u0294\b\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u0298\b\u0018\u0001"+
		"\u0018\u0003\u0018\u029b\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u02a2\b\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u02a9\b\u0019\u0005\u0019\u02ab"+
		"\b\u0019\n\u0019\f\u0019\u02ae\t\u0019\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u02b2\b\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u02b6\b\u001a\u0001"+
		"\u001b\u0003\u001b\u02b9\b\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003"+
		"\u001b\u02be\b\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u02cd\b\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0005\u001e\u02d2\b\u001e\n\u001e\f\u001e\u02d5\t\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u02da\b\u001f\n\u001f"+
		"\f\u001f\u02dd\t\u001f\u0001 \u0001 \u0001 \u0005 \u02e2\b \n \f \u02e5"+
		"\t \u0001!\u0001!\u0001!\u0001!\u0005!\u02eb\b!\n!\f!\u02ee\t!\u0001\""+
		"\u0001\"\u0001\"\u0001\"\u0003\"\u02f4\b\"\u0001#\u0001#\u0001#\u0005"+
		"#\u02f9\b#\n#\f#\u02fc\t#\u0001$\u0001$\u0001$\u0005$\u0301\b$\n$\f$\u0304"+
		"\t$\u0001%\u0001%\u0001%\u0005%\u0309\b%\n%\f%\u030c\t%\u0001&\u0001&"+
		"\u0001&\u0005&\u0311\b&\n&\f&\u0314\t&\u0001\'\u0001\'\u0001\'\u0005\'"+
		"\u0319\b\'\n\'\f\'\u031c\t\'\u0001(\u0001(\u0001(\u0005(\u0321\b(\n(\f"+
		"(\u0324\t(\u0001)\u0001)\u0001)\u0005)\u0329\b)\n)\f)\u032c\t)\u0001*"+
		"\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u0334\b*\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0003+\u033c\b+\u0001,\u0001,\u0001-\u0001-\u0001"+
		"-\u0005-\u0343\b-\n-\f-\u0346\t-\u0001.\u0001.\u0001/\u0001/\u0001/\u0003"+
		"/\u034d\b/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u0355\b/\u0003"+
		"/\u0357\b/\u00010\u00030\u035a\b0\u00010\u00010\u00010\u00010\u00030\u0360"+
		"\b0\u00010\u00010\u00010\u00011\u00031\u0366\b1\u00011\u00011\u00012\u0001"+
		"2\u00032\u036c\b2\u00012\u00012\u00013\u00043\u0371\b3\u000b3\f3\u0372"+
		"\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u037c\b4\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00034\u0384\b4\u00015\u00015\u0003"+
		"5\u0388\b5\u00015\u00015\u00015\u00015\u00015\u00035\u038f\b5\u00035\u0391"+
		"\b5\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00036\u03a5"+
		"\b6\u00016\u00016\u00036\u03a9\b6\u00016\u00016\u00016\u00016\u00036\u03af"+
		"\b6\u00016\u00016\u00016\u00036\u03b4\b6\u00017\u00017\u00037\u03b8\b"+
		"7\u00018\u00038\u03bb\b8\u00018\u00018\u00018\u00019\u00019\u00039\u03c2"+
		"\b9\u0001:\u0001:\u0001:\u0001:\u0001:\u0003:\u03c9\b:\u0001:\u0001:\u0003"+
		":\u03cd\b:\u0001:\u0001:\u0001;\u0001;\u0001<\u0004<\u03d4\b<\u000b<\f"+
		"<\u03d5\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0003=\u03e1\b=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0003>\u03eb\b>\u0001?\u0001?\u0001?\u0003?\u03f0\b?\u0001?\u0001?\u0001"+
		"?\u0001?\u0001@\u0003@\u03f7\b@\u0001@\u0003@\u03fa\b@\u0001@\u0001@\u0001"+
		"@\u0003@\u03ff\b@\u0001@\u0001@\u0001@\u0003@\u0404\b@\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001B\u0001B\u0001C\u0001C\u0001"+
		"C\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u0419\bD\u0001E\u0004"+
		"E\u041c\bE\u000bE\fE\u041d\u0001E\u0003E\u0421\bE\u0001F\u0001F\u0001"+
		"G\u0001G\u0001H\u0001H\u0001I\u0001I\u0001I\u0003I\u042c\bI\u0001J\u0001"+
		"J\u0001J\u0001J\u0003J\u0432\bJ\u0001K\u0004K\u0435\bK\u000bK\fK\u0436"+
		"\u0001K\u0003K\u043a\bK\u0001L\u0004L\u043d\bL\u000bL\fL\u043e\u0001L"+
		"\u0003L\u0442\bL\u0001M\u0001M\u0001N\u0001N\u0001O\u0003O\u0449\bO\u0001"+
		"O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0003O\u0452\bO\u0001O\u0004"+
		"O\u0455\bO\u000bO\fO\u0456\u0001O\u0003O\u045a\bO\u0001O\u0001O\u0003"+
		"O\u045e\bO\u0001O\u0001O\u0003O\u0462\bO\u0001O\u0001O\u0003O\u0466\b"+
		"O\u0001O\u0001O\u0001O\u0003O\u046b\bO\u0001O\u0005O\u046e\bO\nO\fO\u0471"+
		"\tO\u0001O\u0001O\u0001O\u0003O\u0476\bO\u0001O\u0001O\u0001O\u0001O\u0003"+
		"O\u047c\bO\u0001P\u0001P\u0001P\u0001P\u0003P\u0482\bP\u0001Q\u0001Q\u0001"+
		"Q\u0001Q\u0003Q\u0488\bQ\u0001Q\u0001Q\u0001R\u0001R\u0003R\u048e\bR\u0001"+
		"R\u0003R\u0491\bR\u0001R\u0001R\u0001R\u0001R\u0003R\u0497\bR\u0001R\u0001"+
		"R\u0003R\u049b\bR\u0001R\u0001R\u0003R\u049f\bR\u0001R\u0003R\u04a2\b"+
		"R\u0001S\u0001S\u0001T\u0001T\u0001T\u0001T\u0003T\u04aa\bT\u0003T\u04ac"+
		"\bT\u0001T\u0001T\u0001U\u0001U\u0003U\u04b2\bU\u0001U\u0003U\u04b5\b"+
		"U\u0001U\u0003U\u04b8\bU\u0001U\u0003U\u04bb\bU\u0001V\u0001V\u0003V\u04bf"+
		"\bV\u0001V\u0001V\u0003V\u04c3\bV\u0001V\u0001V\u0001W\u0001W\u0003W\u04c9"+
		"\bW\u0001X\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0005Y\u04d1\bY\nY\fY\u04d4"+
		"\tY\u0001Z\u0001Z\u0001Z\u0003Z\u04d9\bZ\u0001[\u0001[\u0001\\\u0001\\"+
		"\u0003\\\u04df\b\\\u0001]\u0001]\u0001^\u0003^\u04e4\b^\u0001^\u0001^"+
		"\u0001^\u0003^\u04e9\b^\u0001^\u0001^\u0003^\u04ed\b^\u0001^\u0001^\u0001"+
		"_\u0001_\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001a\u0003a\u04fa"+
		"\ba\u0001a\u0001a\u0001b\u0001b\u0003b\u0500\bb\u0001b\u0001b\u0003b\u0504"+
		"\bb\u0001b\u0001b\u0001b\u0001c\u0003c\u050a\bc\u0001c\u0001c\u0001c\u0003"+
		"c\u050f\bc\u0001c\u0001c\u0001c\u0001d\u0001d\u0001d\u0001d\u0001d\u0001"+
		"d\u0001e\u0001e\u0001e\u0001e\u0003e\u051e\be\u0001e\u0001e\u0003e\u0522"+
		"\be\u0001f\u0004f\u0525\bf\u000bf\ff\u0526\u0001g\u0001g\u0001g\u0003"+
		"g\u052c\bg\u0001g\u0001g\u0001g\u0003g\u0531\bg\u0001h\u0001h\u0001h\u0001"+
		"h\u0003h\u0537\bh\u0001h\u0003h\u053a\bh\u0001h\u0001h\u0001i\u0001i\u0001"+
		"i\u0005i\u0541\bi\ni\fi\u0544\ti\u0001i\u0003i\u0547\bi\u0001j\u0001j"+
		"\u0001j\u0003j\u054c\bj\u0001j\u0001j\u0003j\u0550\bj\u0001k\u0001k\u0001"+
		"l\u0001l\u0003l\u0556\bl\u0001l\u0001l\u0001m\u0004m\u055b\bm\u000bm\f"+
		"m\u055c\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001"+
		"n\u0001n\u0001n\u0001n\u0001n\u0004n\u056c\bn\u000bn\fn\u056d\u0003n\u0570"+
		"\bn\u0001o\u0001o\u0001o\u0005o\u0575\bo\no\fo\u0578\to\u0001p\u0001p"+
		"\u0003p\u057c\bp\u0001q\u0001q\u0001q\u0001q\u0001q\u0003q\u0583\bq\u0001"+
		"r\u0001r\u0003r\u0587\br\u0005r\u0589\br\nr\fr\u058c\tr\u0001r\u0001r"+
		"\u0001s\u0001s\u0001s\u0003s\u0593\bs\u0001s\u0001s\u0001s\u0001s\u0003"+
		"s\u0599\bs\u0001s\u0001s\u0001s\u0001s\u0003s\u059f\bs\u0001s\u0001s\u0003"+
		"s\u05a3\bs\u0003s\u05a5\bs\u0005s\u05a7\bs\ns\fs\u05aa\ts\u0001t\u0001"+
		"t\u0003t\u05ae\bt\u0001t\u0001t\u0003t\u05b2\bt\u0001t\u0003t\u05b5\b"+
		"t\u0001t\u0003t\u05b8\bt\u0001t\u0003t\u05bb\bt\u0001u\u0001u\u0001u\u0003"+
		"u\u05c0\bu\u0001v\u0001v\u0003v\u05c4\bv\u0001v\u0003v\u05c7\bv\u0001"+
		"v\u0001v\u0003v\u05cb\bv\u0001v\u0003v\u05ce\bv\u0003v\u05d0\bv\u0001"+
		"w\u0004w\u05d3\bw\u000bw\fw\u05d4\u0001x\u0001x\u0001y\u0001y\u0001z\u0003"+
		"z\u05dc\bz\u0001z\u0001z\u0001{\u0001{\u0003{\u05e2\b{\u0001|\u0001|\u0003"+
		"|\u05e6\b|\u0001|\u0001|\u0001|\u0001|\u0003|\u05ec\b|\u0001}\u0001}\u0004"+
		"}\u05f0\b}\u000b}\f}\u05f1\u0001}\u0003}\u05f5\b}\u0003}\u05f7\b}\u0001"+
		"~\u0001~\u0001~\u0001~\u0003~\u05fd\b~\u0001~\u0001~\u0003~\u0601\b~\u0001"+
		"~\u0001~\u0001~\u0001~\u0003~\u0607\b~\u0001~\u0001~\u0001~\u0001~\u0001"+
		"~\u0003~\u060e\b~\u0001~\u0001~\u0003~\u0612\b~\u0003~\u0614\b~\u0005"+
		"~\u0616\b~\n~\f~\u0619\t~\u0001\u007f\u0005\u007f\u061c\b\u007f\n\u007f"+
		"\f\u007f\u061f\t\u007f\u0001\u007f\u0001\u007f\u0001\u0080\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0003\u0080"+
		"\u062a\b\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u062e\b\u0080\u0003"+
		"\u0080\u0630\b\u0080\u0005\u0080\u0632\b\u0080\n\u0080\f\u0080\u0635\t"+
		"\u0080\u0001\u0081\u0001\u0081\u0003\u0081\u0639\b\u0081\u0001\u0081\u0003"+
		"\u0081\u063c\b\u0081\u0001\u0082\u0001\u0082\u0001\u0082\u0005\u0082\u0641"+
		"\b\u0082\n\u0082\f\u0082\u0644\t\u0082\u0001\u0083\u0003\u0083\u0647\b"+
		"\u0083\u0001\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u064c\b\u0083\u0003"+
		"\u0083\u064e\b\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u0652\b\u0083"+
		"\u0001\u0084\u0003\u0084\u0655\b\u0084\u0001\u0084\u0003\u0084\u0658\b"+
		"\u0084\u0001\u0084\u0001\u0084\u0003\u0084\u065c\b\u0084\u0001\u0084\u0001"+
		"\u0084\u0001\u0085\u0003\u0085\u0661\b\u0085\u0001\u0085\u0001\u0085\u0001"+
		"\u0085\u0001\u0085\u0001\u0085\u0003\u0085\u0668\b\u0085\u0001\u0086\u0001"+
		"\u0086\u0001\u0086\u0001\u0086\u0001\u0086\u0003\u0086\u066f\b\u0086\u0001"+
		"\u0087\u0001\u0087\u0001\u0087\u0003\u0087\u0674\b\u0087\u0001\u0088\u0001"+
		"\u0088\u0003\u0088\u0678\b\u0088\u0001\u0089\u0001\u0089\u0003\u0089\u067c"+
		"\b\u0089\u0001\u0089\u0001\u0089\u0001\u0089\u0003\u0089\u0681\b\u0089"+
		"\u0005\u0089\u0683\b\u0089\n\u0089\f\u0089\u0686\t\u0089\u0001\u008a\u0001"+
		"\u008a\u0001\u008a\u0003\u008a\u068b\b\u008a\u0003\u008a\u068d\b\u008a"+
		"\u0001\u008a\u0001\u008a\u0001\u008b\u0001\u008b\u0003\u008b\u0693\b\u008b"+
		"\u0001\u008c\u0001\u008c\u0001\u008c\u0003\u008c\u0698\b\u008c\u0001\u008c"+
		"\u0001\u008c\u0001\u008d\u0001\u008d\u0003\u008d\u069e\b\u008d\u0001\u008d"+
		"\u0001\u008d\u0003\u008d\u06a2\b\u008d\u0003\u008d\u06a4\b\u008d\u0001"+
		"\u008d\u0003\u008d\u06a7\b\u008d\u0001\u008d\u0001\u008d\u0003\u008d\u06ab"+
		"\b\u008d\u0001\u008d\u0001\u008d\u0003\u008d\u06af\b\u008d\u0003\u008d"+
		"\u06b1\b\u008d\u0003\u008d\u06b3\b\u008d\u0001\u008e\u0003\u008e\u06b6"+
		"\b\u008e\u0001\u008e\u0001\u008e\u0001\u008f\u0001\u008f\u0001\u0090\u0001"+
		"\u0090\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0004\u0091\u06c2"+
		"\b\u0091\u000b\u0091\f\u0091\u06c3\u0001\u0092\u0003\u0092\u06c7\b\u0092"+
		"\u0001\u0092\u0003\u0092\u06ca\b\u0092\u0001\u0092\u0003\u0092\u06cd\b"+
		"\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001"+
		"\u0092\u0001\u0092\u0003\u0092\u06d6\b\u0092\u0001\u0093\u0001\u0093\u0001"+
		"\u0093\u0005\u0093\u06db\b\u0093\n\u0093\f\u0093\u06de\t\u0093\u0001\u0094"+
		"\u0001\u0094\u0003\u0094\u06e2\b\u0094\u0001\u0094\u0003\u0094\u06e5\b"+
		"\u0094\u0001\u0094\u0003\u0094\u06e8\b\u0094\u0003\u0094\u06ea\b\u0094"+
		"\u0001\u0094\u0003\u0094\u06ed\b\u0094\u0001\u0094\u0003\u0094\u06f0\b"+
		"\u0094\u0001\u0094\u0001\u0094\u0003\u0094\u06f4\b\u0094\u0001\u0095\u0004"+
		"\u0095\u06f7\b\u0095\u000b\u0095\f\u0095\u06f8\u0001\u0096\u0001\u0096"+
		"\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0098\u0001\u0098"+
		"\u0001\u0098\u0001\u0099\u0001\u0099\u0003\u0099\u0706\b\u0099\u0001\u0099"+
		"\u0001\u0099\u0001\u0099\u0003\u0099\u070b\b\u0099\u0005\u0099\u070d\b"+
		"\u0099\n\u0099\f\u0099\u0710\t\u0099\u0001\u009a\u0003\u009a\u0713\b\u009a"+
		"\u0001\u009a\u0001\u009a\u0001\u009a\u0003\u009a\u0718\b\u009a\u0001\u009a"+
		"\u0001\u009a\u0001\u009a\u0003\u009a\u071d\b\u009a\u0001\u009a\u0001\u009a"+
		"\u0003\u009a\u0721\b\u009a\u0001\u009b\u0003\u009b\u0724\b\u009b\u0001"+
		"\u009b\u0001\u009b\u0003\u009b\u0728\b\u009b\u0001\u009c\u0001\u009c\u0001"+
		"\u009d\u0001\u009d\u0001\u009e\u0001\u009e\u0001\u009e\u0001\u009f\u0001"+
		"\u009f\u0003\u009f\u0733\b\u009f\u0001\u00a0\u0001\u00a0\u0003\u00a0\u0737"+
		"\b\u00a0\u0001\u00a1\u0001\u00a1\u0001\u00a1\u0001\u00a2\u0001\u00a2\u0003"+
		"\u00a2\u073e\b\u00a2\u0001\u00a2\u0001\u00a2\u0001\u00a2\u0003\u00a2\u0743"+
		"\b\u00a2\u0005\u00a2\u0745\b\u00a2\n\u00a2\f\u00a2\u0748\t\u00a2\u0001"+
		"\u00a3\u0001\u00a3\u0001\u00a3\u0003\u00a3\u074d\b\u00a3\u0001\u00a3\u0001"+
		"\u00a3\u0003\u00a3\u0751\b\u00a3\u0001\u00a4\u0001\u00a4\u0003\u00a4\u0755"+
		"\b\u00a4\u0001\u00a5\u0001\u00a5\u0001\u00a5\u0001\u00a6\u0001\u00a6\u0001"+
		"\u00a6\u0001\u00a6\u0003\u00a6\u075e\b\u00a6\u0001\u00a7\u0001\u00a7\u0001"+
		"\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a8\u0001\u00a8\u0001"+
		"\u00a8\u0005\u00a8\u0769\b\u00a8\n\u00a8\f\u00a8\u076c\t\u00a8\u0001\u00a9"+
		"\u0001\u00a9\u0003\u00a9\u0770\b\u00a9\u0001\u00aa\u0001\u00aa\u0001\u00aa"+
		"\u0001\u00aa\u0001\u00aa\u0003\u00aa\u0777\b\u00aa\u0001\u00aa\u0001\u00aa"+
		"\u0003\u00aa\u077b\b\u00aa\u0001\u00aa\u0003\u00aa\u077e\b\u00aa\u0001"+
		"\u00aa\u0003\u00aa\u0781\b\u00aa\u0001\u00aa\u0003\u00aa\u0784\b\u00aa"+
		"\u0001\u00aa\u0001\u00aa\u0003\u00aa\u0788\b\u00aa\u0001\u00ab\u0001\u00ab"+
		"\u0001\u00ab\u0003\u00ab\u078d\b\u00ab\u0001\u00ab\u0001\u00ab\u0001\u00ac"+
		"\u0001\u00ac\u0001\u00ac\u0003\u00ac\u0794\b\u00ac\u0001\u00ac\u0001\u00ac"+
		"\u0003\u00ac\u0798\b\u00ac\u0001\u00ac\u0001\u00ac\u0003\u00ac\u079c\b"+
		"\u00ac\u0001\u00ad\u0001\u00ad\u0001\u00ae\u0001\u00ae\u0003\u00ae\u07a2"+
		"\b\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0003\u00ae\u07a7\b\u00ae"+
		"\u0005\u00ae\u07a9\b\u00ae\n\u00ae\f\u00ae\u07ac\t\u00ae\u0001\u00af\u0001"+
		"\u00af\u0001\u00af\u0003\u00af\u07b1\b\u00af\u0001\u00b0\u0001\u00b0\u0001"+
		"\u00b0\u0001\u00b0\u0003\u00b0\u07b7\b\u00b0\u0001\u00b0\u0003\u00b0\u07ba"+
		"\b\u00b0\u0001\u00b1\u0003\u00b1\u07bd\b\u00b1\u0001\u00b1\u0001\u00b1"+
		"\u0001\u00b1\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001\u00b2"+
		"\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b4\u0001\u00b4"+
		"\u0003\u00b4\u07cd\b\u00b4\u0001\u00b4\u0001\u00b4\u0001\u00b4\u0001\u00b5"+
		"\u0004\u00b5\u07d3\b\u00b5\u000b\u00b5\f\u00b5\u07d4\u0001\u00b6\u0001"+
		"\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b7\u0003"+
		"\u00b7\u07de\b\u00b7\u0001\u00b7\u0001\u00b7\u0001\u00b7\u0003\u00b7\u07e3"+
		"\b\u00b7\u0001\u00b7\u0003\u00b7\u07e6\b\u00b7\u0001\u00b8\u0001\u00b8"+
		"\u0003\u00b8\u07ea\b\u00b8\u0001\u00b9\u0001\u00b9\u0003\u00b9\u07ee\b"+
		"\u00b9\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0003\u00ba\u07f3\b\u00ba\u0001"+
		"\u00ba\u0001\u00ba\u0001\u00bb\u0001\u00bb\u0003\u00bb\u07f9\b\u00bb\u0001"+
		"\u00bb\u0001\u00bb\u0001\u00bb\u0003\u00bb\u07fe\b\u00bb\u0005\u00bb\u0800"+
		"\b\u00bb\n\u00bb\f\u00bb\u0803\t\u00bb\u0001\u00bc\u0001\u00bc\u0001\u00bc"+
		"\u0001\u00bc\u0001\u00bc\u0001\u00bc\u0003\u00bc\u080b\b\u00bc\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0003\u00bd\u0810\b\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0003\u00bd\u0815\b\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0003\u00bd\u0840\b\u00bd\u0001\u00be\u0001\u00be"+
		"\u0001\u00bf\u0001\u00bf\u0004\u00bf\u0846\b\u00bf\u000b\u00bf\f\u00bf"+
		"\u0847\u0003\u00bf\u084a\b\u00bf\u0001\u00c0\u0001\u00c0\u0004\u00c0\u084e"+
		"\b\u00c0\u000b\u00c0\f\u00c0\u084f\u0001\u00c0\u0003\u00c0\u0853\b\u00c0"+
		"\u0001\u00c1\u0004\u00c1\u0856\b\u00c1\u000b\u00c1\f\u00c1\u0857\u0001"+
		"\u00c1\u0003\u041d\u0847\u084f\u0006\n\u001e2\u00e6\u00fc\u0100\u00c2"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082"+
		"\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a"+
		"\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2"+
		"\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca"+
		"\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2"+
		"\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa"+
		"\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112"+
		"\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a"+
		"\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140\u0142"+
		"\u0144\u0146\u0148\u014a\u014c\u014e\u0150\u0152\u0154\u0156\u0158\u015a"+
		"\u015c\u015e\u0160\u0162\u0164\u0166\u0168\u016a\u016c\u016e\u0170\u0172"+
		"\u0174\u0176\u0178\u017a\u017c\u017e\u0180\u0182\u0000\u0017\u0002\u0000"+
		"aaee\u0004\u0000\u0018\u0018\u001f\u001f::AA\u0002\u0000||\u0081\u0081"+
		"\u0001\u0000xy\u0002\u0000[]ad\u0002\u0000{{\u0082\u0082\u0001\u0000]"+
		"_\u0001\u0000[\\\u0002\u0000fgtu\u0001\u0000rs\u0002\u0000eehq\u0005\u0000"+
		"$$//99??FF\u0003\u0000\"\",,PP\u0002\u0000..<<\u0002\u0000==NN\u0002\u0000"+
		"\u0015\u0015BB\u0001\u0000UZ\u0002\u0000aavv\u0002\u0000\u0016\u0016R"+
		"R\u0001\u0000\u001b\u001c\u0002\u0000&&55\u0001\u000068\u0001\u0000\u0001"+
		"\u0007\u0946\u0000\u0185\u0001\u0000\u0000\u0000\u0002\u0195\u0001\u0000"+
		"\u0000\u0000\u0004\u0199\u0001\u0000\u0000\u0000\u0006\u01a5\u0001\u0000"+
		"\u0000\u0000\b\u01a7\u0001\u0000\u0000\u0000\n\u01ad\u0001\u0000\u0000"+
		"\u0000\f\u01c3\u0001\u0000\u0000\u0000\u000e\u01c9\u0001\u0000\u0000\u0000"+
		"\u0010\u01d5\u0001\u0000\u0000\u0000\u0012\u01d7\u0001\u0000\u0000\u0000"+
		"\u0014\u01d9\u0001\u0000\u0000\u0000\u0016\u01e6\u0001\u0000\u0000\u0000"+
		"\u0018\u01ed\u0001\u0000\u0000\u0000\u001a\u01f0\u0001\u0000\u0000\u0000"+
		"\u001c\u01f5\u0001\u0000\u0000\u0000\u001e\u0224\u0001\u0000\u0000\u0000"+
		" \u0244\u0001\u0000\u0000\u0000\"\u0246\u0001\u0000\u0000\u0000$\u025b"+
		"\u0001\u0000\u0000\u0000&\u0278\u0001\u0000\u0000\u0000(\u027a\u0001\u0000"+
		"\u0000\u0000*\u027d\u0001\u0000\u0000\u0000,\u028d\u0001\u0000\u0000\u0000"+
		".\u0291\u0001\u0000\u0000\u00000\u029a\u0001\u0000\u0000\u00002\u029c"+
		"\u0001\u0000\u0000\u00004\u02b5\u0001\u0000\u0000\u00006\u02b8\u0001\u0000"+
		"\u0000\u00008\u02c1\u0001\u0000\u0000\u0000:\u02cc\u0001\u0000\u0000\u0000"+
		"<\u02ce\u0001\u0000\u0000\u0000>\u02d6\u0001\u0000\u0000\u0000@\u02de"+
		"\u0001\u0000\u0000\u0000B\u02e6\u0001\u0000\u0000\u0000D\u02f3\u0001\u0000"+
		"\u0000\u0000F\u02f5\u0001\u0000\u0000\u0000H\u02fd\u0001\u0000\u0000\u0000"+
		"J\u0305\u0001\u0000\u0000\u0000L\u030d\u0001\u0000\u0000\u0000N\u0315"+
		"\u0001\u0000\u0000\u0000P\u031d\u0001\u0000\u0000\u0000R\u0325\u0001\u0000"+
		"\u0000\u0000T\u032d\u0001\u0000\u0000\u0000V\u033b\u0001\u0000\u0000\u0000"+
		"X\u033d\u0001\u0000\u0000\u0000Z\u033f\u0001\u0000\u0000\u0000\\\u0347"+
		"\u0001\u0000\u0000\u0000^\u0356\u0001\u0000\u0000\u0000`\u0359\u0001\u0000"+
		"\u0000\u0000b\u0365\u0001\u0000\u0000\u0000d\u0369\u0001\u0000\u0000\u0000"+
		"f\u0370\u0001\u0000\u0000\u0000h\u0383\u0001\u0000\u0000\u0000j\u0390"+
		"\u0001\u0000\u0000\u0000l\u03b3\u0001\u0000\u0000\u0000n\u03b7\u0001\u0000"+
		"\u0000\u0000p\u03ba\u0001\u0000\u0000\u0000r\u03c1\u0001\u0000\u0000\u0000"+
		"t\u03cc\u0001\u0000\u0000\u0000v\u03d0\u0001\u0000\u0000\u0000x\u03d3"+
		"\u0001\u0000\u0000\u0000z\u03e0\u0001\u0000\u0000\u0000|\u03ea\u0001\u0000"+
		"\u0000\u0000~\u03ec\u0001\u0000\u0000\u0000\u0080\u0403\u0001\u0000\u0000"+
		"\u0000\u0082\u0405\u0001\u0000\u0000\u0000\u0084\u040d\u0001\u0000\u0000"+
		"\u0000\u0086\u040f\u0001\u0000\u0000\u0000\u0088\u0418\u0001\u0000\u0000"+
		"\u0000\u008a\u041b\u0001\u0000\u0000\u0000\u008c\u0422\u0001\u0000\u0000"+
		"\u0000\u008e\u0424\u0001\u0000\u0000\u0000\u0090\u0426\u0001\u0000\u0000"+
		"\u0000\u0092\u042b\u0001\u0000\u0000\u0000\u0094\u0431\u0001\u0000\u0000"+
		"\u0000\u0096\u0434\u0001\u0000\u0000\u0000\u0098\u043c\u0001\u0000\u0000"+
		"\u0000\u009a\u0443\u0001\u0000\u0000\u0000\u009c\u0445\u0001\u0000\u0000"+
		"\u0000\u009e\u047b\u0001\u0000\u0000\u0000\u00a0\u0481\u0001\u0000\u0000"+
		"\u0000\u00a2\u0483\u0001\u0000\u0000\u0000\u00a4\u04a1\u0001\u0000\u0000"+
		"\u0000\u00a6\u04a3\u0001\u0000\u0000\u0000\u00a8\u04a5\u0001\u0000\u0000"+
		"\u0000\u00aa\u04af\u0001\u0000\u0000\u0000\u00ac\u04bc\u0001\u0000\u0000"+
		"\u0000\u00ae\u04c6\u0001\u0000\u0000\u0000\u00b0\u04ca\u0001\u0000\u0000"+
		"\u0000\u00b2\u04cd\u0001\u0000\u0000\u0000\u00b4\u04d5\u0001\u0000\u0000"+
		"\u0000\u00b6\u04da\u0001\u0000\u0000\u0000\u00b8\u04de\u0001\u0000\u0000"+
		"\u0000\u00ba\u04e0\u0001\u0000\u0000\u0000\u00bc\u04e3\u0001\u0000\u0000"+
		"\u0000\u00be\u04f0\u0001\u0000\u0000\u0000\u00c0\u04f2\u0001\u0000\u0000"+
		"\u0000\u00c2\u04f9\u0001\u0000\u0000\u0000\u00c4\u04fd\u0001\u0000\u0000"+
		"\u0000\u00c6\u0509\u0001\u0000\u0000\u0000\u00c8\u0513\u0001\u0000\u0000"+
		"\u0000\u00ca\u0519\u0001\u0000\u0000\u0000\u00cc\u0524\u0001\u0000\u0000"+
		"\u0000\u00ce\u0530\u0001\u0000\u0000\u0000\u00d0\u0532\u0001\u0000\u0000"+
		"\u0000\u00d2\u053d\u0001\u0000\u0000\u0000\u00d4\u054b\u0001\u0000\u0000"+
		"\u0000\u00d6\u0551\u0001\u0000\u0000\u0000\u00d8\u0553\u0001\u0000\u0000"+
		"\u0000\u00da\u055a\u0001\u0000\u0000\u0000\u00dc\u056f\u0001\u0000\u0000"+
		"\u0000\u00de\u0571\u0001\u0000\u0000\u0000\u00e0\u0579\u0001\u0000\u0000"+
		"\u0000\u00e2\u0582\u0001\u0000\u0000\u0000\u00e4\u058a\u0001\u0000\u0000"+
		"\u0000\u00e6\u0598\u0001\u0000\u0000\u0000\u00e8\u05ab\u0001\u0000\u0000"+
		"\u0000\u00ea\u05bc\u0001\u0000\u0000\u0000\u00ec\u05cf\u0001\u0000\u0000"+
		"\u0000\u00ee\u05d2\u0001\u0000\u0000\u0000\u00f0\u05d6\u0001\u0000\u0000"+
		"\u0000\u00f2\u05d8\u0001\u0000\u0000\u0000\u00f4\u05db\u0001\u0000\u0000"+
		"\u0000\u00f6\u05df\u0001\u0000\u0000\u0000\u00f8\u05eb\u0001\u0000\u0000"+
		"\u0000\u00fa\u05f6\u0001\u0000\u0000\u0000\u00fc\u0606\u0001\u0000\u0000"+
		"\u0000\u00fe\u061d\u0001\u0000\u0000\u0000\u0100\u0622\u0001\u0000\u0000"+
		"\u0000\u0102\u0636\u0001\u0000\u0000\u0000\u0104\u063d\u0001\u0000\u0000"+
		"\u0000\u0106\u0646\u0001\u0000\u0000\u0000\u0108\u0654\u0001\u0000\u0000"+
		"\u0000\u010a\u0667\u0001\u0000\u0000\u0000\u010c\u066e\u0001\u0000\u0000"+
		"\u0000\u010e\u0673\u0001\u0000\u0000\u0000\u0110\u0677\u0001\u0000\u0000"+
		"\u0000\u0112\u0679\u0001\u0000\u0000\u0000\u0114\u0687\u0001\u0000\u0000"+
		"\u0000\u0116\u0692\u0001\u0000\u0000\u0000\u0118\u0694\u0001\u0000\u0000"+
		"\u0000\u011a\u06b2\u0001\u0000\u0000\u0000\u011c\u06b5\u0001\u0000\u0000"+
		"\u0000\u011e\u06b9\u0001\u0000\u0000\u0000\u0120\u06bb\u0001\u0000\u0000"+
		"\u0000\u0122\u06c1\u0001\u0000\u0000\u0000\u0124\u06d5\u0001\u0000\u0000"+
		"\u0000\u0126\u06d7\u0001\u0000\u0000\u0000\u0128\u06f3\u0001\u0000\u0000"+
		"\u0000\u012a\u06f6\u0001\u0000\u0000\u0000\u012c\u06fa\u0001\u0000\u0000"+
		"\u0000\u012e\u06fc\u0001\u0000\u0000\u0000\u0130\u0700\u0001\u0000\u0000"+
		"\u0000\u0132\u0703\u0001\u0000\u0000\u0000\u0134\u0712\u0001\u0000\u0000"+
		"\u0000\u0136\u0727\u0001\u0000\u0000\u0000\u0138\u0729\u0001\u0000\u0000"+
		"\u0000\u013a\u072b\u0001\u0000\u0000\u0000\u013c\u072d\u0001\u0000\u0000"+
		"\u0000\u013e\u0730\u0001\u0000\u0000\u0000\u0140\u0734\u0001\u0000\u0000"+
		"\u0000\u0142\u0738\u0001\u0000\u0000\u0000\u0144\u073b\u0001\u0000\u0000"+
		"\u0000\u0146\u0749\u0001\u0000\u0000\u0000\u0148\u0754\u0001\u0000\u0000"+
		"\u0000\u014a\u0756\u0001\u0000\u0000\u0000\u014c\u0759\u0001\u0000\u0000"+
		"\u0000\u014e\u075f\u0001\u0000\u0000\u0000\u0150\u0765\u0001\u0000\u0000"+
		"\u0000\u0152\u076f\u0001\u0000\u0000\u0000\u0154\u077a\u0001\u0000\u0000"+
		"\u0000\u0156\u0789\u0001\u0000\u0000\u0000\u0158\u079b\u0001\u0000\u0000"+
		"\u0000\u015a\u079d\u0001\u0000\u0000\u0000\u015c\u079f\u0001\u0000\u0000"+
		"\u0000\u015e\u07b0\u0001\u0000\u0000\u0000\u0160\u07b2\u0001\u0000\u0000"+
		"\u0000\u0162\u07bc\u0001\u0000\u0000\u0000\u0164\u07c1\u0001\u0000\u0000"+
		"\u0000\u0166\u07c6\u0001\u0000\u0000\u0000\u0168\u07ca\u0001\u0000\u0000"+
		"\u0000\u016a\u07d2\u0001\u0000\u0000\u0000\u016c\u07d6\u0001\u0000\u0000"+
		"\u0000\u016e\u07e5\u0001\u0000\u0000\u0000\u0170\u07e7\u0001\u0000\u0000"+
		"\u0000\u0172\u07ed\u0001\u0000\u0000\u0000\u0174\u07ef\u0001\u0000\u0000"+
		"\u0000\u0176\u07f6\u0001\u0000\u0000\u0000\u0178\u080a\u0001\u0000\u0000"+
		"\u0000\u017a\u083f\u0001\u0000\u0000\u0000\u017c\u0841\u0001\u0000\u0000"+
		"\u0000\u017e\u0849\u0001\u0000\u0000\u0000\u0180\u0852\u0001\u0000\u0000"+
		"\u0000\u0182\u0855\u0001\u0000\u0000\u0000\u0184\u0186\u0003x<\u0000\u0185"+
		"\u0184\u0001\u0000\u0000\u0000\u0185\u0186\u0001\u0000\u0000\u0000\u0186"+
		"\u0187\u0001\u0000\u0000\u0000\u0187\u0188\u0005\u0000\u0000\u0001\u0188"+
		"\u0001\u0001\u0000\u0000\u0000\u0189\u018b\u0003\u017c\u00be\u0000\u018a"+
		"\u0189\u0001\u0000\u0000\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c"+
		"\u018a\u0001\u0000\u0000\u0000\u018c\u018d\u0001\u0000\u0000\u0000\u018d"+
		"\u0196\u0001\u0000\u0000\u0000\u018e\u0196\u0005E\u0000\u0000\u018f\u0190"+
		"\u0005U\u0000\u0000\u0190\u0191\u0003Z-\u0000\u0191\u0192\u0005V\u0000"+
		"\u0000\u0192\u0196\u0001\u0000\u0000\u0000\u0193\u0196\u0003\u0004\u0002"+
		"\u0000\u0194\u0196\u0003\f\u0006\u0000\u0195\u018a\u0001\u0000\u0000\u0000"+
		"\u0195\u018e\u0001\u0000\u0000\u0000\u0195\u018f\u0001\u0000\u0000\u0000"+
		"\u0195\u0193\u0001\u0000\u0000\u0000\u0195\u0194\u0001\u0000\u0000\u0000"+
		"\u0196\u0003\u0001\u0000\u0000\u0000\u0197\u019a\u0003\u0006\u0003\u0000"+
		"\u0198\u019a\u0003\b\u0004\u0000\u0199\u0197\u0001\u0000\u0000\u0000\u0199"+
		"\u0198\u0001\u0000\u0000\u0000\u019a\u0005\u0001\u0000\u0000\u0000\u019b"+
		"\u01a6\u0005\u0084\u0000\u0000\u019c\u01a6\u0003\u014a\u00a5\u0000\u019d"+
		"\u01a6\u0003\u013c\u009e\u0000\u019e\u01a6\u0003\u014c\u00a6\u0000\u019f"+
		"\u01a2\u0005c\u0000\u0000\u01a0\u01a3\u0003\u0116\u008b\u0000\u01a1\u01a3"+
		"\u0003\u00a2Q\u0000\u01a2\u01a0\u0001\u0000\u0000\u0000\u01a2\u01a1\u0001"+
		"\u0000\u0000\u0000\u01a3\u01a6\u0001\u0000\u0000\u0000\u01a4\u01a6\u0003"+
		"\u0158\u00ac\u0000\u01a5\u019b\u0001\u0000\u0000\u0000\u01a5\u019c\u0001"+
		"\u0000\u0000\u0000\u01a5\u019d\u0001\u0000\u0000\u0000\u01a5\u019e\u0001"+
		"\u0000\u0000\u0000\u01a5\u019f\u0001\u0000\u0000\u0000\u01a5\u01a4\u0001"+
		"\u0000\u0000\u0000\u01a6\u0007\u0001\u0000\u0000\u0000\u01a7\u01a9\u0003"+
		"\n\u0005\u0000\u01a8\u01aa\u0005D\u0000\u0000\u01a9\u01a8\u0001\u0000"+
		"\u0000\u0000\u01a9\u01aa\u0001\u0000\u0000\u0000\u01aa\u01ab\u0001\u0000"+
		"\u0000\u0000\u01ab\u01ac\u0003\u0006\u0003\u0000\u01ac\t\u0001\u0000\u0000"+
		"\u0000\u01ad\u01b1\u0006\u0005\uffff\uffff\u0000\u01ae\u01b2\u0003\u00a0"+
		"P\u0000\u01af\u01b2\u0003\u00b8\\\u0000\u01b0\u01b2\u0003\u00a2Q\u0000"+
		"\u01b1\u01ae\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000\u0000"+
		"\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000\u0000\u0000"+
		"\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01b4\u0005\u007f\u0000\u0000"+
		"\u01b4\u01c0\u0001\u0000\u0000\u0000\u01b5\u01bb\n\u0001\u0000\u0000\u01b6"+
		"\u01bc\u0005\u0084\u0000\u0000\u01b7\u01b9\u0005D\u0000\u0000\u01b8\u01b7"+
		"\u0001\u0000\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u01ba"+
		"\u0001\u0000\u0000\u0000\u01ba\u01bc\u0003\u0156\u00ab\u0000\u01bb\u01b6"+
		"\u0001\u0000\u0000\u0000\u01bb\u01b8\u0001\u0000\u0000\u0000\u01bc\u01bd"+
		"\u0001\u0000\u0000\u0000\u01bd\u01bf\u0005\u007f\u0000\u0000\u01be\u01b5"+
		"\u0001\u0000\u0000\u0000\u01bf\u01c2\u0001\u0000\u0000\u0000\u01c0\u01be"+
		"\u0001\u0000\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000\u01c1\u000b"+
		"\u0001\u0000\u0000\u0000\u01c2\u01c0\u0001\u0000\u0000\u0000\u01c3\u01c5"+
		"\u0003\u000e\u0007\u0000\u01c4\u01c6\u0003\u001c\u000e\u0000\u01c5\u01c4"+
		"\u0001\u0000\u0000\u0000\u01c5\u01c6\u0001\u0000\u0000\u0000\u01c6\u01c7"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c8\u0003d2\u0000\u01c8\r\u0001\u0000"+
		"\u0000\u0000\u01c9\u01cb\u0005W\u0000\u0000\u01ca\u01cc\u0003\u0010\b"+
		"\u0000\u01cb\u01ca\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001\u0000\u0000"+
		"\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd\u01ce\u0005X\u0000\u0000"+
		"\u01ce\u000f\u0001\u0000\u0000\u0000\u01cf\u01d6\u0003\u0014\n\u0000\u01d0"+
		"\u01d3\u0003\u0012\t\u0000\u01d1\u01d2\u0005z\u0000\u0000\u01d2\u01d4"+
		"\u0003\u0014\n\u0000\u01d3\u01d1\u0001\u0000\u0000\u0000\u01d3\u01d4\u0001"+
		"\u0000\u0000\u0000\u01d4\u01d6\u0001\u0000\u0000\u0000\u01d5\u01cf\u0001"+
		"\u0000\u0000\u0000\u01d5\u01d0\u0001\u0000\u0000\u0000\u01d6\u0011\u0001"+
		"\u0000\u0000\u0000\u01d7\u01d8\u0007\u0000\u0000\u0000\u01d8\u0013\u0001"+
		"\u0000\u0000\u0000\u01d9\u01de\u0003\u0016\u000b\u0000\u01da\u01db\u0005"+
		"z\u0000\u0000\u01db\u01dd\u0003\u0016\u000b\u0000\u01dc\u01da\u0001\u0000"+
		"\u0000\u0000\u01dd\u01e0\u0001\u0000\u0000\u0000\u01de\u01dc\u0001\u0000"+
		"\u0000\u0000\u01de\u01df\u0001\u0000\u0000\u0000\u01df\u01e2\u0001\u0000"+
		"\u0000\u0000\u01e0\u01de\u0001\u0000\u0000\u0000\u01e1\u01e3\u0005\u0083"+
		"\u0000\u0000\u01e2\u01e1\u0001\u0000\u0000\u0000\u01e2\u01e3\u0001\u0000"+
		"\u0000\u0000\u01e3\u0015\u0001\u0000\u0000\u0000\u01e4\u01e7\u0003\u0018"+
		"\f\u0000\u01e5\u01e7\u0003\u001a\r\u0000\u01e6\u01e4\u0001\u0000\u0000"+
		"\u0000\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e7\u0017\u0001\u0000\u0000"+
		"\u0000\u01e8\u01ea\u0005a\u0000\u0000\u01e9\u01e8\u0001\u0000\u0000\u0000"+
		"\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea\u01eb\u0001\u0000\u0000\u0000"+
		"\u01eb\u01ee\u0005\u0084\u0000\u0000\u01ec\u01ee\u0005E\u0000\u0000\u01ed"+
		"\u01e9\u0001\u0000\u0000\u0000\u01ed\u01ec\u0001\u0000\u0000\u0000\u01ee"+
		"\u0019\u0001\u0000\u0000\u0000\u01ef\u01f1\u0005a\u0000\u0000\u01f0\u01ef"+
		"\u0001\u0000\u0000\u0000\u01f0\u01f1\u0001\u0000\u0000\u0000\u01f1\u01f2"+
		"\u0001\u0000\u0000\u0000\u01f2\u01f3\u0005\u0084\u0000\u0000\u01f3\u01f4"+
		"\u0003\u010c\u0086\u0000\u01f4\u001b\u0001\u0000\u0000\u0000\u01f5\u01f7"+
		"\u0005U\u0000\u0000\u01f6\u01f8\u0003\u0102\u0081\u0000\u01f7\u01f6\u0001"+
		"\u0000\u0000\u0000\u01f7\u01f8\u0001\u0000\u0000\u0000\u01f8\u01f9\u0001"+
		"\u0000\u0000\u0000\u01f9\u01fb\u0005V\u0000\u0000\u01fa\u01fc\u0005/\u0000"+
		"\u0000\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fb\u01fc\u0001\u0000\u0000"+
		"\u0000\u01fc\u01fe\u0001\u0000\u0000\u0000\u01fd\u01ff\u0003\u0172\u00b9"+
		"\u0000\u01fe\u01fd\u0001\u0000\u0000\u0000\u01fe\u01ff\u0001\u0000\u0000"+
		"\u0000\u01ff\u0201\u0001\u0000\u0000\u0000\u0200\u0202\u0003\u00ccf\u0000"+
		"\u0201\u0200\u0001\u0000\u0000\u0000\u0201\u0202\u0001\u0000\u0000\u0000"+
		"\u0202\u0204\u0001\u0000\u0000\u0000\u0203\u0205\u0003\u00eau\u0000\u0204"+
		"\u0203\u0001\u0000\u0000\u0000\u0204\u0205\u0001\u0000\u0000\u0000\u0205"+
		"\u001d\u0001\u0000\u0000\u0000\u0206\u0207\u0006\u000f\uffff\uffff\u0000"+
		"\u0207\u0225\u0003\u0002\u0001\u0000\u0208\u020b\u0003\u009eO\u0000\u0209"+
		"\u020b\u0003\u0160\u00b0\u0000\u020a\u0208\u0001\u0000\u0000\u0000\u020a"+
		"\u0209\u0001\u0000\u0000\u0000\u020b\u0212\u0001\u0000\u0000\u0000\u020c"+
		"\u020e\u0005U\u0000\u0000\u020d\u020f\u0003\"\u0011\u0000\u020e\u020d"+
		"\u0001\u0000\u0000\u0000\u020e\u020f\u0001\u0000\u0000\u0000\u020f\u0210"+
		"\u0001\u0000\u0000\u0000\u0210\u0213\u0005V\u0000\u0000\u0211\u0213\u0003"+
		"\u0114\u008a\u0000\u0212\u020c\u0001\u0000\u0000\u0000\u0212\u0211\u0001"+
		"\u0000\u0000\u0000\u0213\u0225\u0001\u0000\u0000\u0000\u0214\u0215\u0007"+
		"\u0001\u0000\u0000\u0215\u0216\u0005f\u0000\u0000\u0216\u0217\u0003\u00f6"+
		"{\u0000\u0217\u0218\u0005g\u0000\u0000\u0218\u0219\u0005U\u0000\u0000"+
		"\u0219\u021a\u0003Z-\u0000\u021a\u021b\u0005V\u0000\u0000\u021b\u0225"+
		"\u0001\u0000\u0000\u0000\u021c\u021d\u0003 \u0010\u0000\u021d\u0220\u0005"+
		"U\u0000\u0000\u021e\u0221\u0003Z-\u0000\u021f\u0221\u0003\u00f6{\u0000"+
		"\u0220\u021e\u0001\u0000\u0000\u0000\u0220\u021f\u0001\u0000\u0000\u0000"+
		"\u0221\u0222\u0001\u0000\u0000\u0000\u0222\u0223\u0005V\u0000\u0000\u0223"+
		"\u0225\u0001\u0000\u0000\u0000\u0224\u0206\u0001\u0000\u0000\u0000\u0224"+
		"\u020a\u0001\u0000\u0000\u0000\u0224\u0214\u0001\u0000\u0000\u0000\u0224"+
		"\u021c\u0001\u0000\u0000\u0000\u0225\u0241\u0001\u0000\u0000\u0000\u0226"+
		"\u0227\n\u0007\u0000\u0000\u0227\u022a\u0005W\u0000\u0000\u0228\u022b"+
		"\u0003Z-\u0000\u0229\u022b\u0003\u0114\u008a\u0000\u022a\u0228\u0001\u0000"+
		"\u0000\u0000\u022a\u0229\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000"+
		"\u0000\u0000\u022c\u022d\u0005X\u0000\u0000\u022d\u0240\u0001\u0000\u0000"+
		"\u0000\u022e\u022f\n\u0006\u0000\u0000\u022f\u0231\u0005U\u0000\u0000"+
		"\u0230\u0232\u0003\"\u0011\u0000\u0231\u0230\u0001\u0000\u0000\u0000\u0231"+
		"\u0232\u0001\u0000\u0000\u0000\u0232\u0233\u0001\u0000\u0000\u0000\u0233"+
		"\u0240\u0005V\u0000\u0000\u0234\u0235\n\u0004\u0000\u0000\u0235\u023b"+
		"\u0007\u0002\u0000\u0000\u0236\u0238\u0005D\u0000\u0000\u0237\u0236\u0001"+
		"\u0000\u0000\u0000\u0237\u0238\u0001\u0000\u0000\u0000\u0238\u0239\u0001"+
		"\u0000\u0000\u0000\u0239\u023c\u0003\u0004\u0002\u0000\u023a\u023c\u0003"+
		"$\u0012\u0000\u023b\u0237\u0001\u0000\u0000\u0000\u023b\u023a\u0001\u0000"+
		"\u0000\u0000\u023c\u0240\u0001\u0000\u0000\u0000\u023d\u023e\n\u0003\u0000"+
		"\u0000\u023e\u0240\u0007\u0003\u0000\u0000\u023f\u0226\u0001\u0000\u0000"+
		"\u0000\u023f\u022e\u0001\u0000\u0000\u0000\u023f\u0234\u0001\u0000\u0000"+
		"\u0000\u023f\u023d\u0001\u0000\u0000\u0000\u0240\u0243\u0001\u0000\u0000"+
		"\u0000\u0241\u023f\u0001\u0000\u0000\u0000\u0241\u0242\u0001\u0000\u0000"+
		"\u0000\u0242\u001f\u0001\u0000\u0000\u0000\u0243\u0241\u0001\u0000\u0000"+
		"\u0000\u0244\u0245\u0005K\u0000\u0000\u0245!\u0001\u0000\u0000\u0000\u0246"+
		"\u0247\u0003\u0112\u0089\u0000\u0247#\u0001\u0000\u0000\u0000\u0248\u024a"+
		"\u0003\n\u0005\u0000\u0249\u0248\u0001\u0000\u0000\u0000\u0249\u024a\u0001"+
		"\u0000\u0000\u0000\u024a\u024e\u0001\u0000\u0000\u0000\u024b\u024c\u0003"+
		"\u00a0P\u0000\u024c\u024d\u0005\u007f\u0000\u0000\u024d\u024f\u0001\u0000"+
		"\u0000\u0000\u024e\u024b\u0001\u0000\u0000\u0000\u024e\u024f\u0001\u0000"+
		"\u0000\u0000\u024f\u0250\u0001\u0000\u0000\u0000\u0250\u0251\u0005c\u0000"+
		"\u0000\u0251\u025c\u0003\u00a0P\u0000\u0252\u0253\u0003\n\u0005\u0000"+
		"\u0253\u0254\u0005D\u0000\u0000\u0254\u0255\u0003\u0156\u00ab\u0000\u0255"+
		"\u0256\u0005\u007f\u0000\u0000\u0256\u0257\u0005c\u0000\u0000\u0257\u0258"+
		"\u0003\u00a0P\u0000\u0258\u025c\u0001\u0000\u0000\u0000\u0259\u025a\u0005"+
		"c\u0000\u0000\u025a\u025c\u0003\u00a2Q\u0000\u025b\u0249\u0001\u0000\u0000"+
		"\u0000\u025b\u0252\u0001\u0000\u0000\u0000\u025b\u0259\u0001\u0000\u0000"+
		"\u0000\u025c%\u0001\u0000\u0000\u0000\u025d\u0279\u0003\u001e\u000f\u0000"+
		"\u025e\u0263\u0005x\u0000\u0000\u025f\u0263\u0005y\u0000\u0000\u0260\u0263"+
		"\u0003(\u0014\u0000\u0261\u0263\u0005>\u0000\u0000\u0262\u025e\u0001\u0000"+
		"\u0000\u0000\u0262\u025f\u0001\u0000\u0000\u0000\u0262\u0260\u0001\u0000"+
		"\u0000\u0000\u0262\u0261\u0001\u0000\u0000\u0000\u0263\u0264\u0001\u0000"+
		"\u0000\u0000\u0264\u0279\u0003&\u0013\u0000\u0265\u026e\u0005>\u0000\u0000"+
		"\u0266\u0267\u0005U\u0000\u0000\u0267\u0268\u0003\u00f6{\u0000\u0268\u0269"+
		"\u0005V\u0000\u0000\u0269\u026f\u0001\u0000\u0000\u0000\u026a\u026b\u0005"+
		"\u0083\u0000\u0000\u026b\u026c\u0005U\u0000\u0000\u026c\u026d\u0005\u0084"+
		"\u0000\u0000\u026d\u026f\u0005V\u0000\u0000\u026e\u0266\u0001\u0000\u0000"+
		"\u0000\u026e\u026a\u0001\u0000\u0000\u0000\u026f\u0279\u0001\u0000\u0000"+
		"\u0000\u0270\u0271\u0005\u000b\u0000\u0000\u0271\u0272\u0005U\u0000\u0000"+
		"\u0272\u0273\u0003\u00f6{\u0000\u0273\u0274\u0005V\u0000\u0000\u0274\u0279"+
		"\u0001\u0000\u0000\u0000\u0275\u0279\u00038\u001c\u0000\u0276\u0279\u0003"+
		"*\u0015\u0000\u0277\u0279\u00036\u001b\u0000\u0278\u025d\u0001\u0000\u0000"+
		"\u0000\u0278\u0262\u0001\u0000\u0000\u0000\u0278\u0265\u0001\u0000\u0000"+
		"\u0000\u0278\u0270\u0001\u0000\u0000\u0000\u0278\u0275\u0001\u0000\u0000"+
		"\u0000\u0278\u0276\u0001\u0000\u0000\u0000\u0278\u0277\u0001\u0000\u0000"+
		"\u0000\u0279\'\u0001\u0000\u0000\u0000\u027a\u027b\u0007\u0004\u0000\u0000"+
		"\u027b)\u0001\u0000\u0000\u0000\u027c\u027e\u0005\u007f\u0000\u0000\u027d"+
		"\u027c\u0001\u0000\u0000\u0000\u027d\u027e\u0001\u0000\u0000\u0000\u027e"+
		"\u027f\u0001\u0000\u0000\u0000\u027f\u0281\u00051\u0000\u0000\u0280\u0282"+
		"\u0003,\u0016\u0000\u0281\u0280\u0001\u0000\u0000\u0000\u0281\u0282\u0001"+
		"\u0000\u0000\u0000\u0282\u0288\u0001\u0000\u0000\u0000\u0283\u0289\u0003"+
		".\u0017\u0000\u0284\u0285\u0005U\u0000\u0000\u0285\u0286\u0003\u00f6{"+
		"\u0000\u0286\u0287\u0005V\u0000\u0000\u0287\u0289\u0001\u0000\u0000\u0000"+
		"\u0288\u0283\u0001\u0000\u0000\u0000\u0288\u0284\u0001\u0000\u0000\u0000"+
		"\u0289\u028b\u0001\u0000\u0000\u0000\u028a\u028c\u00034\u001a\u0000\u028b"+
		"\u028a\u0001\u0000\u0000\u0000\u028b\u028c\u0001\u0000\u0000\u0000\u028c"+
		"+\u0001\u0000\u0000\u0000\u028d\u028e\u0005U\u0000\u0000\u028e\u028f\u0003"+
		"\"\u0011\u0000\u028f\u0290\u0005V\u0000\u0000\u0290-\u0001\u0000\u0000"+
		"\u0000\u0291\u0293\u0003\u0096K\u0000\u0292\u0294\u00030\u0018\u0000\u0293"+
		"\u0292\u0001\u0000\u0000\u0000\u0293\u0294\u0001\u0000\u0000\u0000\u0294"+
		"/\u0001\u0000\u0000\u0000\u0295\u0297\u0003\u00ecv\u0000\u0296\u0298\u0003"+
		"0\u0018\u0000\u0297\u0296\u0001\u0000\u0000\u0000\u0297\u0298\u0001\u0000"+
		"\u0000\u0000\u0298\u029b\u0001\u0000\u0000\u0000\u0299\u029b\u00032\u0019"+
		"\u0000\u029a\u0295\u0001\u0000\u0000\u0000\u029a\u0299\u0001\u0000\u0000"+
		"\u0000\u029b1\u0001\u0000\u0000\u0000\u029c\u029d\u0006\u0019\uffff\uffff"+
		"\u0000\u029d\u029e\u0005W\u0000\u0000\u029e\u029f\u0003Z-\u0000\u029f"+
		"\u02a1\u0005X\u0000\u0000\u02a0\u02a2\u0003\u00ccf\u0000\u02a1\u02a0\u0001"+
		"\u0000\u0000\u0000\u02a1\u02a2\u0001\u0000\u0000\u0000\u02a2\u02ac\u0001"+
		"\u0000\u0000\u0000\u02a3\u02a4\n\u0001\u0000\u0000\u02a4\u02a5\u0005W"+
		"\u0000\u0000\u02a5\u02a6\u0003\\.\u0000\u02a6\u02a8\u0005X\u0000\u0000"+
		"\u02a7\u02a9\u0003\u00ccf\u0000\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a8"+
		"\u02a9\u0001\u0000\u0000\u0000\u02a9\u02ab\u0001\u0000\u0000\u0000\u02aa"+
		"\u02a3\u0001\u0000\u0000\u0000\u02ab\u02ae\u0001\u0000\u0000\u0000\u02ac"+
		"\u02aa\u0001\u0000\u0000\u0000\u02ac\u02ad\u0001\u0000\u0000\u0000\u02ad"+
		"3\u0001\u0000\u0000\u0000\u02ae\u02ac\u0001\u0000\u0000\u0000\u02af\u02b1"+
		"\u0005U\u0000\u0000\u02b0\u02b2\u0003\"\u0011\u0000\u02b1\u02b0\u0001"+
		"\u0000\u0000\u0000\u02b1\u02b2\u0001\u0000\u0000\u0000\u02b2\u02b3\u0001"+
		"\u0000\u0000\u0000\u02b3\u02b6\u0005V\u0000\u0000\u02b4\u02b6\u0003\u0114"+
		"\u008a\u0000\u02b5\u02af\u0001\u0000\u0000\u0000\u02b5\u02b4\u0001\u0000"+
		"\u0000\u0000\u02b65\u0001\u0000\u0000\u0000\u02b7\u02b9\u0005\u007f\u0000"+
		"\u0000\u02b8\u02b7\u0001\u0000\u0000\u0000\u02b8\u02b9\u0001\u0000\u0000"+
		"\u0000\u02b9\u02ba\u0001\u0000\u0000\u0000\u02ba\u02bd\u0005\u001c\u0000"+
		"\u0000\u02bb\u02bc\u0005W\u0000\u0000\u02bc\u02be\u0005X\u0000\u0000\u02bd"+
		"\u02bb\u0001\u0000\u0000\u0000\u02bd\u02be\u0001\u0000\u0000\u0000\u02be"+
		"\u02bf\u0001\u0000\u0000\u0000\u02bf\u02c0\u0003:\u001d\u0000\u02c07\u0001"+
		"\u0000\u0000\u0000\u02c1\u02c2\u00052\u0000\u0000\u02c2\u02c3\u0005U\u0000"+
		"\u0000\u02c3\u02c4\u0003Z-\u0000\u02c4\u02c5\u0005V\u0000\u0000\u02c5"+
		"9\u0001\u0000\u0000\u0000\u02c6\u02cd\u0003&\u0013\u0000\u02c7\u02c8\u0005"+
		"U\u0000\u0000\u02c8\u02c9\u0003\u00f6{\u0000\u02c9\u02ca\u0005V\u0000"+
		"\u0000\u02ca\u02cb\u0003:\u001d\u0000\u02cb\u02cd\u0001\u0000\u0000\u0000"+
		"\u02cc\u02c6\u0001\u0000\u0000\u0000\u02cc\u02c7\u0001\u0000\u0000\u0000"+
		"\u02cd;\u0001\u0000\u0000\u0000\u02ce\u02d3\u0003:\u001d\u0000\u02cf\u02d0"+
		"\u0007\u0005\u0000\u0000\u02d0\u02d2\u0003:\u001d\u0000\u02d1\u02cf\u0001"+
		"\u0000\u0000\u0000\u02d2\u02d5\u0001\u0000\u0000\u0000\u02d3\u02d1\u0001"+
		"\u0000\u0000\u0000\u02d3\u02d4\u0001\u0000\u0000\u0000\u02d4=\u0001\u0000"+
		"\u0000\u0000\u02d5\u02d3\u0001\u0000\u0000\u0000\u02d6\u02db\u0003<\u001e"+
		"\u0000\u02d7\u02d8\u0007\u0006\u0000\u0000\u02d8\u02da\u0003<\u001e\u0000"+
		"\u02d9\u02d7\u0001\u0000\u0000\u0000\u02da\u02dd\u0001\u0000\u0000\u0000"+
		"\u02db\u02d9\u0001\u0000\u0000\u0000\u02db\u02dc\u0001\u0000\u0000\u0000"+
		"\u02dc?\u0001\u0000\u0000\u0000\u02dd\u02db\u0001\u0000\u0000\u0000\u02de"+
		"\u02e3\u0003>\u001f\u0000\u02df\u02e0\u0007\u0007\u0000\u0000\u02e0\u02e2"+
		"\u0003>\u001f\u0000\u02e1\u02df\u0001\u0000\u0000\u0000\u02e2\u02e5\u0001"+
		"\u0000\u0000\u0000\u02e3\u02e1\u0001\u0000\u0000\u0000\u02e3\u02e4\u0001"+
		"\u0000\u0000\u0000\u02e4A\u0001\u0000\u0000\u0000\u02e5\u02e3\u0001\u0000"+
		"\u0000\u0000\u02e6\u02ec\u0003@ \u0000\u02e7\u02e8\u0003D\"\u0000\u02e8"+
		"\u02e9\u0003@ \u0000\u02e9\u02eb\u0001\u0000\u0000\u0000\u02ea\u02e7\u0001"+
		"\u0000\u0000\u0000\u02eb\u02ee\u0001\u0000\u0000\u0000\u02ec\u02ea\u0001"+
		"\u0000\u0000\u0000\u02ec\u02ed\u0001\u0000\u0000\u0000\u02edC\u0001\u0000"+
		"\u0000\u0000\u02ee\u02ec\u0001\u0000\u0000\u0000\u02ef\u02f0\u0005g\u0000"+
		"\u0000\u02f0\u02f4\u0005g\u0000\u0000\u02f1\u02f2\u0005f\u0000\u0000\u02f2"+
		"\u02f4\u0005f\u0000\u0000\u02f3\u02ef\u0001\u0000\u0000\u0000\u02f3\u02f1"+
		"\u0001\u0000\u0000\u0000\u02f4E\u0001\u0000\u0000\u0000\u02f5\u02fa\u0003"+
		"B!\u0000\u02f6\u02f7\u0007\b\u0000\u0000\u02f7\u02f9\u0003B!\u0000\u02f8"+
		"\u02f6\u0001\u0000\u0000\u0000\u02f9\u02fc\u0001\u0000\u0000\u0000\u02fa"+
		"\u02f8\u0001\u0000\u0000\u0000\u02fa\u02fb\u0001\u0000\u0000\u0000\u02fb"+
		"G\u0001\u0000\u0000\u0000\u02fc\u02fa\u0001\u0000\u0000\u0000\u02fd\u0302"+
		"\u0003F#\u0000\u02fe\u02ff\u0007\t\u0000\u0000\u02ff\u0301\u0003F#\u0000"+
		"\u0300\u02fe\u0001\u0000\u0000\u0000\u0301\u0304\u0001\u0000\u0000\u0000"+
		"\u0302\u0300\u0001\u0000\u0000\u0000\u0302\u0303\u0001\u0000\u0000\u0000"+
		"\u0303I\u0001\u0000\u0000\u0000\u0304\u0302\u0001\u0000\u0000\u0000\u0305"+
		"\u030a\u0003H$\u0000\u0306\u0307\u0005a\u0000\u0000\u0307\u0309\u0003"+
		"H$\u0000\u0308\u0306\u0001\u0000\u0000\u0000\u0309\u030c\u0001\u0000\u0000"+
		"\u0000\u030a\u0308\u0001\u0000\u0000\u0000\u030a\u030b\u0001\u0000\u0000"+
		"\u0000\u030bK\u0001\u0000\u0000\u0000\u030c\u030a\u0001\u0000\u0000\u0000"+
		"\u030d\u0312\u0003J%\u0000\u030e\u030f\u0005`\u0000\u0000\u030f\u0311"+
		"\u0003J%\u0000\u0310\u030e\u0001\u0000\u0000\u0000\u0311\u0314\u0001\u0000"+
		"\u0000\u0000\u0312\u0310\u0001\u0000\u0000\u0000\u0312\u0313\u0001\u0000"+
		"\u0000\u0000\u0313M\u0001\u0000\u0000\u0000\u0314\u0312\u0001\u0000\u0000"+
		"\u0000\u0315\u031a\u0003L&\u0000\u0316\u0317\u0005b\u0000\u0000\u0317"+
		"\u0319\u0003L&\u0000\u0318\u0316\u0001\u0000\u0000\u0000\u0319\u031c\u0001"+
		"\u0000\u0000\u0000\u031a\u0318\u0001\u0000\u0000\u0000\u031a\u031b\u0001"+
		"\u0000\u0000\u0000\u031bO\u0001\u0000\u0000\u0000\u031c\u031a\u0001\u0000"+
		"\u0000\u0000\u031d\u0322\u0003N\'\u0000\u031e\u031f\u0005v\u0000\u0000"+
		"\u031f\u0321\u0003N\'\u0000\u0320\u031e\u0001\u0000\u0000\u0000\u0321"+
		"\u0324\u0001\u0000\u0000\u0000\u0322\u0320\u0001\u0000\u0000\u0000\u0322"+
		"\u0323\u0001\u0000\u0000\u0000\u0323Q\u0001\u0000\u0000\u0000\u0324\u0322"+
		"\u0001\u0000\u0000\u0000\u0325\u032a\u0003P(\u0000\u0326\u0327\u0005w"+
		"\u0000\u0000\u0327\u0329\u0003P(\u0000\u0328\u0326\u0001\u0000\u0000\u0000"+
		"\u0329\u032c\u0001\u0000\u0000\u0000\u032a\u0328\u0001\u0000\u0000\u0000"+
		"\u032a\u032b\u0001\u0000\u0000\u0000\u032bS\u0001\u0000\u0000\u0000\u032c"+
		"\u032a\u0001\u0000\u0000\u0000\u032d\u0333\u0003R)\u0000\u032e\u032f\u0005"+
		"}\u0000\u0000\u032f\u0330\u0003Z-\u0000\u0330\u0331\u0005~\u0000\u0000"+
		"\u0331\u0332\u0003V+\u0000\u0332\u0334\u0001\u0000\u0000\u0000\u0333\u032e"+
		"\u0001\u0000\u0000\u0000\u0333\u0334\u0001\u0000\u0000\u0000\u0334U\u0001"+
		"\u0000\u0000\u0000\u0335\u033c\u0003T*\u0000\u0336\u0337\u0003R)\u0000"+
		"\u0337\u0338\u0003X,\u0000\u0338\u0339\u0003\u0110\u0088\u0000\u0339\u033c"+
		"\u0001\u0000\u0000\u0000\u033a\u033c\u0003\u0170\u00b8\u0000\u033b\u0335"+
		"\u0001\u0000\u0000\u0000\u033b\u0336\u0001\u0000\u0000\u0000\u033b\u033a"+
		"\u0001\u0000\u0000\u0000\u033cW\u0001\u0000\u0000\u0000\u033d\u033e\u0007"+
		"\n\u0000\u0000\u033eY\u0001\u0000\u0000\u0000\u033f\u0344\u0003V+\u0000"+
		"\u0340\u0341\u0005z\u0000\u0000\u0341\u0343\u0003V+\u0000\u0342\u0340"+
		"\u0001\u0000\u0000\u0000\u0343\u0346\u0001\u0000\u0000\u0000\u0344\u0342"+
		"\u0001\u0000\u0000\u0000\u0344\u0345\u0001\u0000\u0000\u0000\u0345[\u0001"+
		"\u0000\u0000\u0000\u0346\u0344\u0001\u0000\u0000\u0000\u0347\u0348\u0003"+
		"T*\u0000\u0348]\u0001\u0000\u0000\u0000\u0349\u0357\u0003`0\u0000\u034a"+
		"\u0357\u0003v;\u0000\u034b\u034d\u0003\u00ccf\u0000\u034c\u034b\u0001"+
		"\u0000\u0000\u0000\u034c\u034d\u0001\u0000\u0000\u0000\u034d\u0354\u0001"+
		"\u0000\u0000\u0000\u034e\u0355\u0003b1\u0000\u034f\u0355\u0003d2\u0000"+
		"\u0350\u0355\u0003h4\u0000\u0351\u0355\u0003l6\u0000\u0352\u0355\u0003"+
		"t:\u0000\u0353\u0355\u0003\u0166\u00b3\u0000\u0354\u034e\u0001\u0000\u0000"+
		"\u0000\u0354\u034f\u0001\u0000\u0000\u0000\u0354\u0350\u0001\u0000\u0000"+
		"\u0000\u0354\u0351\u0001\u0000\u0000\u0000\u0354\u0352\u0001\u0000\u0000"+
		"\u0000\u0354\u0353\u0001\u0000\u0000\u0000\u0355\u0357\u0001\u0000\u0000"+
		"\u0000\u0356\u0349\u0001\u0000\u0000\u0000\u0356\u034a\u0001\u0000\u0000"+
		"\u0000\u0356\u034c\u0001\u0000\u0000\u0000\u0357_\u0001\u0000\u0000\u0000"+
		"\u0358\u035a\u0003\u00ccf\u0000\u0359\u0358\u0001\u0000\u0000\u0000\u0359"+
		"\u035a\u0001\u0000\u0000\u0000\u035a\u035f\u0001\u0000\u0000\u0000\u035b"+
		"\u0360\u0005\u0084\u0000\u0000\u035c\u035d\u0005\u0010\u0000\u0000\u035d"+
		"\u0360\u0003\\.\u0000\u035e\u0360\u0005\u001b\u0000\u0000\u035f\u035b"+
		"\u0001\u0000\u0000\u0000\u035f\u035c\u0001\u0000\u0000\u0000\u035f\u035e"+
		"\u0001\u0000\u0000\u0000\u0360\u0361\u0001\u0000\u0000\u0000\u0361\u0362"+
		"\u0005~\u0000\u0000\u0362\u0363\u0003^/\u0000\u0363a\u0001\u0000\u0000"+
		"\u0000\u0364\u0366\u0003Z-\u0000\u0365\u0364\u0001\u0000\u0000\u0000\u0365"+
		"\u0366\u0001\u0000\u0000\u0000\u0366\u0367\u0001\u0000\u0000\u0000\u0367"+
		"\u0368\u0005\u0080\u0000\u0000\u0368c\u0001\u0000\u0000\u0000\u0369\u036b"+
		"\u0005Y\u0000\u0000\u036a\u036c\u0003f3\u0000\u036b\u036a\u0001\u0000"+
		"\u0000\u0000\u036b\u036c\u0001\u0000\u0000\u0000\u036c\u036d\u0001\u0000"+
		"\u0000\u0000\u036d\u036e\u0005Z\u0000\u0000\u036ee\u0001\u0000\u0000\u0000"+
		"\u036f\u0371\u0003^/\u0000\u0370\u036f\u0001\u0000\u0000\u0000\u0371\u0372"+
		"\u0001\u0000\u0000\u0000\u0372\u0370\u0001\u0000\u0000\u0000\u0372\u0373"+
		"\u0001\u0000\u0000\u0000\u0373g\u0001\u0000\u0000\u0000\u0374\u0375\u0005"+
		"+\u0000\u0000\u0375\u0376\u0005U\u0000\u0000\u0376\u0377\u0003j5\u0000"+
		"\u0377\u0378\u0005V\u0000\u0000\u0378\u037b\u0003^/\u0000\u0379\u037a"+
		"\u0005 \u0000\u0000\u037a\u037c\u0003^/\u0000\u037b\u0379\u0001\u0000"+
		"\u0000\u0000\u037b\u037c\u0001\u0000\u0000\u0000\u037c\u0384\u0001\u0000"+
		"\u0000\u0000\u037d\u037e\u0005C\u0000\u0000\u037e\u037f\u0005U\u0000\u0000"+
		"\u037f\u0380\u0003j5\u0000\u0380\u0381\u0005V\u0000\u0000\u0381\u0382"+
		"\u0003^/\u0000\u0382\u0384\u0001\u0000\u0000\u0000\u0383\u0374\u0001\u0000"+
		"\u0000\u0000\u0383\u037d\u0001\u0000\u0000\u0000\u0384i\u0001\u0000\u0000"+
		"\u0000\u0385\u0391\u0003Z-\u0000\u0386\u0388\u0003\u00ccf\u0000\u0387"+
		"\u0386\u0001\u0000\u0000\u0000\u0387\u0388\u0001\u0000\u0000\u0000\u0388"+
		"\u0389\u0001\u0000\u0000\u0000\u0389\u038a\u0003\u008aE\u0000\u038a\u038e"+
		"\u0003\u00e2q\u0000\u038b\u038c\u0005e\u0000\u0000\u038c\u038f\u0003\u0110"+
		"\u0088\u0000\u038d\u038f\u0003\u0114\u008a\u0000\u038e\u038b\u0001\u0000"+
		"\u0000\u0000\u038e\u038d\u0001\u0000\u0000\u0000\u038f\u0391\u0001\u0000"+
		"\u0000\u0000\u0390\u0385\u0001\u0000\u0000\u0000\u0390\u0387\u0001\u0000"+
		"\u0000\u0000\u0391k\u0001\u0000\u0000\u0000\u0392\u0393\u0005T\u0000\u0000"+
		"\u0393\u0394\u0005U\u0000\u0000\u0394\u0395\u0003j5\u0000\u0395\u0396"+
		"\u0005V\u0000\u0000\u0396\u0397\u0003^/\u0000\u0397\u03b4\u0001\u0000"+
		"\u0000\u0000\u0398\u0399\u0005\u001d\u0000\u0000\u0399\u039a\u0003^/\u0000"+
		"\u039a\u039b\u0005T\u0000\u0000\u039b\u039c\u0005U\u0000\u0000\u039c\u039d"+
		"\u0003Z-\u0000\u039d\u039e\u0005V\u0000\u0000\u039e\u039f\u0005\u0080"+
		"\u0000\u0000\u039f\u03b4\u0001\u0000\u0000\u0000\u03a0\u03a1\u0005(\u0000"+
		"\u0000\u03a1\u03ae\u0005U\u0000\u0000\u03a2\u03a4\u0003n7\u0000\u03a3"+
		"\u03a5\u0003j5\u0000\u03a4\u03a3\u0001\u0000\u0000\u0000\u03a4\u03a5\u0001"+
		"\u0000\u0000\u0000\u03a5\u03a6\u0001\u0000\u0000\u0000\u03a6\u03a8\u0005"+
		"\u0080\u0000\u0000\u03a7\u03a9\u0003Z-\u0000\u03a8\u03a7\u0001\u0000\u0000"+
		"\u0000\u03a8\u03a9\u0001\u0000\u0000\u0000\u03a9\u03af\u0001\u0000\u0000"+
		"\u0000\u03aa\u03ab\u0003p8\u0000\u03ab\u03ac\u0005~\u0000\u0000\u03ac"+
		"\u03ad\u0003r9\u0000\u03ad\u03af\u0001\u0000\u0000\u0000\u03ae\u03a2\u0001"+
		"\u0000\u0000\u0000\u03ae\u03aa\u0001\u0000\u0000\u0000\u03af\u03b0\u0001"+
		"\u0000\u0000\u0000\u03b0\u03b1\u0005V\u0000\u0000\u03b1\u03b2\u0003^/"+
		"\u0000\u03b2\u03b4\u0001\u0000\u0000\u0000\u03b3\u0392\u0001\u0000\u0000"+
		"\u0000\u03b3\u0398\u0001\u0000\u0000\u0000\u03b3\u03a0\u0001\u0000\u0000"+
		"\u0000\u03b4m\u0001\u0000\u0000\u0000\u03b5\u03b8\u0003b1\u0000\u03b6"+
		"\u03b8\u0003\u0080@\u0000\u03b7\u03b5\u0001\u0000\u0000\u0000\u03b7\u03b6"+
		"\u0001\u0000\u0000\u0000\u03b8o\u0001\u0000\u0000\u0000\u03b9\u03bb\u0003"+
		"\u00ccf\u0000\u03ba\u03b9\u0001\u0000\u0000\u0000\u03ba\u03bb\u0001\u0000"+
		"\u0000\u0000\u03bb\u03bc\u0001\u0000\u0000\u0000\u03bc\u03bd\u0003\u008a"+
		"E\u0000\u03bd\u03be\u0003\u00e2q\u0000\u03beq\u0001\u0000\u0000\u0000"+
		"\u03bf\u03c2\u0003Z-\u0000\u03c0\u03c2\u0003\u0114\u008a\u0000\u03c1\u03bf"+
		"\u0001\u0000\u0000\u0000\u03c1\u03c0\u0001\u0000\u0000\u0000\u03c2s\u0001"+
		"\u0000\u0000\u0000\u03c3\u03cd\u0005\u000f\u0000\u0000\u03c4\u03cd\u0005"+
		"\u0019\u0000\u0000\u03c5\u03c8\u0005;\u0000\u0000\u03c6\u03c9\u0003Z-"+
		"\u0000\u03c7\u03c9\u0003\u0114\u008a\u0000\u03c8\u03c6\u0001\u0000\u0000"+
		"\u0000\u03c8\u03c7\u0001\u0000\u0000\u0000\u03c8\u03c9\u0001\u0000\u0000"+
		"\u0000\u03c9\u03cd\u0001\u0000\u0000\u0000\u03ca\u03cb\u0005*\u0000\u0000"+
		"\u03cb\u03cd\u0005\u0084\u0000\u0000\u03cc\u03c3\u0001\u0000\u0000\u0000"+
		"\u03cc\u03c4\u0001\u0000\u0000\u0000\u03cc\u03c5\u0001\u0000\u0000\u0000"+
		"\u03cc\u03ca\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000"+
		"\u03ce\u03cf\u0005\u0080\u0000\u0000\u03cfu\u0001\u0000\u0000\u0000\u03d0"+
		"\u03d1\u0003|>\u0000\u03d1w\u0001\u0000\u0000\u0000\u03d2\u03d4\u0003"+
		"z=\u0000\u03d3\u03d2\u0001\u0000\u0000\u0000\u03d4\u03d5\u0001\u0000\u0000"+
		"\u0000\u03d5\u03d3\u0001\u0000\u0000\u0000\u03d5\u03d6\u0001\u0000\u0000"+
		"\u0000\u03d6y\u0001\u0000\u0000\u0000\u03d7\u03e1\u0003|>\u0000\u03d8"+
		"\u03e1\u0003\u0108\u0084\u0000\u03d9\u03e1\u0003\u014e\u00a7\u0000\u03da"+
		"\u03e1\u0003\u0162\u00b1\u0000\u03db\u03e1\u0003\u0164\u00b2\u0000\u03dc"+
		"\u03e1\u0003\u00cae\u0000\u03dd\u03e1\u0003\u00bc^\u0000\u03de\u03e1\u0003"+
		"\u0084B\u0000\u03df\u03e1\u0003\u0086C\u0000\u03e0\u03d7\u0001\u0000\u0000"+
		"\u0000\u03e0\u03d8\u0001\u0000\u0000\u0000\u03e0\u03d9\u0001\u0000\u0000"+
		"\u0000\u03e0\u03da\u0001\u0000\u0000\u0000\u03e0\u03db\u0001\u0000\u0000"+
		"\u0000\u03e0\u03dc\u0001\u0000\u0000\u0000\u03e0\u03dd\u0001\u0000\u0000"+
		"\u0000\u03e0\u03de\u0001\u0000\u0000\u0000\u03e0\u03df\u0001\u0000\u0000"+
		"\u0000\u03e1{\u0001\u0000\u0000\u0000\u03e2\u03eb\u0003\u0080@\u0000\u03e3"+
		"\u03eb\u0003\u00c8d\u0000\u03e4\u03eb\u0003\u00c0`\u0000\u03e5\u03eb\u0003"+
		"\u00c4b\u0000\u03e6\u03eb\u0003\u00c6c\u0000\u03e7\u03eb\u0003\u0082A"+
		"\u0000\u03e8\u03eb\u0003~?\u0000\u03e9\u03eb\u0003\u00acV\u0000\u03ea"+
		"\u03e2\u0001\u0000\u0000\u0000\u03ea\u03e3\u0001\u0000\u0000\u0000\u03ea"+
		"\u03e4\u0001\u0000\u0000\u0000\u03ea\u03e5\u0001\u0000\u0000\u0000\u03ea"+
		"\u03e6\u0001\u0000\u0000\u0000\u03ea\u03e7\u0001\u0000\u0000\u0000\u03ea"+
		"\u03e8\u0001\u0000\u0000\u0000\u03ea\u03e9\u0001\u0000\u0000\u0000\u03eb"+
		"}\u0001\u0000\u0000\u0000\u03ec\u03ed\u0005O\u0000\u0000\u03ed\u03ef\u0005"+
		"\u0084\u0000\u0000\u03ee\u03f0\u0003\u00ccf\u0000\u03ef\u03ee\u0001\u0000"+
		"\u0000\u0000\u03ef\u03f0\u0001\u0000\u0000\u0000\u03f0\u03f1\u0001\u0000"+
		"\u0000\u0000\u03f1\u03f2\u0005e\u0000\u0000\u03f2\u03f3\u0003\u00f6{\u0000"+
		"\u03f3\u03f4\u0005\u0080\u0000\u0000\u03f4\u007f\u0001\u0000\u0000\u0000"+
		"\u03f5\u03f7\u0003\u008aE\u0000\u03f6\u03f5\u0001\u0000\u0000\u0000\u03f6"+
		"\u03f7\u0001\u0000\u0000\u0000\u03f7\u03f9\u0001\u0000\u0000\u0000\u03f8"+
		"\u03fa\u0003\u00deo\u0000\u03f9\u03f8\u0001\u0000\u0000\u0000\u03f9\u03fa"+
		"\u0001\u0000\u0000\u0000\u03fa\u03fb\u0001\u0000\u0000\u0000\u03fb\u0404"+
		"\u0005\u0080\u0000\u0000\u03fc\u03fe\u0003\u00ccf\u0000\u03fd\u03ff\u0003"+
		"\u008aE\u0000\u03fe\u03fd\u0001\u0000\u0000\u0000\u03fe\u03ff\u0001\u0000"+
		"\u0000\u0000\u03ff\u0400\u0001\u0000\u0000\u0000\u0400\u0401\u0003\u00de"+
		"o\u0000\u0401\u0402\u0005\u0080\u0000\u0000\u0402\u0404\u0001\u0000\u0000"+
		"\u0000\u0403\u03f6\u0001\u0000\u0000\u0000\u0403\u03fc\u0001\u0000\u0000"+
		"\u0000\u0404\u0081\u0001\u0000\u0000\u0000\u0405\u0406\u0005@\u0000\u0000"+
		"\u0406\u0407\u0005U\u0000\u0000\u0407\u0408\u0003\\.\u0000\u0408\u0409"+
		"\u0005z\u0000\u0000\u0409\u040a\u0005\u0004\u0000\u0000\u040a\u040b\u0005"+
		"V\u0000\u0000\u040b\u040c\u0005\u0080\u0000\u0000\u040c\u0083\u0001\u0000"+
		"\u0000\u0000\u040d\u040e\u0005\u0080\u0000\u0000\u040e\u0085\u0001\u0000"+
		"\u0000\u0000\u040f\u0410\u0003\u00ccf\u0000\u0410\u0411\u0005\u0080\u0000"+
		"\u0000\u0411\u0087\u0001\u0000\u0000\u0000\u0412\u0419\u0003\u008cF\u0000"+
		"\u0413\u0419\u0003\u0092I\u0000\u0414\u0419\u0003\u008eG\u0000\u0415\u0419"+
		"\u0005)\u0000\u0000\u0416\u0419\u0005J\u0000\u0000\u0417\u0419\u0005\u0017"+
		"\u0000\u0000\u0418\u0412\u0001\u0000\u0000\u0000\u0418\u0413\u0001\u0000"+
		"\u0000\u0000\u0418\u0414\u0001\u0000\u0000\u0000\u0418\u0415\u0001\u0000"+
		"\u0000\u0000\u0418\u0416\u0001\u0000\u0000\u0000\u0418\u0417\u0001\u0000"+
		"\u0000\u0000\u0419\u0089\u0001\u0000\u0000\u0000\u041a\u041c\u0003\u0088"+
		"D\u0000\u041b\u041a\u0001\u0000\u0000\u0000\u041c\u041d\u0001\u0000\u0000"+
		"\u0000\u041d\u041e\u0001\u0000\u0000\u0000\u041d\u041b\u0001\u0000\u0000"+
		"\u0000\u041e\u0420\u0001\u0000\u0000\u0000\u041f\u0421\u0003\u00ccf\u0000"+
		"\u0420\u041f\u0001\u0000\u0000\u0000\u0420\u0421\u0001\u0000\u0000\u0000"+
		"\u0421\u008b\u0001\u0000\u0000\u0000\u0422\u0423\u0007\u000b\u0000\u0000"+
		"\u0423\u008d\u0001\u0000\u0000\u0000\u0424\u0425\u0007\f\u0000\u0000\u0425"+
		"\u008f\u0001\u0000\u0000\u0000\u0426\u0427\u0005\u0084\u0000\u0000\u0427"+
		"\u0091\u0001\u0000\u0000\u0000\u0428\u042c\u0003\u0094J\u0000\u0429\u042c"+
		"\u0003\u0118\u008c\u0000\u042a\u042c\u0003\u00a8T\u0000\u042b\u0428\u0001"+
		"\u0000\u0000\u0000\u042b\u0429\u0001\u0000\u0000\u0000\u042b\u042a\u0001"+
		"\u0000\u0000\u0000\u042c\u0093\u0001\u0000\u0000\u0000\u042d\u0432\u0003"+
		"\u009eO\u0000\u042e\u0432\u0003\u00a4R\u0000\u042f\u0432\u0003\u0160\u00b0"+
		"\u0000\u0430\u0432\u0003\u00f0x\u0000\u0431\u042d\u0001\u0000\u0000\u0000"+
		"\u0431\u042e\u0001\u0000\u0000\u0000\u0431\u042f\u0001\u0000\u0000\u0000"+
		"\u0431\u0430\u0001\u0000\u0000\u0000\u0432\u0095\u0001\u0000\u0000\u0000"+
		"\u0433\u0435\u0003\u0092I\u0000\u0434\u0433\u0001\u0000\u0000\u0000\u0435"+
		"\u0436\u0001\u0000\u0000\u0000\u0436\u0434\u0001\u0000\u0000\u0000\u0436"+
		"\u0437\u0001\u0000\u0000\u0000\u0437\u0439\u0001\u0000\u0000\u0000\u0438"+
		"\u043a\u0003\u00ccf\u0000\u0439\u0438\u0001\u0000\u0000\u0000\u0439\u043a"+
		"\u0001\u0000\u0000\u0000\u043a\u0097\u0001\u0000\u0000\u0000\u043b\u043d"+
		"\u0003\u0094J\u0000\u043c\u043b\u0001\u0000\u0000\u0000\u043d\u043e\u0001"+
		"\u0000\u0000\u0000\u043e\u043c\u0001\u0000\u0000\u0000\u043e\u043f\u0001"+
		"\u0000\u0000\u0000\u043f\u0441\u0001\u0000\u0000\u0000\u0440\u0442\u0003"+
		"\u00ccf\u0000\u0441\u0440\u0001\u0000\u0000\u0000\u0441\u0442\u0001\u0000"+
		"\u0000\u0000\u0442\u0099\u0001\u0000\u0000\u0000\u0443\u0444\u0007\r\u0000"+
		"\u0000\u0444\u009b\u0001\u0000\u0000\u0000\u0445\u0446\u0007\u000e\u0000"+
		"\u0000\u0446\u009d\u0001\u0000\u0000\u0000\u0447\u0449\u0003\n\u0005\u0000"+
		"\u0448\u0447\u0001\u0000\u0000\u0000\u0448\u0449\u0001\u0000\u0000\u0000"+
		"\u0449\u044a\u0001\u0000\u0000\u0000\u044a\u047c\u0003\u00a0P\u0000\u044b"+
		"\u044c\u0003\n\u0005\u0000\u044c\u044d\u0005D\u0000\u0000\u044d\u044e"+
		"\u0003\u0156\u00ab\u0000\u044e\u047c\u0001\u0000\u0000\u0000\u044f\u047c"+
		"\u0003\u009cN\u0000\u0450\u0452\u0003\u009cN\u0000\u0451\u0450\u0001\u0000"+
		"\u0000\u0000\u0451\u0452\u0001\u0000\u0000\u0000\u0452\u0454\u0001\u0000"+
		"\u0000\u0000\u0453\u0455\u0003\u009aM\u0000\u0454\u0453\u0001\u0000\u0000"+
		"\u0000\u0455\u0456\u0001\u0000\u0000\u0000\u0456\u0454\u0001\u0000\u0000"+
		"\u0000\u0456\u0457\u0001\u0000\u0000\u0000\u0457\u047c\u0001\u0000\u0000"+
		"\u0000\u0458\u045a\u0003\u009cN\u0000\u0459\u0458\u0001\u0000\u0000\u0000"+
		"\u0459\u045a\u0001\u0000\u0000\u0000\u045a\u045b\u0001\u0000\u0000\u0000"+
		"\u045b\u047c\u0005\u0012\u0000\u0000\u045c\u045e\u0003\u009cN\u0000\u045d"+
		"\u045c\u0001\u0000\u0000\u0000\u045d\u045e\u0001\u0000\u0000\u0000\u045e"+
		"\u045f\u0001\u0000\u0000\u0000\u045f\u047c\u0005\u0013\u0000\u0000\u0460"+
		"\u0462\u0003\u009cN\u0000\u0461\u0460\u0001\u0000\u0000\u0000\u0461\u0462"+
		"\u0001\u0000\u0000\u0000\u0462\u0463\u0001\u0000\u0000\u0000\u0463\u047c"+
		"\u0005\u0014\u0000\u0000\u0464\u0466\u0003\u009cN\u0000\u0465\u0464\u0001"+
		"\u0000\u0000\u0000\u0465\u0466\u0001\u0000\u0000\u0000\u0466\u0467\u0001"+
		"\u0000\u0000\u0000\u0467\u047c\u0005S\u0000\u0000\u0468\u047c\u0005\u000e"+
		"\u0000\u0000\u0469\u046b\u0003\u009cN\u0000\u046a\u0469\u0001\u0000\u0000"+
		"\u0000\u046a\u046b\u0001\u0000\u0000\u0000\u046b\u046f\u0001\u0000\u0000"+
		"\u0000\u046c\u046e\u0003\u009aM\u0000\u046d\u046c\u0001\u0000\u0000\u0000"+
		"\u046e\u0471\u0001\u0000\u0000\u0000\u046f\u046d\u0001\u0000\u0000\u0000"+
		"\u046f\u0470\u0001\u0000\u0000\u0000\u0470\u0472\u0001\u0000\u0000\u0000"+
		"\u0471\u046f\u0001\u0000\u0000\u0000\u0472\u047c\u0005-\u0000\u0000\u0473"+
		"\u047c\u0005\'\u0000\u0000\u0474\u0476\u0003\u009aM\u0000\u0475\u0474"+
		"\u0001\u0000\u0000\u0000\u0475\u0476\u0001\u0000\u0000\u0000\u0476\u0477"+
		"\u0001\u0000\u0000\u0000\u0477\u047c\u0005\u001e\u0000\u0000\u0478\u047c"+
		"\u0005Q\u0000\u0000\u0479\u047c\u0005\r\u0000\u0000\u047a\u047c\u0003"+
		"\u00a2Q\u0000\u047b\u0448\u0001\u0000\u0000\u0000\u047b\u044b\u0001\u0000"+
		"\u0000\u0000\u047b\u044f\u0001\u0000\u0000\u0000\u047b\u0451\u0001\u0000"+
		"\u0000\u0000\u047b\u0459\u0001\u0000\u0000\u0000\u047b\u045d\u0001\u0000"+
		"\u0000\u0000\u047b\u0461\u0001\u0000\u0000\u0000\u047b\u0465\u0001\u0000"+
		"\u0000\u0000\u047b\u0468\u0001\u0000\u0000\u0000\u047b\u046a\u0001\u0000"+
		"\u0000\u0000\u047b\u0473\u0001\u0000\u0000\u0000\u047b\u0475\u0001\u0000"+
		"\u0000\u0000\u047b\u0478\u0001\u0000\u0000\u0000\u047b\u0479\u0001\u0000"+
		"\u0000\u0000\u047b\u047a\u0001\u0000\u0000\u0000\u047c\u009f\u0001\u0000"+
		"\u0000\u0000\u047d\u0482\u0003\u0116\u008b\u0000\u047e\u0482\u0003\u00a6"+
		"S\u0000\u047f\u0482\u0003\u0090H\u0000\u0480\u0482\u0003\u0156\u00ab\u0000"+
		"\u0481\u047d\u0001\u0000\u0000\u0000\u0481\u047e\u0001\u0000\u0000\u0000"+
		"\u0481\u047f\u0001\u0000\u0000\u0000\u0481\u0480\u0001\u0000\u0000\u0000"+
		"\u0482\u00a1\u0001\u0000\u0000\u0000\u0483\u0484\u0005\u001a\u0000\u0000"+
		"\u0484\u0487\u0005U\u0000\u0000\u0485\u0488\u0003Z-\u0000\u0486\u0488"+
		"\u0005\r\u0000\u0000\u0487\u0485\u0001\u0000\u0000\u0000\u0487\u0486\u0001"+
		"\u0000\u0000\u0000\u0488\u0489\u0001\u0000\u0000\u0000\u0489\u048a\u0005"+
		"V\u0000\u0000\u048a\u00a3\u0001\u0000\u0000\u0000\u048b\u049a\u0003\u0120"+
		"\u0090\u0000\u048c\u048e\u0003\u00ccf\u0000\u048d\u048c\u0001\u0000\u0000"+
		"\u0000\u048d\u048e\u0001\u0000\u0000\u0000\u048e\u0490\u0001\u0000\u0000"+
		"\u0000\u048f\u0491\u0003\n\u0005\u0000\u0490\u048f\u0001\u0000\u0000\u0000"+
		"\u0490\u0491\u0001\u0000\u0000\u0000\u0491\u0492\u0001\u0000\u0000\u0000"+
		"\u0492\u049b\u0005\u0084\u0000\u0000\u0493\u049b\u0003\u0156\u00ab\u0000"+
		"\u0494\u0496\u0003\n\u0005\u0000\u0495\u0497\u0005D\u0000\u0000\u0496"+
		"\u0495\u0001\u0000\u0000\u0000\u0496\u0497\u0001\u0000\u0000\u0000\u0497"+
		"\u0498\u0001\u0000\u0000\u0000\u0498\u0499\u0003\u0156\u00ab\u0000\u0499"+
		"\u049b\u0001\u0000\u0000\u0000\u049a\u048d\u0001\u0000\u0000\u0000\u049a"+
		"\u0493\u0001\u0000\u0000\u0000\u049a\u0494\u0001\u0000\u0000\u0000\u049b"+
		"\u04a2\u0001\u0000\u0000\u0000\u049c\u049e\u0005!\u0000\u0000\u049d\u049f"+
		"\u0003\n\u0005\u0000\u049e\u049d\u0001\u0000\u0000\u0000\u049e\u049f\u0001"+
		"\u0000\u0000\u0000\u049f\u04a0\u0001\u0000\u0000\u0000\u04a0\u04a2\u0005"+
		"\u0084\u0000\u0000\u04a1\u048b\u0001\u0000\u0000\u0000\u04a1\u049c\u0001"+
		"\u0000\u0000\u0000\u04a2\u00a5\u0001\u0000\u0000\u0000\u04a3\u04a4\u0005"+
		"\u0084\u0000\u0000\u04a4\u00a7\u0001\u0000\u0000\u0000\u04a5\u04a6\u0003"+
		"\u00aaU\u0000\u04a6\u04ab\u0005Y\u0000\u0000\u04a7\u04a9\u0003\u00b2Y"+
		"\u0000\u04a8\u04aa\u0005z\u0000\u0000\u04a9\u04a8\u0001\u0000\u0000\u0000"+
		"\u04a9\u04aa\u0001\u0000\u0000\u0000\u04aa\u04ac\u0001\u0000\u0000\u0000"+
		"\u04ab\u04a7\u0001\u0000\u0000\u0000\u04ab\u04ac\u0001\u0000\u0000\u0000"+
		"\u04ac\u04ad\u0001\u0000\u0000\u0000\u04ad\u04ae\u0005Z\u0000\u0000\u04ae"+
		"\u00a9\u0001\u0000\u0000\u0000\u04af\u04b1\u0003\u00aeW\u0000\u04b0\u04b2"+
		"\u0003\u00ccf\u0000\u04b1\u04b0\u0001\u0000\u0000\u0000\u04b1\u04b2\u0001"+
		"\u0000\u0000\u0000\u04b2\u04b7\u0001\u0000\u0000\u0000\u04b3\u04b5\u0003"+
		"\n\u0005\u0000\u04b4\u04b3\u0001\u0000\u0000\u0000\u04b4\u04b5\u0001\u0000"+
		"\u0000\u0000\u04b5\u04b6\u0001\u0000\u0000\u0000\u04b6\u04b8\u0005\u0084"+
		"\u0000\u0000\u04b7\u04b4\u0001\u0000\u0000\u0000\u04b7\u04b8\u0001\u0000"+
		"\u0000\u0000\u04b8\u04ba\u0001\u0000\u0000\u0000\u04b9\u04bb\u0003\u00b0"+
		"X\u0000\u04ba\u04b9\u0001\u0000\u0000\u0000\u04ba\u04bb\u0001\u0000\u0000"+
		"\u0000\u04bb\u00ab\u0001\u0000\u0000\u0000\u04bc\u04be\u0003\u00aeW\u0000"+
		"\u04bd\u04bf\u0003\u00ccf\u0000\u04be\u04bd\u0001\u0000\u0000\u0000\u04be"+
		"\u04bf\u0001\u0000\u0000\u0000\u04bf\u04c0\u0001\u0000\u0000\u0000\u04c0"+
		"\u04c2\u0005\u0084\u0000\u0000\u04c1\u04c3\u0003\u00b0X\u0000\u04c2\u04c1"+
		"\u0001\u0000\u0000\u0000\u04c2\u04c3\u0001\u0000\u0000\u0000\u04c3\u04c4"+
		"\u0001\u0000\u0000\u0000\u04c4\u04c5\u0005\u0080\u0000\u0000\u04c5\u00ad"+
		"\u0001\u0000\u0000\u0000\u04c6\u04c8\u0005!\u0000\u0000\u04c7\u04c9\u0007"+
		"\u000f\u0000\u0000\u04c8\u04c7\u0001\u0000\u0000\u0000\u04c8\u04c9\u0001"+
		"\u0000\u0000\u0000\u04c9\u00af\u0001\u0000\u0000\u0000\u04ca\u04cb\u0005"+
		"~\u0000\u0000\u04cb\u04cc\u0003\u0096K\u0000\u04cc\u00b1\u0001\u0000\u0000"+
		"\u0000\u04cd\u04d2\u0003\u00b4Z\u0000\u04ce\u04cf\u0005z\u0000\u0000\u04cf"+
		"\u04d1\u0003\u00b4Z\u0000\u04d0\u04ce\u0001\u0000\u0000\u0000\u04d1\u04d4"+
		"\u0001\u0000\u0000\u0000\u04d2\u04d0\u0001\u0000\u0000\u0000\u04d2\u04d3"+
		"\u0001\u0000\u0000\u0000\u04d3\u00b3\u0001\u0000\u0000\u0000\u04d4\u04d2"+
		"\u0001\u0000\u0000\u0000\u04d5\u04d8\u0003\u00b6[\u0000\u04d6\u04d7\u0005"+
		"e\u0000\u0000\u04d7\u04d9\u0003\\.\u0000\u04d8\u04d6\u0001\u0000\u0000"+
		"\u0000\u04d8\u04d9\u0001\u0000\u0000\u0000\u04d9\u00b5\u0001\u0000\u0000"+
		"\u0000\u04da\u04db\u0005\u0084\u0000\u0000\u04db\u00b7\u0001\u0000\u0000"+
		"\u0000\u04dc\u04df\u0003\u00ba]\u0000\u04dd\u04df\u0003\u00be_\u0000\u04de"+
		"\u04dc\u0001\u0000\u0000\u0000\u04de\u04dd\u0001\u0000\u0000\u0000\u04df"+
		"\u00b9\u0001\u0000\u0000\u0000\u04e0\u04e1\u0005\u0084\u0000\u0000\u04e1"+
		"\u00bb\u0001\u0000\u0000\u0000\u04e2\u04e4\u0005,\u0000\u0000\u04e3\u04e2"+
		"\u0001\u0000\u0000\u0000\u04e3\u04e4\u0001\u0000\u0000\u0000\u04e4\u04e5"+
		"\u0001\u0000\u0000\u0000\u04e5\u04e8\u00050\u0000\u0000\u04e6\u04e9\u0005"+
		"\u0084\u0000\u0000\u04e7\u04e9\u0003\u00ba]\u0000\u04e8\u04e6\u0001\u0000"+
		"\u0000\u0000\u04e8\u04e7\u0001\u0000\u0000\u0000\u04e8\u04e9\u0001\u0000"+
		"\u0000\u0000\u04e9\u04ea\u0001\u0000\u0000\u0000\u04ea\u04ec\u0005Y\u0000"+
		"\u0000\u04eb\u04ed\u0003x<\u0000\u04ec\u04eb\u0001\u0000\u0000\u0000\u04ec"+
		"\u04ed\u0001\u0000\u0000\u0000\u04ed\u04ee\u0001\u0000\u0000\u0000\u04ee"+
		"\u04ef\u0005Z\u0000\u0000\u04ef\u00bd\u0001\u0000\u0000\u0000\u04f0\u04f1"+
		"\u0005\u0084\u0000\u0000\u04f1\u00bf\u0001\u0000\u0000\u0000\u04f2\u04f3"+
		"\u00050\u0000\u0000\u04f3\u04f4\u0005\u0084\u0000\u0000\u04f4\u04f5\u0005"+
		"e\u0000\u0000\u04f5\u04f6\u0003\u00c2a\u0000\u04f6\u04f7\u0005\u0080\u0000"+
		"\u0000\u04f7\u00c1\u0001\u0000\u0000\u0000\u04f8\u04fa\u0003\n\u0005\u0000"+
		"\u04f9\u04f8\u0001\u0000\u0000\u0000\u04f9\u04fa\u0001\u0000\u0000\u0000"+
		"\u04fa\u04fb\u0001\u0000\u0000\u0000\u04fb\u04fc\u0003\u00b8\\\u0000\u04fc"+
		"\u00c3\u0001\u0000\u0000\u0000\u04fd\u0503\u0005O\u0000\u0000\u04fe\u0500"+
		"\u0005L\u0000\u0000\u04ff\u04fe\u0001\u0000\u0000\u0000\u04ff\u0500\u0001"+
		"\u0000\u0000\u0000\u0500\u0501\u0001\u0000\u0000\u0000\u0501\u0504\u0003"+
		"\n\u0005\u0000\u0502\u0504\u0005\u007f\u0000\u0000\u0503\u04ff\u0001\u0000"+
		"\u0000\u0000\u0503\u0502\u0001\u0000\u0000\u0000\u0504\u0505\u0001\u0000"+
		"\u0000\u0000\u0505\u0506\u0003\u0006\u0003\u0000\u0506\u0507\u0005\u0080"+
		"\u0000\u0000\u0507\u00c5\u0001\u0000\u0000\u0000\u0508\u050a\u0003\u00cc"+
		"f\u0000\u0509\u0508\u0001\u0000\u0000\u0000\u0509\u050a\u0001\u0000\u0000"+
		"\u0000\u050a\u050b\u0001\u0000\u0000\u0000\u050b\u050c\u0005O\u0000\u0000"+
		"\u050c\u050e\u00050\u0000\u0000\u050d\u050f\u0003\n\u0005\u0000\u050e"+
		"\u050d\u0001\u0000\u0000\u0000\u050e\u050f\u0001\u0000\u0000\u0000\u050f"+
		"\u0510\u0001\u0000\u0000\u0000\u0510\u0511\u0003\u00b8\\\u0000\u0511\u0512"+
		"\u0005\u0080\u0000\u0000\u0512\u00c7\u0001\u0000\u0000\u0000\u0513\u0514"+
		"\u0005\f\u0000\u0000\u0514\u0515\u0005U\u0000\u0000\u0515\u0516\u0005"+
		"\u0004\u0000\u0000\u0516\u0517\u0005V\u0000\u0000\u0517\u0518\u0005\u0080"+
		"\u0000\u0000\u0518\u00c9\u0001\u0000\u0000\u0000\u0519\u051a\u0005$\u0000"+
		"\u0000\u051a\u0521\u0005\u0004\u0000\u0000\u051b\u051d\u0005Y\u0000\u0000"+
		"\u051c\u051e\u0003x<\u0000\u051d\u051c\u0001\u0000\u0000\u0000\u051d\u051e"+
		"\u0001\u0000\u0000\u0000\u051e\u051f\u0001\u0000\u0000\u0000\u051f\u0522"+
		"\u0005Z\u0000\u0000\u0520\u0522\u0003z=\u0000\u0521\u051b\u0001\u0000"+
		"\u0000\u0000\u0521\u0520\u0001\u0000\u0000\u0000\u0522\u00cb\u0001\u0000"+
		"\u0000\u0000\u0523\u0525\u0003\u00ceg\u0000\u0524\u0523\u0001\u0000\u0000"+
		"\u0000\u0525\u0526\u0001\u0000\u0000\u0000\u0526\u0524\u0001\u0000\u0000"+
		"\u0000\u0526\u0527\u0001\u0000\u0000\u0000\u0527\u00cd\u0001\u0000\u0000"+
		"\u0000\u0528\u0529\u0005W\u0000\u0000\u0529\u052b\u0005W\u0000\u0000\u052a"+
		"\u052c\u0003\u00d2i\u0000\u052b\u052a\u0001\u0000\u0000\u0000\u052b\u052c"+
		"\u0001\u0000\u0000\u0000\u052c\u052d\u0001\u0000\u0000\u0000\u052d\u052e"+
		"\u0005X\u0000\u0000\u052e\u0531\u0005X\u0000\u0000\u052f\u0531\u0003\u00d0"+
		"h\u0000\u0530\u0528\u0001\u0000\u0000\u0000\u0530\u052f\u0001\u0000\u0000"+
		"\u0000\u0531\u00cf\u0001\u0000\u0000\u0000\u0532\u0533\u0005\n\u0000\u0000"+
		"\u0533\u0536\u0005U\u0000\u0000\u0534\u0537\u0003\u00f6{\u0000\u0535\u0537"+
		"\u0003\\.\u0000\u0536\u0534\u0001\u0000\u0000\u0000\u0536\u0535\u0001"+
		"\u0000\u0000\u0000\u0537\u0539\u0001\u0000\u0000\u0000\u0538\u053a\u0005"+
		"\u0083\u0000\u0000\u0539\u0538\u0001\u0000\u0000\u0000\u0539\u053a\u0001"+
		"\u0000\u0000\u0000\u053a\u053b\u0001\u0000\u0000\u0000\u053b\u053c\u0005"+
		"V\u0000\u0000\u053c\u00d1\u0001\u0000\u0000\u0000\u053d\u0542\u0003\u00d4"+
		"j\u0000\u053e\u053f\u0005z\u0000\u0000\u053f\u0541\u0003\u00d4j\u0000"+
		"\u0540\u053e\u0001\u0000\u0000\u0000\u0541\u0544\u0001\u0000\u0000\u0000"+
		"\u0542\u0540\u0001\u0000\u0000\u0000\u0542\u0543\u0001\u0000\u0000\u0000"+
		"\u0543\u0546\u0001\u0000\u0000\u0000\u0544\u0542\u0001\u0000\u0000\u0000"+
		"\u0545\u0547\u0005\u0083\u0000\u0000\u0546\u0545\u0001\u0000\u0000\u0000"+
		"\u0546\u0547\u0001\u0000\u0000\u0000\u0547\u00d3\u0001\u0000\u0000\u0000"+
		"\u0548\u0549\u0003\u00d6k\u0000\u0549\u054a\u0005\u007f\u0000\u0000\u054a"+
		"\u054c\u0001\u0000\u0000\u0000\u054b\u0548\u0001\u0000\u0000\u0000\u054b"+
		"\u054c\u0001\u0000\u0000\u0000\u054c\u054d\u0001\u0000\u0000\u0000\u054d"+
		"\u054f\u0005\u0084\u0000\u0000\u054e\u0550\u0003\u00d8l\u0000\u054f\u054e"+
		"\u0001\u0000\u0000\u0000\u054f\u0550\u0001\u0000\u0000\u0000\u0550\u00d5"+
		"\u0001\u0000\u0000\u0000\u0551\u0552\u0005\u0084\u0000\u0000\u0552\u00d7"+
		"\u0001\u0000\u0000\u0000\u0553\u0555\u0005U\u0000\u0000\u0554\u0556\u0003"+
		"\u00dam\u0000\u0555\u0554\u0001\u0000\u0000\u0000\u0555\u0556\u0001\u0000"+
		"\u0000\u0000\u0556\u0557\u0001\u0000\u0000\u0000\u0557\u0558\u0005V\u0000"+
		"\u0000\u0558\u00d9\u0001\u0000\u0000\u0000\u0559\u055b\u0003\u00dcn\u0000"+
		"\u055a\u0559\u0001\u0000\u0000\u0000\u055b\u055c\u0001\u0000\u0000\u0000"+
		"\u055c\u055a\u0001\u0000\u0000\u0000\u055c\u055d\u0001\u0000\u0000\u0000"+
		"\u055d\u00db\u0001\u0000\u0000\u0000\u055e\u055f\u0005U\u0000\u0000\u055f"+
		"\u0560\u0003\u00dam\u0000\u0560\u0561\u0005V\u0000\u0000\u0561\u0570\u0001"+
		"\u0000\u0000\u0000\u0562\u0563\u0005W\u0000\u0000\u0563\u0564\u0003\u00da"+
		"m\u0000\u0564\u0565\u0005X\u0000\u0000\u0565\u0570\u0001\u0000\u0000\u0000"+
		"\u0566\u0567\u0005Y\u0000\u0000\u0567\u0568\u0003\u00dam\u0000\u0568\u0569"+
		"\u0005Z\u0000\u0000\u0569\u0570\u0001\u0000\u0000\u0000\u056a\u056c\b"+
		"\u0010\u0000\u0000\u056b\u056a\u0001\u0000\u0000\u0000\u056c\u056d\u0001"+
		"\u0000\u0000\u0000\u056d\u056b\u0001\u0000\u0000\u0000\u056d\u056e\u0001"+
		"\u0000\u0000\u0000\u056e\u0570\u0001\u0000\u0000\u0000\u056f\u055e\u0001"+
		"\u0000\u0000\u0000\u056f\u0562\u0001\u0000\u0000\u0000\u056f\u0566\u0001"+
		"\u0000\u0000\u0000\u056f\u056b\u0001\u0000\u0000\u0000\u0570\u00dd\u0001"+
		"\u0000\u0000\u0000\u0571\u0576\u0003\u00e0p\u0000\u0572\u0573\u0005z\u0000"+
		"\u0000\u0573\u0575\u0003\u00e0p\u0000\u0574\u0572\u0001\u0000\u0000\u0000"+
		"\u0575\u0578\u0001\u0000\u0000\u0000\u0576\u0574\u0001\u0000\u0000\u0000"+
		"\u0576\u0577\u0001\u0000\u0000\u0000\u0577\u00df\u0001\u0000\u0000\u0000"+
		"\u0578\u0576\u0001\u0000\u0000\u0000\u0579\u057b\u0003\u00e2q\u0000\u057a"+
		"\u057c\u0003\u010c\u0086\u0000\u057b\u057a\u0001\u0000\u0000\u0000\u057b"+
		"\u057c\u0001\u0000\u0000\u0000\u057c\u00e1\u0001\u0000\u0000\u0000\u057d"+
		"\u0583\u0003\u00e4r\u0000\u057e\u057f\u0003\u00e6s\u0000\u057f\u0580\u0003"+
		"\u00e8t\u0000\u0580\u0581\u0003\u00eau\u0000\u0581\u0583\u0001\u0000\u0000"+
		"\u0000\u0582\u057d\u0001\u0000\u0000\u0000\u0582\u057e\u0001\u0000\u0000"+
		"\u0000\u0583\u00e3\u0001\u0000\u0000\u0000\u0584\u0586\u0003\u00ecv\u0000"+
		"\u0585\u0587\u0005\u0016\u0000\u0000\u0586\u0585\u0001\u0000\u0000\u0000"+
		"\u0586\u0587\u0001\u0000\u0000\u0000\u0587\u0589\u0001\u0000\u0000\u0000"+
		"\u0588\u0584\u0001\u0000\u0000\u0000\u0589\u058c\u0001\u0000\u0000\u0000"+
		"\u058a\u0588\u0001\u0000\u0000\u0000\u058a\u058b\u0001\u0000\u0000\u0000"+
		"\u058b\u058d\u0001\u0000\u0000\u0000\u058c\u058a\u0001\u0000\u0000\u0000"+
		"\u058d\u058e\u0003\u00e6s\u0000\u058e\u00e5\u0001\u0000\u0000\u0000\u058f"+
		"\u0590\u0006s\uffff\uffff\u0000\u0590\u0592\u0003\u00f4z\u0000\u0591\u0593"+
		"\u0003\u00ccf\u0000\u0592\u0591\u0001\u0000\u0000\u0000\u0592\u0593\u0001"+
		"\u0000\u0000\u0000\u0593\u0599\u0001\u0000\u0000\u0000\u0594\u0595\u0005"+
		"U\u0000\u0000\u0595\u0596\u0003\u00e4r\u0000\u0596\u0597\u0005V\u0000"+
		"\u0000\u0597\u0599\u0001\u0000\u0000\u0000\u0598\u058f\u0001\u0000\u0000"+
		"\u0000\u0598\u0594\u0001\u0000\u0000\u0000\u0599\u05a8\u0001\u0000\u0000"+
		"\u0000\u059a\u05a4\n\u0002\u0000\u0000\u059b\u05a5\u0003\u00e8t\u0000"+
		"\u059c\u059e\u0005W\u0000\u0000\u059d\u059f\u0003\\.\u0000\u059e\u059d"+
		"\u0001\u0000\u0000\u0000\u059e\u059f\u0001\u0000\u0000\u0000\u059f\u05a0"+
		"\u0001\u0000\u0000\u0000\u05a0\u05a2\u0005X\u0000\u0000\u05a1\u05a3\u0003"+
		"\u00ccf\u0000\u05a2\u05a1\u0001\u0000\u0000\u0000\u05a2\u05a3\u0001\u0000"+
		"\u0000\u0000\u05a3\u05a5\u0001\u0000\u0000\u0000\u05a4\u059b\u0001\u0000"+
		"\u0000\u0000\u05a4\u059c\u0001\u0000\u0000\u0000\u05a5\u05a7\u0001\u0000"+
		"\u0000\u0000\u05a6\u059a\u0001\u0000\u0000\u0000\u05a7\u05aa\u0001\u0000"+
		"\u0000\u0000\u05a8\u05a6\u0001\u0000\u0000\u0000\u05a8\u05a9\u0001\u0000"+
		"\u0000\u0000\u05a9\u00e7\u0001\u0000\u0000\u0000\u05aa\u05a8\u0001\u0000"+
		"\u0000\u0000\u05ab\u05ad\u0005U\u0000\u0000\u05ac\u05ae\u0003\u0102\u0081"+
		"\u0000\u05ad\u05ac\u0001\u0000\u0000\u0000\u05ad\u05ae\u0001\u0000\u0000"+
		"\u0000\u05ae\u05af\u0001\u0000\u0000\u0000\u05af\u05b1\u0005V\u0000\u0000"+
		"\u05b0\u05b2\u0003\u00eew\u0000\u05b1\u05b0\u0001\u0000\u0000\u0000\u05b1"+
		"\u05b2\u0001\u0000\u0000\u0000\u05b2\u05b4\u0001\u0000\u0000\u0000\u05b3"+
		"\u05b5\u0003\u00f2y\u0000\u05b4\u05b3\u0001\u0000\u0000\u0000\u05b4\u05b5"+
		"\u0001\u0000\u0000\u0000\u05b5\u05b7\u0001\u0000\u0000\u0000\u05b6\u05b8"+
		"\u0003\u0172\u00b9\u0000\u05b7\u05b6\u0001\u0000\u0000\u0000\u05b7\u05b8"+
		"\u0001\u0000\u0000\u0000\u05b8\u05ba\u0001\u0000\u0000\u0000\u05b9\u05bb"+
		"\u0003\u00ccf\u0000\u05ba\u05b9\u0001\u0000\u0000\u0000\u05ba\u05bb\u0001"+
		"\u0000\u0000\u0000\u05bb\u00e9\u0001\u0000\u0000\u0000\u05bc\u05bd\u0005"+
		"|\u0000\u0000\u05bd\u05bf\u0003\u0098L\u0000\u05be\u05c0\u0003\u00f8|"+
		"\u0000\u05bf\u05be\u0001\u0000\u0000\u0000\u05bf\u05c0\u0001\u0000\u0000"+
		"\u0000\u05c0\u00eb\u0001\u0000\u0000\u0000\u05c1\u05c3\u0007\u0011\u0000"+
		"\u0000\u05c2\u05c4\u0003\u00ccf\u0000\u05c3\u05c2\u0001\u0000\u0000\u0000"+
		"\u05c3\u05c4\u0001\u0000\u0000\u0000\u05c4\u05d0\u0001\u0000\u0000\u0000"+
		"\u05c5\u05c7\u0003\n\u0005\u0000\u05c6\u05c5\u0001\u0000\u0000\u0000\u05c6"+
		"\u05c7\u0001\u0000\u0000\u0000\u05c7\u05c8\u0001\u0000\u0000\u0000\u05c8"+
		"\u05ca\u0005]\u0000\u0000\u05c9\u05cb\u0003\u00ccf\u0000\u05ca\u05c9\u0001"+
		"\u0000\u0000\u0000\u05ca\u05cb\u0001\u0000\u0000\u0000\u05cb\u05cd\u0001"+
		"\u0000\u0000\u0000\u05cc\u05ce\u0003\u00eew\u0000\u05cd\u05cc\u0001\u0000"+
		"\u0000\u0000\u05cd\u05ce\u0001\u0000\u0000\u0000\u05ce\u05d0\u0001\u0000"+
		"\u0000\u0000\u05cf\u05c1\u0001\u0000\u0000\u0000\u05cf\u05c6\u0001\u0000"+
		"\u0000\u0000\u05d0\u00ed\u0001\u0000\u0000\u0000\u05d1\u05d3\u0003\u00f0"+
		"x\u0000\u05d2\u05d1\u0001\u0000\u0000\u0000\u05d3\u05d4\u0001\u0000\u0000"+
		"\u0000\u05d4\u05d2\u0001\u0000\u0000\u0000\u05d4\u05d5\u0001\u0000\u0000"+
		"\u0000\u05d5\u00ef\u0001\u0000\u0000\u0000\u05d6\u05d7\u0007\u0012\u0000"+
		"\u0000\u05d7\u00f1\u0001\u0000\u0000\u0000\u05d8\u05d9\u0007\u0011\u0000"+
		"\u0000\u05d9\u00f3\u0001\u0000\u0000\u0000\u05da\u05dc\u0005\u0083\u0000"+
		"\u0000\u05db\u05da\u0001\u0000\u0000\u0000\u05db\u05dc\u0001\u0000\u0000"+
		"\u0000\u05dc\u05dd\u0001\u0000\u0000\u0000\u05dd\u05de\u0003\u0004\u0002"+
		"\u0000\u05de\u00f5\u0001\u0000\u0000\u0000\u05df\u05e1\u0003\u0096K\u0000"+
		"\u05e0\u05e2\u0003\u00f8|\u0000\u05e1\u05e0\u0001\u0000\u0000\u0000\u05e1"+
		"\u05e2\u0001\u0000\u0000\u0000\u05e2\u00f7\u0001\u0000\u0000\u0000\u05e3"+
		"\u05ec\u0003\u00fa}\u0000\u05e4\u05e6\u0003\u00fc~\u0000\u05e5\u05e4\u0001"+
		"\u0000\u0000\u0000\u05e5\u05e6\u0001\u0000\u0000\u0000\u05e6\u05e7\u0001"+
		"\u0000\u0000\u0000\u05e7\u05e8\u0003\u00e8t\u0000\u05e8\u05e9\u0003\u00ea"+
		"u\u0000\u05e9\u05ec\u0001\u0000\u0000\u0000\u05ea\u05ec\u0003\u00fe\u007f"+
		"\u0000\u05eb\u05e3\u0001\u0000\u0000\u0000\u05eb\u05e5\u0001\u0000\u0000"+
		"\u0000\u05eb\u05ea\u0001\u0000\u0000\u0000\u05ec\u00f9\u0001\u0000\u0000"+
		"\u0000\u05ed\u05f7\u0003\u00fc~\u0000\u05ee\u05f0\u0003\u00ecv\u0000\u05ef"+
		"\u05ee\u0001\u0000\u0000\u0000\u05f0\u05f1\u0001\u0000\u0000\u0000\u05f1"+
		"\u05ef\u0001\u0000\u0000\u0000\u05f1\u05f2\u0001\u0000\u0000\u0000\u05f2"+
		"\u05f4\u0001\u0000\u0000\u0000\u05f3\u05f5\u0003\u00fc~\u0000\u05f4\u05f3"+
		"\u0001\u0000\u0000\u0000\u05f4\u05f5\u0001\u0000\u0000\u0000\u05f5\u05f7"+
		"\u0001\u0000\u0000\u0000\u05f6\u05ed\u0001\u0000\u0000\u0000\u05f6\u05ef"+
		"\u0001\u0000\u0000\u0000\u05f7\u00fb\u0001\u0000\u0000\u0000\u05f8\u05f9"+
		"\u0006~\uffff\uffff\u0000\u05f9\u0607\u0003\u00e8t\u0000\u05fa\u05fc\u0005"+
		"W\u0000\u0000\u05fb\u05fd\u0003\\.\u0000\u05fc\u05fb\u0001\u0000\u0000"+
		"\u0000\u05fc\u05fd\u0001\u0000\u0000\u0000\u05fd\u05fe\u0001\u0000\u0000"+
		"\u0000\u05fe\u0600\u0005X\u0000\u0000\u05ff\u0601\u0003\u00ccf\u0000\u0600"+
		"\u05ff\u0001\u0000\u0000\u0000\u0600\u0601\u0001\u0000\u0000\u0000\u0601"+
		"\u0607\u0001\u0000\u0000\u0000\u0602\u0603\u0005U\u0000\u0000\u0603\u0604"+
		"\u0003\u00fa}\u0000\u0604\u0605\u0005V\u0000\u0000\u0605\u0607\u0001\u0000"+
		"\u0000\u0000\u0606\u05f8\u0001\u0000\u0000\u0000\u0606\u05fa\u0001\u0000"+
		"\u0000\u0000\u0606\u0602\u0001\u0000\u0000\u0000\u0607\u0617\u0001\u0000"+
		"\u0000\u0000\u0608\u0613\n\u0004\u0000\u0000\u0609\u0614\u0003\u00e8t"+
		"\u0000\u060a\u060b\u0003\u00fc~\u0000\u060b\u060d\u0005W\u0000\u0000\u060c"+
		"\u060e\u0003\\.\u0000\u060d\u060c\u0001\u0000\u0000\u0000\u060d\u060e"+
		"\u0001\u0000\u0000\u0000\u060e\u060f\u0001\u0000\u0000\u0000\u060f\u0611"+
		"\u0005X\u0000\u0000\u0610\u0612\u0003\u00ccf\u0000\u0611\u0610\u0001\u0000"+
		"\u0000\u0000\u0611\u0612\u0001\u0000\u0000\u0000\u0612\u0614\u0001\u0000"+
		"\u0000\u0000\u0613\u0609\u0001\u0000\u0000\u0000\u0613\u060a\u0001\u0000"+
		"\u0000\u0000\u0614\u0616\u0001\u0000\u0000\u0000\u0615\u0608\u0001\u0000"+
		"\u0000\u0000\u0616\u0619\u0001\u0000\u0000\u0000\u0617\u0615\u0001\u0000"+
		"\u0000\u0000\u0617\u0618\u0001\u0000\u0000\u0000\u0618\u00fd\u0001\u0000"+
		"\u0000\u0000\u0619\u0617\u0001\u0000\u0000\u0000\u061a\u061c\u0003\u00ec"+
		"v\u0000\u061b\u061a\u0001\u0000\u0000\u0000\u061c\u061f\u0001\u0000\u0000"+
		"\u0000\u061d\u061b\u0001\u0000\u0000\u0000\u061d\u061e\u0001\u0000\u0000"+
		"\u0000\u061e\u0620\u0001\u0000\u0000\u0000\u061f\u061d\u0001\u0000\u0000"+
		"\u0000\u0620\u0621\u0003\u0100\u0080\u0000\u0621\u00ff\u0001\u0000\u0000"+
		"\u0000\u0622\u0623\u0006\u0080\uffff\uffff\u0000\u0623\u0624\u0005\u0083"+
		"\u0000\u0000\u0624\u0633\u0001\u0000\u0000\u0000\u0625\u062f\n\u0002\u0000"+
		"\u0000\u0626\u0630\u0003\u00e8t\u0000\u0627\u0629\u0005W\u0000\u0000\u0628"+
		"\u062a\u0003\\.\u0000\u0629\u0628\u0001\u0000\u0000\u0000\u0629\u062a"+
		"\u0001\u0000\u0000\u0000\u062a\u062b\u0001\u0000\u0000\u0000\u062b\u062d"+
		"\u0005X\u0000\u0000\u062c\u062e\u0003\u00ccf\u0000\u062d\u062c\u0001\u0000"+
		"\u0000\u0000\u062d\u062e\u0001\u0000\u0000\u0000\u062e\u0630\u0001\u0000"+
		"\u0000\u0000\u062f\u0626\u0001\u0000\u0000\u0000\u062f\u0627\u0001\u0000"+
		"\u0000\u0000\u0630\u0632\u0001\u0000\u0000\u0000\u0631\u0625\u0001\u0000"+
		"\u0000\u0000\u0632\u0635\u0001\u0000\u0000\u0000\u0633\u0631\u0001\u0000"+
		"\u0000\u0000\u0633\u0634\u0001\u0000\u0000\u0000\u0634\u0101\u0001\u0000"+
		"\u0000\u0000\u0635\u0633\u0001\u0000\u0000\u0000\u0636\u063b\u0003\u0104"+
		"\u0082\u0000\u0637\u0639\u0005z\u0000\u0000\u0638\u0637\u0001\u0000\u0000"+
		"\u0000\u0638\u0639\u0001\u0000\u0000\u0000\u0639\u063a\u0001\u0000\u0000"+
		"\u0000\u063a\u063c\u0005\u0083\u0000\u0000\u063b\u0638\u0001\u0000\u0000"+
		"\u0000\u063b\u063c\u0001\u0000\u0000\u0000\u063c\u0103\u0001\u0000\u0000"+
		"\u0000\u063d\u0642\u0003\u0106\u0083\u0000\u063e\u063f\u0005z\u0000\u0000"+
		"\u063f\u0641\u0003\u0106\u0083\u0000\u0640\u063e\u0001\u0000\u0000\u0000"+
		"\u0641\u0644\u0001\u0000\u0000\u0000\u0642\u0640\u0001\u0000\u0000\u0000"+
		"\u0642\u0643\u0001\u0000\u0000\u0000\u0643\u0105\u0001\u0000\u0000\u0000"+
		"\u0644\u0642\u0001\u0000\u0000\u0000\u0645\u0647\u0003\u00ccf\u0000\u0646"+
		"\u0645\u0001\u0000\u0000\u0000\u0646\u0647\u0001\u0000\u0000\u0000\u0647"+
		"\u0648\u0001\u0000\u0000\u0000\u0648\u064d\u0003\u008aE\u0000\u0649\u064e"+
		"\u0003\u00e2q\u0000\u064a\u064c\u0003\u00f8|\u0000\u064b\u064a\u0001\u0000"+
		"\u0000\u0000\u064b\u064c\u0001\u0000\u0000\u0000\u064c\u064e\u0001\u0000"+
		"\u0000\u0000\u064d\u0649\u0001\u0000\u0000\u0000\u064d\u064b\u0001\u0000"+
		"\u0000\u0000\u064e\u0651\u0001\u0000\u0000\u0000\u064f\u0650\u0005e\u0000"+
		"\u0000\u0650\u0652\u0003\u0110\u0088\u0000\u0651\u064f\u0001\u0000\u0000"+
		"\u0000\u0651\u0652\u0001\u0000\u0000\u0000\u0652\u0107\u0001\u0000\u0000"+
		"\u0000\u0653\u0655\u0003\u00ccf\u0000\u0654\u0653\u0001\u0000\u0000\u0000"+
		"\u0654\u0655\u0001\u0000\u0000\u0000\u0655\u0657\u0001\u0000\u0000\u0000"+
		"\u0656\u0658\u0003\u008aE\u0000\u0657\u0656\u0001\u0000\u0000\u0000\u0657"+
		"\u0658\u0001\u0000\u0000\u0000\u0658\u0659\u0001\u0000\u0000\u0000\u0659"+
		"\u065b\u0003\u00e2q\u0000\u065a\u065c\u0003\u012a\u0095\u0000\u065b\u065a"+
		"\u0001\u0000\u0000\u0000\u065b\u065c\u0001\u0000\u0000\u0000\u065c\u065d"+
		"\u0001\u0000\u0000\u0000\u065d\u065e\u0003\u010a\u0085\u0000\u065e\u0109"+
		"\u0001\u0000\u0000\u0000\u065f\u0661\u0003\u0142\u00a1\u0000\u0660\u065f"+
		"\u0001\u0000\u0000\u0000\u0660\u0661\u0001\u0000\u0000\u0000\u0661\u0662"+
		"\u0001\u0000\u0000\u0000\u0662\u0668\u0003d2\u0000\u0663\u0668\u0003\u0168"+
		"\u00b4\u0000\u0664\u0665\u0005e\u0000\u0000\u0665\u0666\u0007\u0013\u0000"+
		"\u0000\u0666\u0668\u0005\u0080\u0000\u0000\u0667\u0660\u0001\u0000\u0000"+
		"\u0000\u0667\u0663\u0001\u0000\u0000\u0000\u0667\u0664\u0001\u0000\u0000"+
		"\u0000\u0668\u010b\u0001\u0000\u0000\u0000\u0669\u066f\u0003\u010e\u0087"+
		"\u0000\u066a\u066b\u0005U\u0000\u0000\u066b\u066c\u0003\"\u0011\u0000"+
		"\u066c\u066d\u0005V\u0000\u0000\u066d\u066f\u0001\u0000\u0000\u0000\u066e"+
		"\u0669\u0001\u0000\u0000\u0000\u066e\u066a\u0001\u0000\u0000\u0000\u066f"+
		"\u010d\u0001\u0000\u0000\u0000\u0670\u0671\u0005e\u0000\u0000\u0671\u0674"+
		"\u0003\u0110\u0088\u0000\u0672\u0674\u0003\u0114\u008a\u0000\u0673\u0670"+
		"\u0001\u0000\u0000\u0000\u0673\u0672\u0001\u0000\u0000\u0000\u0674\u010f"+
		"\u0001\u0000\u0000\u0000\u0675\u0678\u0003V+\u0000\u0676\u0678\u0003\u0114"+
		"\u008a\u0000\u0677\u0675\u0001\u0000\u0000\u0000\u0677\u0676\u0001\u0000"+
		"\u0000\u0000\u0678\u0111\u0001\u0000\u0000\u0000\u0679\u067b\u0003\u0110"+
		"\u0088\u0000\u067a\u067c\u0005\u0083\u0000\u0000\u067b\u067a\u0001\u0000"+
		"\u0000\u0000\u067b\u067c\u0001\u0000\u0000\u0000\u067c\u0684\u0001\u0000"+
		"\u0000\u0000\u067d\u067e\u0005z\u0000\u0000\u067e\u0680\u0003\u0110\u0088"+
		"\u0000\u067f\u0681\u0005\u0083\u0000\u0000\u0680\u067f\u0001\u0000\u0000"+
		"\u0000\u0680\u0681\u0001\u0000\u0000\u0000\u0681\u0683\u0001\u0000\u0000"+
		"\u0000\u0682\u067d\u0001\u0000\u0000\u0000\u0683\u0686\u0001\u0000\u0000"+
		"\u0000\u0684\u0682\u0001\u0000\u0000\u0000\u0684\u0685\u0001\u0000\u0000"+
		"\u0000\u0685\u0113\u0001\u0000\u0000\u0000\u0686\u0684\u0001\u0000\u0000"+
		"\u0000\u0687\u068c\u0005Y\u0000\u0000\u0688\u068a\u0003\u0112\u0089\u0000"+
		"\u0689\u068b\u0005z\u0000\u0000\u068a\u0689\u0001\u0000\u0000\u0000\u068a"+
		"\u068b\u0001\u0000\u0000\u0000\u068b\u068d\u0001\u0000\u0000\u0000\u068c"+
		"\u0688\u0001\u0000\u0000\u0000\u068c\u068d\u0001\u0000\u0000\u0000\u068d"+
		"\u068e\u0001\u0000\u0000\u0000\u068e\u068f\u0005Z\u0000\u0000\u068f\u0115"+
		"\u0001\u0000\u0000\u0000\u0690\u0693\u0005\u0084\u0000\u0000\u0691\u0693"+
		"\u0003\u0156\u00ab\u0000\u0692\u0690\u0001\u0000\u0000\u0000\u0692\u0691"+
		"\u0001\u0000\u0000\u0000\u0693\u0117\u0001\u0000\u0000\u0000\u0694\u0695"+
		"\u0003\u011a\u008d\u0000\u0695\u0697\u0005Y\u0000\u0000\u0696\u0698\u0003"+
		"\u0122\u0091\u0000\u0697\u0696\u0001\u0000\u0000\u0000\u0697\u0698\u0001"+
		"\u0000\u0000\u0000\u0698\u0699\u0001\u0000\u0000\u0000\u0699\u069a\u0005"+
		"Z\u0000\u0000\u069a\u0119\u0001\u0000\u0000\u0000\u069b\u069d\u0003\u0120"+
		"\u0090\u0000\u069c\u069e\u0003\u00ccf\u0000\u069d\u069c\u0001\u0000\u0000"+
		"\u0000\u069d\u069e\u0001\u0000\u0000\u0000\u069e\u06a3\u0001\u0000\u0000"+
		"\u0000\u069f\u06a1\u0003\u011c\u008e\u0000\u06a0\u06a2\u0003\u011e\u008f"+
		"\u0000\u06a1\u06a0\u0001\u0000\u0000\u0000\u06a1\u06a2\u0001\u0000\u0000"+
		"\u0000\u06a2\u06a4\u0001\u0000\u0000\u0000\u06a3\u069f\u0001\u0000\u0000"+
		"\u0000\u06a3\u06a4\u0001\u0000\u0000\u0000\u06a4\u06a6\u0001\u0000\u0000"+
		"\u0000\u06a5\u06a7\u0003\u0130\u0098\u0000\u06a6\u06a5\u0001\u0000\u0000"+
		"\u0000\u06a6\u06a7\u0001\u0000\u0000\u0000\u06a7\u06b3\u0001\u0000\u0000"+
		"\u0000\u06a8\u06aa\u0005M\u0000\u0000\u06a9\u06ab\u0003\u00ccf\u0000\u06aa"+
		"\u06a9\u0001\u0000\u0000\u0000\u06aa\u06ab\u0001\u0000\u0000\u0000\u06ab"+
		"\u06b0\u0001\u0000\u0000\u0000\u06ac\u06ae\u0003\u011c\u008e\u0000\u06ad"+
		"\u06af\u0003\u011e\u008f\u0000\u06ae\u06ad\u0001\u0000\u0000\u0000\u06ae"+
		"\u06af\u0001\u0000\u0000\u0000\u06af\u06b1\u0001\u0000\u0000\u0000\u06b0"+
		"\u06ac\u0001\u0000\u0000\u0000\u06b0\u06b1\u0001\u0000\u0000\u0000\u06b1"+
		"\u06b3\u0001\u0000\u0000\u0000\u06b2\u069b\u0001\u0000\u0000\u0000\u06b2"+
		"\u06a8\u0001\u0000\u0000\u0000\u06b3\u011b\u0001\u0000\u0000\u0000\u06b4"+
		"\u06b6\u0003\n\u0005\u0000\u06b5\u06b4\u0001\u0000\u0000\u0000\u06b5\u06b6"+
		"\u0001\u0000\u0000\u0000\u06b6\u06b7\u0001\u0000\u0000\u0000\u06b7\u06b8"+
		"\u0003\u0116\u008b\u0000\u06b8\u011d\u0001\u0000\u0000\u0000\u06b9\u06ba"+
		"\u0005&\u0000\u0000\u06ba\u011f\u0001\u0000\u0000\u0000\u06bb\u06bc\u0007"+
		"\u000f\u0000\u0000\u06bc\u0121\u0001\u0000\u0000\u0000\u06bd\u06c2\u0003"+
		"\u0124\u0092\u0000\u06be\u06bf\u0003\u013a\u009d\u0000\u06bf\u06c0\u0005"+
		"~\u0000\u0000\u06c0\u06c2\u0001\u0000\u0000\u0000\u06c1\u06bd\u0001\u0000"+
		"\u0000\u0000\u06c1\u06be\u0001\u0000\u0000\u0000\u06c2\u06c3\u0001\u0000"+
		"\u0000\u0000\u06c3\u06c1\u0001\u0000\u0000\u0000\u06c3\u06c4\u0001\u0000"+
		"\u0000\u0000\u06c4\u0123\u0001\u0000\u0000\u0000\u06c5\u06c7\u0003\u00cc"+
		"f\u0000\u06c6\u06c5\u0001\u0000\u0000\u0000\u06c6\u06c7\u0001\u0000\u0000"+
		"\u0000\u06c7\u06c9\u0001\u0000\u0000\u0000\u06c8\u06ca\u0003\u008aE\u0000"+
		"\u06c9\u06c8\u0001\u0000\u0000\u0000\u06c9\u06ca\u0001\u0000\u0000\u0000"+
		"\u06ca\u06cc\u0001\u0000\u0000\u0000\u06cb\u06cd\u0003\u0126\u0093\u0000"+
		"\u06cc\u06cb\u0001\u0000\u0000\u0000\u06cc\u06cd\u0001\u0000\u0000\u0000"+
		"\u06cd\u06ce\u0001\u0000\u0000\u0000\u06ce\u06d6\u0005\u0080\u0000\u0000"+
		"\u06cf\u06d6\u0003\u0108\u0084\u0000\u06d0\u06d6\u0003\u00c4b\u0000\u06d1"+
		"\u06d6\u0003\u0082A\u0000\u06d2\u06d6\u0003\u014e\u00a7\u0000\u06d3\u06d6"+
		"\u0003~?\u0000\u06d4\u06d6\u0003\u0084B\u0000\u06d5\u06c6\u0001\u0000"+
		"\u0000\u0000\u06d5\u06cf\u0001\u0000\u0000\u0000\u06d5\u06d0\u0001\u0000"+
		"\u0000\u0000\u06d5\u06d1\u0001\u0000\u0000\u0000\u06d5\u06d2\u0001\u0000"+
		"\u0000\u0000\u06d5\u06d3\u0001\u0000\u0000\u0000\u06d5\u06d4\u0001\u0000"+
		"\u0000\u0000\u06d6\u0125\u0001\u0000\u0000\u0000\u06d7\u06dc\u0003\u0128"+
		"\u0094\u0000\u06d8\u06d9\u0005z\u0000\u0000\u06d9\u06db\u0003\u0128\u0094"+
		"\u0000\u06da\u06d8\u0001\u0000\u0000\u0000\u06db\u06de\u0001\u0000\u0000"+
		"\u0000\u06dc\u06da\u0001\u0000\u0000\u0000\u06dc\u06dd\u0001\u0000\u0000"+
		"\u0000\u06dd\u0127\u0001\u0000\u0000\u0000\u06de\u06dc\u0001\u0000\u0000"+
		"\u0000\u06df\u06e9\u0003\u00e2q\u0000\u06e0\u06e2\u0003\u012a\u0095\u0000"+
		"\u06e1\u06e0\u0001\u0000\u0000\u0000\u06e1\u06e2\u0001\u0000\u0000\u0000"+
		"\u06e2\u06e4\u0001\u0000\u0000\u0000\u06e3\u06e5\u0003\u012e\u0097\u0000"+
		"\u06e4\u06e3\u0001\u0000\u0000\u0000\u06e4\u06e5\u0001\u0000\u0000\u0000"+
		"\u06e5\u06ea\u0001\u0000\u0000\u0000\u06e6\u06e8\u0003\u010e\u0087\u0000"+
		"\u06e7\u06e6\u0001\u0000\u0000\u0000\u06e7\u06e8\u0001\u0000\u0000\u0000"+
		"\u06e8\u06ea\u0001\u0000\u0000\u0000\u06e9\u06e1\u0001\u0000\u0000\u0000"+
		"\u06e9\u06e7\u0001\u0000\u0000\u0000\u06ea\u06f4\u0001\u0000\u0000\u0000"+
		"\u06eb\u06ed\u0005\u0084\u0000\u0000\u06ec\u06eb\u0001\u0000\u0000\u0000"+
		"\u06ec\u06ed\u0001\u0000\u0000\u0000\u06ed\u06ef\u0001\u0000\u0000\u0000"+
		"\u06ee\u06f0\u0003\u00ccf\u0000\u06ef\u06ee\u0001\u0000\u0000\u0000\u06ef"+
		"\u06f0\u0001\u0000\u0000\u0000\u06f0\u06f1\u0001\u0000\u0000\u0000\u06f1"+
		"\u06f2\u0005~\u0000\u0000\u06f2\u06f4\u0003\\.\u0000\u06f3\u06df\u0001"+
		"\u0000\u0000\u0000\u06f3\u06ec\u0001\u0000\u0000\u0000\u06f4\u0129\u0001"+
		"\u0000\u0000\u0000\u06f5\u06f7\u0003\u012c\u0096\u0000\u06f6\u06f5\u0001"+
		"\u0000\u0000\u0000\u06f7\u06f8\u0001\u0000\u0000\u0000\u06f8\u06f6\u0001"+
		"\u0000\u0000\u0000\u06f8\u06f9\u0001\u0000\u0000\u0000\u06f9\u012b\u0001"+
		"\u0000\u0000\u0000\u06fa\u06fb\u0007\u0014\u0000\u0000\u06fb\u012d\u0001"+
		"\u0000\u0000\u0000\u06fc\u06fd\u0005e\u0000\u0000\u06fd\u06fe\u0005\u0086"+
		"\u0000\u0000\u06fe\u06ff\u0006\u0097\uffff\uffff\u0000\u06ff\u012f\u0001"+
		"\u0000\u0000\u0000\u0700\u0701\u0005~\u0000\u0000\u0701\u0702\u0003\u0132"+
		"\u0099\u0000\u0702\u0131\u0001\u0000\u0000\u0000\u0703\u0705\u0003\u0134"+
		"\u009a\u0000\u0704\u0706\u0005\u0083\u0000\u0000\u0705\u0704\u0001\u0000"+
		"\u0000\u0000\u0705\u0706\u0001\u0000\u0000\u0000\u0706\u070e\u0001\u0000"+
		"\u0000\u0000\u0707\u0708\u0005z\u0000\u0000\u0708\u070a\u0003\u0134\u009a"+
		"\u0000\u0709\u070b\u0005\u0083\u0000\u0000\u070a\u0709\u0001\u0000\u0000"+
		"\u0000\u070a\u070b\u0001\u0000\u0000\u0000\u070b\u070d\u0001\u0000\u0000"+
		"\u0000\u070c\u0707\u0001\u0000\u0000\u0000\u070d\u0710\u0001\u0000\u0000"+
		"\u0000\u070e\u070c\u0001\u0000\u0000\u0000\u070e\u070f\u0001\u0000\u0000"+
		"\u0000\u070f\u0133\u0001\u0000\u0000\u0000\u0710\u070e\u0001\u0000\u0000"+
		"\u0000\u0711\u0713\u0003\u00ccf\u0000\u0712\u0711\u0001\u0000\u0000\u0000"+
		"\u0712\u0713\u0001\u0000\u0000\u0000\u0713\u0720\u0001\u0000\u0000\u0000"+
		"\u0714\u0721\u0003\u0138\u009c\u0000\u0715\u0717\u0005P\u0000\u0000\u0716"+
		"\u0718\u0003\u013a\u009d\u0000\u0717\u0716\u0001\u0000\u0000\u0000\u0717"+
		"\u0718\u0001\u0000\u0000\u0000\u0718\u0719\u0001\u0000\u0000\u0000\u0719"+
		"\u0721\u0003\u0138\u009c\u0000\u071a\u071c\u0003\u013a\u009d\u0000\u071b"+
		"\u071d\u0005P\u0000\u0000\u071c\u071b\u0001\u0000\u0000\u0000\u071c\u071d"+
		"\u0001\u0000\u0000\u0000\u071d\u071e\u0001\u0000\u0000\u0000\u071e\u071f"+
		"\u0003\u0138\u009c\u0000\u071f\u0721\u0001\u0000\u0000\u0000\u0720\u0714"+
		"\u0001\u0000\u0000\u0000\u0720\u0715\u0001\u0000\u0000\u0000\u0720\u071a"+
		"\u0001\u0000\u0000\u0000\u0721\u0135\u0001\u0000\u0000\u0000\u0722\u0724"+
		"\u0003\n\u0005\u0000\u0723\u0722\u0001\u0000\u0000\u0000\u0723\u0724\u0001"+
		"\u0000\u0000\u0000\u0724\u0725\u0001\u0000\u0000\u0000\u0725\u0728\u0003"+
		"\u0116\u008b\u0000\u0726\u0728\u0003\u00a2Q\u0000\u0727\u0723\u0001\u0000"+
		"\u0000\u0000\u0727\u0726\u0001\u0000\u0000\u0000\u0728\u0137\u0001\u0000"+
		"\u0000\u0000\u0729\u072a\u0003\u0136\u009b\u0000\u072a\u0139\u0001\u0000"+
		"\u0000\u0000\u072b\u072c\u0007\u0015\u0000\u0000\u072c\u013b\u0001\u0000"+
		"\u0000\u0000\u072d\u072e\u00054\u0000\u0000\u072e\u072f\u0003\u013e\u009f"+
		"\u0000\u072f\u013d\u0001\u0000\u0000\u0000\u0730\u0732\u0003\u0096K\u0000"+
		"\u0731\u0733\u0003\u0140\u00a0\u0000\u0732\u0731\u0001\u0000\u0000\u0000"+
		"\u0732\u0733\u0001\u0000\u0000\u0000\u0733\u013f\u0001\u0000\u0000\u0000"+
		"\u0734\u0736\u0003\u00ecv\u0000\u0735\u0737\u0003\u0140\u00a0\u0000\u0736"+
		"\u0735\u0001\u0000\u0000\u0000\u0736\u0737\u0001\u0000\u0000\u0000\u0737"+
		"\u0141\u0001\u0000\u0000\u0000\u0738\u0739\u0005~\u0000\u0000\u0739\u073a"+
		"\u0003\u0144\u00a2\u0000\u073a\u0143\u0001\u0000\u0000\u0000\u073b\u073d"+
		"\u0003\u0146\u00a3\u0000\u073c\u073e\u0005\u0083\u0000\u0000\u073d\u073c"+
		"\u0001\u0000\u0000\u0000\u073d\u073e\u0001\u0000\u0000\u0000\u073e\u0746"+
		"\u0001\u0000\u0000\u0000\u073f\u0740\u0005z\u0000\u0000\u0740\u0742\u0003"+
		"\u0146\u00a3\u0000\u0741\u0743\u0005\u0083\u0000\u0000\u0742\u0741\u0001"+
		"\u0000\u0000\u0000\u0742\u0743\u0001\u0000\u0000\u0000\u0743\u0745\u0001"+
		"\u0000\u0000\u0000\u0744\u073f\u0001\u0000\u0000\u0000\u0745\u0748\u0001"+
		"\u0000\u0000\u0000\u0746\u0744\u0001\u0000\u0000\u0000\u0746\u0747\u0001"+
		"\u0000\u0000\u0000\u0747\u0145\u0001\u0000\u0000\u0000\u0748\u0746\u0001"+
		"\u0000\u0000\u0000\u0749\u0750\u0003\u0148\u00a4\u0000\u074a\u074c\u0005"+
		"U\u0000\u0000\u074b\u074d\u0003\"\u0011\u0000\u074c\u074b\u0001\u0000"+
		"\u0000\u0000\u074c\u074d\u0001\u0000\u0000\u0000\u074d\u074e\u0001\u0000"+
		"\u0000\u0000\u074e\u0751\u0005V\u0000\u0000\u074f\u0751\u0003\u0114\u008a"+
		"\u0000\u0750\u074a\u0001\u0000\u0000\u0000\u0750\u074f\u0001\u0000\u0000"+
		"\u0000\u0751\u0147\u0001\u0000\u0000\u0000\u0752\u0755\u0003\u0136\u009b"+
		"\u0000\u0753\u0755\u0005\u0084\u0000\u0000\u0754\u0752\u0001\u0000\u0000"+
		"\u0000\u0754\u0753\u0001\u0000\u0000\u0000\u0755\u0149\u0001\u0000\u0000"+
		"\u0000\u0756\u0757\u00054\u0000\u0000\u0757\u0758\u0003\u017a\u00bd\u0000"+
		"\u0758\u014b\u0001\u0000\u0000\u0000\u0759\u075d\u00054\u0000\u0000\u075a"+
		"\u075b\u0005\u0004\u0000\u0000\u075b\u075e\u0005\u0084\u0000\u0000\u075c"+
		"\u075e\u0005\u008c\u0000\u0000\u075d\u075a\u0001\u0000\u0000\u0000\u075d"+
		"\u075c\u0001\u0000\u0000\u0000\u075e\u014d\u0001\u0000\u0000\u0000\u075f"+
		"\u0760\u0005D\u0000\u0000\u0760\u0761\u0005f\u0000\u0000\u0761\u0762\u0003"+
		"\u0150\u00a8\u0000\u0762\u0763\u0005g\u0000\u0000\u0763\u0764\u0003z="+
		"\u0000\u0764\u014f\u0001\u0000\u0000\u0000\u0765\u076a\u0003\u0152\u00a9"+
		"\u0000\u0766\u0767\u0005z\u0000\u0000\u0767\u0769\u0003\u0152\u00a9\u0000"+
		"\u0768\u0766\u0001\u0000\u0000\u0000\u0769\u076c\u0001\u0000\u0000\u0000"+
		"\u076a\u0768\u0001\u0000\u0000\u0000\u076a\u076b\u0001\u0000\u0000\u0000"+
		"\u076b\u0151\u0001\u0000\u0000\u0000\u076c\u076a\u0001\u0000\u0000\u0000"+
		"\u076d\u0770\u0003\u0154\u00aa\u0000\u076e\u0770\u0003\u0106\u0083\u0000"+
		"\u076f\u076d\u0001\u0000\u0000\u0000\u076f\u076e\u0001\u0000\u0000\u0000"+
		"\u0770\u0153\u0001\u0000\u0000\u0000\u0771\u0772\u0005D\u0000\u0000\u0772"+
		"\u0773\u0005f\u0000\u0000\u0773\u0774\u0003\u0150\u00a8\u0000\u0774\u0775"+
		"\u0005g\u0000\u0000\u0775\u0777\u0001\u0000\u0000\u0000\u0776\u0771\u0001"+
		"\u0000\u0000\u0000\u0776\u0777\u0001\u0000\u0000\u0000\u0777\u0778\u0001"+
		"\u0000\u0000\u0000\u0778\u077b\u0005\u0015\u0000\u0000\u0779\u077b\u0005"+
		"L\u0000\u0000\u077a\u0776\u0001\u0000\u0000\u0000\u077a\u0779\u0001\u0000"+
		"\u0000\u0000\u077b\u0787\u0001\u0000\u0000\u0000\u077c\u077e\u0005\u0083"+
		"\u0000\u0000\u077d\u077c\u0001\u0000\u0000\u0000\u077d\u077e\u0001\u0000"+
		"\u0000\u0000\u077e\u0780\u0001\u0000\u0000\u0000\u077f\u0781\u0005\u0084"+
		"\u0000\u0000\u0780\u077f\u0001\u0000\u0000\u0000\u0780\u0781\u0001\u0000"+
		"\u0000\u0000\u0781\u0788\u0001\u0000\u0000\u0000\u0782\u0784\u0005\u0084"+
		"\u0000\u0000\u0783\u0782\u0001\u0000\u0000\u0000\u0783\u0784\u0001\u0000"+
		"\u0000\u0000\u0784\u0785\u0001\u0000\u0000\u0000\u0785\u0786\u0005e\u0000"+
		"\u0000\u0786\u0788\u0003\u00f6{\u0000\u0787\u077d\u0001\u0000\u0000\u0000"+
		"\u0787\u0783\u0001\u0000\u0000\u0000\u0788\u0155\u0001\u0000\u0000\u0000"+
		"\u0789\u078a\u0003\u015a\u00ad\u0000\u078a\u078c\u0005f\u0000\u0000\u078b"+
		"\u078d\u0003\u015c\u00ae\u0000\u078c\u078b\u0001\u0000\u0000\u0000\u078c"+
		"\u078d\u0001\u0000\u0000\u0000\u078d\u078e\u0001\u0000\u0000\u0000\u078e"+
		"\u078f\u0005g\u0000\u0000\u078f\u0157\u0001\u0000\u0000\u0000\u0790\u079c"+
		"\u0003\u0156\u00ab\u0000\u0791\u0794\u0003\u014a\u00a5\u0000\u0792\u0794"+
		"\u0003\u014c\u00a6\u0000\u0793\u0791\u0001\u0000\u0000\u0000\u0793\u0792"+
		"\u0001\u0000\u0000\u0000\u0794\u0795\u0001\u0000\u0000\u0000\u0795\u0797"+
		"\u0005f\u0000\u0000\u0796\u0798\u0003\u015c\u00ae\u0000\u0797\u0796\u0001"+
		"\u0000\u0000\u0000\u0797\u0798\u0001\u0000\u0000\u0000\u0798\u0799\u0001"+
		"\u0000\u0000\u0000\u0799\u079a\u0005g\u0000\u0000\u079a\u079c\u0001\u0000"+
		"\u0000\u0000\u079b\u0790\u0001\u0000\u0000\u0000\u079b\u0793\u0001\u0000"+
		"\u0000\u0000\u079c\u0159\u0001\u0000\u0000\u0000\u079d\u079e\u0005\u0084"+
		"\u0000\u0000\u079e\u015b\u0001\u0000\u0000\u0000\u079f\u07a1\u0003\u015e"+
		"\u00af\u0000\u07a0\u07a2\u0005\u0083\u0000\u0000\u07a1\u07a0\u0001\u0000"+
		"\u0000\u0000\u07a1\u07a2\u0001\u0000\u0000\u0000\u07a2\u07aa\u0001\u0000"+
		"\u0000\u0000\u07a3\u07a4\u0005z\u0000\u0000\u07a4\u07a6\u0003\u015e\u00af"+
		"\u0000\u07a5\u07a7\u0005\u0083\u0000\u0000\u07a6\u07a5\u0001\u0000\u0000"+
		"\u0000\u07a6\u07a7\u0001\u0000\u0000\u0000\u07a7\u07a9\u0001\u0000\u0000"+
		"\u0000\u07a8\u07a3\u0001\u0000\u0000\u0000\u07a9\u07ac\u0001\u0000\u0000"+
		"\u0000\u07aa\u07a8\u0001\u0000\u0000\u0000\u07aa\u07ab\u0001\u0000\u0000"+
		"\u0000\u07ab\u015d\u0001\u0000\u0000\u0000\u07ac\u07aa\u0001\u0000\u0000"+
		"\u0000\u07ad\u07b1\u0003\u00f6{\u0000\u07ae\u07b1\u0003\\.\u0000\u07af"+
		"\u07b1\u0003\u0004\u0002\u0000\u07b0\u07ad\u0001\u0000\u0000\u0000\u07b0"+
		"\u07ae\u0001\u0000\u0000\u0000\u07b0\u07af\u0001\u0000\u0000\u0000\u07b1"+
		"\u015f\u0001\u0000\u0000\u0000\u07b2\u07b3\u0005L\u0000\u0000\u07b3\u07b9"+
		"\u0003\n\u0005\u0000\u07b4\u07ba\u0005\u0084\u0000\u0000\u07b5\u07b7\u0005"+
		"D\u0000\u0000\u07b6\u07b5\u0001\u0000\u0000\u0000\u07b6\u07b7\u0001\u0000"+
		"\u0000\u0000\u07b7\u07b8\u0001\u0000\u0000\u0000\u07b8\u07ba\u0003\u0156"+
		"\u00ab\u0000\u07b9\u07b4\u0001\u0000\u0000\u0000\u07b9\u07b6\u0001\u0000"+
		"\u0000\u0000\u07ba\u0161\u0001\u0000\u0000\u0000\u07bb\u07bd\u0005$\u0000"+
		"\u0000\u07bc\u07bb\u0001\u0000\u0000\u0000\u07bc\u07bd\u0001\u0000\u0000"+
		"\u0000\u07bd\u07be\u0001\u0000\u0000\u0000\u07be\u07bf\u0005D\u0000\u0000"+
		"\u07bf\u07c0\u0003z=\u0000\u07c0\u0163\u0001\u0000\u0000\u0000\u07c1\u07c2"+
		"\u0005D\u0000\u0000\u07c2\u07c3\u0005f\u0000\u0000\u07c3\u07c4\u0005g"+
		"\u0000\u0000\u07c4\u07c5\u0003z=\u0000\u07c5\u0165\u0001\u0000\u0000\u0000"+
		"\u07c6\u07c7\u0005I\u0000\u0000\u07c7\u07c8\u0003d2\u0000\u07c8\u07c9"+
		"\u0003\u016a\u00b5\u0000\u07c9\u0167\u0001\u0000\u0000\u0000\u07ca\u07cc"+
		"\u0005I\u0000\u0000\u07cb\u07cd\u0003\u0142\u00a1\u0000\u07cc\u07cb\u0001"+
		"\u0000\u0000\u0000\u07cc\u07cd\u0001\u0000\u0000\u0000\u07cd\u07ce\u0001"+
		"\u0000\u0000\u0000\u07ce\u07cf\u0003d2\u0000\u07cf\u07d0\u0003\u016a\u00b5"+
		"\u0000\u07d0\u0169\u0001\u0000\u0000\u0000\u07d1\u07d3\u0003\u016c\u00b6"+
		"\u0000\u07d2\u07d1\u0001\u0000\u0000\u0000\u07d3\u07d4\u0001\u0000\u0000"+
		"\u0000\u07d4\u07d2\u0001\u0000\u0000\u0000\u07d4\u07d5\u0001\u0000\u0000"+
		"\u0000\u07d5\u016b\u0001\u0000\u0000\u0000\u07d6\u07d7\u0005\u0011\u0000"+
		"\u0000\u07d7\u07d8\u0005U\u0000\u0000\u07d8\u07d9\u0003\u016e\u00b7\u0000"+
		"\u07d9\u07da\u0005V\u0000\u0000\u07da\u07db\u0003d2\u0000\u07db\u016d"+
		"\u0001\u0000\u0000\u0000\u07dc\u07de\u0003\u00ccf\u0000\u07dd\u07dc\u0001"+
		"\u0000\u0000\u0000\u07dd\u07de\u0001\u0000\u0000\u0000\u07de\u07df\u0001"+
		"\u0000\u0000\u0000\u07df\u07e2\u0003\u0096K\u0000\u07e0\u07e3\u0003\u00e2"+
		"q\u0000\u07e1\u07e3\u0003\u00f8|\u0000\u07e2\u07e0\u0001\u0000\u0000\u0000"+
		"\u07e2\u07e1\u0001\u0000\u0000\u0000\u07e2\u07e3\u0001\u0000\u0000\u0000"+
		"\u07e3\u07e6\u0001\u0000\u0000\u0000\u07e4\u07e6\u0005\u0083\u0000\u0000"+
		"\u07e5\u07dd\u0001\u0000\u0000\u0000\u07e5\u07e4\u0001\u0000\u0000\u0000"+
		"\u07e6\u016f\u0001\u0000\u0000\u0000\u07e7\u07e9\u0005G\u0000\u0000\u07e8"+
		"\u07ea\u0003V+\u0000\u07e9\u07e8\u0001\u0000\u0000\u0000\u07e9\u07ea\u0001"+
		"\u0000\u0000\u0000\u07ea\u0171\u0001\u0000\u0000\u0000\u07eb\u07ee\u0003"+
		"\u0174\u00ba\u0000\u07ec\u07ee\u0003\u0178\u00bc\u0000\u07ed\u07eb\u0001"+
		"\u0000\u0000\u0000\u07ed\u07ec\u0001\u0000\u0000\u0000\u07ee\u0173\u0001"+
		"\u0000\u0000\u0000\u07ef\u07f0\u0005G\u0000\u0000\u07f0\u07f2\u0005U\u0000"+
		"\u0000\u07f1\u07f3\u0003\u0176\u00bb\u0000\u07f2\u07f1\u0001\u0000\u0000"+
		"\u0000\u07f2\u07f3\u0001\u0000\u0000\u0000\u07f3\u07f4\u0001\u0000\u0000"+
		"\u0000\u07f4\u07f5\u0005V\u0000\u0000\u07f5\u0175\u0001\u0000\u0000\u0000"+
		"\u07f6\u07f8\u0003\u00f6{\u0000\u07f7\u07f9\u0005\u0083\u0000\u0000\u07f8"+
		"\u07f7\u0001\u0000\u0000\u0000\u07f8\u07f9\u0001\u0000\u0000\u0000\u07f9"+
		"\u0801\u0001\u0000\u0000\u0000\u07fa\u07fb\u0005z\u0000\u0000\u07fb\u07fd"+
		"\u0003\u00f6{\u0000\u07fc\u07fe\u0005\u0083\u0000\u0000\u07fd\u07fc\u0001"+
		"\u0000\u0000\u0000\u07fd\u07fe\u0001\u0000\u0000\u0000\u07fe\u0800\u0001"+
		"\u0000\u0000\u0000\u07ff\u07fa\u0001\u0000\u0000\u0000\u0800\u0803\u0001"+
		"\u0000\u0000\u0000\u0801\u07ff\u0001\u0000\u0000\u0000\u0801\u0802\u0001"+
		"\u0000\u0000\u0000\u0802\u0177\u0001\u0000\u0000\u0000\u0803\u0801\u0001"+
		"\u0000\u0000\u0000\u0804\u0805\u00052\u0000\u0000\u0805\u0806\u0005U\u0000"+
		"\u0000\u0806\u0807\u0003\\.\u0000\u0807\u0808\u0005V\u0000\u0000\u0808"+
		"\u080b\u0001\u0000\u0000\u0000\u0809\u080b\u00052\u0000\u0000\u080a\u0804"+
		"\u0001\u0000\u0000\u0000\u080a\u0809\u0001\u0000\u0000\u0000\u080b\u0179"+
		"\u0001\u0000\u0000\u0000\u080c\u080f\u00051\u0000\u0000\u080d\u080e\u0005"+
		"W\u0000\u0000\u080e\u0810\u0005X\u0000\u0000\u080f\u080d\u0001\u0000\u0000"+
		"\u0000\u080f\u0810\u0001\u0000\u0000\u0000\u0810\u0840\u0001\u0000\u0000"+
		"\u0000\u0811\u0814\u0005\u001c\u0000\u0000\u0812\u0813\u0005W\u0000\u0000"+
		"\u0813\u0815\u0005X\u0000\u0000\u0814\u0812\u0001\u0000\u0000\u0000\u0814"+
		"\u0815\u0001\u0000\u0000\u0000\u0815\u0840\u0001\u0000\u0000\u0000\u0816"+
		"\u0840\u0005[\u0000\u0000\u0817\u0840\u0005\\\u0000\u0000\u0818\u0840"+
		"\u0005]\u0000\u0000\u0819\u0840\u0005^\u0000\u0000\u081a\u0840\u0005_"+
		"\u0000\u0000\u081b\u0840\u0005`\u0000\u0000\u081c\u0840\u0005a\u0000\u0000"+
		"\u081d\u0840\u0005b\u0000\u0000\u081e\u0840\u0005c\u0000\u0000\u081f\u0840"+
		"\u0005d\u0000\u0000\u0820\u0840\u0005e\u0000\u0000\u0821\u0840\u0005g"+
		"\u0000\u0000\u0822\u0840\u0005f\u0000\u0000\u0823\u0840\u0005u\u0000\u0000"+
		"\u0824\u0840\u0005h\u0000\u0000\u0825\u0840\u0005i\u0000\u0000\u0826\u0840"+
		"\u0005j\u0000\u0000\u0827\u0840\u0005l\u0000\u0000\u0828\u0840\u0005m"+
		"\u0000\u0000\u0829\u0840\u0005n\u0000\u0000\u082a\u0840\u0005o\u0000\u0000"+
		"\u082b\u082c\u0005f\u0000\u0000\u082c\u0840\u0005f\u0000\u0000\u082d\u082e"+
		"\u0005g\u0000\u0000\u082e\u0840\u0005g\u0000\u0000\u082f\u0840\u0005q"+
		"\u0000\u0000\u0830\u0840\u0005p\u0000\u0000\u0831\u0840\u0005r\u0000\u0000"+
		"\u0832\u0840\u0005s\u0000\u0000\u0833\u0840\u0005t\u0000\u0000\u0834\u0840"+
		"\u0005v\u0000\u0000\u0835\u0840\u0005w\u0000\u0000\u0836\u0840\u0005x"+
		"\u0000\u0000\u0837\u0840\u0005y\u0000\u0000\u0838\u0840\u0005z\u0000\u0000"+
		"\u0839\u0840\u0005{\u0000\u0000\u083a\u0840\u0005|\u0000\u0000\u083b\u083c"+
		"\u0005U\u0000\u0000\u083c\u0840\u0005V\u0000\u0000\u083d\u083e\u0005W"+
		"\u0000\u0000\u083e\u0840\u0005X\u0000\u0000\u083f\u080c\u0001\u0000\u0000"+
		"\u0000\u083f\u0811\u0001\u0000\u0000\u0000\u083f\u0816\u0001\u0000\u0000"+
		"\u0000\u083f\u0817\u0001\u0000\u0000\u0000\u083f\u0818\u0001\u0000\u0000"+
		"\u0000\u083f\u0819\u0001\u0000\u0000\u0000\u083f\u081a\u0001\u0000\u0000"+
		"\u0000\u083f\u081b\u0001\u0000\u0000\u0000\u083f\u081c\u0001\u0000\u0000"+
		"\u0000\u083f\u081d\u0001\u0000\u0000\u0000\u083f\u081e\u0001\u0000\u0000"+
		"\u0000\u083f\u081f\u0001\u0000\u0000\u0000\u083f\u0820\u0001\u0000\u0000"+
		"\u0000\u083f\u0821\u0001\u0000\u0000\u0000\u083f\u0822\u0001\u0000\u0000"+
		"\u0000\u083f\u0823\u0001\u0000\u0000\u0000\u083f\u0824\u0001\u0000\u0000"+
		"\u0000\u083f\u0825\u0001\u0000\u0000\u0000\u083f\u0826\u0001\u0000\u0000"+
		"\u0000\u083f\u0827\u0001\u0000\u0000\u0000\u083f\u0828\u0001\u0000\u0000"+
		"\u0000\u083f\u0829\u0001\u0000\u0000\u0000\u083f\u082a\u0001\u0000\u0000"+
		"\u0000\u083f\u082b\u0001\u0000\u0000\u0000\u083f\u082d\u0001\u0000\u0000"+
		"\u0000\u083f\u082f\u0001\u0000\u0000\u0000\u083f\u0830\u0001\u0000\u0000"+
		"\u0000\u083f\u0831\u0001\u0000\u0000\u0000\u083f\u0832\u0001\u0000\u0000"+
		"\u0000\u083f\u0833\u0001\u0000\u0000\u0000\u083f\u0834\u0001\u0000\u0000"+
		"\u0000\u083f\u0835\u0001\u0000\u0000\u0000\u083f\u0836\u0001\u0000\u0000"+
		"\u0000\u083f\u0837\u0001\u0000\u0000\u0000\u083f\u0838\u0001\u0000\u0000"+
		"\u0000\u083f\u0839\u0001\u0000\u0000\u0000\u083f\u083a\u0001\u0000\u0000"+
		"\u0000\u083f\u083b\u0001\u0000\u0000\u0000\u083f\u083d\u0001\u0000\u0000"+
		"\u0000\u0840\u017b\u0001\u0000\u0000\u0000\u0841\u0842\u0007\u0016\u0000"+
		"\u0000\u0842\u017d\u0001\u0000\u0000\u0000\u0843\u084a\u0003\u0000\u0000"+
		"\u0000\u0844\u0846\u0003\u0182\u00c1\u0000\u0845\u0844\u0001\u0000\u0000"+
		"\u0000\u0846\u0847\u0001\u0000\u0000\u0000\u0847\u0848\u0001\u0000\u0000"+
		"\u0000\u0847\u0845\u0001\u0000\u0000\u0000\u0848\u084a\u0001\u0000\u0000"+
		"\u0000\u0849\u0843\u0001\u0000\u0000\u0000\u0849\u0845\u0001\u0000\u0000"+
		"\u0000\u084a\u017f\u0001\u0000\u0000\u0000\u084b\u084e\u0003z=\u0000\u084c"+
		"\u084e\u0003\u0182\u00c1\u0000\u084d\u084b\u0001\u0000\u0000\u0000\u084d"+
		"\u084c\u0001\u0000\u0000\u0000\u084e\u084f\u0001\u0000\u0000\u0000\u084f"+
		"\u0850\u0001\u0000\u0000\u0000\u084f\u084d\u0001\u0000\u0000\u0000\u0850"+
		"\u0853\u0001\u0000\u0000\u0000\u0851\u0853\u0005\u0000\u0000\u0001\u0852"+
		"\u084d\u0001\u0000\u0000\u0000\u0852\u0851\u0001\u0000\u0000\u0000\u0853"+
		"\u0181\u0001\u0000\u0000\u0000\u0854\u0856\t\u0000\u0000\u0000\u0855\u0854"+
		"\u0001\u0000\u0000\u0000\u0856\u0857\u0001\u0000\u0000\u0000\u0857\u0855"+
		"\u0001\u0000\u0000\u0000\u0857\u0858\u0001\u0000\u0000\u0000\u0858\u0183"+
		"\u0001\u0000\u0000\u0000\u0138\u0185\u018c\u0195\u0199\u01a2\u01a5\u01a9"+
		"\u01b1\u01b8\u01bb\u01c0\u01c5\u01cb\u01d3\u01d5\u01de\u01e2\u01e6\u01e9"+
		"\u01ed\u01f0\u01f7\u01fb\u01fe\u0201\u0204\u020a\u020e\u0212\u0220\u0224"+
		"\u022a\u0231\u0237\u023b\u023f\u0241\u0249\u024e\u025b\u0262\u026e\u0278"+
		"\u027d\u0281\u0288\u028b\u0293\u0297\u029a\u02a1\u02a8\u02ac\u02b1\u02b5"+
		"\u02b8\u02bd\u02cc\u02d3\u02db\u02e3\u02ec\u02f3\u02fa\u0302\u030a\u0312"+
		"\u031a\u0322\u032a\u0333\u033b\u0344\u034c\u0354\u0356\u0359\u035f\u0365"+
		"\u036b\u0372\u037b\u0383\u0387\u038e\u0390\u03a4\u03a8\u03ae\u03b3\u03b7"+
		"\u03ba\u03c1\u03c8\u03cc\u03d5\u03e0\u03ea\u03ef\u03f6\u03f9\u03fe\u0403"+
		"\u0418\u041d\u0420\u042b\u0431\u0436\u0439\u043e\u0441\u0448\u0451\u0456"+
		"\u0459\u045d\u0461\u0465\u046a\u046f\u0475\u047b\u0481\u0487\u048d\u0490"+
		"\u0496\u049a\u049e\u04a1\u04a9\u04ab\u04b1\u04b4\u04b7\u04ba\u04be\u04c2"+
		"\u04c8\u04d2\u04d8\u04de\u04e3\u04e8\u04ec\u04f9\u04ff\u0503\u0509\u050e"+
		"\u051d\u0521\u0526\u052b\u0530\u0536\u0539\u0542\u0546\u054b\u054f\u0555"+
		"\u055c\u056d\u056f\u0576\u057b\u0582\u0586\u058a\u0592\u0598\u059e\u05a2"+
		"\u05a4\u05a8\u05ad\u05b1\u05b4\u05b7\u05ba\u05bf\u05c3\u05c6\u05ca\u05cd"+
		"\u05cf\u05d4\u05db\u05e1\u05e5\u05eb\u05f1\u05f4\u05f6\u05fc\u0600\u0606"+
		"\u060d\u0611\u0613\u0617\u061d\u0629\u062d\u062f\u0633\u0638\u063b\u0642"+
		"\u0646\u064b\u064d\u0651\u0654\u0657\u065b\u0660\u0667\u066e\u0673\u0677"+
		"\u067b\u0680\u0684\u068a\u068c\u0692\u0697\u069d\u06a1\u06a3\u06a6\u06aa"+
		"\u06ae\u06b0\u06b2\u06b5\u06c1\u06c3\u06c6\u06c9\u06cc\u06d5\u06dc\u06e1"+
		"\u06e4\u06e7\u06e9\u06ec\u06ef\u06f3\u06f8\u0705\u070a\u070e\u0712\u0717"+
		"\u071c\u0720\u0723\u0727\u0732\u0736\u073d\u0742\u0746\u074c\u0750\u0754"+
		"\u075d\u076a\u076f\u0776\u077a\u077d\u0780\u0783\u0787\u078c\u0793\u0797"+
		"\u079b\u07a1\u07a6\u07aa\u07b0\u07b6\u07b9\u07bc\u07cc\u07d4\u07dd\u07e2"+
		"\u07e5\u07e9\u07ed\u07f2\u07f8\u07fd\u0801\u080a\u080f\u0814\u083f\u0847"+
		"\u0849\u084d\u084f\u0852\u0857";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}