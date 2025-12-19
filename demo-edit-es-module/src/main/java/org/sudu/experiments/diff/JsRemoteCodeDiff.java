package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteCodeDiff implements JsRemoteFileDiffView {

  public final WebWindow window;
  private FileDiffWindow w;

  final JsFileDiffViewController0 controller;

  public JsRemoteCodeDiff(
      WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = ((RemoteFileDiffScene) window.scene()).w;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());
  }

  @Override
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {
    Debug.consoleInfo("JsRemoteCodeDiff0.applyState: ", state);
  }

  @Override
  public final void dispose() {
    window.dispose();
    w = null;
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
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }

  @Override
  public void setAutoSave(JSString jsAutoSave, JSNumber jsAutoSaveDelay) {
    LoggingJs.info("JsRemoteCodeDiff.setAutoSave: autoSave = " + jsAutoSave.stringValue()
        + ", autoSaveDelay = " + jsAutoSaveDelay.intValue()
    );
    int autoSave = AutoSave.from(jsAutoSave.stringValue());
    int autoSaveDelay = jsAutoSaveDelay.intValue();
    w.setAutoSave(autoSave, autoSaveDelay);
  }

  @Override
  public JsFileDiffViewController getController() {
    return controller;
  }

  @Override
  public JsDisposable onControllerUpdate(
      JsFunctions.Consumer<JsViewController> callback
  ) {
    return JsDisposable.empty();
  }

  @Override
  public void setExternalDialogProvider(JsDialogProvider opener) {}

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {
    remoteFileDiffWindow().messageBar = emb;
  }

  @Override
  public void setNotificationsProvider(JsNotificationsProvider p) {}

  private RemoteFileDiffWindow remoteFileDiffWindow() {
    return (RemoteFileDiffWindow) this.w;
  }

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {
    remoteFileDiffWindow().contextMenuProvider = p;
  }

  @Override
  public void executeMenuAction(JSString action) {
    remoteFileDiffWindow().executeCommand(action);
  }

  static Function<SceneApi, Scene> sf(EditArgs args, Channel channel) {
    return api -> new RemoteFileDiffScene(api, args.getDisableParserOrDefault(), channel);
  }

  public static Promise<JsRemoteFileDiffView> create(
      EditArgs arguments, Channel channel
  ) {
    return ControlFactory.start(
        arguments,
        sf(arguments, channel),
        JsRemoteCodeDiff::new
    );
  }
}
