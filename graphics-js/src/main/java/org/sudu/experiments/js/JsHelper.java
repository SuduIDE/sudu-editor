package org.sudu.experiments.js;

import org.sudu.experiments.GLApi;
import org.teavm.interop.NoSideEffects;
import org.teavm.jso.*;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.typedarrays.ArrayBuffer;

import java.util.Objects;
import java.util.function.Consumer;

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

  public static GLApi.Context createWebglContext(HTMLCanvasElement canvas) {
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

  public static void onError(JSError error) {
    consoleInfo("on error ", JSString.valueOf(error.getMessage()));
  }

  static String[] splitPath(JSString path) {
    if (JSObjects.isUndefined(path) || path == null || path.getLength() == 0) return new String[0];
    JsArrayReader<JSString> split = stringSplit(path, JSString.valueOf("/"));
    if (split.getLength() == 0) return new String[0];
    String[] strings = new String[split.getLength() - 1];
    for (int i = 0; i < strings.length; i++)
      strings[i] = split.get(i).stringValue();
    return strings;
  }

  @JSBody(params = {"str", "arg" }, script = "return str.split(arg);")
  @NoSideEffects
  static native JsArrayReader<JSString> stringSplit(JSString str, JSString arg);

  static JsFunctions.Consumer<ArrayBuffer> toJava(Consumer<byte[]> consumer) {
    return jsArrayBuffer -> consumer.accept(
        JsMemoryAccess.toByteArray(jsArrayBuffer));
  }

  public static JsFunctions.Consumer<JSError> wrapError(Consumer<String> onError) {
    return jsError -> onError.accept(jsError.getMessage());
  }

  public static JsFunctions.Consumer<JSError> wrapError(String title, Consumer<String> onError) {
    return jsError -> onError.accept(title.concat(jsError.getMessage()));
  }

  interface HTMLElement extends org.teavm.jso.dom.html.HTMLElement {
    @JSMethod("getBoundingClientRect")
    DOMRect getBoundingClientRectD();
  }

  @JSBody(params = {"s"}, script = "console.info(s);")
  public static native void consoleInfo(String s);

  @JSBody(params = {"s"}, script = "console.info(s);")
  public static native void consoleInfo(JSObject s);

  @JSBody(params = {"s0", "obj"}, script = "console.info(s0 + obj);")
  public static native void consoleInfo(String s0, JSObject obj);

  @JSBody(params = {"s0", "obj"}, script = "console.info(s0, obj);")
  public static native void consoleInfo2(String s0, JSObject obj);

  @JSBody(params = {"s0", "obj1", "obj2"}, script = "console.info(s0, obj1, obj2);")
  public static native void consoleInfo2(String s0, JSObject obj1, JSObject obj2);

  @JSBody(params = {"s0", "obj1", "s2", "obj3"}, script = "console.info(s0, obj1, s2, obj3);")
  public static native void consoleInfo2(String s0, JSObject obj1, String s2, JSObject obj3);

  @JSBody(params = {"s0", "obj"}, script = "console.error(s0 + obj);")
  public static native void consoleError(String s0, JSObject obj);

  @JSBody(params = {"s0", "obj"}, script = "console.error(s0, obj);")
  public static native void consoleError2(String s0, JSObject obj);

  @JSBody(params = {"s0"}, script = "console.error(s0);")
  public static native void consoleError(String s0);

  @JSBody(params = {"s0"}, script = "console.error(s0);")
  public static native void consoleError(JSObject s0);

  @JSBody(params = {"s", "d"}, script = "console.info(s + d);")
  public static native void consoleInfo(String s, double d);

  @JSBody(params = {"s0", "obj1", "obj2"}, script = "console.info(s0 + obj1 + obj2);")
  public static native void consoleInfo(String s0, JSObject obj1, JSObject obj2);

  @JSBody(params = {"s0", "obj1", "s2", "obj3"}, script = "console.info(s0 + obj1 + s2 + obj3);")
  public static native void consoleInfo(String s0, JSObject obj1, String s2, JSObject obj3);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  @NoSideEffects
  public static native String toStringTag(JSObject obj);

  @JSBody(params = {"obj"}, script = "return obj[Symbol.toStringTag];")
  @NoSideEffects
  public static native JSString jsToStringTag(JSObject obj);

  @JSBody(params = { "object", "name" }, script = "return name in object;")
  @NoSideEffects
  public static native boolean hasProperty(JSObject object, String name);

  @JSBody(params = { "object", "name" }, script = "return object[name];")
  @NoSideEffects
  public static native <T extends JSObject> T getProperty(JSObject object, String name);

  @JSBody(params = {"a", "b"}, script = "return a === b;")
  @NoSideEffects
  public static native boolean strictEquals(JSObject a, JSObject b);

  @JSBody(params = {"a"}, script = "return typeof a;")
  @NoSideEffects
  public static native JSString typeof(JSObject a);

  @JSBody(params = {"a", "b"}, script = "return [a, b];")
  @NoSideEffects
  public static native <T extends JSObject> JsArray<T> toJsArray(T a, T b);

  @JSBody(params = {"a", "b", "c"}, script = "return [a, b, c];")
  @NoSideEffects
  public static native JsArray<JSObject> toJsArray(JSObject a, JSObject b, JSObject c);

  @JSBody(params = {"n"}, script = "return String(n);")
  @NoSideEffects
  public static native String jsDoubleToString(double n);

  @JSBody(params = {"x"}, script = "return String(x);")
  @NoSideEffects
  public static native JSString jsToString(JSObject x);

  @JSBody(params = {"arg"}, script = "return new Error(arg);")
  @NoSideEffects
  public static native JSError newError(String arg);

  public interface Error extends JSObject {
    @JSProperty JSString getMessage();
  }

  @JSBody(params = "error", script = "return error.message;")
  @NoSideEffects
  public static native JSString message(JSError error);

  @JSBody(params = {"left", "right"}, script = "return left + right;")
  public static native JSString concat(String left, JSObject right);

  @JSBody(params = {"left", "right"}, script = "return left + right;")
  public static native JSString concat(JSString left, JSObject right);

  public static JSString getMessage(JSError error) {
    return JSString.valueOf(error.getMessage());
  }

  public interface WithId extends JSObject {
    @JSProperty JSString getId();

    static JSString get(JSObject o) {
      return o.<WithId>cast().getId();
    }
  }

  @JSBody(params = {"x"}, script = "return x ? 1 : 0;")
  @NoSideEffects
  public static native boolean jsIf(JSObject x);

  @JSBody(params = {"x"}, script = "return x.constructor.name;")
  @NoSideEffects
  public static native JSString constructorName(JSObject x);

  public static String toString(JSString jsString, String orElse) {
    return jsIf(jsString) ? jsString.stringValue() : orElse;
  }

  public static JsArray<JSString> javaToJs(String[] array) {
    if (array == null) {
      return null;
    }
    JsArray<JSString> result = JsArray.create(array.length);
    for (int i = 0; i < array.length; ++i) {
      result.set(i, JSString.valueOf(array[i]));
    }
    return result;
  }

  public static String[] jsToJava(JsArrayReader<JSString> array) {
    if (array == null) {
      return null;
    }
    String[] result = new String[array.getLength()];
    for (int i = 0; i < result.length; ++i) {
      result[i] = array.get(i).stringValue();
    }
    return result;
  }

  public static int[] toJavaIntArray(JsArray<JSNumber> array) {
    int[] r = new int[array.getLength()];
    for (int i = 0; i < r.length; ++i) {
      r[i] = array.get(i).intValue();
    }
    return r;
  }

  public static JsArray<JSNumber> toJs(int[] array) {
    JsArray<JSNumber> r = JsArray.create(array.length);
    for (int i = 0; i < array.length; ++i) {
      r.set(i, JSNumber.valueOf(array[i]));
    }
    return r;
  }

  @NoSideEffects
  public static native JSObject directJavaToJs(Object obj);

  @NoSideEffects
  public static native Object directJsToJava(JSObject obj);

  @NoSideEffects
  @JSBody(params = {"s", "prefix"}, script = "return s.startsWith(prefix);")
  public static native boolean startsWith(JSString s, String prefix);

  @NoSideEffects
  @JSBody(params = {"obj"}, script = "return JSON.stringify(obj);")
  public static native JSString stringify(JSObject obj);
}
