package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

public class ClassBodyDecl extends Decl {

  public boolean isStatic;

  public ClassBodyDecl(String name, Pos position, boolean isStatic) {
    super(name, position);
    this.isStatic = isStatic;
  }
}
