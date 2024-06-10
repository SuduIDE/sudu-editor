package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.js.node.NodeFileHandle;
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
      new Traverser(() -> JsHelper.consoleInfo("traverseDir done")).go(dh);
    } else {
      JsHelper.consoleInfo("reading file as string", path);
      NodeFileHandle fh = new NodeFileHandle(path);
      fh.readAsText(s -> {
        JsHelper.consoleInfo("readAsText complete: "+
            s.substring(0, Math.min(20, s.length())));
        onComplete.f();
      }, e -> JsHelper.consoleError("readAsText error: ", JSString.valueOf(e)));
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
      file.getSize(size -> readFileTest(file, size));
    }

    @Override
    public void onComplete() {
      requests--;
      if (requests == 0) {
        onComplete.run();
      }
    }
  }

  static class FileReader {
    byte[] bytes;

    void readFull(FileHandle fh) {
      int read = 1024 * 64, total = 0;
      for (; ; ) {
        fh.readAsBytes(
            result -> bytes = result,
            System.err::println, total, read
        );
        if (bytes != null && bytes.length > 0) {
          int length = bytes.length;
          bytes = null;
          total += length;
          if (length < read)
            break;
          read *= 2;
        } else break;
      }
      JsHelper.consoleInfo("  read file " + fh.getName() + " => " + total + " bytes");
    }
  }

  static void readFileTest(FileHandle fh, int size) {
    if (size < 1024) {
      fh.readAsText(
          text -> JsHelper.consoleInfo("  readAsText " + fh.getName() + " => " + text.length() + " chars"),
          System.err::println
      );
    } else {
      new FileReader().readFull(fh);
    }
  }
}
