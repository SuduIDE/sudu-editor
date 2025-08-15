package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsExternalMessageBar extends JSObject {
  void setStatusBarMessage(JSString text);
  void setToolBarMessage(JSString text);
}
