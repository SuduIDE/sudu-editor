package org.sudu.experiments;

@SuppressWarnings("ConstantValue")
public class FileHandleHiLoTest {
  public static void main(String[] args) {

    int hi = 54321;
    int lo = 12345678;

    double dAddr = hi * (double)FileHandle._1gb + lo;

    int hi2 = FileHandle.hiGb(dAddr);
    int lo2 = FileHandle.loGb(dAddr);

    if ((hi != hi2)) throw new  RuntimeException("(hi != hi2)");
    if ((lo != lo2)) throw new  RuntimeException("(lo != lo2)");

    double dAddr2 = FileHandle.int2Address(lo2, hi2);
    if ((dAddr != dAddr2)) throw new  RuntimeException("(dAddr != dAddr2)");

    double a = Math.pow(2, 53);
    System.out.println("Double.toHexString(a)   = " + Double.toHexString(a));
    System.out.println("Double.toHexString(a-1) = " + Double.toHexString(a - 1));
    double b = a + 1;
    double c = a + 2;
    System.out.println("Double.toHexString(a+1) = " + Double.toHexString(b));
    System.out.println("Double.toHexString(a+2) = " + Double.toHexString(c));

    System.out.println("(a == a + 1) = " + (a==b));
    System.out.println("(a == a + 2) = " + (a==c));
  }
}
