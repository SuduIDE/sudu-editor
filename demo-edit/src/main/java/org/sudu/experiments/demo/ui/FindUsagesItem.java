package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.TextRect;

public class FindUsagesItem {
  final TextRect tFiles = new TextRect();
  final TextRect tLines = new TextRect();
  final TextRect tContent = new TextRect();
  final DemoRect rectFiles = new DemoRect();
  final DemoRect rectLines = new DemoRect();
  final DemoRect rectContent = new DemoRect();
  final Runnable action;
  FindUsagesItemColors colors;
  String fileName, lineNumber, codeContent;

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
    rectFiles.color.set(colors.bgColor);
    rectLines.color.set(colors.bgColor);
    rectContent.color.set(colors.bgColor);
  }

  public void setHover(boolean b) {
    tFiles.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    tLines.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    tContent.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    rectFiles.color.set(b ? colors.bgHighlight : colors.bgColor);
    rectLines.color.set(b ? colors.bgHighlight : colors.bgColor);
    rectContent.color.set(b ? colors.bgHighlight : colors.bgColor);
  }

  public void setTheme(FindUsagesItemColors findUsagesItemColors) {
    colors = findUsagesItemColors;
    tFiles.setColors(
        findUsagesItemColors.fileColor,
        findUsagesItemColors.bgColor
    );
    tLines.setColors(
        findUsagesItemColors.lineColor,
        findUsagesItemColors.bgColor
    );
    tContent.setColors(
        findUsagesItemColors.contentColor,
        findUsagesItemColors.bgColor
    );
    rectFiles.bgColor.set(findUsagesItemColors.bgColor);
    rectLines.bgColor.set(findUsagesItemColors.bgColor);
    rectContent.bgColor.set(findUsagesItemColors.bgColor);
  }
}
