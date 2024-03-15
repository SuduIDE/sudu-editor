package org.sudu.experiments.diff.folder;

public class RangeCtx {

  private int curCtxId = 0;

  public int nextId() {
    return curCtxId++;
  }

  public void set(int id) {
    curCtxId = id;
  }

  public void clear() {
    curCtxId = 0;
  }
}
