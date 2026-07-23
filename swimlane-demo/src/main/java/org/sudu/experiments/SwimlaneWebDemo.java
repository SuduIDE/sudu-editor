package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.swimlane.SwimlaneTest;

public class SwimlaneWebDemo {
  static void startApp(JsArray<WebWorkerContext> workers) {
    var window = new WebWindow(
        SwimlaneWebDemo::createScene,
        SwimlaneWebDemo::onWebGlError,
        "canvasDiv", workers);
    window.focus();
  }

  public static final String preDiv = "panelDiv";

  static void onWebGlError() {
    JsHelper.addPreText(preDiv, "FATAL: WebGL is not enabled in the browser");
  }

  static Scene createScene(SceneApi api) {
    return new SwimlaneTest(api);
  }

  public static void main(String[] args) {
    startApp(JsArray.create());
  }
}
