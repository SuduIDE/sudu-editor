package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsIModelChangedEvent implements JSObject {
  @JSProperty public abstract JsUri getOldModelUrl();
  @JSProperty public abstract JsUri getNewModelUrl();

  @JSBody(params = {"oldUrl", "newUrl"}, script =
      "return {oldModelUrl: oldUrl, newModelUrl: newUrl};"
  )
  public static native JsIModelChangedEvent create(JsUri oldUrl, JsUri newUrl);
}
