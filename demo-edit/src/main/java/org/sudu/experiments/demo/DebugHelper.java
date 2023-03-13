package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.WglGraphics;

public class DebugHelper {
  static void dumpFontsSize(WglGraphics g) {
    dumpFontSize(Fonts.CourierNew, g);
    dumpFontSize(Fonts.SegoeUI, g);
    dumpFontSize(Fonts.Helvetica, g);
    dumpFontSize(Fonts.Verdana, g);
  }

  static void dumpFontSize(String font, WglGraphics g) {
    for (int i = 5; i < 32; i++) {
      FontDesk fontDesk = g.fontDesk(font, i);
      Debug.consoleInfo("[" + i + "] ascent = ", fontDesk.fAscent);
      Debug.consoleInfo("[" + i + "] descent = ", fontDesk.fDescent);
      Debug.consoleInfo("[" + i + "] WWidth = ", fontDesk.WWidth);
      Debug.consoleInfo("[" + i + "] spaceWidth = ", fontDesk.spaceWidth);
    }
  }

}
