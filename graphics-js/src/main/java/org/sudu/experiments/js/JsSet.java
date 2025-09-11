package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsSet<T extends JSObject> implements JSObject {

  public abstract JsSet<T> add(T a);

  public abstract boolean has(T a);

  @JSProperty
  public abstract int getSize();
}
