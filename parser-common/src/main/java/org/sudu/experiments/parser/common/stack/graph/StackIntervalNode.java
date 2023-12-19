package org.sudu.experiments.parser.common.stack.graph;

import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.tree.IntervalNode;

import java.util.ArrayList;
import java.util.List;

public class StackIntervalNode {

  public Interval interval;
  public StackIntervalNode parent;
  public List<StackIntervalNode> children;
  public StackNode scope;

  public boolean needReparse = false;

  public StackIntervalNode(Interval interval, StackIntervalNode parent, StackNode scope) {
    this.interval = interval;
    this.parent = parent;
    this.children = new ArrayList<>();
    this.scope = scope;
  }

  public StackIntervalNode(Interval interval, StackNode scope) {
    this(interval, null, scope);
  }

  public StackIntervalNode(Interval interval) {
    this(interval, null);
  }

  public StackIntervalNode merge(StackIntervalNode node) {
    int newStart = Math.min(getStart(), node.getStart());
    int newStop = Math.max(getStop(), node.getStop());
    Interval newInterval = new Interval(newStart, newStop, interval.intervalType);
    StackNode stackNode = new StackNode();
    stackNode.inEdges = new ArrayList<>(scope.inEdges);
    stackNode.inEdges.addAll(node.scope.inEdges);
    stackNode.outEdges = new ArrayList<>(scope.outEdges);
    stackNode.outEdges.addAll(node.scope.outEdges);

    StackIntervalNode newNode = new StackIntervalNode(newInterval, stackNode);
    return new StackIntervalNode(newInterval, parent, scope);
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

  public boolean containsInterval(int start, int stop) {
    return interval.start <= start && stop <= interval.stop;
  }

  public boolean containsInterval(IntervalNode b) {
    return containsInterval(b.interval.start, b.interval.stop);
  }

  public boolean containsInInterval(int start, int stop) {
    return start <= interval.start && interval.stop <= stop;
  }

  public boolean between(int p) {
    return getStart() <= p && p < getStop();
  }

}
