package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
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
  public void setFontFamily(JSString fontFamily) {
    w.rootView.setFontFamily(fontFamily.stringValue());
  }

  @Override
  public void setFontSize(float fontSize) {
    w.rootView.setFontSize(fontSize);
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
    ((RemoteFileDiffWindow) this.w).messageBar = emb;
  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteFileDiffScene(api, channel);
  }

  public static Promise<JsRemoteFileDiffView> create(
      EditArgs arguments, Channel channel
  ) {
    return JsLauncher.start(
        arguments,
        sf(channel),
        JsRemoteCodeDiff::new
    );
  }
}
