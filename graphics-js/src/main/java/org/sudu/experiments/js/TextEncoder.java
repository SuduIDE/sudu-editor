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
}
