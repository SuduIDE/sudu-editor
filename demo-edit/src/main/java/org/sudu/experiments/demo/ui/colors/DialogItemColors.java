package org.sudu.experiments.demo.ui.colors;


import org.sudu.experiments.demo.ui.ShadowParameters;
import org.sudu.experiments.math.Color;

public class DialogItemColors {

  public final ShadowParameters shadowParameters;
  public final WindowColors windowColors;
  public final FindUsagesItemColors findUsagesColors;
  public final ToolbarItemColors findUsagesColorsError;
  public final ToolbarItemColors toolbarItemColors;
  public final Color dialogScrollLine;
  public final Color dialogScrollBg;

  public DialogItemColors(
      FindUsagesItemColors findUsagesItemColors,
      ToolbarItemColors noUsagesColors,
      WindowColors windowColors,
      ToolbarItemColors toolbarItemColors,
      ShadowParameters shadowParameters,
      Color scrollBarLine,
      Color scrollBarBg
  ) {
    this.findUsagesColors = findUsagesItemColors;
    this.findUsagesColorsError = noUsagesColors;
    this.windowColors = windowColors;
    this.toolbarItemColors = toolbarItemColors;
    this.shadowParameters = shadowParameters;
    this.dialogScrollLine = scrollBarLine;
    this.dialogScrollBg = scrollBarBg;
  }

  public static DialogItemColors darculaColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darculaFindUsagesItemColors(),
        FindUsagesItemColors.darculaNoUsages(),
        WindowColors.darcula(),
        ToolbarItemColors.darculaToolbarItemColors(),
        ShadowParameters.darculaTheme(),
        DialogColors.Darcula.scrollBarLine,
        DialogColors.Darcula.scrollBarBg
    );
  }

  public static DialogItemColors darkColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.darkFindUsagesItemColors(),
        FindUsagesItemColors.darkNoUsages(),
        WindowColors.dark(),
        ToolbarItemColors.darkToolbarItemColors(),
        ShadowParameters.darkTheme(),
        DialogColors.Dark.scrollBarLine,
        DialogColors.Dark.scrollBarBg
    );
  }

  public static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        FindUsagesItemColors.lightFindUsagesItemColors(),
        FindUsagesItemColors.lightNoUsages(),
        WindowColors.light(),
        ToolbarItemColors.lightToolbarItemColors(),
        ShadowParameters.lightTheme(),
        DialogColors.Light.scrollBarLine,
        DialogColors.Light.scrollBarBg
    );
  }
}
