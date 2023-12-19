package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.InferenceNode;
import org.sudu.experiments.parser.common.graph.node.MemberNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.type.TypeMap;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Nodes.*;

public class ScopeGraphReader {

  public ScopeNode scopeRoot;
  public IntervalNode intervalRoot;
  public TypeMap typeMap;
  private final ArrayReader reader;
  private final char[] chars;
  private ScopeNode[] scopeNodes;
  private List<String> types;
  private DeclNodeReader declNodeReader;
  private RefNodeReader refNodeReader;

  public ScopeGraphReader(
      int[] ints,
      char[] chars
  ) {
    this.reader = new ArrayReader(ints);
    this.chars = chars;
  }

  public void readFromInts() {
    readTypes();
    readScopeRoot();
    readIntervalNode();
  }

  private void readTypes() {
    int len = reader.next();
    typeMap = new TypeMap();
    types = new ArrayList<>();
    readTypesNames(len);
    readSupertypes();
  }

  private void readTypesNames(int len) {
    for (int i = 0; i < len; i++) {
      String name = nextString();
      typeMap.put(name, new ArrayList<>());
      types.add(name);
    }
  }

  private void readSupertypes() {
    for (var typeEntry: typeMap.entrySet()) {
      typeEntry.getValue().addAll(readTypeList());
    }
  }

  private void readScopeRoot() {
    int len = reader.next();
    if (len == -1) {
      scopeRoot = null;
      return;
    }
    scopeNodes = new ScopeNode[len];
    declNodeReader = new DeclNodeReader(reader, chars, types);
    refNodeReader = new RefNodeReader(reader, chars, types);
    scopeRoot = readScope(null);
  }

  private ScopeNode readScope(ScopeNode parent) {
    int scopeInd = reader.next();
    int scopeType = reader.next();

    var declNodeList = declNodeReader.readDeclNodes();
    ScopeNode scopeNode = switch (scopeType) {
      case FAKE_NODE -> new FakeNode(parent);
      case BASE_NODE -> new ScopeNode(parent);
      case MEMBER_NODE -> new MemberNode(parent, declNodeList);
      default -> throw new IllegalStateException("Unknown scope type: " + scopeType);
    };

    scopeNode.declarations = declNodeList;
    scopeNode.references = refNodeReader.readRefNodes();
    scopeNode.importTypes = readTypeList();
    scopeNode.inferences = readInferences();
    scopeNode.type = readScopeType();
    scopeNode.children = readChildrenScopes(scopeNode);
    scopeNodes[scopeInd] = scopeNode;
    return scopeNode;
  }

  private List<String> readTypeList() {
    int len = reader.next();
    List<String> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      int typeInd = reader.next();
      result.add(types.get(typeInd));
    }
    return result;
  }

  private List<ScopeNode> readChildrenScopes(ScopeNode parent) {
    int len = reader.next();
    List<ScopeNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) result.add(readScope(parent));
    return result;
  }

  private List<InferenceNode> readInferences() {
    int len = reader.next();
    List<InferenceNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      var decl = declNodeReader.readDeclNode();
      var ref = refNodeReader.readRefNode();
      var inferType = reader.next();
      result.add(new InferenceNode(decl, ref, inferType));
    }
    return result;
  }

  private String readScopeType() {
    int typeInd = reader.next();
    if (typeInd == -1) return null;
    return types.get(typeInd);
  }

  private void readIntervalNode() {
    int flag = reader.next();
    if (flag == -1) return;
    intervalRoot = IntervalNode.readNode(reader, scopeNodes);
  }

  private String nextString() {
    int offset = reader.next(),
        count = reader.next();
    return new String(chars, offset, count);
  }
}
