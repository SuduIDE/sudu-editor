package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.parser.common.graph.ScopeGraph;

import java.util.List;

public class ScopeProxy {

  public static final String RESOLVE_ALL = "ScopeUtils.resolveAll";

  public static void resolveAll(int[] graphInts, char[] graphChars, int[] lastParsedVersion, List<Object> result) {
    int[] ints = ScopeGraph.resolveFromInts(graphInts, graphChars);
    result.add(ints);
    result.add(lastParsedVersion);
  }

}
