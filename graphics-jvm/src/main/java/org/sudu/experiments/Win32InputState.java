package org.sudu.experiments;

import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.win32.Win32;

import static org.sudu.experiments.win32.WindowPeer.*;

class Win32InputState {

  boolean shift, ctrl, alt, meta;

  boolean onKey(int msg, long wParam, long lParam, InputListeners listeners) {
    boolean onChar = msg == WM_CHAR;
    boolean down = msg == WM_KEYDOWN || msg == WM_SYSKEYDOWN;
    boolean prevState = (lParam & (1 << 30)) != 0;
    int vKey = (int) wParam;

    String key = onChar ? String.valueOf((char) vKey) : ""; // wmchar

    System.out.println((onChar ? "char = " : "vKey = ") + vKey + ", down = " + down + ", prevState = " + prevState);

    updateMods(vKey, down);

    KeyEvent event = new KeyEvent(key, toWebKeyCode(vKey), down, down == prevState,
        ctrl, alt, shift, meta);

    return listeners.sendKeyEvent(event);
  }

  void onKillFocus() {
    alt = ctrl = shift = false;
  }

  void readState() {
    alt = getKeyState(VK_MENU);
    ctrl = getKeyState(VK_CONTROL);
    shift = getKeyState(VK_SHIFT);
  }

  private void updateMods(int vKey, boolean state) {
    switch (vKey) {
      case VK_SHIFT -> shift = state;
      case VK_CONTROL -> ctrl = state;
      case VK_MENU -> alt = state;
    }
  }

  static boolean getKeyState(int vKey) { return (Win32.GetKeyState(vKey) & 0x8000) != 0; }

  static int toWebKeyCode(int vkCode) {
    // pageUp, pageDown, end, home, arrows
    if (vkCode >= VK_PRIOR && vkCode <= VK_DOWN) return vkCode;
    if (vkCode >= VK_KEY0 && vkCode <= VK_KEY9)  return vkCode;
    // a..z
    if ((vkCode >= 65 && vkCode <= 90))          return vkCode;
    // F1 ... F12
    if (vkCode >= VK_F1 && vkCode <= VK_F12)     return vkCode;

    return switch (vkCode) {
      case VK_RETURN -> KeyCode.ENTER;
      case VK_ESCAPE -> KeyCode.ESC;
      case VK_INSERT -> KeyCode.INSERT;
      case VK_DELETE -> KeyCode.DELETE;
      case VK_BACK   -> KeyCode.BACKSPACE;
      case VK_TAB    -> KeyCode.TAB;
//      case VK_SPACE  -> KeyCode.SPACE;
      default -> 0;
    };
  }

  // https://learn.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes

  static final int VK_BACK    = 0x08;
  static final int VK_TAB     = 0x09;
  static final int VK_RETURN  = 0x0D;
  static final int VK_SHIFT   = 0x10;
  static final int VK_CONTROL = 0x11;
  static final int VK_MENU	  = 0x12; // ALT key
  static final int VK_PAUSE   = 0x13;
  static final int VK_CAPITAL = 0x14;
  static final int VK_ESCAPE  = 0x1B;
  static final int VK_SPACE   = 0x20;
  static final int VK_PRIOR   = 0x21; // PAGE UP key
  static final int VK_NEXT    = 0x22; // PAGE DOWN key
  static final int VK_END     = 0x23;
  static final int VK_HOME    = 0x24;
  static final int VK_LEFT    = 0x25;
  static final int VK_UP      = 0x26;
  static final int VK_RIGHT   = 0x27;
  static final int VK_DOWN    = 0x28;
  static final int VK_INSERT  = 0x2D;
  static final int VK_DELETE  = 0x2E;
  static final int VK_HELP    = 0x2F;
  static final int VK_F1      = 0x70;
  static final int VK_F12     = 0x7B;
  static final int VK_KEY0    = 0x30;
  static final int VK_KEY9    = 0x39;

}
