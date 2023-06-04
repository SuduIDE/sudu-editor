package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.demo.worker.WorkerTest;

import java.util.function.Function;

public class TestSceneSelector {
  public static Function<SceneApi, Scene> selectScene(String name) {
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> DemoEdit0::new;
      case "DemoScene1", "test" -> DemoScene1::new;
      case "ManyTexturesLineNumbersScene", "many"
          -> ManyTexturesLineNumbersScene::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
      case "RenderTexture" -> RenderTexture::new;
      case "TextureRegionTestScene" -> TextureRegionTestScene::new;
      case "ClipboardTest" -> ClipboardTest::new;

//      case "#wasm" -> new WasmDemo(api);
    };
  }

}
