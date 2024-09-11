package org.sudu.experiments.arrays;

import java.util.Arrays;

public class ArrayWriter {

  private int[] result;
  private final int initialCapacity;
  private int pointer;

  public ArrayWriter() {
    this(0);
  }

  public ArrayWriter(int capacity) {
    initialCapacity = capacity;
    result = new int[capacity == 0 ? 16 : capacity];
    pointer = 0;
  }

  public void write(int... ints) {
    for (int i: ints) push(i);
  }

  public void write(int i1) {
    push(i1);
  }

  public void write(int i1, int i2) {
    push(i1);
    push(i2);
  }

  public void write(int i1, int i2, int i3) {
    push(i1);
    write(i2, i3);
  }

  public void write(int i1, int i2, int i3, int i4) {
    push(i1);
    write(i2, i3, i4);
  }

  private void push(int x) {
    if (result.length == pointer) {
      result = Arrays.copyOf(result, result.length * 2);
    }
    result[pointer++] = x;
  }

  public int[] getInts() {
    if (initialCapacity != 0 && pointer != initialCapacity) {
      System.err.println("Expected " + initialCapacity + " ints to write, but " + pointer + " written");
    }
    return result.length == pointer ? result : Arrays.copyOf(result, pointer);
  }

  public int getPointer() {
    return pointer;
  }

  public void writeAtPos(int pos, int value) {
    result[pos] = value;
  }

  public void clear() {
    result = new int[initialCapacity == 0 ? 16 : initialCapacity];
    pointer = 0;
  }
}
