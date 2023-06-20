package org.sudu.experiments.demo.worker;

import org.sudu.experiments.parser.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IntervalTree {

  IntervalNode root;
  private boolean updateFlag = false;

  private static final Comparator<Interval> INTERVAL_CMP = IntervalTree::compare;

  private static final Comparator<IntervalNode> INTERVAL_NODE_CMP = (a, b) -> compare(a.interval, b.interval);

  private static int compare(Interval a, Interval b) {
    return a.start == b.start
        ? Integer.compare(b.stop, a.stop)
        : Integer.compare(a.start, b.start);
  }

  public IntervalTree(List<Interval> intervals) {
    this(null, intervals);
  }

  private IntervalTree(Interval root, List<Interval> intervals) {
    if (intervals.isEmpty()) return;
    intervals.sort(INTERVAL_CMP);
    if (root != null)
      this.root = new IntervalNode(root);

    for (var inter: intervals) {
      if (this.root == null) {
        this.root = new IntervalNode(inter);
        continue;
      }
      addNodeRec(this.root, new IntervalNode(inter));
    }
  }

  public IntervalNode getReparseNode() {
    return getReparseNode(root);
  }

  private IntervalNode getReparseNode(IntervalNode curNode) {
    if (curNode.needReparse) return curNode;
    for (var subNode : curNode.subIntervals) {
      var result = getReparseNode(subNode);
      if (result != null) return result;
    }
    return null;
  }

  public void replaceInterval(Interval from, List<Interval> to) {
    var subTree = new IntervalTree(from, to);
    var replaceNode = replaceIntervalRec(root, from);
    if (replaceNode == null) return;

    var newNodes = subTree.root.subIntervals;
    if (replaceNode == root) {
      this.root = newNodes.get(0);
      return;
    }
    if (replaceNode.parent != null) {
      newNodes.forEach(it -> it.parent = replaceNode.parent);

      int ind = replaceNode.parent.subIntervals.indexOf(replaceNode);
      replaceNode.parent.subIntervals.remove(ind);
      replaceNode.parent.subIntervals.addAll(ind, newNodes);
    }
  }

  private IntervalNode replaceIntervalRec(IntervalNode curNode, Interval oldNode) {
    if (curNode.interval.bordersEqual(oldNode)) {
      for (var subInterval: curNode.subIntervals) {
        var result = replaceIntervalRec(subInterval, oldNode);
        if (result != null) return result;
      }
      return curNode;
    } else {
      for (var subInterval: curNode.subIntervals) {
        if (subInterval.containsInterval(oldNode.start, oldNode.stop)) {
          var result = replaceIntervalRec(subInterval, oldNode);
          if (result != null) return result;
        }
      }
      return null;
    }
  }

  public List<Interval> toList() {
    List<Interval> result = new ArrayList<>();
    toListRec(root, result);
    return result;
  }

  private void toListRec(IntervalNode curNode, List<Interval> result) {
    result.add(curNode.interval);
    for (var subInterval: curNode.subIntervals)
      toListRec(subInterval, result);
  }

  public void makeInsertDiff(int start, int size) {
    updateFlag = false;
    makeInsertDiff(root, start, size);
  }

  private void makeInsertDiff(IntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;  // Interval stop lies to the left of start of insertion
    if (curNode.getStart() > start) {       // Interval start lies to the right of insertion
      curNode.updateStart(size);
      curNode.updateStop(size);
      for (var subInterval: curNode.subIntervals)
        makeInsertDiff(subInterval, start, size);
    } else if (curNode.between(start) || (!updateFlag && curNode.getStop() == start)) {    // Interval contains insertion point
      curNode.updateStop(size);
      if (curNode.getStart() == start && updateFlag) curNode.updateStart(size);

      for (var subInterval : curNode.subIntervals) makeInsertDiff(subInterval, start, size);

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

  private void makeDeleteDiff(IntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;            // delete interval lies to the left of current
    else if (curNode.getStart() > start + size) {     // delete interval lies to the right of current
      curNode.updateStart(-size);
      curNode.updateStop(-size);
      for (var subInterval : curNode.subIntervals)
        makeDeleteDiff(subInterval, start, size);
      curNode.subIntervals = updateSubIntervals(curNode.subIntervals);
    } else if (curNode.containsInInterval(start, start + size)) {   // cur node contains in delete interval
      if (curNode == root) {
        curNode.setStart(0);
        curNode.setStop(0);
      } else {
        curNode.setStart(-1);
        curNode.setStop(-1);
      }
      curNode.subIntervals.clear();
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

      for (var subInterval : curNode.subIntervals)
        makeDeleteDiff(subInterval, start, size);

      curNode.subIntervals = updateSubIntervals(curNode.subIntervals);
      if (!updateFlag) {
        curNode.needReparse = true;
        updateFlag = true;
      }
    }
  }

  private List<IntervalNode> updateSubIntervals(List<IntervalNode> subIntervals) {
    var result = new ArrayList<IntervalNode>();
    IntervalNode curNode = null;

    subIntervals.sort(INTERVAL_NODE_CMP);
    for (var interval : subIntervals) {
      if (interval.getStart() == interval.getStop()) continue;
      if (!interval.needReparse) {
        if (curNode != null) {
          result.add(curNode);
          curNode = null;
        }
        result.add(interval);
      } else {
        if (curNode == null) curNode = interval;
        else curNode = curNode.merge(interval);
      }
    }
    if (curNode != null) result.add(curNode);
    return result;
  }

  private void alignSubIntervals(List<IntervalNode> subIntervals) {
    for (int i = 1; i < subIntervals.size(); i++) {
      var prev = subIntervals.get(i - 1);
      var cur = subIntervals.get(i);
      cur.setStart(prev.getStop());
    }
  }

  private void addNodeRec(IntervalNode curNode, IntervalNode node) {
    if (curNode.containsInterval(node)) {
      for (var subNode: curNode.subIntervals) {
        if (subNode.containsInterval(node)) {
          addNodeRec(subNode, node);
          return;
        }
      }
      curNode.subIntervals.add(node);
      node.parent = curNode;
    }
  }

  public void printIntervals(String source) {
    printStructure(source);
    System.out.println();
  }

  public void printStructure(String source) {
    printStructure(0, root, source);
  }

  private void printIntervals(String source, IntervalNode curNode) {
    System.out.println("NeedReparse: " + curNode.needReparse);
    System.out.println("Int: " + curNode.interval);
    System.out.println("________");
    System.out.println(source.substring(curNode.getStart(), curNode.getStop()));
    System.out.println("___________________________________");
    for (var subNode: curNode.subIntervals) {
      printIntervals(source, subNode);
    }
  }

  private void printStructure(int depth, IntervalNode curNode, String source) {
    String intervalString;
    if (curNode.getStop() - curNode.getStart() < 43) {
      intervalString = source.substring(curNode.getStart(), curNode.getStop());
    } else {
      intervalString = source.substring(curNode.getStart(), curNode.getStart() + 20) + "..." + source.substring(curNode.getStop() - 20, curNode.getStop());
    }
    intervalString = intervalString.replace("\n", "\\n");
    System.out.println(depth + " " + curNode + "\t" + intervalString);
    for (var subNode: curNode.subIntervals) {
      printStructure(depth + 1, subNode, source);
    }
  }

  public static class IntervalNode {
    Interval interval;
    IntervalNode parent;
    List<IntervalNode> subIntervals;
    boolean needReparse = false;

    public IntervalNode(Interval interval) {
      this.interval = interval;
      this.parent = null;
      subIntervals = new ArrayList<>();
    }

    public boolean intersects(int start, int stop) {
      return (interval.start <= start && start <= interval.stop)
          || (interval.start <= stop && stop <= interval.stop);
    }

    public int getStart() {
      return interval.start;
    }

    public int getStop() {
      return interval.stop;
    }

    public int getType() {
      return interval.intervalType;
    }

    public void setStart(int start) {
      interval.start = start;
    }

    public void setStop(int stop) {
      interval.stop = stop;
    }

    public void updateStart(int dx) {
      interval.start += dx;
    }

    public void updateStop(int dx) {
      interval.stop += dx;
    }

    public boolean between(int p) {
      return getStart() <= p && p < getStop();
    }

    public boolean containsInterval(int start, int stop) {
      return interval.start <= start && stop <= interval.stop;
    }

    public boolean containsInInterval(int start, int stop) {
      return start <= interval.start && interval.stop <= stop;
    }

    public IntervalNode merge(IntervalNode node) {
      int start = Math.min(getStart(), node.getStart());
      int stop = Math.max(getStop(), node.getStop());
      IntervalNode newNode = new IntervalNode(new Interval(start, stop, interval.intervalType));
      newNode.parent = node.parent;
      newNode.needReparse = true;
      return newNode;
    }

    public boolean containsInterval(IntervalNode b) {
      return containsInterval(b.interval.start, b.interval.stop);
    }

    @Override
    public String toString() {
      return interval.toString() + ", " + needReparse;
    }
  }

}

