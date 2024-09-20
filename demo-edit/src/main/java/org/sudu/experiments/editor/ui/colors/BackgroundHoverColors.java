package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class BackgroundHoverColors {
  public final DiffColors diff;
  public final Color bgColor;
  public final Color caretBgColor;

  public BackgroundHoverColors(DiffColors diffWithHover, Color bgColor, Color caretBgWithHover) {
    this.diff = diffWithHover;
    this.bgColor = bgColor;
    this.caretBgColor = caretBgWithHover;
  }
}
