package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.Pos;

public class Field extends ClassBodyDecl {

  public Field(String name, Pos position, boolean isStatic) {
    super(name, position, isStatic);
  }
}
