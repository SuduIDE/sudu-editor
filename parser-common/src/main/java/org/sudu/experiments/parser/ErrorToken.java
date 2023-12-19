package org.sudu.experiments.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class ErrorToken implements Token {

  public static int ERROR_TYPE = -2;

  String text;

  public ErrorToken(String text) {
    this.text = text;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public int getType() {
    return ERROR_TYPE;
  }

  @Override
  public int getLine() {
    return 1;
  }

  @Override
  public int getCharPositionInLine() {
    return 0;
  }

  @Override
  public int getChannel() {
    return 0;
  }

  @Override
  public int getTokenIndex() {
    return 0;
  }

  @Override
  public int getStartIndex() {
    return 0;
  }

  @Override
  public int getStopIndex() {
    return text.length();
  }

  @Override
  public TokenSource getTokenSource() {
    return null;
  }

  @Override
  public CharStream getInputStream() {
    return null;
  }
}
