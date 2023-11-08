package org.sudu.experiments.parser.activity.graph;

import org.sudu.experiments.parser.activity.graph.stat.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node {
    static int idIncrement = 0;
    static final int offset = 10;

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


    private void countRec(HashMap<Node, Integer> incoming) {
        if (incoming.containsKey(this)) {
            incoming.put(this, incoming.get(this)+1);
        } else {
            incoming.put(this, 1);
            for (var e: edges)
                e.to.countRec(incoming);
        }
    }

    private void simplifyRec(HashSet<Node> visited, HashMap<Node, Integer> incoming) {
        if (!visited.add(this)) return;

        var newEdges = new ArrayList<EdgeTo>();
        boolean modified;
        do {
            modified = false;
            for (var e : edges) {
                Node n = e.to;
                if (n instanceof EmptyNode && incoming.get(n) == 1 && e.expr == null) {
                    newEdges.addAll(n.edges);
                    modified = true;
                } else if (n instanceof EmptyNode && n.edges.size() == 1 && n.edges.get(0).expr == null) {
                    newEdges.add(new EdgeTo(n.edges.get(0).to, e.expr, e.elseBranch));
                    modified = true;
                } else {
                    newEdges.add(e);
                }
            }
            edges.clear();
            edges.addAll(newEdges);
            newEdges.clear();
        } while (modified);

        for (var e : edges) {
            e.to.simplifyRec(visited, incoming);
        }
    }

    public Node simplify() {
        var incoming = new HashMap<Node, Integer>();
        countRec(incoming);
        simplifyRec(new HashSet<>(), incoming);

        return this;
    }

    private void innerRecDag2(PrintContext ctx) {
        if (!ctx.visited.add(this))
            return;

        ctx.acc.append(getMermaidNodeId()+ drawDagNode() +"\r\n");
        for (var e: edges) {
            var n = e.to;

            n.innerRecDag2(ctx);

            String label = e.expr == null ? "" : e.elseBranch ? "else" : ""+e.getLabel()+"";
            if (!label.isEmpty()) label = "|\""+label+"\"|";

            int edgeN = ctx.edgeNumber++;
            ctx.acc.append(getMermaidNodeId()+"-->"+label+ n.getMermaidNodeId() +"\r\n");

            if (ctx.markedEdge.contains(edge(this, n))) {
                ctx.acc.append("linkStyle "+edgeN+" stroke:red\r\n");
            }
        }
    }


    public String printRecDag2(Path highlighted) {

        PrintContext ctx = new PrintContext();
        ctx.acc.append("flowchart TB\r\n");
        if (highlighted != null) {
            for (int i=1; i<highlighted.nodes.length; i++) {
                ctx.markedEdge.add(edge(highlighted.nodes[i-1], highlighted.nodes[i]));
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

    static class PrintContext {
        StringBuilder acc = new StringBuilder();
        Set<Node> visited = new HashSet<>();

        Set<Long> markedEdge = new HashSet<Long>();
        int edgeNumber = 0;
    }

    static class RecContext {
        static final int MAX = 200;

        Node[] fullPath = new Node[MAX];

        long[] visited1 = new long[MAX];
        long[] visited2 = new long[MAX-1];
        long[] visited3 = new long[MAX-2];
        Node[] idsPath = new Node[MAX];
        String[] ids = new String[MAX];
    }

    private void recPaths(Paths paths, RecContext ctx, int fullCount, int idsCount, int branchCount, Node prev) {
        ctx.fullPath[fullCount++] = this;
        if (this instanceof Id id) {
            ctx.idsPath[idsCount] = this;
            ctx.ids[idsCount++] = id.name();
        }

        if (prev != null && prev.edges.size() > 1) { //branching
            ctx.visited1[branchCount++] = edge(prev, this);
            if (branchCount >= 2)
                ctx.visited2[branchCount-2] = adjacent(ctx.visited1[branchCount-2], ctx.visited1[branchCount-1]);
            if (branchCount >= 3)
                ctx.visited3[branchCount-3] = adjacent(ctx.visited1[branchCount-3], ctx.visited1[branchCount-2], ctx.visited1[branchCount-1]);
        }

        if (edges.isEmpty()) { //final
           paths.add(new Path(ctx, fullCount, idsCount, branchCount));
        }

        for (var e: edges) {
            if (e.expr == null || e.expr.check(ctx.ids, 0, idsCount) ^ e.elseBranch)
                e.to.recPaths(paths, ctx, fullCount, idsCount, branchCount, this);
        }

    }

    public Path[][] calculateTestPaths() {
        Paths paths = new Paths();
        recPaths(paths, new RecContext(), 0, 0, 0, null);
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

    static long edge(Node n1, Node n2) {
        return (n1.getId() << offset) + n2.getId();
    }

    static long adjacent(long edge1, long edge2) {
        return (edge1 << 2*offset) + edge2;
    }

    static long adjacent(long edge1, long edge2, long edge3) {
        return (((edge1 << 2*offset) + edge2) << 2*offset) + edge3;
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
