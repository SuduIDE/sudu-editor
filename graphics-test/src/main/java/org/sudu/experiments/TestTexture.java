package org.sudu.experiments;

public class TestTexture implements GLApi.Texture {
  final int n;
  int allocated;
  int texSubImageCalls = 0;

  public TestTexture(int n) {
    this.n = n;
  }

  @Override
  public String toString() {
    return "Texture #" + n
        + ", texSubImageCalls = " + texSubImageCalls;
  }
}
