package org.sudu.experiments.arrays;

public class ArrayReader {

  private final int[] source;
  private int pointer;

  public ArrayReader(int[] source) {
    this.source = source;
    pointer = 0;
  }

  public int next() {
    return source[pointer++];
  }

  public void skip(int len) {
    pointer += len;
  }

  public int expect(int exp) {
    int got = next();
    if (got != exp) {
      throw new IllegalStateException("Unexpected int: " + exp + " expected, but" + got + " got");
    }
    return got;
  }

  public void checkSize() {
    if (pointer != source.length)
      System.err.println("Expected " + source.length + " ints to read, but " + pointer + " read");
  }

}
