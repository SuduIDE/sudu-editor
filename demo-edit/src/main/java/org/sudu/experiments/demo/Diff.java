package org.sudu.experiments.demo;

public class Diff {
  int line, pos;
  boolean isDelete;
  String change;

  public Diff(int line, int pos, boolean isDelete, String change) {
    this.line = line;
    this.pos = pos;
    this.isDelete = isDelete;
    this.change = change;
  }

}
