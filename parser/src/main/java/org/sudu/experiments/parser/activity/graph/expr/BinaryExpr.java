package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.Path;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpr implements IExpr {
    public final ExprKind kind;
    private final List<IExpr> list = new ArrayList<>();

    public BinaryExpr(ExprKind kind) {
        this.kind = kind;
    }

    public List<IExpr> list() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder acc = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            if (list.get(i) instanceof BinaryExpr inner && inner.kind.ordinal() > this.kind.ordinal()) {
                acc.append("(");
                acc.append(list.get(i));
                acc.append(")");
            } else {
                acc.append(list.get(i));
            }

            if (i != list.size() - 1) {
                acc.append(" ");
                acc.append(this.kind.op);
                acc.append(" ");
            }
        }

        return acc.toString();
    }

    @Override
    public boolean check(String[] ids, int from, int to) {
        switch (kind) {
            case And -> {
                for (var expr: list) {
                    if (!expr.check(ids, from, to))
                        return false;
                }
                return true;
            }
            case Xor -> {
                var res = false;
                for (var expr: list) {
                    res ^= !expr.check(ids, from, to);
                }
                return res;
            }
            case Or -> {
                for (var expr: list) {
                    if (expr.check(ids, from, to))
                        return true;
                }
                return false;
            }
            default -> {
                return true;
            }
        }
    }
}
