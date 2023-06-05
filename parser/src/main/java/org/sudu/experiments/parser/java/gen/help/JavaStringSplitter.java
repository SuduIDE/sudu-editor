// Generated from parser/src/main/resources/grammar/java/JavaStringSplitter.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.java.gen.help;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JavaStringSplitter extends Lexer {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRI_QUOTE=1, QUOTE=2, CHARS=3, ESCAPE=4, NEW_LINE=5;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TRI_QUOTE", "QUOTE", "CHARS", "ESCAPE", "NEW_LINE", "HexDigits", "HexDigit"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\"\"\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRI_QUOTE", "QUOTE", "CHARS", "ESCAPE", "NEW_LINE"
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


	public JavaStringSplitter(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JavaStringSplitter.g4"; }

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
		"\u0004\u0000\u0005C\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0004\u0002\u0017\b\u0002\u000b\u0002\f\u0002\u0018\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u001f\b\u0003\u0001\u0003\u0003"+
		"\u0003\"\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0004\u0003\'\b\u0003"+
		"\u000b\u0003\f\u0003(\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u00030\b\u0003\u0001\u0004\u0003\u00043\b\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005:\b"+
		"\u0005\n\u0005\f\u0005=\t\u0005\u0001\u0005\u0003\u0005@\b\u0005\u0001"+
		"\u0006\u0001\u0006\u0000\u0000\u0007\u0001\u0001\u0003\u0002\u0005\u0003"+
		"\u0007\u0004\t\u0005\u000b\u0000\r\u0000\u0001\u0000\u0006\u0002\u0000"+
		"\"\"\'\'\u0004\u0000\n\n\r\r\"\"\\\\\b\u0000\"\"\'\'\\\\bbffnnrrtt\u0001"+
		"\u000003\u0001\u000007\u0003\u000009AFafJ\u0000\u0001\u0001\u0000\u0000"+
		"\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000"+
		"\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000"+
		"\u0001\u000f\u0001\u0000\u0000\u0000\u0003\u0013\u0001\u0000\u0000\u0000"+
		"\u0005\u0016\u0001\u0000\u0000\u0000\u0007/\u0001\u0000\u0000\u0000\t"+
		"2\u0001\u0000\u0000\u0000\u000b6\u0001\u0000\u0000\u0000\rA\u0001\u0000"+
		"\u0000\u0000\u000f\u0010\u0005\"\u0000\u0000\u0010\u0011\u0005\"\u0000"+
		"\u0000\u0011\u0012\u0005\"\u0000\u0000\u0012\u0002\u0001\u0000\u0000\u0000"+
		"\u0013\u0014\u0007\u0000\u0000\u0000\u0014\u0004\u0001\u0000\u0000\u0000"+
		"\u0015\u0017\b\u0001\u0000\u0000\u0016\u0015\u0001\u0000\u0000\u0000\u0017"+
		"\u0018\u0001\u0000\u0000\u0000\u0018\u0016\u0001\u0000\u0000\u0000\u0018"+
		"\u0019\u0001\u0000\u0000\u0000\u0019\u0006\u0001\u0000\u0000\u0000\u001a"+
		"\u001b\u0005\\\u0000\u0000\u001b0\u0007\u0002\u0000\u0000\u001c!\u0005"+
		"\\\u0000\u0000\u001d\u001f\u0007\u0003\u0000\u0000\u001e\u001d\u0001\u0000"+
		"\u0000\u0000\u001e\u001f\u0001\u0000\u0000\u0000\u001f \u0001\u0000\u0000"+
		"\u0000 \"\u0007\u0004\u0000\u0000!\u001e\u0001\u0000\u0000\u0000!\"\u0001"+
		"\u0000\u0000\u0000\"#\u0001\u0000\u0000\u0000#0\u0007\u0004\u0000\u0000"+
		"$&\u0005\\\u0000\u0000%\'\u0005u\u0000\u0000&%\u0001\u0000\u0000\u0000"+
		"\'(\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000()\u0001\u0000\u0000"+
		"\u0000)*\u0001\u0000\u0000\u0000*+\u0003\r\u0006\u0000+,\u0003\r\u0006"+
		"\u0000,-\u0003\r\u0006\u0000-.\u0003\r\u0006\u0000.0\u0001\u0000\u0000"+
		"\u0000/\u001a\u0001\u0000\u0000\u0000/\u001c\u0001\u0000\u0000\u0000/"+
		"$\u0001\u0000\u0000\u00000\b\u0001\u0000\u0000\u000013\u0005\r\u0000\u0000"+
		"21\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u000034\u0001\u0000\u0000"+
		"\u000045\u0005\n\u0000\u00005\n\u0001\u0000\u0000\u00006?\u0003\r\u0006"+
		"\u00007:\u0003\r\u0006\u00008:\u0005_\u0000\u000097\u0001\u0000\u0000"+
		"\u000098\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000"+
		"\u0000\u0000;<\u0001\u0000\u0000\u0000<>\u0001\u0000\u0000\u0000=;\u0001"+
		"\u0000\u0000\u0000>@\u0003\r\u0006\u0000?;\u0001\u0000\u0000\u0000?@\u0001"+
		"\u0000\u0000\u0000@\f\u0001\u0000\u0000\u0000AB\u0007\u0005\u0000\u0000"+
		"B\u000e\u0001\u0000\u0000\u0000\n\u0000\u0018\u001e!(/29;?\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}