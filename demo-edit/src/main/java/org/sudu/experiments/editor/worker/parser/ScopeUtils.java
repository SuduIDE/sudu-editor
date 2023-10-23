package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.parser.common.graph.ScopeGraph;

import java.util.List;

public class ScopeUtils {

  public static final String RESOLVE_ALL = "ScopeUtils.resolveAll";

  public static void resolveAll(int[] graphInts, char[] graphChars, List<Object> result) {
    int[] ints = ScopeGraph.resolveFromInts(graphInts, graphChars);
    result.add(ints);
  }

}
