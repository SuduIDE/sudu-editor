package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaClass extends Decl {

  public List<JavaField> fields;
  public List<JavaMethod> methods;
  public List<JavaConstructor> constructors;
  public List<JavaClass> nestedClasses;
  public JavaClass innerClass;

  public int nestPos = 0;

  public JavaClass(String name, Pos position, JavaClass innerClass) {
    super(name, position);
    this.innerClass = innerClass;
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.constructors = new ArrayList<>();
    this.nestedClasses = new ArrayList<>();
  }

  public JavaField getField(String fieldName) {
    for (var field: fields)
      if (field.name.equals(fieldName)) return field;
    if (innerClass != null) return innerClass.getField(fieldName);
    return null;
  }

  public JavaMethod getMethod(String methodName, List<String> argsTypes) {
    for (var method : methods) {
      if (method.match(methodName, argsTypes)) return method;
    }
    if (innerClass != null) return innerClass.getMethod(methodName, argsTypes);
    return null;
  }

  public JavaConstructor getConstructor(String methodName, List<String> argsTypes) {
    for (var constructor : constructors) {
      if (constructor.match(methodName, argsTypes)) return constructor;
    }
    if (innerClass != null) return innerClass.getConstructor(methodName, argsTypes);
    return null;
  }

  public JavaConstructor getThisConstructor(List<String> argsTypes) {
    for (var constructor : constructors) {
      if (constructor.matchArgs(argsTypes)) return constructor;
    }
    if (innerClass != null) return innerClass.getThisConstructor(argsTypes);
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
