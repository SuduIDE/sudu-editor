package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsExternalFileWriter extends JSObject {
  Promise<JSObject> writeFile(JSString path, JSString content, JSString encoding);
}
