package org.sudu.experiments.merge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.diff.CodeElementS;
import org.sudu.experiments.diff.CodeLineS;

import java.util.Arrays;

public class MergeModelTest {

  public static final boolean PRINT = true;

  @Test
  public void testMergeModel() {
    CodeLineS[] leftText = new CodeLineS[] {
        l("package", " ", "main", ";"),
        l("int", " ", "a", " ", "=", " ", "10", ";"),
        l(""),
        l("int", " ", "a", " ", "=", " ", "10", ";"),
        l(""),
        l("// comment"),
        l(";"),
    };
    CodeLineS[] midText = new CodeLineS[] {
        l("package", " ", "main", ";"),
        l("int", " ", "a", " ", "=", " ", "10", ";"),
        l(""),
        l("int", " ", "m", " ", "=", " ", "10", ";"),
        l(""),
        l("// comment"),
        l("int", " ", "b", " ", "=", " ", "10", ";"),
        l(";"),
    };
    CodeLineS[] rightText = new CodeLineS[] {
        l("package", " ", "main", ";"),
        l(""),
        l("int", " ", "r", " ", "=", " ", "10", ";"),
        l(""),
        l("// comment"),
        l("int", " ", "b", " ", "=", " ", "10", ";"),
        l(";"),
    };
    /*
    common: 1
    [int a = 10;] -> [int a = 10;] <- []
    common: 1
    [int a = 10;] -> [int m = 10;] <- [int r = 10;]
    common: 2
    [] -> [int b = 10;] <- [int b = 10;]
    common: 1
     */
    var ranges = MergeModel.countRanges(leftText, midText, rightText);
    Assertions.assertEquals(7, ranges.size());
    if (PRINT) ranges.forEach(System.out::println);
  }

  CodeLineS l(String... elements) {
    return new CodeLineS(Arrays.stream(elements).map(this::e).toArray(CodeElementS[]::new));
  }

  CodeElementS e(String s) {
    return new CodeElementS(s);
  }
}
