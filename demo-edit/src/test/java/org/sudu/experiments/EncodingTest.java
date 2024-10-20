package org.sudu.experiments;

import org.sudu.experiments.editor.FileEncoding;

public class EncodingTest {

  public static void main(String[] args) {
    testFile("gb2312.txt");
//    testFile("UTF-8-demo.html");
//    testFile("ClassL.java");
  }

  private static void testFile(String filename) {
    System.out.println("filename = " + filename);
    byte[] s = ReadResource.readFileBytes(filename, EncodingTest.class);
    System.out.println("  data = " + s);
    boolean utf8 = FileEncoding.isUtf8(s, true);
    System.out.println("  utf8 = " + utf8);
    boolean gb2312 = FileEncoding.isGB2312_EUC_CN(s);
    System.out.println("  gb2312 = " + gb2312);
  }
}
