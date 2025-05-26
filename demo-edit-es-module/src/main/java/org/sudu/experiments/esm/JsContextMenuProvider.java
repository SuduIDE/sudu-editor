package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsContextMenuProvider extends JSObject {
  void showContextMenu(JsArray<JSString> actions);
}
