package org.sudu.experiments;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class NodeWorkerThreads implements JSObject {

  @JSBody(script = "return parentPort")
  public static native NodeWorkerThreads requireWorkerThreads();

  @JSProperty
  @JSBody(script = "return parentPort")
  public static native JSObject getParentPort();
}
