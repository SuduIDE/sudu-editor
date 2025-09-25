package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsNotificationsProvider extends JSObject {
  void error(JSString message);

  void warn(JSString message);

  void info(JSString message);
}
