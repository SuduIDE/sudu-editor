package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsFolderDiff;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

public interface JsRemoteFolderDiff extends JsFolderDiff {
  JSObject getState();
  void applyState(JSObject state);

  // getController(): FolderDiffViewController | FileDiffViewController;
  JsDiffViewController getController();

  // onControllerUpdate: IEvent<FolderDiffViewController | FileDiffViewController>
  JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsDiffViewController> callback
  );
}
