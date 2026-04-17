package org.sudu.experiments.js;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.canvas.CanvasImageSource;

public abstract class OffscreenCanvas implements JSObject, CanvasImageSource {
  @JSProperty
  public abstract int getWidth();
  @JSProperty
  public abstract int getHeight();

  @JSProperty
  public abstract void setWidth(int width);
  @JSProperty
  public abstract void setHeight(int height);

  public abstract JSObject getContext(String contextId, JSObject attributes);
  public abstract ImageBitmap transferToImageBitmap();

  @JSBody(params = {"w", "h"}, script = "return new OffscreenCanvas(w,h);")
  @NoSideEffects
  public static native OffscreenCanvas crate(int w, int h);
}
