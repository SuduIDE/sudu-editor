package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class FileTreeViewTheme {
  public final Color bg;

  public FileTreeViewTheme(Color bg) {
    this.bg = bg;
  }

  public static FileTreeViewTheme darculaIdea() {
    return new FileTreeViewTheme(
        new Color("#2B2D30")
    );
  }

  public static FileTreeViewTheme darkIdea() {
    return new FileTreeViewTheme(
        new Color("#3C3F41")
    );
  }

  public static FileTreeViewTheme lightIdea() {
    return new FileTreeViewTheme(
        new Color("#F7F8FA")
    );
  }
}
