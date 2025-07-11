package org.sudu.experiments.arrays;

import java.util.Arrays;
import java.util.function.Supplier;

public class ObjPool<T> {
  private int size;
  private T[] data;
  private final Supplier<T> ctor;

  public ObjPool(T[] data, Supplier<T> ctor) {
    this.data = data;
    this.ctor = ctor;
  }

  public int size() {
    return size;
  }

  public void clear() {
    size = 0;
  }

  public T[] data() {
    return data;
  }

  public T add() {
    if (size == data.length)
      data = Arrays.copyOf(data, data.length * 2);
    T r = data[size];
    if (r == null)
      data[size] = r = ctor.get();
    size++;
    return r;
  }

  public T get(int i) {
    return data[i];
  }
}
