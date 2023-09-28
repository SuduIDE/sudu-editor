package org.sudu.experiments.parser.common.graph.node;

import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.ExprRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.QualifiedRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Resolver {

  public BiConsumer<RefNode, DeclNode> onResolve;

  public Resolver(BiConsumer<RefNode, DeclNode> onResolve) {
    this.onResolve = onResolve;
  }

  public DeclNode resolve(ScopeNode currentNode, RefNode ref) {
    if (ref == null ||
        ref.refType == RefNode.THIS ||
        ref.refType == RefNode.SUPER
    ) return null;

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
    var resolvedDecl = curScope.declarationWalk(decl -> decl.match(ref));
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
            && methodNode.matchMethodCall(methodCall)
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
      if (importScope.associatedScope == null) continue;
      var importResolve = resolveFun.apply(importScope.associatedScope, refNode);
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
    if (type == null || type.associatedScope == null) return null;
    return resolve(type.associatedScope, qualifiedRef.cont);
  }

}
