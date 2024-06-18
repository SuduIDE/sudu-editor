package org.sudu.experiments.merge;

import org.sudu.experiments.diff.*;
import org.sudu.experiments.diff.ranges.BaseRange;
import org.sudu.experiments.diff.ranges.Diff;
import org.sudu.experiments.merge.lcs.LCS3;
import org.sudu.experiments.merge.lcs.Myers3LCS;
import org.sudu.experiments.merge.ranges.BaseRange3;
import org.sudu.experiments.merge.ranges.CommonRange3;
import org.sudu.experiments.merge.ranges.Diff3;
import org.sudu.experiments.utils.Enumerator;
import org.sudu.experiments.utils.Utils;

import java.util.Collections;
import java.util.List;

public class MergeModel {

  public LineDiff[] lineDiffsL, lineDiffsM, lineDiffsR;
  public List<BaseRange3<CodeLineS>> linesRanges;

  void findLinesMerge(
      CodeLineS[] docL,
      CodeLineS[] docM,
      CodeLineS[] docR
  ) {
    lineDiffsL = new LineDiff[docL.length];
    lineDiffsM = new LineDiff[docM.length];
    lineDiffsR = new LineDiff[docR.length];

    DiffModel.prepare(docL);
    DiffModel.prepare(docM);
    DiffModel.prepare(docR);

    linesRanges = countRanges(docL, docM, docR);
    for (var range : linesRanges) {
      if (!(range instanceof Diff3<CodeLineS> diff)) continue;
      handleMergeDiff(diff);
    }
  }

  private void handleMergeDiff(Diff3<CodeLineS> diff) {
    int type = diff.getType();
    switch (type) {
      case MergeRangeTypes.LEFT_INSERT,
           MergeRangeTypes.RIGHT_INSERT -> handleInsert(diff, type);
      case MergeRangeTypes.LEFT_DELETE,
           MergeRangeTypes.RIGHT_DELETE -> handleDelete(diff, type);
      case MergeRangeTypes.LEFT_EDITED,
           MergeRangeTypes.RIGHT_EDITED -> handleEdit(diff, type);
      case MergeRangeTypes.CONFLICTING -> handleConflicting(diff);
      default -> throw new IllegalStateException();
    }
  }

  private void handleDelete(Diff3<CodeLineS> diff, int type) {
  }

  private void handleElemDelete(Diff3<CodeLineS> diff, int type) {
  }

  private void handleInsert(Diff3<CodeLineS> diff, int type) {
    LineDiff[] lineDiffs;
    List<CodeLineS> diffList;
    if (type == MergeRangeTypes.LEFT_INSERT) {
      lineDiffs = lineDiffsL;
      diffList = diff.diffL;
    } else if (type == MergeRangeTypes.RIGHT_INSERT) {
      lineDiffs = lineDiffsR;
      diffList = diff.diffR;
    } else throw new IllegalStateException();
    diffList.forEach(it -> lineDiffs[it.lineNum].type = MergeLineTypes.INSERTED);
  }

  private void handleElemInsert(Diff3<CodeElementS> diff, int type) {
    LineDiff[] lineDiffs;
    List<CodeElementS> diffList;
    if (type == MergeRangeTypes.LEFT_INSERT) {
      lineDiffs = lineDiffsL;
      diffList = diff.diffL;
    } else if (type == MergeRangeTypes.RIGHT_INSERT) {
      lineDiffs = lineDiffsR;
      diffList = diff.diffR;
    } else throw new IllegalStateException();
    diffList.forEach(it -> lineDiffs[it.lineNum].elementTypes[it.elemNum] = MergeLineTypes.INSERTED);
  }

  private void handleEdit(Diff3<CodeLineS> diff, int type) {
    LineDiff[] lineDiffs;
    List<CodeLineS> diffList;
    if (type == MergeRangeTypes.LEFT_EDITED) {
      lineDiffs = lineDiffsL;
      diffList = diff.diffL;
    } else if (type == MergeRangeTypes.RIGHT_EDITED) {
      lineDiffs = lineDiffsR;
      diffList = diff.diffR;
    } else throw new IllegalStateException();
    diffList.forEach(it -> lineDiffs[it.lineNum].type = MergeLineTypes.EDITED);
    diff.diffM.forEach(it -> lineDiffsM[it.lineNum].type = MergeLineTypes.EDITED);

    var elementsLR = DiffModel.flatElements(diffList);
    var elementsM = DiffModel.flatElements(diff.diffM);
    var elementsDiffs = DiffModel.countRanges(elementsLR, elementsM);
    elementsDiffs.forEach(it -> handleElemDiff(lineDiffs, it));
  }

  private void handleConflicting(Diff3<CodeLineS> diff) {
    var elemsL = DiffModel.flatElements(diff.diffL);
    var elemsM = DiffModel.flatElements(diff.diffM);
    var elemsR = DiffModel.flatElements(diff.diffR);
    var elementsDiff = MergeModel.countRanges(elemsL, elemsM, elemsR);

    boolean isConflict = false;
    for (var elRange : elementsDiff) {
      if (!(elRange instanceof Diff3<CodeElementS> elDiff)) continue;
      int type = elDiff.getType();
      if (type == MergeRangeTypes.CONFLICTING) {
        isConflict = true;
        elDiff.diffL.forEach(elem -> lineDiffsL[elem.lineNum].elementTypes[elem.elemNum] = MergeLineTypes.CONFLICTING);
        elDiff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = MergeLineTypes.CONFLICTING);
        elDiff.diffR.forEach(elem -> lineDiffsR[elem.lineNum].elementTypes[elem.elemNum] = MergeLineTypes.CONFLICTING);
      }
    }
    int lineType = isConflict ? MergeLineTypes.CONFLICTING : MergeLineTypes.EDITED;
    if (!isConflict) diff.setType(MergeRangeTypes.LEFT_RIGHT_EDITED);
    diff.diffL.forEach(line -> lineDiffsL[line.lineNum].type = lineType);
    diff.diffM.forEach(line -> lineDiffsM[line.lineNum].type = lineType);
    diff.diffR.forEach(line -> lineDiffsR[line.lineNum].type = lineType);
  }

  public void handleElemDiff(LineDiff[] lineDiffs, BaseRange<CodeElementS> elRange) {
    if (!(elRange instanceof Diff<CodeElementS> diff)) return;
    diff.diffN.forEach(elem -> lineDiffs[elem.lineNum].elementTypes[elem.elemNum] = MergeLineTypes.EDITED);
    diff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = MergeLineTypes.EDITED);
  }

  public static <S> List<BaseRange3<S>> countRanges(S[] L, S[] M, S[] R) {
    int lLen = L.length, mLen = M.length, rLen = R.length;
    int minLen = Math.min(Math.min(lLen, rLen), mLen);
    int start = 0, endCut = 0;
    for (; start < minLen && equals(L[start], M[start], R[start]); start++) ;
    for (; endCut < minLen - start && equals(L[lLen - endCut - 1], M[mLen - endCut - 1], R[rLen - endCut - 1]); endCut++) ;
    if (equals(lLen, rLen, mLen) && start == minLen) return singleCommon(minLen);

    var enumerator = new Enumerator<>();
    var prepL = enumerator.enumerateWithPositions(L, start, endCut);
    var prepM = enumerator.enumerateWithPositions(M, start, endCut);
    var prepR = enumerator.enumerateWithPositions(R, start, endCut);
    var discardedLMR = Utils.dropUnique(prepL, prepM, prepR, enumerator.counter);

    LCS3 lcs3 = new Myers3LCS(discardedLMR[0], discardedLMR[1], discardedLMR[2]);
    return lcs3.countRanges(L, M, R, start, endCut);
  }

  private static <S> List<BaseRange3<S>> singleCommon(int len) {
    return Collections.singletonList(new CommonRange3<>(0, 0, 0, len));
  }

  private static <S> boolean equals(S a, S b, S c) {
    return LCS3.equals(a, b, c);
  }
}
