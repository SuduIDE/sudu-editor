package org.sudu.experiments.ui;

import org.sudu.experiments.Window;

public class SetCursor {
  final Window window;

  public SetCursor(Window window) {
    this.window = window;
  }

  public boolean set(String cursor) {
    window.setCursor(cursor);
    return true;
  }

  public final boolean setDefault() {
    return set(null);
  }

  public static SetCursor wrap(Window window) {
    return new SetCursor(window);
  }
}
