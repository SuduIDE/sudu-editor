package org.sudu.experiments;

import org.sudu.experiments.diff.FolderDiff;
import org.sudu.experiments.editor.Editor1;
import org.sudu.experiments.editor.TextSeparatorDemo;
import org.sudu.experiments.editor.SinDemo;

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
}
