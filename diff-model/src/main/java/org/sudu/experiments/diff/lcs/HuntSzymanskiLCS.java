package org.sudu.experiments.diff.lcs;

import org.sudu.experiments.utils.Utils;
import java.util.*;

/**
 * Time complexity – O((r+n)logn)
 * Space complexity – O(r+n)
 * r - number of matches of sequences. r = n*n in worst case
 * Works well if the sequences are very different
 */
public class HuntSzymanskiLCS extends LCS {

  private final int maxElem;

  public HuntSzymanskiLCS(int[][] L, int[][] R, int maxElem) {
    super(L, R);
    this.maxElem = maxElem;
  }

  @Override
  public int[][] findCommon() {
    List<Integer>[] rMatchList = getMatchList();
    int start = 0;
    int end = 0;
    int[] thresh = new int[minLen - start - end + 1];
    Link[] linkL = new Link[minLen - start - end + 1];
    Arrays.fill(thresh,rLen - end);
    thresh[0] = -1;
    int t = 0, m = 0;

    for (int ind = start; ind < lLen - end; ind++) {
      int elem = valL(ind);
      var yIndices = rMatchList[elem];
      if (yIndices == null) continue;
      for (int i = yIndices.size() - 1; i >= 0; i--) {
        int yInd = yIndices.get(i);
        int kStart = bisectLeft(thresh, yInd, 1, t);
        int kEnd = bisectRight(thresh, yInd, kStart, t);
        for (int k = kStart; k < kEnd + 2; k++) {
          if (thresh[k - 1] < yInd && yInd < thresh[k]) {
            thresh[k] = yInd;
            linkL[k] = new Link(ind, linkL[k - 1]);
            if (k > m) m = k;
          }
          if (k > t) t = k;
        }
      }
    }
    return Utils.toIntIntArray(fillResult(linkL[m]));
  }

  private LinkedList<int[]> fillResult(Link linkL) {
    LinkedList<int[]> common = new LinkedList<>();
    var cur = linkL;
    while (cur != null) {
      common.addFirst(L[cur.ind]);
      cur = cur.link;
    }
    return common;
  }

  private List<Integer>[] getMatchList() {
    int start = 0;
    int end = 0;
    List<Integer>[] rMatchList = new List[maxElem];
    for (int i = start; i < rLen - end; i++) {
      int val = valR(i);
      if (rMatchList[val] == null) rMatchList[val] = new ArrayList<>();
      rMatchList[val].add(i);
    }
    return rMatchList;
  }

  private static int bisectLeft(int[] arr, int val, int from, int to) {
    if (from > to) return from;
    int k = Arrays.binarySearch(arr, from, to, val);
    if (k < 0) k = -(k + 1);
    return k;
  }

  private static int bisectRight(int[] A, int val, int from, int to) {
    if (from > to) return from;
    while (from < to) {
      int mid = (from + to) >>> 1;
      if (val < A[mid]) to = mid;
      else from = mid + 1;
    }
    return from;
  }

  private static class Link {
    int ind;
    Link link;

    public Link(int ind, Link link) {
      this.ind = ind;
      this.link = link;
    }

    @Override
    public String toString() {
      return "(" + ind + ", " + link + ")";
    }
  }
}
