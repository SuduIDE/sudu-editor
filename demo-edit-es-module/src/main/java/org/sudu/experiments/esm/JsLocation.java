package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

public abstract class JsLocation implements JSObject {
  @JSProperty
  public abstract JsRange getRange();
  @JSProperty
  public abstract JsUri getUri();

  public static boolean isInstance(JSObject obj) {
    return JSObjects.hasProperty(obj, "range") && JSObjects.hasProperty(obj, "uri");
  }
}
