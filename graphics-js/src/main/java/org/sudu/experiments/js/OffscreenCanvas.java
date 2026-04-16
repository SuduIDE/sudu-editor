package org.sudu.experiments.js;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class OffscreenCanvas implements JSObject {
  public abstract JSObject getContext(String contextId, JSObject attributes);
  public abstract ImageBitmap transferToImageBitmap();

  @JSBody(params = {"w", "h"}, script = "return new OffscreenCanvas(w,h);")
  @NoSideEffects
  public static native OffscreenCanvas crate(int w, int h);
}
