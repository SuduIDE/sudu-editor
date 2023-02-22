package org.sudu.experiments.parser;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class Interval {

  public int start, stop;
  public int intervalType;

  public Interval(int start, int stop, int intervalType) {
    this.start = start;
    this.stop = stop;
    this.intervalType = intervalType;
  }

  public Interval(ParserRuleContext ruleContext, int intervalType) {
    this(ruleContext.start.getStartIndex(), ruleContext.stop.getStopIndex() + 1, intervalType);
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
}
