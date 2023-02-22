package org.sudu.experiments.parser.java.model;

import java.util.Objects;

public class JavaMethodField {

  public String name;
  public boolean isStatic;

  public JavaMethodField(String name, boolean isStatic) {
    this.name = name;
    this.isStatic = isStatic;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JavaMethodField javaField = (JavaMethodField) o;
    return isStatic == javaField.isStatic && Objects.equals(name, javaField.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, isStatic);
  }
}
