package org.sudu.experiments.parser.java;

public class TestMethodResolve {

  void a() {
    b(1);
    b(1 + 1);
    b(c() + 3 * (c() - 1));
    foo();
    foo(1);
    foo(1, 2);
  }

  int b(int a) {
    return a;
  }

  int c() {
    return 1;
  }

  int rec(int n) {
    if (n < 2) return 1;
    return rec(n - 1) + rec(n - 2);
  }

  void foo(){}

  void foo(int a){}

  void foo(int a, int b){}

}