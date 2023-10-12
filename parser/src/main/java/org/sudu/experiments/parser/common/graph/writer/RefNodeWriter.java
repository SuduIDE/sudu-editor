package org.sudu.experiments.parser.common.graph.writer;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;

import java.util.HashMap;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Refs.*;

public class RefNodeWriter {

  private final ArrayWriter writer;
  private final StringBuilder declStringBuilder;
  private final HashMap<String, Integer> typeIdentityMap;

  public RefNodeWriter(
      ArrayWriter writer,
      StringBuilder declStringBuilder,
      HashMap<String, Integer> typeIdentityMap
  ) {
    this.writer = writer;
    this.declStringBuilder = declStringBuilder;
    this.typeIdentityMap = typeIdentityMap;
  }

  void writeRefs(ScopeNode scope) {
    writer.write(scope.references.size());
    scope.references.forEach(this::writeRefNode);
  }

  void writeRefNode(RefNode refNode) {
    if (refNode instanceof ExprRefNode exprRefNode) writeExprRef(exprRefNode);
    else if (refNode instanceof MethodCallNode methodCallNode) writeMethodCall(methodCallNode);
    else if (refNode instanceof QualifiedRefNode qualifiedRefNode) writeQualifiedRef(qualifiedRefNode);
    else if (refNode != null) writeBaseRef(refNode);
    else writer.write(NULL);
  }

  private void writeRefName(RefNode node) {
    String name = node.ref.name;
    writer.write(declStringBuilder.length(), name.length());
    writer.write(node.ref.position);
    writeType(node.type);
    declStringBuilder.append(name);
  }

  private void writeType(String type) {
    if (type == null) writer.write(-1);
    else writer.write(typeIdentityMap.get(type));
  }

  private void writeExprRef(ExprRefNode exprRefNode) {
    writer.write(EXPR_NODE);
    writeArgs(exprRefNode.refNodes);
  }


  private void writeMethodCall(MethodCallNode methodCallNode) {
    writer.write(METHOD_CALL_NODE);
    writeRefName(methodCallNode);
    writer.write(methodCallNode.callType);
    writeArgs(methodCallNode.callArgs);
  }

  private void writeQualifiedRef(QualifiedRefNode qualifiedRefNode) {
    writer.write(QUALIFIED_CALL_NODE);
    writeRefNode(qualifiedRefNode.begin);
    writeRefNode(qualifiedRefNode.cont);
  }

  private void writeBaseRef(RefNode refNode) {
    writer.write(BASE_REF_NODE);
    writeRefName(refNode);
    writer.write(refNode.refType);
  }

  private void writeArgs(List<RefNode> args) {
    writer.write(args.size());
    args.forEach(this::writeRefNode);
  }

}
