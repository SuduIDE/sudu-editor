package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsDefinitionProvider extends JSObject {

  //  type ProviderValue<T> = T | undefined | null;
  //  type ProviderResult<T> = ProviderValue<T> | Promise<ProviderValue<T>>;
  // returns ProviderResult<ILocation[]>

  JSObject provideDefinition(JsITextModel model, JsPosition position, JsCancellationToken token);
}
