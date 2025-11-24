package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;

public interface JsEditorView extends JsIEditorView {
  void setModel(JsITextModel model);
}
