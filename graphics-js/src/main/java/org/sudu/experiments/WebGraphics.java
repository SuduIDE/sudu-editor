package org.sudu.experiments;

import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.V2i;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLImageElement;

import java.util.function.Consumer;

public class WebGraphics extends WglGraphics {
  public WebGraphics(GLApi.Context gl, V2i canvasSize, Runnable repaint) {
    super(gl, canvasSize, repaint);
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
      repaint();
    });
    image.addEventListener("err or", onError);
    image.setCrossOrigin("anonymous");
    image.setSrc(src);
  }

  public Canvas createCanvas(int w, int h) {
    return new JsCanvas(w, h);
  }
}
