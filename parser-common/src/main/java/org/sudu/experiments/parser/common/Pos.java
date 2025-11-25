package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

public class Pos implements Comparable<Pos> {

  public int line, charPos;

  public Pos() {}

  public Pos(int line, int charPos) {
    set(line, charPos);
  }

  public void set(Pos pos) {
    set(pos.line, pos.charPos);
  }

  public void set(int lineInd, int charPos) {
    this.line = lineInd;
    this.charPos = charPos;
  }

  public static Pos fromNode(TerminalNode node) {
    var token = node.getSymbol();
    return fromToken(token);
  }

  public static Pos fromToken(Token token) {
    return new Pos(token.getLine(), token.getCharPositionInLine());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pos pos1 = (Pos) o;
    return line == pos1.line && charPos == pos1.charPos;
  }

  @Override
  public int hashCode() {
    return Objects.hash(line, charPos);
  }

  @Override
  public String toString() {
    return "(" + line + ", " + charPos + ")";
  }

  @Override
  public int compareTo(Pos o) {
    return line != o.line ? Integer.compare(line, o.line) : Integer.compare(charPos, o.charPos);
  }

}
