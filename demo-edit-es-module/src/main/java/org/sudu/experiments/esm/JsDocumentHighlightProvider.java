package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsDocumentHighlightProvider extends JSObject {
  JSObject provideDocumentHighlights(
      JsITextModel model,
      JsPosition position,
      JsCancellationToken token
  );
}
