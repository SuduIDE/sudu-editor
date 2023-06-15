package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsLanguageFilter implements JSObject {
  @JSProperty abstract String getLanguage();
  @JSProperty abstract String getScheme();
}
