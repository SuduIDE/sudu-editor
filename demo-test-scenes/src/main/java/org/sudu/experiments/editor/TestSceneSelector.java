package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.diff.*;
import org.sudu.experiments.ui.MergeButtonsTest;
import org.sudu.experiments.ui.FileViewDemo;
import org.sudu.experiments.editor.menu.FindUsagesDemo;
import org.sudu.experiments.editor.menu.ToolbarDemo;
import org.sudu.experiments.editor.ui.window.*;
import org.sudu.experiments.editor.worker.WorkerTest;
import org.sudu.experiments.ui.WindowDemo;

import java.util.function.Function;

public class TestSceneSelector {
  static Function<SceneApi, Scene> defaultScene() {
    return FolderDiff::new;
  }

  public static Function<SceneApi, Scene> selectScene(String name) {
    if (name.length() > 0) {
      Debug.consoleInfo("selectScene " + name);
    }
    return switch (name) {
      default -> defaultScene();
      case "Diff0", "Diff" -> Diff0::new;
      case "Diff1" -> Diff1::new;
      case "Editor0" -> Editor0::new;
      case "Editor1" -> Editor1::new;
      case "DemoScene1", "test" -> DemoScene1::new;
      case "CleartypeColors" -> CleartypeColors::new;
      case "ToolbarDemo" -> ToolbarDemo::new;
      case "FindUsagesDemo" -> FindUsagesDemo::new;
      case "RegionTextureAllocatorDemo" -> RegionTextureAllocatorDemo::new;
      case "LineNumbersTest" -> LineNumbersTest::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
      case "RenderTexture" -> RenderTexture::new;
      case "ScissorDemo" -> ScissorDemo::new;
      case "TextureRegionTestScene" -> TextureRegionTestScene::new;
      case "ClipboardTest" -> ClipboardTest::new;
      case "CodiconDemo" -> CodiconDemo::new;
      case "LineShaderDemo1" -> LineShaderDemo1::new;
      case "LineShaderDemo2" -> LineShaderDemo2::new;
      case "WindowsDemo" -> WindowsDemo::new;
      case "WindowDemo" -> WindowDemo::new;
      case "MergeButtonsTest" -> MergeButtonsTest::new;
      case "VScrollTest" -> VScrollTest::new;
      case "EditorInViewDemo" -> EditorInViewDemo::new;
      case "ProjectViewDemo" -> ProjectViewDemo::new;
      case "EditorWindowDemo" -> EditorWindowDemo::new;
      case "FileViewDemo" -> FileViewDemo::new;
      case "SinDemo" -> SinDemo::new;
      case "DiffMiddleDemo" -> DiffMiddleDemo::new;
      case "FolderTransferDemo" -> FolderTransferDemo::new;
      case "FolderDiff" -> FolderDiff::new;
      case "FolderDiffScene" -> FolderDiffScene::new;

//      case "#wasm" -> new WasmDemo(api);
    };
  }

}
