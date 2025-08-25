package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.parser.common.graph.ScopeGraph;

import java.util.ArrayList;
import java.util.List;

public class ScopeProxy {

  public static final String RESOLVE_ALL = "ScopeUtils.resolveAll";

  public static void resolveAll(Object[] a, ArrayList<Object> result) {
    int[] graphInts = ArgsCast.array(a, 0).ints();
    char[] graphChars = ArgsCast.array(a, 1).chars();
    int version = ArgsCast.array(a, 2).ints()[0];
    resolveAll(graphInts, graphChars, version, result);
  }

  public static void resolveAll(int[] graphInts, char[] graphChars, int version, List<Object> result) {
    int[] ints = ScopeGraph.resolveFromInts(graphInts, graphChars);
    result.add(ints);
    result.add(new int[]{version});
  }

}
