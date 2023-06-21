package org.sudu.experiments.utils;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;

import java.util.function.Consumer;

public abstract class PromiseUtils {

  public static <T extends JSObject> void promiseOrT(
      JSObject obj, JsFunctions.Consumer<T> consumer,
      Consumer<String> onError
  ) {
    if (Promise.isThenable(obj)) {
      obj.<Promise<T>>cast().then(consumer, consumeError(onError));
    } else {
      consumer.f(obj.cast());
    }
  }

  static JsFunctions.Consumer<JSError> consumeError(Consumer<String> onError) {
    return error -> onError.accept(error.getMessage());
  }
}
