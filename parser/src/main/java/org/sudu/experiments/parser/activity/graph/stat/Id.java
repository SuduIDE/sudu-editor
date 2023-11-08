package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.BaseStat;
import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.Path;

public class Id extends BaseStat implements IExpr {
    String name;

    public Id(String name) {
        this.name = name;
    }

    @Override
    public void print(StringBuilder acc, int indent) {
        acc.append(" ".repeat(indent));
        acc.append(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toDag1() {
        return getMermaidNodeId()+"("+name+")";
    }

    @Override
    public Dag2Part toDag2Part() {
        return Dag2Part.singleExit(new Id(name));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean check(String[] ids, int from, int to) {
        return checkPos(ids, from, to) >= 0;
    }

    int checkPos(String[] ids, int from, int to) {
        for (var i= from; i<to; i++)
            if (ids[i].equals(this.name))
                return i+1;

        return -1;
    }
}
