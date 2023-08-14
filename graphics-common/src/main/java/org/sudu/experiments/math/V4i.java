package org.sudu.experiments.math;

public class V4i {
  public int x, y, z, w;

  public V4i() {}

  public V4i(int x, int y, int z, int w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public V4i(V4i v) {
    x = v.x;
    y = v.y;
    z = v.z;
    w = v.w;
  }

  public void set(V4i v) {
    x = v.x;
    y = v.y;
    z = v.z;
    w = v.w;
  }

  public void set(int x, int y, int z, int w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  @Override
  public String toString() {
    return "x = " + x + ", y = " + y + ", z = " + z + ", w = " + w;
  }

  public boolean equals(Object other) {
    return this == other || (other.getClass() == this.getClass() && equals((V4i) other));
  }

  public boolean equals(V4i other) {
    return x == other.x && y == other.y
        && z == other.z && w == other.w;
  }

  public boolean equals(int x, int y, int z, int w) {
    return this.x == x && this.y == y
        && this.z == z && this.w == w;
  }
}
