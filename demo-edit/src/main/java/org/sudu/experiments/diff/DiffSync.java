package org.sudu.experiments.diff;

import org.sudu.experiments.editor.CodeLineMapping;
import org.sudu.experiments.editor.CompactViewRange;
import org.sudu.experiments.editor.DiffRef;
import org.sudu.experiments.editor.worker.diff.DiffInfo;

public class DiffSync {
  final DiffRef left, right;
  DiffInfo model;

  public DiffSync(DiffRef l, DiffRef r) {
    left = l;
    right = r;

    l.setScrollListeners(null, (delta) -> sync(delta, left, right));
    r.setScrollListeners(null, (delta) -> sync(delta, right, left));
  }

  public void setModel(DiffInfo model) {
    this.model = model;
  }

  void sync(int scrollDelta, DiffRef from, DiffRef to) {
    if (model == null || model.ranges == null) return;
    boolean isLeft = from == left;
    var fromCodeMapping = isLeft ? model.codeMappingL : model.codeMappingR;
    var toCodeMapping = !isLeft ? model.codeMappingL : model.codeMappingR;
    var fromCvr = isLeft ? model.cvrL : model.cvrR;
    var toCvr = !isLeft ? model.cvrL : model.cvrR;

    int viewFromFirstLine = from.getFirstLine();
    int viewFromLastLine = from.getLastLine();
    int viewToFirstLine = to.getFirstLine();
    int viewToLastLine = to.getLastLine();

    int viewFromSyncLine = (viewFromLastLine + viewFromFirstLine) / 2;
    int docFromSyncLine = viewToDoc(fromCodeMapping, fromCvr, viewFromSyncLine);

    int viewToSyncLine = (viewToLastLine + viewToFirstLine) / 2;
    int docToSyncLine = viewToDoc(toCodeMapping, toCvr, viewToSyncLine);

    int rangeInd = model.rangeBinSearch(docFromSyncLine, isLeft);
    var range = model.ranges[rangeInd];

    int fromScrollDelta = -((from.lineToPos(viewFromFirstLine) - from.pos().y));
    int toScrollDelta = -((to.lineToPos(viewToFirstLine) - to.pos().y));

    int viewFromRangeStart = (docToView(fromCodeMapping, fromCvr, isLeft ? range.fromL : range.fromR)
        - viewFromFirstLine) * from.lineHeight() + fromScrollDelta;
    int viewFromRangeEnd = (docToView(fromCodeMapping, fromCvr, isLeft ? range.toL() : range.toR())
        - viewFromFirstLine) * from.lineHeight() + fromScrollDelta;

    int viewToRangeStart = (docToView(toCodeMapping, toCvr, !isLeft ? range.fromL : range.fromR)
        - viewToFirstLine) * to.lineHeight() + toScrollDelta;
    int viewToRangeEnd = (docToView(toCodeMapping, toCvr, !isLeft ? range.toL() : range.toR())
        - viewToFirstLine) * to.lineHeight() + toScrollDelta;

    boolean isGoodFrom = containsIn(viewFromRangeStart, viewToRangeStart, viewToRangeEnd, viewFromRangeEnd);
    boolean isGoodTo = containsIn(viewToRangeStart, viewFromRangeStart, viewFromRangeEnd, viewToRangeEnd);

    int viewLinesDelta = viewFromSyncLine - viewFromFirstLine;
    int docRangeDelta = docFromSyncLine - (isLeft ? range.fromL : range.fromR);

    int toRangeStart = !isLeft ? range.fromL : range.fromR;
    int toDocFirstLine = toRangeStart + docRangeDelta;
    int toViewFirstLine = docToView(toCodeMapping, toCvr, toDocFirstLine);
    int toNewLine = (toViewFirstLine - viewLinesDelta);

    if (isGoodFrom) return;
    int toNeededScrollPos = toNewLine * to.lineHeight() + fromScrollDelta;
    int toCurrentScrollPos = -(to.lineToPos(viewToFirstLine) - viewToFirstLine * to.lineHeight() - to.pos().y);
    int neededDelta = (toNeededScrollPos - toCurrentScrollPos);
    if (Math.signum(scrollDelta) != Math.signum(neededDelta)) return;
    if (scrollDelta == 0) {
      to.setVScrollPosSilent(toNeededScrollPos);
    } else {
      int a = Math.abs(scrollDelta);
      int a2 = a * a;
      int b = Math.abs(neededDelta);
      boolean sign = neededDelta > 0;
      float q = (float) Math.log(Math.max(range.lenL, range.lenR));
      int x = Math.min(b, Math.max(
          a2, (int) (q * b / a)
      )) * (sign ? 1 : -1);
      to.setVScrollPosSilent(toCurrentScrollPos + x);
    }
  }

  private int viewToDoc(CodeLineMapping mapping, CompactViewRange[] cvr, int viewValue) {
    if (mapping == null || cvr == null) return viewValue;
    int ind = mapping.viewToDoc(viewValue);
    if (ind > CodeLineMapping.outOfRange) {
      return ind;
    } else if (ind < CodeLineMapping.outOfRange) {
      int regionInd = CodeLineMapping.regionIndex(ind);
      return cvr[regionInd].startLine;
    }
    return CodeLineMapping.outOfRange;
  }

  private int docToView(CodeLineMapping mapping, CompactViewRange[] cvr, int docValue) {
    if (mapping == null || cvr == null) return docValue;
    int ind = mapping.docToViewCursor(docValue);
    if (ind > CodeLineMapping.outOfRange) {
      return ind;
    } else if (ind < CodeLineMapping.outOfRange) {
      int regionInd = CodeLineMapping.regionIndex(ind);
      int fstLine = mapping.docToView(cvr[regionInd].endLine + 1);
      if (fstLine > CodeLineMapping.outOfRange) return fstLine - 1;
    }
    return CodeLineMapping.outOfRange;
  }

  private boolean containsIn(int x, int a, int b, int y) {
    return x <= a && b <= y;
  }
}
