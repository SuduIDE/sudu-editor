package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteBinaryDiff implements JsRemoteBinaryDiffView {

  public final WebWindow window;
  private BinaryDiffWindow w;
  private JsBinaryDiffViewController controller;

  public JsRemoteBinaryDiff(
      WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = ((RemoteBinaryDiffScene) window.scene()).w;
    controller = new JsBinaryDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {
    Debug.consoleInfo("JsRemoteBinaryDiffDiff0.applyState: ", state);
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {

  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void dispose() {
    window.dispose();
    w = null;
  }

  @Override
  public void focus() {
    window.focus();
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
  public JsBinaryDiffViewController getController() {
    return controller;
  }

  @Override
  public JsDisposable onControllerUpdate(JsFunctions.Consumer<JsViewController> callback) {
    return JsDisposable.empty();
  }

  @Override
  public void setExternalDialogProvider(JsDialogProvider opener) {

  }

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {

  }

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {

  }

  @Override
  public void setNotificationsProvider(JsNotificationsProvider p) {

  }

  @Override
  public void executeMenuAction(JSString action) {

  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteBinaryDiffScene(api, channel);
  }

  public static Promise<JsRemoteBinaryDiff> create(
      EditArgs arguments, Channel channel
  ) {
    return ControlFactory.start(
        arguments,
        sf(channel),
        JsRemoteBinaryDiff::new
    );
  }
}
