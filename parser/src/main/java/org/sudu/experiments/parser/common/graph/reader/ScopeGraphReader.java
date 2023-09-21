package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Nodes.*;

public class ScopeGraphReader {

  private final ArrayReader reader;
  private final char[] chars;

  private Type[] types;
  private ScopeNode[] scopeNodes;
  private int scopePtr = 0;

  public ScopeGraphReader(
      int[] ints,
      char[] chars
  ) {
    this.reader = new ArrayReader(ints);
    this.chars = chars;
  }

  public void readFromInts() {
    readTypes();
  }

  public void readTypes() {
    int len = reader.next();
    types = new Type[len];

    readTypesNames();
    readSupertypes();
  }

  public void readTypesNames() {
    for (int i = 0; i < types.length; i++) {
      String name = nextString();
      types[i] = new Type(name);
    }
  }

  public void readSupertypes() {
    for (Type type: types) {
      int supersLen = reader.next();
      List<Type> supertypes = new ArrayList<>();
      for (int j = 0; j < supersLen; j++) {
        int ind = reader.next();
        supertypes.add(types[ind]);
      }
      type.supertypes = supertypes;
    }
  }

  void readScopes() {
    int len = reader.next();
    scopeNodes = new ScopeNode[len];
  }

  ScopeNode readScope(ScopeNode parent) {
    int type = reader.next();
    if (type == FAKE_NODE) {
      var scope = new FakeNode(parent);
      scopeNodes[scopePtr++] = scope;
      return scope;
    }
  }

  private String nextString() {
    int offset = reader.next(),
        count = reader.next();
    return new String(chars, offset, count);
  }

}
