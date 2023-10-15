package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

public class Name implements Comparable<Name> {

  public String name;
  public int position;

  public Name(String name, int position) {
    this.name = name;
    this.position = position;
  }

  public boolean match(Name another) {
    return name.equals(another.name);
  }

  public static Name fromNode(TerminalNode node, int offset) {
    return fromToken(node.getSymbol(), offset);
  }

  public static Name fromToken(Token token, int offset) {
    return new Name(token.getText(), token.getStartIndex() + offset);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Name name = (Name) o;
    return position == name.position && Objects.equals(this.name, name.name);
  }

  @Override
  public String toString() {
    return name + "(" + position + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, position);
  }

  @Override
  public int compareTo(Name o) {
    return Integer.compare(position, o.position);
  }
}
