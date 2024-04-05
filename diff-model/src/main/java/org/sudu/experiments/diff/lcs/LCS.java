package org.sudu.experiments.diff.lcs;

import org.sudu.experiments.diff.BaseRange;
import org.sudu.experiments.diff.CommonRange;
import org.sudu.experiments.diff.Diff;

import java.util.ArrayList;
import java.util.List;

public abstract class LCS<S> {

  protected final S[] L, R;
  public List<BaseRange<S>> ranges;

  public LCS(S[] L, S[] R) {
    this.L = L;
    this.R = R;
  }

  public abstract List<S> findCommon();

  public void countDiffs(List<S> common) {
    this.ranges = new ArrayList<>();
    int commonPtr = 0,
        leftPtr = 0,
        rightPtr = 0;

    Diff<S> currentDiff = null;
    CommonRange<S> currentCommon = null;
    while (commonPtr < common.size()
        && leftPtr < L.length
        && rightPtr < R.length
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

    for (; leftPtr < L.length && rightPtr < R.length; leftPtr++, rightPtr++) {
      S cL = L[leftPtr], cR = R[rightPtr];
      currentDiff.diffN.add(cL);
      currentDiff.diffM.add(cR);
    }
    for (; leftPtr < L.length; leftPtr++) {
      S cL = L[leftPtr];
      currentDiff.diffN.add(cL);
    }
    for (; rightPtr < R.length; rightPtr++) {
      S cR = R[rightPtr];
      currentDiff.diffM.add(cR);
    }
    if (currentDiff != null && currentDiff.isNotEmpty()) ranges.add(currentDiff);
  }

  public void countAll() {
    countDiffs(findCommon());
  }

  protected boolean equals(S a, S b) {
    return a.equals(b);
  }
}
