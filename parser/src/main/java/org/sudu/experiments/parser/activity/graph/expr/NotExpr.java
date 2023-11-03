package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.Path;
import org.sudu.experiments.parser.activity.graph.expr.BinaryExpr;

public class NotExpr implements IExpr {
    public IExpr innerExpr;

    public NotExpr() {
    }

    @Override
    public String toString() {
        if (innerExpr instanceof BinaryExpr) {
            return "!("+innerExpr+")";
        } else
            return "!"+innerExpr;
    }

    @Override
    public boolean check(Path path, int from) {
        return !innerExpr.check(path, from);
    }
}
