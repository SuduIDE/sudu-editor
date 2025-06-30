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

  // excluded color
  public Color excludedColor;

  public DiffColors(DiffColors c) {
    this(c.deletedColor, c.insertedColor, c.editedColor, c.editedColor2, c.excludedColor);
  }

  DiffColors(Color deletedColor, Color insertedColor, Color editedColor, Color excludedColor) {
    this(deletedColor, insertedColor, editedColor, editedColor, excludedColor);
  }

  DiffColors(
      Color deletedColor, Color insertedColor,
      Color editedColor, Color editedColor2,
      Color excludedColor
  ) {
    this.deletedColor = deletedColor;
    this.insertedColor = insertedColor;
    this.editedColor = editedColor;
    this.editedColor2 = editedColor2;
    this.excludedColor = excludedColor;
  }

  public static DiffColors codeMapVSCode() {
    return new DiffColors(
        new Color("#f14c4c"),
        new Color("#487e01"),
        new Color("#1b81a8"),
        null
    );
  }

  public static DiffColors codeDiffDarcula() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#303C47"),
        new Color("#385570"),
        new Color("#848504")
    );
  }

  public static DiffColors codeDiffDark() {
    return new DiffColors(
        new Color("#484A4A"),
        new Color("#294436"),
        new Color("#283541"),
        new Color("#385570"),
        new Color("#D69A6B")
    );
  }

  public static DiffColors codeMapDark() {
    return new DiffColors(
        new Color("#656E76"),
        new Color("#447152"),
        new Color("#43698D"),
        new Color("#D69A6B")
    );
  }
  public static DiffColors codeMapDarcula() {
    return codeMapDark();
  }

  public static DiffColors codeMapLight() {
    return new DiffColors(
        new Color("#C8C8C8"),
        new Color("#AADEAA"),
        new Color("#B6D2F2"),
        new Color("#8C4F00")
    );
  }

  public static DiffColors codeDiffLight() {
    return new DiffColors(
        new Color("#D6D6D6"),
        new Color("#BEE6BE"),
        new Color("#E7EFFA"),
        new Color("#C2D8F2"),
        new Color("#8C4F00")
    );
  }

  public static DiffColors fileTreeDark() {
    return new DiffColors(
        new Color("#6F737A"),
        new Color("#73BD79"),
        new Color("#70AEFF"),
        new Color("#D69A6B")
    );
  }

  public static DiffColors fileTreeLight() {
    return new DiffColors(
        new Color("#6C707E"),
        new Color("#067D17"),
        new Color("#0033B3"),
        new Color("#8C4F00")
    );
  }

  public static DiffColors fileTreeDarcula() {
    return new DiffColors(
        new Color("#6C6C6C"),
        new Color("#629755"),
        new Color("#6897BB"),
        new Color("#848504")
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

  public Color getDiffColor(int type, boolean excluded, Color bg) {
    if (excluded) return excludedColor;
    return switch (type) {
      case DiffTypes.DELETED -> deletedColor;
      case DiffTypes.INSERTED -> insertedColor;
      case DiffTypes.EDITED -> editedColor;
      case DiffTypes.EDITED2 -> editedColor2;
      default -> bg;
    };
  }

  public Color getDiffColor(int type, Color bg) {
    return getDiffColor(type, false, bg);
  }

  public Color getDiffColor(EditorColorScheme colorScheme, int diffType) {
    return getDiffColor(diffType, false, colorScheme.editor.bg);
  }

  public DiffColors blendWith(Color color) {
    return new DiffColors(
        ColorOp.blend(deletedColor, color),
        ColorOp.blend(insertedColor, color),
        ColorOp.blend(editedColor, color),
        ColorOp.blend(editedColor2, color),
        ColorOp.blend(excludedColor, color)
    );
  }
}
