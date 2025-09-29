package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteBinaryDiff implements JsRemoteFileDiffView {

  public final WebWindow window;
  private BinaryDiffWindow w;

  public JsRemoteBinaryDiff(
      WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = ((RemoteBinaryDiffScene) window.scene()).w;
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {

  }

  @Override
  public void setTheme(JSObject theme) {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void focus() {

  }

  @Override
  public void disconnectFromDom() {

  }

  @Override
  public void reconnectToDom(JSString containedId) {

  }

  @Override
  public JsFileDiffViewController getController() {
    return null;
  }

  @Override
  public JsDisposable onControllerUpdate(JsFunctions.Consumer<JsViewController> callback) {
    return null;
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

  @Override
  public JsITextModel getLeftModel() {
    return null;
  }

  @Override
  public JsITextModel getRightModel() {
    return null;
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {

  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteBinaryDiffScene(api, channel);
  }

  public static Promise<JsRemoteFileDiffView> create(
      EditArgs arguments, Channel channel
  ) {
    return ControlFactory.start(
        arguments,
        sf(channel),
        JsRemoteCodeDiff::new
    );
  }
}
