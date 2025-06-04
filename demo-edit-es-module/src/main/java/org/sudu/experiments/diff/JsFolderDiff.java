package org.sudu.experiments.diff;

import org.sudu.experiments.Debug;
import org.sudu.experiments.JsLauncher;

import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

public class JsFolderDiff implements JsIFolderDiffView {

  public final WebWindow window;
  protected FolderDiffWindow w;

  protected JsFolderDiff(WebWindow window, EditArgs args) {
    this.window = window;
    this.w = ((FolderDiffScene) window.scene()).w;
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public final void dispose() {
    window.dispose();
    System.out.println("debug: JsFolderDiff disposed");
    w = null;
  }

  @Override
  public JsViewController getController() {
    return null;
  }

  @Override
  public JsDisposable onControllerUpdate(JsFunctions.Consumer<JsViewController> callback) {
    return JsDisposable.empty();
  }

  @Override
  public boolean isReady() {
    return w.finished;
  }

  @Override
  public JsDisposable onReadyChanged(JsFunctions.Consumer<JSBoolean> callback) {
    var d = w.rootView.stateListeners.disposableAdd(
        JsIFolderDiffView.toJava(callback));
    return JsDisposable.of(d);
  }

  @Override
  public void disconnectFromDom() {
    window.disconnectFromDom();
  }

  @Override
  public void reconnectToDom(JSString containedId) {
    window.connectToDom(containedId);
  }

  @Override
  public void focus() {
    window.focus();
  }


  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    w.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setTheme(JSObject jsTheme) {
    var theme = ThemeImport.fromJs(jsTheme);
    if (theme != null) {
      w.applyTheme(theme);
    } else {
      JsHelper.consoleError("unknown theme: ", jsTheme);
    }
    window.repaint();
  }

  @Override
  public void setExternalDialogProvider(JsDialogProvider opener) {}

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {}

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {}

  @Override
  public void executeMenuAction(JSString action) {}

  public static Promise<JsIFolderDiffView> newDiff(EditArgs arguments) {
    return JsLauncher.start(
        arguments,
        FolderDiffScene::new,
        JsFolderDiff::new);
  }
}
