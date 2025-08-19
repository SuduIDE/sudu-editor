package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsFont implements JSObject {
  @JSProperty
  public abstract String getFamily();
  @JSProperty
  public abstract float getSize();
  @JSProperty
  public abstract float getWeight();
}
