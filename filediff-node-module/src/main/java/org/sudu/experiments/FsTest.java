package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.teavm.jso.core.JSString;

public class FsTest {

  static void fsTest(JSString path, JsFunctions.Runnable onComplete) {
    Fs fs = Fs.fs();
    JsHelper.consoleInfo("fs = ", fs);
    JsHelper.consoleInfo("O_APPEND = ", fs.constants().O_APPEND());
    var stats = fs.lstatSync(path, Fs.lStatNoThrow());
    JsHelper.consoleInfo("stats = ", stats);
    if (stats.isDirectory()) {
      NodeDirectoryHandle dh = new NodeDirectoryHandle(path);
      new Traverser(() -> {
        JsHelper.consoleInfo("traverseDir done");
        onComplete.f();
      }).go(dh);
    }
  }

  static class Traverser implements DirectoryHandle.Reader {
    final Runnable onComplete;
    int requests;

    Traverser(Runnable onComplete) {
      this.onComplete = onComplete;
    }

    void go(DirectoryHandle h) {
      onDirectory(h);
    }

    @Override
    public void onDirectory(DirectoryHandle dir) {
      requests++;
      dir.read(this);
    }

    @Override
    public void onFile(FileHandle file) {
      file.getSize(size -> {
        JsHelper.consoleInfo("file " + file + " size = " + size);
      });
    }

    @Override
    public void onComplete() {
      requests--;
      if (requests == 0) {
        onComplete.run();
      }
    }
  }

}
