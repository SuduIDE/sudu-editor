package org.sudu.experiments.parser.activity.graph.expr;

public enum ExprKind {
  And("and"),
  Xor("xor"),
  Or("or"),
  Unknown("<UNKNOWN-OP>");

  final String op;

  ExprKind(String op) {
    this.op = op;
  }
}
