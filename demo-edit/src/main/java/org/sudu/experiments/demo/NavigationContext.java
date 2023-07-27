package org.sudu.experiments.demo;

import org.sudu.experiments.parser.common.Pos;

public class NavigationContext {
  private final Pos pos;
  private final Selection selection;

  public NavigationContext(Pos pos, Selection selection) {
    this.pos = pos;
    this.selection = selection;
  }

  public int getLine() {
    return pos.line;
  }

  public int getCharPos() {
    return pos.pos;
  }

  public Selection getSelection() {
    return selection;
  }
}
