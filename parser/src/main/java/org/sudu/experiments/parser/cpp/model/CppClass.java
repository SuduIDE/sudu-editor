package org.sudu.experiments.parser.cpp.model;

import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CppClass extends Decl {

  public List<Decl> fields;
  public List<CppMethod> methods;
  public List<CppClass> nestedClasses;
  public CppClass innerClass;

  public int nestPos = 0;

  public CppClass(String name, Pos position, CppClass innerClass) {
    super(name, position);
    this.innerClass = innerClass;
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.nestedClasses = new ArrayList<>();
  }

  public Decl getField(String fieldName) {
    for (var field: fields)
      if (field.name.equals(fieldName)) return field;
    if (innerClass != null) return innerClass.getField(fieldName);
    return null;
  }

  public CppMethod getMethod(String methodName) {
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
    CppClass cppStruct = (CppClass) o;
    return nestPos == cppStruct.nestPos && Objects.equals(fields, cppStruct.fields) && Objects.equals(methods, cppStruct.methods) && Objects.equals(nestedClasses, cppStruct.nestedClasses) && Objects.equals(innerClass, cppStruct.innerClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), fields, methods, nestedClasses, innerClass, nestPos);
  }
}
