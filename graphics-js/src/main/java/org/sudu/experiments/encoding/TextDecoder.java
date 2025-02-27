package org.sudu.experiments.encoding;

import org.teavm.jso.core.JSString;

import static org.sudu.experiments.js.TextDecoder.fromGbk;
import static org.sudu.experiments.js.TextDecoder.fromUtf8;
import static org.sudu.experiments.js.TextEncoder.toCharArray;

public interface TextDecoder {
  static String decodeUtf8(byte[] bytes) {
    JSString s = fromUtf8(bytes);
    return s.stringValue();
  }

  static String decodeGbk(byte[] bytes) {
    JSString s = fromGbk(bytes);
    return s.stringValue();
  }

  static char[] utf8ToChar(byte[] bytes) {
    return toCharArray(fromUtf8(bytes));
  }

  static char[] gbkToChar(byte[] bytes) {
    return toCharArray(fromGbk(bytes));
  }
}
