package org.sudu.experiments.parser.common.stack.graph;

import org.sudu.experiments.parser.common.stack.graph.edge.Edge;
import org.sudu.experiments.parser.common.stack.graph.node.StackNode;

import java.util.*;
import java.util.stream.Collectors;

public class StackGraph {

  List<StackNode> nodes;
  Map<StackNode, List<Edge>> edges;
  Stack<String> symbolStack;
  List<StackNode> path;
  static final boolean printStack = true;

  void lift(StackNode i) {
    symbolStack.clear();
    path.clear();
    if (i.nodeType == StackNode.PUSH_NODE) {
      path.add(i);
      symbolStack.push(i.symbol);
    } else if (i.nodeType == StackNode.SCOPE_NODE || i.nodeType == StackNode.ROOT_NODE) {
      path.add(i);
    }
    resolve();
  }

  void resolve() {
    if (printStack) printStack();
    if (symbolStack.isEmpty()) return;
    var i0 = path.get(0);
    var i1 = path.get(path.size() - 1);
    var x = symbolStack.peek();
    if (!edges.containsKey(i1)) return;
    for (var edge: edges.get(i1)) {
      var i2 = edge.to;
      if (resolve(i0, i1, i2, x)) {
        resolve();
        return;
      }
    }
    System.out.println();
  }

  boolean resolve(StackNode i0, StackNode i1, StackNode i2, String x) {
    if (i2.nodeType == StackNode.PUSH_NODE) {  // PUSH
      path.add(i2);
      symbolStack.push(i2.symbol);
      return true;
    } else if (i2.nodeType == StackNode.POP_NODE && x.equals(i2.symbol)) {   // POP
      path.add(i2);
      symbolStack.pop();
      return true;
    } else if (i1.nodeType == StackNode.ROOT_NODE && i2.nodeType == StackNode.ROOT_NODE && i1 != i2) {  // ROOT
      path.add(i2);
      return true;
    } else if (i2.nodeType == StackNode.ROOT_NODE || i2.nodeType == StackNode.SCOPE_NODE) { // NOOP
      path.add(i2);
      return true;
    }
    return false;
  }

  void printStack() {
    var stackSymbols = new ArrayList<>(symbolStack.stream().filter(Objects::nonNull).toList());
    Collections.reverse(stackSymbols);
    System.out.println(stackSymbols.stream().collect(Collectors.joining("", "<", ">")));
  }

}
