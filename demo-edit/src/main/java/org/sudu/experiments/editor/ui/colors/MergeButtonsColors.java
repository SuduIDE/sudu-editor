package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class MergeButtonsColors {
  public Color textColor;
  public Color bgColor;
  public Color selectedBg;
  public Color inactiveSelectedBg;

  public MergeButtonsColors(
      Color textColor, Color bgColor,
      Color selectedBg,
      Color inactiveSelectedBg
  ) {
    this.textColor = textColor;
    this.bgColor = bgColor;
//    this.selectedBg = selectedBg;
//    this.inactiveSelectedBg = inactiveSelectedBg;
  }

  public Color bg(boolean selected, boolean hasFocus) {
    return bgColor;
//    return selected ? hasFocus ? selectedBg : inactiveSelectedBg : bgColor;
  }
}
