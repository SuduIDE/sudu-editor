package org.sudu.experiments.input;

public class KeyEvent extends KeyModifiers {
  public final String key;
  public final int keyCode;
  public final boolean isPressed;
  public final boolean isRepeated;
  public boolean prevented = false;

  public KeyEvent(
      String key, int keyCode, boolean pressed, boolean repeated,
      boolean ctrl, boolean alt, boolean shift, boolean meta
  ) {
    super(ctrl, alt, shift, meta);
    this.key = key;
    this.keyCode = keyCode;
    this.isPressed = pressed;
    this.isRepeated = repeated;
  }

  public boolean singlePress() {
    return isPressed && !isRepeated;
  }

  // Ctrl-C
  // Ctrl-X
  // Ctrl-Insert
  // Shift-Delete
  public static boolean isCopyPasteRelatedKey(KeyEvent e) {
    if (e.controlOnly() &&
        (e.keyCode == KeyCode.C ||
         e.keyCode == KeyCode.X ||
         e.keyCode == KeyCode.INSERT)
    ) {
      return true;
    }

    return e.shiftOnly() && e.keyCode == KeyCode.DELETE;
  }

  //  - F5 reload key
  //  - F11 fullscreen key
  //  - F12 web debugger key
  public static boolean isBrowserKey(KeyEvent e) {
    return
        e.keyCode == KeyCode.F11 ||
        e.keyCode == KeyCode.F12 ||
        e.keyCode == KeyCode.F5;
  }

  public String toString() {
    return (isPressed ? "key down: " : "key up: ") + key +
        ", keyCode = " + keyCode + ", isRepeated = " + isRepeated;
  }

  public static boolean handleSpecialKey(KeyEvent event) {
    // do not consume browser keyboard to allow page reload and debug
    if (isCopyPasteRelatedKey(event) || isBrowserKey(event)) {
      event.prevented = true;
    }
    return false;
  }
}
