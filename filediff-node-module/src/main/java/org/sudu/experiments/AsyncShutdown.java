package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;

public interface AsyncShutdown extends JSObject {

  Promise<JSObject> shutdown();

}
