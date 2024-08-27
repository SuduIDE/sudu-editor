package org.sudu.experiments.js;

import org.sudu.experiments.Disposable;
import org.teavm.jso.JSObject;

public interface JsDisposable extends JSObject {
  void dispose();

  static JsDisposable empty() {
    return () -> {};
  }

  static JsDisposable of(Disposable d) {
    return d::dispose;
  }
}
