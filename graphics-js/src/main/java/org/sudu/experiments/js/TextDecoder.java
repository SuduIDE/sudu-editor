package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint16Array;
import org.teavm.jso.typedarrays.Uint8Array;

public abstract class TextDecoder implements JSObject {
  @JSBody(script = "return new TextDecoder();")
  public static native TextDecoder create();

  @JSBody(params = "label", script = "return new TextDecoder(label);")
  public static native TextDecoder create(String label);

  public abstract JSString decode(Uint16Array array);

  public abstract JSString decode(Uint8Array array);

  public final JSString decode(char[] data) {
    return decode(JsMemoryAccess.bufferView(data));
  }

  public static TextDecoder createUTF16() {
    return create("utf-16");
  }

  public static JSString fromCharArray(char[] data) {
    return createUTF16().decode(data);
  }

  public static JSString fromUtf8(byte[] data) {
    return create().decode(JsMemoryAccess.uInt8View(data));
  }
}
