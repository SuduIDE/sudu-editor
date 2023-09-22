package org.sudu.experiments.parser.cpp.model;

import org.sudu.experiments.parser.common.TypedDecl;

import java.util.ArrayList;
import java.util.List;

public class CppBlock {

  public CppBlock innerBlock;
  public CppBlock subBlock;
  public List<TypedDecl> localVars;
  public List<CppMethod> methods;

  public CppBlock(CppBlock innerBlock) {
    this.innerBlock = innerBlock;
    this.subBlock = null;
    this.localVars = new ArrayList<>();
    this.methods = new ArrayList<>();
  }

  public TypedDecl getLocalDecl(String declName) {
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

  public CppMethod getMethod(String methodName, List<String> argsTypes) {
    for (var method : methods) {
      if (method.match(methodName, argsTypes)) return method;
    }
    if (innerBlock != null) return innerBlock.getMethod(methodName, argsTypes);
    return null;
  }

}
