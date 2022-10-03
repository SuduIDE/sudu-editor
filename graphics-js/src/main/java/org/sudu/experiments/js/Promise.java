package org.sudu.experiments.js;

import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.sudu.experiments.js.JsFunctions.Consumer;
import org.sudu.experiments.js.JsFunctions.Function;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;

public interface Promise<T extends JSObject> extends JSObject {
  void then(Consumer<T> onResult, Consumer<JSError> onError);
  <R extends JSObject> Promise<R> then(Function<T, Promise<R>> promiseFunction);

  class Factory {
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

    @JSBody(
        params = {"executor"},
        script = "return new Promise(executor);"
    )
    public static native <T extends JSObject> Promise<T> create(BiConsumer<Consumer<T>, Consumer<JSObject>> executor);
  }
}
