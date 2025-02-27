package org.sudu.experiments.encoding;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface TextDecoder {
  static String decodeUtf8(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  static String decodeGbk(byte[] bytes) {
    return new String(bytes, Charset.forName("GBK"));
  }

  static char[] utf8ToChar(byte[] bytes) {
    return decodeUtf8(bytes).toCharArray();
  }

  static char[] gbkToChar(byte[] bytes) {
    return decodeGbk(bytes).toCharArray();
  }
}
