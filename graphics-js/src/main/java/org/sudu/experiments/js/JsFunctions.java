package org.sudu.experiments.js;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsFunctions {
  @JSFunctor interface Function<T extends JSObject, R extends JSObject> extends JSObject {
    R f(T t);
  }

  @JSFunctor interface Consumer<T extends JSObject> extends JSObject {
    void f(T t);
  }

  @JSFunctor interface BiConsumer<T extends JSObject, U extends JSObject> extends JSObject {
    void f(T t, U u);
  }

  @JSFunctor interface Runnable extends JSObject {
    void f();
  }

  // methodRef !== lambda (C) TeaVM
  @SuppressWarnings("Convert2MethodRef")
  static java.lang.Runnable toJava(Runnable r) {
    return () -> r.f();
  }

  static Consumer<JSString> toJs(java.util.function.Consumer<String> consumer) {
    return jsString-> consumer.accept(jsString.stringValue());
  }

  static java.util.function.Consumer<String> toJava(Consumer<JSString> consumer) {
    return s-> consumer.f(JSString.valueOf(s));
  }

}
