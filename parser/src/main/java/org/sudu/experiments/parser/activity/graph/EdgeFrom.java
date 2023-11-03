package org.sudu.experiments.parser.activity.graph;

public class EdgeFrom {
    private final Node from;
    public final IExpr expr;
    public final boolean isElseEdge;

    public EdgeFrom(Node stat) {
        this(stat, null, false);
    }

    public static EdgeFrom If(Node stat, IExpr expr) {
        return new EdgeFrom(stat, expr, false);
    }
    public static EdgeFrom Else(Node stat, IExpr expr) {
        return new EdgeFrom(stat, expr, true);
    }

    private EdgeFrom(Node from, IExpr expr, boolean isElseEdge) {
        this.from = from;
        this.expr = expr;
        this.isElseEdge = isElseEdge;
    }

    public Node getFrom() {
        return from;
    }

    public String getLabel() {
        if (expr == null)
            return "";
        else  if (isElseEdge)
            return "else";
        else
            return expr.toString();
    }
}
