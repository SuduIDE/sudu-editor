package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

// export type DialogResult
public interface JsDialogResult extends JSObject {
  // button: DialogButton
  @JSProperty
  JsDialogButton getButton();

  // options: DialogOption[]
  @JSProperty
  JsArray<JsDialogOption> getOptions();
}
