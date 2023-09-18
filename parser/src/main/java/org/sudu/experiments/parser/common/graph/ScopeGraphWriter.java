package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.IdentityHashMap;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Nodes.*;
import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Decls.*;

public class ScopeGraphWriter {

  public final ScopeGraph graph;
  public int[] typeInts;
  public char[] typeChars;
  private final ArrayWriter writer;

  public IdentityHashMap<Type, Integer> typeIdentityMap;
  public IdentityHashMap<ScopeNode, Integer> scopeIdentityMap;

  private StringBuilder refDeclStringBuilder;
  private int refDeclFrom = 0;

  public ScopeGraphWriter(ScopeGraph graph) {
    this.graph = graph;
    writer = new ArrayWriter();
    refDeclStringBuilder = new StringBuilder();
  }

  public void toInts() {
    writeInts();
    this.typeInts = writer.getInts();
  }

  private void writeInts() {
    putTypes();
    putScopes();

    writeTypes();
    writeScopes();
  }

  private void writeTypes() {
    writer.write(graph.typeMap.size());

    writeTypesNames();
    writeSupertypes();
  }

  private void putTypes() {
    graph.typeMap.values().forEach(this::putType);
  }

  private void putScopes() {
    putScopesRec(graph.root);
  }

  private void putScopesRec(ScopeNode scopeNode) {
    putScope(scopeNode);
    if (scopeNode.childList == null) return;
    scopeNode.childList.forEach(this::putScopesRec);
  }

  // [s_1, e_1, ..., s_n, e_n]
  private void writeTypesNames() {
    StringBuilder sb = new StringBuilder();
    int from = 0;
    for (var type: graph.typeMap.values()) {
      sb.append(type.type);
      writer.write(from, type.type.length());
      if (type.associatedScope != null) writer.write(-1);
      else {
        int typeNum = scopeIdentityMap.get(type.associatedScope);
        writer.write(typeNum);
      }
      from += type.type.length();
    }
    this.typeChars = sb.toString().toCharArray();
  }

  // [sl = t_i.super.size(), t_i.super[0], ..., t_i.super[sl - 1]]
  private void writeSupertypes() {
    for (var type: graph.typeMap.values()) {
      writer.write(type.supertypes.size());
      for (var supertype: type.supertypes) {
        var num = typeIdentityMap.get(supertype);
        writer.write(num);
      }
    }
  }

  private void writeScopes() {
    writeScope(graph.root);
  }

  // fake node -- -1
  // base node -- 0
  // member node -- 1
  // [sc_type, child.size(), decl.size(), ref.size(), imports.size()]
  private void writeScope(ScopeNode scope) {
    putScope(scope);
    if (scope instanceof FakeNode) {
      writer.write(FAKE_NODE);
      return;
    } else if (scope instanceof MemberNode) writer.write(MEMBER_NODE);
    else writer.write(BASE_NODE);

    writeScopeChildren(scope);
    writeDecls(scope);
    writer.write(scope.refList.size());
    writer.write(scope.importTypes.size());
    writer.write();
    scope.childList.forEach(this::writeScope);
  }

  private void writeScopeChildren(ScopeNode scope) {
    writer.write(scope.childList.size());
    scope.childList.forEach(it -> writer.write(scopeIdentityMap.get(it)));
  }

  private void writeDecls(ScopeNode scope) {
    writer.write(scope.declList.size());
    scope.declList.forEach(decl -> {
      String name = decl.decl.name;
      writer.write(refDeclFrom, name.length());
      refDeclStringBuilder.append(decl.decl.name);
    });
  }

  private void writeDeclBase(DeclNode node) {
    String name = node.decl.name;
    int typeNum = typeIdentityMap.get(node.type);
    writer.write(refDeclFrom, name.length());
    writer.write(typeNum);
    refDeclStringBuilder.append(name);
  }

  private void writeArgDecl(ArgNode node) {
    writer.write(ARG_DECL_NODE);
    writeDeclBase(node);
  }

  private void writeCreatorDecl(CreatorNode node) {
    writer.write(CREATOR_DECL_NODE);
  }

  private void writeDecl(DeclNode node) {
    writer.write(BASE_DECL_NODE);
    writeDeclBase(node);
  }

  private void writeFieldDecl(FieldNode node) {
    writer.write(FIELD_DECL_NODE);
  }

  private void writeMethodDecl(MethodNode node) {
    writer.write(METHOD_DECL_NODE);
  }

  private void writeVarNode(VarNode node) {
    writer.write(VAR_DECL_NODE);
    writeDeclBase(node);
  }

  int putType(Type type) {
    if (typeIdentityMap.containsKey(type))
      return typeIdentityMap.get(type);
    else {
      int size = typeIdentityMap.size();
      typeIdentityMap.put(type, size);
      return size;
    }
  }

  int putScope(ScopeNode scopeNode) {
    if (scopeIdentityMap.containsKey(scopeNode))
      return scopeIdentityMap.get(scopeNode);
    else {
      int size = scopeIdentityMap.size();
      scopeIdentityMap.put(scopeNode, size);
      return size;
    }
  }
}
