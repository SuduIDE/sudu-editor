package org.sudu.experiments.protocol;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.TextDecoder;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Float64Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;

public interface JsCast {

  static int[] ints(JsArray<JSObject> jsArray, int ind) {
    return ints(jsArray.get(ind));
  }

  static int[] ints(JSObject intArray) {
    return JsMemoryAccess.toJavaArray(intArray.<Int32Array>cast());
  }

  static Int32Array jsInts(int... array) {
    return JsMemoryAccess.bufferView(array);
  }

  static byte[] bytes(JsArray<JSObject> jsArray, int ind) {
    return bytes(jsArray.get(ind));
  }

  static byte[] bytes(JSObject intArray) {
    return JsMemoryAccess.toJavaArray(intArray.<Int8Array>cast());
  }

  static Int8Array jsBytes(byte... array) {
    return JsMemoryAccess.bufferView(array);
  }

  static double[] doubles(JsArray<JSObject> jsArray, int ind) {
    return doubles(jsArray.get(ind));
  }

  static double[] doubles(JSObject numbersArray) {
    return JsMemoryAccess.toJavaArray(numbersArray.<Float64Array>cast());
  }

  static Float64Array jsNumbers(double... array) {
    return JsMemoryAccess.bufferView(array);
  }

  static String string(JsArray<JSObject> jsArray, int ind) {
    return jsString(jsArray, ind).stringValue();
  }

  static JSString jsString(JsArray<JSObject> jsArray, int ind) {
    return (JSString) jsArray.get(ind);
  }

  static Object directJsToJava(JsArray<JSObject> jsArray, int ind) {
    return JsHelper.directJsToJava(jsArray.get(ind));
  }

  static JSString jsString(String str) {
    return JSString.valueOf(str);
  }

  static JSString jsString(char[] chars) {
    return TextDecoder.decodeUTF16(chars);
  }
}
