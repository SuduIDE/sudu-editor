package org.sudu.experiments.demo.ui;

public class CachableItem<T> {

  public final T content;
  private int counter = 1;

  public CachableItem(T content) {
    this.content = content;
  }

  public int dec() {
    return --counter;
  }

  public int inc() {
    return ++counter;
  }
}
