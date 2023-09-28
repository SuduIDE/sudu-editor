package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class ResizeObserver implements JSObject {

  @JSBody(params = "callback", script = "return new ResizeObserver(callback);")
  public static native ResizeObserver create(Callback  callback);

  public abstract void disconnect();
  public abstract void observe(HTMLElement e);
  public abstract void observe(HTMLElement e, Options options);
  public abstract void unobserve(HTMLElement e);

  @JSBody(script = "return {box: 'device-pixel-content-box'};")
  public static native Options devicePixelContentBox();

  @JSBody(
      params = {"obs", "element", "options"},
      script = """
          try { obs.observe(element, options); }
          catch (error) { console.error(error); obs.observe(element); }"""
  )
  public static native void tryObserve(ResizeObserver obs, HTMLElement e, Options options);

  public static void observePixelsOrDefault(ResizeObserver o, HTMLElement e) {
    tryObserve(o, e, devicePixelContentBox());
  }

  public void observePixelsOrDefault(HTMLElement e) {
    observePixelsOrDefault(this, e);
  }

  public interface ResizeObserverEntry extends JSObject {
    interface Size extends JSObject {
      @JSProperty double getBlockSize();
      @JSProperty double getInlineSize();
    }

    @JSProperty JsArrayReader<Size> getContentBoxSize();

    String devicePixelContentBoxSize = "devicePixelContentBoxSize";
    @JSProperty JsArrayReader<Size> getDevicePixelContentBoxSize();
    @JSProperty DOMRect getContentRect();
    @JSProperty HTMLElement getTarget();
  }

  @JSFunctor
  public interface Callback extends JSObject {
    void f(JsArrayReader<ResizeObserverEntry> entries, ResizeObserver observer);
  }

  interface Options extends JSObject {}
}
