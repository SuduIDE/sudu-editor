package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.stat.Id;

import java.util.ArrayList;
import java.util.List;

public class ConsExpr implements IExpr {
    public final List<Id> exprs = new ArrayList<>();

    @Override
    public String toString() {
        return String.join("->", exprs.stream().map(Id::toString).toList());
    }

    @Override
    public boolean check(String[] ids, int from, int to) {
        return checkPos(ids, from, to) >= 0;
    }

    int checkPos(String[] ids, int from, int to) {
        outer: for (var i= from; i <= to - exprs.size(); i++) {
            for (var j = 0; j < exprs.size(); j++) {
                if (!exprs.get(j).
                    name().equals(ids[i+j]))
                    continue outer;
            }
            return i + exprs.size();
        }

        return -1;
    }
}
