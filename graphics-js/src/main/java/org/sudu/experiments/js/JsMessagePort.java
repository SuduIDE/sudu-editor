package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsMessagePort extends JsMessagePort0 {
  void on(String msg, JsFunctions.Consumer<JSObject> handler);
  void close();

  default void onMessage(JsFunctions.Consumer<JSObject> handler) {
    on("message", handler);
  }

  class Native {
    @JSBody(params = "url", script = "return new Worker(url);")
    public static native JsMessagePort newWorker(JSString url);
    @JSBody(script = "return parentPort")
    public static native JsMessagePort parentPort();
  }

  static void workerMain(WorkerExecutor executor) {
    Native.parentPort().on("message", e -> {
      JsHelper.consoleInfo("workerMain: message ", e);
      WorkerProtocol.onWorkerMessage(executor, e, Native.parentPort());
    });
    Native.parentPort().postMessage(WorkerProtocol.started());
  }


}
