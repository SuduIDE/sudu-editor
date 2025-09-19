package org.sudu.experiments.esm;

import org.sudu.experiments.diff.JsViewController;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// View Focusable Disposable HasTheme
public interface JsView extends JSObject {
  void setTheme(JSObject theme);
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

  void setExternalMessageBar(JsExternalMessageBar emb);
  void setExternalContextMenuProvider(JsContextMenuProvider p);
  void setJsNotificationsProvider(JsNotificationsProvider p);
  void executeMenuAction(JSString action);
}
