package org.sudu.experiments.diff.ranges;

public class CommonRange<S> extends BaseRange<S> {

  public int length;

  public CommonRange(int fromL, int fromR) {
    this(fromL, fromR, 0);
  }

  public CommonRange(int fromL, int fromR, int length) {
    super(fromL, fromR);
    this.length = length;
  }

  @Override
  public int lengthL() {
    return length;
  }

  @Override
  public int lengthR() {
    return length;
  }
}
