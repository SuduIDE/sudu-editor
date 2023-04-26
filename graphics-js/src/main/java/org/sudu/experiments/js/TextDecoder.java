package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

public abstract class TextDecoder implements JSObject {
  @JSBody(script = "return new TextDecoder();")
  public static native TextDecoder create();

  public abstract JSString decode(Uint8Array array);

  public static JSString fromUtf8(byte[] data) {
    return create().decode(JsMemoryAccess.uInt8View(data));
  }
}
