package org.sudu.experiments.ui;

import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.KeyEvent;

import java.util.function.Consumer;

public interface Focusable extends InputListeners.KeyHandler {
  default void onFocusLost() {}
  default void onFocusGain() {}
  boolean onKeyPress(KeyEvent event);

  default boolean onCopy(Consumer<String> setText, boolean isCut) {
    return false;
  }

  default Consumer<String> onPaste() {
    return null;
  }
}
