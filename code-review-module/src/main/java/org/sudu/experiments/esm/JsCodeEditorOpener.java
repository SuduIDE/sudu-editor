package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsCodeEditorOpener extends JSObject {
  JSObject openCodeEditor(JsIEditorView source, JsUri resource, JSObject selectionOrPosition);
}
