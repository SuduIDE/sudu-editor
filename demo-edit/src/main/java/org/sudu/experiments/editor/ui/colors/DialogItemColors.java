package org.sudu.experiments.editor.ui.colors;


import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.ShadowParameters;
import org.sudu.experiments.ui.ToolbarItemColors;
import org.sudu.experiments.ui.WindowColors;

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

}
