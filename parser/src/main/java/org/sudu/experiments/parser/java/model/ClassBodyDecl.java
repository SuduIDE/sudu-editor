package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

public class ClassBodyDecl extends TypedDecl {

  public boolean isStatic;

  public ClassBodyDecl(String name, Pos position, String type, boolean isStatic) {
    super(name, position, type);
    this.isStatic = isStatic;
  }
}
