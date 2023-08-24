package org.sudu.experiments.diff;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class DiffModelTest {

  @Test
  public void test1() {
    String text1 = """
foo();
int a = 10;
int c = 4;
// comment
boolean b = true;
double c = .5;
double d = .15;
String s = "s" ;
char n = '\\n';
        """;
    String text2 = """
int a = 10;
int b = 3;
int c = 4;
/*
block comment
*/
boolean b = true;
double c = .8;
String s ;
bar();
        """;
    DiffModel model = new DiffModel();
    model.findLinesDiff(d(ln(text1)), d(ln(text2)));
    model.printResults();
  }

  CodeLineS[] d(CodeLineS... line) {
    return line;
  }

  CodeLineS[] ln(String text) {
    return Arrays.stream(text.split("\n")).map(this::ls).toArray(CodeLineS[]::new);
  }

  CodeLineS ls(String line) {
    String[] tmp = line.split("( )+", -1);
    String[] result = new String[2 * tmp.length - 1];
    result[0] = tmp[0];
    for (int i = 1; i < tmp.length; i++) {
      result[2 * i - 1] = " ";
      result[2 * i] = tmp[i];
    }
    return l(result);
  }

  CodeLineS l(String... elements) {
    return l(Arrays.stream(elements).map(CodeElementS::new).toArray(CodeElementS[]::new));
  }

  CodeLineS l(CodeElementS... elements) {
    return new CodeLineS(elements);
  }

  CodeElementS e(String s) {
    return new CodeElementS(s);
  }

}
