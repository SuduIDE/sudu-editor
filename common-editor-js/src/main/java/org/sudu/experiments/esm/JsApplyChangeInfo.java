package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class JsApplyChangeInfo implements JSObject {

  @JSProperty
  public abstract int getOldFrom();

  @JSProperty
  public abstract int getOldTo();

  @JSProperty
  public abstract int getNewFrom();

  @JSProperty
  public abstract int getNewTo();

  @JSProperty
  public abstract boolean getIsAccepted();

  @JSBody(params = {"oldFrom", "oldTo", "newFrom", "newTo", "isAccepted"},
      script = "return {" +
          "oldFrom: oldFrom, oldTo: oldTo" +
          ", newFrom: newFrom, newTo: newTo" +
          ", isAccepted: isAccepted};")
  public static native JsApplyChangeInfo create(
      int oldFrom, int oldTo,
      int newFrom, int newTo,
      boolean isAccepted
  );
}
