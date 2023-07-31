package org.sudu.experiments.input;

public interface MouseListener {

  default boolean onMouseMove(MouseEvent event) {
    return false;
  }

  double clickTimeFrame = 0.5;
  int MOUSE_BUTTON_LEFT = 0;
  int MOUSE_BUTTON_CENTER = 1;
  int MOUSE_BUTTON_RIGHT = 2;

  default boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    return false;
  }
}
