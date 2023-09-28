package org.sudu.experiments.js;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JsArrayReader<T extends JSObject> extends JSObject {
  @JSProperty
  @NoSideEffects
  int getLength();

  @JSIndexer
  @NoSideEffects
  T get(int index);
}
