package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public abstract class URL implements JSObject {
  @JSBody(params = { "obj" }, script = "return URL.createObjectURL(obj);")
  public static native JSString createObjectURL(JSObject obj);

  @JSBody(params = { "url" }, script = "return URL.revokeObjectURL(url);")
  public static native void revokeObjectURL(JSString url);
}
