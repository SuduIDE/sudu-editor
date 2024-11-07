package org.sudu.experiments;

import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.IntStream;

import static org.sudu.experiments.EncodingFilesTest.hexDigit;

public class GbkEncodingTest {

  public static void main(String[] args) {
    testAllGbkPairs();
    testAllGbkAtOnce();
    testGbkEncode();
  }

  private static void testGbkEncode() {
    var b = GbkEncoding.allGbkCodes();
    String s = new String(b, gbk);
    byte[] encode1 = GbkEncoding.encode(s.toCharArray());
    byte[] encode2 = GbkEncoding.encode(s);
    if (!Arrays.equals(b, encode1))
      throw new RuntimeException("Encodes not equal");
    if (!Arrays.equals(b, encode2))
      throw new RuntimeException("Encodes not equal");
    String s2 = new String(encode1, gbk);
    System.out.println("s.equals(s2) = " + s.equals(s2));
  }

  static final Charset gbk = Charset.forName("GBK");

  static void testAllGbkPairs() {
    // [126] x [190]
    // 1st [81–FE] => 2nd [40–7E] + [80-FE]

    byte[] b2 = new byte[2];
    int m = 0;
    for (byte b1 = -127; b1 <= -2; b1++) {
      int n = 0;
      b2[0] = b1;
      // 2nd [40–7E] + [80-FE]
      byte bb2 = 0x40;
      for (; bb2 <= 0x7e; bb2++) {
        n++;
        b2[1] = bb2;
        testPair(b2);
      }
      for (bb2= -128; bb2 <= -2; bb2++) {
        n++;
        b2[1] = bb2;
        testPair(b2);
      }
      if (m++ == 0) System.out.println("n = " + n);
    }
    System.out.println("m = " + m);
  }

  static void testAllGbkCodes(byte[] all) {
    if (all.length != 126*190*2)
      throw new IllegalArgumentException("all.length != 126*190*2");
    boolean isGBK = FileEncoding.isGBK(all);
    if (!isGBK)
      throw new IllegalArgumentException("FileEncoding.isGBK failed");
    System.out.println("isGBK = " + isGBK);
    byte[] cover = new byte[0x10000];
    for (int i = 0; i < all.length; i += 2) {
      int lb = all[i] & 255, hb = all[i + 1] & 255;
      int index = lb + hb * 256;
      if (cover[index] != 0)
        throw new IllegalArgumentException("cover["+index+"] != 0");
      cover[index] = 1;
    }

    int n = 0;
    for (byte b : cover) n += b;
    if (n != 126*190)
      throw new IllegalArgumentException("cover != 126*190");
  }

  static void testAllGbkAtOnce() {
    var b = GbkEncoding.allGbkCodes();
    testAllGbkCodes(b);
    String s = new String(b, gbk);
    System.out.println("s.l = " + s.length());
    System.out.println("126*190 = " + 126 * 190);
    System.out.println("s.toCharArray().length = " + s.toCharArray().length);

    Map<Character, Character> map = new TreeMap<>();
    if (s.length() * 2 == b.length) {
      for (int i = 0; i + i < b.length; i++) {
        int b0 = b[i + i] & 0xFF, b1 = b[i + i + 1] & 0xFF;
        char bytes = (char) (b1 * 256 + b0);
        char ch = s.charAt(i);
        int cp = s.codePointAt(i);
        if (ch != cp) {
          System.out.println("ch != cp = " + cp);
        }
        if (Character.isHighSurrogate(ch)) {
          System.out.println("isHighSurrogate " + Integer.toHexString(ch));
        }
        Character key = ch;
        if (map.containsKey(key)) {
          System.out.println("map.containsKey key = " + Integer.toHexString(ch));
        } else {
          map.put(key, bytes);
        }
        if (ch < 128) {
          System.out.println("ch = " + ch + ", key = " + key);
        }
      }
      System.out.println("map.size() = " + map.size());
      var iterator = map.entrySet().iterator();
      if (iterator.hasNext()) {
        var next = iterator.next();
        System.out.println("first: key = "
            + Integer.toHexString(next.getKey()) + ", value = "
            + Integer.toHexString(next.getValue()));

        while (iterator.hasNext()) {
          next = iterator.next();
        }

        System.out.println("last: key = "
            + Integer.toHexString(next.getKey()) + ", value = "
            + Integer.toHexString(next.getValue()));
      }
    }
  }

  static void testPair(byte[] b2) {
    CharBuffer decode = gbk.decode(ByteBuffer.wrap(b2));
    if (decode.length() != 1) {
      System.out.println("decode.length = " + decode.length());
      char[] chars = new char[decode.length()];
      for (int i = 0; i < chars.length; i++)
        chars[i] = decode.charAt(i);
      IntStream codePoints = decode.codePoints();
      int[] array = codePoints.toArray();
      String x = "codepoints " + Arrays.toString(array) +
          ", chars = " + EncodingFilesTest.toHexString(chars) +
          ", bytes: " + toHexString(b2[0]) + ' ' + toHexString(b2[1]);
      System.out.println(x);
    }
  }

  static String toHexString(byte aByte) {
    char[] value = {hexDigit((aByte >> 4) & 0xF), hexDigit(aByte & 0xF)};
    return new String(value);
  }
}
