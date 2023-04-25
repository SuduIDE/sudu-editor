package org.sudu.experiments.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Objects;

public class Interval implements Comparable<Interval> {

  public int start, stop;
  public int intervalType;

  public Interval(int start, int stop, int intervalType) {
    this.start = start;
    this.stop = stop;
    this.intervalType = intervalType;
  }

  public Interval(ParserRuleContext ruleContext, int intervalType) {
    if (ruleContext.start == null || ruleContext.stop == null) {
      this.start = -1;
      this.stop = -1;
      this.intervalType = -1;
    } else {
      this.start = ruleContext.start.getStartIndex();
      this.stop = ruleContext.stop.getStopIndex() + 1;
      this.intervalType = intervalType;
    }
  }

  public boolean bordersEqual(Interval b) {
    return this.start == b.start
        && this.stop == b.stop;
  }

  public boolean containsIn(int from, int to) {
    return from <= this.start && to >= this.stop;
  }

  public boolean contains(int from, int to) {
    return from >= this.start && to <= this.stop;
  }

  public boolean contains(Token token) {
    return contains(token.getStartIndex(), token.getStopIndex());
  }

  public boolean intersect(int from, int to) {
    return (this.start <= from && from <= this.stop)
        || (this.start <= to && to <= this.stop);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Interval interval = (Interval) o;
    return start == interval.start && stop == interval.stop && intervalType == interval.intervalType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, stop, intervalType);
  }

  @Override
  public String toString() {
    return "(" + start +
        ", " + stop +
        ", " + intervalType +
        ")";
  }

  @Override
  public int compareTo(Interval o) {
    int leftCmp = Integer.compare(start, o.start);
    if (leftCmp != 0) return leftCmp;
    else return Integer.compare(o.stop, stop);
  }
}
