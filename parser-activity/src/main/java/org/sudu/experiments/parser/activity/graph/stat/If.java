package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.BaseStat;
import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;
import java.util.List;

public class If extends BaseStat {
    public IExpr cond;
    public final List<IStat> ifBlock = new ArrayList<>();
    public final List<IStat> elseBlock = new ArrayList<>();

    @Override
    public String name() {
        return "if";
    }
}
