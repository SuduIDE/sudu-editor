package org.sudu.experiments;

import org.sudu.experiments.encoding.FileEncoding;

import java.util.Arrays;

public class EncodingTest {

  public static void main(String[] args) {

    for (int i = 0; i < 256; i++) {
      byte b2 = (byte) i;
      boolean v = b2 < 64 && b2 > -2 || b2 == 127;

      boolean bad2 = b2 == 127 || b2 == -1 || b2 >= 0 && b2 < 0x40;
      boolean bad3 = b2 < 64 && b2 > -2 || b2 == 127;
      if (v != bad2 || v != bad3) {
        System.out.println("i = " + i);
      }

    }

    if (false) {
      testFile("gb2312.txt");
      testFile("gbk.txt");
      testFile("UTF-8-demo.html");
      testUtfOffset("UTF-8-demo.html");
    }
//    testFile("ClassL.java");
  }

  private static void testFile(String filename) {
    System.out.println("testFile: filename = " + filename);
    byte[] s = ReadResource.readFileBytes(filename, EncodingTest.class);
    boolean utf8 = FileEncoding.isUtf8(s, true);
    boolean gb2312 = FileEncoding.isGB2312(s);
    boolean gbk = FileEncoding.isGBK(s);
    System.out.println("  utf8 = " + utf8 + ", gb2312 = " + gb2312 + ", gbk = " + gbk);
  }

  private static void testUtfOffset(String filename) {
    System.out.println("testUtfOffset: filename = " + filename);
    byte[] s = ReadResource.readFileBytes(filename, EncodingTest.class);
    boolean utf8 = FileEncoding.isUtf8(s, true);
    if (!utf8) {
      System.out.println("  test filed: file is not utf-8");
    }

    for (int i = 0; i < s.length; i++) {
      byte[] substring = Arrays.copyOfRange(s, i, s.length);
      boolean subUtf8 = FileEncoding.isUtf8(substring, false);
      if (!subUtf8) {
        System.out.println("  test failed i = " + i);
      }
    }
  }
}
