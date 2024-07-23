package org.sudu.experiments.protocol;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

public interface JsCast {

  static int[] ints(JsArray<JSObject> jsArray, int ind) {
    return JsMemoryAccess.toJavaArray((Int32Array) jsArray.get(ind));
  }

  static Int32Array jsInts(int... array) {
    return JsMemoryAccess.bufferView(array);
  }

  static String string(JsArray<JSObject> jsArray, int ind) {
    return ((JSString) jsArray.get(ind)).stringValue();
  }

  static JSString jsString(String str) {
    return JSString.valueOf(str);
  }
}
