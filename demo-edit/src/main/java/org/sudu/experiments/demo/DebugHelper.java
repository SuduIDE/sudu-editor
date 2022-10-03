package org.sudu.experiments.demo;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.math.V4f;

public class DebugHelper {
  static void dumpFontsSize(Canvas measuringCanvas) {
    dumpFontSize(Fonts.CourierNew, measuringCanvas);
    dumpFontSize(Fonts.SegoeUI, measuringCanvas);
    dumpFontSize(Fonts.Helvetica, measuringCanvas);
    dumpFontSize(Fonts.Verdana, measuringCanvas);
  }

  static void dumpFontSize(String font, Canvas measuringCanvas) {
    for (int i = 5; i < 32; i++) {
      measuringCanvas.setFont(i, font);
      V4f fontMetrics = measuringCanvas.getFontMetrics();
      Debug.consoleInfo("[" + i + "] ascent = ", fontMetrics.x);
      Debug.consoleInfo("[" + i + "] descent = ", fontMetrics.y);
      Debug.consoleInfo("[" + i + "] WWidth = ", fontMetrics.z);
      Debug.consoleInfo("[" + i + "] spaceWidth = ", fontMetrics.w);
    }
  }

}
