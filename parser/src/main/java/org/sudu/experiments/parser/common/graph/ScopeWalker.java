package org.sudu.experiments.parser.common.graph;

import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.*;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.*;

public class ScopeWalker {

  public ScopeNode currentScope;
  public IntervalNode currentNode;
  public Deque<Type> typeStack;
  public ScopeGraph graph;
  public Map<String, Type> typeMap;
  public int newIntervalStart = 0;

  public ScopeWalker(IntervalNode node) {
    graph = new ScopeGraph();
    graph.root = currentScope = new ScopeNode(null);
    currentNode = node;
    typeMap = new HashMap<>();
    typeStack = new LinkedList<>();
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

  public void enterType(Type type) {
    typeStack.addLast(type);
  }

  public void exitType() {
    typeStack.removeLast();
  }

  public Type currentType() {
    return typeStack.getLast();
  }

  public Type getType(String typeString) {
    if (typeString == null || typeString.isBlank()) return null;
    if (!typeMap.containsKey(typeString))
      typeMap.put(typeString, new Type(typeString));
    return typeMap.get(typeString);
  }

  public Type addType(String typeString, ScopeNode scopeNode) {
    if (typeString == null || typeString.isBlank()) return null;
    Type type;
    if (typeMap.containsKey(typeString)) {
      type = typeMap.get(typeString);
      type.associatedScope = scopeNode;
    } else {
      type = new Type(typeString, scopeNode);
      typeMap.put(typeString, type);
    }
    return type;
  }

  public void updateTypes() {
    graph.types = typeMap.values().stream().toList();
  }

  public void addInterval(ParserRuleContext ctx, int intervalType) {
    int end = ctx.stop.getStopIndex() + 1;
    Interval child = new Interval(newIntervalStart, end, intervalType);
    currentNode.addChild(child, currentScope);
    newIntervalStart = end;
  }

  public void enterInterval() {
    currentNode = currentNode.lastChild();
  }

  public void exitInterval() {
    currentNode = currentNode.parent;
  }

  public void addToRoot(DeclNode decl) {
    graph.root.declList.add(decl);
  }

  public void addDecl(DeclNode node) {
    currentScope.declList.add(node);
  }

  public void addDecls(List<DeclNode> nodes) {
    currentScope.declList.addAll(nodes);
  }

  public RefNode addRef(RefNode ref) {
    currentScope.refList.add(ref);
    return ref;
  }

  public List<RefNode> addRefs(List<RefNode> refs) {
    currentScope.refList.addAll(refs);
    return refs;
  }
}
