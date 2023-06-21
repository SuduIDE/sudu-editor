package org.sudu.experiments.parser;

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

  public void checkSize() {
    if (pointer != source.length)
      System.err.println("Expected " + source.length + " ints to read, but " + pointer + " read");
  }

}
