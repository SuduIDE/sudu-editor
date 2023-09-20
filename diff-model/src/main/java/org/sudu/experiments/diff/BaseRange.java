package org.sudu.experiments.diff;

public abstract class BaseRange<S> {

  public int fromL, fromR;

  public BaseRange(int fromL, int fromR) {
    this.fromL = fromL;
    this.fromR = fromR;
  }

  public abstract int lengthL();
  public abstract int lengthR();

}
