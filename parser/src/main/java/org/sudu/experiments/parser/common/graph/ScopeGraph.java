package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.parser.common.graph.node.Resolver;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ScopeGraph {

  public ScopeNode root;
  public List<Type> types;

  public ScopeGraph() {
    types = new ArrayList<>();
  }

  public ScopeGraph(ScopeNode root, List<Type> types) {
    this.root = root;
    this.types = types;
  }

  public void resolveAll(BiConsumer<RefNode, DeclNode> onResolve) {
    resolveAllRec(root, new Resolver(onResolve));
  }

  private void resolveAllRec(ScopeNode current, Resolver resolver) {
    current.referenceWalk(ref -> resolver.resolve(current, ref));
    current.children.forEach(child -> resolveAllRec(child, resolver));
  }

  public void makeInsertDiff(int pos, int len) {
    makeInsertDiffRec(root, pos, len);
  }

  public void makeDeleteDiff(int pos, int len) {
    makeDeleteDiffRec(root, pos, len);
  }

  private void makeInsertDiffRec(ScopeNode curNode, int pos, int len) {
    for (var decl: curNode.declarations) makeInsertDiff(decl, pos, len);
    for (var ref: curNode.references) makeDiff(this::makeInsertDiff, ref, pos, len);
    for (var infer: curNode.inferences) {
      makeInsertDiff(infer.decl, pos, len);
      makeDiff(this::makeInsertDiff, infer.ref, pos, len);
    }
    for (var child: curNode.children) makeInsertDiffRec(child, pos, len);
  }

  private void makeInsertDiff(DeclNode decl, int pos, int len) {
    makeInsertDiff(decl.decl, pos, len);
  }

  private void makeDiff(
      TriConsumer<Name, Integer, Integer> diffFun,
      RefNode refNode, int pos, int len
  ) {
    if (refNode instanceof ExprRefNode exprRefNode) {
      exprRefNode.refNodes.forEach(expr -> makeDiff(diffFun, expr, pos, len));
    } else {
      if (refNode instanceof MethodCallNode callNode) {
        callNode.callArgs.forEach(args -> makeDiff(diffFun, args, pos, len));
      } else if (refNode instanceof QualifiedRefNode qualifiedRef) {
        makeDiff(diffFun, qualifiedRef.begin, pos, len);
        makeDiff(diffFun, qualifiedRef.cont, pos, len);
        return;
      }
    }
    if (refNode != null && refNode.ref != null) diffFun.accept(refNode.ref, pos, len);
  }

  private void makeInsertDiff(Name name, int pos, int len) {
    if (name.position >= pos) name.position += len;
  }

  private void makeDeleteDiffRec(ScopeNode curNode, int pos, int len) {
    for (var decl: curNode.declarations) makeDeleteDiff(decl.decl, pos, len);
    for (var ref: curNode.references) makeDiff(this::makeDeleteDiff, ref, pos, len);
    for (var infer: curNode.inferences) {
      makeDeleteDiff(infer.decl.decl, pos, len);
      makeDiff(this::makeDeleteDiff, infer.ref, pos, len);
    }
    curNode.declarations.removeIf(it -> it.decl.position < 0);
    curNode.references.removeIf(it -> it.ref != null && it.ref.position < 0);
    for (var child: curNode.children) makeDeleteDiffRec(child, pos, len);
  }

  private void makeDeleteDiff(Name name, int pos, int len) {
    if (name.position >= pos) {
      if (name.position < pos + len) name.position = -1;
      else name.position -= len;
    }
  }
}
