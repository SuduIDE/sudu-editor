package org.sudu.experiments.parser.java;

public class TestFieldResolve {

  int field1, field2;
  String field3;
  Number field4;

  void foo(int field1) {
    this.field1 = field1;
    field1 = this.field1;
    f(field1);
    f(this.field1);
    f(this.field1, this.field2);
    f(field2, this.field1);
    f(field2 + this.field1, 2 * this.field1);
    boolean b = true
        ? this.field1
        : field2;
    field3.toCharArray();
    this.field3.toCharArray();
  }

  void f(int a) {}
  void f(int a, int b) {}
}
