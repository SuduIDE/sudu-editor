package org.sudu.experiments.parser.common.graph.type;

import java.util.HashMap;
import java.util.List;

public class TypeMap extends HashMap<String, List<String>> {

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

}
