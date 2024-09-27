package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsCodeEditorView extends JsICodeEditorView {
  void setReadonly(boolean flag);
  void setModel(JsITextModel model);
  JsDisposable onDidChangeModel(JsFunctions.Consumer<JsIModelChangedEvent> f);
}
