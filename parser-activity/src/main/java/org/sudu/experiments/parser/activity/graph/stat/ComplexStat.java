package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.BaseStat;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;
import java.util.List;

public abstract class ComplexStat extends BaseStat {
    List<IStat> block = new ArrayList<>();

    public List<IStat> block() {
        return block;
    }

    @Override
    public String name() {
        return getClass().getSimpleName().toLowerCase();
    }

}
