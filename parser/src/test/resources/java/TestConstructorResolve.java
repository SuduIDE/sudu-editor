package java;

public class TestConstructorResolve {

  int a, b;
  boolean c, d;

  public TestConstructorResolve(int a, int b, boolean c, boolean d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  public TestConstructorResolve(int a, int b) {
    this(a, b, false, false);
  }

  public TestConstructorResolve(boolean c, boolean d) {
    this(1, 1, c, d);
  }

  public TestConstructorResolve(int a, boolean c) {
    this(a, a, c, c);
  }

  public void foo() {
    new TestConstructorResolve(1, 1, false, false);
    TestConstructorResolve a = new TestConstructorResolve(1, false);
    var b = new TestConstructorResolve(1, false);
  }

}
