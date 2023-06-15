package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsDocumentHighlightProvider extends JSObject {

  //  type ProviderValue<T> = T | undefined | null;
  //  type ProviderResult<T> = ProviderValue<T> | Promise<ProviderValue<T>>;
  // returns ProviderResult<IDocumentHighlight[]>

  JSObject provideDocumentHighlights(
      JsITextModel model,
      JsPosition position,
      JsCancellationToken token
  );
}
