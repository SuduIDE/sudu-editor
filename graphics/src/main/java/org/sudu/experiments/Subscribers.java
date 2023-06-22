package org.sudu.experiments;

import java.util.Arrays;

// slow
public class Subscribers<T> {

  private T[] data;

  public Subscribers(T[] init) {
    data = init;
  }

  public void add(T item) {
    T[] array = data;
    int length = array.length, i = 0;
    for (; i < length; i++) {
      if (array[i] == null) {
        array[i] = item;
        break;
      }
    }
    if (i == length) {
      data = Arrays.copyOf(array, length + 4);
      data[length] = item;
    }
  }

  public Disposable disposableAdd(T item) {
    add(item);
    return () -> remove(item);
  }

  public void remove(T item) {
    for (int i = 0; i < data.length; i++) {
      if (data[i] == item) {
        data[i] = null;
        break;
      }
    }
  }

  // returns array with nullable subscribers
  public T[] array() {
    return data;
  }
}
