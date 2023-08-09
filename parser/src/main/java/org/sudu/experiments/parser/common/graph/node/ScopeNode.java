package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.decl.CreatorNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.FieldNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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

  public void resolve(RefNode ref, Consumer<Name> onResolve) {
    DeclNode node = resolve(this, ref);
    onResolve.accept(node.decl);
  }

  public DeclNode resolve(
      ScopeNode refScope,
      RefNode ref
  ) {
    var node = resolveRec(refScope, this, ref);
    if (node != null) ref.updateType(node.type);
    return node;
  }

  private static DeclNode resolveRec(
      ScopeNode refScope,
      ScopeNode curScope,
      RefNode ref
  ) {
    if (ref instanceof CreatorCallNode creatorCall) {
      resolveCallTypes(refScope, curScope, creatorCall);
      return resolveCreatorCall(curScope, creatorCall);
    }
    if (ref instanceof MethodCallNode methodCall) {
      resolveCallTypes(refScope, curScope, methodCall);
      return resolveMethodCall(curScope, methodCall);
    }
    if (ref instanceof QualifiedRefNode qualifiedRef) {
      return resolveQualified(refScope, curScope, qualifiedRef);
    }
    if (ref instanceof FieldRefNode fieldRef) {
      return resolveField(curScope, fieldRef);
    }
    for (var decl: curScope.getDeclarations()) {
      if (decl.match(ref)) return decl;
      if (decl instanceof MethodNode methodNode) {
        for (var argNode: methodNode.args)
          if (argNode.match(ref)) return argNode;
      }
    }

    var resolved = resolveImport(curScope, ref, (_1, _2) -> resolveRec(refScope, _1, _2));
    if (resolved != null) return resolved;

    return curScope.parent != null
        ? resolveRec(refScope, curScope.parent, ref)
        : null;
  }

  private static void resolveCallTypes(
      ScopeNode refScope,
      ScopeNode curScope,
      MethodCallNode methodCall
  ) {
    for (var arg: methodCall.callArgs) {
      if (!(arg instanceof TypeNode)) curScope.resolve(refScope, arg);
    }
  }

  private static CreatorNode resolveCreatorCall(
      ScopeNode curScope,
      CreatorCallNode creatorCall
  ) {
    var root = curScope;
    while (root.parent != null) root = root.parent;

    for (var creator: root.getCreatorNodes()) {
      if (creator.matchCreatorCall(creatorCall)) return creator;
    }
    return null;
  }

  private static MethodNode resolveMethodCall(
      ScopeNode curScope,
      MethodCallNode methodCall
  ) {
    for (var methodNode: curScope.getMethodNodes()) {
      if (methodNode.matchMethodCall(methodCall)) return methodNode;
    }
    MethodNode importResolve = resolveImport(curScope, methodCall, ScopeNode::resolveMethodCall);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveMethodCall(curScope.parent, methodCall)
        : null;
  }

  private static FieldNode resolveField(
      ScopeNode curScope,
      FieldRefNode fieldRef
  ) {
    for (var fieldNode: curScope.getFieldNodes()) {
      if (fieldNode.matchField(fieldRef)) return fieldNode;
    }
    FieldNode importResolve = resolveImport(curScope, fieldRef, ScopeNode::resolveField);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveField(curScope.parent, fieldRef)
        : null;
  }

  private static <D extends DeclNode, R extends RefNode> D resolveImport(
      ScopeNode curScope,
      R refNode,
      BiFunction<ScopeNode, R, D> resolveFun
  ) {
    for (var importScope: curScope.importTypes) {
      if (importScope.associatedScope == null) continue;
      var importResolve = resolveFun.apply(importScope.associatedScope, refNode);
      if (importResolve != null) return importResolve;
    }
    return null;
  }

  private static DeclNode resolveQualified(
      ScopeNode refScope,
      ScopeNode curScope,
      QualifiedRefNode qualifiedRef
  ) {
    var begin = curScope.resolve(refScope, qualifiedRef.begin);
    if (begin == null || begin.type.associatedScope == null) return null;
    return begin.type.associatedScope.resolve(refScope, qualifiedRef.cont);
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
