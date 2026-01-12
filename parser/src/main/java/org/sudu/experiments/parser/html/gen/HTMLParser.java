// Generated from parser-generator/src/main/resources/grammar/html/HTMLParser.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.html.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class HTMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		HTML_COMMENT=1, HTML_CONDITIONAL_COMMENT=2, XML=3, CDATA=4, DTD=5, SCRIPTLET=6, 
		SEA_WS=7, SCRIPT_OPEN=8, STYLE_OPEN=9, SEA_NEW_LINE=10, TAG_OPEN=11, HTML_TEXT=12, 
		TAG_CLOSE=13, TAG_SLASH_CLOSE=14, TAG_SLASH=15, TAG_EQUALS=16, TAG_NAME=17, 
		TAG_WHITESPACE=18, TAG_NEW_LINE=19, ERROR=20, SCRIPT_BODY=21, SCRIPT_SHORT_BODY=22, 
		STYLE_BODY=23, STYLE_SHORT_BODY=24, ATTVALUE_VALUE=25, ATTRIBUTE=26;
	public static final int
		RULE_htmlDocument = 0, RULE_scriptletOrSeaWs = 1, RULE_htmlElements = 2, 
		RULE_htmlElement = 3, RULE_htmlContent = 4, RULE_htmlAttribute = 5, RULE_htmlChardata = 6, 
		RULE_htmlMisc = 7, RULE_htmlComment = 8, RULE_script = 9, RULE_style = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"htmlDocument", "scriptletOrSeaWs", "htmlElements", "htmlElement", "htmlContent", 
			"htmlAttribute", "htmlChardata", "htmlMisc", "htmlComment", "script", 
			"style"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "'<'", 
			null, "'>'", "'/>'", "'/'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "HTML_COMMENT", "HTML_CONDITIONAL_COMMENT", "XML", "CDATA", "DTD", 
			"SCRIPTLET", "SEA_WS", "SCRIPT_OPEN", "STYLE_OPEN", "SEA_NEW_LINE", "TAG_OPEN", 
			"HTML_TEXT", "TAG_CLOSE", "TAG_SLASH_CLOSE", "TAG_SLASH", "TAG_EQUALS", 
			"TAG_NAME", "TAG_WHITESPACE", "TAG_NEW_LINE", "ERROR", "SCRIPT_BODY", 
			"SCRIPT_SHORT_BODY", "STYLE_BODY", "STYLE_SHORT_BODY", "ATTVALUE_VALUE", 
			"ATTRIBUTE"
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
	public String getGrammarFileName() { return "HTMLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HTMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HtmlDocumentContext extends ParserRuleContext {
		public HtmlContentContext htmlContent() {
			return getRuleContext(HtmlContentContext.class,0);
		}
		public TerminalNode EOF() { return getToken(HTMLParser.EOF, 0); }
		public List<ScriptletOrSeaWsContext> scriptletOrSeaWs() {
			return getRuleContexts(ScriptletOrSeaWsContext.class);
		}
		public ScriptletOrSeaWsContext scriptletOrSeaWs(int i) {
			return getRuleContext(ScriptletOrSeaWsContext.class,i);
		}
		public TerminalNode XML() { return getToken(HTMLParser.XML, 0); }
		public TerminalNode DTD() { return getToken(HTMLParser.DTD, 0); }
		public HtmlDocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlDocument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlDocument(this);
		}
	}

	public final HtmlDocumentContext htmlDocument() throws RecognitionException {
		HtmlDocumentContext _localctx = new HtmlDocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_htmlDocument);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(22);
					scriptletOrSeaWs();
					}
					} 
				}
				setState(27);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==XML) {
				{
				setState(28);
				match(XML);
				}
			}

			setState(34);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(31);
					scriptletOrSeaWs();
					}
					} 
				}
				setState(36);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DTD) {
				{
				setState(37);
				match(DTD);
				}
			}

			setState(43);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(40);
					scriptletOrSeaWs();
					}
					} 
				}
				setState(45);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(46);
			htmlContent();
			setState(47);
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
	public static class ScriptletOrSeaWsContext extends ParserRuleContext {
		public TerminalNode SCRIPTLET() { return getToken(HTMLParser.SCRIPTLET, 0); }
		public TerminalNode SEA_WS() { return getToken(HTMLParser.SEA_WS, 0); }
		public TerminalNode SEA_NEW_LINE() { return getToken(HTMLParser.SEA_NEW_LINE, 0); }
		public ScriptletOrSeaWsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scriptletOrSeaWs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterScriptletOrSeaWs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitScriptletOrSeaWs(this);
		}
	}

	public final ScriptletOrSeaWsContext scriptletOrSeaWs() throws RecognitionException {
		ScriptletOrSeaWsContext _localctx = new ScriptletOrSeaWsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_scriptletOrSeaWs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1216L) != 0)) ) {
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
	public static class HtmlElementsContext extends ParserRuleContext {
		public HtmlElementContext htmlElement() {
			return getRuleContext(HtmlElementContext.class,0);
		}
		public List<HtmlMiscContext> htmlMisc() {
			return getRuleContexts(HtmlMiscContext.class);
		}
		public HtmlMiscContext htmlMisc(int i) {
			return getRuleContext(HtmlMiscContext.class,i);
		}
		public HtmlElementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlElements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlElements(this);
		}
	}

	public final HtmlElementsContext htmlElements() throws RecognitionException {
		HtmlElementsContext _localctx = new HtmlElementsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_htmlElements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1158L) != 0)) {
				{
				{
				setState(51);
				htmlMisc();
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
			htmlElement();
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1158L) != 0)) {
				{
				{
				setState(58);
				htmlMisc();
				}
				}
				setState(63);
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
	public static class HtmlElementContext extends ParserRuleContext {
		public TerminalNode TAG_OPEN() { return getToken(HTMLParser.TAG_OPEN, 0); }
		public TerminalNode TAG_NAME() { return getToken(HTMLParser.TAG_NAME, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(HTMLParser.TAG_CLOSE, 0); }
		public TerminalNode TAG_SLASH_CLOSE() { return getToken(HTMLParser.TAG_SLASH_CLOSE, 0); }
		public List<HtmlAttributeContext> htmlAttribute() {
			return getRuleContexts(HtmlAttributeContext.class);
		}
		public HtmlAttributeContext htmlAttribute(int i) {
			return getRuleContext(HtmlAttributeContext.class,i);
		}
		public TerminalNode TAG_SLASH() { return getToken(HTMLParser.TAG_SLASH, 0); }
		public TerminalNode SCRIPTLET() { return getToken(HTMLParser.SCRIPTLET, 0); }
		public ScriptContext script() {
			return getRuleContext(ScriptContext.class,0);
		}
		public StyleContext style() {
			return getRuleContext(StyleContext.class,0);
		}
		public HtmlElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlElement(this);
		}
	}

	public final HtmlElementContext htmlElement() throws RecognitionException {
		HtmlElementContext _localctx = new HtmlElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_htmlElement);
		int _la;
		try {
			int _alt;
			setState(80);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				match(TAG_OPEN);
				setState(65);
				match(TAG_NAME);
				setState(69);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(66);
						htmlAttribute();
						}
						} 
					}
					setState(71);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(72);
				_la = _input.LA(1);
				if ( !(_la==TAG_CLOSE || _la==TAG_SLASH_CLOSE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(73);
				match(TAG_OPEN);
				setState(74);
				match(TAG_SLASH);
				setState(75);
				match(TAG_NAME);
				setState(76);
				match(TAG_CLOSE);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(77);
				match(SCRIPTLET);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(78);
				script();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(79);
				style();
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
	public static class HtmlContentContext extends ParserRuleContext {
		public List<HtmlChardataContext> htmlChardata() {
			return getRuleContexts(HtmlChardataContext.class);
		}
		public HtmlChardataContext htmlChardata(int i) {
			return getRuleContext(HtmlChardataContext.class,i);
		}
		public List<HtmlElementContext> htmlElement() {
			return getRuleContexts(HtmlElementContext.class);
		}
		public HtmlElementContext htmlElement(int i) {
			return getRuleContext(HtmlElementContext.class,i);
		}
		public List<TerminalNode> CDATA() { return getTokens(HTMLParser.CDATA); }
		public TerminalNode CDATA(int i) {
			return getToken(HTMLParser.CDATA, i);
		}
		public List<HtmlCommentContext> htmlComment() {
			return getRuleContexts(HtmlCommentContext.class);
		}
		public HtmlCommentContext htmlComment(int i) {
			return getRuleContext(HtmlCommentContext.class,i);
		}
		public HtmlContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlContent(this);
		}
	}

	public final HtmlContentContext htmlContent() throws RecognitionException {
		HtmlContentContext _localctx = new HtmlContentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_htmlContent);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(82);
					htmlChardata();
					}
					} 
				}
				setState(87);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(101);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(91);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SCRIPTLET:
					case SCRIPT_OPEN:
					case STYLE_OPEN:
					case TAG_OPEN:
						{
						setState(88);
						htmlElement();
						}
						break;
					case CDATA:
						{
						setState(89);
						match(CDATA);
						}
						break;
					case HTML_COMMENT:
					case HTML_CONDITIONAL_COMMENT:
						{
						setState(90);
						htmlComment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(96);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(93);
							htmlChardata();
							}
							} 
						}
						setState(98);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
					}
					}
					} 
				}
				setState(103);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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
	public static class HtmlAttributeContext extends ParserRuleContext {
		public TerminalNode TAG_NAME() { return getToken(HTMLParser.TAG_NAME, 0); }
		public TerminalNode TAG_EQUALS() { return getToken(HTMLParser.TAG_EQUALS, 0); }
		public TerminalNode ATTVALUE_VALUE() { return getToken(HTMLParser.ATTVALUE_VALUE, 0); }
		public HtmlAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlAttribute(this);
		}
	}

	public final HtmlAttributeContext htmlAttribute() throws RecognitionException {
		HtmlAttributeContext _localctx = new HtmlAttributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_htmlAttribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(TAG_NAME);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TAG_EQUALS) {
				{
				setState(105);
				match(TAG_EQUALS);
				setState(106);
				match(ATTVALUE_VALUE);
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
	public static class HtmlChardataContext extends ParserRuleContext {
		public TerminalNode HTML_TEXT() { return getToken(HTMLParser.HTML_TEXT, 0); }
		public TerminalNode SEA_WS() { return getToken(HTMLParser.SEA_WS, 0); }
		public TerminalNode SEA_NEW_LINE() { return getToken(HTMLParser.SEA_NEW_LINE, 0); }
		public HtmlChardataContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlChardata; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlChardata(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlChardata(this);
		}
	}

	public final HtmlChardataContext htmlChardata() throws RecognitionException {
		HtmlChardataContext _localctx = new HtmlChardataContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_htmlChardata);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 5248L) != 0)) ) {
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
	public static class HtmlMiscContext extends ParserRuleContext {
		public HtmlCommentContext htmlComment() {
			return getRuleContext(HtmlCommentContext.class,0);
		}
		public TerminalNode SEA_WS() { return getToken(HTMLParser.SEA_WS, 0); }
		public TerminalNode SEA_NEW_LINE() { return getToken(HTMLParser.SEA_NEW_LINE, 0); }
		public HtmlMiscContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlMisc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlMisc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlMisc(this);
		}
	}

	public final HtmlMiscContext htmlMisc() throws RecognitionException {
		HtmlMiscContext _localctx = new HtmlMiscContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_htmlMisc);
		try {
			setState(114);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case HTML_COMMENT:
			case HTML_CONDITIONAL_COMMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(111);
				htmlComment();
				}
				break;
			case SEA_WS:
				enterOuterAlt(_localctx, 2);
				{
				setState(112);
				match(SEA_WS);
				}
				break;
			case SEA_NEW_LINE:
				enterOuterAlt(_localctx, 3);
				{
				setState(113);
				match(SEA_NEW_LINE);
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
	public static class HtmlCommentContext extends ParserRuleContext {
		public TerminalNode HTML_COMMENT() { return getToken(HTMLParser.HTML_COMMENT, 0); }
		public TerminalNode HTML_CONDITIONAL_COMMENT() { return getToken(HTMLParser.HTML_CONDITIONAL_COMMENT, 0); }
		public HtmlCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlComment(this);
		}
	}

	public final HtmlCommentContext htmlComment() throws RecognitionException {
		HtmlCommentContext _localctx = new HtmlCommentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_htmlComment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			_la = _input.LA(1);
			if ( !(_la==HTML_COMMENT || _la==HTML_CONDITIONAL_COMMENT) ) {
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
	public static class ScriptContext extends ParserRuleContext {
		public TerminalNode SCRIPT_OPEN() { return getToken(HTMLParser.SCRIPT_OPEN, 0); }
		public TerminalNode SCRIPT_BODY() { return getToken(HTMLParser.SCRIPT_BODY, 0); }
		public TerminalNode SCRIPT_SHORT_BODY() { return getToken(HTMLParser.SCRIPT_SHORT_BODY, 0); }
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterScript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitScript(this);
		}
	}

	public final ScriptContext script() throws RecognitionException {
		ScriptContext _localctx = new ScriptContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_script);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(SCRIPT_OPEN);
			setState(119);
			_la = _input.LA(1);
			if ( !(_la==SCRIPT_BODY || _la==SCRIPT_SHORT_BODY) ) {
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
	public static class StyleContext extends ParserRuleContext {
		public TerminalNode STYLE_OPEN() { return getToken(HTMLParser.STYLE_OPEN, 0); }
		public TerminalNode STYLE_BODY() { return getToken(HTMLParser.STYLE_BODY, 0); }
		public TerminalNode STYLE_SHORT_BODY() { return getToken(HTMLParser.STYLE_SHORT_BODY, 0); }
		public StyleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_style; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterStyle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitStyle(this);
		}
	}

	public final StyleContext style() throws RecognitionException {
		StyleContext _localctx = new StyleContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_style);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(STYLE_OPEN);
			setState(122);
			_la = _input.LA(1);
			if ( !(_la==STYLE_BODY || _la==STYLE_SHORT_BODY) ) {
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
		"\u0004\u0001\u001a}\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000\u0005\u0000\u0018"+
		"\b\u0000\n\u0000\f\u0000\u001b\t\u0000\u0001\u0000\u0003\u0000\u001e\b"+
		"\u0000\u0001\u0000\u0005\u0000!\b\u0000\n\u0000\f\u0000$\t\u0000\u0001"+
		"\u0000\u0003\u0000\'\b\u0000\u0001\u0000\u0005\u0000*\b\u0000\n\u0000"+
		"\f\u0000-\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0005\u00025\b\u0002\n\u0002\f\u00028\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002<\b\u0002\n\u0002\f\u0002?\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003D\b\u0003\n\u0003\f\u0003G\t"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003Q\b\u0003\u0001\u0004\u0005"+
		"\u0004T\b\u0004\n\u0004\f\u0004W\t\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004\\\b\u0004\u0001\u0004\u0005\u0004_\b\u0004\n\u0004"+
		"\f\u0004b\t\u0004\u0005\u0004d\b\u0004\n\u0004\f\u0004g\t\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005l\b\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007s\b\u0007\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0004"+
		"EU`e\u0000\u000b\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0000\u0006\u0002\u0000\u0006\u0007\n\n\u0001\u0000\r\u000e\u0003\u0000"+
		"\u0007\u0007\n\n\f\f\u0001\u0000\u0001\u0002\u0001\u0000\u0015\u0016\u0001"+
		"\u0000\u0017\u0018\u0085\u0000\u0019\u0001\u0000\u0000\u0000\u00021\u0001"+
		"\u0000\u0000\u0000\u00046\u0001\u0000\u0000\u0000\u0006P\u0001\u0000\u0000"+
		"\u0000\bU\u0001\u0000\u0000\u0000\nh\u0001\u0000\u0000\u0000\fm\u0001"+
		"\u0000\u0000\u0000\u000er\u0001\u0000\u0000\u0000\u0010t\u0001\u0000\u0000"+
		"\u0000\u0012v\u0001\u0000\u0000\u0000\u0014y\u0001\u0000\u0000\u0000\u0016"+
		"\u0018\u0003\u0002\u0001\u0000\u0017\u0016\u0001\u0000\u0000\u0000\u0018"+
		"\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019"+
		"\u001a\u0001\u0000\u0000\u0000\u001a\u001d\u0001\u0000\u0000\u0000\u001b"+
		"\u0019\u0001\u0000\u0000\u0000\u001c\u001e\u0005\u0003\u0000\u0000\u001d"+
		"\u001c\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e"+
		"\"\u0001\u0000\u0000\u0000\u001f!\u0003\u0002\u0001\u0000 \u001f\u0001"+
		"\u0000\u0000\u0000!$\u0001\u0000\u0000\u0000\" \u0001\u0000\u0000\u0000"+
		"\"#\u0001\u0000\u0000\u0000#&\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000"+
		"\u0000%\'\u0005\u0005\u0000\u0000&%\u0001\u0000\u0000\u0000&\'\u0001\u0000"+
		"\u0000\u0000\'+\u0001\u0000\u0000\u0000(*\u0003\u0002\u0001\u0000)(\u0001"+
		"\u0000\u0000\u0000*-\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000"+
		"+,\u0001\u0000\u0000\u0000,.\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000"+
		"\u0000./\u0003\b\u0004\u0000/0\u0005\u0000\u0000\u00010\u0001\u0001\u0000"+
		"\u0000\u000012\u0007\u0000\u0000\u00002\u0003\u0001\u0000\u0000\u0000"+
		"35\u0003\u000e\u0007\u000043\u0001\u0000\u0000\u000058\u0001\u0000\u0000"+
		"\u000064\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u000079\u0001\u0000"+
		"\u0000\u000086\u0001\u0000\u0000\u00009=\u0003\u0006\u0003\u0000:<\u0003"+
		"\u000e\u0007\u0000;:\u0001\u0000\u0000\u0000<?\u0001\u0000\u0000\u0000"+
		"=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>\u0005\u0001\u0000"+
		"\u0000\u0000?=\u0001\u0000\u0000\u0000@A\u0005\u000b\u0000\u0000AE\u0005"+
		"\u0011\u0000\u0000BD\u0003\n\u0005\u0000CB\u0001\u0000\u0000\u0000DG\u0001"+
		"\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000"+
		"FH\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000HQ\u0007\u0001\u0000"+
		"\u0000IJ\u0005\u000b\u0000\u0000JK\u0005\u000f\u0000\u0000KL\u0005\u0011"+
		"\u0000\u0000LQ\u0005\r\u0000\u0000MQ\u0005\u0006\u0000\u0000NQ\u0003\u0012"+
		"\t\u0000OQ\u0003\u0014\n\u0000P@\u0001\u0000\u0000\u0000PI\u0001\u0000"+
		"\u0000\u0000PM\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000PO\u0001"+
		"\u0000\u0000\u0000Q\u0007\u0001\u0000\u0000\u0000RT\u0003\f\u0006\u0000"+
		"SR\u0001\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000"+
		"\u0000US\u0001\u0000\u0000\u0000Ve\u0001\u0000\u0000\u0000WU\u0001\u0000"+
		"\u0000\u0000X\\\u0003\u0006\u0003\u0000Y\\\u0005\u0004\u0000\u0000Z\\"+
		"\u0003\u0010\b\u0000[X\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000"+
		"[Z\u0001\u0000\u0000\u0000\\`\u0001\u0000\u0000\u0000]_\u0003\f\u0006"+
		"\u0000^]\u0001\u0000\u0000\u0000_b\u0001\u0000\u0000\u0000`a\u0001\u0000"+
		"\u0000\u0000`^\u0001\u0000\u0000\u0000ad\u0001\u0000\u0000\u0000b`\u0001"+
		"\u0000\u0000\u0000c[\u0001\u0000\u0000\u0000dg\u0001\u0000\u0000\u0000"+
		"ef\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000f\t\u0001\u0000\u0000"+
		"\u0000ge\u0001\u0000\u0000\u0000hk\u0005\u0011\u0000\u0000ij\u0005\u0010"+
		"\u0000\u0000jl\u0005\u0019\u0000\u0000ki\u0001\u0000\u0000\u0000kl\u0001"+
		"\u0000\u0000\u0000l\u000b\u0001\u0000\u0000\u0000mn\u0007\u0002\u0000"+
		"\u0000n\r\u0001\u0000\u0000\u0000os\u0003\u0010\b\u0000ps\u0005\u0007"+
		"\u0000\u0000qs\u0005\n\u0000\u0000ro\u0001\u0000\u0000\u0000rp\u0001\u0000"+
		"\u0000\u0000rq\u0001\u0000\u0000\u0000s\u000f\u0001\u0000\u0000\u0000"+
		"tu\u0007\u0003\u0000\u0000u\u0011\u0001\u0000\u0000\u0000vw\u0005\b\u0000"+
		"\u0000wx\u0007\u0004\u0000\u0000x\u0013\u0001\u0000\u0000\u0000yz\u0005"+
		"\t\u0000\u0000z{\u0007\u0005\u0000\u0000{\u0015\u0001\u0000\u0000\u0000"+
		"\u000f\u0019\u001d\"&+6=EPU[`ekr";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}