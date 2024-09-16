package org.sudu.experiments.ui;

import org.sudu.experiments.fonts.FontDesk;

import java.util.Objects;

public class UiFont {
  public String familyName;
  public float size;
  public int weightRegular;
  public int weightBold;

  public UiFont(String familyName, float size) {
    this(familyName, size, false);
  }

  public UiFont(String familyName, float size, boolean light) {
    this(familyName, size,
        light ? FontDesk.WEIGHT_LIGHT : FontDesk.WEIGHT_REGULAR,
        light ? FontDesk.WEIGHT_SEMI_BOLD : FontDesk.WEIGHT_BOLD);
  }

  public UiFont(String familyName, float size, int weightRegular, int weightBold) {
    this.familyName = familyName;
    this.size = size;
    this.weightRegular = weightRegular;
    this.weightBold = weightBold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return equals((UiFont) o);
  }

  public boolean equals(UiFont uiFont) {
    return size == uiFont.size
        && weightRegular == uiFont.weightRegular
        && weightBold == uiFont.weightBold
        && Objects.equals(familyName, uiFont.familyName);
  }

  public UiFont withSize(float fontSize) {
    return new UiFont(familyName, fontSize, weightRegular, weightBold);
  }
}
