package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

public class Name implements Comparable<Name> {

  public String decl;
  public int position;

  public Name(String decl, int position) {
    this.decl = decl;
    this.position = position;
  }

  public static Name fromNode(TerminalNode node) {
    return new Name(node.getText(), node.getSymbol().getStartIndex());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Name name = (Name) o;
    return position == name.position && Objects.equals(decl, name.decl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decl, position);
  }

  @Override
  public int compareTo(Name o) {
    return Integer.compare(position, o.position);
  }
}
