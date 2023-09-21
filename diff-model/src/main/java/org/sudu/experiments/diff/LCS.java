package org.sudu.experiments.diff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <S> can be CodeElementS or CodeLineS
 */
public class LCS<S> {

  private final S[] L, R;
  public List<BaseRange<S>> ranges;

  public LCS(S[] L, S[] R) {
    this.L = L;
    this.R = R;
  }

  public int[][] countLCSMatrix() {
    int[][] matrix = new int[L.length + 1][R.length + 1];
    for (int i = 1; i < L.length + 1; i++) {
      for (int j = 1; j < R.length + 1; j++) {
        if (L[i - 1].equals(R[j - 1])) {
          matrix[i][j] = 1 + matrix[i - 1][j - 1];
        } else {
          matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
        }
      }
    }
    return matrix;
  }

  public List<S> findCommon(int[][] matrix) {
    int i = L.length, j = R.length;
    LinkedList<S> common = new LinkedList<>();

    while (i > 0 && j > 0) {
      if (L[i - 1].equals(R[j - 1])) {
        common.addFirst(L[i - 1]);
        i--;
        j--;
      } else {
        if (matrix[i - 1][j] > matrix[i][j - 1]) i--;
        else j--;
      }
    }
    return new ArrayList<>(common);
  }

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
      if (cS.equals(cL) && cS.equals(cR)) {
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

      if (cS.equals(cL)) {
        currentDiff.diffM.add(cR);
        rightPtr++;
      } else if (cS.equals(cR)) {
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

}