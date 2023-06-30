package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.EditorColorScheme;
import org.sudu.experiments.demo.TextRect;

public class FindUsagesItem {
  final TextRect tFiles = new TextRect();
  final TextRect tLines = new TextRect();
  final TextRect tContent = new TextRect();
  final Runnable action;
  FindUsagesItemColors colors;
  String fileName, lineNumber, codeContent;
  boolean isHover = false;

  public FindUsagesItem(Runnable r, String fileName, String lineNumber, String codeContent, FindUsagesItemColors colors) {
    this.fileName = fileName;
    this.lineNumber = lineNumber;
    this.codeContent = codeContent;
    this.colors = colors;
    action = r;
    tFiles.color.set(colors.fileColor);
    tFiles.bgColor.set(colors.bgColor);
    tLines.color.set(colors.lineColor);
    tLines.bgColor.set(colors.bgColor);
    tContent.color.set(colors.contentColor);
    tContent.bgColor.set(colors.bgColor);
  }

  public void setHover(boolean b) {
    tFiles.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    tLines.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    tContent.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    isHover = b;
  }

  public void setTheme(EditorColorScheme scheme) {
      colors = scheme.dialogItemColor.findUsagesColors;
      tFiles.setColors(
          scheme.dialogItemColor.findUsagesColors.fileColor,
          scheme.dialogItemColor.findUsagesColors.bgColor
      );
      tLines.setColors(
          scheme.dialogItemColor.findUsagesColors.lineColor,
          scheme.dialogItemColor.findUsagesColors.bgColor
      );
      tContent.setColors(
          scheme.dialogItemColor.findUsagesColors.contentColor,
          scheme.dialogItemColor.findUsagesColors.bgColor
      );
      if (isHover) setHover(true);
  }
}
