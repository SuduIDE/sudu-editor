package org.sudu.experiments.parser.activity.graph;

public class EdgeTo {
    public Node getTo() {
        return to;
    }

    public final Node to;
    public final IExpr expr;
    public final boolean elseBranch;

    public EdgeTo(Node stat) {
        this(stat, null, false);
    }

    public EdgeTo(Node to, IExpr expr, boolean elseBranch) {
        this.to = to;
        this.expr = expr;
        this.elseBranch = elseBranch;
    }

    public String getLabel() {
        if (expr == null)
            return "";
        else  if (elseBranch)
            return "else";
        else
            return expr.toString();
    }
}
