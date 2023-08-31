package org.sudu.experiments.demo;

import org.sudu.experiments.math.V4f;

public class LineNumbersColors {

  public final V4f textColor;
  public final V4f bgColor;
  public final V4f caretTextColor;
  public final V4f caretBgColor;

  LineNumbersColors(
      V4f textColor, V4f bgColor,
      V4f caretTextColor, V4f caretBgColor
  ) {
    this.textColor = textColor;
    this.bgColor = bgColor;
    this.caretTextColor = caretTextColor;
    this.caretBgColor = caretBgColor;
  }
}
