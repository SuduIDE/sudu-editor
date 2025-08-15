package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteFolderDiff implements JsRemoteFolderDiffView {

  public final WebWindow window;
  protected RemoteFolderDiffWindow folderDiff;

  protected JsRemoteFolderDiff(WebWindow window, EditArgs args) {
    this.window = window;
    var scene = (RemoteFolderDiffScene) window.scene();
    this.folderDiff = scene.w;
    this.folderDiff.setDisableParser(args.getDisableParserOrDefault());
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public final void dispose() {
    window.dispose();
    System.out.println("debug: JsFolderDiff disposed");
    folderDiff = null;
  }

  @Override
  public void setExternalFileOpener(JsExternalFileOpener opener) {
    folderDiff.opener = opener;
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
    folderDiff.setReadonly(leftReadonly, rightReadonly);
  }


  @Override
  public void setTheme(JSObject jsTheme) {
    var theme = ThemeImport.fromJs(jsTheme);
    if (theme != null) {
      folderDiff.applyTheme(theme);
    } else {
      JsHelper.consoleError("unknown theme: ", jsTheme);
    }
    window.repaint();
  }

  @Override
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {
    Debug.consoleInfo("JsRemoteFolderDiff.applyState: ", state);
  }

  @Override
  public boolean isReady() {
    return folderDiff.finished;
  }

  @Override
  public JsDisposable onReadyChanged(JsFunctions.Consumer<JSBoolean> callback) {
    var d = rootView().stateListeners.disposableAdd(
        JsIFolderDiffView.toJava(callback));
    return JsDisposable.of(d);
  }

  @Override
  public JsViewController getController() {
    return folderDiff.controller;
  }

  @Override
  public JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsViewController> callback
  ) {
    var d = folderDiff.controllerListeners.disposableAdd(
        JsViewController.toJava(callback)
    );
    return JsDisposable.of(d);
  }

  @Override
  public void setExternalDialogProvider(JsDialogProvider provider) {
    folderDiff.dialogProvider = provider;
  }

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {
    folderDiff.messageBar = emb;
  }

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {}

  @Override
  public void executeMenuAction(JSString action) {}

  private FolderDiffRootView rootView() {
    return folderDiff.rootView;
  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteFolderDiffScene(api, channel);
  }

  public static Promise<JsRemoteFolderDiffView> newDiff(
      EditArgs arguments, Channel channel
  ) {
    return ControlFactory.start(arguments,
        sf(channel), JsRemoteFolderDiff::new);
  }
}
