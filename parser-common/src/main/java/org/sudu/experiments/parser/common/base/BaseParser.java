package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.*;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.SplitRules;

import java.nio.CharBuffer;
import java.util.*;
import java.util.function.Supplier;

public abstract class BaseParser<P extends Parser> {

  protected static boolean printResult = true;

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
  protected abstract ParserRuleContext getStartRule(P parser);
  protected abstract IntervalNode walk(ParserRuleContext startRule);
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

    Lexer lexer = initLexer(stream);
    lexer.addErrorListener(tokenRecognitionListener);

    tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    allTokens = tokenStream.getTokens();
    tokenTypes = new int[allTokens.size()];
    tokenStyles = new int[allTokens.size()];
    parserRecognitionListener = new ErrorMarkListener(tokenTypes, tokenStyles);

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

  protected void writeTokens(int N, List<Token>[] tokensByLine) {
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine[i];
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

  protected List<Token>[] groupTokensByLine(List<Token> allTokens, int N) {
    List<Token>[] lineToTokens = new List[N];
    for (var token : allTokens) {
      for (var splitted : splitToken(token)) {
        int line = splitted.getLine() - 1;
        if (line >= lineToTokens.length) lineToTokens = ArrayOp.resizeOrReturn(lineToTokens, line + 1);
        if (lineToTokens[line] == null) lineToTokens[line] = new ArrayList<>();
        lineToTokens[line].add(splitted);
      }
    }
    return lineToTokens;
  }

  protected int filter(List<Token>[] tokensByLine) {
    int M = 0;
    int N = tokensByLine.length;
    for (int i = 0; i < N; i++) {
      if (tokensByLine[i] != null) {
        var filtered = tokensByLine[i].stream()
            .filter(this::tokenFilter)
            .toList();
        tokensByLine[i] = filtered;
        M += filtered.size();
      } else tokensByLine[i] = Collections.emptyList();
    }
    return M;
  }

  protected List<Token> splitToken(Token token) {
    for (var rule: splitRules.getRules()) {
      if (rule.test(token)) return rule.split(token);
    }
    return Collections.singletonList(token);
  }

  protected Interval defaultInterval(int type) {
    return new Interval(0, fileSourceLength, type);
  }

  protected Interval defaultInterval() {
    return new Interval(0, fileSourceLength, ParserConstants.IntervalTypes.ERROR_ROOT);
  }

  protected IntervalNode defaultIntervalNode(int type) {
    return new IntervalNode(defaultInterval(type));
  }

  protected IntervalNode defaultIntervalNode() {
    return new IntervalNode(defaultInterval());
  }

  protected IntervalNode getLinesIntervalNode(List<Token>[] tokensByLine) {
    IntervalNode root = defaultIntervalNode();
    int lineStart = 0;
    for (var line: tokensByLine) {
      if (line.isEmpty()) continue;
      int lineStop = line.get(line.size() - 1).getStopIndex() + 1;
      root.addChild(new Interval(lineStart, lineStop, 0));
      lineStart = lineStop;
    }
    if (root.children.isEmpty()) root.addChild(defaultInterval());
    return root;
  }
}
