package org.sudu.experiments.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class SplitToken implements Token {

  public Token split;
  public int splitType;
  public int line;
  public int startIndex, stopIndex;

  public SplitToken(
      Token split,
      int line,
      int startIndex,
      int stopIndex,
      int splitType
  ) {
    this.split = split;
    this.line = line;
    this.startIndex = startIndex;
    this.stopIndex = stopIndex;
    this.splitType = splitType;
  }

  @Override
  public String getText() {
    return split.getText();
  }

  @Override
  public int getType() {
    throw new UnsupportedOperationException();
  }

  public int getSplitType() {
    return splitType;
  }

  @Override
  public int getLine() {
    return line;
  }

  @Override
  public int getCharPositionInLine() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getChannel() {
    return split.getChannel();
  }

  @Override
  public int getTokenIndex() {
    return split.getTokenIndex();
  }

  @Override
  public int getStartIndex() {
    return startIndex;
  }

  @Override
  public int getStopIndex() {
    return stopIndex;
  }

  @Override
  public TokenSource getTokenSource() {
    return split.getTokenSource();
  }

  @Override
  public CharStream getInputStream() {
    return split.getInputStream();
  }
}
