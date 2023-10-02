package org.sudu.experiments.ui;

public class CountedItem<T> {

  public final T content;
  private int counter = 1;

  public CountedItem(T content) {
    this.content = content;
  }

  public int release() {
    return --counter;
  }

  public int addRef() {
    return ++counter;
  }
}
