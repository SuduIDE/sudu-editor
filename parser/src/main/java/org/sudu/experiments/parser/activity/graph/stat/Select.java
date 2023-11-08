package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EdgeFrom;
import org.sudu.experiments.parser.activity.graph.EmptyNode;
import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;

public class Select extends ComplexStat {
    public final ArrayList<IExpr> conditions = new ArrayList<>();

    @Override
    public Dag2Part toDag2Part() {
        var start = Dag2Part.singleExit(new EmptyNode());
        ArrayList<EdgeFrom> output = new ArrayList<>();
        for (var i=0; i<block.size(); i++) {
            var b = block.get(i);
            var c = conditions.get(i);
            if (c == null) {
                start.output.set(0, new EdgeFrom(start.input));
            } else {
                start.output.set(0, EdgeFrom.If(start.input, c));
            }

            var part = b.toDag2Part();
            IStat.joinDag2(start, part);

            output.addAll(part.output);
        }

        start.output.clear();
        start.output.addAll(output);
        return IStat.joinDag2(start, Dag2Part.singleExit(new EmptyNode()));
    }
}
