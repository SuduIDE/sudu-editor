package org.sudu.experiments;

import org.sudu.experiments.diff.tests.FolderDiffTest;

import java.nio.file.Files;
import java.nio.file.Path;

class FolderDiffTestJvm extends BaseDiffTest {

  FolderDiffTest test;

  FolderDiffTestJvm(
      Path left, Path right,
      boolean content) {
    var leftH = dir(left);
    var rightH = dir(right);

    test = new FolderDiffTest(leftH, rightH,
        content, this, time, this::onComplete);
    test.scan();
  }

  private void run() throws InterruptedException {
    while (test.running()) {
      edt.execute();
      Thread.sleep(1);
    }
    workers.shutdown();
  }

  public static void main(String[] args) throws InterruptedException {
    if (args.length >= 2 && args.length <= 4) {
      Path p1 = Path.of(args[0]);
      Path p2 = Path.of(args[1]);
      boolean d1 = Files.isDirectory(p1);
      boolean d2 = Files.isDirectory(p2);
      boolean content = args.length >= 3 && args[2].equals("content");
      if (d1 && d2) {
        System.out.println("  path1 = " + p1);
        System.out.println("  path2 = " + p2);
        System.out.println("  content = " + content);
        new FolderDiffTestJvm(p1, p2, content).run();
      } else {
        System.err.println(
            "path is not a directory: " + (d1 ? p2 : p1));
      }
    } else {
      System.out.println("Usage: FolderDiffTest <path1> <path2> [content]");
    }
  }
}
