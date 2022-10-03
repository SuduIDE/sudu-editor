package org.sudu.experiments;

import org.sudu.experiments.demo.DemoEdit;
import org.sudu.experiments.demo.DemoScene1;

public class JvmApp {

  public static void main(String[] args) {
    new DesktopWindow(JvmApp::createScene);
  }

  static Scene createScene(SceneApi api, String name) {
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> new DemoEdit(api);
      case "#test" -> new DemoScene1(api);
//      case "#wasm" -> new WasmDemo(api);
    };
  }
}
