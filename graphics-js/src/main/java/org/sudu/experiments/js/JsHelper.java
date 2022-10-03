package org.sudu.experiments.js;

import org.sudu.experiments.GLApi;
import org.sudu.experiments.math.V2i;
import org.teavm.jso.*;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

import java.util.Objects;

public class JsHelper {

  public static void addPreText(String div, String result) {
    org.teavm.jso.dom.html.HTMLElement e = HTMLDocument.current().createElement("pre");
    e.setInnerHTML(result);
    CSSStyleDeclaration style = e.getStyle();
    style.setProperty("padding-left", "10px");
    style.setProperty("padding-right", "10px");
    HTMLDocument.current().getElementById(div).appendChild(e);
  }

  public static HTMLCanvasElement createMainCanvas(V2i size, String className) {
    HTMLCanvasElement element = createCanvas();
    if (className != null) element.setClassName(className);
    element.setWidth(size.x);
    element.setHeight(size.y);
    element.setTabIndex(0);
    CSSStyleDeclaration style = element.getStyle();
    style.setProperty("width", "100%");
    style.setProperty("height", "100%");
    style.setProperty("outline", "none");

    return element;
  }

  public static HTMLCanvasElement createCanvas() {
    return HTMLDocument.current().createElement("canvas").cast();
  }

  @JSBody(params = {"antialias", "stencil", "premultipliedAlpha", "alpha"},
      script = "return {antialias: antialias, stencil: stencil, premultipliedAlpha: premultipliedAlpha, alpha: alpha};")
  public static native JSObject canvasContextAttributes(
      boolean antialias,
      boolean stencil,
      boolean premultipliedAlpha,
      boolean alpha
  );

  public static GLApi.Context createContext(HTMLCanvasElement canvas) {
    JSObject attributes = canvasContextAttributes(false, false, true, true);
    return canvas.getContext("webgl2", attributes).cast();
  }

  public static String setCursor(String cursor, String currentCursor, org.teavm.jso.dom.html.HTMLElement element) {
    if (!Objects.equals(cursor, currentCursor)) {
      currentCursor = cursor;
      CSSStyleDeclaration style = element.getStyle();
      if (cursor != null && cursor.length() > 0) {
        style.setProperty("cursor", cursor);
      } else {
        style.removeProperty("cursor");
      }
    }
    return currentCursor;
  }

  public interface DOMRect extends JSObject {
    @JSProperty double getLeft();
    @JSProperty double getRight();
    @JSProperty double getTop();
    @JSProperty double getBottom();
    @JSProperty double getWidth();
    @JSProperty double getHeight();
  }

  interface HTMLElement extends org.teavm.jso.dom.html.HTMLElement {
    @JSMethod("getBoundingClientRect") JsHelper.DOMRect getBoundingClientRectD();
  }

  public static V2i elementSizeToPixelSize(org.teavm.jso.dom.html.HTMLElement element) {
    double devicePixelRatio = Window.current().getDevicePixelRatio();
    DOMRect rect = element.<HTMLElement>cast().getBoundingClientRectD();
    double left = rect.getLeft() * devicePixelRatio;
    double right = rect.getRight() * devicePixelRatio;
    double top = rect.getTop() * devicePixelRatio;
    double bottom = rect.getBottom() * devicePixelRatio;

    int sizeX = (int) Math.rint(right - left);
    int sizeY = (int) Math.rint(bottom - top);
    return new V2i(sizeX, sizeY);
  }

  @JSBody(params = {"s0", "obj"}, script = "console.info(s0 + obj);")
  public static native void consoleInfo(String s0, JSObject obj);

  @JSBody(params = {"s0", "obj1", "obj2"}, script = "console.info(s0 + obj1 + obj2);")
  public static native void consoleInfo(String s0, JSObject obj1, JSObject obj2);

  @JSBody(params = {"s0", "obj1", "s2", "obj3"}, script = "console.info(s0 + obj1 + s2 + obj3);")
  public static native void consoleInfo(String s0, JSObject obj1, String s2, JSObject obj3);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  public static native String toStringTag(JSObject obj);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  public static native JSString jsToStringTag(JSObject obj);

  private static boolean isDigit(char value) {
    return '0' <= value && value <= '9';
  }

  private static int digitValue(char value) { return value - '0'; }
}
