package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.demo.menu.FindUsagesDemo;
import org.sudu.experiments.demo.menu.ToolbarDemo;
import org.sudu.experiments.demo.worker.WorkerTest;

import java.util.function.Function;

public class TestSceneSelector {
  public static Function<SceneApi, Scene> selectScene(String name) {
    if (name.length() > 0) {
      Debug.consoleInfo("selectScene " + name);
    }
    return switch (name) {
      default -> DemoEdit1::new;
      case "DemoEdit0" -> DemoEdit0::new;
      case "DemoScene1", "test" -> DemoScene1::new;
      case "ToolbarDemo" -> ToolbarDemo::new;
      case "FindUsagesDemo" -> FindUsagesDemo::new;
      case "ManyTexturesLineNumbersScene", "many"
          -> ManyTexturesLineNumbersScene::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
      case "RenderTexture" -> RenderTexture::new;
      case "ScissorDemo" -> ScissorDemo::new;
      case "TextureRegionTestScene" -> TextureRegionTestScene::new;
      case "ClipboardTest" -> ClipboardTest::new;
      case "CodiconDemo" -> CodiconDemo::new;

//      case "#wasm" -> new WasmDemo(api);
    };
  }

}
