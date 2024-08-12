package org.sudu.experiments.js;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public abstract class JsArray<T extends JSObject> implements JsArrayReader<T> {

  @JSIndexer
  public abstract void set(int index, T value);

  public abstract int push(T a);

  public abstract int push(T a, T b);

  public abstract int push(T a, T b, T c);

  public abstract int push(T a, T b, T c, T d);

  public abstract T pop();

  public abstract T shift();

  public abstract int indexOf(T value);

  public abstract JsArray<T> slice(int from, int to);

  @JSMethod("toString")
  public abstract JSString toJsString();

  @JSBody(script = "return new Array();")
  @NoSideEffects
  public static native <T extends JSObject> JsArray<T> create();

  @JSBody(params = "size", script = "return new Array(size);")
  @NoSideEffects
  public static native <T extends JSObject> JsArray<T> create(int size);

  @JSBody(params = "object", script = "return Array.isArray(object);")
  @NoSideEffects
  public static native boolean isArray(JSObject object);

}
