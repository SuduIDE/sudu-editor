package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsReferenceProvider extends JSObject {
  JSObject provideReferences(
      JsITextModel model,
      JsPosition position,
      JSObject context,
      JsCancellationToken token
  );
}
