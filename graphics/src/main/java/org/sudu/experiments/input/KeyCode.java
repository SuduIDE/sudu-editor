package org.sudu.experiments.input;

// https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/keyCode

public interface KeyCode {
  int SHIFT = 16;
  int CTRL = 17;
  int ALT = 18;

  int Pause = 19;
  int CapsLock = 20;

  int META = 91;
  int SPACE = 32;

  int A = 65;
  int C = 67;
  int O = 79;
  int P = 80;
  int V = 86;
  int X = 88;
  int W = 87;
  int Z = A + 25;


  int F5 = 116;
  int F1 = F5 - 4;
  int F10 = 121;
  int F11 = 122;
  int F12 = 123;

  int NumLock = 144;
  int ScrollLock = 145;

  int _0 = 48;
  int _9 = 57;

  // important editor keys
  int ARROW_LEFT = 37;
  int ARROW_UP = 38;
  int ARROW_RIGHT = 39;
  int ARROW_DOWN = 40;
  int PAGE_UP = 33;
  int PAGE_DOWN = 34;
  int END = 35;
  int HOME = 36;
  int BACKSPACE = 8;
  int TAB = 9;
  int INSERT = 45;
  int DELETE = 46;

  int ENTER = 13;
  int ESC = 27;

  static boolean isFKey(int keyCode) {
    return F1 <= keyCode && keyCode <= F12;
  }
}
