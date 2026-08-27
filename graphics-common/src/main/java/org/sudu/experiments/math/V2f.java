
package org.sudu.experiments.math;

public class V2f {
  public float x, y;

  public V2f() {}

  public V2f(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public V2f(V2f v) {
    x = v.x;
    y = v.y;
  }

  public void set(V2f v) {
    x = v.x;
    y = v.y;
  }

  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "x = " + x + ", y = " + y;
  }

  public boolean equals(Object other) {
    return this == other || (other.getClass() == this.getClass() && equals((V2f) other));
  }

  public boolean equals(V2f other) {
    return x == other.x && y == other.y;
  }

  public boolean equals(float x, float y) {
    return this.x == x && this.y == y;
  }
}
