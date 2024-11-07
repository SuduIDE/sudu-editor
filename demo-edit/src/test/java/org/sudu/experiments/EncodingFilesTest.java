package org.sudu.experiments;

import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class EncodingFilesTest {

  public static void main(String[] args) throws IOException {
    ideaBug();

    if (args.length == 0) {
      testDefault();
    } else {
      for (String arg : args) {
        System.out.println("file: " + arg);
        byte[] bytes = Files.readAllBytes(Path.of(arg));
        testBytes(bytes);
      }
    }
  }

  private static void testDefault() {
    testFile("gb2312.txt");
    testFile("gbk.txt");
    testFile("UTF-8-demo.html");
    testUtfOffset("UTF-8-demo.html");
    testGbkGlyph("gbk.a1a1.txt");
  }

  private static void ideaBug() {
    for (int i = 0; i < 256; i++) {
      byte b2 = (byte) i;
      boolean v = b2 < 64 && b2 > -2 || b2 == 127;

      boolean bad2 = b2 == 127 || b2 == -1 || b2 >= 0 && b2 < 0x40;
      boolean bad3 = b2 < 64 && b2 > -2 || b2 == 127;
      if (v != bad2 || v != bad3) {
        System.out.println("i = " + i);
      }

    }
  }

  private static void testFile(String filename) {
    System.out.println("testFile: filename = " + filename);
    byte[] s = ReadResource.readFileBytes(filename, EncodingFilesTest.class);
    testBytes(s);
  }

  static boolean isEol(char c) {
    return c == '\r' || c == '\n';
  }

  static void testGbkGlyph(String filename) {
    System.out.println("testGbkGlyph: filename = " + filename);
    testGbkGlyph(ReadResource.readFileBytes(filename, EncodingFilesTest.class));
  }

  static void testGbkGlyph(byte[] s) {
    if (testBytes(s)) {
      StringBuilder sb = new StringBuilder("bytes: ");
      for (byte b : s)
        sb.append(toHexString(b)).append(' ');
      System.out.println(sb);
      String gbkStr = new String(s, gbk);
      if (gbkStr.length() > 0 && isEol(gbkStr.charAt(gbkStr.length() - 1)))
        gbkStr = gbkStr.substring(0, gbkStr.length() - 1);
      System.out.println("gbkStr = " + gbkStr);
      System.out.println("gbkStr.length() = " + gbkStr.length());
      System.out.println("gbkStr.toCharArray() = " +
          toHexString(gbkStr.toCharArray()));
      char gbkCode = GbkEncoding.charToGbk[gbkStr.charAt(0)];
      System.out.println("charToGbk[" + Integer.toHexString(gbkStr.charAt(0)) +
          "] = " + Integer.toHexString(gbkCode));
    }
  }

  static final Charset gbk = Charset.forName("GBK");

  static String toHexString(char[] charArray) {
    char[] r = new char[charArray.length * 7 - 1];
    for (int i = 0, p = 0; i < charArray.length; i++) {
      char c = charArray[i];
      r[p++] = '0'; r[p++] = 'x';
      for (int j = 0; j < 4; j++) {
        r[p++] = hexDigit((c >> (12 - j * 4)) & 0xF);
      }
      if (i + 1 < charArray.length)
        r[p++] = ' ';
    }
    return new String(r);
  }

  static char hexDigit(int x) {
    return (char) (x < 10 ? x + '0' : x + 'A' - 10);
  }

  private static boolean testBytes(byte[] s) {
    boolean utf8 = FileEncoding.isUtf8(s, true);
    boolean gb2312 = FileEncoding.isGB2312(s);
    boolean gbk = FileEncoding.isGBK(s);
    System.out.println("  utf8 = " + utf8 + ", gb2312 = " + gb2312 + ", gbk = " + gbk);
    return gbk;
  }

  private static void testUtfOffset(String filename) {
    System.out.println("testUtfOffset: filename = " + filename);
    byte[] s = ReadResource.readFileBytes(filename, EncodingFilesTest.class);
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

  static String toHexString(byte aByte) {
    char[] value = {hexDigit((aByte >> 4) & 0xF), hexDigit(aByte & 0xF)};
    return new String(value);
  }
}
