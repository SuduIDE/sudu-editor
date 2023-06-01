package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

public abstract class TextEncoder implements JSObject {
  @JSBody(script = "return new TextEncoder();")
  public static native TextEncoder create();

  public abstract Uint8Array encode(JSString string);

  public static byte[] toUtf8(JSString string) {
    return JsMemoryAccess.toByteArray(create().encode(string));
  }

  public static char[] toCharArray(JSString t) {
    int l = t.getLength();
    char[] buffer = new char[l];
    for (int i = 0; i < l; ++i) {
      char codeAt = (char) t.charCodeAt(i);
      buffer[i] = codeAt;
    }
    return buffer;
  }
}
