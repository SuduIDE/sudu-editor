package org.sudu.experiments.diff.lcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Time complexity – O(nm)
 * Space complexity – O(n + m)
 * Take less memory than LCS. Have same time complexity as LCS, but practically is slower
 */
public class HirschbergLCS<S> extends LCS<S>{

  public HirschbergLCS(S[] L, S[] R) {
    super(L, R);
  }

  @Override
  public List<S> findCommon() {
    List<S> result = new ArrayList<>();
    findCommon(0, L.length, 0, R.length, result);
    return result;
  }

  private void findCommon(
      int fromL, int toL,
      int fromR, int toR,
      List<S> answer
  ) {
    int lenL = toL - fromL;
    int lenR = toR - fromR;
    if (lenL == 0) return;
    if (lenL == 1) {
      if (contains(R, L[fromL], fromR, toR)) answer.add(L[fromL]);
      return;
    }
    int m = fromL + lenL / 2;
    int[] lcs1 = computeLCS(fromL, m, fromR, toR);
    int[] lcs2 = computeRevLCS(m, toL, fromR, toR);
    int k = 0, max = 0;

    for (int i = 0; i < lenR + 1; i++) {
      if (lcs1[i] + lcs2[lenR - i] > max) {
        max = lcs1[i] + lcs2[lenR - i];
        k = i;
      }
    }
    findCommon(fromL, m, fromR, fromR + k, answer);
    findCommon(m, toL,fromR + k, toR, answer);
  }

  private int[] computeLCS(
      int fromL, int toL,
      int fromR, int toR
  ) {
    int n = toL - fromL;
    int m = toR - fromR;

    int[] prev = new int[m + 1];
    int[] curr = new int[m + 1];
    for (int i = 1; i < n + 1; i++) {
      for (int j = 1; j < m + 1; j++) {
        S l = L[fromL + i - 1];
        S r = R[fromR + j - 1];
        if (equals(l, r)) curr[j] = 1 + prev[j - 1];
        else curr[j] = Math.max(prev[j], curr[j - 1]);
      }
      prev = Arrays.copyOf(curr, curr.length);
    }
    return prev;
  }

  private int[] computeRevLCS(
      int fromL, int toL,
      int fromR, int toR
  ) {
    int n = toL - fromL;
    int m = toR - fromR;

    int[] prev = new int[m + 1];
    int[] curr = new int[m + 1];
    for (int i = 1; i < n + 1; i++) {
      for (int j = 1; j < m + 1; j++) {
        S l = L[toL - i];
        S r = R[toR - j];
        if (equals(l, r)) curr[j] = 1 + prev[j - 1];
        else curr[j] = Math.max(prev[j], curr[j - 1]);
      }
      prev = Arrays.copyOf(curr, curr.length);
    }
    return prev;
  }

  private boolean contains(S[] arr, S elem, int from, int to) {
    for (int i = from; i < to; i++) if (equals(arr[i], elem)) return true;
    return false;
  }
}