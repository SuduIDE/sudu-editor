package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

public class FileTreeViewTheme {
  public Color bg;
  public Color selectedBg;
  public Color textColor;
  public DiffColors textDiffColors;
  public Color selectedText;
  public Color inactiveSelectedBg;
  public Color hoveredBg;

  public FileTreeViewTheme(
      Color bg, Color selectedBg,
      Color textColor,
      Color selectedText,
      Color inactiveSelectedBg, Color hoveredBg,
      DiffColors textDiffColors
  ) {
    this.bg = bg;
    this.selectedBg = selectedBg;
    this.textColor = textColor;
    this.textDiffColors = textDiffColors;
    this.selectedText = selectedText;
    this.inactiveSelectedBg = inactiveSelectedBg;
    this.hoveredBg = hoveredBg;
  }

  public static FileTreeViewTheme darculaIdea() {
    return new FileTreeViewTheme(
        new Color("#3C3F41"),
        new Color("#4B6EAF"),
        new Color("#BBBBBB"),
        new Color("#DEDEDE"),
        new Color("#0D293E"),
        new Color("#ffffff11"),
        DiffColors.fileTreeDarcula());
  }

  public static FileTreeViewTheme darkIdea() {
    return new FileTreeViewTheme(
        new Color("#2B2D30"),
        new Color("#2E436E"),
        new Color("#DFE1E5"),
        new Color("#DFE1E5"),
        new Color("#43454A"),
        new Color("#ffffff11"),
        DiffColors.fileTreeDark());
  }

  public static FileTreeViewTheme lightIdea() {
    return new FileTreeViewTheme(
        new Color("#F7F8FA"),
        new Color("#D4E2FF"),
        new Color("#000000"),
        new Color("#000000"),
        new Color("#DFE1E5"),
        new Color("#00000011"),
        DiffColors.fileTreeLight()
    );
  }
}
