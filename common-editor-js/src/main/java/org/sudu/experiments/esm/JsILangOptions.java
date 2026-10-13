package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsILangOptions extends JSObject {
  boolean isEnabled(JSString lang);
  void enable(JSString lang);
  void disable(JSString lang);
  void enableAll();
  void disableAll();
}
