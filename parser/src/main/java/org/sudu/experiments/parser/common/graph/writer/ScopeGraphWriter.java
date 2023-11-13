package org.sudu.experiments.parser.common.graph.writer;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.InferenceNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Nodes.*;

public class ScopeGraphWriter {

  public int[] graphInts;
  public char[] graphChars;
  private  final ScopeGraph graph;
  private  final IntervalNode node;
  private final ArrayWriter writer;
  private final DeclNodeWriter declNodeWriter;
  private final RefNodeWriter refNodeWriter;

  private  final HashMap<String, Integer> typeIdentityMap;
  private  final IdentityHashMap<ScopeNode, Integer> scopeIdentityMap;

  private final StringBuilder refDeclStringBuilder;

  public ScopeGraphWriter(
      ScopeGraph graph,
      IntervalNode node
  ) {
    this.graph = graph;
    this.node = node;
    writer = new ArrayWriter();
    refDeclStringBuilder = new StringBuilder();
    typeIdentityMap = new HashMap<>();
    scopeIdentityMap = new IdentityHashMap<>();
    declNodeWriter = new DeclNodeWriter(writer, refDeclStringBuilder, typeIdentityMap);
    refNodeWriter = new RefNodeWriter(writer, refDeclStringBuilder, typeIdentityMap);
  }

  public void toInts() {
    writeInts();
    this.graphInts = writer.getInts();
    this.graphChars = refDeclStringBuilder.toString().toCharArray();
  }

  private void writeInts() {
    putTypes();
    putScopes();

    writeTypes();
    writeScopes();
    writeIntervalNode();
  }

  private void writeTypes() {
    writer.write(graph.typeMap.size());

    writeTypesNames();
    writeSupertypes();
  }

  private void putTypes() {
    graph.typeMap.keySet().forEach(this::putType);
  }

  private void putScopes() {
    if (graph.root == null) return;
    putScopesRec(graph.root);
  }

  private void putScopesRec(ScopeNode scopeNode) {
    putScope(scopeNode);
    if (scopeNode.children == null) return;
    scopeNode.children.forEach(this::putScopesRec);
  }

  // [s_1, e_1, ..., s_n, e_n]
  private void writeTypesNames() {
    for (var type: graph.typeMap.keySet()) {
      writer.write(refDeclStringBuilder.length(), type.length());
      refDeclStringBuilder.append(type);
    }
  }

  // [sl = t_i.super.size(), t_i.super[0], ..., t_i.super[sl - 1]]
  private void writeSupertypes() {
    for (var type: graph.typeMap.keySet()) {
      var supertypes = graph.typeMap.get(type);
      writer.write(supertypes.size());
      for (var supertype: supertypes) {
        var num = typeIdentityMap.get(supertype);
        writer.write(num);
      }
    }
  }

  private void writeScopes() {
    if (graph.root == null) {
      writer.write(-1);
      return;
    }
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
    writeScopeType(scope.type);
    writeScopeChildren(scope.children);
  }

  private void writeImports(List<String> importTypes) {
    writer.write(importTypes.size());
    importTypes.forEach(type -> writer.write(typeIdentityMap.get(type)));
  }

  private void writeScopeChildren(List<ScopeNode> scopes) {
    writer.write(scopes.size());
    scopes.forEach(this::writeScope);
  }

  private void writeInferences(List<InferenceNode> inferences) {
    writer.write(inferences.size());
    for (var infer: inferences) {
      declNodeWriter.writeDeclNode(infer.decl);
      refNodeWriter.writeRefNode(infer.ref);
      writer.write(infer.inferenceType);
    }
  }

  private void writeScopeType(String type) {
    if (type == null) writer.write(-1);
    else writer.write(typeIdentityMap.get(type));
  }

  private void writeIntervalNode() {
    if (node == null) {
      writer.write(-1);
      return;
    }
    writer.write(1);
    IntervalNode.writeInts(node, writer, scopeIdentityMap);
  }

  private int putType(String type) {
    if (typeIdentityMap.containsKey(type))
      return typeIdentityMap.get(type);
    else {
      int size = typeIdentityMap.size();
      typeIdentityMap.put(type, size);
      return size;
    }
  }

  private int putScope(ScopeNode scopeNode) {
    if (scopeIdentityMap.containsKey(scopeNode))
      return scopeIdentityMap.get(scopeNode);
    else {
      int size = scopeIdentityMap.size();
      scopeIdentityMap.put(scopeNode, size);
      return size;
    }
  }
}
