package org.sudu.experiments.parser.activity.graph;

public class EdgeFrom {
  private final Node from;
  public final IExpr expr;

  public EdgeFrom(Node from, IExpr expr) {
    this.from = from;
    this.expr = expr;
  }

  public Node getFrom() {
    return from;
  }

  public String getLabel() {
    if (expr == null)
      return "";
    else
      return expr.toString();
  }
}
