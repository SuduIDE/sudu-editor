package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsDisposable extends JSObject {
  void dispose();

  static JsDisposable empty() {
    return () -> {};
  }
}
