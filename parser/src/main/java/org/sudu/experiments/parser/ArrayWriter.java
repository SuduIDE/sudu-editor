package org.sudu.experiments.parser;

public class ArrayWriter {

  private final int[] result;
  private int pointer;

  public ArrayWriter(int capacity) {
    result = new int[capacity];
    pointer = 0;
  }

  public void write(int... ints) {
    for (int i: ints) result[pointer++] = i;
  }

  public int[] getInts() {
    if (result.length != pointer)
      System.err.println("Expected " + result.length + " ints to write, but " + pointer + " wrote");
    return result;
  }

}
