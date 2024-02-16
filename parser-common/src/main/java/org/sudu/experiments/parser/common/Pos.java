package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

public class Pos implements Comparable<Pos> {

  public int line, pos;

  public Pos(int line, int pos) {
    this.line = line;
    this.pos = pos;
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
    return line == pos1.line && pos == pos1.pos;
  }

  @Override
  public int hashCode() {
    return Objects.hash(line, pos);
  }

  @Override
  public String toString() {
    return "(" + line + ", " + pos + ")";
  }

  @Override
  public int compareTo(Pos o) {
    return line != o.line ? Integer.compare(line, o.line) : Integer.compare(pos, o.pos);
  }

}
