package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class FileTreeViewTheme {
  public final Color bg;
  public final Color selectedBg;
  public final Color inactiveSelectedBg;
  public final Color hoveredBg;

  public FileTreeViewTheme(Color bg, Color selectedBg, Color inactiveSelectedBg, Color hoveredBg) {
    this.bg = bg;
    this.selectedBg = selectedBg;
    this.inactiveSelectedBg = inactiveSelectedBg;
      this.hoveredBg = hoveredBg;
  }

  public static FileTreeViewTheme darculaIdea() {
    return new FileTreeViewTheme(
            new Color("#2B2D30"), new Color("#04395e"),
            new Color("#37373d"), new Color("#ffffff11"));
  }

  public static FileTreeViewTheme darkIdea() {
    return new FileTreeViewTheme(
            new Color("#3C3F41"), new Color("#04395e"),
            new Color("#37373d"), new Color("#ffffff11"));
  }

  public static FileTreeViewTheme lightIdea() {
    return new FileTreeViewTheme(
            new Color("#F7F8FA"), new Color("#0060c0"),
            new Color("#e4e6f1"), new Color("#00000011"));
  }
}
