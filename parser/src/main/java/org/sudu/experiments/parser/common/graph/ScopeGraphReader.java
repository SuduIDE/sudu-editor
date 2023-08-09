package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.graph.type.Type;

public class ScopeGraphReader {

  public ScopeGraphReader(int[] typeInts, char[] typeChars) {
    ArrayReader reader = new ArrayReader(typeInts);

    int N = reader.next();
    Type[] types = new Type[N];
    for (int i = 0; i < N; i++) {
      int from = reader.next(), offset = reader.next();

      types[i] = new Type(new String(typeChars, from, offset));
    }
  }

}
