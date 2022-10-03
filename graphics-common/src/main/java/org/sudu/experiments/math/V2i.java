package org.sudu.experiments.math;

public class V2i {
  public int x, y;

  public V2i() {}

  public V2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public V2i(V2i v) {
    x = v.x;
    y = v.y;
  }

  public void set(V2i v) {
    x = v.x;
    y = v.y;
  }

  public void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "x = " + x + ", y = " + y;
  }
}
