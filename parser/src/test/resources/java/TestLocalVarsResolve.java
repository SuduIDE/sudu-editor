package java;

import java.io.Closeable;
import java.util.Scanner;
import java.util.function.Function;

public class TestLocalVarsResolve {

  int field, a;

  void foo(int field) {
    bar(a);
    int a = field;
    bar(field);
    bar(a);
    for (int i = 0; i < a; i++) bar(i);
    for (int b: new int[]{1, 2, 3}) bar(b);

    try (Closeable sc = new Scanner(System.in)) {
      if (sc instanceof Scanner scanner) {
        scanner.nextLine();
      }
      sc.getClass();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Function<Integer, Integer> inc = (c) -> c + 1;

    if ((Number) a instanceof Integer d) {
      d.intValue();
    }

    while ((Number) a instanceof Integer f) {
      f.intValue();
    }

    for (; (Number) a instanceof Integer g; ) {
      g.intValue();
    }
  }

  int bar(int a) {
    return a;
  }

}
