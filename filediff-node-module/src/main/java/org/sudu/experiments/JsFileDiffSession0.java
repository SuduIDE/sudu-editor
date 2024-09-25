package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;

public class JsFileDiffSession0 implements JsFileDiffSession {
  @Override
  public Promise<JSObject> shutdown() {
    return Promise.resolve(null);
  }
}
