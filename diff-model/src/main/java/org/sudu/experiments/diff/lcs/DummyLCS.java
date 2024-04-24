package org.sudu.experiments.diff.lcs;

/**
 * This is LCS for cases, when L.equals(R) after all preprocessing
 * Time complexity – O(n)
 * Space complexity – O(n + m)
 */
public class DummyLCS extends LCS {
  public DummyLCS(int[][] L, int[][] R) {
    super(L, R);
  }

  @Override
  protected int[] findCommon() {
    int[] common = new int[L.length];
    for (int i = 0; i < L.length; i++) common[i] = valL(i);
    return common;
  }
}
