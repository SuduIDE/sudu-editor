package org.sudu.experiments;

import org.sudu.experiments.demo.DemoEdit0;
import org.sudu.experiments.demo.EditorComponent;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class EditorModule implements Editor_d_ts {

  private final EditArguments args;
  private final WebWindow window;
  private final DemoEdit0 demoEdit;

  public EditorModule(EditArguments args, WorkerContext worker) {
    this.args = args;

    this.window = new WebWindow(
        DemoEdit0::new,
        EditorModule::onWebGlError,
        args.getContainerId().stringValue(),
        worker);
    demoEdit = (DemoEdit0) window.scene();
  }

  @Override
  public void dispose() {
    window.dispose();
  }

  @Override
  public void focus() {
    if (1<0) JsHelper.consoleInfo("setting focus to ",
        JsHelper.WithId.get(window.canvasDiv));
    window.focus();
  }

  @Override
  public void setText(JSString t) {
    char[] buffer = TextEncoder.toCharArray(t);
    demoEdit.editor().setText(buffer);
  }

  @Override
  public JSString getText() {
    char[] chars = demoEdit.document().getChars();
    return TextDecoder.fromCharArray(chars);
  }

  @Override
  public JSString saySomething() {
    String line = "Hello from java editor " + this;
    return JSString.valueOf(line);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
    EditorComponent editor = demoEdit.editor();
    editor.changeFont(fontFamily.stringValue(), editor.getFontVirtualSize());
  }

  @Override
  public void setFontSize(int fontSize) {
    EditorComponent editor = demoEdit.editor();
    editor.changeFont(editor.getFontFamily(), fontSize);
  }

  static void onWebGlError() {
    JsHelper.consoleInfo("FATAL: WebGL is not enabled in the browser");
  }

  static Promise<Editor_d_ts> newEdit(EditArguments arguments) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WorkerContext.start(
              worker -> postResult.f(new EditorModule(arguments, worker)),
              postError, workerUrl(arguments)));
    } else {
      return Promise.reject(FireFoxWarning.message);
    }
  }

  static JSString workerUrl(EditArguments arguments) {
    return JSObjects.hasProperty(arguments, EditArguments.workerUrlProperty)
        ? arguments.getWorkerUrl() : JSString.valueOf("worker.js");
  }

  public static void main(String[] args) {
    Editor_d_ts.Setter.setApi(EditorModule::newEdit);
  }
}
