package org.sudu.experiments.parser.common.graph.reader;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.node.ref.*;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Refs.*;

public class RefNodeReader {

  private final ArrayReader reader;
  private final char[] chars;
  private final List<String> types;

  public RefNodeReader(
      ArrayReader reader,
      char[] chars,
      List<String> types
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
      case METHOD_CALL_NODE -> readMethodCall();
      case QUALIFIED_CALL_NODE -> readQualified();
      case BASE_REF_NODE -> readBaseRef();
      case EXPR_NODE -> readExprNode();
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

  private String readType() {
    int typeInd = reader.next();
    if (typeInd == -1) return null;
    return types.get(typeInd);
  }

//  private CreatorCallNode readCreatorCall() {
//    Name name = readName();
//    Type type = readType();
//    List<RefNode> args = readArgs();
//    return new CreatorCallNode(name, type, args);
//  }

//  private FieldRefNode readFieldRef() {
//    Name name = readName();
//    Type type = readType();
//    return new FieldRefNode(name, type);
//  }

  private MethodCallNode readMethodCall() {
    Name name = readName();
    String type = readType();
    int callType = reader.next();
    List<RefNode> args = readArgs();
    return new MethodCallNode(name, type, callType, args);
  }

  private QualifiedRefNode readQualified() {
    RefNode begin = readRefNode();
    RefNode cont = readRefNode();
    return new QualifiedRefNode(begin, cont);
  }

  private RefNode readBaseRef() {
    Name name = readName();
    String type = readType();
    int refType = reader.next();
    return new RefNode(name, type, refType);
  }

  private RefNode readExprNode() {
    List<RefNode> refNodes = readArgs();
    return new ExprRefNode(refNodes);
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
