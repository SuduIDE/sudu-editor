// Generated from parser-generator/src/main/resources/grammar/json/JsonLexer.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.json.gen;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class JsonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, NUMBER=2, BOOLEAN=3, NULL=4, OPEN_BRACKET=5, CLOSE_BRACKET=6, 
		OPEN_BRACE=7, CLOSE_BRACE=8, COMMA=9, COLON=10, COMMENT=11, LINE_COMMENT=12, 
		NEWLINE=13, WS=14, ERROR=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"STRING", "NUMBER", "BOOLEAN", "NULL", "OPEN_BRACKET", "CLOSE_BRACKET", 
			"OPEN_BRACE", "CLOSE_BRACE", "COMMA", "COLON", "COMMENT", "LINE_COMMENT", 
			"NEWLINE", "WS", "ERROR", "ESC", "UNICODE", "HEX", "SAFECODEPOINT", "INT", 
			"EXP"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'null'", "'['", "']'", "'{'", "'}'", "','", 
			"':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "STRING", "NUMBER", "BOOLEAN", "NULL", "OPEN_BRACKET", "CLOSE_BRACKET", 
			"OPEN_BRACE", "CLOSE_BRACE", "COMMA", "COLON", "COMMENT", "LINE_COMMENT", 
			"NEWLINE", "WS", "ERROR"
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


	public JsonLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JsonLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u000f\u00ad\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000/\b\u0000\n\u0000\f\u0000"+
		"2\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0003\u00017\b\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0004\u0001<\b\u0001\u000b\u0001\f\u0001"+
		"=\u0003\u0001@\b\u0001\u0001\u0001\u0003\u0001C\b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002N\b\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0005\ne\b\n\n\n\f\n"+
		"h\t\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0005\u000bs\b\u000b\n\u000b\f\u000bv\t\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0003\f{\b\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\r\u0004\r\u0082\b\r\u000b\r\f\r\u0083\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0003\u000f\u008f\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u009e\b\u0013\n"+
		"\u0013\f\u0013\u00a1\t\u0013\u0003\u0013\u00a3\b\u0013\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u00a7\b\u0014\u0001\u0014\u0004\u0014\u00aa\b\u0014"+
		"\u000b\u0014\f\u0014\u00ab\u0001f\u0000\u0015\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0000!\u0000"+
		"#\u0000%\u0000\'\u0000)\u0000\u0001\u0000\t\u0001\u000009\u0002\u0000"+
		"\n\n\r\r\u0002\u0000\t\t  \b\u0000\"\"//\\\\bbffnnrrtt\u0003\u000009A"+
		"Faf\u0003\u0000\u0000\u001f\"\"\\\\\u0001\u000019\u0002\u0000EEee\u0002"+
		"\u0000++--\u00b6\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0001+\u0001\u0000\u0000\u0000"+
		"\u00036\u0001\u0000\u0000\u0000\u0005M\u0001\u0000\u0000\u0000\u0007O"+
		"\u0001\u0000\u0000\u0000\tT\u0001\u0000\u0000\u0000\u000bV\u0001\u0000"+
		"\u0000\u0000\rX\u0001\u0000\u0000\u0000\u000fZ\u0001\u0000\u0000\u0000"+
		"\u0011\\\u0001\u0000\u0000\u0000\u0013^\u0001\u0000\u0000\u0000\u0015"+
		"`\u0001\u0000\u0000\u0000\u0017n\u0001\u0000\u0000\u0000\u0019z\u0001"+
		"\u0000\u0000\u0000\u001b\u0081\u0001\u0000\u0000\u0000\u001d\u0087\u0001"+
		"\u0000\u0000\u0000\u001f\u008b\u0001\u0000\u0000\u0000!\u0090\u0001\u0000"+
		"\u0000\u0000#\u0096\u0001\u0000\u0000\u0000%\u0098\u0001\u0000\u0000\u0000"+
		"\'\u00a2\u0001\u0000\u0000\u0000)\u00a4\u0001\u0000\u0000\u0000+0\u0005"+
		"\"\u0000\u0000,/\u0003\u001f\u000f\u0000-/\u0003%\u0012\u0000.,\u0001"+
		"\u0000\u0000\u0000.-\u0001\u0000\u0000\u0000/2\u0001\u0000\u0000\u0000"+
		"0.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u000013\u0001\u0000\u0000"+
		"\u000020\u0001\u0000\u0000\u000034\u0005\"\u0000\u00004\u0002\u0001\u0000"+
		"\u0000\u000057\u0005-\u0000\u000065\u0001\u0000\u0000\u000067\u0001\u0000"+
		"\u0000\u000078\u0001\u0000\u0000\u00008?\u0003\'\u0013\u00009;\u0005."+
		"\u0000\u0000:<\u0007\u0000\u0000\u0000;:\u0001\u0000\u0000\u0000<=\u0001"+
		"\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000"+
		">@\u0001\u0000\u0000\u0000?9\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000"+
		"\u0000@B\u0001\u0000\u0000\u0000AC\u0003)\u0014\u0000BA\u0001\u0000\u0000"+
		"\u0000BC\u0001\u0000\u0000\u0000C\u0004\u0001\u0000\u0000\u0000DE\u0005"+
		"t\u0000\u0000EF\u0005r\u0000\u0000FG\u0005u\u0000\u0000GN\u0005e\u0000"+
		"\u0000HI\u0005f\u0000\u0000IJ\u0005a\u0000\u0000JK\u0005l\u0000\u0000"+
		"KL\u0005s\u0000\u0000LN\u0005e\u0000\u0000MD\u0001\u0000\u0000\u0000M"+
		"H\u0001\u0000\u0000\u0000N\u0006\u0001\u0000\u0000\u0000OP\u0005n\u0000"+
		"\u0000PQ\u0005u\u0000\u0000QR\u0005l\u0000\u0000RS\u0005l\u0000\u0000"+
		"S\b\u0001\u0000\u0000\u0000TU\u0005[\u0000\u0000U\n\u0001\u0000\u0000"+
		"\u0000VW\u0005]\u0000\u0000W\f\u0001\u0000\u0000\u0000XY\u0005{\u0000"+
		"\u0000Y\u000e\u0001\u0000\u0000\u0000Z[\u0005}\u0000\u0000[\u0010\u0001"+
		"\u0000\u0000\u0000\\]\u0005,\u0000\u0000]\u0012\u0001\u0000\u0000\u0000"+
		"^_\u0005:\u0000\u0000_\u0014\u0001\u0000\u0000\u0000`a\u0005/\u0000\u0000"+
		"ab\u0005*\u0000\u0000bf\u0001\u0000\u0000\u0000ce\t\u0000\u0000\u0000"+
		"dc\u0001\u0000\u0000\u0000eh\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000"+
		"\u0000fd\u0001\u0000\u0000\u0000gi\u0001\u0000\u0000\u0000hf\u0001\u0000"+
		"\u0000\u0000ij\u0005*\u0000\u0000jk\u0005/\u0000\u0000kl\u0001\u0000\u0000"+
		"\u0000lm\u0006\n\u0000\u0000m\u0016\u0001\u0000\u0000\u0000no\u0005/\u0000"+
		"\u0000op\u0005/\u0000\u0000pt\u0001\u0000\u0000\u0000qs\b\u0001\u0000"+
		"\u0000rq\u0001\u0000\u0000\u0000sv\u0001\u0000\u0000\u0000tr\u0001\u0000"+
		"\u0000\u0000tu\u0001\u0000\u0000\u0000uw\u0001\u0000\u0000\u0000vt\u0001"+
		"\u0000\u0000\u0000wx\u0006\u000b\u0000\u0000x\u0018\u0001\u0000\u0000"+
		"\u0000y{\u0005\r\u0000\u0000zy\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000"+
		"\u0000{|\u0001\u0000\u0000\u0000|}\u0005\n\u0000\u0000}~\u0001\u0000\u0000"+
		"\u0000~\u007f\u0006\f\u0000\u0000\u007f\u001a\u0001\u0000\u0000\u0000"+
		"\u0080\u0082\u0007\u0002\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000"+
		"\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000"+
		"\u0085\u0086\u0006\r\u0000\u0000\u0086\u001c\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\t\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008a"+
		"\u0006\u000e\u0000\u0000\u008a\u001e\u0001\u0000\u0000\u0000\u008b\u008e"+
		"\u0005\\\u0000\u0000\u008c\u008f\u0007\u0003\u0000\u0000\u008d\u008f\u0003"+
		"!\u0010\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008e\u008d\u0001\u0000"+
		"\u0000\u0000\u008f \u0001\u0000\u0000\u0000\u0090\u0091\u0005u\u0000\u0000"+
		"\u0091\u0092\u0003#\u0011\u0000\u0092\u0093\u0003#\u0011\u0000\u0093\u0094"+
		"\u0003#\u0011\u0000\u0094\u0095\u0003#\u0011\u0000\u0095\"\u0001\u0000"+
		"\u0000\u0000\u0096\u0097\u0007\u0004\u0000\u0000\u0097$\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\b\u0005\u0000\u0000\u0099&\u0001\u0000\u0000\u0000"+
		"\u009a\u00a3\u00050\u0000\u0000\u009b\u009f\u0007\u0006\u0000\u0000\u009c"+
		"\u009e\u0007\u0000\u0000\u0000\u009d\u009c\u0001\u0000\u0000\u0000\u009e"+
		"\u00a1\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f"+
		"\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1"+
		"\u009f\u0001\u0000\u0000\u0000\u00a2\u009a\u0001\u0000\u0000\u0000\u00a2"+
		"\u009b\u0001\u0000\u0000\u0000\u00a3(\u0001\u0000\u0000\u0000\u00a4\u00a6"+
		"\u0007\u0007\u0000\u0000\u00a5\u00a7\u0007\b\u0000\u0000\u00a6\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7\u00a9\u0001"+
		"\u0000\u0000\u0000\u00a8\u00aa\u0007\u0000\u0000\u0000\u00a9\u00a8\u0001"+
		"\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac*\u0001\u0000"+
		"\u0000\u0000\u0011\u0000.06=?BMftz\u0083\u008e\u009f\u00a2\u00a6\u00ab"+
		"\u0001\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}