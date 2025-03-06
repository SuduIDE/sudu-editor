package org.sudu.experiments;

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
}
