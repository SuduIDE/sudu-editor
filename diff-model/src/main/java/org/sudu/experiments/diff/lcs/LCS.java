package org.sudu.experiments.diff.lcs;

import org.sudu.experiments.diff.BaseRange;
import org.sudu.experiments.diff.CommonRange;
import org.sudu.experiments.diff.Diff;

import java.util.ArrayList;
import java.util.List;

public abstract class LCS<S> {

  protected final S[] L, R;
  protected int minLen;
  protected int lLen, rLen;
  protected int start = 0, end = 0;
  public List<BaseRange<S>> ranges;

  public LCS(S[] L, S[] R) {
    this.L = L;
    this.R = R;
    this.lLen = L.length;
    this.rLen = R.length;
    this.minLen = Math.min(lLen, rLen);
  }

  public void preprocess() {
    for (;start < minLen && equals(L[start], R[start]); start++);
    for (;end < minLen - start && equals(L[lLen - end - 1], R[rLen - end - 1]); end++);
  }

  protected abstract List<S> findCommon();

  public void countDiffs(List<S> common) {
    this.ranges = new ArrayList<>();
    int commonPtr = 0,
        leftPtr = start,
        rightPtr = start;

    Diff<S> currentDiff = null;
    CommonRange<S> currentCommon = null;
    if (start != 0) ranges.add(new CommonRange<>(0, 0, start));
    while (commonPtr < common.size()
        && leftPtr < L.length - end
        && rightPtr < R.length - end
    ) {
      S cS = common.get(commonPtr);
      S cL = L[leftPtr], cR = R[rightPtr];
      if (equals(cS, cL) && equals(cS, cR)) {
        if (currentCommon == null) currentCommon = new CommonRange<>(leftPtr, rightPtr);
        if (currentDiff != null) this.ranges.add(currentDiff);
        commonPtr++;
        leftPtr++;
        rightPtr++;
        currentCommon.length++;
        currentDiff = null;
        continue;
      }
      if (currentDiff == null) currentDiff = new Diff<>(leftPtr, rightPtr);
      if (currentCommon != null) this.ranges.add(currentCommon);
      currentCommon = null;

      if (equals(cS, cL)) {
        currentDiff.diffM.add(cR);
        rightPtr++;
      } else if (equals(cS, cR)) {
        currentDiff.diffN.add(cL);
        leftPtr++;
      } else {
        currentDiff.diffN.add(L[leftPtr]);
        currentDiff.diffM.add(R[rightPtr]);
        leftPtr++;
        rightPtr++;
      }
    }
    if (currentCommon != null) ranges.add(currentCommon);
    if (currentDiff == null) currentDiff = new Diff<>(leftPtr, rightPtr);

    for (; leftPtr < L.length - end && rightPtr < R.length - end; leftPtr++, rightPtr++) {
      S cL = L[leftPtr], cR = R[rightPtr];
      currentDiff.diffN.add(cL);
      currentDiff.diffM.add(cR);
    }
    for (; leftPtr < L.length - end; leftPtr++) {
      S cL = L[leftPtr];
      currentDiff.diffN.add(cL);
    }
    for (; rightPtr < R.length - end; rightPtr++) {
      S cR = R[rightPtr];
      currentDiff.diffM.add(cR);
    }
    if (currentDiff != null && currentDiff.isNotEmpty()) ranges.add(currentDiff);
    if (end != 0) ranges.add(new CommonRange<>(lLen - end, rLen - end, end));
  }

  public void countAll() {
    preprocess();
    countDiffs(findCommon());
  }

  protected boolean equals(S a, S b) {
    return a.equals(b);
  }
}
