package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.editor.ui.colors.DialogColors.Darcula;
import org.sudu.experiments.editor.ui.colors.DialogColors.Dark;
import org.sudu.experiments.editor.ui.colors.DialogColors.Light;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.ShadowParameters;
import org.sudu.experiments.ui.ToolbarItemColors;
import org.sudu.experiments.ui.WindowColors;

public interface Themes {
  static DialogItemColors darculaColorScheme() {
    return new DialogItemColors(
        darculaFindUsagesItemColors(),
        darculaToolbarItemColors(),
        darculaWindowTheme(),
        darculaToolbarItemColors(),
        ShadowParameters.darculaTheme(),
        Darcula.scrollBarLine,
        Darcula.scrollBarBg
    );
  }

  static DialogItemColors darkColorScheme() {
    return new DialogItemColors(
        darkFindUsagesItemColors(),
        darkToolbarItemColors(),
        darkWindowTheme(),
        darkToolbarItemColors(),
        ShadowParameters.darkTheme(),
        Dark.scrollBarLine,
        Dark.scrollBarBg
    );
  }

  static DialogItemColors lightColorScheme() {
    return new DialogItemColors(
        lightFindUsagesItemColors(),
        lightToolbarItemColors(),
        lightWindowTheme(),
        lightToolbarItemColors(),
        ShadowParameters.lightTheme(),
        Light.scrollBarLine,
        Light.scrollBarBg
    );
  }

  static ToolbarItemColors darculaToolbarItemColors() {
    return new ToolbarItemColors(
        new Color("#BBBBBB"), Darcula.toolbarBg, Darcula.toolbarSelectedBg
    );
  }

  static ToolbarItemColors darkToolbarItemColors() {
    return new ToolbarItemColors(
        new Color("#DFE1E5"), Dark.toolbarBg, Dark.toolbarSelectedBg
    );
  }

  static ToolbarItemColors lightToolbarItemColors() {
    return new ToolbarItemColors(
        IdeaCodeColors.Light.defaultText, Light.toolbarBg, Light.toolbarSelectedBg
    );
  }

  /*
    Original color scheme:
          windowTitleBgColor = #3C3F41
          windowTitleTextColor = #BBBBBB
    Use different colors for better look
     */
  static WindowColors darculaWindowTheme() {
    return new WindowColors(
        new Color("#616161"),
        new Color("#393B40"),
        new Color("#DFE1E5"));
  }

  static WindowColors darkWindowTheme() {
    return new WindowColors(
        new Color("#43454A"),
        new Color("#393B40"),
        new Color("#DFE1E5"));
  }

  static WindowColors lightWindowTheme() {
    return new WindowColors(
        new Color("#B9BDC9"),
        new Color("#F7F8FA"),
        new Color(0));
  }

  static FindUsagesItemColors darculaFindUsagesItemColors() {
    return new FindUsagesItemColors(
        Darcula.findUsagesTextCaret,
        Darcula.findUsagesLineNumber,
        Darcula.findUsagesContent,
        Darcula.findUsagesBg,
        Darcula.findUsagesBgCaret,
        Darcula.findUsagesTextCaret
    );
  }

  static FindUsagesItemColors darkFindUsagesItemColors() {
    return new FindUsagesItemColors(
        Dark.findUsagesTextCaret,
        Dark.findUsagesLineNumber,
        Dark.findUsagesContent,
        Dark.findUsagesBg,
        Dark.findUsagesBgCaret,
        Dark.findUsagesTextCaret
    );
  }

  static FindUsagesItemColors lightFindUsagesItemColors() {
    return new FindUsagesItemColors(
        Light.findUsagesTextCaret,
        Light.findUsagesLineNumber,
        Light.findUsagesContent,
        Light.findUsagesBg,
        Light.findUsagesBgCaret,
        Light.findUsagesTextCaret
    );
  }
}
