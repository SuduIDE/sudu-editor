package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsDocumentHighlight implements JSObject {
  @JSProperty abstract JsRange getRange();

  // see JsDocumentHighlightKind for enum values
  @JSProperty abstract int getKind();
}
