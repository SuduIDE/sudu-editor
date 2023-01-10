package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public class EditorColorScheme {
  public final Color editBgColor = new Color(IdeaCodeColors.Colors.editBgColor);
  public final Color editNumbersVLine = new Color(IdeaCodeColors.Colors.editNumbersVLine);
  public final Color editFooterFill = new Color(IdeaCodeColors.Colors.editFooterFill);
  public final Color codeLineTailColor = new Color(editBgColor);
  public final Color selectionBgColor = new Color(IdeaCodeColors.Colors.editSelectedBg);

  public final CodeElementColor[] codeColors = IdeaCodeColors.codeElementColors();
  public final LineNumbersColors lineNumbersColors = IdeaCodeColors.lineNumberColors();

  public Color bgColor(Color bg) {
    return bg != null ? bg : editBgColor;
  }
}
