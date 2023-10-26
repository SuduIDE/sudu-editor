package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.*;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.SplitRules;

import java.nio.CharBuffer;
import java.util.*;
import java.util.function.Supplier;

public abstract class BaseParser<P extends Parser> {

  protected int fileSourceLength;
  protected Supplier<String> fileSource;
  protected List<Token> allTokens;
  protected int[] tokenTypes;
  protected int[] tokenStyles;
  protected ArrayWriter writer;

  protected CommonTokenStream tokenStream;

  protected ErrorRecognizerListener tokenRecognitionListener;
  protected ErrorRecognizerListener parserRecognitionListener;
  protected SplitRules splitRules;

  protected abstract Lexer initLexer(CharStream stream);
  protected abstract P initParser();
  protected abstract SplitRules initSplitRules();
  protected abstract boolean tokenFilter(Token token);
  protected abstract void highlightTokens();

  static <T> Supplier<T> supplier(T t) { return () -> t; }

  protected void initLexer(String source) {
    initLexerWithStream(supplier(source), source.length(), CharStreams.fromString(source));
  }

  protected void initLexer(char[] source) {
    initLexer(source, 0, source.length);
  }

  protected void initLexer(char[] source, int offset, int length) {
    CodePointCharStream stream = CodePointCharStream.fromBuffer(
            CodePointBuffer.withChars(CharBuffer.wrap(source, offset, length))
    );
    initLexerWithStream(makeString(source, offset, length), length, stream);
  }

  static Supplier<String> makeString(char[] source, int offset, int length) {
    return () -> new String(source, offset, length);
  }

  protected void initLexerWithStream(Supplier<String> source, int sourceLength, CharStream stream) {
    fileSource = source;
    fileSourceLength = sourceLength;
    tokenRecognitionListener = new ErrorRecognizerListener();
    parserRecognitionListener = new ErrorRecognizerListener();

    Lexer lexer = initLexer(stream);
    lexer.addErrorListener(tokenRecognitionListener);

    tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    allTokens = tokenStream.getTokens();
    tokenTypes = new int[allTokens.size()];
    tokenStyles = new int[allTokens.size()];

    this.splitRules = initSplitRules();
  }

  protected boolean tokenErrorOccurred() {
    return tokenRecognitionListener.errorOccurred;
  }

  protected boolean parserErrorOccurred() {
    return parserRecognitionListener.errorOccurred;
  }

  protected void makeErrorToken() {
    Token errorToken = new ErrorToken(fileSource.get());
    allTokens = List.of(errorToken);
    tokenTypes = new int[1];
    tokenStyles = new int[1];
  }

  protected void writeTokens(int N, Map<Integer, List<Token>> tokensByLine) {
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine.getOrDefault(i + 1, Collections.emptyList());
      writer.write(tokensOnLine.size());
      for (var token: tokensOnLine) {
        int start = token.getStartIndex();
        int stop = token.getStopIndex() + 1;
        int ind = token.getTokenIndex();
        int type = token instanceof SplitToken splitToken ? splitToken.getSplitType() : tokenTypes[ind];
        int style = token instanceof SplitToken ? ParserConstants.TokenTypes.DEFAULT : tokenStyles[ind];
        writer.write(start, stop, type, style);
      }
    }
  }

  protected void writeIntervals(List<Interval> intervalList, int intervalStart) {
    for (Interval interval : intervalList) {
      writer.write(interval.start + intervalStart, interval.stop + intervalStart, interval.intervalType);
    }
  }

  protected void writeUsageToDefinitions(Map<Pos, Pos> usageMap) {
    for (var entry: usageMap.entrySet()) {
      var usage = entry.getKey();
      var def = entry.getValue();
      writer.write(usage.line - 1, usage.pos, def.line - 1, def.pos);
    }
  }

  protected Map<Integer, List<Token>> groupTokensByLine(List<Token> allTokens) {
    Map<Integer, List<Token>> lineToTokens = new HashMap<>();
    for (var token : allTokens) {
      for (var splitted : splitToken(token)) {
        int line = splitted.getLine();
        if (!lineToTokens.containsKey(line)) lineToTokens.put(line, new ArrayList<>());
        lineToTokens.get(line).add(splitted);
      }
    }
    return lineToTokens;
  }

  protected List<Token> splitToken(Token token) {
    for (var rule: splitRules.getRules()) {
      if (rule.test(token)) return rule.split(token);
    }
    return Collections.singletonList(token);
  }

  protected Interval defaultInterval() {
    return new Interval(0, fileSourceLength, ParserConstants.IntervalTypes.UNKNOWN);
  }

  protected IntervalNode defaultIntervalNode() {
    return new IntervalNode(defaultInterval());
  }

}
