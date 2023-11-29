package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.js.HTMLDocument;
import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLImageElement;

import java.util.function.Consumer;

public class WebGraphics extends WglGraphics {
  protected Runnable repaint;

  public WebGraphics(GLApi.Context gl, Runnable repaint) {
    super(gl, JsCanvas::new, isDesktop());
    this.repaint = repaint;
  }

  // todo: implement
  public static boolean isDesktop() {
    return true;
  }

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
      repaint.run();
    });
    image.addEventListener("err or", onError);
    image.setCrossOrigin("anonymous");
    image.setSrc(src);
  }
}
