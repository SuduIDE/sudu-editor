package org.sudu.experiments.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SplitTextTest {

  @Test
  void split() {
    Assertions.assertArrayEquals(SplitText.split(""), a(""));
    Assertions.assertArrayEquals(SplitText.split("\n"), a("", ""));
    Assertions.assertArrayEquals(SplitText.split("\n\n"), a("", "", ""));
    Assertions.assertArrayEquals(SplitText.split("a\n\n"), a("a", "", ""));
    Assertions.assertArrayEquals(SplitText.split("\na\n"), a("", "a", ""));
    Assertions.assertArrayEquals(SplitText.split("\n\na"), a("", "", "a"));
    Assertions.assertArrayEquals(SplitText.split("a\nb\n"), a("a", "b", ""));
  }

  static String[] a(String ... r) { return r; }
}
