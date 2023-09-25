package org.sudu.experiments.parser.common.graph.writer;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.IdentityHashMap;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Refs.*;

public class RefNodeWriter {

  private final ArrayWriter writer;
  private final StringBuilder declStringBuilder;
  private final IdentityHashMap<Type, Integer> typeIdentityMap;

  public RefNodeWriter(
      ArrayWriter writer,
      StringBuilder declStringBuilder,
      IdentityHashMap<Type, Integer> typeIdentityMap
  ) {
    this.writer = writer;
    this.declStringBuilder = declStringBuilder;
    this.typeIdentityMap = typeIdentityMap;
  }

  void writeRefs(ScopeNode scope) {
    writer.write(scope.refList.size());
    scope.refList.forEach(this::writeRefNode);
  }

  private void writeRefNode(RefNode refNode) {
    if (refNode instanceof CreatorCallNode creatorCallNode) writeCreatorCall(creatorCallNode);
    else if (refNode instanceof FieldRefNode fieldRefNode) writeFieldRef(fieldRefNode);
    else if (refNode instanceof MethodCallNode methodCallNode) writeMethodCall(methodCallNode);
    else if (refNode instanceof QualifiedRefNode qualifiedRefNode) writeQualifiedRef(qualifiedRefNode);
    else if (refNode instanceof SuperNode superNode) writeSuperNode(superNode);
    else if (refNode instanceof ThisNode thisNode) writeThisNode(thisNode);
    else if (refNode instanceof TypeNode typeNode) writeTypeNode(typeNode);
    else writeBaseRef(refNode);
  }

  private void writeRefName(RefNode node) {
    String name = node.decl.name;
    writer.write(declStringBuilder.length(), name.length());
    writer.write(node.decl.position);
    writeType(node.type);
    declStringBuilder.append(name);
  }

  private void writeType(Type type) {
    if (type == null || type.type == null) {
      writer.write(-1);
      return;
    }
    int typeNum = typeIdentityMap.get(type);
    writer.write(typeNum);
  }

  private void writeCreatorCall(CreatorCallNode creatorCallNode) {
    writer.write(CREATOR_CALL_NODE);
    writeRefName(creatorCallNode);
    writeArgs(creatorCallNode.callArgs);
  }

  private void writeFieldRef(FieldRefNode fieldRefNode) {
    writer.write(FIELD_REF_NODE);
    writeRefName(fieldRefNode);
  }

  private void writeMethodCall(MethodCallNode methodCallNode) {
    writer.write(METHOD_CALL_NODE);
    writeRefName(methodCallNode);
    writeArgs(methodCallNode.callArgs);
  }

  private void writeQualifiedRef(QualifiedRefNode qualifiedRefNode) {
    writer.write(QUALIFIED_CALL_NODE);
    writeRefNode(qualifiedRefNode.begin);
    writeRefNode(qualifiedRefNode.cont);
  }

  private void writeSuperNode(SuperNode superNode) {
    writer.write(SUPER_NODE);
    writeRefName(superNode);
  }

  private void writeThisNode(ThisNode thisNode) {
    writer.write(THIS_NODE);
    writeRefName(thisNode);
  }

  private void writeTypeNode(TypeNode typeNode) {
    writer.write(TYPE_NODE);
    writeType(typeNode.type);
  }

  private void writeBaseRef(RefNode refNode) {
    writer.write(BASE_REF_NODE);
    writeRefName(refNode);
  }

  private void writeArgs(List<RefNode> args) {
    writer.write(args.size());
    args.forEach(this::writeRefNode);
  }

}
