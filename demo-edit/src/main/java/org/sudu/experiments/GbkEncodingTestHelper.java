package org.sudu.experiments;

import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.encoding.TextDecoder;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public interface GbkEncodingTestHelper {

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

  static void testGlyph(byte... glyph) {
    System.out.println("testGlyph: " + Integer.toHexString(glyph[0] & 0xFF) +
        " " + Integer.toHexString(glyph[1] & 0xFF) );
    String string = TextDecoder.decodeGbk(glyph);
    System.out.println("  string.charAt(0) = " + Integer.toHexString(string.charAt(0)));
    byte[] bA1A1en = GbkEncoding.encode(string);
    System.out.println("  Encoded: " + Integer.toHexString(bA1A1en[0] & 0xFF) +
        " " + Integer.toHexString(bA1A1en[1] & 0xFF) );
  }
}
