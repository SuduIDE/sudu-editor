package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.java.ParserConstants;

public class EditorColorScheme {
  public final Color editBgColor;
  public final Color editNumbersVLine;
  public final Color editFooterFill;
  public final Color codeLineTailColor;
  public final Color selectionBgColor;

  public final CodeElementColor[] codeColors;
  public final LineNumbersColors lineNumbersColors;

  public static EditorColorScheme darkIdeaColorScheme() {
    return new EditorColorScheme(
        new Color(IdeaCodeColors.Colors.editBgColor),
        new Color(IdeaCodeColors.Colors.editNumbersVLine),
        new Color(IdeaCodeColors.Colors.editFooterFill),
        new Color(IdeaCodeColors.Colors.editBgColor),
        new Color(IdeaCodeColors.Colors.editSelectedBg),
        IdeaCodeColors.codeElementColors(),
        IdeaCodeColors.lineNumberColors()
    );
  }

  public static EditorColorScheme lightIdeaColorScheme() {
    return new EditorColorScheme(
        new Color(IdeaCodeColors.Colors.editBgColorLight),
        new Color(IdeaCodeColors.Colors.editNumbersVLineLight),
        new Color(IdeaCodeColors.Colors.editFooterFill),
        new Color(IdeaCodeColors.Colors.editBgColorLight),
        new Color(IdeaCodeColors.Colors.editSelectedBgLight),
        IdeaCodeColors.codeElementColorsLight(),
        IdeaCodeColors.lineNumberColorsLight()
    );
  }

  private EditorColorScheme(
      Color editBgColor,
      Color editNumbersVLine,
      Color editFooterFill,
      Color codeLineTailColor,
      Color selectionBgColor,
      CodeElementColor[] codeColors,
      LineNumbersColors lineNumbersColors
  ) {
    this.editBgColor = editBgColor;
    this.editNumbersVLine = editNumbersVLine;
    this.editFooterFill = editFooterFill;
    this.codeLineTailColor = codeLineTailColor;
    this.selectionBgColor = selectionBgColor;
    this.codeColors = codeColors;
    this.lineNumbersColors = lineNumbersColors;
    if (codeColors.length < ParserConstants.TokenTypes.TYPES_LENGTH) {
      throw new IllegalArgumentException();
    }
  }

  public Color bgColor(Color bg) {
    return bg != null ? bg : editBgColor;
  }
}
