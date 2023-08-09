package org.sudu.experiments.parser.java;

public class TestFieldResolve {

  class Z {
    int field;
  }

  class A  extends Z {


    int foo() {

    }

    void bar(int a) {

    }
  }

  B b = new B();

  class B extends A {

    public void main(String[] args) {
      this.field = 10;
      field = 10;
      field;
      this.foo();
      foo();
      foo;
      b;
      b.foo();
      b.field;
      bar(field);
      unresolved;
      this.unresolved;
      unresolved();
    }

  }

}
