package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public class FindUsagesItemColors {
  final V4f fileColor;
  final V4f lineColor;
  final V4f contentColor;
  final V4f bgColor;
  final V4f bgHighlightColor;
  final V4f textHighlightColor = new Color(255);

  public FindUsagesItemColors(V4f fileColor, V4f lineColor, V4f contentColor, V4f bgColor, V4f bgHighlightColor) {
    this.fileColor = fileColor;
    this.lineColor = lineColor;
    this.contentColor = contentColor;
    this.bgColor = bgColor;
    this.bgHighlightColor = bgHighlightColor;
  }

  public static FindUsagesItemColors darkFindUsagesItemColors() {
    return new FindUsagesItemColors(
        IdeaCodeColors.Colors.defaultText,
        IdeaCodeColors.Colors.editNumbersVLine,
        new Color("#DFE1E5"),
        Colors.findUsagesBg,
        Colors.findUsagesSelectedBg
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

  public static ToolbarItemColors lightNoUsages() {
    return ToolbarItemColors.lightToolbarItemColors();
  }
}
