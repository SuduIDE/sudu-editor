// Generated from parser-generator/src/main/resources/grammar/activity/ActivityParser.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.activity.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ActivityParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		AND=1, OR=2, XOR=3, NOT=4, EQ=5, COMMA=6, SEMI=7, LPAREN=8, RPAREN=9, 
		LCURLY=10, RCURLY=11, LESSER=12, GREATER=13, CONS=14, ACTIVITY=15, SELECT=16, 
		REPEAT=17, SCHEDULE=18, SEQUENCE=19, RANDOM=20, DEFAULT=21, IF=22, ELSE=23, 
		INT=24, ID=25, WS=26, JAVADOC=27, COMMENT=28, LINE_COMMENT=29, NEW_LINE=30, 
		ERROR=31;
	public static final int
		RULE_activity = 0, RULE_blocksemi = 1, RULE_block = 2, RULE_condblock = 3, 
		RULE_exprstat = 4, RULE_stat = 5, RULE_expr = 6, RULE_exprcomma = 7, RULE_exprcons = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"activity", "blocksemi", "block", "condblock", "exprstat", "stat", "expr", 
			"exprcomma", "exprcons"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'and'", "'or'", "'xor'", "'!'", "'='", "','", "';'", "'('", "')'", 
			"'{'", "'}'", "'<'", "'>'", "'->'", "'activity'", "'select'", "'repeat'", 
			"'schedule'", "'sequence'", "'random'", "'default'", "'if'", "'else'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "AND", "OR", "XOR", "NOT", "EQ", "COMMA", "SEMI", "LPAREN", "RPAREN", 
			"LCURLY", "RCURLY", "LESSER", "GREATER", "CONS", "ACTIVITY", "SELECT", 
			"REPEAT", "SCHEDULE", "SEQUENCE", "RANDOM", "DEFAULT", "IF", "ELSE", 
			"INT", "ID", "WS", "JAVADOC", "COMMENT", "LINE_COMMENT", "NEW_LINE", 
			"ERROR"
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
	public String getGrammarFileName() { return "ActivityParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ActivityParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActivityContext extends ParserRuleContext {
		public TerminalNode ACTIVITY() { return getToken(ActivityParser.ACTIVITY, 0); }
		public BlocksemiContext blocksemi() {
			return getRuleContext(BlocksemiContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ActivityParser.EOF, 0); }
		public ActivityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterActivity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitActivity(this);
		}
	}

	public final ActivityContext activity() throws RecognitionException {
		ActivityContext _localctx = new ActivityContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_activity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(ACTIVITY);
			setState(19);
			blocksemi();
			setState(20);
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
	public static class BlocksemiContext extends ParserRuleContext {
		public TerminalNode LCURLY() { return getToken(ActivityParser.LCURLY, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode RCURLY() { return getToken(ActivityParser.RCURLY, 0); }
		public List<TerminalNode> SEMI() { return getTokens(ActivityParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(ActivityParser.SEMI, i);
		}
		public BlocksemiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blocksemi; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterBlocksemi(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitBlocksemi(this);
		}
	}

	public final BlocksemiContext blocksemi() throws RecognitionException {
		BlocksemiContext _localctx = new BlocksemiContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_blocksemi);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			match(LCURLY);
			setState(23);
			stat();
			setState(28);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(24);
					match(SEMI);
					setState(25);
					stat();
					}
					} 
				}
				setState(30);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(31);
				match(SEMI);
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(37);
			match(RCURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode LCURLY() { return getToken(ActivityParser.LCURLY, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode RCURLY() { return getToken(ActivityParser.RCURLY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ActivityParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ActivityParser.COMMA, i);
		}
		public List<TerminalNode> SEMI() { return getTokens(ActivityParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(ActivityParser.SEMI, i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(LCURLY);
			setState(40);
			stat();
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA || _la==SEMI) {
				{
				{
				setState(41);
				_la = _input.LA(1);
				if ( !(_la==COMMA || _la==SEMI) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(42);
				stat();
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(48);
			match(RCURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CondblockContext extends ParserRuleContext {
		public TerminalNode LCURLY() { return getToken(ActivityParser.LCURLY, 0); }
		public List<ExprstatContext> exprstat() {
			return getRuleContexts(ExprstatContext.class);
		}
		public ExprstatContext exprstat(int i) {
			return getRuleContext(ExprstatContext.class,i);
		}
		public TerminalNode RCURLY() { return getToken(ActivityParser.RCURLY, 0); }
		public TerminalNode LPAREN() { return getToken(ActivityParser.LPAREN, 0); }
		public TerminalNode DEFAULT() { return getToken(ActivityParser.DEFAULT, 0); }
		public TerminalNode RPAREN() { return getToken(ActivityParser.RPAREN, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(ActivityParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ActivityParser.COMMA, i);
		}
		public List<TerminalNode> SEMI() { return getTokens(ActivityParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(ActivityParser.SEMI, i);
		}
		public CondblockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condblock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterCondblock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitCondblock(this);
		}
	}

	public final CondblockContext condblock() throws RecognitionException {
		CondblockContext _localctx = new CondblockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_condblock);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(LCURLY);
			setState(51);
			exprstat();
			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(52);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==SEMI) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(53);
					exprstat();
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA || _la==SEMI) {
				{
				setState(59);
				_la = _input.LA(1);
				if ( !(_la==COMMA || _la==SEMI) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(60);
				match(LPAREN);
				setState(61);
				match(DEFAULT);
				setState(62);
				match(RPAREN);
				setState(63);
				stat();
				}
			}

			setState(66);
			match(RCURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprstatContext extends ParserRuleContext {
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ActivityParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ActivityParser.RPAREN, 0); }
		public ExprstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprstat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterExprstat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitExprstat(this);
		}
	}

	public final ExprstatContext exprstat() throws RecognitionException {
		ExprstatContext _localctx = new ExprstatContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_exprstat);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(68);
				match(LPAREN);
				setState(69);
				expr(0);
				setState(70);
				match(RPAREN);
				}
			}

			setState(74);
			stat();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ActivityParser.ID, 0); }
		public TerminalNode REPEAT() { return getToken(ActivityParser.REPEAT, 0); }
		public TerminalNode LPAREN() { return getToken(ActivityParser.LPAREN, 0); }
		public TerminalNode INT() { return getToken(ActivityParser.INT, 0); }
		public TerminalNode RPAREN() { return getToken(ActivityParser.RPAREN, 0); }
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public TerminalNode SELECT() { return getToken(ActivityParser.SELECT, 0); }
		public CondblockContext condblock() {
			return getRuleContext(CondblockContext.class,0);
		}
		public TerminalNode SCHEDULE() { return getToken(ActivityParser.SCHEDULE, 0); }
		public TerminalNode SEQUENCE() { return getToken(ActivityParser.SEQUENCE, 0); }
		public TerminalNode RANDOM() { return getToken(ActivityParser.RANDOM, 0); }
		public TerminalNode IF() { return getToken(ActivityParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(ActivityParser.SEMI, 0); }
		public TerminalNode ELSE() { return getToken(ActivityParser.ELSE, 0); }
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitStat(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_stat);
		int _la;
		try {
			setState(105);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				match(ID);
				}
				break;
			case REPEAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(REPEAT);
				setState(78);
				match(LPAREN);
				setState(79);
				match(INT);
				setState(80);
				match(RPAREN);
				setState(81);
				block();
				}
				break;
			case SELECT:
				enterOuterAlt(_localctx, 3);
				{
				setState(82);
				match(SELECT);
				setState(83);
				condblock();
				}
				break;
			case SCHEDULE:
				enterOuterAlt(_localctx, 4);
				{
				setState(84);
				match(SCHEDULE);
				setState(85);
				block();
				}
				break;
			case SEQUENCE:
				enterOuterAlt(_localctx, 5);
				{
				setState(86);
				match(SEQUENCE);
				setState(87);
				block();
				}
				break;
			case RANDOM:
				enterOuterAlt(_localctx, 6);
				{
				setState(88);
				match(RANDOM);
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(89);
					match(LPAREN);
					setState(90);
					match(INT);
					setState(91);
					match(RPAREN);
					}
				}

				setState(94);
				block();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 7);
				{
				setState(95);
				match(IF);
				setState(96);
				match(LPAREN);
				setState(97);
				expr(0);
				setState(98);
				match(RPAREN);
				setState(99);
				block();
				setState(103);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(100);
					match(SEMI);
					setState(101);
					match(ELSE);
					setState(102);
					block();
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
	public static class ExprContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ActivityParser.ID, 0); }
		public TerminalNode LCURLY() { return getToken(ActivityParser.LCURLY, 0); }
		public ExprcommaContext exprcomma() {
			return getRuleContext(ExprcommaContext.class,0);
		}
		public TerminalNode RCURLY() { return getToken(ActivityParser.RCURLY, 0); }
		public TerminalNode LPAREN() { return getToken(ActivityParser.LPAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ActivityParser.RPAREN, 0); }
		public TerminalNode NOT() { return getToken(ActivityParser.NOT, 0); }
		public TerminalNode AND() { return getToken(ActivityParser.AND, 0); }
		public TerminalNode XOR() { return getToken(ActivityParser.XOR, 0); }
		public TerminalNode OR() { return getToken(ActivityParser.OR, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(108);
				match(ID);
				}
				break;
			case LCURLY:
				{
				setState(109);
				match(LCURLY);
				setState(110);
				exprcomma();
				setState(111);
				match(RCURLY);
				}
				break;
			case LPAREN:
				{
				setState(113);
				match(LPAREN);
				setState(114);
				expr(0);
				setState(115);
				match(RPAREN);
				}
				break;
			case NOT:
				{
				setState(117);
				match(NOT);
				setState(118);
				expr(4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(130);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(121);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(122);
						match(AND);
						setState(123);
						expr(4);
						}
						break;

					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(124);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(125);
						match(XOR);
						setState(126);
						expr(3);
						}
						break;

					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(127);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(128);
						match(OR);
						setState(129);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
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
	public static class ExprcommaContext extends ParserRuleContext {
		public List<ExprconsContext> exprcons() {
			return getRuleContexts(ExprconsContext.class);
		}
		public ExprconsContext exprcons(int i) {
			return getRuleContext(ExprconsContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ActivityParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ActivityParser.COMMA, i);
		}
		public ExprcommaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprcomma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterExprcomma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitExprcomma(this);
		}
	}

	public final ExprcommaContext exprcomma() throws RecognitionException {
		ExprcommaContext _localctx = new ExprcommaContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_exprcomma);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			exprcons();
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(136);
				match(COMMA);
				setState(137);
				exprcons();
				}
				}
				setState(142);
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
	public static class ExprconsContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(ActivityParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(ActivityParser.ID, i);
		}
		public List<TerminalNode> CONS() { return getTokens(ActivityParser.CONS); }
		public TerminalNode CONS(int i) {
			return getToken(ActivityParser.CONS, i);
		}
		public ExprconsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprcons; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).enterExprcons(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ActivityParserListener ) ((ActivityParserListener)listener).exitExprcons(this);
		}
	}

	public final ExprconsContext exprcons() throws RecognitionException {
		ExprconsContext _localctx = new ExprconsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_exprcons);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(ID);
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CONS) {
				{
				{
				setState(144);
				match(CONS);
				setState(145);
				match(ID);
				}
				}
				setState(150);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);

		case 1:
			return precpred(_ctx, 2);

		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001f\u0098\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u001b\b\u0001\n"+
		"\u0001\f\u0001\u001e\t\u0001\u0001\u0001\u0005\u0001!\b\u0001\n\u0001"+
		"\f\u0001$\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002,\b\u0002\n\u0002\f\u0002/\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u00037\b\u0003\n\u0003\f\u0003:\t\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003A\b\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004I\b"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005]\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005h\b\u0005\u0003\u0005j\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006x\b\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0005\u0006\u0083\b\u0006\n\u0006\f\u0006\u0086"+
		"\t\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u008b\b\u0007"+
		"\n\u0007\f\u0007\u008e\t\u0007\u0001\b\u0001\b\u0001\b\u0005\b\u0093\b"+
		"\b\n\b\f\b\u0096\t\b\u0001\b\u0000\u0001\f\t\u0000\u0002\u0004\u0006\b"+
		"\n\f\u000e\u0010\u0000\u0001\u0001\u0000\u0006\u0007\u00a4\u0000\u0012"+
		"\u0001\u0000\u0000\u0000\u0002\u0016\u0001\u0000\u0000\u0000\u0004\'\u0001"+
		"\u0000\u0000\u0000\u00062\u0001\u0000\u0000\u0000\bH\u0001\u0000\u0000"+
		"\u0000\ni\u0001\u0000\u0000\u0000\fw\u0001\u0000\u0000\u0000\u000e\u0087"+
		"\u0001\u0000\u0000\u0000\u0010\u008f\u0001\u0000\u0000\u0000\u0012\u0013"+
		"\u0005\u000f\u0000\u0000\u0013\u0014\u0003\u0002\u0001\u0000\u0014\u0015"+
		"\u0005\u0000\u0000\u0001\u0015\u0001\u0001\u0000\u0000\u0000\u0016\u0017"+
		"\u0005\n\u0000\u0000\u0017\u001c\u0003\n\u0005\u0000\u0018\u0019\u0005"+
		"\u0007\u0000\u0000\u0019\u001b\u0003\n\u0005\u0000\u001a\u0018\u0001\u0000"+
		"\u0000\u0000\u001b\u001e\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000"+
		"\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\"\u0001\u0000\u0000"+
		"\u0000\u001e\u001c\u0001\u0000\u0000\u0000\u001f!\u0005\u0007\u0000\u0000"+
		" \u001f\u0001\u0000\u0000\u0000!$\u0001\u0000\u0000\u0000\" \u0001\u0000"+
		"\u0000\u0000\"#\u0001\u0000\u0000\u0000#%\u0001\u0000\u0000\u0000$\"\u0001"+
		"\u0000\u0000\u0000%&\u0005\u000b\u0000\u0000&\u0003\u0001\u0000\u0000"+
		"\u0000\'(\u0005\n\u0000\u0000(-\u0003\n\u0005\u0000)*\u0007\u0000\u0000"+
		"\u0000*,\u0003\n\u0005\u0000+)\u0001\u0000\u0000\u0000,/\u0001\u0000\u0000"+
		"\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000.0\u0001\u0000"+
		"\u0000\u0000/-\u0001\u0000\u0000\u000001\u0005\u000b\u0000\u00001\u0005"+
		"\u0001\u0000\u0000\u000023\u0005\n\u0000\u000038\u0003\b\u0004\u00004"+
		"5\u0007\u0000\u0000\u000057\u0003\b\u0004\u000064\u0001\u0000\u0000\u0000"+
		"7:\u0001\u0000\u0000\u000086\u0001\u0000\u0000\u000089\u0001\u0000\u0000"+
		"\u00009@\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000;<\u0007\u0000"+
		"\u0000\u0000<=\u0005\b\u0000\u0000=>\u0005\u0015\u0000\u0000>?\u0005\t"+
		"\u0000\u0000?A\u0003\n\u0005\u0000@;\u0001\u0000\u0000\u0000@A\u0001\u0000"+
		"\u0000\u0000AB\u0001\u0000\u0000\u0000BC\u0005\u000b\u0000\u0000C\u0007"+
		"\u0001\u0000\u0000\u0000DE\u0005\b\u0000\u0000EF\u0003\f\u0006\u0000F"+
		"G\u0005\t\u0000\u0000GI\u0001\u0000\u0000\u0000HD\u0001\u0000\u0000\u0000"+
		"HI\u0001\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000JK\u0003\n\u0005\u0000"+
		"K\t\u0001\u0000\u0000\u0000Lj\u0005\u0019\u0000\u0000MN\u0005\u0011\u0000"+
		"\u0000NO\u0005\b\u0000\u0000OP\u0005\u0018\u0000\u0000PQ\u0005\t\u0000"+
		"\u0000Qj\u0003\u0004\u0002\u0000RS\u0005\u0010\u0000\u0000Sj\u0003\u0006"+
		"\u0003\u0000TU\u0005\u0012\u0000\u0000Uj\u0003\u0004\u0002\u0000VW\u0005"+
		"\u0013\u0000\u0000Wj\u0003\u0004\u0002\u0000X\\\u0005\u0014\u0000\u0000"+
		"YZ\u0005\b\u0000\u0000Z[\u0005\u0018\u0000\u0000[]\u0005\t\u0000\u0000"+
		"\\Y\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000"+
		"\u0000^j\u0003\u0004\u0002\u0000_`\u0005\u0016\u0000\u0000`a\u0005\b\u0000"+
		"\u0000ab\u0003\f\u0006\u0000bc\u0005\t\u0000\u0000cg\u0003\u0004\u0002"+
		"\u0000de\u0005\u0007\u0000\u0000ef\u0005\u0017\u0000\u0000fh\u0003\u0004"+
		"\u0002\u0000gd\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hj\u0001"+
		"\u0000\u0000\u0000iL\u0001\u0000\u0000\u0000iM\u0001\u0000\u0000\u0000"+
		"iR\u0001\u0000\u0000\u0000iT\u0001\u0000\u0000\u0000iV\u0001\u0000\u0000"+
		"\u0000iX\u0001\u0000\u0000\u0000i_\u0001\u0000\u0000\u0000j\u000b\u0001"+
		"\u0000\u0000\u0000kl\u0006\u0006\uffff\uffff\u0000lx\u0005\u0019\u0000"+
		"\u0000mn\u0005\n\u0000\u0000no\u0003\u000e\u0007\u0000op\u0005\u000b\u0000"+
		"\u0000px\u0001\u0000\u0000\u0000qr\u0005\b\u0000\u0000rs\u0003\f\u0006"+
		"\u0000st\u0005\t\u0000\u0000tx\u0001\u0000\u0000\u0000uv\u0005\u0004\u0000"+
		"\u0000vx\u0003\f\u0006\u0004wk\u0001\u0000\u0000\u0000wm\u0001\u0000\u0000"+
		"\u0000wq\u0001\u0000\u0000\u0000wu\u0001\u0000\u0000\u0000x\u0084\u0001"+
		"\u0000\u0000\u0000yz\n\u0003\u0000\u0000z{\u0005\u0001\u0000\u0000{\u0083"+
		"\u0003\f\u0006\u0004|}\n\u0002\u0000\u0000}~\u0005\u0003\u0000\u0000~"+
		"\u0083\u0003\f\u0006\u0003\u007f\u0080\n\u0001\u0000\u0000\u0080\u0081"+
		"\u0005\u0002\u0000\u0000\u0081\u0083\u0003\f\u0006\u0002\u0082y\u0001"+
		"\u0000\u0000\u0000\u0082|\u0001\u0000\u0000\u0000\u0082\u007f\u0001\u0000"+
		"\u0000\u0000\u0083\u0086\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000"+
		"\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\r\u0001\u0000\u0000"+
		"\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0087\u008c\u0003\u0010\b\u0000"+
		"\u0088\u0089\u0005\u0006\u0000\u0000\u0089\u008b\u0003\u0010\b\u0000\u008a"+
		"\u0088\u0001\u0000\u0000\u0000\u008b\u008e\u0001\u0000\u0000\u0000\u008c"+
		"\u008a\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d"+
		"\u000f\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008f"+
		"\u0094\u0005\u0019\u0000\u0000\u0090\u0091\u0005\u000e\u0000\u0000\u0091"+
		"\u0093\u0005\u0019\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0093"+
		"\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0001\u0000\u0000\u0000\u0095\u0011\u0001\u0000\u0000\u0000\u0096"+
		"\u0094\u0001\u0000\u0000\u0000\u000e\u001c\"-8@H\\giw\u0082\u0084\u008c"+
		"\u0094";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}