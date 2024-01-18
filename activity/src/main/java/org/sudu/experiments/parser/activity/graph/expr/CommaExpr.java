package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;

import java.util.ArrayList;
import java.util.List;

public class CommaExpr implements IExpr {
  public final List<ConsExpr> exprs = new ArrayList<>();

  @Override
  public String toString() {
    if (exprs.size() == 1 && exprs.get(0).exprs.size() == 1)
      return exprs.get(0).toString();
    else
      return "{" + String.join(", ", exprs.stream().map(ConsExpr::toString).toList()) + "}";
  }

  @Override
  public boolean check(String[] ids, int from, int to) {
    return checkPos(ids, from, to) >= 0;
  }

  int checkPos(String[] ids, int from, int to) {
    for (var e: exprs) {
      from = e.checkPos(ids, from, to);
      if (from < 0)
        return -1;
    }
    return from;
  }
}
