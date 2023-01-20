package org.sudu.experiments.math;

import java.util.ArrayList;
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

  static <T> T[] remove(T[] from, int fromInd, int toInd, T[] target) {
    if (fromInd > 0) {
      System.arraycopy(from, 0, target, 0, fromInd);
    }
    if (toInd > 0 && toInd <= from.length) {
      System.arraycopy(from, toInd, target, fromInd, from.length - toInd);
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

  static int writeInt16Le(byte[] d, int pos, int value) {
    d[pos] = (byte) value;
    d[pos + 1] = (byte) (value >>> 8);
    return pos + 2;
  }

  static int readInt16Le(byte[] d, int pos) {
    int i0 = d[pos] & 0xFF;
    int i1 = d[pos + 1] & 0xFF;
    return (i1 << 8) + i0;
  }

  static int writeInt32Le(byte[] d, int pos, int value) {
    d[pos] = (byte) value;
    d[pos + 1] = (byte) (value >>> 8);
    d[pos + 2] = (byte) (value >>> 16);
    d[pos + 3] = (byte) (value >>> 24);
    return pos + 4;
  }

  static int readInt32Le(byte[] d, int pos) {
    int i0 = d[pos] & 0xFF;
    int i1 = d[pos + 1] & 0xFF;
    int i2 = d[pos + 2] & 0xFF;
    int i3 = d[pos + 3] & 0xFF;
    return (i3 << 24) + (i2 << 16) + (i1 << 8) + i0;
  }

  // Reason for hand copy:
  //   this is faster (than toArray()) on JavaScript
  //   because JS(Harmony) uses generic iterator implementation
  static <T> T[] cloneArrayList(ArrayList<T> list, T[] target) {
    for (int i = 0; i < target.length; i++)
      target[i] = list.get(i);
    return target;
  }
}
