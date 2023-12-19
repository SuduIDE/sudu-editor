package org.sudu.experiments.parser.common.stack.graph;

import java.util.*;

public class StackGraph {

  List<StackNode> roots, nodes;
  List<StackEdge> edges;

  public StackGraph() {
    this.roots = new ArrayList<>();
    this.nodes = new ArrayList<>();
    this.edges = new ArrayList<>();
  }

  void makeInsertDiff(int pos, int len) {
    for (var edge: edges) {
      if (edge.symbol != null && edge.symbol.position >= pos) {
        edge.symbol.position += len;
      }
    }
  }

  void makeDeleteDiff(int pos, int len) {
    for (var edge: edges) {
      if (edge.symbol != null && edge.symbol.position >= pos) {
        edge.symbol.position -= len;
      }
    }
  }

  void addNode(StackNode node) {
    nodes.add(node);
    if (node.nodeType == StackNode.ROOT_NODE) roots.add(node);
  }

  void addEdge(StackEdge edge) {
    if (!nodes.contains(edge.from)) addNode(edge.from);
    if (!nodes.contains(edge.to)) addNode(edge.to);
    edge.from.outEdges.add(edge);
    edge.to.inEdges.add(edge);
    edges.add(edge);
  }

  void removeNode(StackNode node) {
    for (var edge: node.outEdges) {
      edge.to.inEdges.remove(edge);
      edges.remove(edge);
    }
    for (var edge: node.inEdges) {
      edge.from.outEdges.remove(edge);
      edges.remove(edge);
    }
    nodes.remove(node);
    roots.remove(node);
  }

  void removeEdge(StackEdge edge) {
    edge.from.outEdges.remove(edge);
    edge.to.inEdges.remove(edge);
    edges.remove(edge);
  }

  void removeUnreachable() {
    Map<StackNode, Boolean> visited = new IdentityHashMap<>();
    for (var root: roots) markUnreachable(root, visited);
    List<StackNode> forRemove = new ArrayList<>();
    for (var node: nodes) {
      if (!visited.containsKey(node)) {
        forRemove.add(node);
      }
    }
    forRemove.forEach(this::removeNode);
  }

  private void markUnreachable(StackNode cur, Map<StackNode, Boolean> visited) {
    visited.put(cur, true);
    for (var out: cur.inEdges) {
      if (visited.containsKey(out.from)) continue;
      markUnreachable(out.from, visited);
    }
  }

}
