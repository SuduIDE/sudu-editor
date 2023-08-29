package org.sudu.experiments.demo.ui;

public class FindUsagesItemData {
  final Runnable action;
  final String lineNumber;
  final String codeContent;
  final String fileName;

  public FindUsagesItemData(Runnable r, String lineNumber, String codeContent, String fileName) {
    this.action = r;
    this.lineNumber = lineNumber;
    this.codeContent = codeContent;
    this.fileName = fileName;
  }
}
