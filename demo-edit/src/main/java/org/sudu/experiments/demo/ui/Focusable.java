package org.sudu.experiments.demo.ui;

import org.sudu.experiments.input.KeyEvent;

public interface Focusable {
  default void onFocusLost() {}
  default void onFocusGain() {}
  boolean onKeyPress(KeyEvent event);
}
