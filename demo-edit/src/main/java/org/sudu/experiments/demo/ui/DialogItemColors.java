package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.Color;

public class DialogItemColors {

  public final FindUsagesItemColors findUsagesColors;
  public final FindUsagesItemColors findUsagesColorsContinued;
  public final FindUsagesItemColors findUsagesColorsError;
  public final Color findUsagesColorBorder;

  public final ToolbarItemColors toolbarItemColors;

  public static DialogItemColors darkColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darkFindUsagesItemColors(),
        FindUsagesItemColors.darkFindUsagesItemColorsExtraLine(),
        FindUsagesItemColors.darkFindUsagesItemColorsError(),
        new Color("#616161"),
        ToolbarItemColors.darkToolbarItemColors()
    );
  }

  public static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.lightFindUsagesItemColors(),
        FindUsagesItemColors.lightFindUsagesItemColorsExtraLine(),
        FindUsagesItemColors.lightFindUsagesItemColorsError(),
        new Color("#B9BDC9"),
        ToolbarItemColors.lightToolbarItemColors()
    );
  }

  public DialogItemColors(
      FindUsagesItemColors findUsagesItemColors,
      FindUsagesItemColors findUsagesColorsContinued,
      FindUsagesItemColors findUsagesColorsError,
      Color findUsagesColorsBorder,
      ToolbarItemColors toolbarItemColors
  ) {
    this.findUsagesColors = findUsagesItemColors;
    this.findUsagesColorsContinued = findUsagesColorsContinued;
    this.findUsagesColorsError = findUsagesColorsError;
    this.findUsagesColorBorder = findUsagesColorsBorder;
    this.toolbarItemColors = toolbarItemColors;
  }
}
