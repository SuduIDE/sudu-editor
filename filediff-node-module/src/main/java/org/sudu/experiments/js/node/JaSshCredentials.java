package org.sudu.experiments.js.node;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface JaSshCredentials extends JSObject {
  @JSProperty
  JSString getHost();

  @JSProperty
  JSString getPort();

  @JSProperty
  JSString getUsername();

  @JSProperty
  JSString getPassword();

  @JSProperty
  JSString getPrivateKey();
}
