package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.math.V4f;

public class FindUsagesItemColors {
  final V4f fileColor;
  final V4f lineColor;
  final V4f contentColor;
  final V4f bgColor;
  final V4f bgHighlight;

  public FindUsagesItemColors(V4f fileColor, V4f lineColor, V4f contentColor, V4f bgColor, V4f bgHighlight) {
    this.fileColor = fileColor;
    this.lineColor = lineColor;
    this.contentColor = contentColor;
    this.bgColor = bgColor;
    this.bgHighlight = bgHighlight;
  }

  public static FindUsagesItemColors darkFindUsagesItemColors() {
    return new FindUsagesItemColors(
        IdeaCodeColors.Colors.defaultText,
        IdeaCodeColors.Colors.editNumbersVLine,
        IdeaCodeColors.Colors.defaultText,
        Colors.findUsagesBg,
        Colors.findUsagesSelectedBg
    );
  }

  public static FindUsagesItemColors darkFindUsagesItemColorsExtraLine() {
    return new FindUsagesItemColors(
        IdeaCodeColors.Colors.defaultText,
        IdeaCodeColors.Colors.editNumbersVLine,
        IdeaCodeColors.Colors.defaultText,
        Colors.findUsagesBg,
        Colors.findUsagesBg
    );
  }

  public static ToolbarItemColors darkNoUsages() {
    return ToolbarItemColors.darkToolbarItemColors();
  }

  public static FindUsagesItemColors lightFindUsagesItemColors() {
    return new FindUsagesItemColors(
        IdeaCodeColors.Colors.defaultTextLight,
        IdeaCodeColors.Colors.editNumbersVLineLight,
        IdeaCodeColors.Colors.defaultTextLight,
        Colors.findusagesBgLight,
        Colors.findUsagesSelectedBg
    );
  }

  public static FindUsagesItemColors lightFindUsagesItemColorsExtraLine() {
    return new FindUsagesItemColors(
        IdeaCodeColors.Colors.defaultTextLight,
        IdeaCodeColors.Colors.editNumbersVLineLight,
        IdeaCodeColors.Colors.defaultTextLight,
        Colors.findusagesBgLight,
        Colors.findusagesBgLight
    );
  }

  public static ToolbarItemColors lightNoUsages() {
    return ToolbarItemColors.lightToolbarItemColors();
  }
}
