package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Pos;

import java.util.List;

public class JavaConstructor extends JavaMethod {

  public JavaConstructor(String name, Pos position, List<String> argsTypes) {
    super(name, position, name, false, argsTypes);
  }
}
