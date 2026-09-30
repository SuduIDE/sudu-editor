package org.sudu.experiments;

import org.sudu.experiments.diff.FileDiff;
import org.sudu.experiments.diff.FolderDiffScene;
import org.sudu.experiments.diff.UiToolsDemo;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.window.WindowsDemo;
import org.sudu.experiments.swimlane.SwimlaneTest;
import org.sudu.experiments.ui.MergeButtonsTest;

import static org.sudu.experiments.DemoEditJvm.run;

public interface TMain {
  interface TextSeparatorDemoMain {
    static void main(String[] $) {
      run(TextSeparatorDemo::new);
    }
  }

  interface SinDemoMain {
    static void main(String[] $) {
      run(SinDemo::new);
    }
  }

  interface Editor1Main {
    static void main(String[] $) { run(Editor1::new); }
  }

  interface UiToolsTestMain {
    static void main(String[] $) { run(UiToolsDemo::new); }
  }

  interface MergeButtonsTestMain {
    static void main(String[] $) { run(MergeButtonsTest::new); }
  }

  interface FileDiffMain {
    static void main(String[] $) { run(FileDiff::new); }
  }

  interface FolderDiffMain {
    static void main(String[] $) { run(FolderDiffScene::new); }
  }

  interface WindowsDemoMain {
    static void main(String[] $) { run(WindowsDemo::new); }
  }

  interface SelectFileTestMain {
    static void main(String[] $) { run(SelectFileTest::new); }
  }

  interface SwimlaneTestMain {
    static void main(String[] $) { run(SwimlaneTest::new); }
  }
}
