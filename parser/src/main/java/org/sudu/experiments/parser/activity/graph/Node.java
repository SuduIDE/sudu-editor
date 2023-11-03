package org.sudu.experiments.parser.activity.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Node {
    static int idIncrement = 0;

    private final int uniqueId;

    public String getUniqueId() {
        return "id" + uniqueId;
    }

    public Node() {
        uniqueId = ++idIncrement;
    }

    public final ArrayList<EdgeTo> edges = new ArrayList<>();


    static Set<Node> visited = new HashSet<>();

    void rec(StringBuilder acc) {
        if (!visited.add(this))
            return;

        acc.append(getUniqueId()+ drawDagNode() +"\r\n");
        for (var e: edges) {
            var n = e.to;

            //compress empty node
            if (n instanceof EmptyNode && n.edges.size() == 1) {
                n = n.edges.get(0).to;
            }
            n.rec(acc);

            String label = e.expr == null ? "" : e.elseBranch ? "else" : ""+e.getLabel()+"";
            if (!label.isEmpty()) label = "|\""+label+"\"|";
            acc.append(getUniqueId()+"-->"+label+ n.getUniqueId() +"\r\n");
        }
    }

    public abstract String name();

    public String drawDagNode() {
        return "("+name()+")";
    }


    public static String printRecDag2(Node start) {
        visited.clear();
        var acc = new StringBuilder("flowchart TB\r\n");


        start.rec(acc);

        return acc.toString();
    }
}

