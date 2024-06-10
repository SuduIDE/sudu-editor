package org.sudu.experiments.js.node;

import org.sudu.experiments.js.*;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public class NodeWorkersBridge implements WorkerProtocol.PlatformBridge {

  @Override
  public int toJs(Object javaObject, JsArray<JSObject> message, int idx) {
    if (javaObject instanceof NodeFileHandle nodeFile) {
      message.set(idx++, JSNumber.valueOf(0));
      message.set(idx++, nodeFile.jsPath());
    } else if (javaObject instanceof NodeDirectoryHandle nodeDir) {
      message.set(idx++, JSNumber.valueOf(1));
      message.set(idx++, nodeDir.jsPath());
    } else throw new IllegalArgumentException(
        "Illegal argument sent to worker " + javaObject.getClass().getName()
    );
    return idx;
  }

  @Override
  public int toJava(
      JSObject jsObject, JsArrayReader
      <JSObject> array, int arrayIndex,
      Object[] r, int idx
  ) {
    if (isNumber(jsObject)) {
      int value = jsObject.<JSNumber>cast().intValue();
      switch (value) {
        case 0 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          r[idx] = new NodeFileHandle(jsPath);
        }
        case 1 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          r[idx] = new NodeDirectoryHandle(jsPath);
        }
      }
    }
    return arrayIndex;
  }

  @JSBody(params = "data", script = "return typeof data === 'number';")
  static native boolean isNumber(JSObject data);
}
