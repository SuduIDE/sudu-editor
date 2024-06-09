package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.node.Fs;
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
      FsTest r = new FsTest();
      r.traverseDir(path);
      JsHelper.consoleInfo("traverseDir done");
      onComplete.f();
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

  void traverseDir(JSString dirName) {
    JsHelper.consoleInfo("dirName: ", dirName);

    Fs fs = Fs.fs();
    JsArray<JSString> content = fs.readdirSync(dirName);
    for (int i = 0; i < content.getLength(); i++) {
      JsHelper.consoleInfo("  [" + i + "]", content.get(i));
    }

    for (int i = 0; i < content.getLength(); i++) {
      JSString file = content.get(i);
      JSString child = Fs.concatPath(dirName, file);
      var stats = fs.lstatSync(child);
      if (stats.isDirectory()) {
        traverseDir(child);
      } else {
        var fh = new NodeFileHandle(child);
        System.out.println("fd = " + fh);
        fh.getSize(size -> readFileTest(fh, size));
      }
    }
  }

  static class FileReader {
    byte[] bytes;

    void readFull(NodeFileHandle fh) {
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

  private void readFileTest(NodeFileHandle fh, int size) {
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
