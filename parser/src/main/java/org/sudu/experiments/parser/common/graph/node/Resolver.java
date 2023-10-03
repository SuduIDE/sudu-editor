package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.ExprRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.QualifiedRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Resolver {

  public ScopeGraph graph;
  public BiConsumer<RefNode, DeclNode> onResolve;

  public Resolver(ScopeGraph graph, BiConsumer<RefNode, DeclNode> onResolve) {
    this.graph = graph;
    this.onResolve = onResolve;
  }

  public DeclNode resolve(ScopeNode currentNode, RefNode ref) {
    if (ref == null) return null;
    if (ref.refType == RefNode.THIS ||
        ref.refType == RefNode.SUPER
    ) {
      ref.type = getThisType(currentNode);
      return null;
    }

    DeclNode resolved = resolveRec(currentNode, ref);
    if (resolved != null && resolved.type != null) ref.type = resolved.type;
    if (ref.refType != RefNode.TYPE &&
        !(ref instanceof QualifiedRefNode) &&
        !(ref instanceof ExprRefNode)
    ) onResolve.accept(ref, resolved);

    return resolved;
  }

  private DeclNode resolveRec(
      ScopeNode curScope,
      RefNode ref
  ) {
    if (ref instanceof ExprRefNode exprRefNode) {
      exprRefNode.refNodes.forEach(expr -> resolve(curScope, expr));
      return null;
    }
    if (ref instanceof MethodCallNode methodCall) {
      for (var arg: methodCall.callArgs) resolve(curScope, arg);
      return resolveMethodCall(curScope, methodCall);
    }
    if (ref instanceof QualifiedRefNode qualifiedRef) {
      return resolveQualified(curScope, qualifiedRef);
    }
    DeclNode decl = resolveVar(curScope, ref);
    if (decl != null) return decl;

    return curScope.parent != null
        ? resolveRec(curScope.parent, ref)
        : null;
  }

  private DeclNode resolveVar(
      ScopeNode curScope,
      RefNode ref
  ) {
    var resolvedDecl = curScope.declarationWalk(decl -> decl.match(ref, graph.typeMap));
    return resolvedDecl != null
        ? resolvedDecl
        : resolveImport(curScope, ref, this::resolveRec);
  }

  private MethodNode resolveMethodCall(
      ScopeNode curScope,
      MethodCallNode methodCall
  ) {
    var resolvedDecl = curScope.declarationWalk(decl ->
        decl instanceof MethodNode methodNode
            && methodNode.matchMethodCall(methodCall, graph.typeMap)
    );
    if (resolvedDecl != null) return (MethodNode) resolvedDecl;

    MethodNode importResolve = resolveImport(curScope, methodCall, this::resolveMethodCall);
    if (importResolve != null) return importResolve;

    return curScope.parent != null
        ? resolveMethodCall(curScope.parent, methodCall)
        : null;
  }

  private <D extends DeclNode, R extends RefNode> D resolveImport(
      ScopeNode curScope,
      R refNode,
      BiFunction<ScopeNode, R, D> resolveFun
  ) {
    for (var importScope: curScope.importTypes) {
      var assScope = getTypeScope(importScope);
      if (assScope == null) continue;
      var importResolve = resolveFun.apply(assScope, refNode);
      if (importResolve != null) return importResolve;
    }
    return null;
  }

  private DeclNode resolveQualified(
      ScopeNode curScope,
      QualifiedRefNode qualifiedRef
  ) {
    resolve(curScope, qualifiedRef.begin);
    var type = qualifiedRef.begin.type;
    var assScope = getTypeScope(type);
    if (assScope == null) return null;
    return resolve(assScope, qualifiedRef.cont);
  }

  private ScopeNode getTypeScope(String type) {
    if (type == null) return null;
    return getTypeScopeRec(graph.root, type);
  }

  private String getThisType(ScopeNode current) {
    while (current != null) {
      if (current.type != null) return current.type;
      current = current.parent;
    }
    return null;
  }

  private ScopeNode getTypeScopeRec(ScopeNode current, String type) {
    if (current.type != null && current.type.equals(type)) return current;
    for (var child: current.children) {
      var res = getTypeScopeRec(child, type);
      if (res != null) return res;
    }
    return null;
  }

}
