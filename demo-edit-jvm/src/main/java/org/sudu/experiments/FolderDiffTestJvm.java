package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;

import java.nio.file.Path;

class FolderDiffTestJvm extends BaseDiffTest {

  CollectorFolderDiffTest test;

  FolderDiffTestJvm(
      Path left, Path right,
      boolean content
  ) {
    var leftH = dir(left);
    var rightH = dir(right);

    test = new CollectorFolderDiffTest(leftH, rightH,
        content, this, time, this::onComplete);
    test.scan();
  }

  @Override
  protected boolean running() {
    return test.running();
  }

  public static void main(String[] args) throws InterruptedException {
    run(args, FolderDiffTestJvm::new, FolderDiffTestJvm.class);
  }
}
