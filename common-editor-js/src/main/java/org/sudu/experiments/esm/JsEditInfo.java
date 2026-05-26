package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsEditInfo implements JSObject {

  @JSProperty
  public abstract int getFrom();

  @JSProperty
  public abstract int getTo();

  @JSBody(params = {"from", "to"},
      script = "return {from: from, to: to};"
  )
  public static native JsEditInfo create(int from, int to);
}
