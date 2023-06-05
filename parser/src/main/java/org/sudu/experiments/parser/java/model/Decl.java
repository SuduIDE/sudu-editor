package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.Pos;

import java.util.Objects;

public class Decl {

  public String name;
  public Pos position;

  public Decl(String name, Pos position) {
    this.name = name;
    this.position = position;
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

}
