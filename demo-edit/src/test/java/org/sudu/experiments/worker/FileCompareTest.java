package org.sudu.experiments.worker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.worker.FileCompare;

public class FileCompareTest {
  static byte[] a(int ... b) {
    byte[] result = new byte[b.length];
    for (int i = 0; i < b.length; i++) result[i] = (byte) b[i];
    return result;
  }

  @Test
  public void testCmpArrays() {
    Assertions.assertEquals(-1, FileCompare.cmpArrays(
        a(1, 2, 3), a(1, 2, 3)));
  }
}
