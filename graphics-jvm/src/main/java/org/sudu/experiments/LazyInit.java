package org.sudu.experiments;

import java.util.function.Supplier;

public abstract class LazyInit<T> implements Supplier<T> {

  protected T value;

  protected abstract T create();

  @Override
  public T get() {
    return value == null ? (value = create()) : value;
  }
}
