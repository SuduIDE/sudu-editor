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
    public void print(StringBuilder acc, int indent) {
        acc.append(" ".repeat(indent));
        acc.append(name());
        acc.append(" ");
        IStat.printBlock(acc, indent, ",", block);
    }

    @Override
    public String name() {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String toDag1() {
        StringBuilder acc = new StringBuilder();
        acc.append("subgraph "+getUniqueId()+"[\""+name()+"\"]\r\n");
        acc.append("direction TB\r\n");
        IStat.toDag1Blocks(acc, block);
        acc.append("end\r\n");
        return acc.toString();
    }

}
