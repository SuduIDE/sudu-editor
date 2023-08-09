package org.sudu.experiments.parser.java;

public class TestQualifiedResolve {

  public static void main(String[] args) {
    A a = new A();
    var res0 = y.b;
    var res1 = a.b;
    var res2 = res1.c;
    var res3 = res1.c();
    var res4 = res3.d;
    var res5 = res0.arg;
    y(res3.d);
  }

  class A {
    B b;
  }

  class B {
    int c;
    D c();
  }

  class D {
    int d;
  }

  int y(int a) {
    return a;
  }

}
