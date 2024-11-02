package org.sudu.experiments.encoding;

public interface FileEncoding {
  String gbk = "gbk";
  String utf8 = null;

  // https://en.wikipedia.org/wiki/GB_2312#EUC-CN
  // The value of the first byte is from 0xA1–0xF7 (161–247),
  // while the value of the second byte is from 0xA1–0xFE (161–254).
  static boolean isGB2312(byte[] bytes) {
    byte minValue = (byte) 0xA1; // -95
    byte max1Value = (byte) 0xF7; // -9
    byte max2Value = (byte) 0xFE; // -2
    for (int i = 0, n = bytes.length; i < n; i++) {
      byte b = bytes[i];
      if (b >= 0) continue;
      if (b >= minValue && b <= max1Value) {
        if (i == n - 1)
          return false;
        byte b2 = bytes[++i];
        var inRange = b2 >= minValue && b2 <= max2Value;
        if (!inRange)
          return false;
      } else {
        return false;
      }
    }
    return true;
  }

  // https://en.wikipedia.org/wiki/GBK_(character_encoding)
  // 1st [81–FE] => 2nd [40–7E] + [80-A0] + [A1-FE]
  static boolean isGBK(byte[] bytes) {
    for (int i = 0, n = bytes.length; i < n; i++) {
      byte b = bytes[i];
      if (b >= 0) continue;
      if (b == (byte) 0x80 || b == (byte) 0xFF)
        return false;
      if (i == n - 1)
        return false;
      byte b2 = bytes[++i];
      if (b2 > -2 && b2 < 0x40 || b2 == 0x7F)
        return false;
    }
    return true;
  }

  // First code point	Last code point	Byte 1	Byte 2	Byte 3	Byte 4
  // U+0000    U+007F     0yyyzzzz
  // U+0080    U+07FF     110xxxyy	10yyzzzz
  // U+0800    U+FFFF     1110wwww	10xxxxyy	10yyzzzz
  // U+010000	 U+10FFFF   11110uvv	10vvwwww	10xxxxyy	10yyzzzz

  // Errors:
  //   Bytes that never appear in UTF-8: 0xC0, 0xC1, 0xF5–0xFF
  //   A "continuation byte" (0x80–0xBF) at the start of a character
  //   A non-continuation byte (or the string ending) before the end of a character
  //   An overlong encoding (0xE0 followed by less than 0xA0, or 0xF0 followed by less than 0x90)
  //   A 4-byte sequence that decodes to a value greater that U+10FFFF (0xF4 followed by 0x90 or greater)

  static boolean isBc(byte b) {
    byte bch = (byte) 0b1011_1111;
    return b <= bch;
  }

  static boolean isUtf8(byte[] text, boolean begin) {
    int i = 0, n = text.length;

    final byte bcl = (byte) 0b1000_0000;
    final byte bch = (byte) 0b1011_1111;
    //  BOM the first three text will be 0xEF, 0xBB, 0xBF.
    if (begin) {
      if (n >= 3 && text[0] == -17 && text[1] == -69 && text[2] == -65)
        i = 3;
    } else {
      while (i < n && text[i] <= bch)
        ++i;
    }
    final byte b2l = (byte) 0b1100_0000;
    final byte b3l = (byte) 0b1110_0000;
    final byte b4l = (byte) 0b1111_0000;
    final byte b4h = (byte) 0b1111_0100;
    final int bcm = 0b0011_1111;
    final int b2m = 0b0001_1111;
    final int b3m = 0b0000_1111;
    final int b4m = 0b0000_0111;
    for (; i < n; i++) {
      byte b = text[i];
      if (b >= 0) continue;
      if (i >= n - 1) return false;
      byte b2 = text[++i];
      if (b2 > bch) return false;
      if (b2l <= b && b < b3l) {
        int ch = ((b & b2m) << 6) + (b2 & bcm);
        if (ch != 0 && ch < 0x80) return false;
      } else {
        if (i >= n - 2) return false;
        byte b3 = text[++i];
        if (b3 > bch) return false;
        if (b3l <= b && b < b4l) {
          int ch = ((b & b3m) << 12) + ((b2 & bcm) << 6) + (b3 & bcm);
          if (ch != 0 && ch < 0x0800) return false;
        } else if (b4l <= b && b <= b4h) {
          byte b4 = text[++i];
          if (b4 > bch) return false;
          int ch = ((b & b4m) << 18) + ((b2 & bcm) << 12)
              + ((b3 & bcm) << 6) + (b4 & bcm);
          if (ch != 0 && (ch < 0x10000 || ch > 0x10FFFF)) return false;
        } else return false;
      }
    }
    return true;
  }
}
