package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.sudu.experiments.js.WorkerProtocol.PlatformBridge;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

public interface NodeWorker extends JsMessagePort0 {
  void on(String msg, JsFunctions.Consumer<JSObject> handler);
  void once(String msg, JsFunctions.Consumer<JSObject> handler);
  void terminate();

  default void onMessage(JsFunctions.Consumer<JSObject> handler) {
    on("message", handler);
  }

  default void onMessageOnce(JsFunctions.Consumer<JSObject> handler) {
    once("message", handler);
  }

  class Native {
    @JSBody(params = "url", script = "return new Worker(url);")
    public static native NodeWorker newWorker(JSString url);
    @JSBody(script = "return parentPort")
    public static native NodeWorker parentPort();
    @JSBody(script = "return {once: true};")
    public static native JSObject once();
  }

  static void start(
      JsFunctions.Consumer<JsArray<NodeWorker>> onStart,
      JsFunctions.Consumer<JSError> error,
      JSString url, int count,
      PlatformBridge bridge
  ) {
    WorkerProtocol.bridge = bridge;
    JsArray<NodeWorker> workers = JsArray.create();
    for (int i = 0; i < count; i++) {
      NodeWorker worker = Native.newWorker(url);
      worker.onMessageOnce(message -> {
        if (WorkerProtocol.isStarted(message)) {
          workers.push(worker);
          if (workers.getLength() == count)
            onStart.f(workers);
        } else {
          error.f(JsHelper.newError("worker is not started"));
        }
      });
    }
  }

  static void workerMain(WorkerExecutor executor, PlatformBridge bridge) {
    WorkerProtocol.bridge = bridge;
    Native.parentPort().onMessage(e ->
        WorkerProtocol.onWorkerMessage(executor, e, Native.parentPort())
    );
    Native.parentPort().postMessage(WorkerProtocol.started());
  }
}
