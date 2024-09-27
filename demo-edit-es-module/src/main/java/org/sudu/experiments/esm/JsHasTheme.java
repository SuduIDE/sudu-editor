package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsHasTheme extends JSObject  {
  void setFontFamily(JSString fontFamily);
  void setFontSize(float fontSize);
  void setTheme(JSObject theme);
}
