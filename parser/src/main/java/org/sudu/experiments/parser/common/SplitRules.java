package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SplitRules {

  public abstract List<TokenSplitRule> getRules();

  protected List<Token> splitTokenByLine(Token token) {
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
        result.add(makeSplitToken(token, line, lineNum, start, start + line.length() - 1));

      start += line.length();
    }
    return result;
  }

  protected static Token makeSplitToken(Token token, String text, int line, int start, int stop) {
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

  public static TokenSplitRule makeRule(
      Predicate<Token> predicate,
      Function<Token, List<Token>> function
  ) {
    return new TokenSplitRule() {

      @Override
      public boolean test(Token token) {
        return predicate.test(token);
      }

      @Override
      public List<Token> split(Token token) {
        return function.apply(token);
      }
    };
  }

  public abstract static class TokenSplitRule {

    public abstract boolean test(Token token);

    public abstract List<Token> split(Token token);

  }

}
