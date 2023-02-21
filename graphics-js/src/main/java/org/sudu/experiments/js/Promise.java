package org.sudu.experiments.js;

import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.sudu.experiments.js.JsFunctions.Consumer;
import org.sudu.experiments.js.JsFunctions.Function;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

public abstract class Promise<T extends JSObject> implements JSObject {
  public native void then(Consumer<T> onResult, Consumer<JSError> onError);
  public native <R extends JSObject> Promise<R> then(Function<T, Promise<R>> promiseFunction);

  @JSBody(
      params = {"executor"},
      script = "return new Promise(executor);"
  )
  public static native <T extends JSObject> Promise<T> create(
      BiConsumer<Consumer<T>, Consumer<JSObject>> executor
  );

  @JSBody(
      params = {"value"},
      script = "return Promise.resolve(value);"
  )
  public static native <T extends JSObject> Promise<T> resolve(T value);

  @JSBody(
      params = {"message"},
      script = "return Promise.reject(new Error(message));"
  )
  public static native <T extends JSObject> Promise<T> reject(String message);
}
