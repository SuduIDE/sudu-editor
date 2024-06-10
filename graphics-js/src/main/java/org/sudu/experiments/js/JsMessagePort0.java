package org.sudu.experiments.js;

import org.teavm.jso.JSObject;

public interface JsMessagePort0 extends JSObject {
  void postMessage(JSObject message);
  void postMessage(JSObject message, JsArrayReader<?> transfer);
}
