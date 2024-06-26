package org.sudu.experiments.merge.ranges;

import org.sudu.experiments.merge.MergeRangeTypes;

public class CommonRange3<S> extends BaseRange3<S> {

  public int length;

  public CommonRange3(int fromL, int fromM, int fromR) {
    this(fromL, fromM, fromR, 0);
  }

  public CommonRange3(int fromL, int fromM, int fromR, int length) {
    super(fromL, fromM, fromR);
    this.length = length;
    this.type = MergeRangeTypes.DEFAULT;
  }

  @Override
  public int lengthL() {
    return length;
  }

  @Override
  public int lengthM() {
    return length;
  }

  @Override
  public int lengthR() {
    return length;
  }

  @Override
  public String toString() {
    return "common: " + length;
  }
}
