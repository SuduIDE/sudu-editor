package org.sudu.experiments.editor;

public class SemanticTokenInfo {

  public int line, startCharPos;
  public int tokenType, tokenStyle;
  public String text;

  public SemanticTokenInfo(
      int line, int startCharPos,
      int tokenType, int tokenStyle,
      String text
  ) {
    this.line = line;
    this.startCharPos = startCharPos;
    this.tokenType = tokenType;
    this.tokenStyle = tokenStyle;
    this.text = text;
  }
}
