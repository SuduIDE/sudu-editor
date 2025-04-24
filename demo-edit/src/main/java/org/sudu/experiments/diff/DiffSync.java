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

    //  in compact view: convert syncLine -> docLine
//    syncLine = model.codeMappingL.viewToDoc(syncLine);
    var fromRange = model.range(syncLine, isLeft);
    //  in compact view: convert fromRange -> viewLine
//    syncLine = model.codeMappingL.docToView(syncLine);

    int rangeDelta = syncLine - (isLeft ? fromRange.fromL : fromRange.fromR);
    // this used to be
    //      lineHeight * line - vScrollPos;
    int scrollDelta = -(from.lineToPos(fromFirstLine) - from.pos().y);
    int toRangeStart = isLeft ? fromRange.fromR : fromRange.fromL;
    int toNewLine = (toRangeStart + rangeDelta - linesDelta);
    to.setVScrollPosSilent(toNewLine * to.lineHeight() + scrollDelta);
  }
}
