package org.sudu.experiments.demo.ui.colors;

import org.sudu.experiments.math.Color;

public class WindowColors {
  public Color windowBorderColor;
  public Color windowTitleBgColor;
  public Color windowTitleTextColor;

  public WindowColors(Color windowBorderColor, Color windowTitleBgColor, Color windowTitleTextColor) {
    this.windowBorderColor = windowBorderColor;
    this.windowTitleBgColor = windowTitleBgColor;
    this.windowTitleTextColor = windowTitleTextColor;
  }

  /*
  Original color scheme:
        windowTitleBgColor = #3C3F41
        windowTitleTextColor = #BBBBBB
  Use different colors for better look
   */
  static WindowColors darcula() {
    return new WindowColors(
        new Color("#616161"),
        new Color("#393B40"),
        new Color("#DFE1E5"));
  }

  static WindowColors dark() {
    return new WindowColors(
        new Color("#43454A"),
        new Color("#393B40"),
        new Color("#DFE1E5"));
  }

  static WindowColors light() {
    return new WindowColors(
        new Color("#B9BDC9"),
        new Color("#F7F8FA"),
        new Color(0));
  }
}
