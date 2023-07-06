package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.Color;

public class DialogItemColors {

  public final FindUsagesItemColors findUsagesColors;
  public final FindUsagesItemColors findUsagesColorsContinued;
  public final ToolbarItemColors findUsagesColorsError;
  public final Color dialogBorderColor;
  public final ToolbarItemColors toolbarItemColors;
  public final ShadowParameters shadowParameters;

  public static DialogItemColors darkColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darkFindUsagesItemColors(),
        FindUsagesItemColors.darkFindUsagesItemColorsExtraLine(),
        FindUsagesItemColors.darkNoUsages(),
        new Color("#616161"),
        ToolbarItemColors.darkToolbarItemColors(),
        ShadowParameters.darkTheme()
    );
  }

  public static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.lightFindUsagesItemColors(),
        FindUsagesItemColors.lightFindUsagesItemColorsExtraLine(),
        FindUsagesItemColors.lightNoUsages(),
        new Color("#B9BDC9"),
        ToolbarItemColors.lightToolbarItemColors(),
        ShadowParameters.lightTheme()
    );
  }

  public DialogItemColors(
      FindUsagesItemColors findUsagesItemColors,
      FindUsagesItemColors findUsagesColorsContinued,
      ToolbarItemColors noUsagesColors,
      Color dialogBorderColor,
      ToolbarItemColors toolbarItemColors,
      ShadowParameters shadowParameters
  ) {
    this.findUsagesColors = findUsagesItemColors;
    this.findUsagesColorsContinued = findUsagesColorsContinued;
    this.findUsagesColorsError = noUsagesColors;
    this.dialogBorderColor = dialogBorderColor;
    this.toolbarItemColors = toolbarItemColors;
    this.shadowParameters = shadowParameters;
  }
}
