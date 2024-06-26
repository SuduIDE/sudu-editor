package org.sudu.experiments.editor.worker.diff;

public class DiffRange {

  public int fromL, lenL;
  public int fromR, lenR;
  public int type;

  public DiffRange(int fromL, int lenL, int fromR, int lenR, int type) {
    this.fromL = fromL;
    this.lenL = lenL;
    this.fromR = fromR;
    this.lenR = lenR;
    this.type = type;
  }

  public final int toL() { return fromL + lenL; }

  public final int toR() { return fromR + lenR; }

  @Override
  public String toString() {
    return String.format("[%d: %d) |-> [%d: %d)", fromL, fromL + lenL, fromR, fromR + lenR);
  }
}
