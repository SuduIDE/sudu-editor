package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface Channel extends JSObject {
  void sendMessage(JsArray<JSObject> message);

  @JSProperty("onMessage")
  void setOnMessage(JsFunctions.Consumer<JsArray<JSObject>> onMessage);
}
