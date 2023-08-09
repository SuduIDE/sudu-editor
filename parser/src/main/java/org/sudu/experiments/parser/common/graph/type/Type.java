package org.sudu.experiments.parser.common.graph.type;

import org.sudu.experiments.parser.common.graph.node.ScopeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Type {

  public String type;
  public List<Type> supertypes;
  public ScopeNode associatedScope;

  public Type(String type, ScopeNode scopeNode) {
    this.type = type;
    this.supertypes = new ArrayList<>();
    this.associatedScope = scopeNode;
  }

  public Type(String type) {
    this(type, null);
  }

  public boolean match(Type another) {
    if (type == null || another == null || another.type == null) return true;
    if (type.equals(another.type)) return true;
    for (var supertype: supertypes) {
      if (match(supertype)) return true;
    }
    return false;
  }

  public static Type UNKNOWN() {
    return new Type(null);
  }

  @Override
  public String toString() {
    if (supertypes.isEmpty()) return type;
    if (supertypes.size() == 1) return type + " <= " + supertypes.get(0);
    else {
      return type + " <= (" + String.join(", ", supertypes.stream().map(Type::toString).toList()) + ")";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Type type1 = (Type) o;
    return Objects.equals(type, type1.type) && Objects.equals(supertypes, type1.supertypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, supertypes);
  }

}
