
class A {
  field = 3;

  static staticFunction(arg) {
    console.log("staticFunction", arg);
  }

  constructor(... args) {
    console.log("A.ctor", ... args);
    console.log("new.target=", new.target);
    console.log("new.target === Class", new.target === A);
    this.methodFromCTor();
  }

  methodFromCTor() {
    console.log("A.methodFromCTor");
  }

  method() {
    console.log("A.method", "field =", this.field);
  }
}

class B extends A {
  constructor(... args) {
    super();
    console.log("B.ctor", ... args);
  }


  method() {
    console.log("B.method", "field =", this.field);
    super.method();
  }

  methodFromCTor() {
    console.log("B.methodFromCTor");
  }
}

let a = new A(1,2,3)

a.method();

A.staticFunction(5);

console.log("-----");

let b = new B(333);

b.method();
