package org.sudu.experiments.win32;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.Arrays;

class JHandleTest {

  @Test
  public void test() {
    int iterations = 10;
    int numObjects = 100;

    JHandle<String> h = new JHandle<>(new String[0]);

    String[] data = strings(numObjects);
    Integer[] idx = new Integer[data.length];

    allocateAll(h, data, idx);

    for (int i = 0; i < data.length; i++) {
      Assertions.assertEquals(h.get(i), data[i]);
    }

    XorShiftRandom rng = new XorShiftRandom();

    for (int k = 0; k < iterations; k++) {
      rng.shuffle(idx);

      String[] removed = new String[idx.length * (k + 1) / (iterations + 1)];

      for (int i = 0; i < removed.length; i++) {
        Integer index = idx[i];
        removed[i] = h.get(index);
        h.free(index);
        Assertions.assertNull(h.get(index));
        idx[i] = null;
      }

      rng.shuffle(removed);

      allocateAll(h, removed, idx);

      String[] content = new String[idx.length];
      for (int i = 0; i < idx.length; i++) {
        content[i] = h.get(idx[i]);
      }

      Arrays.sort(content);

      boolean equals = Arrays.equals(data, content);
      Assertions.assertTrue(equals);
    }
  }

  static void allocateAll(JHandle<String> h, String[] input, Integer[] out) {
    for (int i = 0; i < input.length; i++) {
      out[i] = h.alloc(input[i]);
    }
  }

  static String[] strings(int n) {
    String[] r = new String[n];
    char[] ch = new char[2];
    for (int i = 0; i < n; i++) {
      ch[0] = d(i / 10);
      ch[1] = d(i % 10);
      r[i] = new String(ch);
    }
    return r;
  }

  static char d(int i) {
    return (char) ('0' + i);
  }
}