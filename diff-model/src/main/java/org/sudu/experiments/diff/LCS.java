package org.sudu.experiments.diff;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <S> can be CodeElementS or CodeLineS
 */
public class LCS<S> {

  private final S[] N, M;
  public List<Diff<S>> diffs;

  public LCS(S[] N, S[] M) {
    this.N = N;
    this.M = M;
  }

  public int[][] countLCSMatrix() {
    int[][] matrix = new int[N.length + 1][M.length + 1];
    for (int i = 1; i < N.length + 1; i++) {
      for (int j = 1; j < M.length + 1; j++) {
        if (N[i - 1].equals(M[j - 1])) {
          matrix[i][j] = 1 + matrix[i - 1][j - 1];
        } else {
          matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
        }
      }
    }
    return matrix;
  }

  public List<S> findCommon(int[][] matrix) {
    int i = N.length, j = M.length;
    LinkedList<S> common = new LinkedList<>();

    while (i > 0 && j > 0) {
      if (N[i - 1].equals(M[j - 1])) {
        common.addFirst(N[i - 1]);
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
    this.diffs = new ArrayList<>();
    int i = 0, j = 0, k = 0;

    Diff<S> currentEditDiff = new Diff<>();
    while (i < common.size()
        && j < N.length
        && k < M.length
    ) {
      S cS = common.get(i);
      S cN = N[j], cM = M[k];
      if (cS.equals(cN) && cS.equals(cM)) {
        i++;
        j++;
        k++;
        if (currentEditDiff.isNotEmpty()) {
          diffs.add(currentEditDiff);
          currentEditDiff = new Diff<>();
        }
      } else if (cS.equals(cN)) {
        currentEditDiff.diffM.add(cM);
        k++;
      } else if (cS.equals(cM)) {
        currentEditDiff.diffN.add(cN);
        j++;
      } else {
        currentEditDiff.diffN.add(N[j]);
        currentEditDiff.diffM.add(M[k]);
        j++;
        k++;
      }
    }
    for (; j < N.length && k < M.length; j++, k++) {
      S cN = N[j], cM = M[k];
      currentEditDiff.diffN.add(cN);
      currentEditDiff.diffM.add(cM);
    }
    for (; j < N.length; j++) {
      S cN = N[j];
      currentEditDiff.diffN.add(cN);
    }
    for (; k < M.length; k++) {
      S cM = M[k];
      currentEditDiff.diffM.add(cM);
    }
    if (currentEditDiff.isNotEmpty()) diffs.add(currentEditDiff);
  }

}