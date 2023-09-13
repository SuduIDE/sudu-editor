package org.sudu.experiments.demo.ui.colors;

import org.sudu.experiments.math.Color;

public class EditorColors {
  public final Color cursor;
  public final Color bg;
  public final Color numbersVLine;
  public final Color footerFill;
  public final Color lineTailContent;
  public final Color selectionBg;
  public final Color definitionBg;
  public final Color usageBg;
  public final Color scrollBarBg;
  public final Color scrollBarLine;
  public final Color caretBg;

  private EditorColors(
      Color cursor,
      Color bg,
      Color numbersVLine,
      Color footerFill,
      Color lineTailContent,
      Color selectionBg,
      Color definitionBg,
      Color usageBg,
      Color scrollBarBg,
      Color scrollBarLine,
      Color caretBg
  ) {
    this.cursor = cursor;
    this.bg = bg;
    this.numbersVLine = numbersVLine;
    this.footerFill = footerFill;
    this.lineTailContent = lineTailContent;
    this.selectionBg = selectionBg;
    this.definitionBg = definitionBg;
    this.usageBg = usageBg;
    this.scrollBarBg = scrollBarBg;
    this.scrollBarLine = scrollBarLine;
    this.caretBg = caretBg;
  }

  public static EditorColors darculaIdeaColorScheme() {
    return new EditorColors(
        new Color(IdeaCodeColors.Darcula.cursor),
        new Color(IdeaCodeColors.Darcula.editBg),
        new Color(IdeaCodeColors.Darcula.editNumbersVLine),
        new Color(IdeaCodeColors.Darcula.editFooterFill),
        new Color(IdeaCodeColors.Darcula.editBg),
        new Color(IdeaCodeColors.Darcula.editSelectedBg),
        new Color(IdeaCodeColors.Darcula.definitionBgColor),
        new Color(IdeaCodeColors.Darcula.usageBgColor),
        new Color(IdeaCodeColors.Darcula.scrollBarBg),
        new Color(IdeaCodeColors.Darcula.scrollBarLine),
        new Color(IdeaCodeColors.Darcula.caretBg)
    );
  }

  public static EditorColors darkIdeaColorScheme() {
    return new EditorColors(
        new Color(IdeaCodeColors.Dark.cursor),
        new Color(IdeaCodeColors.Dark.editBg),
        new Color(IdeaCodeColors.Dark.editNumbersVLine),
        new Color(IdeaCodeColors.Dark.editFooterFill),
        new Color(IdeaCodeColors.Dark.editBg),
        new Color(IdeaCodeColors.Dark.editSelectedBg),
        new Color(IdeaCodeColors.Dark.definitionBgColor),
        new Color(IdeaCodeColors.Dark.usageBgColor),
        new Color(IdeaCodeColors.Dark.scrollBarBg),
        new Color(IdeaCodeColors.Dark.scrollBarLine),
        new Color(IdeaCodeColors.Dark.caretBg)
    );
  }

  public static EditorColors lightIdeaColorScheme() {
    return new EditorColors(
        new Color(IdeaCodeColors.Light.cursor),
        new Color(IdeaCodeColors.Light.editBg),
        new Color(IdeaCodeColors.Light.editNumbersVLine),
        new Color(IdeaCodeColors.Light.editFooterFill),
        new Color(IdeaCodeColors.Light.editBg),
        new Color(IdeaCodeColors.Light.editSelectedBg),
        new Color(IdeaCodeColors.Light.definitionBgColor),
        new Color(IdeaCodeColors.Light.usageBgColor),
        new Color(IdeaCodeColors.Light.scrollBarBg),
        new Color(IdeaCodeColors.Light.scrollBarLine),
        new Color(IdeaCodeColors.Light.caretBg)
    );
  }
}
