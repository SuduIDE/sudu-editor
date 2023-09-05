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
      Color scrollBarLine
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
        new Color(IdeaCodeColors.Darcula.scrollBarLine)
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
        new Color(IdeaCodeColors.Light.scrollBarLine)
    );
  }
}
