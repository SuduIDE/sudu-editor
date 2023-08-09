package org.sudu.experiments.parser.common;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.util.Objects;

public class Decl implements Comparable<Decl> {

  public String name;
  public Pos position;

  public Decl(String name, Pos position) {
    this.name = name;
    this.position = position;
  }

  public static Decl fromNode(TerminalNode node) {
    return new Decl(node.getText(), Pos.fromNode(node));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Decl decl = (Decl) o;
    return Objects.equals(name, decl.name) && Objects.equals(position, decl.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, position);
  }

  @Override
  public String toString() {
    return name + " " + position;
  }

  @Override
  public int compareTo(Decl o) {
    return position.compareTo(o.position);
  }

}
