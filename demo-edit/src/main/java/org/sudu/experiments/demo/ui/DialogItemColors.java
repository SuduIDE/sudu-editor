package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.Color;

public class DialogItemColors {

  public final FindUsagesItemColors findUsagesColors;
  public final ToolbarItemColors findUsagesColorsError;
  public final Color windowBorderColor;
  public final Color windowTitleBgColor;
  public final Color windowTitleTextColor;
  public final ToolbarItemColors toolbarItemColors;
  public final ShadowParameters shadowParameters;

  public static DialogItemColors darkColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darkFindUsagesItemColors(),
        FindUsagesItemColors.darkNoUsages(),
        new Color("#616161"),
        new Color("#393B40"),
        new Color("#DFE1E5"), // same color for usages text color
        ToolbarItemColors.darkToolbarItemColors(),
        ShadowParameters.darkTheme()
    );
  }

  public static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.lightFindUsagesItemColors(),
        FindUsagesItemColors.lightNoUsages(),
        new Color("#B9BDC9"),
        new Color("#F7F8FA"),
        new Color(0),
        ToolbarItemColors.lightToolbarItemColors(),
        ShadowParameters.lightTheme()
    );
  }

  public DialogItemColors(
      FindUsagesItemColors findUsagesItemColors,
      ToolbarItemColors noUsagesColors,
      Color windowBorderColor,
      Color windowTitleBgColor,
      Color windowTitleTextColor,
      ToolbarItemColors toolbarItemColors,
      ShadowParameters shadowParameters
  ) {
    this.findUsagesColors = findUsagesItemColors;
    this.findUsagesColorsError = noUsagesColors;
    this.windowBorderColor = windowBorderColor;
    this.windowTitleBgColor = windowTitleBgColor;
    this.windowTitleTextColor = windowTitleTextColor;
    this.toolbarItemColors = toolbarItemColors;
    this.shadowParameters = shadowParameters;
  }
}
