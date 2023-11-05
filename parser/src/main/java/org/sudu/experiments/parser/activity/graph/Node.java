package org.sudu.experiments.parser.activity.graph;

import org.sudu.experiments.parser.activity.graph.stat.Id;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node {
    static int idIncrement = 0;
    static final int offset = 16;

    private final int uniqueId;

    public String getMermaidNodeId() {
        return "id" + uniqueId;
    }

    public long getId() {
        return uniqueId;
    }

    public Node() {
        //handle overflows, also usable in composition
        if (idIncrement == 1 << offset)
            idIncrement = 0;

        uniqueId = ++idIncrement;
    }

    public final ArrayList<EdgeTo> edges = new ArrayList<>();






    public abstract String name();

    public String drawDagNode() {
        return "("+name()+")";
    }

    private void innerRecDag2(PrintContext ctx) {
        if (!ctx.visited.add(this))
            return;

        ctx.acc.append(getMermaidNodeId()+ drawDagNode() +"\r\n");
        for (var e: edges) {
            var n = e.to;

            //compress empty node
            while (n instanceof EmptyNode && n.edges.size() == 1) {
                n = n.edges.get(0).to;
            }
            n.innerRecDag2(ctx);

            String label = e.expr == null ? "" : e.elseBranch ? "else" : ""+e.getLabel()+"";
            if (!label.isEmpty()) label = "|\""+label+"\"|";

            int edgeN = ctx.edgeNumber++;
            ctx.acc.append(getMermaidNodeId()+"-->"+label+ n.getMermaidNodeId() +"\r\n");

            if (ctx.markedEdge.contains(pair(this, n))) {
                ctx.acc.append("linkStyle "+edgeN+" stroke:red\r\n");
            }
        }
    }


    public String printRecDag2(Path highlighted) {

        PrintContext ctx = new PrintContext();
        ctx.acc.append("flowchart TB\r\n");
        if (highlighted != null) {
            for (int i=1; i<highlighted.nodes.length; i++) {
                ctx.markedEdge.add(pair(highlighted.nodes[i-1], highlighted.nodes[i]));
            }
        }

        this.innerRecDag2(ctx);

        if (highlighted != null) {
            for (var n: highlighted.nodes) {
                ctx.acc.append("style "+n.getMermaidNodeId()+" stroke:red\r\n");
            }
        }

        return ctx.acc.toString();
    }

    class PrintContext {
        StringBuilder acc = new StringBuilder();
        Set<Node> visited = new HashSet<>();

        Set<Long> markedEdge = new HashSet<Long>();
        int edgeNumber = 0;
    }

    class RecContext {
        static final int MAX = 200;

        Node[] fullPath = new Node[MAX];

        long[] visited1 = new long[MAX];
        long[] visited2 = new long[MAX-1];
        long[] visited3 = new long[MAX-2];
        Node[] idsPath = new Node[MAX];
        String[] ids = new String[MAX];
    }

    private void recPaths(Paths paths, RecContext ctx, int depth, int ids) {
        if (this instanceof EmptyNode && edges.size() == 1) {
            edges.get(0).to.recPaths(paths, ctx, depth, ids);
            return;
        }

        ctx.fullPath[depth++] = this;
        if (this instanceof Id id) {
            ctx.idsPath[ids] = this;
            ctx.ids[ids++] = id.name();

            ctx.visited1[ids - 1] = this.getId();

            if (ids >= 2)
                ctx.visited2[ids - 2] = pair(ctx.idsPath[ids - 2], this);
            if (ids >= 3)
                ctx.visited3[ids - 3] = triple(ctx.idsPath[ids - 3], ctx.idsPath[ids - 2], this);
        }

        if (edges.isEmpty()) { //final
           paths.add(new Path(ctx, ids, depth));
        }

        for (var e: edges) {
            if (e.expr == null || e.expr.check(ctx.ids, 0, ids) ^ e.elseBranch)
                e.to.recPaths(paths, ctx, depth, ids);
        }

    }

    public Path[][] calculateTestPaths() {
        Paths paths = new Paths();
        recPaths(paths, new RecContext(), 0, 0);
        paths.process();

        var res = new Path[4][];

        List<Path>[] lst = new List[]{
                paths.coverage1,
                paths.coverage2,
                paths.coverage3,
                paths.fullCoverage()
        };
        for (int i=0; i<4; i++) {
            res[i] = new Path[lst[i].size()];
            for (int j = 0; j<res[i].length; j++)
                res[i][j] = lst[i].get(j);
        }
        return res;
    }

    static long pair(Node n1, Node n2) {
        return (n1.getId() << offset) + n2.getId();
    }

    static long triple(Node n1, Node n2, Node n3) {
        return (((n1.getId() << offset) + n2.getId()) << offset) + n3.getId();
    }
}

class Paths {
    List<Path> unsorted = new ArrayList<>();

    List<Path> coverage1 = new ArrayList<>();
    List<Path> coverage2 = new ArrayList<>();
    List<Path> coverage3 = new ArrayList<>();

    public void add(Path path) {
        unsorted.add(path);
    }

    public List<Path> fullCoverage() {
        ArrayList<Path> res = new ArrayList();
        for (Path p: unsorted)
            if (!p.used)
                res.add(p);

        return res;
    }

    void process() {
        while (true) {
            Path best = null;
            for (Path p : unsorted) {
                if (p.used)
                    continue;
                if (best == null || p.compareTo(best) > 0) {
                    best = p;
                }
            }

            if (best == null) {
                return; //everything processed
            }

            if (best.unvisited[0] > 0)
                coverage1.add(best);
            else if (best.unvisited[1] > 0)
                coverage2.add(best);
            else if (best.unvisited[2] > 0)
                coverage3.add(best);
            else
                return;

            best.used = true;
            for (Path p: unsorted) {
                if (!p.used)
                    p.except(best);
            }
        }
    }
}
