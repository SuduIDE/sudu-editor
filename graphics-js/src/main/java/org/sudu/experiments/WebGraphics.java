package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.js.HTMLDocument;
import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.OffscreenCanvas;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLImageElement;

import java.util.function.Consumer;

public class WebGraphics extends WglGraphics {
  static final float cleartypeTextPow = 2.25f;
  static final float grayscaleTextPow = 0.625f;

  static WebGraphics instance;

  OffscreenCanvas glCanvas;
  int canvasWidth;
  int canvasHeight;

  public static WebGraphics getInstance() {
    if (instance == null) {
      var c = OffscreenCanvas.crate(256, 256);
      var gl = createWebglContext(c);
      if (gl != null) {
        instance = new WebGraphics(c, gl);
      }
    }
    return instance;
  }

  private WebGraphics(OffscreenCanvas canvas, GLApi.Context gl) {
    super(gl, JsCanvas::new, isDesktop(), cleartypeTextPow, grayscaleTextPow);
    glCanvas = canvas;
    canvasWidth = glCanvas.getWidth();
    canvasHeight = glCanvas.getHeight();
  }

  public static GLApi.Context createWebglContext(OffscreenCanvas canvas) {
    JSObject attributes = canvasContextAttributes(false, false, true, true);
    return canvas.getContext("webgl2", attributes).cast();
  }

  public void ensureSize(int x, int y) {
    int cWidth = glCanvas.getWidth(), cHeight = glCanvas.getHeight();
    if (cWidth < x) {
      glCanvas.setWidth(canvasWidth = x);
      JsHelper.consoleInfo("canvasWidth now", canvasWidth);
    }
    if (cHeight < y) {
      glCanvas.setHeight(canvasHeight = y);
      JsHelper.consoleInfo("canvasHeight now", canvasHeight);
    }
  }

  // todo: implement
  public static boolean isDesktop() {
    return true;
  }

  @JSBody(params = {"antialias", "stencil", "premultipliedAlpha", "alpha"},
      script = "return {" +
          "antialias: antialias, " +
          "stencil: stencil, " +
          "premultipliedAlpha: premultipliedAlpha, " +
          "alpha: alpha};")
  public static native JSObject canvasContextAttributes(
      boolean antialias,
      boolean stencil,
      boolean premultipliedAlpha,
      boolean alpha
  );

  public FontDesk fontDesk(String family, float size, int weight, int style) {
    return ((JsCanvas)mCanvas).createFontDesk(family, size, weight, style);
  }

  public interface ErrorEvent extends org.teavm.jso.dom.events.ErrorEvent {
    @JSProperty("message")
    JSString getMessageJs();
  }

  EventListener<ErrorEvent> onError = e -> JsHelper.consoleInfo("Error loading image: ", e.getMessageJs());

  public void loadImage(String src, Consumer<GL.Texture> onLoad) {
    HTMLImageElement image = HTMLDocument.current().createElement("img").cast();
    image.addEventListener("load", e -> {
      GL.Texture texture = createTexture();
      texture.setContent(image.getNaturalWidth(), image.getNaturalHeight(), (t, gl) ->
          gl.texSubImage2D(gl.TEXTURE_2D, 0, 0, 0, gl.RGBA, gl.UNSIGNED_BYTE, image));
      onLoad.accept(texture);
    });
    image.addEventListener("error", onError);
    image.setCrossOrigin("anonymous");
    image.setSrc(src);
  }
}
