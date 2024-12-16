package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.ColorOp;

public class DiffColors {
  public Color deletedColor;
  public Color insertedColor;
  public Color editedColor;

  // edited element on edited region
  public Color editedColor2;

  public DiffColors(DiffColors c) {
    this(c.deletedColor, c.insertedColor, c.editedColor, c.editedColor2);
  }

  DiffColors(Color deletedColor, Color insertedColor, Color editedColor) {
    this(deletedColor, insertedColor, editedColor, editedColor);
  }

  DiffColors(
      Color deletedColor, Color insertedColor,
      Color editedColor, Color editedColor2
  ) {
    this.deletedColor = deletedColor;
    this.insertedColor = insertedColor;
    this.editedColor = editedColor;
    this.editedColor2 = editedColor2;
  }

  public static DiffColors codeDiffDarcula() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#303C47"),
        new Color("#385570")
    );
  }

  public static DiffColors codeDiffDark() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#283541"),
        new Color("#385570")
    );
  }

  public static DiffColors codeDiffLight() {
    return new DiffColors(
        new Color("#D6D6D6"),
        new Color("#BEE6BE"),
        new Color("#E7EFFA"),
        new Color("#C2D8F2")
    );
  }

  public static DiffColors fileTreeDark() {
    return new DiffColors(
        new Color("#6F737A"),
        new Color("#73BD79"),
        new Color("#70AEFF")
    );
  }

  public static DiffColors fileTreeLight() {
    return new DiffColors(
        new Color("#6C707E"),
        new Color("#067D17"),
        new Color("#0033B3")
    );
  }

  public static DiffColors fileTreeDarcula() {
    return new DiffColors(
        new Color("#6C6C6C"),
        new Color("#629755"),
        new Color("#6897BB")
    );
  }

  public Color getDiffColor(int elementType, int lineType, Color defaultBg) {
    int type = elementType != 0 ? elementType : lineType;
    return switch (type) {
      case DiffTypes.DELETED -> deletedColor;
      case DiffTypes.INSERTED -> insertedColor;
      case DiffTypes.EDITED ->
          lineType == type ? editedColor2 : editedColor;
      default -> defaultBg;
    };
  }

  public Color getDiffColor(int type, Color bg) {
    return switch (type) {
      case DiffTypes.DELETED -> deletedColor;
      case DiffTypes.INSERTED -> insertedColor;
      case DiffTypes.EDITED -> editedColor;
      case DiffTypes.EDITED2 -> editedColor2;
      default -> bg;
    };
  }

  public Color getDiffColor(EditorColorScheme colorScheme, int diffType) {
    return getDiffColor(diffType, colorScheme.editor.bg);
  }

  public DiffColors blendWith(Color color) {
    return new DiffColors(
        ColorOp.blend(deletedColor, color),
        ColorOp.blend(insertedColor, color),
        ColorOp.blend(editedColor, color),
        ColorOp.blend(editedColor2, color)
    );
  }
}
