package org.sudu.experiments.diff;

import org.sudu.experiments.JsLauncher;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeDiff0 implements JsCodeDiff {

  public final WebWindow window;
  private FileDiffWindow w;

  public JsCodeDiff0(
      WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = ((FileDiffScene) window.scene()).w;
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

  public static Promise<JsCodeDiff> newDiff(EditArgs arguments) {
    return JsLauncher.start(
        arguments,
        FileDiffScene::new,
        JsCodeDiff0::new
    );
  }
}
