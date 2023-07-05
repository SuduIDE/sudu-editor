package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Decl;
import java.util.ArrayList;
import java.util.List;

public class JavaBlock {

  public JavaBlock innerBlock;
  public JavaBlock subBlock;
  public List<TypedDecl> localVars;

  public JavaBlock(JavaBlock innerBlock) {
    this.innerBlock = innerBlock;
    this.subBlock = null;
    this.localVars = new ArrayList<>();
  }

  public TypedDecl getLocalDecl(String declName) {
    for (var local: localVars) {
      if (local.name.equals(declName)) return local;
    }
    if (innerBlock != null) return innerBlock.getLocalDecl(declName);
    return null;
  }

}
