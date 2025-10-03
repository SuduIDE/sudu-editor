package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsIBinaryDiffView;
import org.teavm.jso.JSObject;

// interface RemoteCodeDiffView from editor.d.ts
public interface JsRemoteBinaryDiffView extends JsIBinaryDiffView {
  JSObject getState();
  void applyState(JSObject state);
}
