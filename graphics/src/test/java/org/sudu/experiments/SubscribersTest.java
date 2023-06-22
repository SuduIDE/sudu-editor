package org.sudu.experiments;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubscribersTest {

  @Test
  void disposableAdd() {
    Subscribers<String> list = new Subscribers<>(new String[0]);
    Disposable d1 = list.disposableAdd("1");
    Disposable d2 = list.disposableAdd("2");
    Disposable d3 = list.disposableAdd("3");
    Assertions.assertTrue(has(list.array(), "1"));
    Assertions.assertTrue(has(list.array(), "2"));
    Assertions.assertTrue(has(list.array(), "3"));
    d1.dispose();
    Assertions.assertFalse(has(list.array(), "1"));
    d3.dispose();
    Assertions.assertFalse(has(list.array(), "3"));
  }

  @Test void test100() {
    Subscribers<Integer> list = new Subscribers<>(new Integer[0]);

    int N = 100;
    for (int i = 0; i < N; i++) {
      list.add((i * 37 % N));
    }

    for (int i = 0; i < N; i++) {
      list.remove((i * 41 % N));
    }

    for (Integer integer : list.array()) {
      Assertions.assertNull(integer);
    }
  }

  static boolean has(String[] list, String value) {
    for (String s : list) {
      if (value.equals(s)) return true;
    }
    return false;
  }
}
