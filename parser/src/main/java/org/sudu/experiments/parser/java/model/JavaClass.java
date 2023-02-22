package org.sudu.experiments.parser.java.model;

import java.util.ArrayList;
import java.util.Objects;

public class JavaClass {

  public String className;
  public ArrayList<JavaMethodField> fields;
  public ArrayList<JavaMethodField> methods;
  public ArrayList<JavaClass> nestedClasses;
  public JavaClass innerClass;

  public int nestPos = 0;

  public JavaClass(String className, JavaClass innerClass) {
    this.className = className;
    this.innerClass = innerClass;

    fields = new ArrayList<>();
    methods = new ArrayList<>();
    nestedClasses = new ArrayList<>();
  }

  public JavaClass(String className) {
    this(className, null);
  }

  public JavaMethodField getField(String fieldName) {
    for (var field: fields) {
      if (field.name.equals(fieldName)) return field;
    }
    if (innerClass != null) return innerClass.getField(fieldName);
    return null;
  }

  public JavaMethodField getMethod(String methodName) {
    for (var method: methods) {
      if (method.name.equals(methodName)) return method;
    }
    if (innerClass != null) return innerClass.getMethod(methodName);
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JavaClass javaClass = (JavaClass) o;
    return Objects.equals(className, javaClass.className) && Objects.equals(fields, javaClass.fields) && Objects.equals(nestedClasses, javaClass.nestedClasses) && Objects.equals(innerClass, javaClass.innerClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className, innerClass);
  }
}
