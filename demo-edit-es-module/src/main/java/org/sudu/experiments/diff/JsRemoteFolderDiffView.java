package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsExternalFileOpener;
import org.sudu.experiments.esm.JsIFolderDiffView;
import org.teavm.jso.JSObject;

public interface JsRemoteFolderDiffView extends JsIFolderDiffView {
  JSObject getState();
  void applyState(JSObject state);

  //  setExternalFileOpener(opener: ExternalFileOpener): void
  void setExternalFileOpener(JsExternalFileOpener opener);
}
