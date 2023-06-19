package org.sudu.experiments.js;

import org.sudu.experiments.GLApi;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.teavm.jso.*;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
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

  public static HTMLCanvasElement createMainCanvas(String className) {
    HTMLCanvasElement element = createCanvas();
    if (className != null) element.setClassName(className);
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

  interface HTMLElement extends org.teavm.jso.dom.html.HTMLElement {
    @JSMethod("getBoundingClientRect")
    DOMRect getBoundingClientRectD();
  }

  @SuppressWarnings("SuspiciousNameCombination")
  public static V2i getBoundingClientRect(
      org.teavm.jso.dom.html.HTMLElement element,
      JSString id
  ) {
    double devicePixelRatio = Window.current().getDevicePixelRatio();
    JsHelper.consoleInfo("getBoundingClientRect: ", id);
    DOMRect rect = element.<HTMLElement>cast().getBoundingClientRectD();
    double top = rect.getTop() * devicePixelRatio;
    double left = rect.getLeft() * devicePixelRatio;
    double right = rect.getRight() * devicePixelRatio;
    double bottom = rect.getBottom() * devicePixelRatio;

    JsHelper.consoleInfo(" dpr = " + devicePixelRatio);
    JsHelper.consoleInfo("  top * dpr = ", top);
    JsHelper.consoleInfo("  left * dpr = ", left);
    JsHelper.consoleInfo("  right * dpr = ", right);
    JsHelper.consoleInfo("  bottom * dpr = ", bottom);
    JsHelper.consoleInfo("  doubleW = ", right - left);
    JsHelper.consoleInfo("  doubleH = ", bottom - top);

    int iTop = Numbers.iRnd(top);
    int iLeft = Numbers.iRnd(left);
    int iRight = Numbers.iRnd(right);
    int iBottom = Numbers.iRnd(bottom);

    JsHelper.consoleInfo("  iTop = ", iTop);
    JsHelper.consoleInfo("  iLeft = ", iLeft);
    JsHelper.consoleInfo("  iRight = ", iRight);
    JsHelper.consoleInfo("  iBottom = ", iBottom);

    int sizeX = iRight - iLeft;
    int sizeY = iBottom - iTop;
    return new V2i(sizeX, sizeY);
  }

  public static String getStringOrNull(JSString str) {
    return jsIf(str) ? str.stringValue() : null;
  }

  @JSBody(params = {"s"}, script = "console.info(s);")
  public static native void consoleInfo(String s);

  @JSBody(params = {"s"}, script = "console.info(s);")
  public static native void consoleInfo(JSObject s);

  @JSBody(params = {"s0", "obj"}, script = "console.info(s0 + obj);")
  public static native void consoleInfo(String s0, JSObject obj);

  @JSBody(params = {"s", "d"}, script = "console.info(s + d);")
  public static native void consoleInfo(String s, double d);

  @JSBody(params = {"s0", "obj1", "obj2"}, script = "console.info(s0 + obj1 + obj2);")
  public static native void consoleInfo(String s0, JSObject obj1, JSObject obj2);

  @JSBody(params = {"s0", "obj1", "s2", "obj3"}, script = "console.info(s0 + obj1 + s2 + obj3);")
  public static native void consoleInfo(String s0, JSObject obj1, String s2, JSObject obj3);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  public static native String toStringTag(JSObject obj);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  public static native JSString jsToStringTag(JSObject obj);

  @JSBody(params = {"a", "b"}, script = "return a === b;")
  public static native boolean strictEquals(JSObject a, JSObject b);

  @JSBody(params = {"array"}, script = "return array;")
  public static native JSArray<JSObject> toJsArray(@JSByRef JSObject ... array);

  @JSBody(params = {"a", "b"}, script = "return [a, b];")
  public static native JSArray<JSObject> toJsArray(JSObject a, JSObject b);

  @JSBody(params = {"a", "b", "c"}, script = "return [a, b, c];")
  public static native JSArray<JSObject> toJsArray(JSObject a, JSObject b, JSObject c);

  @JSBody(params = {"a"}, script = "return Array.isArray(a);")
  public static native boolean isJsArray(JSObject a);

  @JSBody(params = {"n"}, script = "return String(n);")
  public static native String jsDoubleToString(double n);

  @JSBody(params = {"arg"}, script = "return new JSError(arg);")
  public static native JSError newError(String arg);

  public interface Error extends JSObject {
    @JSProperty JSString getMessage();
  }

  public interface WithId extends JSObject {
    @JSProperty JSString getId();

    static JSString get(JSObject o) {
      return o.<WithId>cast().getId();
    }
  }

  @JSBody(params = {"x"}, script = "return x ? true : false;")
  public static native boolean jsIf(JSObject x);
}
