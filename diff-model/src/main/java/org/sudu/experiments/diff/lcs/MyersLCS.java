package org.sudu.experiments.diff.lcs;

import java.util.Arrays;
import java.util.BitSet;

public class MyersLCS extends LCS {

  private final BitSet common;
  private int bitsetLen;

  private final int[] VForward;
  private final int[] VBackward;

  public MyersLCS(int[][] L, int[][] R) {
    super(L, R);
    this.common = new BitSet(lLen);
    this.bitsetLen = 0;

    int totalLen = L.length + R.length;
    this.VForward = new int[totalLen + 1];
    this.VBackward = new int[totalLen + 1];
  }

  @Override
  public int[][] findCommon() {
    int threshold = 20000 + 10 * (int) Math.sqrt(lLen + rLen);
    lcs(0, lLen, 0, rLen, Math.min(threshold, lLen + rLen));

    int[][] res = new int[bitsetLen][2];
    int ptr = 0;
    for (int i = 0; i < lLen; i++) {
      if (common.get(i)) res[ptr++] = L[i];
    }
    if (ptr == bitsetLen) return res;
    else return Arrays.copyOf(res, ptr);
  }

  private void lcs(
      int fromL, int toL,
      int fromR, int toR,
      int diff
  ) {
    if (!(fromL < toL && fromR < toR)) return;
    int lenL = toL - fromL;
    int lenR = toR - fromR;
    VForward[lenR + 1] = 0;
    VBackward[lenR + 1] = 0;

    int halfD = (diff + 1) / 2;
    int xx = -1, kk = -1, td = -1;

    loop:
    for (int d = 0; d <= halfD; ++d) {
      int L = lenR + Math.max(-d, -lenR + ((d ^ lenR) & 1));
      int R = lenR + Math.min(d, lenL - ((d ^ lenL) & 1));

      for (int k = L; k <= R; k += 2) {
        int x = k == L || k != R && VForward[k - 1] < VForward[k + 1] ? VForward[k + 1] : VForward[k - 1] + 1;
        int y = x - k + lenR;
        x += commonLenForward(fromL + x, fromR + y,
            Math.min(toL - fromL - x, toR - fromR - y));
        VForward[k] = x;
      }
      if ((lenL - lenR) % 2 != 0) {
        for (int k = L; k <= R; k += 2) {
          if (lenL - (d - 1) <= k && k <= lenL + (d - 1)) {
            if (VForward[k] + VBackward[lenR + lenL - k] >= lenL) {
              xx = VForward[k];
              kk = k;
              td = 2 * d - 1;
              break loop;
            }
          }
        }
      }
      for (int k = L; k <= R; k += 2) {
        int x = k == L || k != R && VBackward[k - 1] < VBackward[k + 1] ? VBackward[k + 1] : VBackward[k - 1] + 1;
        int y = x - k + lenR;
        x += commonLenBackward(
            toL - 1 - x, toR - 1 - y,
            Math.min(toL - fromL - x, toR - fromR - y));
        VBackward[k] = x;
      }
      if ((lenL - lenR) % 2 == 0) {
        for (int k = L; k <= R; k += 2) {
          if (lenL - d <= k && k <= lenL + d) {
            if (VForward[lenL + lenR - k] + VBackward[k] >= lenL) {
              xx = lenL - VBackward[k];
              kk = lenL + lenR - k;
              td = 2 * d;
              break loop;
            }
          }
        }
      }
    }

    if (td > 1) {
      int yy = xx - kk + lenR;
      int oldDiff = (td + 1) / 2;
      if (0 < xx && 0 < yy) lcs(fromL, fromL + xx, fromR, fromR + yy, oldDiff);
      if (fromL + xx < toL && fromR + yy < toR)
        lcs(fromL + xx, toL, fromR + yy, toR, td - oldDiff);
    } else if (td >= 0) {
      int x = fromL;
      int y = fromR;
      while (x < toL && y < toR) {
        int commonLen = commonLenForward(x, y, Math.min(toL - x, toR - y));
        if (commonLen > 0) {
          markCommon(x, commonLen);
          x += commonLen;
          y += commonLen;
        } else if (toL - fromL > toR - fromR) ++x;
        else ++y;
      }
    }
  }

  private int commonLenForward(int lInd, int rInd, int maxLen) {
    int x = lInd;
    int y = rInd;

    maxLen = Math.min(maxLen, Math.min(lLen - lInd, rLen - rInd));
    while (x - lInd < maxLen && valL(x) == valR(y)) {
      ++x;
      ++y;
    }
    return x - lInd;
  }

  private int commonLenBackward(int lInd, int rInd, int maxLen) {
    int x = lInd;
    int y = rInd;

    maxLen = Math.min(maxLen, Math.min(lInd, rInd) + 1);
    while (lInd - x < maxLen && valL(x) == valR(y)) {
      --x;
      --y;
    }
    return lInd - x;
  }

  private void markCommon(int fromL, int count) {
    common.set(fromL, fromL + count);
    bitsetLen += count;
  }
}
