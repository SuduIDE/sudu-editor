package org.sudu.experiments.input;

import java.util.function.Consumer;

public interface MouseListener {

  default boolean onMouseMove(MouseEvent event) {
    return false;
  }

  float clickTimeFrame = 0.5f;

  int MOUSE_BUTTON_LEFT = 0;
  int MOUSE_BUTTON_CENTER = 1;
  int MOUSE_BUTTON_RIGHT = 2;

  default Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    return null;
  }

  default boolean onMouseUp(MouseEvent event, int button) {
    return false;
  }

  default boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return false;
  }

  interface Static {
    Consumer<MouseEvent> emptyConsumer = e -> {};
  }

}
