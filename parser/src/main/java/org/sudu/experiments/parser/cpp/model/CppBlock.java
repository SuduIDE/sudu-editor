package org.sudu.experiments.parser.cpp.model;

import org.sudu.experiments.parser.common.Decl;
import java.util.ArrayList;
import java.util.List;

public class CppBlock {

  public CppBlock innerBlock;
  public CppBlock subBlock;
  public List<Decl> localVars;
  public List<CppMethod> methods;

  public CppBlock(CppBlock innerBlock) {
    this.innerBlock = innerBlock;
    this.subBlock = null;
    this.localVars = new ArrayList<>();
    this.methods = new ArrayList<>();
  }

  public Decl getLocalDecl(String declName) {
    for (var local: localVars) {
      if (local.name.equals(declName)) return local;
    }
    if (innerBlock != null) return innerBlock.getLocalDecl(declName);
    return null;
  }

  public CppMethod getMethod(String methodName) {
    for (var local: methods) {
      if (local.name.equals(methodName)) return local;
    }
    if (innerBlock != null) return innerBlock.getMethod(methodName);
    return null;
  }

}
