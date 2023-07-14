package org.sudu.experiments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubscribersTest {

  @Test
  void disposableAdd() {
    Subscribers<String> list = new Subscribers<>(new String[0]);
    Disposable d1 = list.disposableAdd("1");
    Disposable d2 = list.disposableAdd("2");
    Disposable d3 = list.disposableAdd("3");
    assertTrue(has(list.array(), "1"));
    assertTrue(has(list.array(), "2"));
    assertTrue(has(list.array(), "3"));
    d1.dispose();
    assertFalse(has(list.array(), "1"));
    d3.dispose();
    assertFalse(has(list.array(), "3"));
  }

  @Test void test4() {
    Subscribers<Integer> list = new Subscribers<>(new Integer[0]);
    list.add(1);
    list.add(2);
    list.add(3);
    list.disposableAdd(4).dispose();
    Integer[] array = list.array();
    assertEquals(3, array.length);
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
      assertNull(integer);
    }
  }

  @Test void testOrder() {
    int[] box = new int[1];
    int[] got = new int[4];

    Subscribers<Runnable> list = new Subscribers<>(new Runnable[0]);

    list.disposableAdd(() -> got[0] = ++box[0]);
    Disposable d2 = list.disposableAdd(
            /*      */ () -> got[1] = ++box[0]);
    list.disposableAdd(() -> got[2] = ++box[0]);

    fire(list);
    test(got, 1,2,3,0);
    d2.dispose();
    list.disposableAdd(() -> got[3] = ++box[0]);
    fire(list);
    test(got, 4,2,5,6);

  }

  private void test(int[]got, int ... values) {
    for (int i = 0; i < values.length; i++) {
      assertEquals(values[i], got[i]);
    }
  }

  private static void fire(Subscribers<Runnable> list) {
    for (Runnable runnable : list.array()) {
      runnable.run();
    }
  }


  static boolean has(String[] list, String value) {
    for (String s : list) {
      if (value.equals(s)) return true;
    }
    return false;
  }
}
