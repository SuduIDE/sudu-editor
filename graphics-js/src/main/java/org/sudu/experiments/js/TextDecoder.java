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

  public static TextDecoder gbk() {
    return create("gbk");
  }

  public static JSString decodeUTF16(char[] data) {
    return Singleton.decoderUTF16.decode(data);
  }

  public static JSString fromUtf8(byte[] data) {
    return create().decode(JsMemoryAccess.uInt8View(data));
  }

  public static JSString fromGbk(byte[] data) {
    return gbk().decode(JsMemoryAccess.uInt8View(data));
  }

  public interface Singleton {
    TextDecoder decoderUTF16 = TextDecoder.createUTF16();
  }
}
