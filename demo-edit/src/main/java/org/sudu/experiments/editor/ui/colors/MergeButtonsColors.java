package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class MergeButtonsColors {
  public DiffColors textColors;
  public DiffColors bgColors;
  public Color textColor;
  public Color bgColor;
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
  }

  public Color bg(boolean selected, boolean hasFocus) {
    return bgColor;
//    return selected ? hasFocus ? selectedBg : inactiveSelectedBg : bgColor;
  }
}
