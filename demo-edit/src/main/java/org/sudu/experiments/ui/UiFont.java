package org.sudu.experiments.ui;

import java.util.Objects;

public class UiFont {
  public String familyName;
  public float size;

  public UiFont(String familyName, float size) {
    this.familyName = familyName;
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return equals((UiFont) o);
  }

  public boolean equals(UiFont uiFont) {
    return size == uiFont.size && Objects.equals(familyName, uiFont.familyName);
  }
}
