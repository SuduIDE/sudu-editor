package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsFolderDiff;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;

interface JsRemoteFolderDiff extends JsFolderDiff {
  JSObject getState();
  void applyState(JSObject state);

  JsFolderDiffViewSelection getSelected();

  JsDisposable onSelectionChanged(
      JsFunctions.Consumer<JsFolderDiffViewSelection> callback
  );
}
