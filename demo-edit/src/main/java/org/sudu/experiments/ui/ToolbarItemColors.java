package org.sudu.experiments.ui;

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

  public V4f bgColor(boolean highlighted) {
    return highlighted ? bgHighlight : bgColor;
  }
}
