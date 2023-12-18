package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.reader.ScopeGraphReader;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.function.BiConsumer;

import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class ScopeGraph {

  public ScopeNode root;
  public TypeMap typeMap;
  public static final boolean printResolveInfo = false;
  public static int refs, decls;

  public ScopeGraph() {
    typeMap = new TypeMap();
  }

  public ScopeGraph(ScopeNode root, TypeMap typeMap) {
    this.root = root;
    this.typeMap = typeMap;
  }

  // -1 -- null ref/decl
  // ref |-> decl writes as [1, ref.pos, 1, decl.pos, type, style]
  // ref |-> null  writes as [1, ref.pos, -1]
  public static int[] resolveFromInts(int[] graphInts, char[] graphChars) {
    var reader = new ScopeGraphReader(graphInts, graphChars);
    reader.readFromInts();

    ScopeGraph graph = new ScopeGraph(reader.scopeRoot, reader.typeMap);
    ArrayWriter writer = new ArrayWriter();
    BiConsumer<RefNode, DeclNode> onResolve = (ref, decl) -> {
      if (ref == null || ref.ref == null || (
          ref.refType == RefTypes.TYPE_USAGE && decl == null)
      ) {
        writer.write(-1);
        return;
      }
      writer.write(1, ref.ref.position);
      if (decl == null || decl.decl == null) {
        writer.write(-1);
      } else {
        int type = decl.declType == DeclTypes.FIELD
            ? ParserConstants.TokenTypes.FIELD
            : ParserConstants.TokenTypes.DEFAULT;
        int style = decl instanceof MethodNode
            ? ParserConstants.TokenStyles.BOLD
            : ParserConstants.TokenStyles.NORMAL;
        writer.write(1, decl.decl.position, type, style);
      }
    };
    graph.resolveAll(onResolve);
    return writer.getInts();
  }

  void resolveAll(BiConsumer<RefNode, DeclNode> onResolve) {
    if (root == null) return;
    refs = 0; decls = 0;
    Resolver resolver = new Resolver(this, onResolve);
    resolveAllRec(root, resolver);
    if (printResolveInfo) System.out.println("Resolved " + refs + " refs to " + decls + " decls");
  }

  private void resolveAllRec(ScopeNode current, Resolver resolver) {
    current.referenceWalk(ref -> {
      var decl = resolver.resolve(current, ref);
      if (ref != null) refs++;
      if (decl != null) decls++;
      return decl;
    });
    current.children.forEach(child -> resolveAllRec(child, resolver));
  }

  public void makeInsertDiff(int pos, int len) {
    if (root == null) return;
    makeInsertDiffRec(root, pos, len);
  }

  public void makeDeleteDiff(int pos, int len) {
    if (root == null) return;
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
    curNode.references.removeIf(it -> it == null || (it.ref != null && it.ref.position < 0));
    for (var child: curNode.children) makeDeleteDiffRec(child, pos, len);
  }

  private void makeDeleteDiff(Name name, int pos, int len) {
    if (name.position >= pos) name.position -= len;
  }
}
