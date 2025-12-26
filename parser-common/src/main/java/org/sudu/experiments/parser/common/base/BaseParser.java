package org.sudu.experiments.parser.common.base;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.*;
import org.sudu.experiments.parser.common.Pair;
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
  protected abstract String language();
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
    lexer.setTokenFactory(new LangTokenFactory(language()));
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

  protected void writeTokens(
      int N,
      List<Token>[] tokensByLine,
      List<Pair<Integer, Token>> tokenInfo
  ) {
    int tokenInd = 0;
    String source = fileSource.get();
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine[i];
      writer.write(tokensOnLine.size());
      for (var token: tokensOnLine) {
        var pair = tokenInfo.get(tokenInd);
        int start = pair.first;
        int stop = start + pair.second.getText().length();
        String substring = source.substring(start, stop);
        String text = token.getText();
        if (!substring.equals(text)) {
          System.out.println();
        }
        int ind = token.getTokenIndex();
        int type = token instanceof SplitToken splitToken ? splitToken.getSplitType() : tokenTypes[ind];
        int style = token instanceof SplitToken ? ParserConstants.TokenTypes.DEFAULT : tokenStyles[ind];
        writer.write(start, stop, type, style);
        tokenInd++;
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
      writer.write(usage.line - 1, usage.charPos, def.line - 1, def.charPos);
    }
  }

  protected List<Token>[] groupTokensByLine(
      List<Token> allTokens,
      List<Pair<Integer, Token>> tokenInfo,  // (start, token)
      int N
  ) {
    String source = fileSource.get();
    List<Token>[] lineToTokens = new List[N];
    int startIndex = 0;
    for (var token: allTokens) {
      for (var split: splitToken(token)) {
        if (split instanceof SplitToken splitToken && splitToken.getSplitType() == Token.EOF
            || (!(split instanceof SplitToken) && split.getType() == Token.EOF)
        ) continue;
        int line = split.getLine() - 1;
        if (line >= lineToTokens.length) lineToTokens = ArrayOp.resizeOrReturn(lineToTokens, line + 1);
        if (lineToTokens[line] == null) lineToTokens[line] = new ArrayList<>();
        int stopIndex = startIndex + split.getText().length();
        String substring = source.substring(startIndex, stopIndex);
        String text = split.getText();
        if (!substring.equals(text)) {
          System.out.println();
        }
        if (filter(split)) {
          lineToTokens[line].add(split);
          tokenInfo.add(Pair.of(startIndex, split));
        }
        startIndex = stopIndex;
      }
    }
    return lineToTokens;
  }

  protected int count(List<Token>[] tokens) {
    int cnt = 0;
    for (var lineTokens: tokens)
      cnt += lineTokens.size();
    return cnt;
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

  protected static boolean filter(Token token) {
    if (!(token instanceof SplitToken) && token.getType() == Token.EOF) return false;
    String text = token.getText();
    return !(text.equals("\n") || text.equals("\r") || text.equals("\r\n"));
  }
}
