package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class LineNumbersColors {

  public Color textColor;
  public Color caretTextColor;
  public Color caretBgColor;
  public Color syncPoint;        // todo better colors for sync points
  public Color currentSyncPoint;
  public Color hoverSyncPoint;

  public static LineNumbersColors darcula() {
    return new LineNumbersColors(
        new Color("#606366"),
        new Color("#A4A3A3"),
        new Color(IdeaCodeColors.Darcula.caretBg),
        new Color("#ff0000"),
        new Color("#00ff00"),
        new Color("#0000ff")
    );
  }

  public static LineNumbersColors dark() {
    return new LineNumbersColors(
        new Color("#4B5059"),
        new Color("#A1A3AB"),
        new Color(IdeaCodeColors.Dark.caretBg),
        new Color("#ff0000"),
        new Color("#00ff00"),
        new Color("#0000ff")
    );
  }

  public static LineNumbersColors light() {
    return new LineNumbersColors(
        new Color("#AEB3C2"),
        new Color("#767A8A"),
        new Color(IdeaCodeColors.Light.caretBg),
        new Color("#ff0000"),
        new Color("#00ff00"),
        new Color("#0000ff")
    );
  }

  LineNumbersColors(
      Color textColor, Color caretTextColor, Color caretBgColor,
      Color syncPoint, Color currentSyncPoint, Color hoverSyncPoint
  ) {
    this.textColor = textColor;
    this.caretTextColor = caretTextColor;
    this.caretBgColor = caretBgColor;
    this.syncPoint = syncPoint;
    this.currentSyncPoint = currentSyncPoint;
    this.hoverSyncPoint = hoverSyncPoint;
  }
}
