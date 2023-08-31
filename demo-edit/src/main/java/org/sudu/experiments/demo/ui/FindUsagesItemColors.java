package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.DialogColors;
import org.sudu.experiments.math.V4f;

public class FindUsagesItemColors {
  final V4f fileColor;
  final V4f lineColor;
  final V4f contentColor;
  final V4f bgColor;
  final V4f bgCaretColor;
  final V4f textCaretColor;

  public FindUsagesItemColors(V4f fileColor, V4f lineColor, V4f contentColor, V4f bgColor, V4f bgCaretColor, V4f textCaretColor) {
    this.fileColor = fileColor;
    this.lineColor = lineColor;
    this.contentColor = contentColor;
    this.bgColor = bgColor;
    this.bgCaretColor = bgCaretColor;
    this.textCaretColor = textCaretColor;
  }

  public static FindUsagesItemColors darculaFindUsagesItemColors() {
    return new FindUsagesItemColors(
        DialogColors.Darcula.findUsagesTextCaret,
        DialogColors.Darcula.findUsagesLineNumber,
        DialogColors.Darcula.findUsagesContent,
        DialogColors.Darcula.findUsagesBg,
        DialogColors.Darcula.findUsagesBgCaret,
        DialogColors.Darcula.findUsagesTextCaret
    );
  }

  public static ToolbarItemColors darculaNoUsages() {
    return ToolbarItemColors.darculaToolbarItemColors();
  }

  public static FindUsagesItemColors lightFindUsagesItemColors() {
    return new FindUsagesItemColors(
        DialogColors.Light.findUsagesTextCaret,
        DialogColors.Light.findUsagesLineNumber,
        DialogColors.Light.findUsagesContent,
        DialogColors.Light.findUsagesBg,
        DialogColors.Light.findUsagesBgCaret,
        DialogColors.Light.findUsagesTextCaret
    );
  }

  public static ToolbarItemColors lightNoUsages() {
    return ToolbarItemColors.lightToolbarItemColors();
  }
}
