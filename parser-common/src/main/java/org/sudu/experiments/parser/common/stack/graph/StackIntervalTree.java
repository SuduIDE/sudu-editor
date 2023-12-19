package org.sudu.experiments.parser.common.stack.graph;

import org.sudu.experiments.parser.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StackIntervalTree {

  public final StackIntervalNode root;
  public final StackGraph graph;
  private boolean updateFlag = false;

  private static final Comparator<StackIntervalNode> INTERVAL_NODE_CMP = (a, b) -> compare(a.interval, b.interval);

  private static int compare(Interval a, Interval b) {
    return a.start == b.start ? Integer.compare(b.stop, a.stop) : Integer.compare(a.start, b.start);
  }

  public StackIntervalTree(Interval interval, StackGraph graph) {
    this(new StackIntervalNode(interval), graph);
  }

  public StackIntervalTree(StackIntervalNode node, StackGraph graph) {
    this.root = node;
    this.graph = graph;
  }

  public void replaceInterval(Interval from, StackIntervalNode newTree) {
    var replaceNode = replaceIntervalRec(root, from);
    if (replaceNode == null) return;

    var newNodes = newTree.children;
    var newFromNodes = newTree.scope.inEdges.stream().map(it -> it.from).toList();
    var newOutNodes = newTree.scope.outEdges.stream().map(it -> it.to).toList();


    if (replaceNode == root) {
      this.root.children = newNodes;
      this.root.scope.addInEdges(newFromNodes);
      this.root.scope.addOutEdges(newOutNodes);
      this.root.needReparse = false;
    } else {
      replaceIntervalNode(replaceNode, newNodes);
      replaceScopeNode(replaceNode, newOutNodes);
    }
  }

  private static void replaceIntervalNode(StackIntervalNode replaceNode, List<StackIntervalNode> newNodes) {
    if (!newNodes.isEmpty() && replaceNode.parent != null) {
      var parent = replaceNode.parent;
      newNodes.forEach(it -> it.parent = parent);
      int nodeInd = parent.children.indexOf(replaceNode);
      if (nodeInd == -1) {
        parent.children.addAll(newNodes);
      } else {
        parent.children.remove(nodeInd);
        parent.children.addAll(nodeInd, newNodes);
      }
    }
  }

  public void replaceScopeNode(StackIntervalNode replaceNode, List<StackNode> newScopes) {
    if (replaceNode.scope == null) return;
    var oldFromNodes = replaceNode.scope.inEdges.stream().map(it -> it.from).toList();
    oldFromNodes.forEach(it -> it.addOutEdges(newScopes));
    graph.removeNode(replaceNode.scope);
  }

  private StackIntervalNode replaceIntervalRec(StackIntervalNode curNode, Interval oldNode) {
    if (curNode.interval.bordersEqual(oldNode)) {
      for (var subInterval: curNode.children) {
        var result = replaceIntervalRec(subInterval, oldNode);
        if (result != null) return result;
      }
      return curNode;
    } else {
      for (var subInterval: curNode.children) {
        if (subInterval.containsInterval(oldNode.start, oldNode.stop)) {
          var result = replaceIntervalRec(subInterval, oldNode);
          if (result != null) return result;
        }
      }
      return null;
    }
  }

  public void makeInsertDiff(int start, int size) {
    updateFlag = false;
    makeInsertDiff(root, start, size);
  }

  private void makeInsertDiff(StackIntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;  // Interval stop lies to the left of start of insertion
    if (curNode.getStart() > start) {       // Interval start lies to the right of insertion
      curNode.updateStart(size);
      curNode.updateStop(size);
      for (var subInterval: curNode.children)
        makeInsertDiff(subInterval, start, size);
    } else if (curNode.between(start) || (!updateFlag && curNode.getStop() == start)) {    // Interval contains insertion point
      curNode.updateStop(size);
      if (curNode.getStart() == start && updateFlag) curNode.updateStart(size);

      for (var subInterval: curNode.children) makeInsertDiff(subInterval, start, size);

      if (!updateFlag) {
        curNode.needReparse = true;
        updateFlag = true;
      }
    }
  }

  public void makeDeleteDiff(int start, int size) {
    updateFlag = false;
    makeDeleteDiff(root, start, size);
  }

  private void makeDeleteDiff(StackIntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;            // delete interval lies to the left of current
    else if (curNode.getStart() > start + size) {     // delete interval lies to the right of current
      curNode.updateStart(-size);
      curNode.updateStop(-size);
      for (var subInterval: curNode.children)
        makeDeleteDiff(subInterval, start, size);
      curNode.children = updateChildren(curNode.children);
    } else if (curNode.containsInInterval(start, start + size)) {   // cur node contains in delete interval
      if (curNode == root) {
        curNode.setStart(0);
        curNode.setStop(0);
        if (curNode.scope != null) {
          graph.removeNode(curNode.scope);
          graph.removeUnreachable();
        }
      } else {
        curNode.setStart(-1);
        curNode.setStop(-1);
        if (curNode.scope != null) {
          curNode.scope.inEdges.forEach(graph::removeEdge);
          curNode.scope.outEdges.forEach(graph::removeEdge);
          graph.removeUnreachable();
        }
      }
      curNode.children.clear();
    } else {
      boolean containsStart = curNode.between(start);
      boolean containsEnd = curNode.between(start + size);

      if (containsStart && containsEnd) {
        curNode.updateStop(-size);
      } else if (containsStart) {
        curNode.setStop(start);
      } else if (containsEnd) {
        curNode.setStart(start);
        curNode.updateStop(-size);
      } else return;

      for (var subInterval: curNode.children)
        makeDeleteDiff(subInterval, start, size);

      boolean upd = curNode.children.stream().reduce(false, (acc, it) -> acc || it.needReparse, (b1, b2) -> b1 || b2);
      if (!upd && !updateFlag) {
        curNode.needReparse = true;
      } else updateFlag = true;
      curNode.children = updateChildren(curNode.children);
    }
  }

  private List<StackIntervalNode> updateChildren(List<StackIntervalNode> children) {
    var result = new ArrayList<StackIntervalNode>();
    StackIntervalNode curNode = null;

    children.sort(INTERVAL_NODE_CMP);
    for (var interval: children) {
      if (interval.getStart() == interval.getStop()) continue;
      if (!interval.needReparse) {
        if (curNode != null) {
          result.add(curNode);
          curNode = null;
        }
        result.add(interval);
      } else {
        if (curNode == null) curNode = interval;
        else {
          curNode = curNode.merge(interval);
          if (interval.scope != null) {
            graph.removeNode(interval.scope);
            graph.removeUnreachable();
          }
        }
      }
    }
    if (curNode != null) result.add(curNode);
    return result;
  }

}
