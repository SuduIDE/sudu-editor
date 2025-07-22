package org.sudu.experiments.editor;

import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

public class SyncPoints {

  public int[] syncL, syncR;              // doc line
  public int curL, curR;                  // doc line
  public int midLineHoverSyncPoint = -1;  // syncL & syncR index
  public int hoverSyncPoint = -1;

  private static final boolean DEBUG = false;

  public SyncPoints() {
    syncL = new int[0];
    syncR = new int[0];
    curL = curR = -1;
  }

  public boolean hasLeft(int i) {
    return curL == i || Arrays.binarySearch(syncL, i) >= 0;
  }

  public boolean hasRight(int i) {
    return curR == i || Arrays.binarySearch(syncR, i) >= 0;
  }

  public void removeLeft(int i) {
    if (i == curL) {
      curL = -1;
    } else {
      int p = Arrays.binarySearch(syncL, i);
      remove(p);
    }
  }

  public void removeRight(int i) {
    if (i == curR) {
      curR = -1;
    } else {
      int p = Arrays.binarySearch(syncR, i);
      remove(p);
    }
  }

  public void remove(int i) {
    if (i == -1) return;
    syncL = ArrayOp.removeAt(syncL, i);
    syncR = ArrayOp.removeAt(syncR, i);
    if (midLineHoverSyncPoint == i) midLineHoverSyncPoint = -1;
  }

  public void setLeft(int i) {
    if (hasLeft(i)) return;
    curL = i;
    setSyncPoints();
  }

  public void setRight(int i) {
    if (hasRight(i)) return;
    curR = i;
    setSyncPoints();
  }

  private void setSyncPoints() {
    if (curL == -1 || curR == -1) return;
    int lP = -Arrays.binarySearch(syncL, curL) - 1;
    int rP = -Arrays.binarySearch(syncR, curR) - 1;
    if (lP == rP) {
      syncL = ArrayOp.insertAt(curL, syncL, lP);
      syncR = ArrayOp.insertAt(curR, syncR, rP);
    } else {
      int min = Math.min(lP, rP);
      int max = Math.max(lP, rP);
      int[] newSyncL = new int[syncL.length - (max - min) + 1];
      int[] newSyncR = new int[syncR.length - (max - min) + 1];
      System.arraycopy(syncL, 0, newSyncL, 0, min);
      System.arraycopy(syncR, 0, newSyncR, 0, min);
      newSyncL[min] = curL;
      newSyncR[min] = curR;
      System.arraycopy(syncL, max, newSyncL, min + 1, syncL.length - max);
      System.arraycopy(syncR, max, newSyncR, min + 1, syncR.length - max);
      this.syncL = newSyncL;
      this.syncR = newSyncR;
    }
    if (DEBUG) {
      System.out.println("lP = " + lP);
      System.out.println("rP = " + rP);
      System.out.println("syncL = " + Arrays.toString(syncL));
      System.out.println("syncR = " + Arrays.toString(syncR));
    }
    curL = curR = -1;
  }

  public void updateOnDiff(Diff diff, boolean isUndo, boolean left) {
    int[] sync = left ? syncL : syncR;
    int from = diff.line;
    int lineCount = diff.lineCount();
    boolean isDelete = diff.isDelete ^ isUndo;
    for (int i = 0; i < sync.length; i++) {
      if (isDelete) {
        if (from > sync[i]) continue;
        int change = Math.min(lineCount, sync[i] - from);
        if (!isUndo) diff.syncPointDiff = change;
        sync[i] -= change;
      } else {
        if (from >= sync[i]) continue;
        int change = isUndo && diff.syncPointDiff != -1 ? diff.syncPointDiff : lineCount;
        sync[i] += change;
      }
    }
    removeRepeatPoints();
  }

  private void removeRepeatPoints() {
    int i = 1;
    while (i < syncL.length) {
      if (syncL[i] == syncL[i - 1] ||
          syncR[i] == syncR[i - 1]
      ) {
        syncL = ArrayOp.removeAt(syncL, i - 1);
        syncR = ArrayOp.removeAt(syncR, i - 1);
      } else i++;
    }
  }
}
