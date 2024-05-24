package org.sudu.experiments.merge.ranges;

import org.sudu.experiments.merge.MergeRangeTypes;

import java.util.ArrayList;
import java.util.List;

public class Diff3<S> extends BaseRange3<S> {

  public List<S> diffL = new ArrayList<>();
  public List<S> diffM = new ArrayList<>();
  public List<S> diffR = new ArrayList<>();

  public Diff3(int fromL, int fromM, int fromR) {
    super(fromL, fromM, fromR);
  }

  public int getType() {
    if (type == -1) return type = countType();
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  private int countType() {
    boolean leftEq = diffL.equals(diffM);
    boolean rightEq = diffR.equals(diffM);
    if (leftEq && rightEq) throw new IllegalStateException();

    if (diffL.isEmpty()) {
      if (diffM.isEmpty()) {
        if (diffR.isEmpty()) throw new IllegalStateException();
        else return MergeRangeTypes.RIGHT_INSERT;
      } else {
        if (diffR.isEmpty()) return MergeRangeTypes.CONFLICTING;
        else if (rightEq) return MergeRangeTypes.LEFT_DELETE;
        else return MergeRangeTypes.CONFLICTING;
      }
    } else {
      if (diffM.isEmpty()) {
        if (diffR.isEmpty()) return MergeRangeTypes.LEFT_INSERT;
        else return MergeRangeTypes.CONFLICTING;
      } else {
        if (diffR.isEmpty()) {
          if (leftEq) return MergeRangeTypes.RIGHT_DELETE;
          else return MergeRangeTypes.CONFLICTING;
        } else {
          if (!leftEq && !rightEq) return MergeRangeTypes.CONFLICTING;
          else if (leftEq) return MergeRangeTypes.RIGHT_EDITED;
          else return MergeRangeTypes.LEFT_EDITED;
        }
      }
    }
  }

  /*
    L   M   R   Result

    E   E   E   Illegal
    E   E  !E   Right-Insert
    E  !E   E   Conflicting?
    E  !E  !E   Left-Delete or Conflicting
   !E   E   E   Left-Insert
   !E   E  !E   Conflicting
   !E  !E   E   Right-Delete or Conflicting
   !E  !E  !E   Left-Edit or Right-Edit or Conflicting
   */

  public boolean isNotEmpty() {
    return !(diffL.isEmpty() && diffM.isEmpty() && diffR.isEmpty());
  }

  @Override
  public int lengthL() {
    return diffL.size();
  }

  @Override
  public int lengthM() {
    return diffM.size();
  }

  @Override
  public int lengthR() {
    return diffR.size();
  }

  @Override
  public String toString() {
    return diffL.toString() + " -> " + diffM.toString() + " <- " + diffR.toString();
  }
}
