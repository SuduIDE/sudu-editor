package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

interface EditApi {
  void setText(String t);

  String getText();

  Disposable addListener(Listener listener);

  interface Listener {
    void somethingHappened();
  }

}

public class EditorModule implements Editor_d_ts {

  private final EditArguments args;
  private final WebWindow window;
  private final DemoEdit demoEdit;

  public EditorModule(EditArguments args, WorkerContext worker) {
    this.args = args;

    this.window = new WebWindow(
        DemoEdit::new,
        EditorModule::onWebGlError,
        args.getContainerId().stringValue(),
        worker);
    demoEdit = (DemoEdit) window.scene();
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
    EditorComponent editor = demoEdit.editor();
    Document document = demoEdit.document();
    JsHelper.consoleInfo("setText: ", JSString.valueOf(t.stringValue()));
  }

  @Override
  public JSString saySomething() {
    String line = "Hello from java editor " + this;
    return JSString.valueOf(line);
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
        ? arguments.getWorkerUrl() : JSString.valueOf("worker1.js");
  }

  public static void main(String[] args) {
    Editor_d_ts.Setter.setApi(EditorModule::newEdit);
  }
}
