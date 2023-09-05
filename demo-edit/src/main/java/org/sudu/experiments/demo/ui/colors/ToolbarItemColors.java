package org.sudu.experiments.demo.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public final class ToolbarItemColors {
  public V4f color;
  public V4f bgColor;
  public V4f bgHighlight;

  public ToolbarItemColors(V4f color, V4f bgColor, V4f bgHighlight) {
    this.color = color;
    this.bgColor = bgColor;
    this.bgHighlight = bgHighlight;
  }

  public static ToolbarItemColors darculaToolbarItemColors() {
    return new ToolbarItemColors(
        new Color("#BBBBBB"), DialogColors.Darcula.toolbarBg, DialogColors.Darcula.toolbarSelectedBg
    );
  }

  public static ToolbarItemColors lightToolbarItemColors() {
    return new ToolbarItemColors(
        IdeaCodeColors.Light.defaultText, DialogColors.Light.toolbarBg, DialogColors.Light.toolbarSelectedBg
    );
  }
}
