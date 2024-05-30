package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface NodeWorker extends JsMessagePort0 {
  void on(String msg, JsFunctions.Consumer<JSObject> handler, JSObject options);
  void close();
  void terminate();

  default void onMessage(JsFunctions.Consumer<JSObject> handler) {
    on("message", handler, null);
  }

  default void onMessageOnce(JsFunctions.Consumer<JSObject> handler) {
    on("message", handler, Native.once());
  }

  class Native {
    @JSBody(params = "url", script = "return new Worker(url);")
    public static native NodeWorker newWorker(JSString url);
    @JSBody(script = "return parentPort")
    public static native NodeWorker parentPort();
    @JSBody(script = "return {once: true};")
    public static native JSObject once();
  }

  static void workerMain(WorkerExecutor executor) {
    Native.parentPort().onMessage(e -> {
      JsHelper.consoleInfo("workerMain: message ", e);
      WorkerProtocol.onWorkerMessage(executor, e, Native.parentPort());
    });
    Native.parentPort().postMessage(WorkerProtocol.started());
  }


}
