package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.ExprRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.QualifiedRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class Resolver {

  public ScopeGraph graph;
  public BiConsumer<RefNode, DeclNode> onResolve;
  private final Map<String, DeclNode> typeUsageResolveCache = new HashMap<>();
  private final Map<String, ScopeNode> typeResolveCache = new HashMap<>();

  public Resolver(ScopeGraph graph, BiConsumer<RefNode, DeclNode> onResolve) {
    this.graph = graph;
    this.onResolve = onResolve;
  }

  public DeclNode resolve(ScopeNode currentNode, RefNode ref) {
    if (ref == null
        || ref.refType == RefTypes.LITERAL
        || (ref instanceof MethodCallNode callNode
        && callNode.callType == MethodTypes.ARRAY_CREATOR)
    ) return null;
    if (ref.refType == RefTypes.THIS ||
        ref.refType == RefTypes.SUPER
    ) {
      ref.type = getThisType(currentNode);
      return null;
    }

    DeclNode resolved = resolveRec(currentNode, ref);
    if (resolved != null && resolved.type != null) ref.type = resolved.type;
    if (!(ref instanceof QualifiedRefNode) &&
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
      resolveCallArgs(curScope, methodCall);
      var result = resolveMethodCall(curScope, methodCall);
      /*if (result != null) */return result;
    }
    if (ref instanceof QualifiedRefNode qualifiedRef) {
      return resolveQualified(curScope, qualifiedRef);
    }
    if (ref.refType == RefTypes.TYPE_USAGE) return resolveType(ref);

    DeclNode decl = resolveVar(curScope, ref);
    if (decl != null) return decl;

    if (curScope == null) return null;
    return curScope.parent != null
        ? resolveRec(curScope.parent, ref)
        : null;
  }

  private void resolveCallArgs(ScopeNode curScope, MethodCallNode methodCall) {
    for (var arg: methodCall.callArgs) resolve(curScope, arg);
  }

  private DeclNode resolveVar(
      ScopeNode curScope,
      RefNode ref
  ) {
    if (curScope == null) return null;
    var resolvedDecl = curScope.declarationWalk(decl -> decl.match(ref, graph.typeMap));
    return resolvedDecl != null
        ? resolvedDecl
        : resolveImport(curScope, ref, this::resolveRec);
  }

  private MethodNode resolveMethodCall(
      ScopeNode curScope,
      MethodCallNode methodCall
  ) {
    if (methodCall.callType == MethodTypes.CREATOR) {
      var scope = getTypeScope(methodCall.type);
      if (scope == null) return null;
      return (MethodNode) scope.declarationWalk(decl -> {
        if (!(decl instanceof MethodNode creator)) return false;
        return creator.matchMethodCall(methodCall, graph.typeMap);
      });
    }

    if (curScope == null) return null;
    var resolvedDecl = curScope.declarationWalk(decl -> {
      if (!(decl instanceof MethodNode methodNode)) return false;
      return methodNode.matchMethodCall(methodCall, graph.typeMap);
    });
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
    List<RefNode> flatten = qualifiedRef.flatten();
    resolve(curScope, qualifiedRef.begin);
    var type = qualifiedRef.begin.type;
    var assScope = getTypeScope(type);
    for (int i = 1; i < flatten.size(); i++) {
      RefNode ref = flatten.get(i);
      if (ref instanceof MethodCallNode methodCallNode) {
        resolveCallArgs(curScope, methodCallNode);
        var decl = resolveMethodCall(assScope, methodCallNode);
        onResolve.accept(methodCallNode, decl);
      } else {
        resolve(assScope, ref);
        type = qualifiedRef.begin.type;
        assScope = getTypeScope(type);
      }
    }
    return null;
  }

  private DeclNode resolveType(RefNode ref) {
    if (typeUsageResolveCache.containsKey(ref.ref.name)) {
      return typeUsageResolveCache.get(ref.ref.name);
    } else {
      var result = resolveTypeRec(graph.root, ref);
      typeUsageResolveCache.put(ref.ref.name, result);
      return result;
    }
  }

  private DeclNode resolveTypeRec(ScopeNode current, RefNode ref) {
    DeclNode res = current.declarationWalk(decl -> decl.match(ref, graph.typeMap));
    if (res != null) return res;
    for (var child: current.children) {
      res = resolveTypeRec(child, ref);
      if (res != null) return res;
    }
    return null;
  }

  private ScopeNode getTypeScope(String type) {
    if (type == null || !graph.typeMap.containsKey(type)) return null;
    if (typeResolveCache.containsKey(type)) return typeResolveCache.get(type);
    var scope = getTypeScopeRec(graph.root, type);
    typeResolveCache.put(type, scope);
    return scope;
  }

  private ScopeNode getTypeScopeRec(ScopeNode current, String type) {
    if (current.type != null && current.type.equals(type)) return current;
    for (var child: current.children) {
      var res = getTypeScopeRec(child, type);
      if (res != null) return res;
    }
    return null;
  }

  private String getThisType(ScopeNode current) {
    while (current != null) {
      if (current.type != null) return current.type;
      current = current.parent;
    }
    return null;
  }

}
