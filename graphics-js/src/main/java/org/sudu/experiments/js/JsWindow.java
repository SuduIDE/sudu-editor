package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Location;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.WindowEventTarget;

public abstract class JsWindow implements JSObject, WindowEventTarget {
  @JSProperty
  public abstract Location getLocation();

  @JSProperty
  public abstract HTMLDocument getDocument();


  @JSBody(script = "return window;")
  public static native JsWindow current();

  @JSProperty
  public abstract JsWindow getTop();

  @JSBody(script = "return self;")
  public static native JsWindow worker();

  @JSProperty
  public abstract double getDevicePixelRatio();

  @JSBody(params = { "handler", "delay" }, script = "return setTimeout(handler, delay);")
  public static native int setTimeout(TimerHandler handler, int delay);

  @JSBody(params = "callback", script = "return requestAnimationFrame(callback);")
  public static native int requestAnimationFrame(AnimationFrameCallback callback);

  @JSBody(params = "requestId", script = "cancelAnimationFrame(requestId);")
  public static native void cancelAnimationFrame(int requestId);
}
