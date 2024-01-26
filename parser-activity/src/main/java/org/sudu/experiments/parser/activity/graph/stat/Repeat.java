package org.sudu.experiments.parser.activity.graph.stat;

public class Repeat extends ComplexStat {
    int count;

    public Repeat(int count) {
        this.count = count;
    }

    @Override
    public String name() {
        return "repeat("+count+")";
    }

}
