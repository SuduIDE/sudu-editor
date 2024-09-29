package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsRemoteEditorView extends JsIEditorView {
  JSObject getState();
  void applyState(JSObject state);

}
