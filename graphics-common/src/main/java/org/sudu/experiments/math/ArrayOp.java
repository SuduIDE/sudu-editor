package org.sudu.experiments.math;

import java.util.Arrays;

public interface ArrayOp {
  static <T> T[] add(T[] a, T[] b) {
    T[] s = Arrays.copyOf(a, a.length + b.length);
    System.arraycopy(b, 0, s, a.length, b.length);
    return s;
  }

  static <T> T[] segment(T[] a, int pos, T[] target) {
    System.arraycopy(a, pos, target, 0, target.length);
    return target;
  }
  static <T> T[] remove(T[] from, int pos, T[] target) {
    if (pos > 0) {
      System.arraycopy(from, 0, target, 0, pos);
    }
    if (pos < target.length) {
      System.arraycopy(from, pos + 1, target, pos, target.length - pos);
    }
    return target;
  }

  static <T> T[] add(T[] array, T element) {
    T[] r = Arrays.copyOf(array, array.length + 1);
    r[array.length] = element;
    return r;
  }

  static <T> T[] remove(T[] array, T element) {
    if (array == null) return null;
    if (array.length == 1 && array[0] == element) return null;
    int pos = indexOf(array, element);
    if (pos < 0) return array;
    T[] r = Arrays.copyOf(array, array.length - 1);
    if (pos < r.length) System.arraycopy(array, pos + 1, r, pos, r.length - pos);
    return r;
  }

  static <T> int indexOf(T[] array, T item) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == item) return i;
    }
    return -1;
  }
}
