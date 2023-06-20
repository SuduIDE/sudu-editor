package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.MessageEvent;

import java.util.Map;
import java.util.function.Consumer;

public abstract class WorkerContext implements JSObject {

  @JSBody(params = "url", script = "return new Worker(url);")
  public static native WorkerContext newWorker(String url);

  @JSBody(params = "url", script = "return new Worker(url);")
  public static native WorkerContext newWorker(JSString url);

  @JSBody(script = "return self")
  public static native WorkerContext self();

  @JSProperty("onmessage")
  public abstract void onMessage(JsFunctions.Consumer<MessageEvent> f);

  @JSProperty("onmessage")
  public abstract void onMessage(JSObject jsObject);

  public abstract void postMessage(JSObject message);
  public abstract void postMessage(JSObject message, JSArrayReader<?> transfer);

  public static void start(
      Consumer<WorkerContext> onStart,
      JsFunctions.Consumer<JSObject> error,
      JSString url
  ) {
    WorkerContext worker = newWorker(url);
    worker.onMessage(message -> {
      if (WorkerProtocol.isStarted(message.getData())) {
        worker.onMessage((JSObject) null);
        onStart.accept(worker);
      } else {
        error.f(JsHelper.newError("worker is not started"));
      }
    });
  }

  public static void start(Consumer<WorkerContext> onStart, String url) {
    start(onStart, WorkerContext::throwJsError, JSString.valueOf(url));
  }

  static void throwJsError(JSObject jsError) {
    throw new RuntimeException(jsError.<JSError>cast().getMessage());
  }

  public static void workerMain(WorkerExecutor executor) {
    self().onMessage(e -> onWorkerMessage(executor, e.getData()));
    self().postMessage(WorkerProtocol.started());
  }

  static void onWorkerMessage(WorkerExecutor executor, JSObject message) {
    if (WorkerProtocol.isPing(message)) {
//      JsHelper.consoleInfo("Worker: hello");
      self().postMessage(WorkerProtocol.ping());
    } else if (WorkerProtocol.isArray(message)) {
      WorkerProtocol.execute(executor, message.cast());
    } else throw new IllegalArgumentException();
  }

  public static void onEdtMessage(Map<Integer, Consumer<Object[]>> handlers, JSObject message) {
    if (WorkerProtocol.isPing(message)) {
//      JsHelper.consoleInfo("App: hello from worker");
    } else if (WorkerProtocol.isArray(message)) {
//      JsHelper.consoleInfo("App: message from worker = ", message);
      WorkerProtocol.dispatchResult(handlers, message.cast());
    } else throw new IllegalArgumentException();
  }
}
