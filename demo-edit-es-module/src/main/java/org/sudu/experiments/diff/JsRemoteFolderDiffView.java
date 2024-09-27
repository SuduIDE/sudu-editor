package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsExternalFileOpener;
import org.sudu.experiments.esm.JsIFolderDiffView;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

public interface JsRemoteFolderDiffView extends JsIFolderDiffView {
  JSObject getState();
  void applyState(JSObject state);

  // getController(): FolderDiffViewController | FileDiffViewController;
  JsViewController getController();

  // onControllerUpdate: IEvent<FolderDiffViewController | FileDiffViewController>
  JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsViewController> callback
  );

  //  setExternalFileOpener(opener: ExternalFileOpener): void
  void setExternalFileOpener(JsExternalFileOpener opener);
}
