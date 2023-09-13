package org.sudu.experiments;

import org.sudu.experiments.demo.Diff0;
import org.sudu.experiments.esm.JsCodeDiff;
import org.sudu.experiments.esm.JsCodeEditor;
import org.sudu.experiments.esm.JsITextModel;
import org.sudu.experiments.esm.JsTextModel;
import org.sudu.experiments.js.FireFoxWarning;
import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.WorkerContext;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

public class JsCodeDiff0 implements JsCodeDiff {

  private final Diff0 diff;
  private final JsCodeEditor.EditArguments args;
  private final WebWindow window;

  public JsCodeDiff0(JsCodeEditor.EditArguments args, WorkerContext worker) {
    this.args = args;

    this.window = new WebWindow(
        Diff0::new,
        JsCodeEditor0::onWebGlError,
        args.getContainerId().stringValue(),
        worker);
    this.diff = (Diff0) window.scene();
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly()) setReadonly(args.getReadonly());
  }

  @Override
  public void focus() {
    window.focus();
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
    diff.setFontFamily(fontFamily.stringValue());
  }

  @Override
  public void setFontSize(int fontSize) {
    diff.setFontSize(fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    switch (theme.stringValue()) {
      case "light" -> diff.toggleLight();
      case "darcula" -> diff.toggleDarcula();
      case "dark" -> diff.toggleDark();
      default -> Debug.consoleInfo("unknown theme: " + theme);
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

  @Override
  public void setReadonly(JSBoolean flag) {
    diff.setReadonly(flag.booleanValue());
  }

  static Promise<JsCodeDiff> newDiff(JsCodeEditor.EditArguments arguments) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WorkerContext.start(
              worker -> postResult.f(new JsCodeDiff0(arguments, worker)),
              postError, arguments.workerUrl()));
    } else {
      return Promise.reject(FireFoxWarning.message);
    }
  }
}
