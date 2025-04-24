package org.sudu.experiments;

import org.sudu.experiments.demo.wasm.WasmDemo;
import org.sudu.experiments.editor.DiffDemoJs;
import org.sudu.experiments.editor.TestSceneSelector;
import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.fonts.Codicon;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.js.*;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;

public class WebApp {

  public static final String preDiv = "panelDiv";

  boolean fontsLoaded;
  JsArray<WebWorkerContext> workers;

  public static void main(String[] args) {
    if (JsCanvas.checkFontMetricsAPI()) {
      WebApp webApp = new WebApp();
      WebWorkerContext.start(webApp::setWorkers, "teavm/worker.js",
          EditorWorker.numDemoThreads());
      FontFace.loadFonts(ArrayOp.add(JetBrainsMono.webConfig(), Codicon.webConfig()))
          .then(webApp::loadFonts, WebApp::fontLoadError);
    } else {
      FireFoxWarning.display(preDiv);
    }
  }

  private void loadFonts(JsArrayReader<JSObject> fontFaces) {
    FontFace.addToDocument(fontFaces);
    fontsLoaded = true;
    if (workers != null) startApp(workers);
  }

  private void setWorkers(JsArray<WebWorkerContext> workers) {
    this.workers = workers;
    if (fontsLoaded) startApp(workers);
  }

  static void fontLoadError(JSError error) {
    JsHelper.consoleInfo("font load error ", error);
  }

  static void startApp(JsArray<WebWorkerContext> workers) {
    var window = new WebWindow(
        WebApp::createScene,
        WebApp::onWebGlError,
        "canvasDiv", workers);
    window.focus();
  }

  static Scene createScene(SceneApi api) {
    String hash = JsWindow.current().getLocation().getHash();
    if ("#wasmDemo".equals(hash)) return new WasmDemo(api);
    if ("#diffDemo".equals(hash) || hash.isEmpty())
      return new DiffDemoJs(api);
    String name = hash.isEmpty() ? "" : hash.substring(1);
    return TestSceneSelector.selectScene(name).apply(api);
  }

  static void onWebGlError() {
    JsHelper.addPreText(preDiv, "FATAL: WebGL is not enabled in the browser");
  }
}
