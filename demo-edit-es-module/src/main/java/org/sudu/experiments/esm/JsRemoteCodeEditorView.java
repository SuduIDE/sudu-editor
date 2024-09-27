package org.sudu.experiments.esm;

import org.sudu.experiments.diff.JsEditorViewController;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

public interface JsRemoteCodeEditorView extends JsICodeEditorView {
  JSObject getState();
  void applyState(JSObject state);

  JsEditorViewController getController();

  // onControllerUpdate: IEvent<FolderDiffViewController | FileDiffViewController>
  JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsEditorViewController> callback
  );

}
