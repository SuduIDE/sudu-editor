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
    Assertions.assertEquals(1, FileCompare.cmpArrays(
        a(1, 2, 3), a(1, 3, 3)));
    Assertions.assertEquals(3, FileCompare.cmpArrays(
        a(1, 2, 3), a(1, 2, 3, 4 )));
    Assertions.assertEquals(-1, FileCompare.cmpArrays(
        a(1, 2, 3, 5), a(1, 2, 3 ), 3));
    Assertions.assertEquals( 1, FileCompare.cmpArrays(
        a(1, 3, 3, 5), a(1, 2, 3 ), 3));
  }

  static final int maxArraySize = 16 * 1024;
  static final int minArraySize = 64;
  static final int maxToRead =  1024 * 1024;

  public static void main(String[] args) {
    int filePos = 0;
    int readLength = minArraySize;

    while (filePos < maxToRead) {
      System.out.println("filePos = " + filePos + ", readLength = " + readLength);
      filePos += readLength;
      if (readLength * 4 <= maxArraySize) {
        readLength *= 4;
      }
      if (filePos + readLength > maxToRead)
        readLength = maxToRead - filePos;

      System.out.println("next readLength = " + readLength);
    }
    if (filePos > maxToRead) {
      System.out.println("maxToRead-filePos = " + (maxToRead - filePos));
    }
    System.out.println("filePos = " + filePos);
    System.out.println("maxToRead = " + maxToRead);


  }
}
