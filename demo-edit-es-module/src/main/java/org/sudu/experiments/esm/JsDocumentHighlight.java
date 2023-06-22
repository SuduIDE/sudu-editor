package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

public abstract class JsDocumentHighlight implements JSObject {
  @JSProperty public abstract JsRange getRange();

  @JSProperty public abstract int getKind();

  public final boolean hasKind() {
    return JSObjects.hasProperty(this, "kind");
  }
}
