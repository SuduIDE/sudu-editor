package org.sudu.experiments.win32;

import java.util.Arrays;

public class JHandle<T> {
  private T[] values;
  private Entry freeList;

  public JHandle(T[] init) {
    values = init;
    freeList = init.length == 0 ? null : new Entry(0, values.length);
  }

  public int alloc(T value) {
    if (freeList == null) {
      int size = values.length;
      values = Arrays.copyOf(values, Math.max(values.length * 2, 4));
      freeList = new Entry(size, values.length);
    }
    int r = freeList.allocate();
    values[r] = value;
    if (freeList.empty()) {
      freeList = freeList.next;
    }
    return r;
  }

  public void free(int index) {
    values[index] = null;
    freeList = new Entry(index, freeList);
  }

  @Override
  public String toString() {
    return Arrays.toString(values);
  }

  public T get(int index) {
    return values[index];
  }

  static class Entry {
    int first, last;
    Entry next;

    Entry(int f, int l) {
      first = f;
      last = l;
    }

    Entry(int x, Entry n) {
      first = x;
      last = x + 1;
      next = n;
    }

    int allocate() {
      int r = first;
      first ++;
      return r;
    }

    boolean empty() {
      return first == last;
    }
  }
}
