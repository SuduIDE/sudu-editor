// Generated from parser-generator/src/main/resources/grammar/help/StringSplitter.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.help.gen;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class StringSplitter extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRI_QUOTE=1, QUOTE=2, CHARS=3, ESCAPE=4, NEW_LINE=5, ERROR=6;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TRI_QUOTE", "QUOTE", "CHARS", "ESCAPE", "NEW_LINE", "ERROR", "HexDigits", 
			"HexDigit"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRI_QUOTE", "QUOTE", "CHARS", "ESCAPE", "NEW_LINE", "ERROR"
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


	public StringSplitter(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "StringSplitter.g4"; }

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
		"\u0004\u0000\u0006M\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000\u0018\b\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0004\u0002\u001d\b\u0002\u000b\u0002\f\u0002\u001e\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003%\b\u0003\u0001\u0003\u0003"+
		"\u0003(\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0004\u0003-\b\u0003"+
		"\u000b\u0003\f\u0003.\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u00036\b\u0003\u0001\u0004\u0003\u00049\b\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004=\b\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0005\u0006D\b\u0006\n\u0006\f\u0006G\t"+
		"\u0006\u0001\u0006\u0003\u0006J\b\u0006\u0001\u0007\u0001\u0007\u0000"+
		"\u0000\b\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0000\u000f\u0000\u0001\u0000\u0006\u0002\u0000\"\"\'\'\u0004"+
		"\u0000\n\n\r\r\"\"\\\\\b\u0000\"\"\'\'\\\\bbffnnrrtt\u0001\u000003\u0001"+
		"\u000007\u0003\u000009AFafV\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0001\u0017\u0001\u0000\u0000\u0000\u0003\u0019"+
		"\u0001\u0000\u0000\u0000\u0005\u001c\u0001\u0000\u0000\u0000\u00075\u0001"+
		"\u0000\u0000\u0000\t<\u0001\u0000\u0000\u0000\u000b>\u0001\u0000\u0000"+
		"\u0000\r@\u0001\u0000\u0000\u0000\u000fK\u0001\u0000\u0000\u0000\u0011"+
		"\u0012\u0005\"\u0000\u0000\u0012\u0013\u0005\"\u0000\u0000\u0013\u0018"+
		"\u0005\"\u0000\u0000\u0014\u0015\u0005\'\u0000\u0000\u0015\u0016\u0005"+
		"\'\u0000\u0000\u0016\u0018\u0005\'\u0000\u0000\u0017\u0011\u0001\u0000"+
		"\u0000\u0000\u0017\u0014\u0001\u0000\u0000\u0000\u0018\u0002\u0001\u0000"+
		"\u0000\u0000\u0019\u001a\u0007\u0000\u0000\u0000\u001a\u0004\u0001\u0000"+
		"\u0000\u0000\u001b\u001d\b\u0001\u0000\u0000\u001c\u001b\u0001\u0000\u0000"+
		"\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u001c\u0001\u0000\u0000"+
		"\u0000\u001e\u001f\u0001\u0000\u0000\u0000\u001f\u0006\u0001\u0000\u0000"+
		"\u0000 !\u0005\\\u0000\u0000!6\u0007\u0002\u0000\u0000\"\'\u0005\\\u0000"+
		"\u0000#%\u0007\u0003\u0000\u0000$#\u0001\u0000\u0000\u0000$%\u0001\u0000"+
		"\u0000\u0000%&\u0001\u0000\u0000\u0000&(\u0007\u0004\u0000\u0000\'$\u0001"+
		"\u0000\u0000\u0000\'(\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000"+
		")6\u0007\u0004\u0000\u0000*,\u0005\\\u0000\u0000+-\u0005u\u0000\u0000"+
		",+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000"+
		"\u0000./\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000001\u0003\u000f"+
		"\u0007\u000012\u0003\u000f\u0007\u000023\u0003\u000f\u0007\u000034\u0003"+
		"\u000f\u0007\u000046\u0001\u0000\u0000\u00005 \u0001\u0000\u0000\u0000"+
		"5\"\u0001\u0000\u0000\u00005*\u0001\u0000\u0000\u00006\b\u0001\u0000\u0000"+
		"\u000079\u0005\r\u0000\u000087\u0001\u0000\u0000\u000089\u0001\u0000\u0000"+
		"\u00009:\u0001\u0000\u0000\u0000:=\u0005\n\u0000\u0000;=\u0005\r\u0000"+
		"\u0000<8\u0001\u0000\u0000\u0000<;\u0001\u0000\u0000\u0000=\n\u0001\u0000"+
		"\u0000\u0000>?\t\u0000\u0000\u0000?\f\u0001\u0000\u0000\u0000@I\u0003"+
		"\u000f\u0007\u0000AD\u0003\u000f\u0007\u0000BD\u0005_\u0000\u0000CA\u0001"+
		"\u0000\u0000\u0000CB\u0001\u0000\u0000\u0000DG\u0001\u0000\u0000\u0000"+
		"EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000FH\u0001\u0000\u0000"+
		"\u0000GE\u0001\u0000\u0000\u0000HJ\u0003\u000f\u0007\u0000IE\u0001\u0000"+
		"\u0000\u0000IJ\u0001\u0000\u0000\u0000J\u000e\u0001\u0000\u0000\u0000"+
		"KL\u0007\u0005\u0000\u0000L\u0010\u0001\u0000\u0000\u0000\f\u0000\u0017"+
		"\u001e$\'.58<CEI\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}