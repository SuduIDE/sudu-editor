package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

class LineNumbersColorScheme {

  public final V4f textColor;
  public final V4f bgColor;
  public final V4f caretTextColor;
  public final V4f caretBgColor;

  public LineNumbersColorScheme(Color t, Color bg, Color c, Color cbg) {
    this(t.v4f, bg.v4f, c.v4f, cbg.v4f);
  }

  public static LineNumbersColorScheme ideaColorScheme() {
    return new LineNumbersColorScheme(
        new Color(0x60, 0x63, 0x66),
        new Color(0x31, 0x33, 0x35),
        new Color(0xA4, 0xA3, 0xA3),
        new Color(0x32)
    );
  }

  public static LineNumbersColorScheme stdColorScheme() {
    return new LineNumbersColorScheme(
        Colors.defaultText.v4f,
        Colors.editBgColor.v4f,
        Colors.white,
        Colors.black
    );
  }

  LineNumbersColorScheme(
      V4f textColor, V4f bgColor,
      V4f caretTextColor, V4f caretBgColor
  ) {
    this.textColor = textColor;
    this.bgColor = bgColor;
    this.caretTextColor = caretTextColor;
    this.caretBgColor = caretBgColor;
  }
}