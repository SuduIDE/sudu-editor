package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public abstract class JsLanguageFilter implements JSObject {

  @JSProperty abstract JSString getLanguage();
  @JSProperty abstract JSString getScheme();

  public String getLanguageOrNull() {
    return JsHelper.toString(getLanguage(), null);
  }

  public String getSchemeOrNull() {
    return JsHelper.toString(getScheme(), null);
  }
}
