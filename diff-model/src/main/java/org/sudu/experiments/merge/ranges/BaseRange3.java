package org.sudu.experiments.merge.ranges;

public abstract class BaseRange3<S> {

  public int fromL, fromM, fromR;
  public int type = -1;

  public BaseRange3(int fromL, int fromM, int fromR) {
    this.fromL = fromL;
    this.fromM = fromM;
    this.fromR = fromR;
  }

  public abstract int lengthL();
  public abstract int lengthM();
  public abstract int lengthR();
}
