package org.sudu.experiments.diff;

public class LineDiff {

  public static final int DEFAULT = 0;
  public static final int DELETED = 1;
  public static final int INSERTED = 2;
  public static final int EDITED = 3;

  public int type;
  public int[] elementTypes;

  public LineDiff(int type) {
    this.type = type;
  }

  public LineDiff(int type, int lineLen) {
    this.type = type;
    this.elementTypes = new int[lineLen];
  }

}
