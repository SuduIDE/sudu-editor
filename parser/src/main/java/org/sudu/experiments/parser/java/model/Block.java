package org.sudu.experiments.parser.java.model;

import java.util.ArrayList;
import java.util.List;

public class Block {

  public Block innerBlock;
  public Block subBlock;
  public List<Decl> localVars;

  public Block(Block innerBlock) {
    this.innerBlock = innerBlock;
    this.subBlock = null;
    this.localVars = new ArrayList<>();
  }

  public Decl getLocalDecl(String declName) {
    for (var local: localVars) {
      if (local.name.equals(declName)) return local;
    }
    if (innerBlock != null) return innerBlock.getLocalDecl(declName);
    return null;
  }

}
