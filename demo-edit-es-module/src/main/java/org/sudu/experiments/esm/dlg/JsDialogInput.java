package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

// export type DialogInput
public interface JsDialogInput extends JSObject {
  //  title: string
  @JSProperty
  JSString getTitle();

  //  text: string
  @JSProperty
  JSString getText();

  //  options: DialogOption[]
  @JSProperty
  JsArray<JsDialogOption> getOptions();

  //  buttons: DialogButton[]
  @JSProperty
  JsArray<JsDialogButton> getButtons();
}
