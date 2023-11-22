package org.sudu.experiments;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSObject;

public class Debug {

  @JSBody(params = {"s0"}, script = "console.info(s0);")
  public static native void consoleInfo(String s0);

  @JSBody(params = {"s0", "n"}, script = "console.info(s0 + n);")
  public static native void consoleInfo(String s0, double n);

  public static void consoleInfo(String s0, JSObject jsObject) {
    JsHelper.consoleInfo(s0, jsObject);
  }

  @JSBody(params = {"s0", "array"}, script = "console.info(s0 + array);")
  public static native void consoleInfo(String s0, @JSByRef float[] array);

  @JSBody(params = {"s0", "array"}, script = "console.info(s0 + array);")
  public static native void consoleInfo(String s0, @JSByRef int[] array);

  @JSBody(params = {"s0", "obj1", "s2", "obj3"}, script = "console.info(s0 + obj1 + s2 + obj3);")
  public static native void consoleInfo(String s0, double obj1, String s2, double obj3);

  @JSBody(params = {"s0", "obj1", "s2", "s3"}, script = "console.info(s0 + obj1 + s2 + s3);")
  public static native void consoleInfo(String s0, double obj1, String s2, String s3);

  @JSBody(params = {"s0", "obj1", "s2", "s3", "s4"}, script = "console.info(s0 + obj1 + s2 + s3 + s4);")
  public static native void consoleInfo(String s0, double obj1, String s2, String s3, String s4);
}
