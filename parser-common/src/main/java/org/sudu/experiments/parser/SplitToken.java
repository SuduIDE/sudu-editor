package org.sudu.experiments.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class SplitToken implements Token {

  public Token split;
  public String text;
  public int tokenType, tokenStyle;
  public int line;

  public SplitToken(Token split, String text, int tokenType, int tokenStyle) {
    this.split = split;
    this.text = text;
    this.tokenType = tokenType;
    this.tokenStyle = tokenStyle;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public int getType() {
    return split.getType();
  }

  @Override
  public int getLine() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getCharPositionInLine() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getChannel() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTokenIndex() {
    return split.getTokenIndex();
  }

  @Override
  public int getStartIndex() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getStopIndex() {
    throw new UnsupportedOperationException();
  }

  @Override
  public TokenSource getTokenSource() {
    return split.getTokenSource();
  }

  @Override
  public CharStream getInputStream() {
    return split.getInputStream();
  }

  public int getTokenType() {
    return tokenType;
  }

  public int getTokenStyle() {
    return tokenStyle;
  }
}
