package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.TriFunction;
import org.sudu.experiments.parser.common.graph.node.decl.CreatorNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.FieldNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScopeNode {

  public ScopeNode parent;
  public List<ScopeNode> childList;

  public List<RefNode> refList;
  public List<DeclNode> declList;
  public List<Type> importTypes;

  public ScopeNode() {
  }

  public ScopeNode(ScopeNode parent) {
    this.parent = parent;
    this.childList = new ArrayList<>();
    this.refList = new ArrayList<>();
    this.declList = new ArrayList<>();
    this.importTypes = new ArrayList<>();
  }

  public ScopeNode getChild(int i) {
    return childList.get(i);
  }

  public DeclNode resolve(RefNode ref, BiConsumer<RefNode, DeclNode> onResolve) {
    DeclNode node = resolveRec(this, ref, onResolve);
    if (node != null) ref.updateType(node.type);
    onResolve.accept(ref, node);
    return node;
  }

  private static DeclNode resolveRec(
      ScopeNode curScope,
      RefNode ref,
      BiConsumer<RefNode, DeclNode> onResolve
  ) {
    if (ref instanceof CreatorCallNode creatorCall) {
      resolveCallTypes(curScope, creatorCall, onResolve);
      return resolveCreatorCall(curScope, creatorCall, onResolve);
    }
    if (ref instanceof MethodCallNode methodCall) {
      resolveCallTypes(curScope, methodCall, onResolve);
      return resolveMethodCall(curScope, methodCall, onResolve);
    }
    if (ref instanceof QualifiedRefNode qualifiedRef) {
      return resolveQualified(curScope, qualifiedRef, onResolve);
    }
    if (ref instanceof FieldRefNode fieldRef) {
      return resolveField(curScope, fieldRef, onResolve);
    }
    for (var decl: curScope.getDeclarations()) {
      if (decl.match(ref)) return decl;
      if (decl instanceof MethodNode methodNode) {
        for (var argNode: methodNode.args)
          if (argNode.match(ref)) return argNode;
      }
    }

    var resolved = resolveImport(curScope, ref, onResolve, ScopeNode::resolveRec);
    if (resolved != null) return resolved;

    return curScope.parent != null
        ? resolveRec(curScope.parent, ref, onResolve)
        : null;
  }

  private static void resolveCallTypes(
      ScopeNode curScope,
      MethodCallNode methodCall,
      BiConsumer<RefNode, DeclNode> onResolve
  ) {
    for (var arg: methodCall.callArgs) {
      if (!(arg instanceof TypeNode)) curScope.resolve(arg, onResolve);
    }
  }

  private static CreatorNode resolveCreatorCall(
      ScopeNode curScope,
      CreatorCallNode creatorCall,
      BiConsumer<RefNode, DeclNode> onResolve
  ) {
    var root = curScope;
    while (root.parent != null) root = root.parent;

    for (var creator: root.getCreatorNodes()) {
      if (creator.matchCreatorCall(creatorCall)) return creator;
    }
    CreatorNode importResolve = resolveImport(curScope, creatorCall, onResolve, ScopeNode::resolveCreatorCall);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveCreatorCall(curScope.parent, creatorCall, onResolve)
        : null;
  }

  private static MethodNode resolveMethodCall(
      ScopeNode curScope,
      MethodCallNode methodCall, BiConsumer<RefNode, DeclNode> onResolve
  ) {
    for (var methodNode: curScope.getMethodNodes()) {
      if (methodNode.matchMethodCall(methodCall)) return methodNode;
    }
    MethodNode importResolve = resolveImport(curScope, methodCall, onResolve, ScopeNode::resolveMethodCall);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveMethodCall(curScope.parent, methodCall, onResolve)
        : null;
  }

  private static FieldNode resolveField(
      ScopeNode curScope,
      FieldRefNode fieldRef, BiConsumer<RefNode, DeclNode> onResolve
  ) {
    for (var fieldNode: curScope.getFieldNodes()) {
      if (fieldNode.matchField(fieldRef)) return fieldNode;
    }
    FieldNode importResolve = resolveImport(curScope, fieldRef, onResolve, ScopeNode::resolveField);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveField(curScope.parent, fieldRef, onResolve)
        : null;
  }

  private static <D extends DeclNode, R extends RefNode> D resolveImport(
      ScopeNode curScope,
      R refNode,
      BiConsumer<RefNode, DeclNode> onResolve,
      TriFunction<ScopeNode, R, BiConsumer<RefNode, DeclNode>, D> resolveFun
  ) {
    for (var importScope: curScope.importTypes) {
      if (importScope.associatedScope == null) continue;
      var importResolve = resolveFun.apply(importScope.associatedScope, refNode, onResolve);
      if (importResolve != null) return importResolve;
    }
    return null;
  }

  private static DeclNode resolveQualified(
      ScopeNode curScope,
      QualifiedRefNode qualifiedRef, BiConsumer<RefNode, DeclNode> onResolve
  ) {
    var begin = curScope.resolve(qualifiedRef.begin, onResolve);
    if (begin == null || begin.type.associatedScope == null) return null;
    return begin.type.associatedScope.resolve(qualifiedRef.cont, onResolve);
  }

  public List<ScopeNode> getChildren() {
    return new ArrayList<>(childList);
  }

  public List<MemberNode> getMembers() {
    return getMembersStream().toList();
  }

  public void print(int depth) {
    System.out.print("—".repeat(depth));
    if (this instanceof FakeNode) System.out.println("F");
    else if (this instanceof MemberNode) System.out.println("M");
    else System.out.println("S");
    for (var child: getChildren()) child.print(depth + 1);
  }

  public Stream<MemberNode> getMembersStream() {
    return childList.stream()
        .filter(it -> it instanceof MemberNode)
        .map(it -> (MemberNode) it);
  }

  private Stream<MemberNode> getMembersStream(Predicate<MemberNode> predicate) {
    return childList.stream()
        .filter(it -> it instanceof MemberNode memberNode && predicate.test(memberNode))
        .map(it -> (MemberNode) it);
  }

  private <R> Stream<R> getMembersStream(Predicate<MemberNode> predicate, Function<MemberNode, R> fun) {
    return childList.stream()
        .filter(it -> it instanceof MemberNode memberNode && predicate.test(memberNode))
        .map(it -> fun.apply((MemberNode) it));
  }

  private Set<DeclNode> getDeclarations() {
    Set<DeclNode> result = new HashSet<>(declList);
    getMembersStream().forEach(member -> result.addAll(member.declList));
    return result;
  }

  private Set<MethodNode> getMethodNodes() {
    return getMembersStream(
        it -> it.member instanceof MethodNode,
        it -> (MethodNode) it.member
    ).collect(Collectors.toSet());
  }

  private Set<CreatorNode> getCreatorNodes() {
    return getMembersStream(
        it -> it.member instanceof CreatorNode,
        it -> (CreatorNode) it.member
    ).collect(Collectors.toSet());
  }

  private Set<FieldNode> getFieldNodes() {
    return getMembersStream(it -> it.member instanceof FieldNode)
        .flatMap(fields -> fields.declList.stream())
        .map(it -> (FieldNode) it)
        .collect(Collectors.toSet());
  }

}
