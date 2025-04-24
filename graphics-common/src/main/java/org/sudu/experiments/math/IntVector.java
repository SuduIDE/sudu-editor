package org.sudu.experiments.math;

import java.util.Arrays;

public class IntVector {

  private int[] data = new int[4];
  private int pos;

  public void reset() {
    pos = 0;
  }

  public void push(int value) {
    if (data.length == pos)
      data = Arrays.copyOf(data, data.length * 2);
    data[pos++] = value;
  }

  public int position() {
    return pos;
  }

  public int[] data() {
    return data;
  }
}
