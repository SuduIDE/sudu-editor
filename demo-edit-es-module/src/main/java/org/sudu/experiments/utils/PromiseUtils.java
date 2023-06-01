package org.sudu.experiments.utils;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;

public abstract class PromiseUtils {

  public static <T extends JSObject> void promiseOrT(
      JSObject obj, JsFunctions.Consumer<T> consumer
  ) {
    if (Promise.isThenable(obj)) {
      Promise<T> promise = obj.cast();
      promise.then(consumer, JsHelper::consoleInfo);
    } else {
      consumer.f(obj.cast());
    }
  }
}
