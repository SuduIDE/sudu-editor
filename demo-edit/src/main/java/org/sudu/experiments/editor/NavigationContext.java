package org.sudu.experiments.editor;

import org.sudu.experiments.parser.common.Pos;

public class NavigationContext {
  private final Pos pos;
  private final Selection selection;

  public NavigationContext(int line, int charPos, Selection selection) {
    this.pos = new Pos(line, charPos);
    this.selection = new Selection(selection);
    this.selection.isSelectionStarted = false;
  }

  public int getLine() {
    return pos.line;
  }

  public int getCharPos() {
    return pos.charPos;
  }

  public Selection getSelection() {
    return selection;
  }
}
