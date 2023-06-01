package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsDefinitionProvider extends JSObject {
  JSObject provideDefinition(JsITextModel model, JsPosition position, JsCancellationToken token);
}
