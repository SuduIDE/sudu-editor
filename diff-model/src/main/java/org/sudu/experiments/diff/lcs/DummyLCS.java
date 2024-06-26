package org.sudu.experiments.diff.lcs;

/**
 * This is LCS for cases, when L.equals(R) after all preprocessing
 * Time complexity – O(1)
 * Space complexity – O(n + m)
 */
public class DummyLCS extends LCS {
  public DummyLCS(int[][] L, int[][] R) {
    super(L, null);
  }

  @Override
  public int[][] findCommon() {
    return L;
  }
}
