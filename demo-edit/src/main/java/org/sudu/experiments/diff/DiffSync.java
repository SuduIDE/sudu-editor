package org.sudu.experiments.diff;

import org.sudu.experiments.editor.CodeLineMapping;
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
    var fromCodeMapping = isLeft ? model.codeMappingL : model.codeMappingR;
    var toCodeMapping = !isLeft ? model.codeMappingL : model.codeMappingR;
    var fromCvr = isLeft ? model.cvrL : model.cvrR;
    var toCvr = !isLeft ? model.cvrL : model.cvrR;

    int fromFirstLine = from.getFirstLine();
    int fromLastLine = from.getLastLine();
    int viewSyncLine = (fromLastLine + fromFirstLine) / 2;
    int docSyncLine = viewSyncLine;
    if (fromCodeMapping != null) {
      int ind = fromCodeMapping.viewToDoc(viewSyncLine);
      if (ind > CodeLineMapping.outOfRange) {
        docSyncLine = ind;
      } else if (ind < CodeLineMapping.outOfRange) {
        int regionInd = CodeLineMapping.regionIndex(ind);
        docSyncLine = fromCvr[regionInd].startLine;
      }
    }

    //  in compact view: convert syncLine -> docLine
//    syncLine = model.codeMappingL.viewToDoc(syncLine);
    int rangeInd = model.rangeBinSearch(docSyncLine, isLeft);
    var range = model.ranges[rangeInd];
    //  in compact view: convert fromRange -> viewLine
//    syncLine = model.codeMappingL.docToView(syncLine);

    int viewLinesDelta = viewSyncLine - fromFirstLine;
    int docRangeDelta = docSyncLine - (isLeft ? range.fromL : range.fromR);
    // this used to be
    //      lineHeight * line - vScrollPos;
    int scrollDelta = -((from.lineToPos(fromFirstLine) - from.pos().y));
    DiffRange toRange = range;
    if (range.type != DiffTypes.DEFAULT && range.type != DiffTypes.EDITED) {
      if (rangeInd > 0) {
        var prevRange = model.ranges[rangeInd - 1];
        if (prevRange.type != DiffTypes.DEFAULT && prevRange.type != DiffTypes.EDITED) toRange = prevRange;
      }
    }

    int toRangeStart = isLeft ? toRange.fromR : toRange.fromL;
    int toDocFirstLine = toRangeStart + docRangeDelta;
    int toViewFirstLine = toDocFirstLine;
    if (toCodeMapping != null) {
      int ind = toCodeMapping.docToView(toDocFirstLine);
      if (ind > CodeLineMapping.outOfRange) {
        toViewFirstLine = ind;
      } else if (ind < CodeLineMapping.outOfRange) {
        int regionInd = CodeLineMapping.regionIndex(ind);
        int fstLine = toCodeMapping.docToView(toCvr[regionInd].endLine);
        if (fstLine > CodeLineMapping.outOfRange) toViewFirstLine = fstLine - 1;
      }
    }
    int toNewLine = (toViewFirstLine - viewLinesDelta);
    to.setVScrollPosSilent(toNewLine * to.lineHeight() + scrollDelta);
  }
}
