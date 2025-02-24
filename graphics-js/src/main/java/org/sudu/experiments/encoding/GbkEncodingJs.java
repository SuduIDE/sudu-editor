package org.sudu.experiments.encoding;

import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int8Array;

import static org.sudu.experiments.encoding.GbkEncoding.*;

public interface GbkEncodingJs {

  static Int8Array jsStringToGbk(JSString jsText) {
    return JsMemoryAccess.bufferView(
        GbkEncodingJs.encode(jsText));
  }

  static byte[] encode(JSString s) {
    var charToGbk = GbkEncoding.Table.charToGbk;
    int p = 0;
    byte[] data = new byte[byteLength(s)];
    for (int i = 0, l = s.getLength(); i < l; ++i)
      p = putChar(p, data, (char) s.charCodeAt(i), charToGbk);
    return data;
  }

  static int byteLength(JSString s) {
    var charToGbk = GbkEncoding.Table.charToGbk;
    int n = 0;
    for (int i = 0, l = s.getLength(); i < l; ++i) {
      char c = (char) s.charCodeAt(i);
      n += bytesForChar(c, charToGbk);
    }
    return n;
  }
}
