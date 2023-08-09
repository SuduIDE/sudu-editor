package org.sudu.experiments.parser;

public class TestImportsResolve {

  public void main(String[] args) {
    A a = new A(12);
    int res = a.b + a.c;
    a.f1();
    a.f2();
  }

  class A extends B implements I1, I2 {

    public A(int a) {}

    public void main(int a) {
      int res = a + b + c + f1(a) + f2();
    }

    @Override
    public int f2() {
      return 2;
    }
  }

  class B extends C {
    int b;
  }

  class C {
    int c;
  }

  interface I1 {
    default int f1(int a) {
      return a;
    }
  }

  interface I2 {
    int f2();
  }
}
