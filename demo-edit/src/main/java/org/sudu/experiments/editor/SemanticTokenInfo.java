package org.sudu.experiments.editor;

public class SemanticTokenInfo {

  public int line, startCharPos;
  public int tokenType, tokenStyle;
  public String foreground, background;
  public String text;

  public SemanticTokenInfo(
      int line, int startCharPos,
      int tokenType, int tokenStyle,
      String foreground, String background,
      String text
  ) {
    this.line = line;
    this.startCharPos = startCharPos;
    this.tokenType = tokenType;
    this.tokenStyle = tokenStyle;
    this.foreground = foreground;
    this.background = background;
    this.text = text;
  }

  public boolean hasColor() {
    return foreground != null || background != null;
  }
}
