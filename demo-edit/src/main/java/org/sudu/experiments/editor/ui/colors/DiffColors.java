package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.math.Color;

public class DiffColors {
  public final Color deletedBgColor;
  public final Color insertedBgColor;
  public final Color editedBgColor;
  public final Color editedBgPaleColor;

  DiffColors(
      Color deletedBgColor, Color insertedBgColor,
      Color editedBgColor, Color editedBgPaleColor
  ) {
    this.deletedBgColor = deletedBgColor;
    this.insertedBgColor = insertedBgColor;
    this.editedBgColor = editedBgColor;
    this.editedBgPaleColor = editedBgPaleColor;
  }

  public static DiffColors darcula() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#385570"),
        new Color("#303C47")
    );
  }

  public static DiffColors dark() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#385570"),
        new Color("#283541")
    );
  }

  public static DiffColors light() {
    return new DiffColors(
        new Color("#D6D6D6"),
        new Color("#BEE6BE"),
        new Color("#C2D8F2"),
        new Color("#E7EFFA")
    );
  }

  public Color getDiffColor(int elementType, int lineType, Color defaultBg) {
    if (elementType != 0) lineType = elementType;
    return switch (lineType) {
      case DiffTypes.DELETED -> deletedBgColor;
      case DiffTypes.INSERTED -> insertedBgColor;
      case DiffTypes.EDITED ->
          elementType == DiffTypes.EDITED ? editedBgColor : editedBgPaleColor;
      default -> defaultBg;
    };
  }

  public Color getDiffColor(int lineType, Color defaultBg) {
    return getDiffColor(0, lineType, defaultBg);
  }

  public Color getDiffColor(EditorColorScheme colorScheme, int lineType) {
    return getDiffColor(lineType, colorScheme.editor.bg);
  }
}
