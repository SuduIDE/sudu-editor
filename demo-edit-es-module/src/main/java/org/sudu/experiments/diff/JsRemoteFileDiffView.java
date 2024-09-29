package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsIFileDiffView;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

// interface RemoteCodeDiffView from editor.d.ts
public interface JsRemoteFileDiffView extends JsIFileDiffView {
    JSObject getState();
    void applyState(JSObject state);
    // getController(): FileDiffViewController;
    JsFileDiffViewController getController();
}
