package org.sudu.experiments.editor;

import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

public class UserXY {
  int usrX, usrY;

  boolean onKeyEvent(KeyEvent event) {
    if (event.isPressed) switch (event.keyCode) {
      case KeyCode.ARROW_LEFT -> usrX--;
      case KeyCode.ARROW_RIGHT -> usrX++;
      case KeyCode.ARROW_UP -> usrY--;
      case KeyCode.ARROW_DOWN -> usrY++;
    }
    return false;
  }

}
