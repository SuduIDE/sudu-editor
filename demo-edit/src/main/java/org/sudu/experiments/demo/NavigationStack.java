package org.sudu.experiments.demo;

import java.util.Arrays;

public class NavigationStack {
  private NavigationContext[] st = new NavigationContext[16];
  private int size = 0;
  private int current = -1;

  private void push(NavigationContext ctx) {
    if (size == st.length) {
      st = Arrays.copyOf(st, size + 16);
    }
    st[size++] = ctx;
  }

  public void add(NavigationContext ctx) {
    if (current == size - 1) {
      push(ctx);
    } else {
      for (int i = current + 1; i < size; i++) {
        pop();
      }
      push(ctx);
    }
    current++;
  }

  private void pop() {
    st[--size] = null;
  }

  public void decCurrent() {
    if (current > 0) current--;
  }

  public NavigationContext getCurrentCtx() {
    return st[current];
  }

  public NavigationContext getPrevCtx() {
    if (current <= 0) return null;
    return st[--current];
  }

  public NavigationContext getNextCtx() {
    if (current == size - 1) return null;
    return st[++current];
  }

}
