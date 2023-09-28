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
    writer.write(scope.declarations.size());
    scope.declarations.forEach(this::writeDeclNode);
  }

  void writeDeclNode(DeclNode declNode) {
    if (declNode instanceof MethodNode methodNode) writeMethodDecl(methodNode);
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

  private void writeDecl(DeclNode node) {
    writer.write(BASE_DECL_NODE);
    writeDeclName(node);
    writer.write(node.declType);
  }

  private void writeMethodDecl(MethodNode node) {
    writer.write(METHOD_DECL_NODE);
    writeDeclName(node);
    writer.write(node.callType);
    writeArgs(node.argTypes);
  }

  private void writeArgs(List<Type> typeList) {
    writer.write(typeList.size());
    typeList.forEach(this::writeType);
  }
}
