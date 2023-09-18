package org.sudu.experiments.diff;

public class CommonRange<S> extends BaseRange<S> {

  public int length;

  public CommonRange(int fromL, int fromR) {
    super(fromL, fromR);
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
