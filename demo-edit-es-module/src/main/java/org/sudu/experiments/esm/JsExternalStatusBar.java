package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsExternalStatusBar extends JSObject {
  void setMessage(JSString text);
}
