package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.ColorOp;

public class MergeButtonsColors {
  public DiffColors textColors;
  public DiffColors bgColors;
  public Color textColor;
  public Color acceptColor = new Color(0, 255, 0);
  public Color rejectColor = new Color(255, 0, 0);
  public Color bgColor;
  public Color bgColorHovered;
  public Color hoverBg;

  public MergeButtonsColors(
      DiffColors textColors,
      DiffColors bgColors,
      Color textColor,
      Color bgColor,
      Color hoverBg
  ) {
    this.textColors = textColors;
    this.bgColors = bgColors;
    this.textColor = textColor;
    this.bgColor = bgColor;
    this.hoverBg = hoverBg;
    bgColorHovered = ColorOp.blend(bgColor, hoverBg);
  }
}
