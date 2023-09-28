package org.sudu.experiments.parser.common.graph.writer;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.InferenceNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.IdentityHashMap;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Nodes.*;

public class ScopeGraphWriter {

  public final ScopeGraph graph;
  public int[] ints;
  public char[] chars;
  private final ArrayWriter writer;
  private final DeclNodeWriter declNodeWriter;
  private final RefNodeWriter refNodeWriter;

  public final IdentityHashMap<Type, Integer> typeIdentityMap;
  public final IdentityHashMap<ScopeNode, Integer> scopeIdentityMap;

  private final StringBuilder refDeclStringBuilder;

  public ScopeGraphWriter(ScopeGraph graph) {
    this.graph = graph;
    writer = new ArrayWriter();
    refDeclStringBuilder = new StringBuilder();
    typeIdentityMap = new IdentityHashMap<>();
    scopeIdentityMap = new IdentityHashMap<>();
    declNodeWriter = new DeclNodeWriter(writer, refDeclStringBuilder, typeIdentityMap);
    refNodeWriter = new RefNodeWriter(writer, refDeclStringBuilder, typeIdentityMap);
  }

  public void toInts() {
    writeInts();
    this.ints = writer.getInts();
    this.chars = refDeclStringBuilder.toString().toCharArray();
  }

  private void writeInts() {
    putTypes();
    putScopes();

    writeTypes();
    writeScopes();
    writeAssociatedScopes();
  }

  private void writeTypes() {
    writer.write(graph.types.size());

    writeTypesNames();
    writeSupertypes();
  }

  private void writeAssociatedScopes() {
    for (var entry: typeIdentityMap.entrySet()) {
      Type type = entry.getKey();
      if (type.associatedScope == null) {
        writer.write(-1);
        continue;
      }
      int scopeInd = scopeIdentityMap.get(type.associatedScope);
      writer.write(entry.getValue());
      writer.write(scopeInd);
    }
  }

  private void putTypes() {
    graph.types.forEach(this::putType);
  }

  private void putScopes() {
    putScopesRec(graph.root);
  }

  private void putScopesRec(ScopeNode scopeNode) {
    putScope(scopeNode);
    if (scopeNode.children == null) return;
    scopeNode.children.forEach(this::putScopesRec);
  }

  // [s_1, e_1, ..., s_n, e_n]
  private void writeTypesNames() {
    for (var type: graph.types) {
      writer.write(refDeclStringBuilder.length(), type.type.length());
      refDeclStringBuilder.append(type.type);
    }
  }

  // [sl = t_i.super.size(), t_i.super[0], ..., t_i.super[sl - 1]]
  private void writeSupertypes() {
    for (var type: graph.types) {
      writer.write(type.supertypes.size());
      for (var supertype: type.supertypes) {
        var num = typeIdentityMap.get(supertype);
        writer.write(num);
      }
    }
  }

  private void writeScopes() {
    writer.write(scopeIdentityMap.size());
    writeScope(graph.root);
  }

  // fake node -- -1
  // base node -- 0
  // member node -- 1
  // [sc_ind, sc_type, decls, refs, imports, child_scopes]
  private void writeScope(ScopeNode scope) {
    int scopeInd = scopeIdentityMap.get(scope);
    writer.write(scopeInd);

    if (scope instanceof FakeNode) writer.write(FAKE_NODE);
    else if (scope instanceof MemberNode) writer.write(MEMBER_NODE);
    else writer.write(BASE_NODE);

    declNodeWriter.writeDeclNodes(scope);
    refNodeWriter.writeRefs(scope);
    writeImports(scope.importTypes);
    writeInferences(scope.inferences);
    writeScopeChildren(scope.children);
  }

  private void writeImports(List<Type> importTypes) {
    writer.write(importTypes.size());
    importTypes.forEach(type -> writer.write(typeIdentityMap.get(type)));
  }

  private void writeScopeChildren(List<ScopeNode> scopes) {
    writer.write(scopes.size());
    scopes.forEach(this::writeScope);
  }

  private void writeInferences(List<InferenceNode> inferences) {
    writer.write(inferences.size());
    for (var infer: inferences){
      declNodeWriter.writeDeclNode(infer.decl);
      refNodeWriter.writeRefNode(infer.ref);
    }
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
