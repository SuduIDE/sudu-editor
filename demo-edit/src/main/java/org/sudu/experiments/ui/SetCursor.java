package org.sudu.experiments.ui;

import org.sudu.experiments.Window;

public interface SetCursor {

  // returns true
  boolean set(String value);

  default boolean setDefault() {
    return set(null);
  }

  static SetCursor wrap(Window window) {
    return cursor -> {
      window.setCursor(cursor);
      return true;
    };
  }
}
