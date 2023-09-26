package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Refs.*;

public class RefNodeReader {

  private final ArrayReader reader;
  private final char[] chars;
  private final List<Type> types;

  public RefNodeReader(
      ArrayReader reader,
      char[] chars,
      List<Type> types
  ) {
    this.reader = reader;
    this.chars = chars;
    this.types = types;
  }

  public List<RefNode> readRefNodes() {
    int len = reader.next();
    List<RefNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      result.add(readRefNode());
    }
    return result;
  }

  public RefNode readRefNode() {
    int type = reader.next();
    return switch (type) {
      case NULL -> null;
      case CREATOR_CALL_NODE -> readCreatorCall();
      case FIELD_REF_NODE -> readFieldRef();
      case METHOD_CALL_NODE -> readMethodCall();
      case QUALIFIED_CALL_NODE -> readQualified();
      case BASE_REF_NODE -> readBaseRef();
      case SUPER_NODE -> readSuperNode();
      case THIS_NODE -> readThisNode();
      case TYPE_NODE -> readTypeNode();
      default -> throw new IllegalStateException("Unexpected ref node type: " + type);
    };
  }

  private Name readName() {
    int offset = reader.next(),
        count = reader.next();
    String name = new String(chars, offset, count);
    int pos = reader.next();
    return new Name(name, pos);
  }

  private Type readType() {
    int typeInd = reader.next();
    if (typeInd == -1) return null;
    return types.get(typeInd);
  }

  private CreatorCallNode readCreatorCall() {
    Name name = readName();
    Type type = readType();
    List<RefNode> args = readArgs();
    return new CreatorCallNode(name, type, args);
  }

  private FieldRefNode readFieldRef() {
    Name name = readName();
    Type type = readType();
    return new FieldRefNode(name, type);
  }

  private MethodCallNode readMethodCall() {
    Name name = readName();
    Type type = readType();
    List<RefNode> args = readArgs();
    return new MethodCallNode(name, type, args);
  }

  private QualifiedRefNode readQualified() {
    RefNode begin = readRefNode();
    RefNode cont = readRefNode();
    return new QualifiedRefNode(begin, cont);
  }

  private RefNode readBaseRef() {
    Name name = readName();
    Type type = readType();
    return new RefNode(name, type);
  }

  private SuperNode readSuperNode() {
    Name name = readName();
    Type type = readType();
    return new SuperNode(name, type);
  }

  private ThisNode readThisNode() {
    Name name = readName();
    Type type = readType();
    return new ThisNode(name, type);
  }

  private TypeNode readTypeNode() {
    Type type = readType();
    return new TypeNode(type);
  }

  private List<RefNode> readArgs() {
    int len = reader.next();
    List<RefNode> result = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      result.add(readRefNode());
    }
    return result;
  }

}
