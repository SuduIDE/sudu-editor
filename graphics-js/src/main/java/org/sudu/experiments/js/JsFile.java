package org.sudu.experiments.js;

import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface JsFile extends JsBlob {
  @JSProperty JSString getName();
  @JSProperty JSString getType();
  @JSProperty double getSize();
  @JSProperty JSString getWebkitRelativePath();
}
