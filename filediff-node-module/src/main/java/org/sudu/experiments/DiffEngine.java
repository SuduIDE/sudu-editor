package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
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
//    NodeFileHandle fh = new NodeFileHandle(leftPath);
//    NodeFileHandle fh = new NodeFileHandle(rightPath);

    JsHelper.consoleInfo("Starting folder diff ");
    JsHelper.consoleInfo("\t leftPath ", leftPath);
    JsHelper.consoleInfo("\t rightPath ", rightPath);
    channel.setOnMessage(
        m -> JsHelper.consoleInfo("channel onMessage ", m)
    );
  }

  @Override
  public void testFS(JSString path, JsFunctions.Runnable onComplete) {
    FsTest.fsTest(path, onComplete);
  }

}
