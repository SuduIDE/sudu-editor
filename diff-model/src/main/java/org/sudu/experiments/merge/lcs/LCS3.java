package org.sudu.experiments.merge.lcs;

import org.sudu.experiments.diff.lcs.LCS;
import org.sudu.experiments.merge.ranges.BaseRange3;
import org.sudu.experiments.merge.ranges.CommonRange3;
import org.sudu.experiments.merge.ranges.Diff3;

import java.util.ArrayList;
import java.util.List;

public abstract class LCS3 {

  private final int[][] L, M, R;

  public LCS3(int[][] L, int[][] M, int[][] R) {
    this.L = L;
    this.M = M;
    this.R = R;
  }

  private int[][] findCommon() {
    LCS lmLCS = getLCS(L, M);
    int[][] lmCommon = lmLCS.findCommon();
    LCS lmrLCS = getLCS(lmCommon, R);
    return lmrLCS.findCommon();
  }

  public <S> List<BaseRange3<S>> countRanges(
      S[] objL, S[] objM, S[] objR,
      int start, int endCut
  ) {
    List<BaseRange3<S>> ranges = new ArrayList<>();
    if (start != 0) ranges.add(new CommonRange3<>(0, 0, 0, start));

    int[][] common = findCommon();
    int commonPtr = 0,
        leftPtr = start,
        midPtr = start,
        rightPtr = start;

    Diff3<S> currentDiff = null;
    CommonRange3<S> currentCommon = null;

    while (commonPtr < common.length) {
      S cS = objL[common[commonPtr][1]];
      S cL = objL[leftPtr], cM = objM[midPtr], cR = objR[rightPtr];

      if (equals(cL, cM, cR)) {
        if (currentCommon == null) currentCommon = new CommonRange3<>(leftPtr, midPtr, rightPtr);
        if (currentDiff != null) ranges.add(currentDiff);
        commonPtr++;
        leftPtr++;
        midPtr++;
        rightPtr++;
        currentCommon.length++;
        currentDiff = null;
        continue;
      }

      if (currentDiff == null) currentDiff = new Diff3<>(leftPtr, midPtr, rightPtr);
      if (currentCommon != null) ranges.add(currentCommon);
      currentCommon = null;

      if (!equals(cS, cL)) {
        currentDiff.diffL.add(objL[leftPtr]);
        leftPtr++;
      }
      if (!equals(cS, cM)) {
        currentDiff.diffM.add(objM[midPtr]);
        midPtr++;
      }
      if (!equals(cS, cR)) {
        currentDiff.diffR.add(objR[rightPtr]);
        rightPtr++;
      }
    }
    if (currentCommon != null) ranges.add(currentCommon);
    if (currentDiff == null) currentDiff = new Diff3<>(leftPtr, midPtr, rightPtr);

    for (; leftPtr < objL.length - endCut; leftPtr++) {
      S cL = objL[leftPtr];
      currentDiff.diffL.add(cL);
    }
    for (; midPtr < objM.length - endCut; midPtr++) {
      S cM = objM[midPtr];
      currentDiff.diffM.add(cM);
    }
    for (; rightPtr < objR.length - endCut; rightPtr++) {
      S cR = objR[rightPtr];
      currentDiff.diffR.add(cR);
    }
    if (currentDiff != null && currentDiff.isNotEmpty()) ranges.add(currentDiff);
    if (endCut != 0) {
      var lastCommon = new CommonRange3<S>(
          objL.length - endCut,
          objM.length - endCut,
          objR.length - endCut, endCut
      );
      ranges.add(lastCommon);
    }
    return ranges;
  }

  protected abstract LCS getLCS(int[][] L, int[][] R);

  public static <S> boolean equals(S a, S b) {
    return a.equals(b);
  }

  public static <S> boolean equals(S a, S b, S c) {
    return equals(a, b) && equals(b, c);
  }
}
