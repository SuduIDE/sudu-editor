package org.sudu.experiments;

import org.sudu.experiments.demo.DemoEdit;
import org.sudu.experiments.demo.DemoScene1;
import org.sudu.experiments.demo.ManyTexturesLineNumbersScene;
import org.sudu.experiments.demo.SelectFileTest;
import org.sudu.experiments.demo.worker.EditorWorker;
import org.sudu.experiments.demo.worker.WorkerTest;
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
        "DemoEditJvm",
        FontLoader.JetBrainsMono.all()
    );
  }

  static Function<SceneApi, Scene> selectScene(String name) {
    Debug.consoleInfo("createScene: " + name);
    return switch (name) {
      default -> DemoEdit::new;
      case "test" -> DemoScene1::new;
      case "many" -> ManyTexturesLineNumbersScene::new;
      case "SelectFileTest" -> SelectFileTest::new;
      case "WorkerTest" -> WorkerTest::new;
//      case "#wasm" -> new WasmDemo(api);
    };
  }
}
