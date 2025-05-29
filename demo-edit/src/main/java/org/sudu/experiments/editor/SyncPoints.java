package org.sudu.experiments.editor;

import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

public class SyncPoints {

  public int[] syncL, syncR;              // doc line
  public int curL, curR;                  // doc line
  public int midLineHoverSyncPoint = -1;  // syncL & syncR index
  private final Runnable onSyncPointsChanged;
  private static final boolean DEBUG = false;

  public SyncPoints(Runnable onSyncPointsChanged) {
    syncL = new int[0];
    syncR = new int[0];
    curL = curR = -1;
    this.onSyncPointsChanged = onSyncPointsChanged;
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
    onSyncPointsChanged.run();
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

  public int[] getSyncL() {
    return this.syncL;
  }

  public int[] getSyncR() {
    return this.syncR;
  }

  public void setSyncPoints() {
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
    onSyncPointsChanged.run();
  }
}
