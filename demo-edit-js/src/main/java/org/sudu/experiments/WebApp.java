package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.demo.wasm.WasmDemo;
import org.sudu.experiments.fonts.Codicon;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.js.*;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSError;

public class WebApp {

  public static final String preDiv = "panelDiv";

  boolean fontsLoaded;
  WorkerContext workerStarted;

  public static void main(String[] args) {
    if (JsCanvas.checkFontMetricsAPI()) {
      WebApp webApp = new WebApp();
      WorkerContext.start(webApp::setWorker, "teavm/worker.js");
      FontFace.loadFonts(ArrayOp.add(JetBrainsMono.webConfig(), Codicon.webConfig()))
          .then(webApp::loadFonts, WebApp::fontLoadError);
    } else {
      FireFoxWarning.display(preDiv);
    }
  }

  private void loadFonts(JsArrayReader<JSObject> fontFaces) {
    FontFace.addToDocument(fontFaces);
    fontsLoaded = true;
    if (workerStarted != null) startApp(workerStarted);
  }

  private void setWorker(WorkerContext worker) {
    workerStarted = worker;
    if (fontsLoaded) startApp(workerStarted);
  }

  static void fontLoadError(JSError error) {
    JsHelper.consoleInfo("font load error ", error);
  }

  static void startApp(WorkerContext worker) {
    var window = new WebWindow(
        WebApp::createScene,
        WebApp::onWebGlError,
        "canvasDiv", worker);
    window.focus();
  }

  static Scene createScene(SceneApi api) {
    String hash = Window.current().getLocation().getHash();
    if ("#wasm".equals(hash)) return new WasmDemo(api);
    if ("#diffDemo".equals(hash)) return new DiffDemoJs(api);
    String name = hash.length() > 0 ? hash.substring(1) : "";
    return TestSceneSelector.selectScene(name).apply(api);
  }

  static void onWebGlError() {
    JsHelper.addPreText(preDiv, "FATAL: WebGL is not enabled in the browser");
  }
}
