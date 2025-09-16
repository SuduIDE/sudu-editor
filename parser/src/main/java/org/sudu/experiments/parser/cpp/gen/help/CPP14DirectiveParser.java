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
		T__0=1, T__1=2, String=3, Hash=4, Include=5, Error=6, Whitespace=7, BlockComment=8, 
		LineComment=9, NewLineSlash=10, NewLine=11, Left=12, Right=13, IntegerLiteral=14, 
		DecimalLiteral=15, OctalLiteral=16, HexadecimalLiteral=17, BinaryLiteral=18, 
		Keyword=19, Operators=20, Identifier=21, DirChar=22, Unknown=23;
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
			null, "'/'", "'.'", null, "'#'", "'include'", "'error'", null, null, 
			null, "'\\'", null, "'<'", "'>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "String", "Hash", "Include", "Error", "Whitespace", 
			"BlockComment", "LineComment", "NewLineSlash", "NewLine", "Left", "Right", 
			"IntegerLiteral", "DecimalLiteral", "OctalLiteral", "HexadecimalLiteral", 
			"BinaryLiteral", "Keyword", "Operators", "Identifier", "DirChar", "Unknown"
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
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2097158L) != 0)) ) {
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
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 2097158L) != 0) );
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16773240L) != 0)) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16773240L) != 0)) {
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
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16773240L) != 0)) ) {
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

	public static final String _serializedATN =
		"\u0004\u0001\u00174\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0014\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0004\u0001\u001b\b\u0001\u000b"+
		"\u0001\f\u0001\u001c\u0001\u0001\u0003\u0001 \b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002%\b\u0002\n\u0002\f\u0002(\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003-\b\u0003\n\u0003\f\u00030\t"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0000\u0000\u0005\u0000\u0002"+
		"\u0004\u0006\b\u0000\u0003\u0002\u0000\u0001\u0002\u0015\u0015\u0002\u0000"+
		"\u0013\u0013\u0015\u0015\u0002\u0000\u0003\u0006\f\u00174\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0002\u0015\u0001\u0000\u0000\u0000\u0004!\u0001"+
		"\u0000\u0000\u0000\u0006)\u0001\u0000\u0000\u0000\b1\u0001\u0000\u0000"+
		"\u0000\n\u000b\u0003\u0002\u0001\u0000\u000b\f\u0005\u0000\u0000\u0001"+
		"\f\u0014\u0001\u0000\u0000\u0000\r\u000e\u0003\u0004\u0002\u0000\u000e"+
		"\u000f\u0005\u0000\u0000\u0001\u000f\u0014\u0001\u0000\u0000\u0000\u0010"+
		"\u0011\u0003\u0006\u0003\u0000\u0011\u0012\u0005\u0000\u0000\u0001\u0012"+
		"\u0014\u0001\u0000\u0000\u0000\u0013\n\u0001\u0000\u0000\u0000\u0013\r"+
		"\u0001\u0000\u0000\u0000\u0013\u0010\u0001\u0000\u0000\u0000\u0014\u0001"+
		"\u0001\u0000\u0000\u0000\u0015\u0016\u0005\u0004\u0000\u0000\u0016\u001f"+
		"\u0005\u0005\u0000\u0000\u0017 \u0005\u0003\u0000\u0000\u0018\u001a\u0005"+
		"\f\u0000\u0000\u0019\u001b\u0007\u0000\u0000\u0000\u001a\u0019\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000"+
		"\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000"+
		"\u0000\u0000\u001e \u0005\r\u0000\u0000\u001f\u0017\u0001\u0000\u0000"+
		"\u0000\u001f\u0018\u0001\u0000\u0000\u0000 \u0003\u0001\u0000\u0000\u0000"+
		"!\"\u0005\u0004\u0000\u0000\"&\u0005\u0006\u0000\u0000#%\u0003\b\u0004"+
		"\u0000$#\u0001\u0000\u0000\u0000%(\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000&\'\u0001\u0000\u0000\u0000\'\u0005\u0001\u0000\u0000\u0000"+
		"(&\u0001\u0000\u0000\u0000)*\u0005\u0004\u0000\u0000*.\u0007\u0001\u0000"+
		"\u0000+-\u0003\b\u0004\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000\u0000"+
		"\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/\u0007\u0001"+
		"\u0000\u0000\u00000.\u0001\u0000\u0000\u000012\u0007\u0002\u0000\u0000"+
		"2\t\u0001\u0000\u0000\u0000\u0005\u0013\u001c\u001f&.";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}