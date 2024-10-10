package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

// export type DialogState
public interface JsDialogState extends JSObject {
  // options: DialogOption[]
  @JSProperty
  JsArray<JsDialogOption> getOptions();
}
