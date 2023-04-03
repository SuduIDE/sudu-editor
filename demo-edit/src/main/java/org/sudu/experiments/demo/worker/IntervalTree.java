package org.sudu.experiments.demo.worker;

import org.sudu.experiments.parser.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IntervalTree {

  IntervalNode root;

  private static final Comparator<Interval> INTERVAL_CMP = (a, b) -> {
    if (a.start == b.start) {
      return Integer.compare(b.stop, a.stop);
    } else return Integer.compare(a.start, b.start);
  };

  public IntervalTree(List<Interval> intervals) {
    intervals.sort(INTERVAL_CMP);
    for (var inter: intervals) {
      if (root == null) {
        root = new IntervalNode(inter);
        continue;
      }
      addNodeRec(root, new IntervalNode(inter));
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
    makeInsertDiff(root, start, size);
  }

  private void makeInsertDiff(IntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;  // Interval stop lies to the left of start of insertion
    if (curNode.getStart() > start) {       // Interval start lies to the right of insertion
      curNode.updateStart(size);
      curNode.updateStop(size);
      for (var subInterval: curNode.subIntervals)
        makeInsertDiff(subInterval, start, size);
    } else if (curNode.between(start)) {    // Interval contains insertion point
      curNode.updateStop(size);
      curNode.needReparse = true;
      for (var subInterval: curNode.subIntervals)
        makeInsertDiff(subInterval, start, size);
    }
  }

  public void makeDeleteDiff(int start, int size) {
    makeDeleteDiff(root, start, size);
  }

  private void makeDeleteDiff(IntervalNode curNode, int start, int size) {
    if (curNode.getStop() < start) return;
    if (curNode.between(start)) {
      if (start + size < curNode.getStop()) curNode.updateStop(-size);
      else curNode.setStop(start);
      for (var subInterval: curNode.subIntervals)
        makeDeleteDiff(subInterval, start, size);
      curNode.needReparse = true;
      curNode.subIntervals = makeGood(curNode.subIntervals);
    } else if (curNode.containsInInterval(start, start + size)) {
      curNode.setStart(-1);
      curNode.setStop(-1);
      curNode.subIntervals.clear();
    } else if (curNode.between(start + size)) {
      curNode.setStart(start);
      curNode.updateStop(-size);
      for (var subInterval: curNode.subIntervals)
        makeDeleteDiff(subInterval, start, size);
      curNode.needReparse = true;
      curNode.subIntervals = makeGood(curNode.subIntervals);
    } if (curNode.getStart() > start + size) {
      curNode.updateStart(-size);
      curNode.updateStop(-size);
      for (var subInterval: curNode.subIntervals)
        makeDeleteDiff(subInterval, start, size);
      curNode.subIntervals = makeGood(curNode.subIntervals);
    }
  }

  private List<IntervalNode> makeGood(List<IntervalNode> subIntervals) {
    var result = new ArrayList<IntervalNode>();
    IntervalNode curNode = null;

    for (var interval : subIntervals) {
      if (interval.getStart() == interval.getStop()) continue;
      if (!interval.needReparse) {
        if (curNode != null) {
          result.add(curNode);
          curNode = null;
        }
        result.add(interval);
      } else {
        if (curNode == null) {
          curNode = interval;
        } else if (interval.getType() == curNode.getType()) {
          curNode = curNode.merge(interval);
        } else {
          result.add(curNode);
          curNode = interval;
        }
      }
    }
    if (curNode != null) result.add(curNode);
    return result;
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
    }
  }

  public void printIntervals(String source) {
    printIntervals(source, root);
    printStructure(source);
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
    int st = curNode.interval.start;
    int end = Math.min(curNode.interval.start + 20, curNode.getStop());
    String intervalStart = source.substring(st, end).replace("\n", "");
    System.out.println(depth + " " + curNode + "\t" + intervalStart);
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
      return getStart() <= p && p <= getStop();
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

