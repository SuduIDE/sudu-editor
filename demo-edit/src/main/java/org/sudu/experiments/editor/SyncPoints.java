package org.sudu.experiments.editor;

import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

public class SyncPoints {

  public int[] syncL, syncR;
  public int curL, curR;
  private final Runnable onSyncPointsChanged;

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

  public boolean canSetLeft(int i) {
    if (i == curL) return false;
    if (curR == -1) return true;
    return Arrays.binarySearch(syncL, i) == Arrays.binarySearch(syncR, curR);
  }

  public boolean canSetRight(int i) {
    if (i == curR) return false;
    if (curL == -1) return true;
    return Arrays.binarySearch(syncR, i) == Arrays.binarySearch(syncL, curL);
  }

  public void removeLeft(int i) {
    if (i == curL) {
      curL = -1;
    } else {
      int p = Arrays.binarySearch(syncL, i);
      if (p == -1) return;
      syncL = ArrayOp.removeAt(syncL, p);
      syncR = ArrayOp.removeAt(syncR, p);
      onSyncPointsChanged.run();
    }
  }

  public void removeRight(int i) {
    if (i == curR) {
      curR = -1;
    } else {
      int p = Arrays.binarySearch(syncR, i);
      if (p == -1) return;
      syncL = ArrayOp.removeAt(syncL, p);
      syncR = ArrayOp.removeAt(syncR, p);
      onSyncPointsChanged.run();
    }
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
    syncL = ArrayOp.insertAt(curL, syncL, lP);
    syncR = ArrayOp.insertAt(curR, syncR, rP);
    System.out.println("lP = " + lP);
    System.out.println("rP = " + rP);
    System.out.println("syncL = " + Arrays.toString(syncL));
    System.out.println("syncR = " + Arrays.toString(syncR));
    curL = curR = -1;
    onSyncPointsChanged.run();
  }
}
