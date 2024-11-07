package org.sudu.experiments.encoding;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public interface GbkEncoding {
  char[] charToGbk = init();

  static char[] init() {
    char[] chars = new char[0x10000];
    byte[] gbkCodes = allGbkCodes();
    String s = TextDecoder.decodeGbk(gbkCodes);
    if (s.length() * 2 != gbkCodes.length)
      throw new RuntimeException("unexpected TextDecoder.decodeGbk length");
    for (int i = 0; i + i < gbkCodes.length; i++) {
      int lb = gbkCodes[i + i] & 255, hb = gbkCodes[i + i + 1] & 255;
      char cp = s.charAt(i), src = (char) (lb + hb * 256);
      chars[cp] = src;
    }
    return chars;
  }

  static byte[] encode(char[] s) {
    int p = 0;
    byte[] data = new byte[byteLength(s)];
    for (char c : s)
      p = putChar(p, data, c);
    return data;
  }

  static byte[] encode(String s) {
    int p = 0;
    byte[] data = new byte[byteLength(s)];
    for (int i = 0, l = s.length(); i < l; ++i)
      p = putChar(p, data, s.charAt(i));
    return data;
  }

  static int putChar(int p, byte[] data, char c) {
    if (c > 127) {
      char gbkCode = charToGbk[c];
      if (gbkCode != 0) {
        if (p + 1 < data.length) {
          data[p++] = (byte) gbkCode;
          data[p++] = (byte) (gbkCode >> 8);
        }
      } else {
        if (p < data.length) data[p++] = '?';
      }
    } else {
      if (p < data.length) data[p++] = (byte) c;
    }
    return p;
  }

  static int byteLength(char[] s) {
    int n = 0;
    for (char c : s) {
      if (c > 127 && charToGbk[c] != 0) {
        n += 2;
      } else {
        n++;
      }
    }
    return n;
  }

  static int byteLength(String s) {
    int n = 0;
    for (int i = 0, l = s.length(); i < l; ++i) {
      char c = s.charAt(i);
      if (c > 127 && charToGbk[c] != 0) {
        n += 2;
      } else {
        n++;
      }
    }
    return n;
  }

  static byte[] allGbkCodes() {
    // [126] x [190]
    byte[] b = new byte[126 * 190 * 2];
    // 1st [81–FE]
    for (byte b1 = -127; b1 <= -2; b1++) {
      int y = b1 + 127;
      for (int i = 0; i < 190; i++) {
        b[i * 2 + y * 190 * 2] = b1;
      }
    }

    // 2nd [40–7E] + [80-FE]
    byte b2 = 0x40;
    for (; b2 <= 0x7e; b2++) {
      int x = b2 - 0x40;
      for (int i = 0; i < 126; i++)
        b[i * 190 * 2 + x * 2 + 1] = b2;
    }
    for (b2= -128; b2 <= -2; b2++) {
      int x = b2 + 0x40 + 127;
      for (int i = 0; i < 126; i++)
        b[i * 190 * 2 + x * 2 + 1] = b2;
    }
    return b;
  }

  static void dump() {
    var b = GbkEncoding.allGbkCodes();
    String string = TextDecoder.decodeGbk(b);
    System.out.println("  string.length() = " + string.length() + ", allGBK = " + b.length / 2);
    System.out.println("  Arrays.equals(b, GbkEncoding.encode(string)) = " +
        Arrays.equals(b, GbkEncoding.encode(string)));

    Map<Character, Character> map = new TreeMap<>();
    for (int i = 0; i + i < b.length; i++) {
      int b0 = b[i + i] & 0xFF, b1 = b[i + i + 1] & 0xFF;
      char bytes = (char) (b1 * 256 + b0);
      char ch = string.charAt(i);
      int cp = string.codePointAt(i);
      if (ch != cp) {
        System.out.println("  ch != cp = " + cp);
      }
      if (Character.isHighSurrogate(ch)) {
        System.out.println("  isHighSurrogate " + Integer.toHexString(ch));
      }
      Character key = ch;
      if (map.containsKey(key)) {
        System.out.println("  map.containsKey key = " + Integer.toHexString(ch));
      } else {
        map.put(key, bytes);
      }
      if (ch < 128) {
        System.out.println("  ch = " + ch + ", key = " + key);
      }
    }
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
    System.out.println("  map.size() = " + map.size());
  }
}
