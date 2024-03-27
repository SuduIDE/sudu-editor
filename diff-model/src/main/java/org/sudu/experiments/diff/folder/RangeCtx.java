package org.sudu.experiments.diff.folder;

import org.sudu.experiments.diff.DiffTypes;

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

  public void markUp(FolderDiffModel left, FolderDiffModel right) {
    int rangeId = this.nextId();
    left.markUp(DiffTypes.EDITED, this);
    this.set(rangeId + 1);
    right.markUp(DiffTypes.EDITED, this);
  }
}
