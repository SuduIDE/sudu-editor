package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.Arrays;

public class Random extends ComplexStat {
    private static long globalSeed = 42;
    static XorShiftRandom random;

    public static long getGlobalSeed() {
        return globalSeed;
    }
    public static void setGlobalSeedAndInitiateRandom(long seed) {
        globalSeed = seed;
        random = new XorShiftRandom((int)globalSeed, (int)globalSeed);
    }

    int count;

    public Random(int count) {
        this.count = count;
    }

    @Override
    public String name() {
        return count == 1 ? "random":"random("+count+")";
    }

    private int[] getRandomPermutation() {
        int n = block().size();
        int[] res = new int[n];

        for (int i = 0; i< n; i++) res[i] = i;
        for (int i=0; i<n; i++) {
            var next = i + random.nextInt(n-i);
            var s = res[i];
            res[i] = res[next];
            res[next] = s;
        }
        return res;
    }

    public Dag2Part toDag2() {
        if (count <= 0 || count > block().size()) {
            count = 1;
        }

        Dag2Part res = null;
        var permutation = getRandomPermutation();
        for (int i=0; i<count; i++) {
            res = IStat.joinDag2(res, block.get(permutation[i]).toDag2());
        }
        System.out.println("seed="+globalSeed+" "+Arrays.toString(permutation));
        return res;
    }
}
