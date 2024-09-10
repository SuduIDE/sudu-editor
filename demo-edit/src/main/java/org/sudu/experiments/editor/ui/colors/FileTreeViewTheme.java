package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class FileTreeViewTheme {
  public final Color bg;
  public final Color selectedBg;

  public FileTreeViewTheme(Color bg, Color selectedBg) {
    this.bg = bg;
    this.selectedBg = selectedBg;
  }

  public static FileTreeViewTheme darculaIdea() {
    return new FileTreeViewTheme(
        new Color("#2B2D30"), new Color("#04395e")
    );
  }

  public static FileTreeViewTheme darkIdea() {
    return new FileTreeViewTheme(
        new Color("#3C3F41"), new Color("#04395e")
    );
  }

  public static FileTreeViewTheme lightIdea() {
    return new FileTreeViewTheme(
        new Color("#F7F8FA"), new Color("#04395e")
    );
  }
}
