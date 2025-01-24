package org.sudu.experiments.diff;

import org.sudu.experiments.editor.DiffRef;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;

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
    int rangeInd = model.rangeBinSearch(syncLine, isLeft);
    var range = model.ranges[rangeInd];
    DiffRange toRange = range;
    if (range.type != DiffTypes.DEFAULT && range.type != DiffTypes.EDITED) {
      if (rangeInd > 0) {
        var prevRange = model.ranges[rangeInd - 1];
        if (prevRange.type != DiffTypes.DEFAULT && prevRange.type != DiffTypes.EDITED) toRange = prevRange;
      }
    }

    int fromLine = (isLeft ? range.fromL : range.fromR);
    int fromLen = (isLeft ? range.lenL : range.lenR);
    int toLine = (!isLeft ? toRange.fromL : toRange.fromR);
    int toLen = (!isLeft ? toRange.lenL : toRange.lenR);
    //  in compact view: convert fromRange -> viewLine
//    syncLine = model.codeMappingL.docToView(syncLine);

    int toNewLine = toLine - linesDelta;
    if (range.type == DiffTypes.EDITED) {
      float posInRange = (float) (syncLine - fromLine) / fromLen;
      toNewLine += (int) (posInRange * toLen);
    } else {
      toNewLine += syncLine - fromLine;
    }

    int scrollDelta = -(from.lineToPos(fromFirstLine) - from.pos().y);
    to.setVScrollPosSilent(toNewLine * to.lineHeight() + scrollDelta);
  }
}
