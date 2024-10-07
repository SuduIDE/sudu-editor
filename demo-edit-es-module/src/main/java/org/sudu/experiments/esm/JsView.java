package org.sudu.experiments.esm;

import org.sudu.experiments.diff.JsViewController;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.core.JSString;

// View Focusable Disposable HasTheme
public interface JsView extends JsHasTheme {
  void dispose();
  void focus();
  void disconnectFromDom();
  void reconnectToDom(JSString containedId);

  // getController(): FolderDiffViewController | FileDiffViewController;
  JsViewController getController();

  // onControllerUpdate: IEvent<FolderDiffViewController | FileDiffViewController>
  JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsViewController> callback
  );

  void setExternalDialogProvider(JsExternalDialogProvider opener);
}
