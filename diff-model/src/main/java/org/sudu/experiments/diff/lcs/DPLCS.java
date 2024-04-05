package org.sudu.experiments.diff.lcs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Time complexity – O(nm)
 * Space complexity – O(nm)
 */
public class DPLCS<S> extends LCS<S> {

  public DPLCS(S[] L, S[] R) {
    super(L, R);
  }

  @Override
   protected List<S> findCommon() {
    short[][] matrix = countLCSMatrix();
    int i = L.length - end, j = R.length - end;
    LinkedList<S> common = new LinkedList<>();

    while (i > start && j > start) {
      if (equals(L[i - 1], R[j - 1])) {
        common.addFirst(L[i - 1]);
        i--;
        j--;
      } else {
        if (matrix[i - 1 - start][j - start] > matrix[i - start][j - 1 - start]) i--;
        else j--;
      }
    }
    return new ArrayList<>(common);
  }

  public short[][] countLCSMatrix() {
    int d = start + end;
    short[][] matrix = new short[L.length + 1 - d][R.length + 1 - d];
    for (int i = 1; i < L.length + 1 - d; i++) {
      for (int j = 1; j < R.length + 1 - d; j++) {
        if (equals(L[start + i - 1], R[start + j - 1])) {
          matrix[i][j] = (short) (1 + matrix[i - 1][j - 1]);
        } else {
          matrix[i][j] = (short) Math.max(matrix[i - 1][j], matrix[i][j - 1]);
        }
      }
    }
    return matrix;
  }
}