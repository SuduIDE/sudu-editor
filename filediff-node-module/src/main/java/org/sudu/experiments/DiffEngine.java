package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeFileHandle;
import org.teavm.jso.core.JSString;

import static org.sudu.experiments.editor.worker.EditorWorker.array;

public class DiffEngine implements DiffEngineJs {
  final NodeWorkersPool pool;

  DiffEngine(JsArray<NodeWorker> worker) {
    pool = new NodeWorkersPool(worker);
  }

  @Override
  public void dispose() {
    pool.terminateAll();
  }

  @Override
  public Promise<JSString> fib(int n) {
    return Promise.create((postResult, postError) -> {
      pool.sendToWorker(
          result -> {
            int[] intResult = array(result, 0).ints();
            postResult.f(JSString.valueOf(
                "r: " + intResult[0] + ", time: " + intResult[1]
            ));
          }, TestJobs.fibonacci, new int[]{n}
      );
    });
  }

  @Override
  public void startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
    JsHelper.consoleInfo("Starting folder diff ");
    JsHelper.consoleInfo("\t leftPath ", leftPath);
    JsHelper.consoleInfo("\t rightPath ", rightPath);
    channel.setOnMessage(
        m -> JsHelper.consoleInfo("channel onMessage ", m)
    );
  }

  @Override
  public void testFS(JSString path, JsFunctions.Runnable onComplete) {
    Fs fs = Fs.fs();
    JsHelper.consoleInfo("fs = ", fs);
    JsHelper.consoleInfo("O_APPEND = ", fs.constants().O_APPEND());
    var stats = fs.lstatSync(path, Fs.lStatNoThrow());
    JsHelper.consoleInfo("stats = ", stats);
    if (stats.isDirectory()) {
      traverseDir(path);
      JsHelper.consoleInfo("traverseDir done");
    }
  }

  static final class PInt { public int value; }

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
        var fh = new NodeFileHandle(child.stringValue(), new String[0]);
        System.out.println("fd = " + fh);
        int read = 1024 * 64, total = 0;
        PInt pResult = new PInt();
        do {
          fh.readAsBytes(
              bytes -> pResult.value = bytes.length,
              System.err::println, total, read
          );
          if (pResult.value > 0) {
            total += pResult.value;
            read *= 2;
          }
        } while(pResult.value > 0);
        JsHelper.consoleInfo("  read file " + file.stringValue() + " => " + total + " bytes");
      }
    }
  }
}
