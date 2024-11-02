package org.sudu.experiments.encoding;

import org.teavm.jso.core.JSString;

public interface TextDecoder {
  static String decodeUtf8(byte[] bytes) {
    JSString s = org.sudu.experiments.js.TextDecoder.fromUtf8(bytes);
    return s.stringValue();
  }

  static String decodeGbk(byte[] bytes) {
    JSString s = org.sudu.experiments.js.TextDecoder.fromGbk(bytes);
    return s.stringValue();
  }
}
