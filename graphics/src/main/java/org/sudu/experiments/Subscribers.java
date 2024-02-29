package org.sudu.experiments;

import java.util.Arrays;

// slow
public class Subscribers<T> {

  private T[] data;
  private T[] copy;
  private int length;

  public Subscribers(T[] init) {
    data = init;
  }

  public int length() { return length; }

  public T get(int idx) { return data[idx]; }

  public void add(T item) {
    if (length == data.length) {
      data = Arrays.copyOf(data, length + 4);
    }
    data[length++] = item;
    copy = null;
  }

  public void add(int index, T item) {
    if (length == data.length) {
      data = Arrays.copyOf(data, length + 4);
    }
    for (int j = length; index < j; --j)
      data[j] = data[j - 1];
    data[index] = item;
    length++;
    copy = null;
  }

  public void moveToFront(int index) {
    if (length <= index)
      throw new ArrayIndexOutOfBoundsException(index);
    if (index == 0) return;

    T value = data[index];
    for (int i = index; i > 0; i--)
      data[i] = data[i - 1];
    data[0] = value;

    if (copy != null)
      System.arraycopy(data, 0, copy, 0, length);
  }

  public Disposable disposableAdd(T item) {
    add(item);
    return () -> remove(item);
  }

  public void remove(T item) {
    for (int i = 0; i < data.length; i++) {
      if (data[i] == item) {
        for (; i + 1 < data.length; i++)
          data[i] = data[i + 1];
        data[i] = null;
        length--;
        copy = null;
      }
    }
  }

  public void clear() {
    copy = null;
    for (int i = 0; i < length; i++) {
      data[i] = null;
    }
    length = 0;
  }

  // returns actual copy of subscribers
  public T[] array() {
    if (copy == null || copy.length != length) {
      copy = Arrays.copyOf(data, length);
    }
    return copy;
  }

  public int find(T item) {
    for (int i = 0, l = data.length; i < l; i++) {
      if (item == data[i])
        return i;
    }
    return -1;
  }
}
