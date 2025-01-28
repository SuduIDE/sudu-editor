package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

// node Buffer class
// https://nodejs.org/api/buffer.html
public abstract class JsBuffer extends Uint8Array {

  @NoSideEffects
  @JSBody(params = {"ab"}, script = "return Buffer.from(ab);")
  public static native JsBuffer from(ArrayBuffer ab);

  public static JsBuffer from(byte[] data) {
    return JsBuffer.from(JsMemoryAccess.bufferView(data).getBuffer());
  }
}
