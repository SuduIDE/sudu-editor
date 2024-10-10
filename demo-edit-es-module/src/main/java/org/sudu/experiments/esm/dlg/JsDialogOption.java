package org.sudu.experiments.esm.dlg;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

/*
export type DialogOption = {
  title: string
  isEnabled: boolean
}
*/

public interface JsDialogOption extends JSObject {
  @JSProperty
  JSObject getTitle();

  @JSProperty
  boolean getIsEnabled();
}
