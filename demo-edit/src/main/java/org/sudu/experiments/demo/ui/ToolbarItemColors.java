package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
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

  public static ToolbarItemColors darkToolbarItemColors() {
    return new ToolbarItemColors(
        new Color("#BBBBBB"), Colors.toolbarBg, Colors.toolbarSelectedBg
    );
  }

  public static ToolbarItemColors lightToolbarItemColors() {
    return new ToolbarItemColors(
        IdeaCodeColors.Colors.defaultTextLight, Colors.findusagesBgLight, Colors.toolbarSelectedBg
    );
  }
}
