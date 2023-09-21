package org.sudu.experiments.parser.common.graph.writer;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.common.graph.node.decl.*;
import org.sudu.experiments.parser.common.graph.type.Type;

import java.util.IdentityHashMap;
import java.util.List;

import static org.sudu.experiments.parser.common.graph.ScopeGraphConstants.Decls.*;

public class DeclNodeWriter {

  private final ArrayWriter writer;
  private final StringBuilder declStringBuilder;
  private final IdentityHashMap<Type, Integer> typeIdentityMap;

  public DeclNodeWriter(
      ArrayWriter writer,
      StringBuilder declStringBuilder,
      IdentityHashMap<Type, Integer> typeIdentityMap
  ) {
    this.writer = writer;
    this.declStringBuilder = declStringBuilder;
    this.typeIdentityMap = typeIdentityMap;
  }

  void writeDeclNodes(ScopeNode scope) {
    writer.write(scope.declList.size());
    scope.declList.forEach(this::writeDeclNode);
  }

  private void writeDeclNode(DeclNode declNode) {
    if (declNode instanceof ArgNode argNode) writeArgDecl(argNode);
    else if (declNode instanceof CreatorNode creatorNode) writeCreatorDecl(creatorNode);
    else if (declNode instanceof FieldNode fieldNode) writeFieldDecl(fieldNode);
    else if (declNode instanceof MethodNode methodNode) writeMethodDecl(methodNode);
    else if (declNode instanceof VarNode varNode) writeVarNode(varNode);
    else writeDecl(declNode);
  }

  private void writeDeclName(DeclNode node) {
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

  private void writeArgDecl(ArgNode node) {
    writer.write(ARG_DECL_NODE);
    writeDeclName(node);
  }

  private void writeCreatorDecl(CreatorNode node) {
    writer.write(CREATOR_DECL_NODE);
    writeDeclName(node);
    writeArgs(node.args);
  }

  private void writeDecl(DeclNode node) {
    writer.write(BASE_DECL_NODE);
    writeDeclName(node);
  }

  private void writeFieldDecl(FieldNode node) {
    writer.write(FIELD_DECL_NODE);
    writeDeclName(node);
  }

  private void writeMethodDecl(MethodNode node) {
    writer.write(METHOD_DECL_NODE);
    writeDeclName(node);
    writeArgs(node.args);
  }

  private void writeVarNode(VarNode node) {
    writer.write(VAR_DECL_NODE);
    writeDeclName(node);
  }

  private void writeArgs(List<ArgNode> argNodeList) {
    writer.write(argNodeList.size());
    argNodeList.forEach(this::writeArgDecl);
  }
}
