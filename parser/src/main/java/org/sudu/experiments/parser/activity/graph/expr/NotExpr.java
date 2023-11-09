package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.Path;

public class NotExpr implements IExpr {
    public IExpr innerExpr;
    private String qualifier;

    public NotExpr() {}
    public NotExpr(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        if (qualifier != null)
            return qualifier;
        else if (innerExpr instanceof BinaryExpr) {
            return "!("+innerExpr+")";
        } else
            return "!"+innerExpr;
    }

    @Override
    public boolean check(String[] ids, int from, int to) {
        return !innerExpr.check(ids, from, to);
    }
}
