package org.sudu.experiments.diff;

import org.sudu.experiments.editor.DiffRef;
import org.sudu.experiments.editor.worker.diff.DiffInfo;

public class DiffSync {
  final DiffRef left, right;
  DiffInfo model;

  public DiffSync(DiffRef l, DiffRef r) {
    left = l;
    right = r;

    l.setScrollListeners(null, () -> sync(left, right));
    r.setScrollListeners(null, () -> sync(right, left));
  }

  public void setModel(DiffInfo model) {
    this.model = model;
  }

  void sync(DiffRef from, DiffRef to) {
    if (model == null || model.ranges == null) return;
    boolean isLeft = from == left;

    int fromFirstLine = from.getFirstLine();
    int fromLastLine = from.getLastLine();
    int syncLine = (fromLastLine + fromFirstLine) / 2;
    int linesDelta = syncLine - fromFirstLine;

    var range = model.range(syncLine, isLeft);
    int fromLine = (isLeft ? range.fromL : range.fromR);
    int fromLen = (isLeft ? range.lenL : range.lenR);
    int toLine = (!isLeft ? range.fromL : range.fromR);
    int toLen = (!isLeft ? range.lenL : range.lenR);

    float posInRange = (float) (syncLine - fromLine) / fromLen;

    int scrollDelta = -(from.lineToPos(fromFirstLine) - from.pos().y);
    int toNewLine = toLine + (int) (posInRange * toLen) - linesDelta;
    to.setVScrollPosSilent(toNewLine * to.lineHeight() + scrollDelta);
  }
}
