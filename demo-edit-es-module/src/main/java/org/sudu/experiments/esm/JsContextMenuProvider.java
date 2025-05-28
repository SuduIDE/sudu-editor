package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsContextMenuProvider extends JSObject {
  void showContextMenu(JsArray<JSString> actions);

  static JsArray<JSString> cutCopyPaste() {
    var a = JsArray.<JSString>create();
    a.push(jsCut());
    a.push(jsCopy());
    a.push(jsPaste());
    return a;
  }

  static JSString jsCopy() { return JSString.valueOf("Copy"); }
  static JSString jsCut() { return JSString.valueOf("Cut"); }
  static JSString jsPaste() { return JSString.valueOf("Paste"); }
  static JSString jsAlignWith() { return JSString.valueOf("AlignWith"); }
  static JSString jsRemoveAlignment() { return JSString.valueOf("RemoveAlignment"); }
}
