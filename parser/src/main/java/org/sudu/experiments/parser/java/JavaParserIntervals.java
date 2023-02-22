package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;

import java.util.*;
import java.util.stream.Collectors;

public class JavaParserIntervals {

  private static final int COMMENT = 11;

  public static int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();
    CharStream stream = CharStreams.fromString(source);

    JavaLexer lexer = new JavaLexer(stream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();
    List<Token> allTokens = tokenStream.getTokens();
    int[] tokenTypes = new int[allTokens.size()];
    int[] tokenStyles = new int[allTokens.size()];

    JavaParser parser = new JavaParser(tokenStream);

    var compUnit = parser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    var classWalker = new ClassWalker();
    walker.walk(classWalker, compUnit);
    System.out.println("Max depth: " + classWalker.maxDepth);
    System.out.println("Avg depth: " + (classWalker.depthSum / classWalker.amount));
    System.out.println("Num of tokens: " + classWalker.amount);

    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, classWalker.dummy);
    walker.walk(javaWalker, compUnit);

    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = COMMENT;
    }

    var result = getInts(allTokens, classWalker.intervals, tokenTypes, tokenStyles);
    System.out.println("Parsing time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  private static int[] getInts(List<Token> allTokens, List<Interval> intervalList, int[] tokenTypes, int[] tokenStyles) {
    int N = allTokens.get(allTokens.size() - 1).getLine();
    int M = 0;
    int K = intervalList.size();
    Map<Integer, List<Token>> tokensByLine = groupTokensByLine(allTokens);
    for (var entry : tokensByLine.entrySet()) {
      var filtered = entry.getValue().stream()
          .filter(JavaParserIntervals::tokenFilter)
          .collect(Collectors.toList());
      entry.setValue(filtered);
      M += filtered.size();
    }

    int[] result = new int[3 + N + 4 * M + 3 * K];
    result[0] = N;
    result[1] = M;
    result[2] = K;
    int ind = 0;
    for (int i = 0; i < N; i++) {
      var tokensOnLine = tokensByLine.getOrDefault(i + 1, Collections.emptyList());
      result[3 + i] = tokensOnLine.size();
      for (var token : tokensOnLine) {
        result[3 + N + 4 * ind] = token.getStartIndex();
        result[3 + N + 4 * ind + 1] = token.getStopIndex() + 1;
        result[3 + N + 4 * ind + 2] = tokenTypes[token.getTokenIndex()];
        result[3 + N + 4 * ind + 3] = tokenStyles[token.getTokenIndex()];
        ind++;
      }
    }
    for (int i = 0; i < K; i++) {
      result[3 + N + 4 * M + 3 * i] = intervalList.get(i).start;
      result[3 + N + 4 * M + 3 * i + 1] = intervalList.get(i).stop;
      result[3 + N + 4 * M + 3 * i + 2] = intervalList.get(i).intervalType;
    }
    return result;
  }

  private static Map<Integer, List<Token>> groupTokensByLine(List<Token> allTokens) {
    Map<Integer, List<Token>> lineToTokens = new HashMap<>();
    for (var token : allTokens) {
      for (var splitted : splitTokensByLines(token)) {
        int line = splitted.getLine();
        if (!lineToTokens.containsKey(line)) lineToTokens.put(line, new ArrayList<>());
        lineToTokens.get(line).add(splitted);
      }
    }
    return lineToTokens;
  }

  private static boolean isComment(int type) {
    return type == JavaLexer.COMMENT || type == JavaLexer.LINE_COMMENT;
  }

  private static boolean isMultiline(int type) {
    return type == JavaLexer.COMMENT || type == JavaLexer.TEXT_BLOCK;
  }

  private static boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  // divide multiline tokens like TEXT_BLOCK or COMMENT
  private static List<Token> splitTokensByLines(Token token) {
    if (!isMultiline(token.getType())) return Collections.singletonList(token);
    List<Token> result = new ArrayList<>();
    String text = token.getText();

    StringTokenizer lineTokenizer = new StringTokenizer(text, "\n\r", true);
    int lineNum = token.getLine();
    int start = token.getStartIndex();
    while (lineTokenizer.hasMoreTokens()) {
      var line = lineTokenizer.nextToken();
      if (line.equals("\n"))
        lineNum++;
      else if (!line.equals("\r"))
        result.add(makeToken(token, line, lineNum, start, start + line.length() - 1));

      start += line.length();
    }
    return result;
  }

  private static Token makeToken(Token token, String text, int line, int start, int stop) {
    return new Token() {
      @Override
      public String getText() {
        return text;
      }

      @Override
      public int getType() {
        return token.getType();
      }

      @Override
      public int getLine() {
        return line;
      }

      @Override
      public int getCharPositionInLine() {
        return token.getCharPositionInLine();
      }

      @Override
      public int getChannel() {
        return token.getChannel();
      }

      @Override
      public int getTokenIndex() {
        return token.getTokenIndex();
      }

      @Override
      public int getStartIndex() {
        return start;
      }

      @Override
      public int getStopIndex() {
        return stop;
      }

      @Override
      public TokenSource getTokenSource() {
        return token.getTokenSource();
      }

      @Override
      public CharStream getInputStream() {
        return token.getInputStream();
      }
    };
  }

}
