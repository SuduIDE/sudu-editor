package org.sudu.experiments.parser.activity.graph.expr;

import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.stat.Id;
import org.sudu.experiments.parser.activity.graph.Path;

import java.util.ArrayList;
import java.util.List;

public class ConsExpr implements IExpr {
    public final List<Id> exprs = new ArrayList<>();

    @Override
    public String toString() {
        return String.join("->", exprs.stream().map(Id::toString).toList());
    }

    @Override
    public boolean check(Path path, int from) {
        return checkPos(path, from) >= 0;
    }

    int checkPos(Path path, int from) {
        outer: for (var i= from; i <= path.ids.size() - exprs.size(); i++) {
            for (var j = 0; j < exprs.size(); j++) {
                if (!exprs.get(j).name().equals(path.ids.get(i+j)))
                    continue outer;
            }
            return i + exprs.size();
        }

        return -1;
    }
}
