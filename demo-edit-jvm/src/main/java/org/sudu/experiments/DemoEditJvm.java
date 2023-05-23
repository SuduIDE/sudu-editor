package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.demo.worker.EditorWorker;
import org.sudu.experiments.demo.worker.WorkerTest;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.nativelib.AngleDll;
import org.sudu.experiments.nativelib.SuduDll;

import java.util.function.Function;

public class DemoEditJvm {

  public static void main(String[] args) throws InterruptedException {
    AngleDll.require();
    SuduDll.require();

    var selectScene = selectScene(args.length > 0 ? args[0] : "default");
    Application.run(
        selectScene, EditorWorker::execute,
        "DemoEditJvm", JetBrainsMono.all()
    );
  }

  static Function<SceneApi, Scene> selectScene(String name) {
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> DemoEditWithToolbar::new;
      case "DemoScene1" -> DemoScene1::new;
      case "ManyTexturesLineNumbersScene" -> ManyTexturesLineNumbersScene::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
      case "RenderTexture" -> RenderTexture::new;
//      case "#wasm" -> new WasmDemo(api);
    };
  }
}
