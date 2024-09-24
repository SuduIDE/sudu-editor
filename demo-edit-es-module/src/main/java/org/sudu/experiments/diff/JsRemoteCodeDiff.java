package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;

// interface RemoteCodeDiffView from editor.d.ts
public interface JsRemoteCodeDiff extends JsCodeDiff {
    // getController(): FileDiffViewController;
    JsFileDiffViewController getController();

    // onControllerUpdate: IEvent<FileDiffViewController>
    JsDisposable onControllerUpdate(
        JsFunctions.Consumer<JsFileDiffViewController> callback
    );
}
