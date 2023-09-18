package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.Map;
import java.util.function.BiConsumer;

public class ScopeGraph {

  public ScopeNode root;
  public Map<String, Type> typeMap;

  public void resolveAll(BiConsumer<RefNode, DeclNode> onResolve) {
    resolveAllRec(root, onResolve);
  }

  private void resolveAllRec(ScopeNode current, BiConsumer<RefNode, DeclNode> onResolve) {
    for (var ref: current.refList) current.resolve(ref, onResolve);
    current.childList.forEach(child -> resolveAllRec(child, onResolve));
  }

  public void makeInsertDiff(int pos, int len) {
    makeInsertDiffRec(root, pos, len);
  }

  public void makeDeleteDiff(int pos, int len) {
    makeDeleteDiffRec(root, pos, len);
  }

  private void makeInsertDiffRec(ScopeNode curNode, int pos, int len) {
    for (var decl: curNode.declList) makeInsertDiff(decl.decl, pos, len);
    for (var ref: curNode.refList) makeInsertDiff(ref.decl, pos, len);
    for (var child: curNode.getChildren()) makeInsertDiffRec(child, pos, len);
  }

  private void makeInsertDiff(Name name, int pos, int len) {
    if (name.position > pos) name.position += len;
  }

  private void makeDeleteDiffRec(ScopeNode curNode, int pos, int len) {
    for (var decl: curNode.declList) makeDeleteDiff(decl.decl, pos, len);
    for (var ref: curNode.refList) makeDeleteDiff(ref.decl, pos, len);
    curNode.declList.removeIf(it -> it.decl.position < 0);
    curNode.refList.removeIf(it -> it.decl.position < 0);
    for (var child: curNode.getChildren()) makeDeleteDiffRec(child, pos, len);
  }

  private void makeDeleteDiff(Name name, int pos, int len) {
    if (name.position >= pos) {
      if (name.position < pos + len) name.position = -1;
      else name.position -= len;
    }
  }
}
