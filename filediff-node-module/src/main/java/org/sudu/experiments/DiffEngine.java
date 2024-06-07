package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

import static org.sudu.experiments.editor.worker.EditorWorker.array;

public class DiffEngine implements DiffEngineJs {
  final NodeWorkersPool pool;

  DiffEngine(JsArray<NodeWorker> worker) {
    pool = new NodeWorkersPool(worker);
  }

  @Override
  public void terminateWorkers() {
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

  void traverseDir(JSString dirName) {
    JsHelper.consoleInfo("dirName: ", dirName);

    Fs fs = Fs.fs();
    JsArray<JSString> content = fs.readdirSync(dirName);
    for (int i = 0; i < content.getLength(); i++) {
      JsHelper.consoleInfo("  [" + i + "]", content.get(i));
    }

    Uint8Array readBuffer = Uint8Array.create(1024 * 64);
    for (int i = 0; i < content.getLength(); i++) {
      JSString file = content.get(i);
      JSString child = Fs.concatPath(dirName, file);
      var stats = fs.lstatSync(child);
      if (stats.isDirectory()) {
        traverseDir(child);
      } else {
        int fd = fs.openSync(child, Fs.fs().constants().O_RDONLY());
        System.out.println("fd = " + fd);
        int read = 0, total = 0;
        do {
          read = fs.readSync(fd,
              readBuffer, 0, readBuffer.getByteLength(), total);
          if (read > 0) total += read;
        } while(read > 0);
        fs.closeSync(fd);
        JsHelper.consoleInfo("  read file " + file.stringValue() + " => " + total + " bytes");
      }
    }
  }
}
