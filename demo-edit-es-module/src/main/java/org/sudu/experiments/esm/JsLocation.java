package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsLocation implements JSObject {
  @JSProperty abstract JsRange getRange();
  @JSProperty abstract JsUri getUri();
}
