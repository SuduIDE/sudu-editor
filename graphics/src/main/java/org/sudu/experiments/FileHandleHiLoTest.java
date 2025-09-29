package org.sudu.experiments;

public class FileHandleHiLoTest {
  public static void main(String[] args) {

    int hi = 54321;
    int lo = 12345678;

    testAddress(lo, hi);

    int[] address2int = FileHandle.address2int(-1);
    testAddress(address2int[0], address2int[1]);
  }

  private static void testAddress(int lo, int hi) {
    double dAddr = FileHandle.int2Address(lo, hi);

    int hi2 = FileHandle.hiGb(dAddr);
    int lo2 = FileHandle.loGb(dAddr);

    if ((hi != hi2)) throw new  RuntimeException("(hi != hi2)");
    if ((lo != lo2)) throw new  RuntimeException("(lo != lo2)");

    double dAddr2 = FileHandle.int2Address(lo2, hi2);
    if ((dAddr != dAddr2)) throw new  RuntimeException("(dAddr != dAddr2)");
  }
}
