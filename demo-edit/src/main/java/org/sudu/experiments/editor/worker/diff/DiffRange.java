package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.diff.DiffTypes;

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

  public int from(boolean isLeft) { return isLeft ? fromL : fromR; }

  public int to(boolean isLeft) { return isLeft ? toL() : toR(); }

  public int len(boolean isLeft) { return isLeft ? lenL : lenR; }

  public final int toL() { return fromL + lenL; }

  public final int toR() { return fromR + lenR; }

  public boolean inside(int line, boolean left) {
    return from(left) <= line && line < to(left);
  }

  @Override
  public String toString() {
    return String.format("[%d: %d) |-> [%d: %d) %s",
        fromL, toL(),
        fromR, toR(),
        DiffTypes.name(type));
  }
}
