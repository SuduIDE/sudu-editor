package org.sudu.experiments.parser.common.graph;

import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.InferenceNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.*;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

import java.util.*;

public class ScopeWalker {

  public ScopeNode currentScope;
  public IntervalNode currentNode;
  public ScopeGraph graph;
  public int newIntervalStart = 0;
  public int offset = 0;

  public ScopeWalker(IntervalNode node) {
    graph = new ScopeGraph();
    graph.root = currentScope = new ScopeNode(null);
    currentNode = node;
    node.scope = currentScope;
  }

  public void enterScope() {
    ScopeNode newNode = new ScopeNode(currentScope);
    currentScope.children.add(newNode);
    currentScope = newNode;
  }

  public void enterMember(DeclNode node) {
    MemberNode newNode = new MemberNode(currentScope, node);
    currentScope.children.add(newNode);
    currentScope = newNode;
  }

  public <D extends DeclNode> void enterMember(List<D> nodes) {
    MemberNode newNode = new MemberNode(currentScope, nodes);
    currentScope.children.add(newNode);
    currentScope = newNode;
  }

  public void enterFakeScope() {
    FakeNode fakeNode = new FakeNode(currentScope);
    currentScope.children.add(fakeNode);
    currentScope = fakeNode;
  }

  public void exitMember() {
    exitScope();
  }

  public void exitFakeScope() {
    exitScope();
  }

  public void exitScope() {
    currentScope = currentScope.parent;
  }

  public String getType(String type) {
    if (type == null || type.isBlank()) return null;
    graph.typeMap.putIfAbsent(type, new ArrayList<>());
    return type;
  }

  public String associateType(String type, ScopeNode scopeNode) {
    if (type == null || type.isBlank()) return null;
    graph.typeMap.putIfAbsent(type, new ArrayList<>());
    scopeNode.type = type;
    return type;
  }

  public void addSupertype(String type, String supertype) {
    graph.typeMap.get(type).add(supertype);
  }

  public void addSupertypes(String type, List<String> supertypes) {
    graph.typeMap.get(type).addAll(supertypes);
  }

  public void addInterval(ParserRuleContext ctx, int intervalType) {
    addInterval(ctx, intervalType, currentScope);
  }

  public void addInterval(ParserRuleContext ctx, int intervalType, ScopeNode scopeNode) {
    int end = ctx.stop.getStopIndex() + 1;
    Interval child = new Interval(newIntervalStart + offset, end + offset, intervalType);
    currentNode.addChild(child, scopeNode);
    newIntervalStart = end;
  }

  public void enterInterval() {
    currentNode = currentNode.lastChild();
  }

  public void exitInterval() {
    currentNode = currentNode.parent;
  }

  public void addDecl(DeclNode node) {
    if (node == null) return;
    currentScope.declarations.add(node);
  }

  public void addDecls(List<DeclNode> nodes) {
    currentScope.declarations.addAll(nodes);
  }

  public void addRef(RefNode ref) {
    if (ref == null) return;
    currentScope.references.add(ref);
  }

  public void addRefs(List<RefNode> refs) {
    refs.forEach(this::addRef);
  }

  public void addInference(InferenceNode inference) {
    currentScope.inferences.add(inference);
  }

}
