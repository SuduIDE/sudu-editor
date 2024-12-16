package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public class LineNumbersColors {

  public Color textColor;
  public Color caretTextColor;
  public Color caretBgColor;

  public static LineNumbersColors darcula() {
    return new LineNumbersColors(
        new Color("#606366"),
        new Color("#A4A3A3"),
        new Color(IdeaCodeColors.Darcula.caretBg)
    );
  }

  public static LineNumbersColors dark() {
    return new LineNumbersColors(
        new Color("#4B5059"),
        new Color("#A1A3AB"),
        new Color(IdeaCodeColors.Dark.caretBg)
    );
  }

  public static LineNumbersColors light() {
    return new LineNumbersColors(
        new Color("#AEB3C2"),
        new Color("#767A8A"),
        new Color(IdeaCodeColors.Light.caretBg)
    );
  }

  LineNumbersColors(
      Color textColor, Color caretTextColor, Color caretBgColor
  ) {
    this.textColor = textColor;
    this.caretTextColor = caretTextColor;
    this.caretBgColor = caretBgColor;
  }
}
