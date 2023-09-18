package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.menu.FindUsagesDemo;
import org.sudu.experiments.editor.menu.ToolbarDemo;
import org.sudu.experiments.editor.ui.window.WindowDemo;
import org.sudu.experiments.editor.worker.WorkerTest;

import java.util.function.Function;

public class TestSceneSelector {
  public static Function<SceneApi, Scene> selectScene(String name) {
    if (name.length() > 0) {
      Debug.consoleInfo("selectScene " + name);
    }
    return switch (name) {
      default -> Editor1::new;
      case "Diff" -> Diff0::new;
      case "Editor0" -> Editor0::new;
      case "DemoScene1", "test" -> DemoScene1::new;
      case "ToolbarDemo" -> ToolbarDemo::new;
      case "FindUsagesDemo" -> FindUsagesDemo::new;
      case "RegionTextureAllocatorDemo" -> RegionTextureAllocatorDemo::new;
      case "ManyTexturesLineNumbersScene", "many"
          -> ManyTexturesLineNumbersScene::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
      case "RenderTexture" -> RenderTexture::new;
      case "ScissorDemo" -> ScissorDemo::new;
      case "TextureRegionTestScene" -> TextureRegionTestScene::new;
      case "ClipboardTest" -> ClipboardTest::new;
      case "CodiconDemo" -> CodiconDemo::new;
      case "LineShaderDemo1" -> LineShaderDemo1::new;
      case "LineShaderDemo2" -> LineShaderDemo2::new;
      case "WindowDemo" -> WindowDemo::new;

//      case "#wasm" -> new WasmDemo(api);
    };
  }

}