package org.sudu.experiments.js;

import org.teavm.jso.JSObject;

public interface JsAsyncIterator<T extends JSObject> extends JSObject  {
  Promise<JsIterator.Result<T>> next();
}
