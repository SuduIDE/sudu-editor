package org.sudu.experiments.js;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

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
}
