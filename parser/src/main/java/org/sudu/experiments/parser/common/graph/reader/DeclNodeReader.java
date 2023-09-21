package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.decl.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Decls.*;

public class DeclNodeReader {

  private final ArrayReader reader;
  private final char[] chars;
  private final Type[] types;

  public DeclNodeReader(
      ArrayReader reader,
      char[] chars,
      Type[] types
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
      case ARG_DECL_NODE -> readArgNode();
      case CREATOR_DECL_NODE -> readCreatorDecl();
      case BASE_DECL_NODE -> readDecl();
      case FIELD_DECL_NODE -> readFieldDecl();
      case METHOD_DECL_NODE -> readMethodDecl();
      case VAR_DECL_NODE -> readVarNode();
      default -> throw new IllegalStateException("Unexpected type: " + type);
    };
  }

  private Name readDeclName() {
    int offset = reader.next(),
        count = reader.next();
    String name = new String(chars, offset, count);
    int pos = reader.next();
    return new Name(name, pos);
  }

  private Type readType() {
    int typeInd = reader.next();
    if (typeInd == -1) return null;
    return types[typeInd];
  }

  private ArgNode readArgNode() {
    Name name = readDeclName();
    Type type = readType();
    return new ArgNode(name, type);
  }

  private CreatorNode readCreatorDecl() {
    Name name = readDeclName();
    Type type = readType();
    List<ArgNode> args = readArgs();
    return new CreatorNode(name, type, args);
  }

  private DeclNode readDecl() {
    Name name = readDeclName();
    Type type = readType();
    return new DeclNode(name, type);
  }

  private FieldNode readFieldDecl() {
    Name name = readDeclName();
    Type type = readType();
    return new FieldNode(name, type);
  }

  private MethodNode readMethodDecl() {
    Name name = readDeclName();
    Type type = readType();
    List<ArgNode> args = readArgs();
    return new MethodNode(name, type, args);
  }

  private VarNode readVarNode() {
    Name name = readDeclName();
    Type type = readType();
    return new VarNode(name, type);
  }

  private List<ArgNode> readArgs() {
    int len = reader.next();
    List<ArgNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      result.add(readArgNode());
    }
    return result;
  }

}
