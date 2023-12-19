package org.sudu.experiments.parser.activity.graph;

import java.util.Arrays;

public class Path implements Comparable<Path>{

    public static final int LEN = 3;

    public Node[] nodes;
    public String[] ids;
    long[][] visited = new long[LEN][];
    int[] unvisited = new int[LEN];

    public boolean used;

    public Path(Node.RecContext ctx, int fullCount, int idsCount, int branchCount) {
        this.nodes = new Node[fullCount];
        System.arraycopy(ctx.fullPath, 0, nodes, 0, fullCount);

        this.ids = new String[idsCount];
        System.arraycopy(ctx.ids, 0, this.ids, 0, idsCount);

        unvisited[0] = branchCount;
        visited[0] = new long[unvisited[0]];
        System.arraycopy(ctx.visited1, 0, visited[0], 0, unvisited[0]);
        Arrays.sort(visited[0], 0, unvisited[0]);

        unvisited[1] = Math.max(0, idsCount-1);
        visited[1] = new long[unvisited[1]];
        System.arraycopy(ctx.visited2, 0, visited[1], 0, unvisited[1]);
        Arrays.sort(visited[1], 0, unvisited[1]);

        unvisited[2] = Math.max(0, idsCount-2);
        visited[2] = new long[unvisited[2]];
        System.arraycopy(ctx.visited3, 0, visited[2], 0, unvisited[2]);
        Arrays.sort(visited[2], 0, unvisited[2]);
    }

    public void except(Path other) {
        for (int i=0; i<LEN; i++) {
            unvisited[i] = except(
                    visited[i],
                    other.visited[i],
                    unvisited[i],
                    other.unvisited[i]
            );
        }
    }

    private int except(long[] my, long[] other, int myCount, int otherCount) {
        int pMy = 0;
        int pOther = 0;

        while (pMy < myCount && pOther < otherCount) {
            if (my[pMy] < other[pOther])
                pMy++;
            else if (my[pMy] > other[pOther])
                pOther++;
            else {
                my[pMy] = -1;
                pMy++;
                pOther++;
            }
        }

        int newCount = 0;
        pMy = 0;
        for (int i=0; i < myCount; i++) {
            if (my[i] != -1) {
                my[pMy++] = my[i];
                newCount++;
            }
        }

        return newCount;
    }

    @Override
    public int compareTo(Path o) {
        for (int i=0; i<LEN; i++) {
            var c = unvisited[i] - o.unvisited[i];
            if (c != 0)
                return c;
        }
        return 0;
    }

    public String toString() {
        if (ids.length == 0) return "<empty>";

        StringBuilder res = new StringBuilder();
        res.append(ids[0]);
        for (int i=1; i< ids.length; i++) {
            res.append(" → ");
            res.append(ids[i]);
        }
        return res.toString();
    }
}
