package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaClass extends Decl{

  public List<Field> fields;
  public List<Method> methods;
  public ArrayList<JavaClass> nestedClasses;
  public JavaClass innerClass;

  public int nestPos = 0;

  public JavaClass(String name, Pos position, JavaClass innerClass) {
    super(name, position);
    this.innerClass = innerClass;
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.nestedClasses = new ArrayList<>();
  }

  public Field getField(String fieldName) {
    for (var field: fields)
      if (field.name.equals(fieldName)) return field;
    if (innerClass != null) return innerClass.getField(fieldName);
    return null;
  }

  public Method getMethod(String methodName) {
    for (var method: methods)
      if (method.name.equals(methodName)) return method;
    if (innerClass != null) return innerClass.getMethod(methodName);
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JavaClass javaClass = (JavaClass) o;
    return nestPos == javaClass.nestPos && Objects.equals(fields, javaClass.fields) && Objects.equals(methods, javaClass.methods) && Objects.equals(nestedClasses, javaClass.nestedClasses) && Objects.equals(innerClass, javaClass.innerClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), fields, methods, nestedClasses, innerClass, nestPos);
  }
}
