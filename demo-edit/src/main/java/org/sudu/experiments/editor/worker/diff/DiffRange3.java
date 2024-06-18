package org.sudu.experiments.editor.worker.diff;

public class DiffRange3 extends DiffRange {

  public int fromM, lenM;

  public DiffRange3(
      int fromL, int lenL,
      int fromM, int lenM,
      int fromR, int lenR,
      int type
  ) {
    super(fromL, lenL, fromR, lenR, type);
    this.fromM = fromM;
    this.lenM = lenM;
  }
}
