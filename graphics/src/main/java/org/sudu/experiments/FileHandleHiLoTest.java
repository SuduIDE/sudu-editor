package org.sudu.experiments;

public class FileHandleHiLoTest {
  public static void main(String[] args) {

    int hi = 54321;
    int lo = 12345678;

    double dAddr = hi * FileHandle.c2_32 + lo;

    int hi2 = FileHandle.hiGb(dAddr);
    int lo2 = FileHandle.loGb(dAddr);

    System.out.println("hi2 = " + (hi == hi2));
    System.out.println("lo2 = " + (lo == lo2));

    System.out.println("pi in hex = " + Double.toHexString(Math.PI));

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
