package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EdgeFrom;
import org.sudu.experiments.parser.activity.graph.EmptyNode;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;

public class Select extends ComplexStat {

    @Override
    public Dag2Part toDag2() {
        var start = Dag2Part.singleExit(new EmptyNode());
        ArrayList<EdgeFrom> output = new ArrayList<>();
        for (var b:block) {
            var part = b.toDag2();
            IStat.joinDag2(start, part);

            output.addAll(part.output);
        }

        start.output.clear();
        start.output.addAll(output);
        return IStat.joinDag2(start, Dag2Part.singleExit(new EmptyNode()));
    }
}
