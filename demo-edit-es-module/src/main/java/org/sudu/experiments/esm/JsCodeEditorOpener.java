package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;

public interface JsCodeEditorOpener extends JSObject {
  JSObject openCodeEditor(JsCodeEditor source, JsUri resource, JSObject selectionOrPosition);
}
