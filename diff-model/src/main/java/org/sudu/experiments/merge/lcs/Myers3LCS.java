package org.sudu.experiments.merge.lcs;

import org.sudu.experiments.diff.lcs.LCS;
import org.sudu.experiments.diff.lcs.MyersLCS;

public class Myers3LCS extends LCS3 {

  public Myers3LCS(int[][] L, int[][] M, int[][] R) {
    super(L, M, R);
  }

  @Override
  protected LCS getLCS(int[][] L, int[][] R) {
    return new MyersLCS(L, R);
  }
}
