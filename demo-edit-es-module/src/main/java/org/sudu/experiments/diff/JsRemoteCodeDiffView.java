package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsICodeDiffView;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

// interface RemoteCodeDiffView from editor.d.ts
public interface JsRemoteCodeDiffView extends JsICodeDiffView {
    JSObject getState();
    void applyState(JSObject state);
    // getController(): FileDiffViewController;
    JsFileDiffViewController getController();

    // onControllerUpdate: IEvent<FileDiffViewController>
    JsDisposable onControllerUpdate(
        JsFunctions.Consumer<JsFileDiffViewController> callback
    );
}
