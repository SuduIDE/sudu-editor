package org.sudu.experiments;

import org.sudu.experiments.diff.FileDiff;
import org.sudu.experiments.diff.FolderDiff;
import org.sudu.experiments.editor.Editor1;
import org.sudu.experiments.editor.SelectFileTest;
import org.sudu.experiments.editor.TextSeparatorDemo;
import org.sudu.experiments.editor.SinDemo;
import org.sudu.experiments.editor.ui.window.WindowsDemo;
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

  interface FolderDiffMain {
    static void main(String[] $) { run(FolderDiff::new); }
  }

  interface MergeButtonsTestMain {
    static void main(String[] $) { run(MergeButtonsTest::new); }
  }

  interface FileDiffMain {
    static void main(String[] $) { run(FileDiff::new); }
  }

  interface WindowsDemoMain {
    static void main(String[] $) { run(WindowsDemo::new); }
  }

  interface SelectFileTestMain {
    static void main(String[] $) { run(SelectFileTest::new); }
  }
}
