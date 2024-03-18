package org.sudu.experiments;

import org.sudu.experiments.editor.TestSceneSelector;
import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.fonts.Codicon;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.nativelib.AngleDll;
import org.sudu.experiments.nativelib.SuduDll;

public class DemoEditJvm {

  public static void main(String[] args) throws InterruptedException {
    AngleDll.require();
    SuduDll.require();

    var selectScene = TestSceneSelector.selectScene(
        args.length > 0 ? args[0] : "default");

    Application.run(
        selectScene, EditorWorker::execute, EditorWorker.numDemoThreads(),
        JetBrainsMono.all(), Codicon.fontResource()
    );
  }

}
