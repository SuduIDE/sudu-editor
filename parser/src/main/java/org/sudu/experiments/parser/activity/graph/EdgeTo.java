package org.sudu.experiments.parser.activity.graph;

public class EdgeTo {
    public Node getTo() {
        return to;
    }

    public final Node to;
    public final IExpr expr;


    public EdgeTo(Node to, IExpr expr) {
        this.to = to;
        this.expr = expr;
    }

    public String getLabel() {
        if (expr == null)
            return "";
        else
            return expr.toString();
    }
}
