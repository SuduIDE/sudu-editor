package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EmptyNode;

public class UnknownStat extends ComplexStat {
    @Override
    public Dag2Part toDag2() {
        return Dag2Part.singleExit(new EmptyNode());
    }
}
