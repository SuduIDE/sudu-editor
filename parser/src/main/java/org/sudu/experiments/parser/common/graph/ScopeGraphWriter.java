package org.sudu.experiments.parser.common.graph;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.IdentityHashMap;

public class ScopeGraphWriter {

  public final ScopeGraph graph;
  public int[] typeInts;
  public char[] typeChars;
  private ArrayWriter writer;

  public ScopeGraphWriter(ScopeGraph graph) {
    this.graph = graph;
    writer = new ArrayWriter();
  }

  public IdentityHashMap<Type, Integer> typeIdentityMap;

  public void toInts() {
    writeInts();
    this.typeInts = writer.getInts();
  }

  private void writeInts() {
    writeTypes();
  }

  private void writeTypes() {
    writer.write(graph.typeMap.size());

    StringBuilder sb = new StringBuilder();
    int from = 0;
    for (var type: graph.typeMap.values()) {
      getTypeNum(type);
      sb.append(type.type);
      writer.write(from, type.type.length());
      from += type.type.length();
    }
    this.typeChars = sb.toString().toCharArray();

    for (var type: graph.typeMap.values()) {
      writer.write(type.supertypes.size());
      for (var supertype: type.supertypes) {
        var num = getTypeNum(supertype);
        writer.write(num);
      }
    }
  }

  private void writeTypesNames() {

  }

  int getTypeNum(Type type) {
    if (typeIdentityMap.containsKey(type)) return typeIdentityMap.get(type);
    else {
      int size = typeIdentityMap.size();
      typeIdentityMap.put(type, size);
      return size;
    }
  }

}
