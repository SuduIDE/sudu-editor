package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.Color;

public class DialogItemColors {

  public final ShadowParameters shadowParameters;
  public final WindowTheme windowTheme;
  public final FindUsagesItemColors findUsagesColors;
  public final ToolbarItemColors findUsagesColorsError;
  public final ToolbarItemColors toolbarItemColors;

  public static DialogItemColors darculaColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darculaFindUsagesItemColors(),
        FindUsagesItemColors.darculaNoUsages(),
        windowDarcula(),
        ToolbarItemColors.darculaToolbarItemColors(),
        ShadowParameters.darculaTheme()
    );
  }

  public static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.lightFindUsagesItemColors(),
        FindUsagesItemColors.lightNoUsages(),
        windowLight(),
        ToolbarItemColors.lightToolbarItemColors(),
        ShadowParameters.lightTheme()
    );
  }

  static WindowTheme windowDarcula() {
    return new WindowTheme(
        new Color("#616161"),
        new Color("#393B40"),
        new Color("#DFE1E5"));
  }

  static WindowTheme windowLight() {
    return new WindowTheme(
        new Color("#B9BDC9"),
        new Color("#F7F8FA"),
        new Color(0));
  }

  public DialogItemColors(
      FindUsagesItemColors findUsagesItemColors,
      ToolbarItemColors noUsagesColors,
      WindowTheme windowTheme,
      ToolbarItemColors toolbarItemColors,
      ShadowParameters shadowParameters
  ) {
    this.findUsagesColors = findUsagesItemColors;
    this.findUsagesColorsError = noUsagesColors;
    this.windowTheme = windowTheme;
    this.toolbarItemColors = toolbarItemColors;
    this.shadowParameters = shadowParameters;
  }
}
