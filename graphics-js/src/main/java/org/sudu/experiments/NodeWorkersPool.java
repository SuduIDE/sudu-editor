package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.NodeWorker;
import org.sudu.experiments.js.WorkerProtocol;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class NodeWorkersPool extends WorkersPool {
  public NodeWorkersPool(JsArray<NodeWorker> workers) {
    super(workers);
  }

  @Override
  protected void setMessageHandler(int index) {
    workers.get(index).<NodeWorker>cast().onMessage(
        msg -> onWorkerMessage(msg, index)
    );
  }

  public void terminateAll() {
    for (int i = 0; i < workers.getLength(); ++i) {
      workers.get(i).<NodeWorker>cast().terminate();
      workers.set(i, null);
    }
  }

  public static void start(
      JsFunctions.Consumer<JsArray<NodeWorker>> onStart,
      JsFunctions.Consumer<JSObject> error,
      JSString url, int count
  ) {
    JsArray<NodeWorker> workers = JsArray.create();
    for (int i = 0; i < count; i++) {
//      JsHelper.consoleInfo("NodeWorker.Native.newWorker(url) ", i);

      NodeWorker worker = NodeWorker.Native.newWorker(url);
      JsFunctions.Consumer<JSObject> handler = message -> {
        if (WorkerProtocol.isStarted(message)) {
          workers.push(worker);
          if (workers.getLength() == count)
            onStart.f(workers);
        } else {
          error.f(JsHelper.newError("worker is not started"));
        }
      };
      worker.onMessageOnce(handler);
    }
  }
}
