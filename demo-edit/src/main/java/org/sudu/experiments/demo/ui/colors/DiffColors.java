package org.sudu.experiments.demo.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public class DiffColors {
  public final V4f deletedBgColor;
  public final V4f insertedBgColor;
  public final V4f editedBgColor;

  public static DiffColors darcula() {
    return new DiffColors(
        new Color("#CE11EF"),
        new Color("#63F27D"),
        new Color("#B35252")
    );
  }

  public static DiffColors dark() {
    return new DiffColors(
        new Color("#CE11EF"),
        new Color("#63F27D"),
        new Color("#B35252")
    );
  }

  public static DiffColors light() {
    return new DiffColors(
        new Color("#CE11EF"),
        new Color("#63F27D"),
        new Color("#B35252")
    );
  }

  DiffColors(
      V4f deletedBgColor, V4f insertedBgColor,
      V4f editedBgColor
  ) {
    this.deletedBgColor = deletedBgColor;
    this.insertedBgColor = insertedBgColor;
    this.editedBgColor = editedBgColor;
  }
}
