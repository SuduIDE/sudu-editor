package org.sudu.experiments.diff.lcs;

/**
 * Time complexity – O(nm)
 * Space complexity – O(nm)
 */
public class DPLCS extends LCS {

  public DPLCS(int[][] L, int[][] R) {
    super(L, R);
  }

  @Override
   public int[][] findCommon() {
    short[][] matrix = countLCSMatrix();
    int i = lLen, j = rLen;
    int ptr = matrix[i][j];
    int[][] common = new int[ptr][2];

    while (i > 0 && j > 0) {
      if (valL(i - 1) == valR(j - 1)) {
        common[--ptr] = L[i - 1];
        i--;
        j--;
      } else {
        if (matrix[i - 1][j] > matrix[i][j - 1]) i--;
        else j--;
      }
    }
    return common;
  }

  public short[][] countLCSMatrix() {
    short[][] matrix = new short[lLen + 1][rLen + 1];
    for (int i = 1; i < lLen + 1; i++) {
      for (int j = 1; j < rLen + 1; j++) {
        if (valL(i - 1) == valR(j - 1)) {
          matrix[i][j] = (short) (1 + matrix[i - 1][j - 1]);
        } else {
          matrix[i][j] = (short) Math.max(matrix[i - 1][j], matrix[i][j - 1]);
        }
      }
    }
    return matrix;
  }
}