package org.sudu.experiments.parser.common.graph.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeMap extends HashMap<String, List<String>> {

  public void addType(String type) {
    if (containsKey(type)) return;
    put(type, new ArrayList<>());
  }

  public boolean matchType(String type1, String type2) {
    if (type1 == null || type2 == null) return true;
    if (!containsKey(type1)) throw new IllegalArgumentException("Cannot find type: " + type1);
    if (!containsKey(type2)) throw new IllegalArgumentException("Cannot find type: " + type2);
    if (type1.equals(type2)) return true;
    else {
      List<String> supertypes = get(type2);
      for(var supertype: supertypes) {
        if (matchType(type1, supertype)) return true;
      }
    }
    return false;
  }


  public static String getArrayElemType(String type) {
    if (type == null) return null;
    if (typeStartsWith(type,
        "Iterable<", "Collection<",
        "List<", "ArrayList<", "LinkedList<", "Vector<", "Stack<",
        "Set<", "SortedSet<", "TreeSet<", "HashSet<", "LinkedHashSet<",
        "Queue<", "Deque<", "ArrayDeque<")
    ) {
      int stInd = type.indexOf("<");
      int endInd = type.indexOf(">");
      if (stInd != -1 && endInd != -1) return type.substring(stInd + 1, endInd);
    }
    int stInd = type.lastIndexOf("[");
    if (stInd != -1) return type.substring(0, stInd);
    return null;
  }

  private static boolean typeStartsWith(String type, String... starts) {
    for (var start: starts)
      if (type.startsWith(start)) return true;
    return false;
  }

}
