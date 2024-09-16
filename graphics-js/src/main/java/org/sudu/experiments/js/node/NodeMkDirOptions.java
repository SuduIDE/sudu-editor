package org.sudu.experiments.js.node;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class NodeMkDirOptions implements JSObject {

  @JSBody(params = {"rec"}, script = "return {recursive: rec};")
  public static native NodeMkDirOptions create(boolean rec);

}
