package org.sudu.experiments.input;

import java.util.function.Consumer;

public interface InputListener {
  default boolean onKey(KeyEvent event) {
    return false;
  }

  default boolean onMouseMove(MouseEvent event) {
    return false;
  }

  int MOUSE_BUTTON_LEFT   = 0;
  int MOUSE_BUTTON_CENTER = 1;
  int MOUSE_BUTTON_RIGHT  = 2;

  default boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    return false;
  }

  default boolean onMouseWheel(MouseEvent event, double dX, double dY) {
    return false;
  }

  default boolean onContextMenu(MouseEvent event) {
    return false;
  }

  default boolean onCopy(Consumer<String> setText, boolean isCut) {
    return false;
  }

  default Consumer<String> onPastePlainText() {
    return null;
  }

  default void onFocus() {}

  default void onBlur() {}
}
