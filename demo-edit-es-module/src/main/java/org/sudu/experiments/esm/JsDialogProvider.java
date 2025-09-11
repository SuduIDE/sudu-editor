package org.sudu.experiments.esm;

import org.sudu.experiments.esm.dlg.JsDialogInput;
import org.sudu.experiments.esm.dlg.JsDialogOption;
import org.sudu.experiments.esm.dlg.JsDialogResult;
import org.sudu.experiments.js.JsSet;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

// export interface ExternalDialogProvider
public interface JsDialogProvider extends JSObject {
  // showModalDialog(input: DialogInput): Promise<DialogResult | null>
  Promise<JsDialogResult> showModalDialog(JsDialogInput input);

  @JSProperty(value = "selectedOptions")
  JsSet<JsDialogOption> getSelectedOptions();
}
