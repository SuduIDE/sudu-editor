package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.java.ParserConstants;

import java.util.*;

public abstract class BaseJavaParser {

  protected String fileSource;
  protected List<Token> allTokens;
  protected int[] tokenTypes;
  protected int[] tokenStyles;

  protected CommonTokenStream tokenStream;

  protected abstract boolean isMultilineToken(int tokenType);
  protected abstract boolean isComment(int tokenType);
  protected abstract Lexer initLexer(CharStream stream);
  protected abstract boolean tokenFilter(Token token);

  protected void initLexer(String source) {
    this.fileSource = source;
    CharStream stream = CharStreams.fromString(fileSource);
    Lexer lexer = initLexer(stream);

    tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();
    allTokens = tokenStream.getTokens();
    tokenTypes = new int[allTokens.size()];
    tokenStyles = new int[allTokens.size()];
  }

  protected static Token makeToken(Token token, String text, int line, int start, int stop) {
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

  protected Map<Integer, List<Token>> groupTokensByLine(List<Token> allTokens) {
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

  // divide multiline tokens like TEXT_BLOCK or COMMENT
  private List<Token> splitTokensByLines(Token token) {
    if (!isMultilineToken(token.getType())) return Collections.singletonList(token);
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

  protected void highlightTokens() {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
    }
  }

}
