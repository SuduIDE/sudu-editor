package org.sudu.experiments.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class SplitToken implements Token {

  public Token split;
  public int splitType;
  public int startLine, startPos;

  public SplitToken(Token split, int startLine, int startPos, int splitType) {
    this.split = split;
    this.startLine = startLine;
    this.startPos = startPos;
    this.splitType = splitType;
  }

  @Override
  public String getText() {
    return split.getText();
  }

  @Override
  public int getType() {
    return split.getType();
  }

  public int getSplitType() {
    return splitType;
  }

  @Override
  public int getLine() {
    return startLine + split.getLine();
  }

  @Override
  public int getCharPositionInLine() {
    return split.getCharPositionInLine();
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
    return startPos + split.getStartIndex();
  }

  @Override
  public int getStopIndex() {
    return startPos + split.getStopIndex();
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
