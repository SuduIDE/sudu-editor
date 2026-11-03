// Generated from parser-generator/src/main/resources/grammar/cpp/help/CPP14Directive.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.cpp.gen.help;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CPP14DirectiveParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, RAW_STRING=2, HASH=3, INCLUDE=4, ERROR_DIRECTIVE=5, WHITESPACE=6, 
		BLOCK_COMMENT=7, LINE_COMMENT=8, NEW_LINE_SLASH=9, NEW_LINE=10, LEFT=11, 
		RIGHT=12, INTEGER_LITERAL=13, DECIMAL_LITERAL=14, OCTAL_LITERAL=15, HEXADECIMAL_LITERAL=16, 
		BINARY_LITERAL=17, KEYWORD=18, OPERATORS=19, SLASH=20, DOT=21, IDENTIFIER=22, 
		DIR_CHAR=23, ERROR=24;
	public static final int
		RULE_directive = 0, RULE_include = 1, RULE_error = 2, RULE_dir = 3, RULE_other = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"directive", "include", "error", "dir", "other"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'#'", "'include'", "'error'", null, null, null, "'\\'", 
			null, "'<'", "'>'", null, null, null, null, null, null, null, "'/'", 
			"'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "STRING", "RAW_STRING", "HASH", "INCLUDE", "ERROR_DIRECTIVE", "WHITESPACE", 
			"BLOCK_COMMENT", "LINE_COMMENT", "NEW_LINE_SLASH", "NEW_LINE", "LEFT", 
			"RIGHT", "INTEGER_LITERAL", "DECIMAL_LITERAL", "OCTAL_LITERAL", "HEXADECIMAL_LITERAL", 
			"BINARY_LITERAL", "KEYWORD", "OPERATORS", "SLASH", "DOT", "IDENTIFIER", 
			"DIR_CHAR", "ERROR"
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
	public String getGrammarFileName() { return "CPP14Directive.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CPP14DirectiveParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectiveContext extends ParserRuleContext {
		public IncludeContext include() {
			return getRuleContext(IncludeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CPP14DirectiveParser.EOF, 0); }
		public ErrorContext error() {
			return getRuleContext(ErrorContext.class,0);
		}
		public DirContext dir() {
			return getRuleContext(DirContext.class,0);
		}
		public DirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).enterDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).exitDirective(this);
		}
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_directive);
		try {
			setState(19);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(10);
				include();
				setState(11);
				match(EOF);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(13);
				error();
				setState(14);
				match(EOF);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(16);
				dir();
				setState(17);
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
	public static class IncludeContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(CPP14DirectiveParser.HASH, 0); }
		public TerminalNode INCLUDE() { return getToken(CPP14DirectiveParser.INCLUDE, 0); }
		public TerminalNode STRING() { return getToken(CPP14DirectiveParser.STRING, 0); }
		public TerminalNode LEFT() { return getToken(CPP14DirectiveParser.LEFT, 0); }
		public TerminalNode RIGHT() { return getToken(CPP14DirectiveParser.RIGHT, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(CPP14DirectiveParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(CPP14DirectiveParser.IDENTIFIER, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(CPP14DirectiveParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(CPP14DirectiveParser.SLASH, i);
		}
		public List<TerminalNode> DOT() { return getTokens(CPP14DirectiveParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(CPP14DirectiveParser.DOT, i);
		}
		public IncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).enterInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).exitInclude(this);
		}
	}

	public final IncludeContext include() throws RecognitionException {
		IncludeContext _localctx = new IncludeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_include);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			match(HASH);
			setState(22);
			match(INCLUDE);
			setState(31);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				{
				setState(23);
				match(STRING);
				}
				break;
			case LEFT:
				{
				{
				setState(24);
				match(LEFT);
				setState(26); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(25);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(28); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0) );
				setState(30);
				match(RIGHT);
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
	public static class ErrorContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(CPP14DirectiveParser.HASH, 0); }
		public TerminalNode ERROR_DIRECTIVE() { return getToken(CPP14DirectiveParser.ERROR_DIRECTIVE, 0); }
		public List<OtherContext> other() {
			return getRuleContexts(OtherContext.class);
		}
		public OtherContext other(int i) {
			return getRuleContext(OtherContext.class,i);
		}
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).exitError(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_error);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			match(HASH);
			setState(34);
			match(ERROR_DIRECTIVE);
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16775230L) != 0)) {
				{
				{
				setState(35);
				other();
				}
				}
				setState(40);
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
	public static class DirContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(CPP14DirectiveParser.HASH, 0); }
		public TerminalNode IDENTIFIER() { return getToken(CPP14DirectiveParser.IDENTIFIER, 0); }
		public TerminalNode KEYWORD() { return getToken(CPP14DirectiveParser.KEYWORD, 0); }
		public List<OtherContext> other() {
			return getRuleContexts(OtherContext.class);
		}
		public OtherContext other(int i) {
			return getRuleContext(OtherContext.class,i);
		}
		public DirContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dir; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).enterDir(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).exitDir(this);
		}
	}

	public final DirContext dir() throws RecognitionException {
		DirContext _localctx = new DirContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_dir);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(HASH);
			setState(42);
			_la = _input.LA(1);
			if ( !(_la==KEYWORD || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16775230L) != 0)) {
				{
				{
				setState(43);
				other();
				}
				}
				setState(48);
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
	public static class OtherContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(CPP14DirectiveParser.STRING, 0); }
		public TerminalNode RAW_STRING() { return getToken(CPP14DirectiveParser.RAW_STRING, 0); }
		public TerminalNode LEFT() { return getToken(CPP14DirectiveParser.LEFT, 0); }
		public TerminalNode RIGHT() { return getToken(CPP14DirectiveParser.RIGHT, 0); }
		public TerminalNode KEYWORD() { return getToken(CPP14DirectiveParser.KEYWORD, 0); }
		public TerminalNode OPERATORS() { return getToken(CPP14DirectiveParser.OPERATORS, 0); }
		public TerminalNode DOT() { return getToken(CPP14DirectiveParser.DOT, 0); }
		public TerminalNode SLASH() { return getToken(CPP14DirectiveParser.SLASH, 0); }
		public TerminalNode IDENTIFIER() { return getToken(CPP14DirectiveParser.IDENTIFIER, 0); }
		public TerminalNode INTEGER_LITERAL() { return getToken(CPP14DirectiveParser.INTEGER_LITERAL, 0); }
		public TerminalNode DECIMAL_LITERAL() { return getToken(CPP14DirectiveParser.DECIMAL_LITERAL, 0); }
		public TerminalNode OCTAL_LITERAL() { return getToken(CPP14DirectiveParser.OCTAL_LITERAL, 0); }
		public TerminalNode HEXADECIMAL_LITERAL() { return getToken(CPP14DirectiveParser.HEXADECIMAL_LITERAL, 0); }
		public TerminalNode BINARY_LITERAL() { return getToken(CPP14DirectiveParser.BINARY_LITERAL, 0); }
		public TerminalNode DIR_CHAR() { return getToken(CPP14DirectiveParser.DIR_CHAR, 0); }
		public TerminalNode INCLUDE() { return getToken(CPP14DirectiveParser.INCLUDE, 0); }
		public TerminalNode ERROR_DIRECTIVE() { return getToken(CPP14DirectiveParser.ERROR_DIRECTIVE, 0); }
		public TerminalNode HASH() { return getToken(CPP14DirectiveParser.HASH, 0); }
		public OtherContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_other; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).enterOther(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14DirectiveListener ) ((CPP14DirectiveListener)listener).exitOther(this);
		}
	}

	public final OtherContext other() throws RecognitionException {
		OtherContext _localctx = new OtherContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_other);
		int _la;
		try {
			setState(64);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
			case RAW_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(49);
				_la = _input.LA(1);
				if ( !(_la==STRING || _la==RAW_STRING) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case LEFT:
				enterOuterAlt(_localctx, 2);
				{
				setState(50);
				match(LEFT);
				}
				break;
			case RIGHT:
				enterOuterAlt(_localctx, 3);
				{
				setState(51);
				match(RIGHT);
				}
				break;
			case KEYWORD:
				enterOuterAlt(_localctx, 4);
				{
				setState(52);
				match(KEYWORD);
				}
				break;
			case OPERATORS:
			case SLASH:
			case DOT:
				enterOuterAlt(_localctx, 5);
				{
				setState(53);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3670016L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 6);
				{
				setState(54);
				match(IDENTIFIER);
				}
				break;
			case INTEGER_LITERAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(55);
				match(INTEGER_LITERAL);
				}
				break;
			case DECIMAL_LITERAL:
				enterOuterAlt(_localctx, 8);
				{
				setState(56);
				match(DECIMAL_LITERAL);
				}
				break;
			case OCTAL_LITERAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(57);
				match(OCTAL_LITERAL);
				}
				break;
			case HEXADECIMAL_LITERAL:
				enterOuterAlt(_localctx, 10);
				{
				setState(58);
				match(HEXADECIMAL_LITERAL);
				}
				break;
			case BINARY_LITERAL:
				enterOuterAlt(_localctx, 11);
				{
				setState(59);
				match(BINARY_LITERAL);
				}
				break;
			case DIR_CHAR:
				enterOuterAlt(_localctx, 12);
				{
				setState(60);
				match(DIR_CHAR);
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 13);
				{
				setState(61);
				match(INCLUDE);
				}
				break;
			case ERROR_DIRECTIVE:
				enterOuterAlt(_localctx, 14);
				{
				setState(62);
				match(ERROR_DIRECTIVE);
				}
				break;
			case HASH:
				enterOuterAlt(_localctx, 15);
				{
				setState(63);
				match(HASH);
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

	public static final String _serializedATN =
		"\u0004\u0001\u0018C\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0014\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0004\u0001\u001b\b\u0001\u000b"+
		"\u0001\f\u0001\u001c\u0001\u0001\u0003\u0001 \b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002%\b\u0002\n\u0002\f\u0002(\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003-\b\u0003\n\u0003\f\u00030\t"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001"+
		"\u0004\u0000\u0000\u0005\u0000\u0002\u0004\u0006\b\u0000\u0004\u0001\u0000"+
		"\u0014\u0016\u0002\u0000\u0012\u0012\u0016\u0016\u0001\u0000\u0001\u0002"+
		"\u0001\u0000\u0013\u0015Q\u0000\u0013\u0001\u0000\u0000\u0000\u0002\u0015"+
		"\u0001\u0000\u0000\u0000\u0004!\u0001\u0000\u0000\u0000\u0006)\u0001\u0000"+
		"\u0000\u0000\b@\u0001\u0000\u0000\u0000\n\u000b\u0003\u0002\u0001\u0000"+
		"\u000b\f\u0005\u0000\u0000\u0001\f\u0014\u0001\u0000\u0000\u0000\r\u000e"+
		"\u0003\u0004\u0002\u0000\u000e\u000f\u0005\u0000\u0000\u0001\u000f\u0014"+
		"\u0001\u0000\u0000\u0000\u0010\u0011\u0003\u0006\u0003\u0000\u0011\u0012"+
		"\u0005\u0000\u0000\u0001\u0012\u0014\u0001\u0000\u0000\u0000\u0013\n\u0001"+
		"\u0000\u0000\u0000\u0013\r\u0001\u0000\u0000\u0000\u0013\u0010\u0001\u0000"+
		"\u0000\u0000\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u0016\u0005\u0003"+
		"\u0000\u0000\u0016\u001f\u0005\u0004\u0000\u0000\u0017 \u0005\u0001\u0000"+
		"\u0000\u0018\u001a\u0005\u000b\u0000\u0000\u0019\u001b\u0007\u0000\u0000"+
		"\u0000\u001a\u0019\u0001\u0000\u0000\u0000\u001b\u001c\u0001\u0000\u0000"+
		"\u0000\u001c\u001a\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000\u0000"+
		"\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e \u0005\f\u0000\u0000"+
		"\u001f\u0017\u0001\u0000\u0000\u0000\u001f\u0018\u0001\u0000\u0000\u0000"+
		" \u0003\u0001\u0000\u0000\u0000!\"\u0005\u0003\u0000\u0000\"&\u0005\u0005"+
		"\u0000\u0000#%\u0003\b\u0004\u0000$#\u0001\u0000\u0000\u0000%(\u0001\u0000"+
		"\u0000\u0000&$\u0001\u0000\u0000\u0000&\'\u0001\u0000\u0000\u0000\'\u0005"+
		"\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000)*\u0005\u0003\u0000"+
		"\u0000*.\u0007\u0001\u0000\u0000+-\u0003\b\u0004\u0000,+\u0001\u0000\u0000"+
		"\u0000-0\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000"+
		"\u0000\u0000/\u0007\u0001\u0000\u0000\u00000.\u0001\u0000\u0000\u0000"+
		"1A\u0007\u0002\u0000\u00002A\u0005\u000b\u0000\u00003A\u0005\f\u0000\u0000"+
		"4A\u0005\u0012\u0000\u00005A\u0007\u0003\u0000\u00006A\u0005\u0016\u0000"+
		"\u00007A\u0005\r\u0000\u00008A\u0005\u000e\u0000\u00009A\u0005\u000f\u0000"+
		"\u0000:A\u0005\u0010\u0000\u0000;A\u0005\u0011\u0000\u0000<A\u0005\u0017"+
		"\u0000\u0000=A\u0005\u0004\u0000\u0000>A\u0005\u0005\u0000\u0000?A\u0005"+
		"\u0003\u0000\u0000@1\u0001\u0000\u0000\u0000@2\u0001\u0000\u0000\u0000"+
		"@3\u0001\u0000\u0000\u0000@4\u0001\u0000\u0000\u0000@5\u0001\u0000\u0000"+
		"\u0000@6\u0001\u0000\u0000\u0000@7\u0001\u0000\u0000\u0000@8\u0001\u0000"+
		"\u0000\u0000@9\u0001\u0000\u0000\u0000@:\u0001\u0000\u0000\u0000@;\u0001"+
		"\u0000\u0000\u0000@<\u0001\u0000\u0000\u0000@=\u0001\u0000\u0000\u0000"+
		"@>\u0001\u0000\u0000\u0000@?\u0001\u0000\u0000\u0000A\t\u0001\u0000\u0000"+
		"\u0000\u0006\u0013\u001c\u001f&.@";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}