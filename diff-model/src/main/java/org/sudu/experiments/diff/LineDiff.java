package org.sudu.experiments.diff;

public class LineDiff {

  public int type;
  public int[] elementTypes;

  public LineDiff(int type) {
    this.type = type;
  }

  public LineDiff seType(int type) {
    this.type = type;
    return this;
  }

  public LineDiff(int type, int lineLen) {
    this.type = type;
    this.elementTypes = new int[lineLen];
  }

  public boolean isDefault() {
    return type == DiffTypes.DEFAULT;
  }
}
