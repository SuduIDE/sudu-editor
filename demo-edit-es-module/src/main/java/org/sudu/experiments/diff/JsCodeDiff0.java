package org.sudu.experiments.diff;

import org.sudu.experiments.Debug;
import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSString;

public class JsCodeDiff0 implements JsCodeDiff {

  public final WebWindow window;
  private Diff0 diff;

  public JsCodeDiff0(
      EditArgs args,
      JsArray<WorkerContext> workers
  ) {
    this.window = new WebWindow(
        Diff0::new, WebGLError::onWebGlError,
        args.getContainerId(), workers);
    this.diff = (Diff0) window.scene();
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly()) setReadonly(args.getReadonly());
  }

  @Override
  public final void dispose() {
    window.dispose();
    diff = null;
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
    diff.setReadonly(flag);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
    diff.setFontFamily(fontFamily.stringValue());
  }

  @Override
  public void setFontSize(float fontSize) {
    diff.setFontSize(fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    switch (theme.stringValue()) {
      case "light" -> diff.toggleLight();
      case "darcula" -> diff.toggleDarcula();
      case "dark" -> diff.toggleDark();
      default -> Debug.consoleInfo("unknown theme: ", theme);
    }
  }

  @Override
  public void setLeftModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      diff.setLeftModel(jsTextModel.javaModel);
    } else {
      throw new IllegalArgumentException("bad model");
    }
  }

  @Override
  public void setRightModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      diff.setRightModel(jsTextModel.javaModel);
    } else {
      throw new IllegalArgumentException("bad model");
    }
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(diff.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(diff.getRightModel());
  }

  public static Promise<JsCodeDiff> newDiff(EditArgs arguments) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WorkerContext.start(
              workers -> postResult.f(new JsCodeDiff0(arguments, workers)),
              postError,
              arguments.workerUrl(),
              arguments.numWorkerThreads()));
    } else {
      return Promise.reject(FireFoxWarning.message);
    }
  }
}
