package org.sudu.experiments;

import org.sudu.experiments.math.V4f;

public class FontDesk {
  public final String name;
  public final int size;
  public final int ascent, descent;
  public final float spaceWidth, WWidth;
  public final boolean monospace;
  public final Object platformFont;

  public FontDesk(int size, String name, Canvas measuringCanvas) {
    this.name = name;
    this.size = size;

    Debug.consoleInfo("new FontDesk: font " + name + ", size " + size);

    measuringCanvas.setFont(size, name);

    V4f fontMetrics = measuringCanvas.getFontMetrics();
    ascent = (int) (fontMetrics.x + 0.5f);
    descent = (int) (fontMetrics.y + 0.5f);
    WWidth = fontMetrics.z;
    spaceWidth = fontMetrics.w;

    Debug.consoleInfo("  font ascent = ", fontMetrics.x);
    Debug.consoleInfo("  font descent = ", fontMetrics.y);

    int dotSize = (int) (measuringCanvas.measureText(".") * 32);
    int spaceSize = (int) (spaceWidth * 32);
    int WSize = (int) (WWidth * 32);

    monospace = spaceSize == dotSize && spaceSize == WSize;

    if (1>0) {
      Debug.consoleInfo("  '.' size * 32 = ", dotSize);
      Debug.consoleInfo("  'W' size * 32 = ", WSize);
      Debug.consoleInfo("  ' ' size * 32 = ", spaceSize);
    }

    platformFont = measuringCanvas.platformFont(name, size);

    Debug.consoleInfo("  monospace = " + monospace);
    Debug.consoleInfo("  platformFont = ", platformFont);
  }

  // usually this is very near to (size * 1.2)
  public int realFontSize() {
    return ascent + descent;
  }

  public int caretHeight(int lineHeight) {
    int caretOffer = (ascent + descent + size) / 2;
    return lineHeight - (lineHeight / 2 - caretOffer / 2) * 2;
  }
}
