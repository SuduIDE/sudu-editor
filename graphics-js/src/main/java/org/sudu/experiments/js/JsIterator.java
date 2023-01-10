package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JsIterator<T extends JSObject> extends JSObject  {
  interface Result<T extends JSObject> extends JSObject {
    @JSProperty
    boolean getDone();
    @JSProperty T getValue();
  }
  Result<T> next();
}
