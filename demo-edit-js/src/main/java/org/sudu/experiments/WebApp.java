package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.demo.wasm.WasmDemo;
import org.sudu.experiments.js.FireFoxWarning;
import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.WebFont;

public class WebApp {

  public static final String preDiv = "panelDiv";

  public static void main(String[] args) {
//    JsPlayground.jsoTagTest();
//    JsPlayground.jsTestViewOfJavaArray();
    if (JsCanvas.checkFontMetricsAPI()) {
      WebFont.loadGoogleFont(
          WebApp::startApp,
          WebApp::fontsLoadError,
          WebApp::fontLoadEvent,
          WebFont.makeFontList(Fonts.googleFontLoadScript)
      );
    } else {
      FireFoxWarning.display(preDiv);
    }
  }

  static void startApp() {
    new WebWindow(WebApp::createScene, WebApp::onWebGlError, "canvasDiv");
  }

  static Scene createScene(SceneApi api, String name) {
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> new DemoEdit(api);
      case "#test" -> new DemoScene1(api);
      case "#wasm" -> new WasmDemo(api);
      case "#oneTexture" -> new TextureRegionTestScene(api);
      case "#manyTextures" -> new ManyTexturesLineNumerationScene(api);
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
