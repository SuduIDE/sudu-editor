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
		String=1, Hash=2, Include=3, Error=4, Whitespace=5, BlockComment=6, LineComment=7, 
		NewLineSlash=8, NewLine=9, Left=10, Right=11, IntegerLiteral=12, DecimalLiteral=13, 
		OctalLiteral=14, HexadecimalLiteral=15, BinaryLiteral=16, Keyword=17, 
		Operators=18, Slash=19, Dot=20, Identifier=21, DirChar=22, Unknown=23;
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
			null, null, "'#'", "'include'", "'error'", null, null, null, "'\\'", 
			null, "'<'", "'>'", null, null, null, null, null, null, null, "'/'", 
			"'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "String", "Hash", "Include", "Error", "Whitespace", "BlockComment", 
			"LineComment", "NewLineSlash", "NewLine", "Left", "Right", "IntegerLiteral", 
			"DecimalLiteral", "OctalLiteral", "HexadecimalLiteral", "BinaryLiteral", 
			"Keyword", "Operators", "Slash", "Dot", "Identifier", "DirChar", "Unknown"
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
		public TerminalNode Hash() { return getToken(CPP14DirectiveParser.Hash, 0); }
		public TerminalNode Include() { return getToken(CPP14DirectiveParser.Include, 0); }
		public TerminalNode String() { return getToken(CPP14DirectiveParser.String, 0); }
		public TerminalNode Left() { return getToken(CPP14DirectiveParser.Left, 0); }
		public TerminalNode Right() { return getToken(CPP14DirectiveParser.Right, 0); }
		public List<TerminalNode> Identifier() { return getTokens(CPP14DirectiveParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CPP14DirectiveParser.Identifier, i);
		}
		public List<TerminalNode> Slash() { return getTokens(CPP14DirectiveParser.Slash); }
		public TerminalNode Slash(int i) {
			return getToken(CPP14DirectiveParser.Slash, i);
		}
		public List<TerminalNode> Dot() { return getTokens(CPP14DirectiveParser.Dot); }
		public TerminalNode Dot(int i) {
			return getToken(CPP14DirectiveParser.Dot, i);
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
			match(Hash);
			setState(22);
			match(Include);
			setState(31);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
				{
				setState(23);
				match(String);
				}
				break;
			case Left:
				{
				{
				setState(24);
				match(Left);
				setState(26); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(25);
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
					}
					setState(28); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 3670016L) != 0) );
				setState(30);
				match(Right);
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
		public TerminalNode Hash() { return getToken(CPP14DirectiveParser.Hash, 0); }
		public TerminalNode Error() { return getToken(CPP14DirectiveParser.Error, 0); }
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
			match(Hash);
			setState(34);
			match(Error);
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16776222L) != 0)) {
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
		public TerminalNode Hash() { return getToken(CPP14DirectiveParser.Hash, 0); }
		public TerminalNode Identifier() { return getToken(CPP14DirectiveParser.Identifier, 0); }
		public TerminalNode Keyword() { return getToken(CPP14DirectiveParser.Keyword, 0); }
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
			match(Hash);
			setState(42);
			_la = _input.LA(1);
			if ( !(_la==Keyword || _la==Identifier) ) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16776222L) != 0)) {
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
		public TerminalNode String() { return getToken(CPP14DirectiveParser.String, 0); }
		public TerminalNode Left() { return getToken(CPP14DirectiveParser.Left, 0); }
		public TerminalNode Right() { return getToken(CPP14DirectiveParser.Right, 0); }
		public TerminalNode Keyword() { return getToken(CPP14DirectiveParser.Keyword, 0); }
		public TerminalNode Operators() { return getToken(CPP14DirectiveParser.Operators, 0); }
		public TerminalNode Dot() { return getToken(CPP14DirectiveParser.Dot, 0); }
		public TerminalNode Slash() { return getToken(CPP14DirectiveParser.Slash, 0); }
		public TerminalNode Identifier() { return getToken(CPP14DirectiveParser.Identifier, 0); }
		public TerminalNode IntegerLiteral() { return getToken(CPP14DirectiveParser.IntegerLiteral, 0); }
		public TerminalNode DecimalLiteral() { return getToken(CPP14DirectiveParser.DecimalLiteral, 0); }
		public TerminalNode OctalLiteral() { return getToken(CPP14DirectiveParser.OctalLiteral, 0); }
		public TerminalNode HexadecimalLiteral() { return getToken(CPP14DirectiveParser.HexadecimalLiteral, 0); }
		public TerminalNode BinaryLiteral() { return getToken(CPP14DirectiveParser.BinaryLiteral, 0); }
		public TerminalNode DirChar() { return getToken(CPP14DirectiveParser.DirChar, 0); }
		public TerminalNode Include() { return getToken(CPP14DirectiveParser.Include, 0); }
		public TerminalNode Error() { return getToken(CPP14DirectiveParser.Error, 0); }
		public TerminalNode Unknown() { return getToken(CPP14DirectiveParser.Unknown, 0); }
		public TerminalNode Hash() { return getToken(CPP14DirectiveParser.Hash, 0); }
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
			setState(65);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(49);
				match(String);
				}
				break;
			case Left:
				enterOuterAlt(_localctx, 2);
				{
				setState(50);
				match(Left);
				}
				break;
			case Right:
				enterOuterAlt(_localctx, 3);
				{
				setState(51);
				match(Right);
				}
				break;
			case Keyword:
				enterOuterAlt(_localctx, 4);
				{
				setState(52);
				match(Keyword);
				}
				break;
			case Operators:
			case Slash:
			case Dot:
				enterOuterAlt(_localctx, 5);
				{
				setState(53);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1835008L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 6);
				{
				setState(54);
				match(Identifier);
				}
				break;
			case IntegerLiteral:
				enterOuterAlt(_localctx, 7);
				{
				setState(55);
				match(IntegerLiteral);
				}
				break;
			case DecimalLiteral:
				enterOuterAlt(_localctx, 8);
				{
				setState(56);
				match(DecimalLiteral);
				}
				break;
			case OctalLiteral:
				enterOuterAlt(_localctx, 9);
				{
				setState(57);
				match(OctalLiteral);
				}
				break;
			case HexadecimalLiteral:
				enterOuterAlt(_localctx, 10);
				{
				setState(58);
				match(HexadecimalLiteral);
				}
				break;
			case BinaryLiteral:
				enterOuterAlt(_localctx, 11);
				{
				setState(59);
				match(BinaryLiteral);
				}
				break;
			case DirChar:
				enterOuterAlt(_localctx, 12);
				{
				setState(60);
				match(DirChar);
				}
				break;
			case Include:
				enterOuterAlt(_localctx, 13);
				{
				setState(61);
				match(Include);
				}
				break;
			case Error:
				enterOuterAlt(_localctx, 14);
				{
				setState(62);
				match(Error);
				}
				break;
			case Unknown:
				enterOuterAlt(_localctx, 15);
				{
				setState(63);
				match(Unknown);
				}
				break;
			case Hash:
				enterOuterAlt(_localctx, 16);
				{
				setState(64);
				match(Hash);
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
		"\u0004\u0001\u0017D\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0014\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0004\u0001\u001b\b\u0001\u000b"+
		"\u0001\f\u0001\u001c\u0001\u0001\u0003\u0001 \b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002%\b\u0002\n\u0002\f\u0002(\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003-\b\u0003\n\u0003\f\u00030\t"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004B\b"+
		"\u0004\u0001\u0004\u0000\u0000\u0005\u0000\u0002\u0004\u0006\b\u0000\u0003"+
		"\u0001\u0000\u0013\u0015\u0002\u0000\u0011\u0011\u0015\u0015\u0001\u0000"+
		"\u0012\u0014S\u0000\u0013\u0001\u0000\u0000\u0000\u0002\u0015\u0001\u0000"+
		"\u0000\u0000\u0004!\u0001\u0000\u0000\u0000\u0006)\u0001\u0000\u0000\u0000"+
		"\bA\u0001\u0000\u0000\u0000\n\u000b\u0003\u0002\u0001\u0000\u000b\f\u0005"+
		"\u0000\u0000\u0001\f\u0014\u0001\u0000\u0000\u0000\r\u000e\u0003\u0004"+
		"\u0002\u0000\u000e\u000f\u0005\u0000\u0000\u0001\u000f\u0014\u0001\u0000"+
		"\u0000\u0000\u0010\u0011\u0003\u0006\u0003\u0000\u0011\u0012\u0005\u0000"+
		"\u0000\u0001\u0012\u0014\u0001\u0000\u0000\u0000\u0013\n\u0001\u0000\u0000"+
		"\u0000\u0013\r\u0001\u0000\u0000\u0000\u0013\u0010\u0001\u0000\u0000\u0000"+
		"\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u0016\u0005\u0002\u0000\u0000"+
		"\u0016\u001f\u0005\u0003\u0000\u0000\u0017 \u0005\u0001\u0000\u0000\u0018"+
		"\u001a\u0005\n\u0000\u0000\u0019\u001b\u0007\u0000\u0000\u0000\u001a\u0019"+
		"\u0001\u0000\u0000\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u001a"+
		"\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001e"+
		"\u0001\u0000\u0000\u0000\u001e \u0005\u000b\u0000\u0000\u001f\u0017\u0001"+
		"\u0000\u0000\u0000\u001f\u0018\u0001\u0000\u0000\u0000 \u0003\u0001\u0000"+
		"\u0000\u0000!\"\u0005\u0002\u0000\u0000\"&\u0005\u0004\u0000\u0000#%\u0003"+
		"\b\u0004\u0000$#\u0001\u0000\u0000\u0000%(\u0001\u0000\u0000\u0000&$\u0001"+
		"\u0000\u0000\u0000&\'\u0001\u0000\u0000\u0000\'\u0005\u0001\u0000\u0000"+
		"\u0000(&\u0001\u0000\u0000\u0000)*\u0005\u0002\u0000\u0000*.\u0007\u0001"+
		"\u0000\u0000+-\u0003\b\u0004\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000"+
		"\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/\u0007"+
		"\u0001\u0000\u0000\u00000.\u0001\u0000\u0000\u00001B\u0005\u0001\u0000"+
		"\u00002B\u0005\n\u0000\u00003B\u0005\u000b\u0000\u00004B\u0005\u0011\u0000"+
		"\u00005B\u0007\u0002\u0000\u00006B\u0005\u0015\u0000\u00007B\u0005\f\u0000"+
		"\u00008B\u0005\r\u0000\u00009B\u0005\u000e\u0000\u0000:B\u0005\u000f\u0000"+
		"\u0000;B\u0005\u0010\u0000\u0000<B\u0005\u0016\u0000\u0000=B\u0005\u0003"+
		"\u0000\u0000>B\u0005\u0004\u0000\u0000?B\u0005\u0017\u0000\u0000@B\u0005"+
		"\u0002\u0000\u0000A1\u0001\u0000\u0000\u0000A2\u0001\u0000\u0000\u0000"+
		"A3\u0001\u0000\u0000\u0000A4\u0001\u0000\u0000\u0000A5\u0001\u0000\u0000"+
		"\u0000A6\u0001\u0000\u0000\u0000A7\u0001\u0000\u0000\u0000A8\u0001\u0000"+
		"\u0000\u0000A9\u0001\u0000\u0000\u0000A:\u0001\u0000\u0000\u0000A;\u0001"+
		"\u0000\u0000\u0000A<\u0001\u0000\u0000\u0000A=\u0001\u0000\u0000\u0000"+
		"A>\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000A@\u0001\u0000\u0000"+
		"\u0000B\t\u0001\u0000\u0000\u0000\u0006\u0013\u001c\u001f&.A";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}