package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsCancellationToken implements JSObject {
  @JSProperty abstract boolean getIsCancellationRequested();
  @JSProperty abstract void setIsCancellationRequested(boolean isCancellationRequested);

  @JSBody(script = "return {isCancellationRequested: false};")
  public static native JsCancellationToken create();
}
