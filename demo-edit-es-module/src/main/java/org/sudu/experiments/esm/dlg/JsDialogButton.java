package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

// export type DialogButton
public interface JsDialogButton extends JSObject {
  // title: string
  @JSProperty
  JSString getTitle();

  @JSProperty
  boolean isDefault();

  // isDefault?: boolean
  static boolean isDefault(JsDialogButton button) {
    return JsHelper.hasProperty(button, "default")
        && button.isDefault();
  }

  // isEnabled: (state: DialogState) => boolean
  @JSProperty
  IsEnabled getIsEnabled();

  @JSFunctor
  interface IsEnabled extends JSObject {
    boolean get(JsDialogState state);
  }
}
