// Generated from parser/src/main/resources/grammar/java/JavaStructureParser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.java.gen.st;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JavaStructureParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LBRACE=1, RBRACE=2, SEMI=3, PACKAGE=4, IMPORT=5, STATIC=6, CLASS=7, INTERFACE=8, 
		ENUM=9, RECORD=10, WS=11, COMMENT=12, LINE_COMMENT=13, NEW_LINE=14, STRING_LITERAL=15, 
		CHAR_LITERAL=16, AT_INTERFACE=17, ANNOTATION=18, COR_PAREN_SEQ=19, DEFAULT=20, 
		SYNCHRONIZED=21, FINAL=22, SEALED=23, NON_SEALED=24, MODIFIER=25, IDENTIFIER=26, 
		ANY=27;
	public static final int
		RULE_compilationUnit = 0, RULE_packageDeclaration = 1, RULE_importDeclaration = 2, 
		RULE_typeDeclaration = 3, RULE_modifier = 4, RULE_classDeclaration = 5, 
		RULE_interfaceDeclaration = 6, RULE_enumDeclaration = 7, RULE_recordDeclaration = 8, 
		RULE_annotationTypeDeclaration = 9, RULE_classBody = 10, RULE_classBodyDeclaration = 11, 
		RULE_memberDeclaration = 12, RULE_methodDeclaration = 13, RULE_recordBody = 14, 
		RULE_compactConstructorDeclaration = 15, RULE_fieldDeclaration = 16, RULE_methodBody = 17, 
		RULE_anyBlock = 18, RULE_block = 19, RULE_anyToken = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnit", "packageDeclaration", "importDeclaration", "typeDeclaration", 
			"modifier", "classDeclaration", "interfaceDeclaration", "enumDeclaration", 
			"recordDeclaration", "annotationTypeDeclaration", "classBody", "classBodyDeclaration", 
			"memberDeclaration", "methodDeclaration", "recordBody", "compactConstructorDeclaration", 
			"fieldDeclaration", "methodBody", "anyBlock", "block", "anyToken"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "';'", null, null, "'static'", null, null, "'enum'", 
			null, null, null, null, null, null, null, null, null, null, "'default'", 
			"'synchronized'", "'final'", "'sealed'", "'non-sealed'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LBRACE", "RBRACE", "SEMI", "PACKAGE", "IMPORT", "STATIC", "CLASS", 
			"INTERFACE", "ENUM", "RECORD", "WS", "COMMENT", "LINE_COMMENT", "NEW_LINE", 
			"STRING_LITERAL", "CHAR_LITERAL", "AT_INTERFACE", "ANNOTATION", "COR_PAREN_SEQ", 
			"DEFAULT", "SYNCHRONIZED", "FINAL", "SEALED", "NON_SEALED", "MODIFIER", 
			"IDENTIFIER", "ANY"
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
	public String getGrammarFileName() { return "JavaStructureParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JavaStructureParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JavaStructureParser.EOF, 0); }
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
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterCompilationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitCompilationUnit(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PACKAGE) {
				{
				setState(42);
				packageDeclaration();
				}
			}

			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(45);
				importDeclaration();
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 66455496L) != 0)) {
				{
				{
				setState(51);
				typeDeclaration();
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
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
	public static class PackageDeclarationContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(JavaStructureParser.PACKAGE, 0); }
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterPackageDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitPackageDeclaration(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_packageDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(PACKAGE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode IMPORT() { return getToken(JavaStructureParser.IMPORT, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitImportDeclaration(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(IMPORT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(JavaStructureParser.SEMI, 0); }
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitTypeDeclaration(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeDeclaration);
		int _la;
		try {
			setState(77);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STATIC:
			case CLASS:
			case INTERFACE:
			case ENUM:
			case RECORD:
			case AT_INTERFACE:
			case ANNOTATION:
			case DEFAULT:
			case SYNCHRONIZED:
			case FINAL:
			case SEALED:
			case NON_SEALED:
			case MODIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 66322496L) != 0)) {
					{
					{
					setState(63);
					modifier();
					}
					}
					setState(68);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(74);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(69);
					classDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(70);
					interfaceDeclaration();
					}
					break;
				case ENUM:
					{
					setState(71);
					enumDeclaration();
					}
					break;
				case RECORD:
					{
					setState(72);
					recordDeclaration();
					}
					break;
				case AT_INTERFACE:
					{
					setState(73);
					annotationTypeDeclaration();
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
				setState(76);
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
		public TerminalNode ANNOTATION() { return getToken(JavaStructureParser.ANNOTATION, 0); }
		public TerminalNode MODIFIER() { return getToken(JavaStructureParser.MODIFIER, 0); }
		public TerminalNode STATIC() { return getToken(JavaStructureParser.STATIC, 0); }
		public TerminalNode FINAL() { return getToken(JavaStructureParser.FINAL, 0); }
		public TerminalNode SYNCHRONIZED() { return getToken(JavaStructureParser.SYNCHRONIZED, 0); }
		public TerminalNode DEFAULT() { return getToken(JavaStructureParser.DEFAULT, 0); }
		public TerminalNode SEALED() { return getToken(JavaStructureParser.SEALED, 0); }
		public TerminalNode NON_SEALED() { return getToken(JavaStructureParser.NON_SEALED, 0); }
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitModifier(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 66322496L) != 0)) ) {
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
	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(JavaStructureParser.CLASS, 0); }
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitClassDeclaration(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_classDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(CLASS);
			setState(82);
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
	public static class InterfaceDeclarationContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(JavaStructureParser.INTERFACE, 0); }
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterInterfaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitInterfaceDeclaration(this);
		}
	}

	public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
		InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_interfaceDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(INTERFACE);
			setState(85);
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
	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(JavaStructureParser.ENUM, 0); }
		public AnyBlockContext anyBlock() {
			return getRuleContext(AnyBlockContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitEnumDeclaration(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(ENUM);
			setState(88);
			anyBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode RECORD() { return getToken(JavaStructureParser.RECORD, 0); }
		public TerminalNode COR_PAREN_SEQ() { return getToken(JavaStructureParser.COR_PAREN_SEQ, 0); }
		public RecordBodyContext recordBody() {
			return getRuleContext(RecordBodyContext.class,0);
		}
		public RecordDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterRecordDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitRecordDeclaration(this);
		}
	}

	public final RecordDeclarationContext recordDeclaration() throws RecognitionException {
		RecordDeclarationContext _localctx = new RecordDeclarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_recordDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(RECORD);
			setState(91);
			match(COR_PAREN_SEQ);
			setState(92);
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
	public static class AnnotationTypeDeclarationContext extends ParserRuleContext {
		public TerminalNode AT_INTERFACE() { return getToken(JavaStructureParser.AT_INTERFACE, 0); }
		public AnyBlockContext anyBlock() {
			return getRuleContext(AnyBlockContext.class,0);
		}
		public AnnotationTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterAnnotationTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitAnnotationTypeDeclaration(this);
		}
	}

	public final AnnotationTypeDeclarationContext annotationTypeDeclaration() throws RecognitionException {
		AnnotationTypeDeclarationContext _localctx = new AnnotationTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_annotationTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(AT_INTERFACE);
			setState(95);
			anyBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode LBRACE() { return getToken(JavaStructureParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaStructureParser.RBRACE, 0); }
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
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterClassBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitClassBody(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_classBody);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(LBRACE);
			setState(101);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(98);
					classBodyDeclaration();
					}
					} 
				}
				setState(103);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(104);
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
		public TerminalNode SEMI() { return getToken(JavaStructureParser.SEMI, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(JavaStructureParser.STATIC, 0); }
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
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterClassBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitClassBodyDeclaration(this);
		}
	}

	public final ClassBodyDeclarationContext classBodyDeclaration() throws RecognitionException {
		ClassBodyDeclarationContext _localctx = new ClassBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_classBodyDeclaration);
		int _la;
		try {
			int _alt;
			setState(118);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				match(SEMI);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STATIC) {
					{
					setState(107);
					match(STATIC);
					}
				}

				setState(110);
				block();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(111);
						modifier();
						}
						} 
					}
					setState(116);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				}
				setState(117);
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
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
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
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public FieldDeclarationContext fieldDeclaration() {
			return getRuleContext(FieldDeclarationContext.class,0);
		}
		public MemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitMemberDeclaration(this);
		}
	}

	public final MemberDeclarationContext memberDeclaration() throws RecognitionException {
		MemberDeclarationContext _localctx = new MemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_memberDeclaration);
		try {
			setState(127);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(120);
				methodDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(121);
				classDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(122);
				interfaceDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(123);
				annotationTypeDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(124);
				recordDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(125);
				enumDeclaration();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(126);
				fieldDeclaration();
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
		public TerminalNode COR_PAREN_SEQ() { return getToken(JavaStructureParser.COR_PAREN_SEQ, 0); }
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitMethodDeclaration(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_methodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(COR_PAREN_SEQ);
			setState(130);
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
	public static class RecordBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaStructureParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaStructureParser.RBRACE, 0); }
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
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterRecordBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitRecordBody(this);
		}
	}

	public final RecordBodyContext recordBody() throws RecognitionException {
		RecordBodyContext _localctx = new RecordBodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_recordBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(LBRACE);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 66979834L) != 0)) {
				{
				setState(135);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(133);
					classBodyDeclaration();
					}
					break;

				case 2:
					{
					setState(134);
					compactConstructorDeclaration();
					}
					break;
				}
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(140);
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
	public static class CompactConstructorDeclarationContext extends ParserRuleContext {
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
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterCompactConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitCompactConstructorDeclaration(this);
		}
	}

	public final CompactConstructorDeclarationContext compactConstructorDeclaration() throws RecognitionException {
		CompactConstructorDeclarationContext _localctx = new CompactConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_compactConstructorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 66322496L) != 0)) {
				{
				{
				setState(142);
				modifier();
				}
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(148);
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
	public static class FieldDeclarationContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(JavaStructureParser.SEMI, 0); }
		public List<AnyBlockContext> anyBlock() {
			return getRuleContexts(AnyBlockContext.class);
		}
		public AnyBlockContext anyBlock(int i) {
			return getRuleContext(AnyBlockContext.class,i);
		}
		public List<AnyTokenContext> anyToken() {
			return getRuleContexts(AnyTokenContext.class);
		}
		public AnyTokenContext anyToken(int i) {
			return getRuleContext(AnyTokenContext.class,i);
		}
		public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterFieldDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitFieldDeclaration(this);
		}
	}

	public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
		FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_fieldDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(152);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LBRACE:
						{
						setState(150);
						anyBlock();
						}
						break;
					case SEMI:
					case PACKAGE:
					case IMPORT:
					case STATIC:
					case CLASS:
					case INTERFACE:
					case ENUM:
					case RECORD:
					case ANNOTATION:
					case COR_PAREN_SEQ:
					case DEFAULT:
					case SYNCHRONIZED:
					case FINAL:
					case SEALED:
					case NON_SEALED:
						{
						setState(151);
						anyToken();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(157);
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
	public static class MethodBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(JavaStructureParser.SEMI, 0); }
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterMethodBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitMethodBody(this);
		}
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_methodBody);
		try {
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(159);
				block();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(160);
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
	public static class AnyBlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JavaStructureParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaStructureParser.RBRACE, 0); }
		public List<AnyBlockContext> anyBlock() {
			return getRuleContexts(AnyBlockContext.class);
		}
		public AnyBlockContext anyBlock(int i) {
			return getRuleContext(AnyBlockContext.class,i);
		}
		public List<AnyTokenContext> anyToken() {
			return getRuleContexts(AnyTokenContext.class);
		}
		public AnyTokenContext anyToken(int i) {
			return getRuleContext(AnyTokenContext.class,i);
		}
		public List<TerminalNode> MODIFIER() { return getTokens(JavaStructureParser.MODIFIER); }
		public TerminalNode MODIFIER(int i) {
			return getToken(JavaStructureParser.MODIFIER, i);
		}
		public AnyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterAnyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitAnyBlock(this);
		}
	}

	public final AnyBlockContext anyBlock() throws RecognitionException {
		AnyBlockContext _localctx = new AnyBlockContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_anyBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(LBRACE);
			setState(169);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(167);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LBRACE:
						{
						setState(164);
						anyBlock();
						}
						break;
					case SEMI:
					case PACKAGE:
					case IMPORT:
					case STATIC:
					case CLASS:
					case INTERFACE:
					case ENUM:
					case RECORD:
					case ANNOTATION:
					case COR_PAREN_SEQ:
					case DEFAULT:
					case SYNCHRONIZED:
					case FINAL:
					case SEALED:
					case NON_SEALED:
						{
						setState(165);
						anyToken();
						}
						break;
					case MODIFIER:
						{
						setState(166);
						match(MODIFIER);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(171);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			setState(172);
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
		public TerminalNode LBRACE() { return getToken(JavaStructureParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JavaStructureParser.RBRACE, 0); }
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public List<AnyTokenContext> anyToken() {
			return getRuleContexts(AnyTokenContext.class);
		}
		public AnyTokenContext anyToken(int i) {
			return getRuleContext(AnyTokenContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_block);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(LBRACE);
			setState(179);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(177);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LBRACE:
						{
						setState(175);
						block();
						}
						break;
					case SEMI:
					case PACKAGE:
					case IMPORT:
					case STATIC:
					case CLASS:
					case INTERFACE:
					case ENUM:
					case RECORD:
					case ANNOTATION:
					case COR_PAREN_SEQ:
					case DEFAULT:
					case SYNCHRONIZED:
					case FINAL:
					case SEALED:
					case NON_SEALED:
						{
						setState(176);
						anyToken();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(181);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			setState(182);
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
	public static class AnyTokenContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(JavaStructureParser.SEMI, 0); }
		public TerminalNode PACKAGE() { return getToken(JavaStructureParser.PACKAGE, 0); }
		public TerminalNode IMPORT() { return getToken(JavaStructureParser.IMPORT, 0); }
		public TerminalNode STATIC() { return getToken(JavaStructureParser.STATIC, 0); }
		public TerminalNode CLASS() { return getToken(JavaStructureParser.CLASS, 0); }
		public TerminalNode INTERFACE() { return getToken(JavaStructureParser.INTERFACE, 0); }
		public TerminalNode ENUM() { return getToken(JavaStructureParser.ENUM, 0); }
		public TerminalNode RECORD() { return getToken(JavaStructureParser.RECORD, 0); }
		public TerminalNode COR_PAREN_SEQ() { return getToken(JavaStructureParser.COR_PAREN_SEQ, 0); }
		public TerminalNode ANNOTATION() { return getToken(JavaStructureParser.ANNOTATION, 0); }
		public TerminalNode FINAL() { return getToken(JavaStructureParser.FINAL, 0); }
		public TerminalNode SYNCHRONIZED() { return getToken(JavaStructureParser.SYNCHRONIZED, 0); }
		public TerminalNode DEFAULT() { return getToken(JavaStructureParser.DEFAULT, 0); }
		public TerminalNode SEALED() { return getToken(JavaStructureParser.SEALED, 0); }
		public TerminalNode NON_SEALED() { return getToken(JavaStructureParser.NON_SEALED, 0); }
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public AnyTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).enterAnyToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaStructureParserListener ) ((JavaStructureParserListener)listener).exitAnyToken(this);
		}
	}

	public final AnyTokenContext anyToken() throws RecognitionException {
		AnyTokenContext _localctx = new AnyTokenContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_anyToken);
		try {
			setState(200);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(184);
				match(SEMI);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(185);
				match(PACKAGE);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(186);
				match(IMPORT);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(187);
				match(STATIC);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(188);
				match(CLASS);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(189);
				match(INTERFACE);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(190);
				match(ENUM);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(191);
				match(RECORD);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(192);
				match(COR_PAREN_SEQ);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(193);
				match(ANNOTATION);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(194);
				match(FINAL);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(195);
				match(SYNCHRONIZED);
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(196);
				match(DEFAULT);
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(197);
				match(SEALED);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(198);
				match(NON_SEALED);
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(199);
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

	public static final String _serializedATN =
		"\u0004\u0001\u001b\u00cb\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0001\u0000\u0003"+
		"\u0000,\b\u0000\u0001\u0000\u0005\u0000/\b\u0000\n\u0000\f\u00002\t\u0000"+
		"\u0001\u0000\u0005\u00005\b\u0000\n\u0000\f\u00008\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0005\u0003A\b\u0003\n\u0003\f\u0003D\t\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003K\b\u0003\u0001\u0003"+
		"\u0003\u0003N\b\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0005\nd\b\n\n\n\f\ng\t\n\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0003\u000bm\b\u000b\u0001\u000b\u0001\u000b\u0005\u000bq\b\u000b"+
		"\n\u000b\f\u000bt\t\u000b\u0001\u000b\u0003\u000bw\b\u000b\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0080\b\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u0088\b\u000e"+
		"\n\u000e\f\u000e\u008b\t\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0005"+
		"\u000f\u0090\b\u000f\n\u000f\f\u000f\u0093\t\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0005\u0010\u0099\b\u0010\n\u0010\f\u0010\u009c"+
		"\t\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0003\u0011\u00a2"+
		"\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00a8"+
		"\b\u0012\n\u0012\f\u0012\u00ab\t\u0012\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0005\u0013\u00b2\b\u0013\n\u0013\f\u0013\u00b5"+
		"\t\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u00c9\b\u0014\u0001\u0014\u0004e\u009a\u00a9\u00b3"+
		"\u0000\u0015\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016"+
		"\u0018\u001a\u001c\u001e \"$&(\u0000\u0001\u0003\u0000\u0006\u0006\u0012"+
		"\u0012\u0014\u0019\u00e3\u0000+\u0001\u0000\u0000\u0000\u0002;\u0001\u0000"+
		"\u0000\u0000\u0004=\u0001\u0000\u0000\u0000\u0006M\u0001\u0000\u0000\u0000"+
		"\bO\u0001\u0000\u0000\u0000\nQ\u0001\u0000\u0000\u0000\fT\u0001\u0000"+
		"\u0000\u0000\u000eW\u0001\u0000\u0000\u0000\u0010Z\u0001\u0000\u0000\u0000"+
		"\u0012^\u0001\u0000\u0000\u0000\u0014a\u0001\u0000\u0000\u0000\u0016v"+
		"\u0001\u0000\u0000\u0000\u0018\u007f\u0001\u0000\u0000\u0000\u001a\u0081"+
		"\u0001\u0000\u0000\u0000\u001c\u0084\u0001\u0000\u0000\u0000\u001e\u0091"+
		"\u0001\u0000\u0000\u0000 \u009a\u0001\u0000\u0000\u0000\"\u00a1\u0001"+
		"\u0000\u0000\u0000$\u00a3\u0001\u0000\u0000\u0000&\u00ae\u0001\u0000\u0000"+
		"\u0000(\u00c8\u0001\u0000\u0000\u0000*,\u0003\u0002\u0001\u0000+*\u0001"+
		"\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000,0\u0001\u0000\u0000\u0000"+
		"-/\u0003\u0004\u0002\u0000.-\u0001\u0000\u0000\u0000/2\u0001\u0000\u0000"+
		"\u00000.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u000016\u0001\u0000"+
		"\u0000\u000020\u0001\u0000\u0000\u000035\u0003\u0006\u0003\u000043\u0001"+
		"\u0000\u0000\u000058\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u0000"+
		"67\u0001\u0000\u0000\u000079\u0001\u0000\u0000\u000086\u0001\u0000\u0000"+
		"\u00009:\u0005\u0000\u0000\u0001:\u0001\u0001\u0000\u0000\u0000;<\u0005"+
		"\u0004\u0000\u0000<\u0003\u0001\u0000\u0000\u0000=>\u0005\u0005\u0000"+
		"\u0000>\u0005\u0001\u0000\u0000\u0000?A\u0003\b\u0004\u0000@?\u0001\u0000"+
		"\u0000\u0000AD\u0001\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BC\u0001"+
		"\u0000\u0000\u0000CJ\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000"+
		"EK\u0003\n\u0005\u0000FK\u0003\f\u0006\u0000GK\u0003\u000e\u0007\u0000"+
		"HK\u0003\u0010\b\u0000IK\u0003\u0012\t\u0000JE\u0001\u0000\u0000\u0000"+
		"JF\u0001\u0000\u0000\u0000JG\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000"+
		"\u0000JI\u0001\u0000\u0000\u0000KN\u0001\u0000\u0000\u0000LN\u0005\u0003"+
		"\u0000\u0000MB\u0001\u0000\u0000\u0000ML\u0001\u0000\u0000\u0000N\u0007"+
		"\u0001\u0000\u0000\u0000OP\u0007\u0000\u0000\u0000P\t\u0001\u0000\u0000"+
		"\u0000QR\u0005\u0007\u0000\u0000RS\u0003\u0014\n\u0000S\u000b\u0001\u0000"+
		"\u0000\u0000TU\u0005\b\u0000\u0000UV\u0003\u0014\n\u0000V\r\u0001\u0000"+
		"\u0000\u0000WX\u0005\t\u0000\u0000XY\u0003$\u0012\u0000Y\u000f\u0001\u0000"+
		"\u0000\u0000Z[\u0005\n\u0000\u0000[\\\u0005\u0013\u0000\u0000\\]\u0003"+
		"\u001c\u000e\u0000]\u0011\u0001\u0000\u0000\u0000^_\u0005\u0011\u0000"+
		"\u0000_`\u0003$\u0012\u0000`\u0013\u0001\u0000\u0000\u0000ae\u0005\u0001"+
		"\u0000\u0000bd\u0003\u0016\u000b\u0000cb\u0001\u0000\u0000\u0000dg\u0001"+
		"\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000"+
		"fh\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000hi\u0005\u0002\u0000"+
		"\u0000i\u0015\u0001\u0000\u0000\u0000jw\u0005\u0003\u0000\u0000km\u0005"+
		"\u0006\u0000\u0000lk\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000"+
		"mn\u0001\u0000\u0000\u0000nw\u0003&\u0013\u0000oq\u0003\b\u0004\u0000"+
		"po\u0001\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000rp\u0001\u0000\u0000"+
		"\u0000rs\u0001\u0000\u0000\u0000su\u0001\u0000\u0000\u0000tr\u0001\u0000"+
		"\u0000\u0000uw\u0003\u0018\f\u0000vj\u0001\u0000\u0000\u0000vl\u0001\u0000"+
		"\u0000\u0000vr\u0001\u0000\u0000\u0000w\u0017\u0001\u0000\u0000\u0000"+
		"x\u0080\u0003\u001a\r\u0000y\u0080\u0003\n\u0005\u0000z\u0080\u0003\f"+
		"\u0006\u0000{\u0080\u0003\u0012\t\u0000|\u0080\u0003\u0010\b\u0000}\u0080"+
		"\u0003\u000e\u0007\u0000~\u0080\u0003 \u0010\u0000\u007fx\u0001\u0000"+
		"\u0000\u0000\u007fy\u0001\u0000\u0000\u0000\u007fz\u0001\u0000\u0000\u0000"+
		"\u007f{\u0001\u0000\u0000\u0000\u007f|\u0001\u0000\u0000\u0000\u007f}"+
		"\u0001\u0000\u0000\u0000\u007f~\u0001\u0000\u0000\u0000\u0080\u0019\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0005\u0013\u0000\u0000\u0082\u0083\u0003"+
		"\"\u0011\u0000\u0083\u001b\u0001\u0000\u0000\u0000\u0084\u0089\u0005\u0001"+
		"\u0000\u0000\u0085\u0088\u0003\u0016\u000b\u0000\u0086\u0088\u0003\u001e"+
		"\u000f\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087\u0086\u0001\u0000"+
		"\u0000\u0000\u0088\u008b\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u008c\u0001\u0000"+
		"\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008c\u008d\u0005\u0002"+
		"\u0000\u0000\u008d\u001d\u0001\u0000\u0000\u0000\u008e\u0090\u0003\b\u0004"+
		"\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u0090\u0093\u0001\u0000\u0000"+
		"\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000"+
		"\u0000\u0092\u0094\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000"+
		"\u0000\u0094\u0095\u0003&\u0013\u0000\u0095\u001f\u0001\u0000\u0000\u0000"+
		"\u0096\u0099\u0003$\u0012\u0000\u0097\u0099\u0003(\u0014\u0000\u0098\u0096"+
		"\u0001\u0000\u0000\u0000\u0098\u0097\u0001\u0000\u0000\u0000\u0099\u009c"+
		"\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009a\u0098"+
		"\u0001\u0000\u0000\u0000\u009b\u009d\u0001\u0000\u0000\u0000\u009c\u009a"+
		"\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u0003\u0000\u0000\u009e!\u0001"+
		"\u0000\u0000\u0000\u009f\u00a2\u0003&\u0013\u0000\u00a0\u00a2\u0005\u0003"+
		"\u0000\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a1\u00a0\u0001\u0000"+
		"\u0000\u0000\u00a2#\u0001\u0000\u0000\u0000\u00a3\u00a9\u0005\u0001\u0000"+
		"\u0000\u00a4\u00a8\u0003$\u0012\u0000\u00a5\u00a8\u0003(\u0014\u0000\u00a6"+
		"\u00a8\u0005\u0019\u0000\u0000\u00a7\u00a4\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a6\u0001\u0000\u0000\u0000\u00a8"+
		"\u00ab\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000\u00a9"+
		"\u00a7\u0001\u0000\u0000\u0000\u00aa\u00ac\u0001\u0000\u0000\u0000\u00ab"+
		"\u00a9\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005\u0002\u0000\u0000\u00ad"+
		"%\u0001\u0000\u0000\u0000\u00ae\u00b3\u0005\u0001\u0000\u0000\u00af\u00b2"+
		"\u0003&\u0013\u0000\u00b0\u00b2\u0003(\u0014\u0000\u00b1\u00af\u0001\u0000"+
		"\u0000\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b5\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000"+
		"\u0000\u0000\u00b4\u00b6\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000"+
		"\u0000\u0000\u00b6\u00b7\u0005\u0002\u0000\u0000\u00b7\'\u0001\u0000\u0000"+
		"\u0000\u00b8\u00c9\u0005\u0003\u0000\u0000\u00b9\u00c9\u0005\u0004\u0000"+
		"\u0000\u00ba\u00c9\u0005\u0005\u0000\u0000\u00bb\u00c9\u0005\u0006\u0000"+
		"\u0000\u00bc\u00c9\u0005\u0007\u0000\u0000\u00bd\u00c9\u0005\b\u0000\u0000"+
		"\u00be\u00c9\u0005\t\u0000\u0000\u00bf\u00c9\u0005\n\u0000\u0000\u00c0"+
		"\u00c9\u0005\u0013\u0000\u0000\u00c1\u00c9\u0005\u0012\u0000\u0000\u00c2"+
		"\u00c9\u0005\u0016\u0000\u0000\u00c3\u00c9\u0005\u0015\u0000\u0000\u00c4"+
		"\u00c9\u0005\u0014\u0000\u0000\u00c5\u00c9\u0005\u0017\u0000\u0000\u00c6"+
		"\u00c9\u0005\u0018\u0000\u0000\u00c7\u00c9\u0003\u0010\b\u0000\u00c8\u00b8"+
		"\u0001\u0000\u0000\u0000\u00c8\u00b9\u0001\u0000\u0000\u0000\u00c8\u00ba"+
		"\u0001\u0000\u0000\u0000\u00c8\u00bb\u0001\u0000\u0000\u0000\u00c8\u00bc"+
		"\u0001\u0000\u0000\u0000\u00c8\u00bd\u0001\u0000\u0000\u0000\u00c8\u00be"+
		"\u0001\u0000\u0000\u0000\u00c8\u00bf\u0001\u0000\u0000\u0000\u00c8\u00c0"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c1\u0001\u0000\u0000\u0000\u00c8\u00c2"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c3\u0001\u0000\u0000\u0000\u00c8\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c5\u0001\u0000\u0000\u0000\u00c8\u00c6"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c9)\u0001"+
		"\u0000\u0000\u0000\u0016+06BJMelrv\u007f\u0087\u0089\u0091\u0098\u009a"+
		"\u00a1\u00a7\u00a9\u00b1\u00b3\u00c8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}