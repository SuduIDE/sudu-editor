package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.decl.*;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Decls.*;

public class DeclNodeReader {

  private final ArrayReader reader;
  private final char[] chars;
  private final List<String> types;

  public DeclNodeReader(
      ArrayReader reader,
      char[] chars,
      List<String> types
  ) {
    this.reader = reader;
    this.chars = chars;
    this.types = types;
  }

  List<DeclNode> readDeclNodes() {
    int len = reader.next();
    List<DeclNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      result.add(readDeclNode());
    }
    return result;
  }

  DeclNode readDeclNode() {
    int type = reader.next();
    return switch (type) {
      case BASE_DECL_NODE -> readDecl();
      case METHOD_DECL_NODE -> readMethodDecl();
      default -> throw new IllegalStateException("Unexpected type: " + type);
    };
  }

  private Name readName() {
    int offset = reader.next(),
        count = reader.next();
    String name = new String(chars, offset, count);
    int pos = reader.next();
    return new Name(name, pos);
  }

  private String readType() {
    int typeInd = reader.next();
    if (typeInd == -1) return null;
    return types.get(typeInd);
  }

  private DeclNode readDecl() {
    Name name = readName();
    String type = readType();
    int declType = reader.next();
    return new DeclNode(name, type, declType);
  }

  private MethodNode readMethodDecl() {
    Name name = readName();
    String type = readType();
    int declType = reader.next();
    int callType = reader.next();
    List<String> types = readTypeList();
    return new MethodNode(name, type, declType, callType, types);
  }

  private List<String> readTypeList() {
    int len = reader.next();
    List<String> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      result.add(readType());
    }
    return result;
  }

}
