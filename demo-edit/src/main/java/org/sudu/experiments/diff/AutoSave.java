package org.sudu.experiments.diff;

public interface AutoSave {

  int OFF              = 0;
  int AFTER_DELAY      = 1;
  int ON_FOCUS_CHANGE  = 2;
  int ON_WINDOW_CHANGE = 3;

  static int from(String type) {
    return switch (type) {
      case "off"            -> 0;
      case "afterDelay"     -> 1;
      case "onFocusChange"  -> 2;
      case "onWindowChange" -> 3;
      default -> -1;
    };
  }

  static String name(int type) {
    return switch (type) {
      case OFF              -> "off";
      case AFTER_DELAY      -> "afterDelay";
      case ON_FOCUS_CHANGE  -> "onFocusChange";
      case ON_WINDOW_CHANGE -> "onWindowChange";
      default -> "unknown";
    };
  }
}
