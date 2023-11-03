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
        return getUniqueId()+"("+name+")";
    }

    @Override
    public Dag2Part toDag2() {
        return Dag2Part.singleExit(new Id(name));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean check(Path path, int from) {
        return checkPos(path, from) >= 0;
    }

    int checkPos(Path path, int from) {
        for (var i= from; i<path.ids.size(); i++)
            if (path.ids.get(i).equals(this.name))
                return i+1;

        return -1;
    }
}
