package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EdgeFrom;
import org.sudu.experiments.parser.activity.graph.EmptyNode;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends ComplexStat {

    private <A> void swap(List<A> lst, int i, int j) {
        var s = lst.get(i);
        lst.set(i, lst.get(j));
        lst.set(j, s);
    }

    private void  permutations(List<IStat> mutated, List<Dag2Part> acc, int pos) {
        if (pos == mutated.size()) {
            acc.add(IStat.joinDag2(null, mutated));
        }
        for (int i = pos; i < mutated.size(); i++) {
            swap(mutated, pos, i);
            permutations(mutated, acc, pos+1);
            swap(mutated, pos, i);
        }
    }

    @Override
    public Dag2Part toDag2Part() {
        var start = Dag2Part.singleExit(new EmptyNode());
        ArrayList<EdgeFrom> output = new ArrayList<>();

        ArrayList<Dag2Part> acc = new ArrayList<>();
        permutations(new ArrayList(block), acc, 0);

        for (var b : acc) {
            IStat.joinDag2(start, b);
            output.addAll(b.output);
        }

        start.output.clear();
        start.output.addAll(output);
        return IStat.joinDag2(start, Dag2Part.singleExit(new EmptyNode()));
    }
}
