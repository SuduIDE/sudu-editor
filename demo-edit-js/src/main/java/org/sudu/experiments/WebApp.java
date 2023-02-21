package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.demo.wasm.WasmDemo;
import org.sudu.experiments.demo.worker.WorkerTest;
import org.sudu.experiments.js.*;
import org.teavm.jso.browser.Window;

public class WebApp {

  public static final String preDiv = "panelDiv";

  boolean fontsLoaded;
  WorkerContext workerStarted;

  public static void main(String[] args) {
    if (JsCanvas.checkFontMetricsAPI()) {
      WebApp webApp = new WebApp();
      WorkerContext.start(webApp::setWorker, "teavm/worker.js");

      WebFont.loadGoogleFont(
          webApp::onFontsLoad,
          WebApp::fontsLoadError,
          WebApp::fontLoadEvent,
          WebFont.makeFontList(Fonts.googleFontLoadScript)
      );
    } else {
      FireFoxWarning.display(preDiv);
    }
  }

  private void onFontsLoad() {
    fontsLoaded = true;
    if (workerStarted != null) startApp(workerStarted);
  }

  private void setWorker(WorkerContext worker) {
    workerStarted = worker;
    if (fontsLoaded) startApp(workerStarted);
  }

  static void startApp(WorkerContext worker) {
    var window = new WebWindow(
        WebApp::createScene,
        WebApp::onWebGlError,
        "canvasDiv", worker);
    window.focus();
  }

  static Scene createScene(SceneApi api) {
    String name = Window.current().getLocation().getHash();
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> new DemoEdit(api);
      case "#test" -> new DemoScene1(api);
      case "#wasm" -> new WasmDemo(api);
      case "#oneTexture" -> new TextureRegionTestScene(api);
      case "#manyTextures" -> new ManyTexturesLineNumbersScene(api);
      case "#SelectFileTest" -> new SelectFileTest(api);
      case "#WorkerTest" -> new WorkerTest(api);
    };
  }

  static void onWebGlError() {
    JsHelper.addPreText(preDiv, "FATAL: WebGL is not enabled in the browser");
  }

  static void fontsLoadError() {
    JsHelper.addPreText(preDiv, "WebFont failed to load google fonts: " + Fonts.googleFontLoadScript);
  }

  static void fontLoadEvent(String familyName, String fvd) {
    Debug.consoleInfo("fontActive: " + familyName + ", fvd=" + fvd);
  }
}
