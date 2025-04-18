package org.sudu.experiments.editor;

import java.util.Arrays;

public class EditorSyncPoints {

  public final SyncPoints syncPoints;
  public final boolean left;

  public EditorSyncPoints(SyncPoints syncPoints, boolean left) {
    this.syncPoints = syncPoints;
    this.left = left;
  }

  public int[] copiedSyncPoints() {
    int[] points = syncPoints();
    return Arrays.copyOf(points, points.length);
  }

  public int[] syncPoints() {
    return left ? syncPoints.syncL : syncPoints.syncR;
  }

  public int curSyncPoint() {
    return left ? syncPoints.curL : syncPoints.curR;
  }

  public void removeSyncPoint(int i) {
    if (left) syncPoints.removeLeft(i);
    else syncPoints.removeRight(i);
  }

  public boolean hasPoint(int i) {
    return left ? syncPoints.hasLeft(i) : syncPoints.hasRight(i);
  }

  public boolean hasSyncPoints() {
    return left ? syncPoints.syncL.length != 0 : syncPoints.syncR.length != 0;
  }

  public boolean canSet(int i) {
    return left ? syncPoints.canSetLeft(i) : syncPoints.canSetRight(i);
  }

  public void setPoint(int i) {
    if (left) syncPoints.setLeft(i);
    else syncPoints.setRight(i);
  }
}
