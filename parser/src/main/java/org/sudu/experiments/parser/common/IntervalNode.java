package org.sudu.experiments.parser.common;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntervalNode {

  public Interval interval;
  public IntervalNode parent;
  public List<IntervalNode> children;
  public boolean needReparse = false;

  public IntervalNode(Interval interval, IntervalNode parent) {
    this.interval = interval;
    this.parent = parent;
    this.children = new ArrayList<>();
  }

  public IntervalNode(Interval interval) {
    this(interval, null);
  }

  public void addChild(Interval node) {
    children.add(new IntervalNode(node, this));
  }

  public void addChild(IntervalNode node) {
    node.parent = this;
    children.add(node);
  }

  public IntervalNode getChild(int i) {
    return children.get(i);
  }

  public IntervalNode lastChild() {
    return getChild(children.size() - 1);
  }

  public static IntervalNode getNode(ArrayReader reader) {
    return readNode(reader);
  }

  private static IntervalNode readNode(ArrayReader reader) {
    int start = reader.next(),
        stop = reader.next(),
        type = reader.next();
    int childCount = reader.next();

    Interval interval = new Interval(start, stop, type);
    IntervalNode node = new IntervalNode(interval);
    for (int i = 0; i < childCount; i++) {
      var child = readNode(reader);
      node.addChild(child);
    }
    return node;
  }

  public int[] toInts() {
    ArrayWriter writer = new ArrayWriter();
    writeInts(this, writer);
    return writer.getInts();
  }

  private static void writeInts(IntervalNode node, ArrayWriter writer) {
    Interval interval = node.interval;
    List<IntervalNode> children = node.children;

    writer.write(interval.start, interval.stop, interval.intervalType);
    writer.write(children.size());

    for (var child: children) writeInts(child, writer);
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

  public IntervalNode merge(IntervalNode node) {
    int start = Math.min(getStart(), node.getStart());
    int stop = Math.max(getStop(), node.getStop());

    Interval newInterval = new Interval(start, stop, interval.intervalType);
    IntervalNode newNode = new IntervalNode(newInterval, parent);
    newNode.needReparse = true;
    return newNode;
  }

  @Override
  public String toString() {
    return interval.toString() + ", " + needReparse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IntervalNode that = (IntervalNode) o;
    return Objects.equals(interval, that.interval) && Objects.equals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interval, children);
  }

}