package org.sudu.experiments.diff;

import org.sudu.experiments.JsLauncher;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsITextModel;
import org.sudu.experiments.esm.JsTextModel;
import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsRemoteCodeDiff0 implements JsRemoteCodeDiff {

  public final WebWindow window;
  private FileDiffWindow w;

  final JsFileDiffViewController0 controller;

  public JsRemoteCodeDiff0(
      WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = ((FileDiff) window.scene()).w;
    controller = new JsFileDiffViewController0();
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly()) setReadonly(args.getReadonly());
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
    if (1<0)
      JsHelper.consoleInfo("setting focus to ", window.canvasDivId());
    window.focus();
  }

  @Override
  public void setReadonly(boolean flag) {
    w.rootView.setReadonly(flag);
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
  public void setTheme(JSString theme) {
    w.setTheme(theme.stringValue());
  }

  @Override
  public void setLeftModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      w.rootView.setLeftModel(jsTextModel.javaModel);
    } else if (JSObjects.isUndefined(model)) {
      throw new IllegalArgumentException("left model is undefined");
    } else {
      throw new IllegalArgumentException("bad left model");
    }
  }

  @Override
  public void setRightModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      w.rootView.setRightModel(jsTextModel.javaModel);
    } else if (JSObjects.isUndefined(model)) {
      throw new IllegalArgumentException("right model is undefined");
    } else {
      throw new IllegalArgumentException("bad right model");
    }
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
      JsFunctions.Consumer<JsFileDiffViewController> callback
  ) {
    return JsDisposable.empty();
  }

  public static Promise<JsCodeDiff> newDiff(EditArgs arguments) {
    return JsLauncher.start(
        arguments,
        FileDiff::new,
        JsRemoteCodeDiff0::new
    );
  }
}
