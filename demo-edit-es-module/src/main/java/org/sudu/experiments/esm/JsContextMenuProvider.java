package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsContextMenuProvider extends JSObject {
  void showContextMenu(JsArray<JSString> actions);

  static JsArray<JSString> cutCopyPaste() {
    var a = JsArray.<JSString>create();
    a.push(JSString.valueOf("Cut"));
    a.push(JSString.valueOf("Copy"));
    a.push(JSString.valueOf("Paste"));
    return a;
  }
}
