package org.sudu.experiments;

import org.sudu.experiments.editor.TestSceneSelector;
import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.fonts.Codicon;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.nativelib.AngleDll;

import java.util.function.Function;

public class DemoEditJvm {

  public static void main(String[] args) {
    var selectScene = TestSceneSelector.selectScene(
        args.length > 0 ? args[0] : "");
    run(selectScene);
  }

  static void run(Function<SceneApi, Scene> f) {
    AngleDll.require();

    Application.run(f, EditorWorker::execute, EditorWorker.numDemoThreads(),
        JetBrainsMono.all(), Codicon.fontResource()
    );
  }
}
