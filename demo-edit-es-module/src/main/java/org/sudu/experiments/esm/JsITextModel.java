package org.sudu.experiments.esm;

import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface JsITextModel extends JsDisposable {
  @JSProperty JsUri getUri();
  @JSProperty JSString getLanguage();
  int getOffsetAt(JsPosition position);
  JsPosition getPositionAt(int offset);
}
