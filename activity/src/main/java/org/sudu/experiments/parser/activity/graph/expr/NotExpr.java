package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;

public class NotExpr implements IExpr {
  public IExpr innerExpr;
  private final String qualifier;

  public NotExpr(String qualifier) {
    this.qualifier = qualifier;
  }

  public NotExpr(String qualifier, IExpr expr) {
    this.qualifier = qualifier;
    this.innerExpr = expr;
  }

  @Override
  public String toString() {
    if (qualifier != null)
      return qualifier;
    else if (innerExpr instanceof BinaryExpr) {
      return "!(" + innerExpr + ")";
    } else
      return "!" + innerExpr;
  }

  @Override
  public boolean check(String[] ids, int from, int to) {
    return !innerExpr.check(ids, from, to);
  }
}
