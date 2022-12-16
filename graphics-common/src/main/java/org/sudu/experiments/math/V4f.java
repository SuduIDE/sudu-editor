package org.sudu.experiments.math;

public class V4f {
  public float x, y, z, w;

  public V4f() {}

  public V4f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public V4f(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public void set(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public V4f(V4f v) {
    x = v.x;
    y = v.y;
    z = v.z;
    w = v.w;
  }

  public V4f setW(float value) {
    w = value;
    return this;
  }

  public V4f setW(double value) {
    w = (float) value;
    return this;
  }

  public V4f set(V4f v) {
    x = v.x;
    y = v.y;
    z = v.z;
    w = v.w;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    return o != null && getClass() == o.getClass() && equals((V4f) o);
  }

  public boolean equals(V4f v) {
    return v.x == x && v.y == y && v.z == z && v.w == w;
  }

  @Override
  public String toString() {
    return "x = " + x + ", y = " + y + ", z = " + z;
  }
}
