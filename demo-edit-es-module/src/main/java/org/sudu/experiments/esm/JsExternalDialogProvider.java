package org.sudu.experiments.esm;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;

public interface JsExternalDialogProvider extends JSObject {
  Promise<JSObject> showModalDialog(JSObject input);
}
